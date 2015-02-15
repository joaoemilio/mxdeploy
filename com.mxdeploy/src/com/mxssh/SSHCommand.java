package com.mxssh;

import java.io.IOException;
import java.util.ArrayList;

import com.mxscript.MXBeanShellException;

public interface SSHCommand {
	
	public String[] getProcessIDs(String processName) throws IOException, InterruptedException, SSHException, MXBeanShellException;
	
	public String getUserIDGroups(String userID) throws IOException, InterruptedException, MXBeanShellException;
		
	public String getProcessLineDetails(String processName) throws IOException, InterruptedException, MXBeanShellException;
	
	public ArrayList<String> getFileList(String path, String filter) throws IOException, InterruptedException, MXBeanShellException;
	
	public void exitUser() throws IOException, InterruptedException, MXBeanShellException;

}
