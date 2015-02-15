package com.mxdeploy.swt.menu.event;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;

import com.mxdeploy.plugin.event.EventHandler;
import com.mxdeploy.swt.MainShell;
import com.mxterminal.swt.TerminalFace;
import com.mxterminal.swt.view.CentralComposite;
import com.mxterminal.swt.view.helper.TerminalHelper;

public class NextTerminalEventHandler implements EventHandler{

	public void execute() {
  		CTabFolder tabFolderServer = MainShell.getCTopTabFolder();	
  		if(tabFolderServer.getItemCount()==0)
  			return;
  		CTabItem item2 = null;
  		if( (tabFolderServer.getSelectionIndex()+1)==tabFolderServer.getItemCount()){
  			item2 = tabFolderServer.getItem(0);
  			tabFolderServer.setSelection(item2);
  		} else {
  			item2 = tabFolderServer.getItem(tabFolderServer.getSelectionIndex()+1);
  			tabFolderServer.setSelection(item2);
  		}
        if( tabFolderServer.getSelection().getControl() instanceof TerminalFace ){
		  TerminalFace face = (TerminalFace)tabFolderServer.getSelection().getControl();
	      if(face.getTerminalWin()!=null){
	    	  face.getTerminalWin().requestFocus();
	          //logger.debug("TerminalFace:termianlFace.getTerminalWin().requestFocus();");
	      }
	    } else if( tabFolderServer.getSelection().getControl() instanceof CentralComposite ){
		  CentralComposite serverComposite = (CentralComposite)tabFolderServer.getSelection().getControl();
		  TerminalHelper consoleHelper2 = (TerminalHelper)serverComposite.getData();
	      if(consoleHelper2.getSSHInteractiveClient()!=null){
	    	  consoleHelper2.getSSHInteractiveClient().getTerminalWin().requestFocus();
	          //logger.debug("ServerComposite:termianlFace.getTerminalWin().requestFocus();");
	      }
	    }
	}

}
