package com.mxdeploy.swt.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.mxdeploy.swt.MainShell;

public class LoginDialog {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"
	private Display display = Display.getDefault(); 
	
	protected LoginComposite composite = null;
	
	/**
	 * This method initializes sShell
	 */
	private void createSShell() {
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
		sShell.setText("Login"); 
		sShell.setLayout(gridLayout);
		sShell.setSize(new Point(451, 274));
		
		composite = new LoginComposite(sShell,SWT.NONE);
		composite.setLayoutData(gridData);
		
		if(MainShell.getMainHelper().intranetID.trim().length()>0){
		   composite.userText.setText(MainShell.getMainHelper().intranetID);
		   composite.passwordText.forceFocus();
		}
		
		Rectangle rec = MainShell.display.getActiveShell().getBounds(); 
		Rectangle shellBounds = sShell.getBounds();
		sShell.setLocation((rec.width - shellBounds.width) / 2, (rec.height - shellBounds.height) / 2);
	}
	
	public int open(){
		createSShell();
		sShell.open();
		Display display = sShell.getDisplay();
		while (!sShell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}		
		return composite.result; 
	}		

}
