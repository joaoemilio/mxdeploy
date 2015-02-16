package com.mxscript;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.widgets.Display;

import bsh.EvalError;
import bsh.Interpreter;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.ProxySOCKS5;
import com.mxdeploy.api.domain.Database;
import com.mxdeploy.api.domain.Property;
import com.mxdeploy.swt.MainShell;
import com.mxscript.logger.Logger;
import com.mxscript.swt.event.LauncherBeanShellEvent;
import com.mxssh.SCPClient;
import com.mxssh.SSHClient;
import com.mxssh.exception.CannotSudoException;
import com.mxterminal.swt.util.TerminalDomainRule;
import com.mxterminal.swt.util.TerminalProperty;

public class Task extends DeploymentSuiteBSTask {
	
	private int indexItem = 0;
	private LauncherBeanShellEvent event = null;
	private String serverName;
	private Connection connection = null;
	
	private Logger logger = null;
	private String scriptName;
	private boolean error = false;
	private long totalTime = 0;
	private Boolean isFirst = false;
	private String filePath = null;
	private List<Property> localPropertyList = new ArrayList<Property>();
	private List<Property> globalPropertyList = new ArrayList<Property>();	
	
	public Task(LauncherBeanShellEvent event, String filePath, String scriptName, Logger logger, int indexItem, String serverName){
		super(event, indexItem, serverName);
		this.indexItem = indexItem;
		this.event = event;
		this.serverName = serverName;
		this.scriptName = scriptName;
		this.logger = logger;
		this.filePath = filePath;
	}

	public Interpreter createInterpreter(String scriptName) throws EvalError, FileNotFoundException, IOException {
		logger.debug("creating beanshell interpreter");
		Interpreter bsh = new Interpreter();
		
		logger.debug("setting up beanshell variables: shell, event, logger, map " );
		bsh.set("shell", MainShell.sShell);
		bsh.set("event", this.event);
		bsh.set("logger", logger);
		bsh.set("map", new HashMap());
		bsh.set("task", this);
		
		logger.debug("adding beanshell library");
		for(String commandFile : getCommandList() ){
			logger.debug("adding lib: " + commandFile);
			bsh.source(Database.HOME+"/lib/commands/"+commandFile); 
		}
		
		logger.debug("loading beanshell: " + filePath);
		bsh.source(filePath);
		
		return bsh;
		
	}
	
	public static List<String> getCommandList(){
		
		List<String> commandNameList = new ArrayList<String>();
		
		File file = new File(Database.HOME+"/lib/commands/");
		File[] files = file.listFiles();
		for(int i=0; i < files.length; i++){
			if(!files[i].isDirectory()){
				if(files[i].getName().endsWith(".bsh")){
					commandNameList.add(files[i].getName());
				}
			}
		}
		
		return commandNameList;
	}	
	
	
	public void run() {
		try {
			if ( isStopping() ){
				setColumnStatus("STOPPED");
				return;
			}
			this.totalTime = System.currentTimeMillis();
			setRunning(true);

			try {
				logger.debug("creating interpreter for script: " + scriptName + " server: " + serverName);
				this.setBsh( createInterpreter(scriptName) );
				logger.debug("interpreter created");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				this.error = true;
			} catch (EvalError e) {
				e.printStackTrace();
				this.error = true;
			} catch (IOException e) {
				e.printStackTrace();
				this.error = true;
			}
			
			event.setColumnValue(indexItem, 1, "Running");
			logger.debug("running start method - server: " + serverName );
			this.getBsh().eval("start()"); 
		} catch (EvalError e) {
			this.error = true;
			if(!this.isStopping()){
				this.setStopping(true);
				e.printStackTrace(); 
				String error = "=========================================================================================="
					          +"\n File Path : "+e.getErrorSourceFile()
					          +"\n Line [ "+e.getErrorLineNumber()+" ] " + e.getErrorText()
					          +"\n "+e.getScriptStackTrace()
					          +"\n "+e.getMessage()
					          +"\n "+e.getLocalizedMessage()
					          +"\n "+e.fillInStackTrace()
					          +"\n==========================================================================================";
				event.getLogger().error(error);
				Display.getDefault().syncExec(new Runnable(){
					public void run(){ 
						setColumnStatus("FAIL");	
					} 
				});				
			}
		} catch ( Throwable t){
			this.error = true;
			if(!this.isStopping()){
				this.setStopping(true);
				t.printStackTrace();
				event.getLogger().error(t.getMessage());
				event.print(t.getMessage());
			}
			interrupt();
		} finally {
			if(this.isConnected()) {
			   this.disconnect();
			   logger.debug("SSH has been disconnected");
			}
			this.setBsh( null );
			//this.setSshClient( null );
			this.setComplete(true);
			this.setRunning(false);
			this.totalTime = System.currentTimeMillis() - this.totalTime;
			if(this.error) {
				logger.info("BeanShell has completed to run with error in " + this.totalTime + " milis" );
			} else {
				logger.info("BeanShell has completed to run succesfully in " + this.totalTime + " milis" );
			}
		}
	}	
	
