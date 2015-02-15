package com.mxdeploy.plugin.menu;

import java.util.List;

import com.mxdeploy.plugin.domain.Menu;
import com.mxdeploy.plugin.domain.Plugin;
import com.mxdeploy.plugin.util.URLClassLoaderPlugin;

public class MenuPlugin {
	
	public static void addSubMenuCustom(MenuBT menuBt){
		
		List<Plugin> pluginList = URLClassLoaderPlugin.getPluginList();
		for(Plugin plugin : pluginList){
			Menu menu = plugin.getMenu();
			if(menu==null)
				continue;
			if(menu.getType().equals(Menu.SUBMENU_CUSTOM)){
				try {
					Class clazz = Class.forName(menu.getClassAddOn());
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
	
	public static void addSubMenuHelp(HelpMenu helpMenu){
		List<Plugin> pluginList = URLClassLoaderPlugin.getPluginList();
		for(Plugin plugin : pluginList){
			Menu menu = plugin.getMenu();
			if(menu==null)
				continue;
			if(menu.getType().equals(Menu.SUBMENU_HELP)){
				try {
					Class clazz = Class.forName(menu.getClassAddOn());
					MenuAddon menuAddon = (MenuAddon)clazz.newInstance();
					SubMenu subMenu = menuAddon.addOn();
					helpMenu.addSubMenu(subMenu);
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

}
