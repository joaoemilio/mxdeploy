package com.mxscript;

import java.io.FileReader;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.Parser;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.ProxySOCKS5;
import com.mxscript.swt.event.LauncherBeanShellEvent;
import com.mxssh.CannotSudoException;
import com.mxssh.SCPClient;
import com.mxssh.SSHServiceNew;

public abstract class DeploymentSuiteBSTask extends Thread {
	
	private int indexItem = 0;
	private LauncherBeanShellEvent event = null;
	private String serverName;
	private SSHServiceNew sshClient;
	private Interpreter bsh = null;
	private int sessionTimeOut = 0;
	private int connectionTimeOut = 0;
	private ProxySOCKS5 proxySOCKS5=null;
	private String username;
	private String password;
	
	private boolean running = false;
	private boolean isCompleted = false;
	private boolean isStopping=false;

	
	public DeploymentSuiteBSTask() {
		
	}
	
	public DeploymentSuiteBSTask(LauncherBeanShellEvent event, int indexItem, String serverName){
		this.indexItem = indexItem;
		this.event = event;
		this.serverName = serverName;
	}
	
	public void run() {
		try {
			bsh.eval("startLine(\""+serverName+"\","+String.valueOf(indexItem)+")");
		} catch (EvalError e) { 
			e.printStackTrace();
			event.getLogger().error(e.getMessage());
			event.print(e.getMessage()); 
		}
	}	
	
	public void connect(String hostname, String username, String password) throws InterruptedException, MXBeanShellException{
		sshClient = new SSHServiceNew();
		
        if( proxySOCKS5 != null){
        	sshClient.setProxySOCKS5(proxySOCKS5);
        }
        
		if( sessionTimeOut > 0 ){
			sshClient.setSessionTimeOut(sessionTimeOut);
		}
		if( connectionTimeOut > 0 ){
			sshClient.setConnectionTimeOut(connectionTimeOut);
		}
		
		try {
			setColumnStatus("Connecting");
			info("Connecting to server "+hostname);
			 
			sshClient.connect(hostname, username, password );
			
			
			if ( isStopping ) {
				 setColumnStatus("STOPPED");
			} else {
				 setColumnStatus("Running");
			}
		} catch (SocketException e) {
			error("SocketException");
			setColumnStatus("SocketException");
		} catch (UnknownHostException e){
			error("UnknownHost");
			setColumnStatus("UnknownHost");
		} catch (IOException e) {
			error("IOException");
			setColumnStatus("IOException");
		} catch (JSchException e) {
			
			e.printStackTrace();
			error(e.getMessage());
			setColumnStatus(e.getMessage().toUpperCase());
		}		
	}

	public void connectPK(String hostname, String username, String privateKeyPath) throws InterruptedException, MXBeanShellException{
		sshClient = new SSHServiceNew();
		
        if( proxySOCKS5 != null){
        	sshClient.setProxySOCKS5(proxySOCKS5);
        }
        
		if( sessionTimeOut > 0 ){
			sshClient.setSessionTimeOut(sessionTimeOut);
		}
		if( connectionTimeOut > 0 ){
			sshClient.setConnectionTimeOut(connectionTimeOut);
		}
		
		try {
			setColumnStatus("Connecting");
			info("Connecting to server "+hostname);
			
			sshClient.connectPK(hostname, username, privateKeyPath );
			if ( isStopping ) {
				 setColumnStatus("STOPPED");
			} else {
				 setColumnStatus("Running");
			}
		} catch (SocketException e) {
			error("SocketException");
			setColumnStatus("SocketException");
		} catch (UnknownHostException e){
			error("UnknownHost");
			setColumnStatus("UnknownHost");
		} catch (IOException e) {
			error("IOException");
			setColumnStatus("IOException");
		} catch (JSchException e) {
			
			e.printStackTrace();
			error(e.getMessage());
			setColumnStatus(e.getMessage().toUpperCase());
		}		
	}
	
	public void connect() throws InterruptedException, MXBeanShellException{
		connect(serverName, getUsername(), getPassword() );
	}
	
	public void disconnect(){
		if( sshClient!=null && sshClient.isConnected() )
		    sshClient.disconnect();
	}
	
	
	public String executeCommand(String command) throws IOException, InterruptedException, MXBeanShellException{
	   return sshClient.executeCommand(command);
	}
	
