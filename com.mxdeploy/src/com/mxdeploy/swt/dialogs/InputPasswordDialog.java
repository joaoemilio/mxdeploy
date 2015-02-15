package com.mxdeploy.swt.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.mxdeploy.swt.MainShell;

public class InputPasswordDialog {
  
	private Shell sShell = null;
	
	private PasswordComposite passwordComposite = null;
	/**
	 * This method initializes sShell
	 */
	private void createSShell(String hostname) {
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginHeight = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginWidth = 0;

		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		
		sShell = new Shell(MainShell.sShell, SWT.CLOSE | SWT.APPLICATION_MODAL);
		sShell.setLayout(gridLayout); 
		sShell.setText(hostname+" - Password...");
		sShell.setSize(200, 120);
		passwordComposite = new PasswordComposite(sShell, SWT.NONE |SWT.CENTER);
		passwordComposite.setLayoutData(gridData);
		
		Rectangle rec = MainShell.sShell.getBounds();
		Rectangle shellBounds = sShell.getBounds();
		sShell.setLocation((rec.width - shellBounds.width) / 2, (rec.height - shellBounds.height) / 2);
	}

	public InputPasswordDialog(){
		createSShell(null);
	}
	
	public InputPasswordDialog(String hostname){
		createSShell(hostname);
	}	
	
	public void setPassword(String password){
		if(password!=null){
		   passwordComposite.textPassword.setText(password);
		}
	}
	
	public String open(){
		sShell.open();
		Display display = MainShell.sShell.getDisplay();
		while (!sShell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}		
		return passwordComposite.result;
	}
}
