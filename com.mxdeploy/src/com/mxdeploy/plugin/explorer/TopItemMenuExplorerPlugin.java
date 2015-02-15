package com.mxdeploy.plugin.explorer;

import java.util.List;

import com.mxdeploy.plugin.domain.MenuExplorer;
import com.mxdeploy.plugin.domain.Plugin;
import com.mxdeploy.plugin.menu.MenuAddon;
import com.mxdeploy.plugin.menu.MenuBT;
import com.mxdeploy.plugin.menu.SubMenu;
import com.mxdeploy.plugin.util.URLClassLoaderPlugin;

public class TopItemMenuExplorerPlugin {
	
	public static void addTopItemMenuExplorer(MenuBT menuBt){
		
		List<Plugin> pluginList = URLClassLoaderPlugin.getPluginList();
		for(Plugin plugin : pluginList){
			MenuExplorer menuExplorer = plugin.getTopMenuExplorer();
			if(menuExplorer==null)
				continue; 

				try {
					Class clazz = Class.forName(menuExplorer.getClassAddOn());
					MenuAddon menuAddon = (MenuAddon)clazz.newInstance();
					SubMenu subMenu = menuAddon.addOn();
					menuBt.addSubMenu(subMenu);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}

		}
		
	}
	
}
