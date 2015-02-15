package com.mxssh;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserAuth;
import com.mxdeploy.AccountConfig;
import com.mxdeploy.SocksConfig;
import com.mxdeploy.api.domain.Database;
import com.mxdeploy.api.domain.Server;
import com.mxdeploy.api.domain.operatingsystem.UnixDir;
import com.mxdeploy.api.domain.operatingsystem.UnixFile;
import com.mxdeploy.api.domain.operatingsystem.UnixUser;
import com.mxscript.MXBSListener;
import com.mxscript.MXBeanShellException;
import com.mxterminal.ssh.exception.UnknownFileSystemException;

public class SSHConsoleService extends SSHServiceNew {
	public static final String VERSION = "1.0.0.1"; 
	
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
	protected MXBSListener listener = null;
	protected SocksConfig socksConfig = null;
	protected OSService osService;
	protected SSHFileSystemService fsService;
	protected SCPClient scpClient;
	protected int timeout = 300000;
	private String prvkeyPath;
	
	
	public SSHConsoleService() {
		this.osService = new OSService(this);
		this.fsService = new SSHFileSystemService(this);
		this.scpClient = new SCPClient();
	}

	public SSHConsoleService(SocksConfig socksConfig) {
		this();
		this.socksConfig = socksConfig;
	}

	public SSHConsoleService(MXBSListener listener) {
		this();
		this.listener = listener;
	}
	
	public Session getSshSession(){
		return session;
		
	}
	
	public String getPrvkeyPath() {
		return prvkeyPath;
	}

	public void setPrvkeyPath(String prvkeyPath) {
		this.prvkeyPath = prvkeyPath;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
	public Logger getLogger() {
		return this.logger;
	}
	
	/**
	 * Create loggers to be used in this class. It will log any SSH activity on a server and store it under MXD_HOME/logs
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
		try {
			org.apache.commons.io.FileUtils.touch(new File(debuglogfile));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String sshlogfile = logdir + "ssh_" + datetime + ".log";
		try {
			org.apache.commons.io.FileUtils.touch(new File(sshlogfile));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
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

	public boolean connect(String hostname, String username) throws UnknownHostException, SocketException, IOException, JSchException, InterruptedException {
	      return connect(hostname, username, null);	
	}
	
	public boolean connect(String hostname, String username, String password) throws UnknownHostException, SocketException, IOException, JSchException, InterruptedException {
		STATUS =1;
		boolean isConnected = false;
		this.interactive = true;
		
		createLoggers(hostname);
			 
		if( AccountConfig.getInstance() !=null ){
	        if( AccountConfig.getInstance().isConnectViaIP() ){
	     	   for(Server server : Database.getInstance().getServerList()){
	     		   if(server.getHostname().contains(hostname)){
	     			   if(server.getAddress()!=null && server.getAddress().trim().length()>0){
	     				  hostname = server.getAddress();
	     				  break;
	     			   }
	     		   }
	     	   }
	        }
		}
        
		logger.debug("opening session channel");
   		isConnected = openSessionChannel(hostname, username, password, 0);
   		
   		logger.debug("isConnected: " + isConnected);
   		if(isConnected){
   		   STATUS=2;
   		}
   		
    	String msg = flush2();
    	loggerSSH.info(msg);
    	logger.debug("SSHServiceNew VERSION: " + VERSION );

    	this.password = password;
    	
    	/**
    	 * This piece of code starts the input/output listeners for commands and outputs
    	 * once a command is sent, OutputThread will read InputStream from SSH connection
    	 * and return as a String setting a flag to warn that it is no longer running
    	 */
		thread = new OutputThread();
		thread.setPassword(password);
        thread.start();

        logger.debug("set prompt to MXTERMINAL" );
		executeCommand("export PS1='MXTERMINAL>'" );

        logger.debug("Reset buffer" );
		Thread.sleep(100);
		flush2();
		
        logger.debug("unalias ls" );
		executeCommand("unalias ls");
		Thread.sleep(100);
		flush2();
		
		logger.debug("setup umask for file permissions default");
		executeCommand("umask 022");
		Thread.sleep(100);
		flush2();

		return isConnected;
	}
	
	public String connectInteractively(String hostname, String username, String password) throws UnknownHostException, SocketException, IOException, JSchException, InterruptedException {
		STATUS =1;
		boolean isConnected = false;
		this.interactive = true;
		
        if( AccountConfig.getInstance().isConnectViaIP() ){
      	   for(Server server : Database.getInstance().getServerList()){
      		   if(server.getHostname().contains(hostname)){
      			   if(server.getAddress()!=null && server.getAddress().trim().length()>0){
      				  hostname = server.getAddress();
      				  break;
      			   }
      		   }
      	   }
         }
         
   		isConnected = openSessionChannel(hostname, username, password, 0);
   		
   		if(isConnected){
   		   STATUS=2;
   		}
    	this.password = password;
   		
    	String msg = flush2();
    	logger.debug("after flush on connectInteractively o flush\n***********************************\n" + msg + "\n***********************************\n");
    	
    	//setup current password to be sent if requested
		thread = new OutputThread();
		thread.setPassword(password);
        thread.start();
    	return msg;
	}
	
