package com.mxdeploy.swt.explorer.event;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TreeItem;

import com.mxdeploy.api.domain.Project;
import com.mxdeploy.api.domain.Server;
import com.mxdeploy.api.domain.Url;
import com.mxdeploy.api.service.ProjectService;
import com.mxdeploy.api.service.ServerService;
import com.mxdeploy.api.service.ServiceException;
import com.mxdeploy.api.service.UrlServices;
import com.mxdeploy.images.Constant;
import com.mxdeploy.swt.MainShell;

public class RemoveProjectSelectedEventHandler {
	
	public void execute(){ 
		Project project = MainShell.getControlPanelHelper().getProjectSelectedInMyProjectTree();
		ServerService serverService = new ServerService();
		UrlServices urlservices = new UrlServices();
		try {
			List<Server> servers = serverService.searchByProject(project.getId());

	    	if( servers!=null && !servers.isEmpty() ){
	            MainShell.sendMessage("You can't remove because there are servers related.");
	    	} else {
	    		List<Url> urls = urlservices.searchByProject(project.getId());
	    		if( urls!=null && !urls.isEmpty() ){
	    			MainShell.sendMessage("You can't remove because there are URLs related.");
	    		} else {
		    		if( MainShell.getControlPanelHelper().confirm("Do you want to remove Project "+project.getAlias()+" ?")){
		    		   ProjectService service = new ProjectService();
		    	  	   service.removerProject(project);
		               TreeItem treeItem = MainShell.getControlPanelHelper().getMyProjectTreeItemSelected();
		               treeItem.removeAll();
		               treeItem.dispose();
		    		}
	    		}
	    	}
		} catch (ServiceException e) { 
			//logger.error(e.getMessage(),e);
			MainShell.sendMessage(Constant.MSN_SEE_LOG_ERROR, SWT.OK | SWT.ICON_ERROR);
			e.printStackTrace();
		}    	
	}	
	

}
