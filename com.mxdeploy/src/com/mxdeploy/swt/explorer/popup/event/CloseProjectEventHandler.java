package com.mxdeploy.swt.explorer.popup.event;

import com.mxdeploy.plugin.event.EventHandler;
import com.mxdeploy.swt.MainShell;
import com.mxdeploy.swt.explorer.event.LoadAllProjectTreeEventHandler;

public class CloseProjectEventHandler implements EventHandler {

	public void execute() {
		  MainShell.getControlPanelHelper().closeProjectSelected();
		  LoadAllProjectTreeEventHandler.execute();		  
	}

}
