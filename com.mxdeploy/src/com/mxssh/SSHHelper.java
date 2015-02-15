package com.mxssh;

import java.io.IOException;

import com.mxscript.MXBeanShellException;

public class SSHHelper {

	private SSHCommand sshOS = null;
	private SSHServiceNew ssh = null;
	
	public SSHHelper(SSHServiceNew ssh) throws MXBeanShellException {
		this.ssh = ssh;
		try {
			String msg = ssh.executeCommand("uname -a");
			if(msg.contains("Linux")) {
				this.sshOS = new SSHServiceLinux(ssh);
			}else{
				this.sshOS = new SSHServiceAIX(ssh);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Please send the process name that you want and it will return an array of String with respective IDs running that process. Can be multiple.
	 * @param processName
	 * @return
	 * @throws InterruptedException 
	 * @throws SSHException 
	 * @throws MXBeanShellException 
	 */
	public String[] getProcessIDs(String processName) throws IOException, InterruptedException, SSHException, MXBeanShellException {
		return sshOS.getProcessIDs(processName);
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

	public String getProcessLineDetails(String processName) throws IOException, InterruptedException, MXBeanShellException {
		String message = null;
		String cmd = null;
		cmd = "ps -ef | grep  " + processName + " | grep -v grep";
		message = ssh.executeCommand(cmd);
		
		return message;
	}	

	public void sudoSUToUser(String userID) throws CannotSudoException , IOException, InterruptedException, MXBeanShellException {
		//sshOS.sudoSUToUser(userID);
		ssh.sudoSUToUser(userID);
	}
	
	public void exitUser() throws IOException, InterruptedException, MXBeanShellException {
		sshOS.exitUser();
	}


}
