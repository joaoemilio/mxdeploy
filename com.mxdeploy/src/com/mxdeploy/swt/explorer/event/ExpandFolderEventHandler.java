package com.mxdeploy.swt.explorer.event;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TreeItem;

import com.mxdeploy.images.Constant;
import com.mxdeploy.swt.MainShell;

public class ExpandFolderEventHandler {
	
	public static void execute(){
		final TreeItem treeItemSeleted = MainShell.getControlPanelHelper().getMyProjectTreeItemSelected();
		//String path = "/";
		if(!treeItemSeleted.getText().equals("/")){
			//SftpFile sftpFileSeleted = (SftpFile)treeItemSeleted.getData();
			//path = sftpFileSeleted.getAbsolutePath();			
		}

		treeItemSeleted.removeAll();
		if(treeItemSeleted==null){  
			return;
		}
		try {
			Cursor cursorWait = new Cursor(MainShell.display,SWT.CURSOR_WAIT);
			MainShell.sShell.setCursor(cursorWait);
			
			Display.getDefault().syncExec(new Runnable(){
				public void run(){
					treeItemSeleted.setImage(Constant.IMAGE_FOLDER_OPENED);
				}
			});	
		} finally {
			Cursor cursorArrow = new Cursor(MainShell.display,SWT.CURSOR_ARROW);
			MainShell.sShell.setCursor(cursorArrow); 			
		}
		
    	
    }
	

}
