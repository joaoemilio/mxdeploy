package com.mxscript.menu.event;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;

import com.mxdeploy.images.Constant;
import com.mxdeploy.plugin.event.EventHandler;
import com.mxdeploy.swt.MainShell;
import com.mxdeploy.swt.menu.event.WindownApplicationEventHandler;
import com.mxscript.swt.DiscoverComposite;
import com.mxterminal.swt.util.SWTUtils;

public class OpenDiscoverEventHandler  implements EventHandler {

	private String fileName = null;
	
	public OpenDiscoverEventHandler(String fileName){
		this.fileName = fileName;
	}
	
	public void execute() {
		CTabFolder cTabFolder = MainShell.getCTopTabFolder();
		DiscoverComposite discoverComposite = new DiscoverComposite(cTabFolder,SWT.NONE);
		
		
		SWTUtils.createGridLayoutNoMargins(discoverComposite);
		SWTUtils.createGridDataMaximized(discoverComposite);		

		CTabItem tabItem = new CTabItem(cTabFolder,SWT.CLOSE);
		tabItem.setText(fileName); 			

		tabItem.setControl(discoverComposite);
		tabItem.setImage(Constant.IMAGE_JAVABEAN);
		
		cTabFolder.setSelection(tabItem);
		
		(new WindownApplicationEventHandler()).openMaximized();
		
		discoverComposite.loadProperties();
	}
}
