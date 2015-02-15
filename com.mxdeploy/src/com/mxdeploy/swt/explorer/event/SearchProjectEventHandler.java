package com.mxdeploy.swt.explorer.event;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;

import com.mxdeploy.api.domain.Database;
import com.mxdeploy.api.domain.Project;
import com.mxdeploy.swt.MainShell;
import com.mxdeploy.swt.explorer.ControlPanelComposite;

public class SearchProjectEventHandler {

    @SuppressWarnings("unchecked")
	public static void execute(ModifyEvent e){
    	Text seachText = (Text)e.widget;
    	ControlPanelComposite composite = MainShell.getControlPanelHelper().getComposite();    	

    	Database database = Database.getInstance();
		List<Project> projects = database.getProjects();
		if( (database.getProjects()!=null)&&(!projects.isEmpty()) ){
    	    projects = new ArrayList();
    	    TreeItem[] item = composite.allProjectTree.getItems();
	        for(int i=0; item.length>i;i++){
    		    Project project = (Project)item[i].getData();
    		    projects.add(project);
	        }    	   
    	}
 
    	composite.allProjectTree.removeAll();

    	for(Project project : projects){
    		if(project.getAlias().toUpperCase().startsWith(seachText.getText().toUpperCase())){
    			AddInAllProjectTreeItemEventHandler.execute(project);
    		}
    	}
    	
    	if(composite.allProjectTree.getItemCount()>0){
    		composite.allProjectTree.setSelection(composite.allProjectTree.getItem(0));
    	}
    	
    } 
	
}
