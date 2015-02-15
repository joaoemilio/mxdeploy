package com.mxdeploy.swt.server;

import com.mxdeploy.api.domain.Project;
import com.mxdeploy.api.domain.Server;

public class ServerCRUDFactory {

	public static void openNewServer(Project project){
		ServerEditDialog dialog = new ServerEditDialog();
		dialog.createSShell("New Server");
		dialog.getHelper().setProject(project);
		//dialog.getHelper().setWorkingSetHelper(workingSetHelper);
		dialog.openShell();
	}

	/*
	 * This method will run with Sever List CRUD
	 */
	public static void openEditServer(Project project, Server server ){
		ServerEditDialog dialog = new ServerEditDialog(); 
		dialog.createSShell("Edit Server");
		dialog.getHelper().setProject(project);
		dialog.getHelper().loadFields(server);
		dialog.openShell();
	}	
	
	/*
	 * This method will run with Project Explorer SubMenu
	 */
//	public static void openEditServer(TreeItem serverTreeItem ){
//		Server server = (Server)serverTreeItem.getData();
//		ServerEditDialog dialog = new ServerEditDialog();
//		dialog.createSShell("Edit Server");
//		dialog.getHelper().loadFields(server);
//		dialog.getHelper().setServerTreeItem(serverTreeItem);
//		dialog.openShell();
//	}	
	
	
}
