package com.mxdeploy.swt.command;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Shell;

import com.mxdeploy.swt.MainShell;

public class CopyCommandProjectServerDialog {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"

	public CopyCommandProjectServerDialog() {
		// TODO Auto-generated constructor stub
	}
	
	private CopyCommandProjectServerComposite composite = null;

	/**
	 * This method initializes sShell
	 */
	private void createSShell() {
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		sShell = new Shell();
		sShell.setText("Copy Command"); 
		sShell.setSize(new Point(478, 391));
		sShell.setLayout(new GridLayout());
		
		composite = new CopyCommandProjectServerComposite(sShell,SWT.NONE);
		composite.setLayoutData(gridData);
		
		Rectangle rec = MainShell.display.getActiveShell().getBounds(); 
		Rectangle shellBounds = sShell.getBounds();
		sShell.setLocation((rec.width - shellBounds.width) / 2, (rec.height - shellBounds.height) / 2);
		
	}

}
