package com.mxterminal.swt.view.event;

import com.mxdeploy.plugin.event.EventHandler;
import com.mxdeploy.swt.MainShell;
import com.mxterminal.swt.TunnelDialog;
import com.mxterminal.swt.view.helper.TerminalHelper;

public class OpenTunnelDialogEventHandler implements EventHandler {
	
	public void execute(){
		TerminalHelper terminalHelper = MainShell.getToolBarViewHelper().getTerminalHelper();
		String hostname = terminalHelper.getTermianlFace().getHostname();
		
    	if(terminalHelper.getSSHInteractiveClient()==null || !terminalHelper.getSSHInteractiveClient().isAuthenticated()){
    		MainShell.sendMessage("Connect on Server first !");
    	} else {
			TunnelDialog dialog = new TunnelDialog();
			dialog.createSShell("Configure Tunnel");
			dialog.getHelper().setSSHInteractiveClient(terminalHelper.getSSHInteractiveClient());
			dialog.getHelper().loadBindHash(hostname);
			dialog.openShell();
    	}
	}
	

}
