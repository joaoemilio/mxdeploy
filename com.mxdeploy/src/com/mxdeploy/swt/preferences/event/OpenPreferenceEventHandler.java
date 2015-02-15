package com.mxdeploy.swt.preferences.event;

import com.mxdeploy.plugin.event.EventHandler;
import com.mxdeploy.swt.MainShell;
import com.mxdeploy.swt.preferences.PreferencesDialog;
import com.mxterminal.swt.view.helper.TerminalHelper;

public class OpenPreferenceEventHandler implements EventHandler {

	@Override
	public void execute() {
		TerminalHelper terminalHelper = MainShell.getToolBarViewHelper().getTerminalHelper();
		String hostname = terminalHelper.getTermianlFace().getHostname();
		
    	if(terminalHelper.getSSHInteractiveClient()==null || !terminalHelper.getSSHInteractiveClient().isAuthenticated()){
    		MainShell.sendMessage("Connect on Server first !");
    	} else {
    		PreferencesDialog dialog = new PreferencesDialog();
			dialog.createSShell("Configure Tunnel");
			dialog.openShell();
    	}

		
	}

}
