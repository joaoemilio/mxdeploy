package com.mxdeploy.swt.menu.event;

import com.mxdeploy.plugin.event.EventHandler;
import com.mxdeploy.swt.MainShell;
import com.mxterminal.swt.view.CentralTabFolderView;
import com.mxterminal.swt.view.CTopTabFolder;
import com.mxterminal.swt.view.CentralComposite;

public class WindowConsoleApplicationEventHandler implements EventHandler {

	public void execute() {
		CTopTabFolder tabFolderServer = MainShell.getCTopTabFolder();
		
  		if(tabFolderServer.getItemCount()<=0){
  			return;
  		}
  		if(tabFolderServer.getServerHelper().getTerminalHelper().getSSHInteractiveClient()==null){
  		  return;	
  		} else if (tabFolderServer.getServerHelper().getTerminalHelper().getSSHInteractiveClient().isAuthenticated()){
  			if(tabFolderServer.getServerHelper().getProcedureTreeComposite().getCTabFolder().getItemCount()==0){
      		   tabFolderServer.getServerHelper().getViewHelper().getServerCommandView();	      				
  			}

    	    if( tabFolderServer.getSelection().getControl() instanceof CentralComposite ){
    		  CentralComposite serverComposite = (CentralComposite)tabFolderServer.getSelection().getControl();
    		  CentralTabFolderView cTabFolderConsole = serverComposite.getProcedureSashFormComposite().getViewComposite().getCTabFolder();
    		  if(cTabFolderConsole.getSashForm().getMaximizedControl()==null){
    			 cTabFolderConsole.minimize(); 
	    	  } else {
	    		 cTabFolderConsole.restore();
	    	  }
    	    }
  		}
	}

}
