package com.mxdeploy.swt.explorer.popup.event;

import org.eclipse.swt.widgets.TreeItem;

import com.mxdeploy.api.domain.Database;
import com.mxdeploy.api.domain.Project;
import com.mxdeploy.api.service.WorkbookService;
import com.mxdeploy.plugin.event.EventHandler;
import com.mxdeploy.swt.MainShell;

public class RemoveWorkbookEventHandler implements EventHandler {

	public void execute() {
		WorkbookService service = new WorkbookService();
		
		TreeItem treeItemProject = MainShell.getControlPanelHelper().getTreeItemProjectSelected(MainShell.getControlPanelHelper().getMyProjectTreeItemSelected());
		Project project = (Project)treeItemProject.getData();
		String workbookPath = Database.getProjectPath()+"/"+project.getAlias()+"/"+Database.WORKBOOK_FILE_NAME;
		service.remove(workbookPath);
		
		for( int index=0; index < treeItemProject.getItemCount(); index++){
			TreeItem treeItem = treeItemProject.getItem(index);
			if( treeItem.getText().equals("deploy")){
				treeItem.removeAll();
			}
		}
	}

}