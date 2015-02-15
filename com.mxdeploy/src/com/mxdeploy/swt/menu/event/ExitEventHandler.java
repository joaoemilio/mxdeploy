package com.mxdeploy.swt.menu.event;

import com.mxdeploy.plugin.event.EventHandler;
import com.mxdeploy.swt.MainShell;

public class ExitEventHandler implements EventHandler{

	public void execute() {
		MainShell.sShell.dispose(); 
	}

}
