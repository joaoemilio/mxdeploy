package com.mxdeploy.swt.explorer.popup.event;

import com.mxdeploy.plugin.event.EventHandler;
import com.mxdeploy.swt.MainShell;
import com.mxdeploy.swt.server.ServerEditDialog;

public class NewServerEventHandler implements EventHandler {

	public void execute() {
		ServerEditDialog dialog = new ServerEditDialog();
		dialog.createSShell("New Server");
		dialog.getHelper().setProject(MainShell.getControlPanelHelper().getProjectSelectedInMyProjectTree());
		//dialog.getHelper().setWorkingSetHelper(workingSetHelper);
		dialog.openShell();
	}

}
