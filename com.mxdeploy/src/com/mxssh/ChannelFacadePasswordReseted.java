package com.mxssh; 

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * This example demonstrates the connection process using the standard SSH
 * interfaces that enable connections to both SSH1 and SSH2 servers.
 * 
 * @author Joao Emilio
 */
public class ChannelFacadePasswordReseted { 
	static Logger logger = Logger.getLogger(ChannelFacadePasswordReseted.class);
    protected JSch jsch = new JSch();
    protected Session session;
    protected Channel channel;
    protected String newPassword;
    protected String password;
	
	protected static int currbyte = 0;
	//protected ChannelAdapter eventListener = null;
	//protected DynamicBuffer dynamic = null;
	protected List<ChannelListener> listeners;

	private boolean canClose = false;
	private boolean isStartShell = true;
	private boolean isStopped = false;
	private int canExit = 0;
	//protected InputStream inputStream;
	//protected OutputStream outputStream;
	protected PipedOutputStream commandIO = null;
	protected InputStream sessionInput = null;
	protected InputStream sessionOutput = null;
	protected InputStream sessionError = null;
	
	public static final int MESSAGE_COMMAND = 0;
	public static final int MESSAGE_PASSWORD = 1;
	public static final int MESSAGE_INFORMATION = 2;
	public static final int MESSAGE_SENT = 3;
	public static final int MESSAGE_RECEIVED = 4;
	public static final int MESSAGE_REQUEST_PASSWORD = 5;
	public static final int MESSAGE_RECEIVED_END = 6;
	
	public Session getSshSession(){
		return session;
	}
	
	public ChannelFacadePasswordReseted(){ 
	}
	
	/**
	 * @return the isStopped
	 */
	public boolean isStopped() {
		return isStopped;
	}


	/**
	 * @param isStopped the isStopped to set
	 */
	public void setStopped(boolean isStopped) {
		this.isStopped = isStopped;
	}

	public String executeCommand(final String command, Callback callback){
		return executeCommand(command, null, callback);
	}

