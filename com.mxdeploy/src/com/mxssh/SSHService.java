package com.mxssh;
/*

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;

import org.apache.commons.net.SocketClient;
import org.apache.commons.net.discard.DiscardTCPClient;
import org.apache.log4j.Logger;

import com.ds.swt.util.TerminalProperty;
import com.jcraft.jsch.JSchException;

public class SSHService {
	static Logger LOGGER = Logger.getLogger(SSHService.class);

	//static Logger logger = Logger.getLogger(SSHService.class);
	private ChannelController channelController = null;	
	private ChannelListener listener = null;
	
	public static int commandTypeRunning =0;
	private static SSHService me;
	private boolean isConnected;
	private SSHCommand sshOS = null;
	protected int promptLines;
	
	public static SSHService getInstance(){
		if(me == null){
			me = new SSHService();
		}
		return me;
	}
	
	public ChannelController getChannelController(){
		return this.channelController;
	}
	
	public SSHService(){
		this.channelController = new ChannelController();
		setupChannelControler();
	}
	
	public void setupChannelControler(){
		listener = new SSHService.XChannelListener(); 
		channelController.setListener(listener);
	}
	
	public boolean changeResetedPassword(String hostname, String username, String password, String newPassword) throws SocketException, IOException {
		SocketClient socketClient = new DiscardTCPClient();
		
		socketClient.connect(hostname, 22);
		socketClient.disconnect();
		
		ChannelControllerPasswordReseted channelController = new ChannelControllerPasswordReseted();
		
		if(listener==null){
		   listener = new SSHService.XChannelListener();
		}
		
		LOGGER.debug("connecting..." + hostname + " - " + username + " - " + "");
		boolean isConnected = channelController.openSessionChannel(hostname, username, password, newPassword, 0);
		
		channelController.disconnect();

		return isConnected;
	}
	
	public boolean connect(String hostname, String username, String password) throws SocketException, IOException, JSchException, InterruptedException {
		
		boolean isConnected = false;
		
        if(TerminalProperty.getTunnelSSH()!=null){
        	String serverTunnel = TerminalProperty.getTunnelSSH();
        	LOGGER.debug("connecting tunnel..." + serverTunnel + " - " + username + " - " + "");
    		isConnected = channelController.openSessionChannel(serverTunnel, username, password, 0);
        } else {
    		LOGGER.debug("Connecting..." + hostname + " - " + username + " - " + "");
    		isConnected = channelController.openSessionChannel(hostname, username, password, 0);
        }
        
		String message = null;
		SimpleCallback callback = new SimpleCallback();
		int timeout = channelController.getSshSession().getTimeout();
		int timeoutCount = timeout/1000;
		while(message == null || message.trim().equals("")){
			channelController.forceFlush(callback);
			message = callback.getMessage();
			Thread.sleep(1000);
			timeoutCount --;
			if(timeoutCount <= 0){
			   throw new IOException("Timeout waiting for server to return signal");
			}
		}
		
		
		if(message.contains("Linux")){
			sshOS = (SSHCommand)new SSHServiceLinux(this);
		}else if (message.contains("AIX")){
			sshOS = (SSHCommand)new SSHServiceAIX(this);
		}else {
			sshOS = (SSHCommand)new SSHServiceAIX(this);
		}
		
		if(TerminalProperty.getTunnelSSH()!=null){
	    	if( TerminalProperty.getTaxi()!=null){
	    		String resultLast = sshOS.executeCommand("ssh "+TerminalProperty.getTaxi());	
	    	}
	    	
	    	sshOS.executeCommand("ssh "+hostname);	
	       	sshOS.executeCommand("export PS1='$ '");
	       	//resultLast=sshOS.executeCommand("uname -a"); 
	       	//System.out.println(resultLast);
		} else {
			sshOS.executeCommand("export PS1='$ '");
		}

//		message = sshOS.executeCommand("export PS1='$ '");
//		timeoutCount = timeout/1000;
//		while(message == null || !message.contains("$")){
//			message = getChannelController().forceFlush(cmd);
//			Thread.sleep(1000);
//			timeoutCount --;
//			if(timeoutCount <= 0){
//				break;
//			}
//		}
		String[] linhas = message.split("\r\n"); 
		this.promptLines = linhas.length;
		this.setConnected(isConnected);
		return isConnected;
	}
	
	public void disconnect(){
		channelController.disconnect();
	}
	
	public String[] getProcessIDs(String processName) throws IOException, InterruptedException {
		return sshOS.getProcessIDs(processName);
	}

	public String getUserIDGroups(String userID) throws IOException, InterruptedException {
		return sshOS.getUserIDGroups(userID);
	}

	public void sudoSUToUser(String userID) throws CannotSudoException , IOException, InterruptedException {
		sshOS.sudoSUToUser(userID);
	}
	
	public void exitUser() throws IOException, InterruptedException {
		sshOS.exitUser();
	}
	
	public String executeCommand(String command) throws IOException, InterruptedException {
		return sshOS.executeCommand(command);
	}
	
	public String executeLongCommand(String command) throws IOException, InterruptedException {
		return sshOS.executeLongCommand(command);
	}
	
	public String getProcessLineDetails(String processName) throws IOException, InterruptedException {
		return sshOS.getProcessLineDetails(processName);
	}

	public ArrayList<String> getFileList(String path, String filter) throws IOException, InterruptedException {
		return sshOS.getFileList(path, filter);
	}
	
	public void downloadFiles(String path, String filter, String destDir)  throws IOException, InterruptedException {
		ArrayList<String> fileList = getFileList(path, filter);
		for(String fileName: fileList){
			LOGGER.info("downloading file: " + fileName + " from: " + this.channelController.getHostname());
			SCPClient scp = new SCPClient();
			try{
				scp.getFile( this, path, fileName, destDir + "/");
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	class XChannelListener implements ChannelListener {

		int received =0;
		String _message = "";
		
		public void receiveMessage(String message, int messageType, Callback callback) {
			//logger.debug(received++);
			if(messageType == ChannelFacade.MESSAGE_RECEIVED || messageType == ChannelFacade.MESSAGE_SENT){
				LOGGER.debug("message received: " + messageType + " : " + message);
				if(callback != null) callback.sendMessage(message);
			}else if(messageType == ChannelFacade.MESSAGE_RECEIVED_END){
				if(message != null && !message.equals("")){
					LOGGER.debug("message received: MESSAGE_RECEIVED_END" + " : " + message);
				}
				if(callback != null) callback.sendMessageEnd(message);
			}else if(messageType == ChannelFacade.MESSAGE_REQUEST_PASSWORD){
				LOGGER.debug("message received: MESSAGE_REQUEST_PASSWORD" + " : " + message);
				channelController.sendPassword( channelController.getPassword() );
			}
		}
		
		public void receiveMessage(String message, int messageType) {
			//logger.debug(received++);
			if(messageType == ChannelFacade.MESSAGE_RECEIVED || messageType == ChannelFacade.MESSAGE_SENT){
				LOGGER.debug("message received: " + messageType + " : " + message);
			}else if(messageType == ChannelFacade.MESSAGE_RECEIVED_END){
				LOGGER.debug("message received: MESSAGE_RECEIVED_END" + " : " + message);
			}else if(messageType == ChannelFacade.MESSAGE_REQUEST_PASSWORD){
				LOGGER.debug("message received: MESSAGE_REQUEST_PASSWORD" + " : " + message);
				channelController.sendPassword( channelController.getPassword() );
			}
		}
		
	}	
	
	public static int getCommandTypeRunning() {
		return commandTypeRunning;
	}

	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}

	public boolean isConnected() {
		return isConnected;
	}
	
}
*/