package com.mxdeploy.swt.explorer.event;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;

import com.mxdeploy.api.domain.Project;
import com.mxdeploy.api.service.ProjectService;
import com.mxdeploy.swt.MainShell;
import com.mxdeploy.swt.explorer.ControlPanelComposite;

public class SearchKeyPressEventHandler {
	
    public static void execute(KeyEvent e){
    	if( e.keyCode == SWT.CR ){ 
			Project project = MainShell.getControlPanelHelper().getProjectSelectedInAllProjectTree();
			if( project!=null){
		    	ProjectService service = new ProjectService();

		    	project = service.obtemProject(project.getId());
		    	project.setIsOpen(new Integer(1));
		    	
				service.update(project);
				AddInMyProjectTreeItemEventHandler.execute(project);
				
				ControlPanelComposite composite = MainShell.getControlPanelHelper().getComposite();
				composite.cTabFolderBotton.setSelection(composite.tabItemMyProject);
			}
     	}
    }
    
    
	

}
