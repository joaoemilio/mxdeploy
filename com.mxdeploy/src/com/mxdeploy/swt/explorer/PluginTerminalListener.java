package com.mxdeploy.swt.explorer;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Display;

import com.mxdeploy.api.domain.Server;
import com.mxdeploy.plugin.PluginListener;
import com.mxdeploy.swt.MainShell;
import com.mxterminal.ssh.InteractiveClientSSH;
import com.mxterminal.swt.TerminalFace;
import com.mxterminal.swt.view.CentralComposite;

public class PluginTerminalListener implements PluginListener {

	protected Server server;
	private CTabItem currentItem;
	
	public PluginTerminalListener(Server server){
		this.server = server;
	}
	
	public void sendMessage(String message) {
  		final CTabFolder tabFolderServer = MainShell.getCTopTabFolder();
  		final Server server = this.server;

  		if(tabFolderServer.getItemCount()==0){
  			return;
  		}

  		final int count = tabFolderServer.getItemCount();
		Thread t = new Thread() {
			public void run() {
				Display.getDefault().syncExec(new Runnable(){						
					public void run() {
			      		for(int i=0; i < count; i++){
			      			currentItem = tabFolderServer.getItem(i);
				      			tabFolderServer.setSelection(currentItem);
				      		    TerminalFace face = null;
				      		    if( currentItem.getControl() instanceof CentralComposite ){
			   	    	    	  CentralComposite serverComposite = (CentralComposite)tabFolderServer.getSelection().getControl();
			   	                  face = serverComposite.getProcedureSashFormComposite().getTerminalComposite().getTermianlFace();
			   	                  InteractiveClientSSH sshClient = face.getTerminalComposite().getSSHInteractiveClient();
			   	                  if(sshClient == null){
			   	                	  continue;
			   	                  }
			   	                  
			   	                  String hostname = sshClient.getHost(); 
			   	                  if(hostname == null || hostname.equals("")){
			   	                	  continue;
			   	                  }
			   	                  
			   	                  if(hostname.equals(server.getHostname())){
			   	                	  
			   	                  }
				      		    } else {
				      		    	continue;
				      		    }
	    				}
					}
				});
			}
		};
		t.start(); 				
	}

	public void setLogLevel(int logLevel) {
		// TODO Auto-generated method stub
		
	}

}
