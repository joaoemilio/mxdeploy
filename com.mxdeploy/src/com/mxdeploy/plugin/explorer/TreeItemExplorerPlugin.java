package com.mxdeploy.plugin.explorer;

import java.util.List;

import org.eclipse.swt.widgets.TreeItem;

import com.mxdeploy.plugin.domain.Plugin;
import com.mxdeploy.plugin.domain.TreeItemExplorerDomain;
import com.mxdeploy.plugin.util.URLClassLoaderPlugin;

public class TreeItemExplorerPlugin {

	public void addTreeItemExplorerPlugin(TreeItem parent){
		List<Plugin> pluginList = URLClassLoaderPlugin.getPluginList();
		for(Plugin plugin : pluginList){
			TreeItemExplorerDomain domain = plugin.getTreeItemExplorer();
			if(domain==null)
				continue;

				try {
					if ( domain!=null && domain.getClassAddOn()!=null ){
					   Class clazz = Class.forName(domain.getClassAddOn());
					   TreeItemExplorer treeItemExplorer = (TreeItemExplorer)clazz.newInstance();
					   treeItemExplorer.addTreeItem(parent);
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
