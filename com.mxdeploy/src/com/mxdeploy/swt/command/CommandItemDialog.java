package com.mxdeploy.swt.command;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.mxdeploy.swt.MainShell;

public class CommandItemDialog {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"

	private CommandItemComposite composite = null;
	/**
	 * This method initializes sShell
	 */
	public void createSShell() {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginHeight = 0;
		sShell = new Shell(MainShell.sShell,SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		sShell.setText("Add/Edit Command Item");
		sShell.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream("/enabled/extension_obj.gif")));
		sShell.setLayout(gridLayout);
		sShell.setSize(new Point(900, 450));
        composite = new CommandItemComposite(sShell,SWT.NONE);
        composite.setLayoutData(gridData);
        
//		Rectangle rec = sShell.getDisplay().getClientArea();
//		Rectangle shellBounds = sShell.getBounds();
//		sShell.setLocation((rec.width - shellBounds.width) / 2, (rec.height - shellBounds.height) / 2);
        
	}
	
	public void openShell(){
		sShell.open(); 
	}	
	
	public CommandItemHelper getHelper(){
		return composite.helper; 
	}
	


}
