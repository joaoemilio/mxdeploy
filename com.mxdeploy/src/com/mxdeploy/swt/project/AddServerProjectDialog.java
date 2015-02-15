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
import com.mxdeploy.swt.project.helper.AddServerProjectHelper;

public class AddServerProjectDialog {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"

	private ServerProjectComposite addServerProjectComposite = null;
	
	
	
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
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginHeight = 0;
		sShell = new Shell(MainShell.sShell,SWT.APPLICATION_MODAL );
		sShell.setText(title);
		//sShell.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream("/enabled/wiz_new_server_project.gif")));
		sShell.setLayout(gridLayout);
		sShell.setSize(new Point(566, 470));
		addServerProjectComposite = new ServerProjectComposite(sShell,SWT.NONE);
		addServerProjectComposite.setLayoutData(gridData);
		
		Rectangle rec = MainShell.display.getActiveShell().getBounds(); 
		Rectangle shellBounds = sShell.getBounds();
		sShell.setLocation((rec.width - shellBounds.width) / 2, (rec.height - shellBounds.height) / 2);

	}

	public void openShell(){
		sShell.open();
	}	
	
	
	
	public AddServerProjectHelper getHelper(){
		return addServerProjectComposite.helper;
	}		
	
}
