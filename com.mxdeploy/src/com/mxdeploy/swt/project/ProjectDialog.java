package com.mxdeploy.swt.project;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.mxdeploy.swt.MainShell;
import com.mxdeploy.swt.project.helper.ProjectHelper;

public class ProjectDialog {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"

	private ProjectComposite projectComposite = null;
	/**
	 * This method initializes sShell
	 */
	public void createSShell(String title) {
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginWidth = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginHeight = 0;
		sShell = new Shell(MainShell.sShell,SWT.APPLICATION_MODAL );
		sShell.setText(title);
		//sShell.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream("/enabled/connectionFolder.gif")));
		sShell.setLayout(gridLayout);
		sShell.setSize(new Point(391, 416));
		projectComposite = new ProjectComposite(sShell, SWT.NONE);
		projectComposite.setLayoutData(gridData);
		
		Rectangle rec = MainShell.display.getActiveShell().getBounds(); 
		Rectangle shellBounds = sShell.getBounds();
		sShell.setLocation((rec.width - shellBounds.width) / 2, (rec.height - shellBounds.height) / 2);
		
	}
	
	public void openShell(){
		sShell.open();
	}	
	
	public ProjectHelper getHelper(){
		return projectComposite.helper;
	}	

}
