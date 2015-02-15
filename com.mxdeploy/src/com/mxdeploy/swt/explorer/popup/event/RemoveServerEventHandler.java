package com.mxdeploy.swt.explorer.popup.event;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TreeItem;

import com.mxdeploy.api.domain.Command;
import com.mxdeploy.api.domain.Project;
import com.mxdeploy.api.domain.ProjectServer;
import com.mxdeploy.api.domain.Server;
import com.mxdeploy.api.service.CommandService;
import com.mxdeploy.api.service.ProjectService;
import com.mxdeploy.api.service.ServiceException;
import com.mxdeploy.images.Constant;
import com.mxdeploy.plugin.event.EventHandler;
import com.mxdeploy.swt.MainShell;

public class RemoveServerEventHandler implements EventHandler{

	public void execute() {
		Server server = (Server)MainShell.getControlPanelHelper().getMyProjectTreeItemSelected().getData();
		Project project = MainShell.getControlPanelHelper().getProjectSelectedInMyProjectTree();
		
		CommandService commandService = new CommandService();
		try {
			List<Command> commands = commandService.searchCommandByProjectAndServer(server, project);
		
	    	if( commands!=null && !commands.isEmpty() ){
	            MainShell.sendMessage("You can't remove because there are commands related.");
	    	} else {
	    		if(MainShell.getControlPanelHelper().confirm("Do you want to remove Server "+server.getName()+" from Project "+project.getAlias()+"?")){
	    		   //getPropertyHelper().removeProperty(server);
	    		   
	    		   ProjectService projectService = new ProjectService();
	    		   ProjectServer projectServer = new ProjectServer();
	    		   projectServer.setIdProject(project.getId());
	    		   projectServer.setIdServer(server.getId());
	    		   
	    		   projectService.removerProjectServer(projectServer);
	               TreeItem treeItem = MainShell.getControlPanelHelper().getMyProjectTreeItemSelected(); 
	               treeItem.removeAll();
	               treeItem.dispose();
	    		}
	    	}  
		} catch (ServiceException e) {
			//logger.error(e.getMessage(),e);
			MainShell.sendMessage(Constant.MSN_SEE_LOG_ERROR, SWT.OK | SWT.ICON_ERROR);
			e.printStackTrace();
		}  
	}

}
