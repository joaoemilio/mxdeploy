package com.mxdeploy.plugin.event;

import com.mxterminal.ssh.InteractiveClientSSH;

public interface SSHEventHandler  {

	public void execute(InteractiveClientSSH client); 	
}
