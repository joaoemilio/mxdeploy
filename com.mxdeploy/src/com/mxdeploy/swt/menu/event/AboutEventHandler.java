package com.mxdeploy.swt.menu.event;

import com.mxdeploy.plugin.event.EventHandler;
import com.mxdeploy.swt.dialogs.AboutDialog;

public class AboutEventHandler implements EventHandler {

	public void execute() {
		  AboutDialog dialog = new AboutDialog();
  		  dialog.createSShell();
  	  	  dialog.openShell();
	}

}
