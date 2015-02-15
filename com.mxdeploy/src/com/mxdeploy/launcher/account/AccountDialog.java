package com.mxdeploy.launcher.account;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

import com.mxdeploy.images.Constant;

public class AccountDialog {

	private Shell sShell = null; // @jve:decl-index=0:visual-constraint="10,10"
	private Display display = Display.getDefault();  //  @jve:decl-index=0:
	
	private AccountComposite composite = null;

	/**
	 * This method initializes sShell
	 */
	private void createSShell() {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginHeight = 0;  
		sShell = new Shell(SWT.CENTER | SWT.TOP ); 
		sShell.setText("MXDeploy");
	    //sShell.setImage( Constant.IMAGE_LOGO);
		sShell.setLayout(gridLayout);
		sShell.setSize(new Point(500, 245));

		composite = new AccountComposite(sShell, SWT.NONE);
		composite.setLayoutData(gridData);

		// center the dialog screen to the monitor
		Monitor primary = display.getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = sShell.getBounds();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		sShell.setLocation(x, y);
	}

	public String open(List<String> accounts) {
		createSShell();
		sShell.open();
		
		if (accounts.isEmpty()) {
			composite.getOKButton().setEnabled(false);
			//composite.getCreateButton().setEnabled(true);
		} else {
			for (String account : accounts) {
				composite.getCCombo().add(account);
			}
			composite.getCCombo().select(0);
		} 

		while (!sShell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		// int index = composite.getCCombo().getSelectionIndex();
		return composite.getRetorno();
	}
	

}
