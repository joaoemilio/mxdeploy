package com.mxdeploy.swt.explorer.popup.event;

import com.mxdeploy.plugin.event.EventHandler;
import com.mxdeploy.swt.project.ProjectDialog;

public class NewProjectEventHandler implements EventHandler {

	public void execute() {
		
		ProjectDialog dialog = new ProjectDialog();
		dialog.createSShell("New Project");
		dialog.openShell();
	}

}
