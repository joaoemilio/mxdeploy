package com.mxdeploy.swt.menu.event;

import org.eclipse.swt.custom.CTabItem;

import com.mxdeploy.plugin.event.EventHandler;
import com.mxdeploy.swt.MainShell;
import com.mxterminal.swt.view.CTopTabFolder;

public class CloseAllTerminalsEventHandler implements EventHandler {

	public void execute() {
  		CTopTabFolder tabFolderServer = MainShell.getCTopTabFolder();
  		if(tabFolderServer.getItemCount()>0){
  			CTabItem[] item = tabFolderServer.getItems();
  			for(int i=0;item.length>i;i++){
      		   tabFolderServer.close(item[i],tabFolderServer);
      		   item[i].dispose();
  			}
  		}
	}

}