	public long getTotalTime() {
		return this.totalTime;
	}
	
	public String executeCommand(String command) throws IOException, InterruptedException, MXBeanShellException{
	   if( this.isStopping() || !this.getSSHClient().isConnected() ){
			//return "Wait - Stopping BeanShell";
		   throw new InterruptedException();
	   }
	   
	   if(getConnection()!=null){
		   return getConnection().getSshServiceNew().executeCommand(command);
	   }
	   
	   return this.getSSHClient().executeCommand(command);
	}
	
	public void printWrite(String command){
		this.getSSHClient().printWrite(command);
	}
	
	public String channelExec(String command) throws IOException, InterruptedException, JSchException{
		   if( this.isStopping() || !this.getSSHClient().isConnected() ){
				//return "Wait - Stopping BeanShell";
			   throw new InterruptedException();
		   }
		   
		   return this.getSSHClient().channelExec(command);
	}	
	
	public String stopCommand() throws IOException, InterruptedException, MXBeanShellException{
		String stopCommand = new String(new char[] {'\003'});
		if(getConnection()!=null){
		   return getConnection().getSshServiceNew().executeCommand(stopCommand);
		}
		   
		return this.getSSHClient().executeCommand(stopCommand);		
		
	}
	
	public String executeRemoteShellScript(String command) throws IOException, InterruptedException{
		   if( isStopping() || !isConnected() ){
			   throw new InterruptedException();
		   }
	   
		   return this.getSSHClient().printWriteln(command); 
	}	
	
	public boolean isConnected(){ 
		if(this.getSSHClient()==null && getConnection()==null){
			return false;
		}
		
	    if(getConnection()!=null){
		   return getConnection().getSshServiceNew().isConnected();
	    }		
	    
		return this.getSSHClient().isConnected();
	}

	public LauncherBeanShellEvent getEvent() {
		return event;
	}

	public int getIndexItem() {
		return indexItem;
	}

	public String getServerName() {
		return serverName;
	}
	
	public void setColumnValue(int indexColumn, String value){
		event.setColumnValue(indexItem, indexColumn, value);
	}

	public void setColumnStatus(String value){
		event.setColumnValue(indexItem, 1, value);
	}
	
	public String getColumnValue(int indexColumn){
		return event.getColumnValue(indexItem, indexColumn);
	}
	
	public void log(String value){
		event.getLogger().log("["+serverName+"] - "+value);
	}
	
	public void debug(String value){
		event.getLogger().debug("["+serverName+"] - "+value);
	}
	
	public void warn(String value){
		event.getLogger().warn("["+serverName+"] - "+value);
	}	
	
	public void info(String value){
		event.getLogger().info("["+serverName+"] - "+value);
	}	
	
	public void error(String value){
		event.getLogger().error("["+serverName+"] - "+value);
	}
	
