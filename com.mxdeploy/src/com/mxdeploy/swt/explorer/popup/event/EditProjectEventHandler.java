package com.mxdeploy.swt.explorer.popup.event;

import com.mxdeploy.plugin.event.EventHandler;
import com.mxdeploy.swt.MainShell;
import com.mxdeploy.swt.project.ProjectDialog;

public class EditProjectEventHandler implements EventHandler {

	public void execute() {
		ProjectDialog dialog = new ProjectDialog();
		dialog.createSShell("Edit Project");
		dialog.getHelper().loadFields(MainShell.getControlPanelHelper().getProjectSelectedInMyProjectTree());
		dialog.openShell();
	}

}
