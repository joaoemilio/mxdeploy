package com.mxdeploy.swt.menu.event;

import com.mxdeploy.plugin.event.EventHandler;
import com.mxdeploy.swt.MainShell;
import com.mxterminal.swt.view.CTopTabFolder;

public class CloseTerminalEventHandler implements EventHandler{

	public void execute() {
  		CTopTabFolder tabFolderServer = MainShell.getCTopTabFolder();
  		if(tabFolderServer.getItemCount()>0){
      		tabFolderServer.close(tabFolderServer.getSelection(),tabFolderServer);
      		tabFolderServer.getSelection().dispose();
  		}
	}

}
