package com.mxdeploy.swt.workbook;

import org.eclipse.swt.widgets.Shell;

import com.mxdeploy.api.domain.Project;
import com.mxdeploy.swt.MainShell;
import com.mxdeploy.swt.explorer.helper.ControlPanelHelper;

public class WorkbookTabItemHelper {
	//static Logger logger = Logger.getLogger(ServerEditHelper.class);
	
	private WorkbookTabItemComposite composite = null;
	private Project project;
		
	public WorkbookTabItemHelper(WorkbookTabItemComposite composite) {
		this.composite = composite;
	}

	protected void close(){
		Shell shell = (Shell)composite.getParent();
		shell.close();
		shell.dispose();
	}
	
	/**
	 * @return the workingSetHelper
	 */
	public ControlPanelHelper getWorkingSetHelper() {
		return MainShell.getControlPanelHelper();
	}
	
	/**
	 * @return the project
	 */
	public Project getProject() {
		return project;
	}

	/**
	 * @param project the project to set
	 */
	public void setProject(Project project) {
		this.project = project;
	}	

}
