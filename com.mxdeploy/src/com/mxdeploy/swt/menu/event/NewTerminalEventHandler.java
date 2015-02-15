package com.mxdeploy.swt.menu.event;

import org.eclipse.swt.custom.CTabItem;

import com.mxdeploy.plugin.event.EventHandler;
import com.mxdeploy.swt.MainShell;
import com.mxterminal.swt.view.CTopTabFolder;
import com.mxterminal.swt.view.CentralTabItemFactory;

public class NewTerminalEventHandler implements EventHandler {

	public void execute() {
		
		CTopTabFolder tabFolderServer = MainShell.getCTopTabFolder();
		CTabItem serverTabItem = CentralTabItemFactory.createTabItemServer(null, null, tabFolderServer);
		
		tabFolderServer.setSelection(serverTabItem);
		
		MainShell.getToolBarViewHelper().focusCombo();
		
		//ServerComposite serverComposite = (ServerComposite) tabFolderServer.getSelection().getControl();
		//final TerminalFace face = serverComposite.getProcedureSashFormComposite().getConsoleComposite().getTermianlFace();
	}

}
