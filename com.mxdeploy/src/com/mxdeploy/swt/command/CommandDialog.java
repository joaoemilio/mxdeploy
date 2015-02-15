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

public class CommandDialog {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"
    private CommandComposite composite = null;
	/**
	 * This method initializes sShell
	 */
	public void createSShell() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginHeight = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginWidth = 0;
		GridData gridData = new GridData();
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		sShell = new Shell(MainShell.sShell,SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		sShell.setText("Create Command");
		sShell.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream("/inew_obj.gif")));
		sShell.setLayout(gridLayout);
		sShell.setSize(new Point(316, 271));
		composite =  new CommandComposite(sShell,SWT.NONE);
		composite.setLayoutData(gridData);
		
		Rectangle rec = MainShell.display.getActiveShell().getBounds(); 
		Rectangle shellBounds = sShell.getBounds();
		sShell.setLocation((rec.width - shellBounds.width) / 2, (rec.height - shellBounds.height) / 2);
		
	}
	
	public void openShell(){
		sShell.open();
	}	
	
	public CommandHelper getHelper(){
		return composite.helper; 
	}
	

}
