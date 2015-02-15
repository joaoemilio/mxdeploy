package com.mxdeploy.swt.menu.event;

import org.eclipse.swt.widgets.Tree;

import com.mxdeploy.plugin.event.EventHandler;
import com.mxdeploy.swt.MainShell;
import com.mxterminal.swt.view.CentralTabFolderView;
import com.mxterminal.swt.view.CTopTabFolder;
import com.mxterminal.swt.view.CommandComposite;
import com.mxterminal.swt.view.CentralComposite;
import com.mxterminal.swt.view.helper.TerminalHelper;

public class FocusTerminalEventHandler implements EventHandler{

	public void execute() {
		CTopTabFolder tabFolderServer = MainShell.getCTopTabFolder();
  		if( tabFolderServer.getItemCount()>0 ){
  			
  		  if(tabFolderServer.getServerHelper()==null){
  				return;
  		  }
  			
  		   CentralTabFolderView cTabFolder = tabFolderServer.getServerHelper().getProcedureTreeComposite().getCTabFolder();
	       if(cTabFolder.getSashForm().getMaximizedControl()!=null ||  cTabFolder.getItemCount() > 0){
	    	  if(cTabFolder.getSelection()!=null){
		    	  if(cTabFolder.getSelection().getControl() instanceof CommandComposite ){
		    		  CommandComposite commandComposite = (CommandComposite)cTabFolder.getSelection().getControl();
		    		  if(!commandComposite.getTextArea().isFocusControl()){
		    			  commandComposite.getTextArea().setFocus();
		    			  return;
		    		  }
		    	  } else if(cTabFolder.getSelection().getControl() instanceof Tree ){
		    		  if(!cTabFolder.getSelection().getControl().isFocusControl()){
		    			  cTabFolder.getSelection().getControl().setFocus();
		    			  return;
		    		  }
		    	  }
	    	  }
	       }
	      
		   CentralComposite serverComposite = (CentralComposite)tabFolderServer.getSelection().getControl();
		   TerminalHelper consoleHelper2 = (TerminalHelper)serverComposite.getData();
	       if(consoleHelper2.getSSHInteractiveClient()!=null){
	    	  consoleHelper2.getSSHInteractiveClient().getTerminalWin().requestFocus();
	       }
  		}
	}

}
