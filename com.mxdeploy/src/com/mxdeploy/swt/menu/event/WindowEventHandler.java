package com.mxdeploy.swt.menu.event;

import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import com.mxdeploy.plugin.event.EventHandler;
import com.mxdeploy.swt.MainShell;
import com.mxdeploy.swt.menu.MenuBarUtils;
import com.mxterminal.swt.view.CTopTabFolder;

public class WindowEventHandler implements EventHandler{

	public void execute() {
  		  
		CTopTabFolder tabFolderServer = MainShell.getCTopTabFolder();
		Menu menuBar = MenuBarUtils.getMenuBar();
		
		for(int i=0; i < menuBar.getItemCount(); i++){
			MenuItem menuItem = menuBar.getItem(i);
			if( menuItem.getText().contains("Window") ){
				for(int j=0; j < menuItem.getMenu().getItemCount(); j++){
					MenuItem menuSubItem = menuItem.getMenu().getItem(j);
					if(menuSubItem.getText().contains("Tab &Views - Maximize") ){
						if(tabFolderServer.getItemCount()==0){
				  			menuSubItem.setEnabled(false);
				  		} else {
				  			menuSubItem.setEnabled(true);
				  		}
					}
				}
			}
		}  
		
	}

}
