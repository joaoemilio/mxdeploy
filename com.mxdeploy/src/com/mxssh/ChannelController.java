package com.mxssh;

import java.io.IOException;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class ChannelController {

	private ChannelFacade channel;
	private ChannelListener listener = null;
	private boolean opennedSession = false;
	private String password;
	private String hostname;
	
	
	public String getPassword(){
		return this.password;
	}
	public ChannelController(){
		initialize();
	}
	
	private void initialize(){
		this.channel = new EventBasedChannel();
	}
	
	public void setListener(ChannelListener listener){
		this.listener = listener;
		this.channel.addListener(this.listener);
	}
	
	public String executeCommand(String command) throws IOException, InterruptedException{
		 System.out.println("executeCommand - "+command);
		return this.channel.executeCommand(command, null);
	}
	
	public String executeCommand(String command, Callback callback) throws IOException, InterruptedException{
		return this.channel.executeCommand(command, callback);
	}
	
	public String executeCommand(String command, String expectedEnd, Callback callback) throws IOException, InterruptedException{
		return this.channel.executeCommand(command, expectedEnd, callback);
	}
	
	public void executeStatement(String command) throws InterruptedException{
		this.channel.executeStatement(command);
	}	
	
	public void sendPassword(String password){
		this.channel.sendPassword(password);
	}
	
	public boolean openSessionChannel(String hostname, String username, String password, int tunnelPort) throws JSchException, InterruptedException{
		this.hostname = hostname;
		this.opennedSession = this.channel.openSessionChannel(hostname, username, password,tunnelPort);
		
		this.password = password;
		return this.opennedSession;
	}
	
	public boolean isSessionOpen(){
		return this.opennedSession;
	}
	
	public void closeSessionChannel(){
		this.channel.closeSessioChannel();
	}
	
	public void disconnect(){
		if(this.channel != null){
			this.channel.disconnect();
		}
		
		if(this.getSshSession() != null){
			this.getSshSession().disconnect();
		}
	}
	/**
	 * @return the isStopped
	 */
	public boolean isStopped() {
		return this.channel.isStopped();
	}
	/**
	 * @param isStopped the isStopped to set
	 */
	public void setStopped(boolean isStopped) {
		this.channel.setStopped(isStopped);
	}
	
	public Session getSshSession(){
		return this.channel.getSshSession();
	}
	/**
	 * @return the hostname
	 */
	public String getHostname() {
		return hostname;
	}
	
	public void closeAll() {
		channel.closeSessioChannel();
		
		if(this.channel.getSshSession()!=null)
			this.channel.getSshSession().disconnect();
	}
	
	public void forceFlush(Callback callback) throws InterruptedException{
		channel.inserirLinha(callback);
	}
	
	public String forceFlush(String command) throws InterruptedException{
		SimpleCallback callback = new SimpleCallback();
		String output = channel.inserirLinha(callback);
		if(callback != null) output = ((SimpleCallback)callback).getMessage();
		if(output != null && (output.length() > command.length())){
			if(output.substring(0, command.length()).equals(command)){
				output = output.substring(command.length());
				output = output.replaceAll("\b", "");
				if(callback != null) ((SimpleCallback)callback).sendMessageEnd(output);
			}
		}

		String[] linhas = output.split("\r\n");
		output = "";
		if(linhas != null && linhas.length > 0){
			if(linhas[0] != null && !linhas[0].trim().equals("")){
				output = linhas[0] + "\r\n";
			}
		}
		for(int i = 1;i < linhas.length; i++){
			output = output + linhas[i] + "\r\n";
		}

		return output;
	}
	
}

