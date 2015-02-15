package com.mxdeploy.swt.command;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Shell;

import com.mxdeploy.swt.MainShell;

public class AddCommandProjectServerDialog {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"
    private AddCommandprojectServerComposite2 composite = null;
    
    
	/**
	 * This method initializes sShell
	 */ 
	public void createSShell() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0; 
		gridLayout.marginHeight = 0;
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		sShell = new Shell(MainShell.sShell, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		sShell.setLayout(gridLayout);
		sShell.setSize(new Point(608, 472));
		sShell.setText("Add/Edit Project-Server");
		sShell.setLayoutData(gridData);
		
		composite = new AddCommandprojectServerComposite2(sShell,SWT.NONE); 
		composite.setLayoutData(gridData);
		composite.setLayout(gridLayout);
		
		Rectangle rec = MainShell.display.getActiveShell().getBounds(); 
		Rectangle shellBounds = sShell.getBounds();
		sShell.setLocation((rec.width - shellBounds.width) / 2, (rec.height - shellBounds.height) / 2);
		
	}
	
	public AddCommandProjectServerHelper getHelper(){
		return composite.helper;
	}
	
	public void openShell(){
		sShell.open();
	}	
	


}
