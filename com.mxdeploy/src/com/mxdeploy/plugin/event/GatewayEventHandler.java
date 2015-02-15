package com.mxdeploy.plugin.event;

import java.io.IOException;

import com.jcraft.jsch.JSchException;
import com.mxdeploy.plugin.Listener;
import com.mxssh.SSHConsoleService;

public interface GatewayEventHandler {
	
	public boolean execute(SSHConsoleService sshClient, String hostname, String username, String password, Listener listener) throws IOException, InterruptedException, JSchException;

}