	public boolean isConnected(){ 
		if(sshClient==null){
			return false;
		}
		return sshClient.isConnected();
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
	
	public void getFile(String sourceDir, String sourceFile, String destDir) throws MXBeanShellException{
		SSHServiceNew ssh = new SSHServiceNew();
		try {
			ssh.connect(getServerName(), getUsername(), getPassword());
			SCPClient scp = new SCPClient();
			scp.getFile(ssh, sourceDir, sourceFile, destDir);			
		} catch (SocketException e) {
			error(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			error(e.getMessage());
			e.printStackTrace();
		} catch (JSchException e) {
			error(e.getMessage());
			e.printStackTrace();
		} catch (InterruptedException e) {
			error(e.getMessage());
			e.printStackTrace();
		} finally{
			if( ssh!=null && ssh.isConnected() )
			    ssh.disconnect();
		}
	}
	
	public void getFile(String username, String password, String sourceDir, String sourceFile, String destDir) throws MXBeanShellException {
		SSHServiceNew ssh = new SSHServiceNew();
		try {
			ssh.connect(getServerName(), username, password);
			SCPClient scp = new SCPClient();
			scp.getFile(ssh, sourceDir, sourceFile, destDir);			
		} catch (SocketException e) {
			error(e.getMessage());
		} catch (IOException e) {
			error(e.getMessage());
		} catch (JSchException e) {
			error(e.getMessage());
		} catch (InterruptedException e) {
			error(e.getMessage());
		} finally{
			if( ssh!=null && ssh.isConnected() )
			    ssh.disconnect();
		}
	}
	
	public void getFile(String hostname, String username, String password, String sourceDir, String sourceFile, String destDir) throws MXBeanShellException {
		SSHServiceNew ssh = new SSHServiceNew();
		try {
			ssh.connect(hostname, username, password);
			SCPClient scp = new SCPClient();
			scp.getFile(ssh, sourceDir, sourceFile, destDir);			
		} catch (SocketException e) {
			error(e.getMessage());
		} catch (IOException e) {
			error(e.getMessage());
		} catch (JSchException e) {
			error(e.getMessage());
		} catch (InterruptedException e) {
			error(e.getMessage());
		} finally{
			if( ssh!=null && ssh.isConnected() )
			    ssh.disconnect();
		}
	}	
	
	public void uploadFile(String sourceFile, String destFile) throws MXBeanShellException {
		SSHServiceNew ssh = new SSHServiceNew();
		try {
			ssh.connect(getServerName(), getUsername(), getPassword());
			SCPClient scp = new SCPClient();
			scp.sendFile(ssh, sourceFile, destFile);		
		} catch (SocketException e) {
			error(e.getMessage());
		} catch (IOException e) {
			error(e.getMessage());
		} catch (JSchException e) {
			error(e.getMessage());
		} catch (InterruptedException e) {
			error(e.getMessage());
		} finally{
			if( ssh!=null && ssh.isConnected() )
			    ssh.disconnect();
		}		
	}	
	
	public void uploadFile(String username, String password, String sourceFile, String destFile) throws MXBeanShellException {
		SSHServiceNew ssh = new SSHServiceNew();
		try {
			ssh.connect(getServerName(), username, password);
			SCPClient scp = new SCPClient();
			scp.sendFile(ssh, sourceFile, destFile);		
		} catch (SocketException e) {
			error(e.getMessage());
		} catch (IOException e) {
			error(e.getMessage());
		} catch (JSchException e) {
			error(e.getMessage());
		} catch (InterruptedException e) {
			error(e.getMessage());
		} finally{
			if( ssh!=null && ssh.isConnected() )
			    ssh.disconnect();
		}		
	}		
	
	public void uploadFile(String hostname, String username, String password, String sourceFile, String destFile) throws MXBeanShellException {
		SSHServiceNew ssh = new SSHServiceNew();
		try {
			ssh.connect(hostname, username, password);
			SCPClient scp = new SCPClient();
			scp.sendFile(ssh, sourceFile, destFile);		
		} catch (SocketException e) {
			error(e.getMessage());
		} catch (IOException e) {
			error(e.getMessage());
		} catch (JSchException e) {
			error(e.getMessage());
		} catch (InterruptedException e) {
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
	
		
	public ProxySOCKS5 getProxySOCKS5() {
		return proxySOCKS5;
	}

	public void setProxySOCKS5(ProxySOCKS5 proxySOCKS5) {
		this.proxySOCKS5 = proxySOCKS5;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}	
	
	public boolean isRunning() {
		return this.running;
	}
	
	public void stopTask() {
		this.running = false;
		this.isCompleted = true;
		setStopping(true);
		if ( isConnected() ){
		     this.disconnect();
		}
		setColumnStatus("STOPPED");
	}
	
	public void setRunning(boolean running) {
		this.running = running;
	}
	
	public boolean isStopping() {
		return this.isStopping;
	}

	protected void setStopping(boolean isStopping) { 
		this.isStopping = isStopping;
	}
	
	public boolean isComplete() {
		return this.isCompleted;
	}
	
	public void setComplete(boolean isCompleted ){
		this.isCompleted = isCompleted;
	}

	public Interpreter getBsh() {
		return bsh;
	}

	public void setBsh(Interpreter bsh) {
		this.bsh = bsh;
	}

	public int getConnectionTimeOut() {
		return connectionTimeOut;
	}

	public void setConnectionTimeOut(int connectionTimeOut) {
		this.connectionTimeOut = connectionTimeOut;
	}

	public void setEvent(LauncherBeanShellEvent event) {
		this.event = event;
	}

	public SSHServiceNew getSSHClient() {
		return this.sshClient;
	}

//	public void setSshClient(SSHServiceNew sshClient) {
//		this.sshClient = sshClient;
//	}
	
	public int getSessionTimeOut() {
		return sessionTimeOut;
	}

	public void setSessionTimeOut(int sessionTimeOut) {
		this.sessionTimeOut = sessionTimeOut;
	}
	
}
