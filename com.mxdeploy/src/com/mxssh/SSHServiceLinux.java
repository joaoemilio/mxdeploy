package com.mxssh;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.mxscript.MXBeanShellException;

public class SSHServiceLinux implements SSHCommand {
	static Logger LOGGER = Logger.getLogger(SSHServiceNew.class);

	protected SSHServiceNew ssh;
	private String strLinha;
	
	public SSHServiceLinux(SSHServiceNew ssh){
		this.ssh = ssh;
	}
	
	/**
	 * Please send the process name that you want and it will return an array of String with respective IDs running that process. Can be multiple.
	 * @param processName
	 * @return
	 * @throws InterruptedException 
	 * @throws MXBeanShellException 
	 */
	public String[] getProcessIDs(String processName) throws IOException, InterruptedException, MXBeanShellException {
		String message = null;
		String cmd = null;
		int running = 0;

		cmd = "ps -ef | grep " + processName + " | grep -v grep | wc -l";
		message = ssh.executeCommand(cmd);
		
		String msg = message.replaceAll("\r\n", "").replaceAll("\\$", "").trim(); 
		LOGGER.debug(msg);

		try{
			if(new Integer(msg.substring(0,1)) == 0){
				running = -1;
			}
		}finally{
		}
		
		if(running == -1) {
			LOGGER.debug("Process: " + processName + " not running");
			return null;
		}

		cmd = "ps -ef | grep " + processName + " | grep -v grep | awk {'print $1'}";
		message = ssh.executeCommand(cmd); 

		String[] ids = message.split( "\r\n" );
		return ids;
	}

	/**
	 * Get process ID groups
	 * @param userID
	 * @return
	 * @throws InterruptedException 
	 * @throws MXBeanShellException 
	 */
	public String getUserIDGroups(String userID) throws IOException, InterruptedException, MXBeanShellException {
		String message = null;
		String cmd = null;

		cmd = "id " + userID;
		message = ssh.executeCommand(cmd); 

		String[] groups = message.split( "\r\n" );

		return groups[0];
	}

	public void exitUser() throws IOException, InterruptedException, MXBeanShellException {
		String cmd = null;
		cmd = "exit";
		ssh.executeCommand(cmd);
		
		cmd = "export PS1='$ '";
		ssh.executeCommand(cmd);
		
		cmd = "id";
		ssh.executeCommand(cmd);
	}
	
	public String getProcessLineDetails(String processName) throws IOException, InterruptedException, MXBeanShellException {
		String message = null;
		String cmd = null;
		cmd = "ps -ef | grep  " + processName + " | grep -v grep";
		message = ssh.executeCommand(cmd);
		
		return message;
	}

	public ArrayList<String> getFileList(String path, String filter) throws IOException, InterruptedException, MXBeanShellException {
		String message = null;
		String cmd = null;
		
		cmd = "ls " + path + " | xargs";
		message = ssh.executeCommand(cmd);
		
		message = message.replaceAll("\r\n", "").replaceAll("\\$", "").replaceAll(" \b", "").replaceAll("\b", "");
		String[] arquivos = message.split( " " );
		ArrayList<String> fileList = new ArrayList<String>();
		for(String arquivo: arquivos){
			if(filter != null && !filter.equals("") && arquivo.contains(filter)){
				if(arquivo.contains(path)){
					arquivo = arquivo.substring(path.length());
				}
				fileList.add(arquivo);
			}
		}
		
		return fileList;
	}
	
}
