package com.mxdeploy.swt.menu.event;

import com.mxdeploy.SocksConfig;
import com.mxdeploy.images.Constant;
import com.mxdeploy.plugin.event.EventHandler;
import com.mxdeploy.plugin.menu.SubMenu;

public class SelectSocksEventHandler implements EventHandler {

	private SocksConfig socksConfig;
	private SubMenu subMenu;
	
	public SelectSocksEventHandler() {
		
	}
	
	public SelectSocksEventHandler(SocksConfig cfg, SubMenu subMenu) {
		this.socksConfig = cfg;
		this.subMenu = subMenu;
	}
	
	public void execute() {
	}

}
