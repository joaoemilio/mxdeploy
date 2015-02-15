package com.mxdeploy.swt.explorer.popup.event;

import org.eclipse.swt.widgets.TreeItem;

import com.mxdeploy.api.domain.Server;
import com.mxdeploy.plugin.event.EventHandler;
import com.mxdeploy.swt.MainShell;
import com.mxdeploy.swt.server.ServerCRUDFactory;

public class EditServerEventHandler implements EventHandler{

	public void execute() {
		TreeItem serverTreeItem = (TreeItem)MainShell.getControlPanelHelper().getMyProjectTree().getSelection()[0];
		Server server = (Server)serverTreeItem.getData();
		ServerCRUDFactory.openEditServer(MainShell.getControlPanelHelper().getProjectSelectedInMyProjectTree(),server);
		
	}

}
