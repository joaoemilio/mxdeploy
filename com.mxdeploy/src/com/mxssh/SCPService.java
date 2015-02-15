package com.mxssh;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.jcraft.jsch.JSchException;
import com.mxdeploy.api.domain.operatingsystem.UnixFile;


public class SCPService {

	private SSHServiceNew ssh = null;
	
	public SCPService(SSHServiceNew ssh){
		this.ssh = ssh;
	}
	
	public SCPService(){

	}
	
	public void downloadFile(String sourceDir, String sourceFile, String destinationDir, String destinationFile) {
		SCPClient scp = new SCPClient();
		scp.getFile( this.ssh, sourceDir, sourceFile, destinationDir);
	}
	
	public UnixFile uploadFile(String sourceFilePath, String hostname, String destinationPath, String username, String password) throws SCPException {
		SSHConsoleService sshClient = new SSHConsoleService();
		try{
			sshClient.connect(hostname, username, password);
			SCPClient scpClient = new SCPClient();
			
			File file = new File(sourceFilePath);
			String fileName = file.getName();
			if(file.isFile()) {
				scpClient.sendFile(sshClient, sourceFilePath, destinationPath + "/" + fileName);
			}
			UnixFile ufile = sshClient.getUnixFile(destinationPath, fileName);
			if(ufile == null) {
				throw new SCPException("File could not be uploaded. Unknown reason");
			}
			return ufile;
		}catch(Exception e) {
			throw new SCPException(e);
		}finally{
			if(sshClient != null && sshClient.isConnected()) {
				sshClient.disconnect();
			}
		}

	}
	
}
