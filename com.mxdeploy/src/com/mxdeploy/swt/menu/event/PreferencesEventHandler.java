package com.mxdeploy.swt.menu.event;

import com.mxdeploy.plugin.event.EventHandler;
import com.mxdeploy.swt.preferences.PreferencesDialog;

public class PreferencesEventHandler implements EventHandler {

	public void execute() {
		PreferencesDialog dialog = new PreferencesDialog();
		//dialog.createSShell();
		//dialog.open();
	}

}
