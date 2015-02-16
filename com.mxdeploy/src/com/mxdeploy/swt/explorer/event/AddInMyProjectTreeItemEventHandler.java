package com.mxdeploy.swt.explorer.event;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TreeItem;

import com.mxdeploy.api.domain.Project;
import com.mxdeploy.api.domain.Server;
import com.mxdeploy.images.Constant;
import com.mxdeploy.plugin.explorer.TreeItemExplorerPlugin;
import com.mxdeploy.swt.MainShell;
import com.mxdeploy.swt.explorer.ControlPanelComposite;

public class AddInMyProjectTreeItemEventHandler {
	
    public static void execute(Project project){
    	ControlPanelComposite composite = MainShell.getControlPanelHelper().getComposite();
    	
		TreeItem treeItemProject = new TreeItem(composite.myProjectTree,SWT.NULL);
		treeItemProject.setText(project.getAlias());
		treeItemProject.setImage(Constant.IMAGE_APPLICATION);
		
		treeItemProject.setData(project);
						
		TreeItemExplorerPlugin plugin = new TreeItemExplorerPlugin();
		plugin.addTreeItemExplorerPlugin(treeItemProject);
				
		List<Server> listServer = project.getServers();
		if(listServer !=null && !listServer.isEmpty()){ 
			for(Server server : listServer){
				TreeItem treeItemServer = new TreeItem(treeItemProject,SWT.NONE);
				treeItemServer.setText(server.getName()); 
				treeItemServer.setImage(Constant.IMAGE_SERVER_STOPPED);
				treeItemServer.setData(server); 
			}
		}
		
	}
	

}
