package com.mxdeploy.swt.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.mxdeploy.swt.MainShell;

public class ExpectDialog {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"
	private ExpectComposite composite = null;

	/**
	 * This method initializes sShell
	 */
	public void createSShell() { 
		sShell = new Shell(MainShell.sShell, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		GridData gridData3 = new GridData();
		gridData3.grabExcessHorizontalSpace = true;
		gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData3.grabExcessVerticalSpace = true;
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.horizontalSpacing = 5;
		gridLayout1.verticalSpacing = 5;
		gridLayout1.marginWidth = 5;
		gridLayout1.marginHeight = 5;
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginHeight = 0;
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		//sShell = new Shell(SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM); 
		sShell.setText("Expect Value");

		sShell.setLayout(gridLayout);
		sShell.setSize(new Point(356, 134));
		composite = new ExpectComposite(sShell,SWT.NONE);		
		Rectangle rec = MainShell.sShell.getBounds(); 
		composite.setLayoutData(gridData3);
		Rectangle shellBounds = sShell.getBounds();
		sShell.setLocation((rec.width - shellBounds.width) / 2, (rec.height - shellBounds.height) / 2);
	}  
	
	public String open(){
		sShell.open();
		Display display = MainShell.sShell.getDisplay();
		while (!sShell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}		
		return composite.result; 
	}	
	
	public void setValue(String value){
		composite.variavelExpect.setText(value);
	}
	
	public void setLabelValue(String value){
		composite.label.setText(value);
	}	
	
	

}