	public String executeCommand(final String command, String expectedEnd, Callback callback){
	    if(command.contains("exit")&&(canExit==0)){
	    	return null; 
	    } if(command.contains("exit")&&(canExit>0)){
	    	canExit--;
	    }
		
	    //System.out.println("new dynamic"); 
	    String cmd = command+" \n";
	    if( (command.trim().startsWith("su -"))||(command.trim().startsWith("sudo su -"))||(command.contains("ssh")) ){
	    	cmd = command+" \n";	
	    	canExit++;
	    } else {
	    	cmd = command+" \n";
	    }
	    if(!command.trim().startsWith("echo")){
		   notifyListeners(cmd, MESSAGE_INFORMATION, callback);
	    } else {
	       notifyListeners("\n", MESSAGE_INFORMATION, callback);
	    }
		try {
			if(cmd != null) {
				commandIO.write(cmd.getBytes());
				commandIO.flush();
			}
			//logger.debug("Executou comando: " + cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String output = inserirLinha(callback);
		if(callback != null) output = ((SimpleCallback)callback).getMessage();
		if(output != null && (output.length() > command.length())){
			if(output.substring(0, command.length()).equals(command)){
					output = output.substring(command.length());
				if(callback != null) ((SimpleCallback)callback).sendMessageEnd(output);
			}
		}
		
		if(output!=null){
			String newout = "";
			char barrab = '\b';
	        int ibarrab = (int) barrab;
			for(int j = 0; j < output.length(); j++ ){
				char c = output.charAt(j);
		        int ascii = (int) c;
		        if(ascii < 127 && ascii != ibarrab){
		        	newout += c;
		        }
			}
			output = newout;
		}
				
		return output; 
	}

	public String executeStatement(final String command){
	    if(command.contains("exit")&&(canExit==0)){
	    	return null; 
	    } if(command.contains("exit")&&(canExit>0)){
	    	canExit--;
	    }
		
	    if( (command.contains("sudo"))||(command.contains("su -"))||(command.contains("ssh")) ){
	    	canExit++;
	    }
				
		notifyListeners("\r", MESSAGE_INFORMATION);
		try {
			commandIO.write(" \n".getBytes());
			commandIO.flush();
			//logger.debug("Executou executeStatement: ");
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return inserirLinha(null);
	}
	
	public void sendPassword(final String password){
		String cmd = password+"\n";
		//notifyListeners(cmd, MESSAGE_INFORMATION);
		try {
			commandIO.write(cmd.getBytes());
			commandIO.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    public void closeSessioChannel(){
	   if(channel!=null){
	      channel.disconnect();
	   }
    }
    
    public void disconnect(){
    	channel.disconnect();
    }
    
	public String inserirLinha(Callback callback){
		//boolean isRunningPasswd = false;
        byte[] tmp = new byte[1024];
        String stdOut = "";
        String stdErr = "";

        int i;
        boolean sendpass = true;
        try {
        	boolean breakErr = false;
			while(true){
	            if (sessionError.available() >  0) {
	                i = sessionError.read(tmp, 0, tmp.length);
	                if (i < 0) {
	                    System.err.println("input stream closed earlier than expected");
	                    System.exit(1);
	                }
	                stdErr += new String(tmp, 0, i);
	                breakErr = false;
	            }else{
	            	breakErr = true;
	            }

	            if (sessionOutput.available() > 0) {
	                i = sessionOutput.read(tmp, 0, tmp.length);
	                if (i < 0) {
	                    System.err.println("input stream closed earlier than expected");
	                    System.exit(1);
	                }
	                stdOut += new String(tmp, 0, i);
	            }else {
	            	if(breakErr){
	            		break;
	            	}
	            }
	            	
				if ((stdOut.contains("Re-enter") && stdOut.contains("ew password") ) && sendpass) {
					sendpass = false;
	    			String cmd = this.newPassword + "\n";
	    			commandIO.write(cmd.getBytes());
	    			commandIO.flush();
	    			Thread.sleep(3000);
				} else if ((stdOut.contains("New password") )&& sendpass) {
	    			String cmd = this.newPassword + "\n";
	    			commandIO.write(cmd.getBytes());
	    			commandIO.flush();
	    			Thread.sleep(3000);
	            }else if ((stdOut.contains("Old password") )&& sendpass) {
	    			String cmd = this.password + "\n";
	    			commandIO.write(cmd.getBytes());
	    			commandIO.flush();
	    			Thread.sleep(3000);
	            }

	            Thread.sleep(1000);
			}
			notifyListeners(stdOut, ChannelFacadePasswordReseted.MESSAGE_RECEIVED_END, callback);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String output = stdOut;
		
		output = output.replaceAll("\b", "");
		
		return output;
	}

	public boolean openSessionChannel(String hostname, String username,	String password,int tunnelPort) {
		return openSessionChannel(hostname, username, password, null, tunnelPort);
	}

	public boolean openSessionChannel(String hostname, String username,	String password, String newPassword, int tunnelPort) {
		boolean result = true;
		this.newPassword = newPassword;
		this.password = password;
	    logger.debug("Hostname: ");
       	notifyListeners("Application is trying to connect, please wait...\n", MESSAGE_INFORMATION);
		int idx = hostname.indexOf(':');
		int port = 22;
		if (idx > -1) {
			port = Integer.parseInt(hostname.substring(idx + 1));
			hostname = hostname.substring(0, idx);
		}
        try {
        	
			this.session = jsch.getSession(username, hostname, 22);
			this.session.setTimeout(15);
	        this.session.setConfig("StrictHostKeyChecking", "no");

	        this.session.setPassword(password);
	        this.session.connect();

	        this.channel = session.openChannel("shell");
	        this.commandIO = new PipedOutputStream();
	        this.sessionInput = new PipedInputStream(this.commandIO);
	        // this set's the InputStream the remote server will read from.
	        this.channel.setInputStream(sessionInput);
	        ChannelShell cs = (ChannelShell)channel;
	        cs.setPty(true);

	        // this will have the STDOUT from server.
	        this.sessionOutput = channel.getInputStream();

	        // this will have the STDERR from server
	        this.sessionError = channel.getExtInputStream();
	        this.channel.connect();
	        inserirLinha(null);
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (username == null || username.trim().equals(""))
			username = System.getProperty("user.name");

		return result;
	}

	public void addListener(ChannelListener listener){
		if(listeners == null){
			listeners = new ArrayList<ChannelListener>();
		}
		listeners.add(listener);
	}
	
	public void notifyListeners(String message, int messageType, Callback callback){
		if(listeners != null){
			for(ChannelListener listener: listeners){
				listener.receiveMessage(message, messageType, callback);
			}
		}
	}
	
	public void notifyListeners(String message, int messageType){
		if(listeners != null){
			for(ChannelListener listener: listeners){
				listener.receiveMessage(message, messageType);
			}
		}
	}
	

}
