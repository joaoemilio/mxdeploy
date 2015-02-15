package com.mxdeploy.swt.explorer.event;

import java.io.File;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TreeItem;

import com.mxdeploy.api.domain.Database;
import com.mxdeploy.api.domain.Project;
import com.mxdeploy.api.domain.Server;
import com.mxdeploy.api.service.WorkbookService;
import com.mxdeploy.images.Constant;
import com.mxdeploy.plugin.explorer.TreeItemExplorerPlugin;
import com.mxdeploy.swt.MainShell;
import com.mxdeploy.swt.explorer.ControlPanelComposite;
import com.wds.bean.WorkBook;

public class AddInMyProjectTreeItemEventHandler {
	
    public static void execute(Project project){
    	ControlPanelComposite composite = MainShell.getControlPanelHelper().getComposite();
    	
		TreeItem treeItemProject = new TreeItem(composite.myProjectTree,SWT.NULL);
		treeItemProject.setText(project.getAlias());
		treeItemProject.setImage(Constant.IMAGE_APPLICATION);
		
		treeItemProject.setData(project);
				
		String PROJECT_PATH = Database.getProjectPath()+"/"+project.getAlias();		
		File f = new File(PROJECT_PATH+"/"+Database.WORKBOOK_FILE_NAME);
		if( f.exists() ){
			TreeItem workbookTreeItemXML = new TreeItem(treeItemProject,SWT.NONE);
			workbookTreeItemXML.setText("WorkBook");
			workbookTreeItemXML.setImage(Constant.IMAGE_WORKBOOK );
			
			WorkbookService service = new WorkbookService();
			WorkBook workbook = service.loadWorkBook(PROJECT_PATH+"/"+Database.WORKBOOK_FILE_NAME);
			workbookTreeItemXML.setData(workbook);
		}
		
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
