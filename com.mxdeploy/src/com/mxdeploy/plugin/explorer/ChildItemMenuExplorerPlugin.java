package com.mxdeploy.plugin.explorer;

import java.util.List;

import com.mxdeploy.plugin.domain.MenuExplorer;
import com.mxdeploy.plugin.domain.Plugin;
import com.mxdeploy.plugin.menu.ChildrenMenuExplorer;
import com.mxdeploy.plugin.menu.MenuBT;
import com.mxdeploy.plugin.menu.SubMenu;
import com.mxdeploy.plugin.util.URLClassLoaderPlugin;

public class ChildItemMenuExplorerPlugin {

	public static void addSubItemMenuExplorer(MenuBT menuBt){
		
		List<Plugin> pluginList = URLClassLoaderPlugin.getPluginList();
		for(Plugin plugin : pluginList){
			MenuExplorer menuExplorer = plugin.getChildMenuExplorer();
			if(menuExplorer==null)
				continue;

				try {
					if ( menuExplorer!=null && menuExplorer.getClassAddOn()!=null ){
					   Class clazz = Class.forName(menuExplorer.getClassAddOn());
					   ChildrenMenuExplorer childrenMenuExplorer = (ChildrenMenuExplorer)clazz.newInstance();
					   MenuBT menuBt2 = childrenMenuExplorer.addChildMenu();
					   for(SubMenu subMenu : menuBt2.getSubMenuList()){
						   menuBt.addSubMenu(subMenu);
					   }
					}
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
