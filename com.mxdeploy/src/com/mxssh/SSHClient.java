package com.mxssh;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.ProxySOCKS5;
import com.jcraft.jsch.Session;
import com.mxdeploy.AccountConfig;
import com.mxdeploy.api.domain.Database;
import com.mxdeploy.api.domain.Server;
import com.mxscript.MXBSListener;
import com.mxscript.MXBeanShellException;
import com.mxssh.exception.CannotSudoException;

public class SSHClient {
	public static final String VERSION = "1.0.0.1"; 
	
	//static Logger logger = Logger.getLogger(ChannelFacade.class);
	protected Logger logger = null;
	protected Logger loggerSSH = null;
	protected JSch jsch = new JSch();
    protected Session session;
    protected ChannelShell channel;
    protected String username;
    protected String password;
    private Integer STATUS = 0;  
	
	protected PipedOutputStream commandIO = null;
	protected InputStream sessionInput = null;
	protected InputStream sessionOutput = null;
	protected InputStream sessionError = null;
	protected boolean interactive;
	protected OutputThread thread = null;
	protected int promptLines = 0;
	protected SocksService socksService = new SocksService();
	
	protected int sessionTimeOut = 300000;
	protected int connectionTimeOut = 0;
	
	private ProxySOCKS5 proxySOCKS5=null;
	
	public SSHClient() {
		
	}
	
	public SSHClient(MXBSListener listener) {
		
	}
	
	public Session getSshSession(){
		return session;
		
	}
	
	public Logger getLogger() {
		return this.logger;
	}
	
	
	public int getSessionTimeOut() {
		return sessionTimeOut;
	}

	public void setSessionTimeOut(int sessionTimeOut) {
		this.sessionTimeOut = sessionTimeOut;
	}

	public int getConnectionTimeOut() {
		return connectionTimeOut;
	}

