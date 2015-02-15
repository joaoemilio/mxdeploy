package com.mxdeploy.swt.explorer.event;

import java.util.List;

import com.mxdeploy.api.domain.Database;
import com.mxdeploy.api.domain.Project;
import com.mxdeploy.swt.MainShell;
import com.mxdeploy.swt.explorer.ControlPanelComposite;

public class LoadAllProjectTreeEventHandler {

	public static void execute(){
		  ControlPanelComposite composite = MainShell.getControlPanelHelper().getComposite();
	      composite.allProjectTree.removeAll();
	      Database database = Database.getInstance();
	      if( (database.getProjects()!=null)&&(!database.getProjects().isEmpty()) ){
	    	  List<Project> list = database.getProjects();
		      for(Project next : list){
		    	  if( next.isOpen()==null || next.isOpen().equals(0))
		    	  	  AddInAllProjectTreeItemEventHandler.execute(next);
		      }
	      }
	}
	
}
