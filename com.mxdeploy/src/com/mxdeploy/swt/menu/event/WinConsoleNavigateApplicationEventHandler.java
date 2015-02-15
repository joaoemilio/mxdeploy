package com.mxdeploy.swt.menu.event;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;

import com.mxdeploy.plugin.event.EventHandler;
import com.mxdeploy.swt.MainShell;
import com.mxterminal.swt.view.CentralTabFolderView;
import com.mxterminal.swt.view.CTopTabFolder;
import com.mxterminal.swt.view.CentralComposite;

public class WinConsoleNavigateApplicationEventHandler implements EventHandler {

	public void execute() {
		CTopTabFolder tabFolderServer = MainShell.getCTopTabFolder();
  		if(tabFolderServer.getItemCount()<=0){
  			return;
  		}
  		if(tabFolderServer.getServerHelper().getTerminalHelper().getSSHInteractiveClient()!=null){
  			if(tabFolderServer.getServerHelper().getProcedureTreeComposite().getCTabFolder().getItemCount()==0){
      		   tabFolderServer.getServerHelper().getViewHelper().getServerCommandView();	
	    	    if( tabFolderServer.getSelection().getControl() instanceof CentralComposite ){
	    		    CentralComposite serverComposite = (CentralComposite)tabFolderServer.getSelection().getControl();
	    		    CentralTabFolderView cTabFolderConsole = serverComposite.getProcedureSashFormComposite().getViewComposite().getCTabFolder();
	    		    if(cTabFolderConsole.getSashForm().getMaximizedControl()!=null){
		    		   cTabFolderConsole.restore();
		    	    }
	    	    }
  			} else {
  				CTabFolder cTabFolder = tabFolderServer.getServerHelper().getProcedureTreeComposite().getCTabFolder();
	      		CTabItem item2 = null;
	      		if( (cTabFolder.getSelectionIndex()+1)==cTabFolder.getItemCount()){
	      			item2 = cTabFolder.getItem(0);
	      			cTabFolder.setSelection(item2);
	      		} else {
	      			item2 = cTabFolder.getItem(cTabFolder.getSelectionIndex()+1);
	      			cTabFolder.setSelection(item2);
	      		}
	      		item2.getControl().setFocus();
  			}
  		}
	}

}
