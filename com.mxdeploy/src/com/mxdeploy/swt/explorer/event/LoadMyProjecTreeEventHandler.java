package com.mxdeploy.swt.explorer.event;

import java.util.Collections;
import java.util.List;

import com.mxdeploy.api.domain.Database;
import com.mxdeploy.api.domain.Project;

public class LoadMyProjecTreeEventHandler {

	public static void execute() {
		Database database = Database.getInstance();
		if ((database.getProjects() != null) && (!database.getProjects().isEmpty())) {
			List<Project> list = database.getProjects();
			Collections.sort(list); 
			for (Project project : list) {
				if (project.isOpen() != null && project.isOpen() == 1) {
					AddInMyProjectTreeItemEventHandler.execute(project);
				}
			}
		}
	} 
	
}
