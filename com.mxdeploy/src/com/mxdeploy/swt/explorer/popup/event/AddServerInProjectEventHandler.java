package com.mxdeploy.swt.explorer.popup.event;

import com.mxdeploy.api.domain.Project;
import com.mxdeploy.plugin.event.EventHandler;
import com.mxdeploy.swt.MainShell;
import com.mxdeploy.swt.project.AddServerProjectDialog;

public class AddServerInProjectEventHandler implements EventHandler{

	public void execute() {
		AddServerProjectDialog dialog = new AddServerProjectDialog();
		Project project = MainShell.getControlPanelHelper().getProjectSelectedInMyProjectTree();
		dialog.createSShell(project.getName()+" - Add Server");
		dialog.getHelper().loadCreatedProject();
		dialog.openShell();
	}

}