	public void setConnectionTimeOut(int connectionTimeOut) {
		this.connectionTimeOut = connectionTimeOut;
	}

	
	/**
	 * Create loggers to be used in this class
	 * logger is to manage debug messages 
	 * loggerSSH is to manage SSH interaction command/output
	 * @param hostname
	 */
	protected void createLoggers(String hostname) {
		logger = Logger.getLogger("SSHServiceNew_Debug_" + hostname);
		logger.removeAllAppenders();
		logger.addAppender(new ConsoleAppender());
		loggerSSH = Logger.getLogger("SSHServiceNew_SSH_" + hostname);
		loggerSSH.removeAllAppenders();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		String date = sdf.format( Calendar.getInstance().getTime() );
		String logdir = Database.HOME + "/logs/" + hostname + "/" + date + "/";
		try {
			org.apache.commons.io.FileUtils.forceMkdir(new File(logdir));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		sdf = new SimpleDateFormat("yyyyMMdd_hhmmss");
		String datetime = sdf.format(Calendar.getInstance().getTime());
		String debuglogfile = logdir + "debug_" + datetime + ".log";
		String sshlogfile = logdir + "ssh_" + datetime + ".log";
		
		FileAppender fileappender = null;
		FileAppender fileappender2 = null;
		try {
			fileappender = new FileAppender(new PatternLayout( "[%d{dd MMM yyyy HH:mm:ss,SSS}] %n %m %n %n" ), debuglogfile);
			fileappender2 = new FileAppender(new PatternLayout("[%d{dd MMM yyyy HH:mm:ss,SSS}] %n %m %n %n"), sshlogfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.addAppender(fileappender);
		logger.setLevel(Level.DEBUG);
		loggerSSH.addAppender(fileappender2);
		loggerSSH.setLevel(Level.DEBUG);
	}

	public boolean connect(String hostname, String username, String password) throws UnknownHostException, SocketException, IOException, JSchException, InterruptedException, MXBeanShellException {
		STATUS =1;
		boolean isConnected = false;
		this.interactive = false;
		
		createLoggers(hostname);

		socksService.setupSocksByHostname(hostname);
		
        if( AccountConfig.getInstance()!=null && AccountConfig.getInstance().isConnectViaIP() ){
      	   for(Server server : Database.getInstance().getServerList()){
      		   if(server.getHostname().contains(hostname)){
      			   if(server.getAddress()!=null && server.getAddress().trim().length()>0){
      				  hostname = server.getAddress();
      				  break;
      			   }
      		   }
      	   } 
         }
         
		logger.debug("opening session channel");
		
   		isConnected = openSessionChannel(hostname, username, password);
   		
   		logger.debug("isConnected: " + isConnected);
   		if(isConnected){
   		   STATUS=2;
   		}
   		
    	String msg = flush2();
    	loggerSSH.info(msg);
    	logger.debug("SSHServiceNew VERSION: " + VERSION );

		thread = new OutputThread();
		thread.setPassword(password);
        thread.start();

		executeCommand("export PS1='MXTERMINAL>'" );

		Thread.sleep(100);
		flush2();
		
		executeCommand("unalias ls");
		Thread.sleep(100);
		flush2();
		
		executeCommand("umask 022");
		Thread.sleep(100);
		flush2();
		
		executeCommand("set -o vi" );
		Thread.sleep(100);
		flush2();
		
		return isConnected;
	}
	
	public boolean connectPK(String hostname, String username, String privateKeyPath) throws UnknownHostException, SocketException, IOException, JSchException, InterruptedException, MXBeanShellException {
		STATUS =1;
		boolean isConnected = false;
		this.interactive = false;
		
		createLoggers(hostname);

		socksService.setupSocksByHostname(hostname);
		
        if( AccountConfig.getInstance()!=null && AccountConfig.getInstance().isConnectViaIP() ){
      	   for(Server server : Database.getInstance().getServerList()){
      		   if(server.getHostname().contains(hostname)){
      			   if(server.getAddress()!=null && server.getAddress().trim().length()>0){
      				  hostname = server.getAddress();
      				  break;
      			   }
      		   }
      	   } 
         }
         
		logger.debug("opening session channel");
		
   		isConnected = openSessionChannelPK(hostname, username, privateKeyPath);
   		
   		logger.debug("isConnected: " + isConnected);
   		if(isConnected){
   		   STATUS=2;
   		}
   		
    	String msg = flush2();
    	loggerSSH.info(msg);
    	logger.debug("SSHServiceNew VERSION: " + VERSION );

		thread = new OutputThread();
		thread.setPassword(password);
        thread.start();

		executeCommand("export PS1='MXTERMINAL>'" );

		Thread.sleep(100);
		flush2();
		
		executeCommand("unalias ls");
		Thread.sleep(100);
		flush2();
		
		executeCommand("umask 022");
		Thread.sleep(100);
		flush2();
		
		executeCommand("set -o vi" );
		Thread.sleep(100);
		flush2();
		
		return isConnected;
	}
	
	public void disconnect(){
		try{
		  logger.debug("DISCONNECTING !!!");
		  if ( thread!= null ){
		       this.thread.kill();
		  }
		  if(channel!=null && channel.isConnected()){
			 channel.disconnect();
		  }
		  if( session !=null ){
			  this.session.setTimeout(1);
			  this.session.disconnect();
		  }
		  logger.removeAllAppenders();
		  loggerSSH.removeAllAppenders();
		  
		} catch (JSchException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
		 STATUS=3;
		}
	}
	
	public String flush2() throws IOException {
	    byte[] tmp = new byte[1024];
	    String stdOut = "";
	    String stdErr = "";
	
    	int i;
		while(sessionOutput.available() > 0 || sessionError.available() >  0){
            if (sessionError.available() >  0) {
                i = sessionError.read(tmp, 0, tmp.length);
                if (i < 0) {
                    System.err.println("input stream closed earlier than expected");
                    System.exit(1);
                }
                stdErr += new String(tmp, 0, i);
            }

            if (sessionOutput.available() > 0) {
                i = sessionOutput.read(tmp, 0, tmp.length);
                if (i < 0) {
                    System.err.println("input stream closed earlier than expected");
                    System.exit(1);
                }
                stdOut += new String(tmp, 0, i);
            }
		}
		logger.debug("FLUSHING BEGIN->\n" + stdOut + "\nFLUSHING END");
		logger.error(stdErr);
		return stdOut;
	}
	
	public boolean openSessionChannel(String hostname, String username,	String password) throws UnknownHostException, IOException, JSchException, InterruptedException {
		boolean result = true;
		this.password = password;
		this.username = username;
		
		int idx = hostname.indexOf(':');
		@SuppressWarnings("unused")
		int port = 22;
		if (idx > -1) {
			port = Integer.parseInt(hostname.substring(idx + 1));
			hostname = hostname.substring(0, idx);
		}
        
		this.session = jsch.getSession(username, hostname, 22);
		this.session.setTimeout(sessionTimeOut);
        this.session.setConfig("StrictHostKeyChecking", "no");

        if( proxySOCKS5 != null){
        	session.setProxy(proxySOCKS5);
        }
        
        this.session.setPassword(password);
        
        if( connectionTimeOut > 0){
        	this.session.connect(connectionTimeOut);
        } else {
        	this.session.connect();
        }

        this.channel = (ChannelShell)session.openChannel("shell");
		int tcol=1023;
		int trow=24;
		int twp=640;
		int thp=480; 
		this.channel.setPtySize(tcol, trow, twp, thp);
		this.channel.setPty(true);
        this.commandIO = new PipedOutputStream();
        this.sessionInput = new PipedInputStream(this.commandIO);
        // this set's the InputStream the remote server will read from.
        this.channel.setInputStream(sessionInput);
//	        ChannelShell cs = (ChannelShell)channel;
//	        cs.setPty(true);

        // this will have the STDOUT from server.
        this.sessionOutput = channel.getInputStream();

        // this will have the STDERR from server
        this.sessionError = channel.getExtInputStream();
        this.channel.connect();

		if (username == null || username.trim().equals(""))
			username = System.getProperty("user.name");

		return result;
	}
	
	public boolean openSessionChannelPK(String hostname, String username, String privateKeyPath) throws UnknownHostException, IOException, JSchException, InterruptedException {
		boolean result = true;
		this.username = username;
		
		int idx = hostname.indexOf(':');
		@SuppressWarnings("unused")
		int port = 22;
		if (idx > -1) {
			port = Integer.parseInt(hostname.substring(idx + 1));
			hostname = hostname.substring(0, idx);
		}
        jsch.addIdentity(privateKeyPath);
		this.session = jsch.getSession(username, hostname, 22);
		this.session.setTimeout(sessionTimeOut);
        this.session.setConfig("StrictHostKeyChecking", "no");
        
        if( proxySOCKS5 != null){
        	session.setProxy(proxySOCKS5);
        }

        //this.session.setPassword(password);
        
        if( connectionTimeOut > 0){
        	this.session.connect(connectionTimeOut);
        } else {
        	this.session.connect();
        }

        this.channel = (ChannelShell)session.openChannel("shell");
		int tcol=1023;
		int trow=24;
		int twp=640;
		int thp=480; 
		this.channel.setPtySize(tcol, trow, twp, thp);
		this.channel.setPty(true);
        this.commandIO = new PipedOutputStream();
        this.sessionInput = new PipedInputStream(this.commandIO);
        // this set's the InputStream the remote server will read from.
        this.channel.setInputStream(sessionInput);
//	        ChannelShell cs = (ChannelShell)channel;
//	        cs.setPty(true);

        // this will have the STDOUT from server.
        this.sessionOutput = channel.getInputStream();

        // this will have the STDERR from server
        this.sessionError = channel.getExtInputStream();
        this.channel.connect();

		if (username == null || username.trim().equals(""))
			username = System.getProperty("user.name");

		return result;
	}
	
	public String executeCommand(String command) throws IOException, InterruptedException, MXBeanShellException {
	    
        String msg = executeCommand(command, "MXTERMINAL>");
        String[] lines = msg.split("\r\n");
        String result = "";
        for(int i = 1; i < lines.length-1; i++) {
        	String m = lines[i];
        	result += m;
        	if( (i+1) != (lines.length-1) ){
        		result += "\r\n";
        	}
        }
    	
    	return result;
	}
	
	public String executeCommandConsole(String command) throws IOException, InterruptedException, MXBeanShellException {
	    
        String msg = executeCommand(command, "MXTERMINAL>");
    	
    	return msg;
	}
	
	public boolean isID(String id) throws IOException, InterruptedException, MXBeanShellException {
		boolean valid = true;
		String msg = executeCommand("finger " + id);
		if(msg != null && (msg.contains("ser not found") || msg.contains("o such user")) ) {
			valid = false;
		}
		return valid;
	}

	public String sudoSUToUser(String userID) throws CannotSudoException , IOException, InterruptedException, MXBeanShellException {

		if(!isID(userID)) {
			throw new CannotSudoException("User " + userID + " not found");
		}
		
		String msg = executeCommand("sudo su " + userID);
		boolean gotit = true;
		if(msg != null && msg.contains("Sorry") ) {
			msg = executeCommand("sudo su - " + userID);
			if(msg != null && msg.contains("Sorry")) {
				gotit = false;
			}			
		}
		
		if(gotit) {
			executeCommand("umask 022");
			flush2();
			executeCommand("export PS1='MXTERMINAL>'" );
			flush2();
			executeCommand("set -o vi" );
			flush2();			
		}else {
			throw new CannotSudoException(msg);
		}
		
		return msg;
	}
	
	public String executeCommand(String command, String expect) throws IOException, InterruptedException, MXBeanShellException {
//        while( !isConnected() ){
//        	Thread.sleep(300);
//        }
		String msg = ""; 
		command = command + "\n";
    	commandIO.write(command.getBytes());
        commandIO.flush();
    	thread.read();
        while( thread.isRunning() ){
        	Thread.sleep(30);
        	if(this.thread!=null && !this.thread.live){
        		thread.running=false;
        		throw new MXBeanShellException("Thread Stopped");
        	}
        }
         
    	msg = thread.getOutput();
		//loggerSSH.info("COMMAND: \n-----------------\n" + command);
		loggerSSH.debug("execute command response: " + msg);
		flush2();
    	return msg;
	}
	
	public String printWriteln(String command) throws IOException, InterruptedException {
        while( !isConnected() ){
        	Thread.sleep(30); 
        }
		String msg = "";
		command = command + "\n";
		PrintWriter pWriter = new PrintWriter(commandIO, true);
		pWriter.println(command);

		thread.read();
        while( thread.isRunning() ){
        	Thread.sleep(30);
        }
        
    	msg = thread.getOutput();

    	return msg;
	}	
	
	public void printWrite(String command){
		PrintWriter pWriter = new PrintWriter(commandIO, true);
		pWriter.println(command);
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public String getLastOutput() {
		return thread.getLastOutput();
	}

	private class OutputThread extends Thread {
		private String output;
		
		private boolean live = true;
		private boolean running = true;
		private String password = null;

		private String lastOutput;
		
		public String getLastOutput() {
			return this.lastOutput;
		}
		
		public void setPassword(String password) {
			this.password = password;
		}
		
		public void kill(){
			live = false;
		}
		
		public boolean isRunning() {
			return running;
		}
		
		public synchronized void read() {
			output = "";
			running = true;
		}
		
		public String getOutput() {
			return this.output;
		}
		
		public void run() {
			try {			
				readBuffer("MXTERMINAL>");
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	
		public void readBuffer(String expect) throws IOException, InterruptedException {
			byte[] tmp = new byte[2048];
			String stdOut = "";
		    String stdErr = "";
		
	    	int i;
	    	@SuppressWarnings("unused")
			boolean sendpass = true;
	    	
			while(true){
				if(!live){
					break;
				}
				 
	            if (sessionError.available() >  0) {
	                i = sessionError.read(tmp, 0, tmp.length);
	                if (i < 0) {
	                    System.err.println("input stream closed earlier than expected");
	                    System.exit(1);
	                }
	                stdErr += new String(tmp, 0, i);
	            }
	
	            if (sessionOutput.available() > 0) {
	                i = sessionOutput.read(tmp, 0, tmp.length);
	                if (i < 0) {
	                    System.err.println("input stream closed earlier than expected");
	                    System.exit(1);
	                }
	                //stdOut += new String(tmp, 0, i);
	                for(int j=0;j<i;j++){
	                	if( tmp[j] == 27){
	                		continue;
	                	} else if( j>0 && ( tmp[j-1] == 27 && tmp[j] == 91) ){
	                		j = j + 3;
	                	} else {
	                		stdOut+= new String(tmp, j, 1);
	                	}
	                }
	                if(!interactive) {
	                	logger.debug("stdOut :"+stdOut);
	                }
	                this.setLastOutput(stdOut);
	            }
	            
	    		if(stdOut.contains("'$")){
	    			stdOut = stdOut.replace("'$", "");
	    			logger.debug("globalVar Replaced ="+stdOut);
	    		}		 
	    		
	    		
	    		String test = null;
	    		if(stdOut.trim().length()>4){
	    			test = stdOut.trim().substring(stdOut.trim().length()-3).trim();
	    		} else {
	    			test = stdOut.trim();
	    		} 	            
	        
	            if( test.trim().endsWith(expect) || test.trim().endsWith("#") || test.trim().endsWith("$") 
	             || test.trim().endsWith(">") || ( !stdOut.contains("if [") && test.trim().endsWith("]")) ) {
	            	this.output = stdOut;
	    			output = output.replaceAll("\b", ""); 
	    			stdOut = "";
	    			logger.debug("========STDOUT\n" + output + "\n===========");
	    			logger.debug("SETTING RUNNING = FALSE => if expect");
	            	this.running = false;
	            }

	    		if ( stdOut.contains("connecting (yes/no)") || stdErr.contains("connecting (yes/no)") ){
	            	sendpass = false; 
	    			String cmd = "yes \n";
	    			stdOut = "";
	    			commandIO.write(cmd.getBytes());
	    			commandIO.flush();
	    			Thread.sleep(30);
	    		}   

	    		/* N�o sei pq isso est� aqui, e estava dando problema. Estou comentando at� que precise habilitar 
	    		 * cuidado com 
	    		if ( stdOut.contains("ermission denied") || stdErr.contains("ermission denied") ){
	            	this.output = stdOut;
	    			output = output.replaceAll("\b", "");
	    			stdOut = "";
	    			logger.debug("========STDOUT\n" + output + "\n===========");
	    			logger.debug("SETTING RUNNING = FALSE => if permission denied");
	            	this.running = false;
	    		}
	    		*/
	    		
	            if ( !interactive && (stdOut.contains("assword:") || stdErr.contains("assword:") ) ) {
	    			//String cmd = this.password + "\n";
	    			String cmd = this.password + "\n";
	    			stdOut = "";
	    			commandIO.write(cmd.getBytes());
	    			commandIO.flush();
	    			Thread.sleep(30); 
	            }
	            
	            if(interactive && ( (stdOut.contains("(current) UNIX password")) || (stdOut.contains("New UNIX password")) || (stdOut.contains("Retype new UNIX password")) || (stdOut.contains("Old password") || stdOut.contains("ew password") || stdOut.contains("Enter the new password again")) ) ) {
	            	this.output = stdOut;
	    			output = output.replaceAll("\b", "");
	    			stdOut = "";
	    			logger.debug("========STDOUT\n" + output + "\n===========");
	    			logger.debug("SETTING RUNNING = FALSE => change password");
	            	this.running = false;
	            }
	            Thread.sleep(300);
			}
	 
		}

		private void setLastOutput(String stdOut) {
			this.lastOutput += stdOut;
			if(this.lastOutput.length() > 5000) {
				this.lastOutput = this.lastOutput.substring(this.lastOutput.length()-5000);
			}
		}
	}
	
//	public static void main(String args[]){
//		//testAmex();
//		testIGA();
//	}
	
//	private static void testAmex() {
//		SSHServiceNew ssh = new SSHServiceNew();
//		try {
//			ssh.connect("167.210.245.161", "jsilv1", "");
//			
//			String msg = ssh.executeCommand("ssh SYDSCTECCB01");
//			if(msg.contains("denied")) {
//				System.err.println(msg);
//			} else {
//				ssh.executeCommand("uname -a");
//				ssh.executeCommand("exit");
//			}
//		} catch (SocketException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (JSchException e) {
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		} finally {
//			ssh.disconnect();
//		}
//	}
	
//	private static void testIGA() {
//		SSHServiceNew ssh = new SSHServiceNew();
//		try {
//			ssh.connect("localhost", "fsbsilva", "");
//			String x = ssh.sudoSUToUser("xxxx");
//			x = ssh.executeCommand("id");
//		} catch (SocketException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (JSchException e) {
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			ssh.disconnect();
//		}
//
//		
//		try {
//			ssh = new SSHServiceNew();
//			ssh.connect("localhost", "jemilio", "123");
//			ssh.sudoSUToUser("xxxx");
//			ssh.executeCommand("id");
//		
//		} catch (SocketException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (JSchException e) {
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			ssh.disconnect();
//		}
//
//	}

	public boolean isConnected() {
		if(getSshSession() == null) return false;
		return getSshSession().isConnected();
		//return (STATUS==2) ? true : false;
	}
	
	public File getFile(String sourceDir, String sourceFile) {
		SCPClient scp = new SCPClient();
		return scp.getFile(this, sourceDir, sourceFile);			
	}
	
	public boolean isConnecting(){
		synchronized (STATUS) {
			if(STATUS==1){
			   return true;
			}
			return false;
		}
	}

	public ProxySOCKS5 getProxySOCKS5() {
		return proxySOCKS5;
	}

	public void setProxySOCKS5(ProxySOCKS5 proxySOCKS5) {
		this.proxySOCKS5 = proxySOCKS5;
	}

	public String channelExec(String command) throws IOException, InterruptedException, JSchException {
		ChannelExec channelExec = null;
		try{
			if ( !isConnected() ){
				 return "";
			}
			
			channelExec = (ChannelExec)session.openChannel("exec");
		
			BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(channelExec.getInputStream()));
			StringBuilder localStringBuilder = new StringBuilder();
			channelExec.setCommand(command);
			
			channelExec.connect();
			
		    for (String str = localBufferedReader.readLine(); str != null; str = localBufferedReader.readLine())
		    {
		      if(localStringBuilder.length()==0){
		    	 localStringBuilder.append(str);
		      } else {
		    	 localStringBuilder.append("\n");
		    	 localStringBuilder.append(str);
		      }
		    }
		    loggerSSH.debug("["+(new Date())+"] - ChannelExec Command  :\n"+command);
		    loggerSSH.debug("["+(new Date())+"] - ChannelExec Response :\n"+ localStringBuilder.toString());

		    return localStringBuilder.toString();
		} finally {
			  if ( channelExec.isConnected() ){
	   		       channelExec.disconnect();
			  	   channelExec = null;
			  }			
		}
	}	
}

