package com.mxdeploy.swt.command;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.mxdeploy.swt.MainShell;

public class CommandByServerDialog {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"

	private CommandByServerComposite composite = null;
	/**
	 * This method initializes sShell
	 */
	public void createSShell(String title) {
		GridData gridData = new GridData();
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		sShell = new Shell(MainShell.sShell, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		sShell.setText(title);
		sShell.setLayout(new GridLayout());
		sShell.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream("/enabled/inew_obj.gif")));
		sShell.setSize(new Point(787, 601)); 
		composite = new CommandByServerComposite(sShell,SWT.NONE);
		composite.setLayoutData(gridData);
		
		Rectangle rec = MainShell.display.getActiveShell().getBounds(); 
		Rectangle shellBounds = sShell.getBounds();
		sShell.setLocation((rec.width - shellBounds.width) / 2, (rec.height - shellBounds.height) / 2);
		
	}
	
	public void openShell(){
		sShell.open();
	}	
	
	public CommandByServerHelper getHelper(){
		return composite.helper; 
	}
	

}