	public String sendConnectingCommands() throws IOException, InterruptedException {
		executeCommand("export PS1='MXTERMINAL>'" );

		Thread.sleep(100);
		String msg = flush2();
		
		executeCommand("unalias ls");
		Thread.sleep(100);
		msg += flush2();
		return msg;
	}
	
	public void disconnect(){
		try{
			if(this.session != null && this.session.isConnected()) {
				  this.session.disconnect();
				  this.session = null;
			}
			if(this.thread != null) {
				this.thread.kill();
			}
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
		return stdOut;
	}

	public boolean openSessionChannel(String hostname, String username,	String password, int tunnelPort) throws UnknownHostException, IOException, JSchException, InterruptedException {
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

		//enable or disable socks
		if( AccountConfig.getInstance() != null )
			socksService.setupSocksByHostname(hostname);
		
		if( this.getPrvkeyPath() != null ){
			jsch.addIdentity(this.getPrvkeyPath());
		}
		//setting up JSCH session 
		this.session = jsch.getSession(username, hostname, 22);
		
		this.session.setTimeout(this.timeout);
        this.session.setConfig("StrictHostKeyChecking", "no");
		if( this.getPrvkeyPath() == null ){
			this.session.setPassword(password);
		}        
        
        this.session.connect();

        this.channel = (ChannelShell)session.openChannel("shell");
		int tcol=1023;
		int trow=24;
		int twp=640;
		int thp=480;
		this.channel.setPtySize(tcol, trow, twp, thp);
		this.channel.setPty(true);
        this.commandIO = new PipedOutputStream();
        this.sessionInput = new PipedInputStream(this.commandIO);

        this.channel.setInputStream(sessionInput);
        this.sessionOutput = channel.getInputStream();

        // this will have the STDERR from server
        this.sessionError = channel.getExtInputStream();
        this.channel.connect();

		if (username == null || username.trim().equals(""))
			username = System.getProperty("user.name");

		return result;
	}
	
	/**
	 * Send a command to the server 
	 * @param command - Command to be sent
	 * @return String - output generated by the command sent, removing lines in regards to the command itself, in other words, just the output is returned
	 */
	public String executeCommand(String command) throws IOException, InterruptedException {
    
        String msg = executeCommand(command, "MXTERMINAL>");
        /*
        if(msg.contains("assword") && !msg.contains("-v password")) {
        	return msg;
        }
        */
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
	
	/**
	 * Test if a specific user id exists by issuing the finger command
	 * @param id - is the id to be validated
	 * @return true if the finger command finds the ID 
	 */
	public boolean isID(String id) throws IOException, InterruptedException {
		boolean valid = true;
		String msg = executeCommand("finger " + id);
		if(msg != null && (msg.contains("ser not found") || msg.contains("o such user")) ) {
			valid = false;
		}
		return valid;
	}

	/**
	 * Switch user id by issuing the sudo su <userID> command. 
	 * This will first validate if the ID exists and if the switch succeed then will send default commands umask and PS1
	 * @param userID ID to be active
	 * @return output of the command
	 */
	public String sudoSUToUser(String userID) throws CannotSudoException , IOException, InterruptedException {

		if(!isID(userID)) {
			throw new CannotSudoException("User " + userID + " not found");
		}
		
		String msg = executeCommand("sudo su " + userID, "$");
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
		}else {
			throw new CannotSudoException(msg);
		}
		
		return msg;
	}

	/**
	 * Switch user id by issuing the sudo su <userID> command. 
	 * This will first validate if the ID exists and if the switch succeed then will send default commands umask and PS1
	 * @param userID ID to be active
	 * @param password is the password to be used in case asked by the operating system
	 * @return output of the command
	 */
	public String sudoSUToUser(String userID, String password) throws CannotSudoException , IOException, InterruptedException {

		if(!isID(userID) && !userID.equals("")) {
			throw new CannotSudoException("User " + userID + " not found");
		}
		
		String msg = executeCommand("sudo su - " + userID);
		boolean gotit = true;
		if(msg != null && msg.contains("Sorry")) {
			gotit = false;
		} else {
			if( msg != null && msg.contains("assword")) {
				executeCommand(password);
			}
			
			msg = this.executeCommand("id");
			if(!msg.contains(userID)) {
				gotit = false;
			}
		}
		
		if(gotit) {
			executeCommand("umask 022");
			flush2();
			executeCommand("export PS1='MXTERMINAL>'" );
			flush2();
		}else {
			throw new CannotSudoException(msg);
		}
		
		return msg;
	}

	public String execute(String command) throws IOException, InterruptedException {
		int timeout = 50000;
		String msg = ""; 
		command = command + "\n";

		System.out.println("COMMAND TO EXECUTE: \n" + command + "\n");
    	commandIO.write(command.getBytes());
        commandIO.flush();
         
    	thread.read();
    	int count = 50;
        while( thread.isRunning() && timeout > 0){
        	count --;
        	if(count==0) {
        		System.out.println("thread.isRunning");
        		logger.debug("Thread.isRunning");
        		count = 50;
        	}
        	Thread.sleep(300);
        	timeout = timeout -30;
        }
         
        if(timeout < 0) {
        	msg = "command " + command + " has timed out waiting for a response. Last output was: " + thread.getLastOutput();
        } else {
        	msg = thread.getOutput();
        }

    	return msg;
	}	
	
	public String executeSSHCommandWithPassword(String command, String password) throws IOException, InterruptedException {
		String keepSave = thread.getPassword();
		thread.setPassword(password);
		String msg = executeCommand(command);
		thread.setPassword(keepSave);
		return msg;
	}
	
	public String executeCommand(String command, String expect) throws IOException, InterruptedException {
//      while( !isConnected() ){
//      	Thread.sleep(300);
//      }
		int timeout = 50000;
		
		flush2();
		
		String msg = ""; 
		command = command + "\n";
		System.out.println("COMMAND TO EXECUTE: \n" + command + "\n");
		
  	    commandIO.write(command.getBytes());
        commandIO.flush();
        
  	    thread.read();
        while( thread.isRunning() && timeout > 0){
      	  Thread.sleep(30);
      	  timeout = timeout -30;
        }
       
      if(timeout < 0) {
      	msg = "command " + command + " has timed out waiting for a response. Last output was: " + thread.getLastOutput();
      } else {
      	msg = thread.getOutput();
      }
		logger.debug("command (" + command + ") output: " + msg );

		String xx = flush2();
  	return msg;
	}
	
	public String executeCommandExpectPassword(String command, String expect) throws IOException, InterruptedException {
	      thread.expectPassword = true;
//      while( !isConnected() ){
//      	Thread.sleep(300);
//      }
		int timeout = 50000;
		
		flush2();
		
		String msg = ""; 
		command = command + "\n";
		System.out.println("COMMAND TO EXECUTE: \n" + command + "\n");
  	commandIO.write(command.getBytes());
      commandIO.flush();
  	thread.read();
      while( thread.isRunning() && timeout > 0){
      	Thread.sleep(30);
      	timeout = timeout -30;
      }
      thread.expectPassword = false;

      if(timeout < 0) {
      	msg = "command " + command + " has timed out waiting for a response. Last output was: " + thread.getLastOutput();
      } else {
      	msg = thread.getOutput();
      }
		logger.debug("command (" + command + ") output: " + msg );

		String xx = flush2();
  	return msg;
	}
	
	/**
	 * Send a command to the server and wait till it returns an expected statement or the timeout limit is reached
	 * @param command
	 * @param expect
	 * @param timeoutMilis
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public String executeCommand(String command, String expect, long timeoutMilis) throws IOException, InterruptedException {
		String result = "";
		
		String msg = execute(command);
		if(msg.contains(expect)) {
			result = msg;
		} else {
			boolean timeout = false;
			long startMilis = System.currentTimeMillis();
			while( ( (msg == null) || (!msg.contains(expect)) ) && !timeout) {
				long currMilis = System.currentTimeMillis();
				if(currMilis - startMilis > timeoutMilis) {
					timeout = true;
					result = "command timeout reached";
				}
				msg = flush2();
			}
			if(msg != null && msg.contains(expect)) {
				result = msg;
			}
		}
		
		return result;
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
	
	public String getLastOutput() {
		return thread.getLastOutput();
	}

	private class OutputThread extends Thread {
		private String output;
		
		private boolean live = true;
		private boolean running = true;
		private String password = null;
		private boolean stopReading = false;

		private String lastOutput;

		private boolean expectPassword;
		
		public String getLastOutput() {
			return this.lastOutput;
		}
		
		public String getPassword() {
			return this.password;
		}
		
		public void setPassword(String password) {
			this.password = password;
		}
		
		public void stopReading() {
			stopReading = true;
		}
		
		public void startReading() {
			stopReading = false;
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
			boolean sendpass = false;
	    	
			while(true){
				if(!live){
					break;
				}
				
				if(stopReading) {
					Thread.sleep(300);
					continue;
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
	                
	                //logger.debug(new String(tmp) );

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
	    		
	    		
	            if ( this.expectPassword && (stdOut.contains("assword") || stdErr.contains("assword") ) ) {
	            	System.out.println("entrou no if de password");
	            	this.output = stdOut;
	    			output = output.replaceAll("\b", "");
	    			stdOut = "";
	    			logger.debug("========STDOUT\n" + output + "\n===========");
	    			logger.debug("SETTING RUNNING = FALSE => change password");
	            	this.running = false;
	            }
	            
	            if ( (stdOut.contains("assword:") || stdErr.contains("assword:") ) ) {
	    			//String cmd = this.password + "\n";
	    			String cmd = this.password + "\n";
	    			stdOut = "";
	    			commandIO.write(cmd.getBytes());
	    			commandIO.flush();
	    			Thread.sleep(30); 
	            }

	            
	            if(interactive && ( (stdOut.contains("(current) UNIX password")) || (stdOut.contains("New UNIX password")) || (stdOut.contains("Retype new UNIX password")) || (stdOut.contains("Old password") || stdOut.contains("ew password") || stdOut.contains("Enter the new password again")) ) ) {
	            	System.out.println("entrou no if de trocar password");
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
	
	public static void main(String args[]){
		//testAmex();
		testIGA();
	}
	
	private static void testAmex() {
		SSHConsoleService ssh = new SSHConsoleService();
		try {
			ssh.connect("167.210.245.161", "jsilv1", "");
			
			String msg = ssh.executeCommand("ssh SYDSCTECCB01");
			if(msg.contains("denied")) {
				System.err.println(msg);
			} else {
				ssh.executeCommand("uname -a");
				ssh.executeCommand("exit");
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSchException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			ssh.disconnect();
		}
	}
	
	private static void testIGA() {
		SSHConsoleService ssh = new SSHConsoleService();
		try {
			ssh.connect("localhost", "fsbsilva", "");
			String x = ssh.sudoSUToUser("xxxx");
			x = ssh.executeCommand("id");
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSchException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ssh.disconnect();
		}

		
		try {
			ssh = new SSHConsoleService();
			ssh.connect("localhost", "fsbsilva", "jemi");
			ssh.sudoSUToUser("xxxx");
			ssh.executeCommand("id");
		
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSchException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ssh.disconnect();
		}

	}

	public boolean isConnected() {
		if(getSshSession() == null) return false;
		return getSshSession().isConnected();
		//return (STATUS==2) ? true : false;
	}
	
	public File getFile(String sourceDir, String sourceFile) {
		SCPClient scp = new SCPClient();
		return scp.getFile(this, sourceDir, sourceFile);			
	}
	
	public void downloadFile(String sourceDir, String sourceFile, String destDir) {
		SCPClient scp = new SCPClient();
		logger.debug("downloadFile: " + sourceDir + " " + sourceFile + " -> " + destDir);
		scp.getFile(this, sourceDir, sourceFile, destDir);			
	}
	
	public boolean isConnecting(){
		synchronized (STATUS) {
			if(STATUS==1){
			   return true;
			}
			return false;
		}
	}

	public UnixUser getUnixUser(String id) throws InterruptedException, IOException {
		return this.osService.getUnixUser(id);
	}

	public UnixDir getUnixDirectory(String path, Boolean recursive, String filter) throws UnknownFileSystemException, IOException, InterruptedException, MXBeanShellException{
		return this.fsService.getUnixDirectory(path, recursive, filter);
	}
	
	public UnixDir getUnixDirectory(String path, Boolean recursive) throws UnknownFileSystemException, IOException, InterruptedException, MXBeanShellException{
		System.out.println("getUnixDirectory(String path, Boolean recursive, null) ");
		return getUnixDirectory(path, recursive, null);
	}
	
	public UnixDir getUnixDirectory(String path) throws UnknownFileSystemException, IOException, InterruptedException, MXBeanShellException{
		return getUnixDirectory(path, true);
	}
	
	public UnixFile getUnixFile(String dir, String fileName) throws UnknownFileSystemException, IOException, InterruptedException, MXBeanShellException {
		return this.fsService.getUnixFile(dir, fileName);
	}

	public UnixFile getUnixFileByPath(String filePath) throws UnknownFileSystemException, IOException, InterruptedException, MXBeanShellException {
		return this.fsService.getUnixFilebyPath(filePath);
	}

	public void uploadFile(String sourceFilePath, String destinationPath ) {
		scpClient.sendFile(this, sourceFilePath, destinationPath);
	}

	public String sshToHost(String host) throws IOException, InterruptedException {
		String msg =  executeCommandExpectPassword("ssh " + host, "MXTERMINAL>");
		System.out.println(msg);
		if(msg.contains("Are you sure you want to continue connecting")) {
			msg = executeCommandExpectPassword("yes", "MXTERMINAL>");
			System.out.println(msg);
		}
		return msg;
	}

}