	public void getFile(String sourceDir, String sourceFile, String destDir) throws MXBeanShellException {
		SSHClient ssh =  getSSHClient();
		boolean isNew = false;
		try {
			if( ssh == null ){
				ssh = new SSHClient();
				isNew = true;
				ssh.connect(getServerName(), getUsername(), getPassword());
			}
			SCPClient scp = new SCPClient();
			scp.getFile(ssh, sourceDir, sourceFile, destDir);			
		} catch (SocketException e) {
			this.error = true;
			error(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			this.error = true;
			error(e.getMessage());
			e.printStackTrace();
		} catch (JSchException e) {
			this.error = true;
			error(e.getMessage());
			e.printStackTrace();
		} catch (InterruptedException e) {
			this.error = true;
			error(e.getMessage());
			e.printStackTrace();
		} finally{
			if( isNew && ssh!=null && ssh.isConnected() )
			    ssh.disconnect();
		}
	}
	
	public void getFile(String username, String password, String sourceDir, String sourceFile, String destDir) throws MXBeanShellException {
		SSHClient ssh = new SSHClient();
		try {
			ssh.connect(getServerName(), username, password);
			SCPClient scp = new SCPClient();
			scp.getFile(ssh, sourceDir, sourceFile, destDir);			
		} catch (SocketException e) {
			this.error = true;
			error(e.getMessage());
		} catch (IOException e) {
			this.error = true;
			error(e.getMessage());
		} catch (JSchException e) {
			this.error = true;
			error(e.getMessage());
		} catch (InterruptedException e) {
			this.error = true;
			error(e.getMessage());
		} finally{
			if( ssh!=null && ssh.isConnected() )
			    ssh.disconnect();
		}
	}
	
	public void getFile(String hostname, String username, String password, String sourceDir, String sourceFile, String destDir) throws MXBeanShellException {
		SSHClient ssh = new SSHClient();
		try {
			ssh.connect(hostname, username, password);
			SCPClient scp = new SCPClient();
			scp.getFile(ssh, sourceDir, sourceFile, destDir);			
		} catch (SocketException e) {
			this.error = true;
			error(e.getMessage());
		} catch (IOException e) {
			this.error = true;
			error(e.getMessage());
		} catch (JSchException e) {
			this.error = true;
			error(e.getMessage());
		} catch (InterruptedException e) {
			this.error = true;
			error(e.getMessage());
		} finally{
			if( ssh!=null && ssh.isConnected() )
			    ssh.disconnect();
		}
	}	
	
	public void uploadFile(String sourceFile, String destFile) throws MXBeanShellException {
		SSHClient ssh =  getSSHClient();
		boolean isNew = false;
		try {
			if( ssh == null ){
				ssh = new SSHClient();
				isNew = true;
				ssh.connect(getServerName(), getUsername(), getPassword());
			}			
			
			SCPClient scp = new SCPClient();
			scp.sendFile(ssh, sourceFile, destFile);		
		} catch (SocketException e) {
			this.error = true;
			error(e.getMessage());
		} catch (IOException e) {
			this.error = true;
			error(e.getMessage());
		} catch (JSchException e) {
			this.error = true;
			error(e.getMessage());
		} catch (InterruptedException e) {
			this.error = true;
			error(e.getMessage());
		} finally{
			if( isNew && ssh!=null && ssh.isConnected() )
			    ssh.disconnect();
		}		
	}	
	
	public void uploadFile(String username, String password, String sourceFile, String destFile) throws MXBeanShellException {
		SSHClient ssh = new SSHClient();
		try {
			ssh.connect(getServerName(), username, password);
			SCPClient scp = new SCPClient();
			scp.sendFile(ssh, sourceFile, destFile);		
		} catch (SocketException e) {
			this.error = true;
			error(e.getMessage());
		} catch (IOException e) {
			this.error = true;
			error(e.getMessage());
		} catch (JSchException e) {
			this.error = true;
			error(e.getMessage());
		} catch (InterruptedException e) {
			this.error = true;
			error(e.getMessage());
		} finally{
			if( ssh!=null && ssh.isConnected() )
			    ssh.disconnect();
		}		
	}		
	
	public void uploadFile(String hostname, String username, String password, String sourceFile, String destFile) throws MXBeanShellException {
		SSHClient ssh = new SSHClient();
		try {
			ssh.connect(hostname, username, password);
			SCPClient scp = new SCPClient();
			scp.sendFile(ssh, sourceFile, destFile);		
		} catch (SocketException e) {
			this.error = true;
			error(e.getMessage());
		} catch (IOException e) {
			this.error = true;
			error(e.getMessage());
		} catch (JSchException e) {
			this.error = true;
			error(e.getMessage());
		} catch (InterruptedException e) {
			this.error = true;
			error(e.getMessage());
		} finally{
			if( ssh!=null && ssh.isConnected() )
			    ssh.disconnect();
		}		
	}
	
	public void sudoSUToUser(String userID) throws CannotSudoException , IOException, InterruptedException, MXBeanShellException {
		String cmd = null;
		cmd = "sudo su - " + userID + "\n";
    	executeCommand( cmd );
		executeCommand("export PS1='MXTERMINAL>'" );
		executeCommand("set -o vi" );
		executeCommand("umask 002" );
	}
	
//	public SSHServiceNew getSSHClient() {
//	    if(getConnection()!=null){
//		   return getConnection().getSshServiceNew();
//	    }		
//		return this.sshClient;
//	}
	

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) throws InterruptedException, MXBeanShellException {
		this.connection = connection;
		connect();
	}
		
