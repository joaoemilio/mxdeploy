package com.mxscript.menu;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;

import xml.module;

import com.mxdeploy.images.Constant;
import com.mxdeploy.swt.MainShell;
import com.mxdeploy.swt.menu.event.WindownApplicationEventHandler;
import com.mxscript.swt.DiscoverComposite;
import com.mxterminal.swt.util.SWTUtils;

public class OpenModuleEventHandler {
	private module __module__ = null;
	
	public OpenModuleEventHandler(module __module__){
		this.__module__ = __module__;
	}
	
	public void execute() {
		CTabFolder cTabFolder = MainShell.getCTopTabFolder();
		DiscoverComposite discoverComposite = new DiscoverComposite(cTabFolder,SWT.NONE);

		SWTUtils.createGridLayoutNoMargins(discoverComposite);
		SWTUtils.createGridDataMaximized(discoverComposite);		

		CTabItem tabItem = new CTabItem(cTabFolder,SWT.CLOSE);
		tabItem.setText(__module__.getName()); 	
		tabItem.setData(this.__module__);

		tabItem.setControl(discoverComposite);
		tabItem.setImage(Constant.IMAGE_MODULE_ITEM);
		
		cTabFolder.setSelection(tabItem);
		
		(new WindownApplicationEventHandler()).openMaximized();
		
		discoverComposite.loadProperties();
	}
}
