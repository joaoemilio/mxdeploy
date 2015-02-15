package com.mxdeploy.swt.explorer.popup.event;


import com.mxdeploy.plugin.event.EventHandler;
import com.mxdeploy.swt.MainShell;
import com.mxdeploy.swt.workbook.WorkbookDialog;

public class NewWorkbookEventHandler implements EventHandler {

	public void execute() {
		WorkbookDialog dialog = new WorkbookDialog();
		dialog.createSShell("New Workbook");
		dialog.getHelper().setProject(MainShell.getControlPanelHelper().getProjectSelectedInMyProjectTree());
		dialog.openShell();
	}

}