	public boolean hasError() {
		return this.error;
	}


	public Boolean isFirst() {
		return isFirst;
	}

	public void setFirst(Boolean isFirst) {
		this.isFirst = isFirst;
	}
	
	public void connect(String hostname) throws UnknownHostException, SocketException, IOException, JSchException, InterruptedException, MXBeanShellException {
		connect(hostname, getUsername(), getPassword());
	}

	@SuppressWarnings("unused")	
	private String getDomainRule(ProxySOCKS5 proxy) {
		try {
			String serverAddrStr = getServerName();
			debug("MXTerminal is trying first to connect, directly using "+serverAddrStr);
			proxy.connect(null, serverAddrStr, 22, 0);
		    proxy.getSocket().close();
			return serverAddrStr;
		} catch (JSchException e) {
			if (TerminalProperty.getTerminalDomainRuleList() != null && TerminalProperty.getTerminalDomainRuleList().size() > 0) {
				return connectDomainRuleGateway(proxy);
			} else {
				e.printStackTrace();
				error("JSchException"+e.getMessage());
			}
		} catch (IOException e) {
			if (TerminalProperty.getTerminalDomainRuleList() != null && TerminalProperty.getTerminalDomainRuleList().size() > 0) {
				return connectDomainRuleGateway(proxy);
			} else {
				e.printStackTrace();
				error("JSchException"+e.getMessage());
			}
		}
		return null;
	}
	
	@SuppressWarnings("unused")
	private String connectDomainRuleGateway(ProxySOCKS5 proxy) {
		debug("Trying first, to connect directly without the DOMINIO");

	    String regexIP = "\\d?\\d?\\d[.]\\d?\\d?\\d[.]\\d?\\d?\\d[.]\\d?\\d?\\d";  
	    Pattern patternIP = Pattern.compile( regexIP );  
	    Matcher matcherIP = patternIP.matcher( getServerName() );         
	    
	    if( getServerName().matches( regexIP ) && matcherIP.matches() ) {
	        return null; 
	    }

		for(TerminalDomainRule tdr : TerminalProperty.getTerminalDomainRuleList()) {
			StringTokenizer token = new StringTokenizer(getServerName(), ".");
			String serverName = token.nextToken();
			String regex = tdr.getRegexp();
			Pattern pattern = Pattern.compile(regex);

			Matcher matcher = pattern.matcher(serverName);

			if (serverName.matches(regex)) {
				if (matcher.matches()) {
                    
					String domainName = tdr.getDomainName();
					if( domainName.startsWith(".")) {
						domainName = domainName.substring(1);
					}
					serverName = serverName + "." + domainName;
					debug("DOMINIO="+serverName);
					/*
					 * If the gateway TIMEOUT was expired, we must disconnect
					 * and connect again without TIMEOUT
					 */
					try {
						proxy.connect(null, serverName, 22, 8000);
						proxy.getSocket().close();						
						return serverName;
					} catch (IOException e) {
						e.printStackTrace();
 						error("ConnectException"+e.getMessage());
					} catch (JSchException e) {
						e.printStackTrace();
						error("ConnectException"+e.getMessage());
					}
				}
			}
		}
		return null;
	}

	public List<Property> getLocalPropertyList() {
		return localPropertyList;
	}

	public void setLocalPropertyList(List<Property> localPropertyList) {
		this.localPropertyList = localPropertyList;
	}

	public List<Property> getGlobalPropertyList() {
		return globalPropertyList;
	}

	public void setGlobalPropertyList(List<Property> globalPropertyList) {
		this.globalPropertyList = globalPropertyList;
	}
	
	public String getLocalProperty(String name){
		for ( Property property : localPropertyList ){
			  if( property.getName().equals(name) ){
				  return property.getValue();
			  }
		}
		return null;
	}
	
	public String getGlobalProperty(String name){
		for ( Property property : globalPropertyList ){
			  if( property.getName().equals(name) ){
				  return property.getValue();
			  }
		}
		return null;
	}	
	
}
