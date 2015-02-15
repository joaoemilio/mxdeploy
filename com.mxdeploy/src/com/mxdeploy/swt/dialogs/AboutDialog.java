package com.mxdeploy.swt.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Shell;

import com.mxdeploy.swt.MainShell;

public class AboutDialog {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"

	/**
	 * This method initializes sShell
	 */
	public void createSShell() {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		sShell = new Shell();
		sShell.setText("Help - About");
		sShell.setLayout(new GridLayout());
		sShell.setSize(new Point(417, 284));
        AboutComposite composite = new AboutComposite(sShell,SWT.NONE);
        composite.setLayoutData(gridData);
        
		Rectangle rec = MainShell.display.getActiveShell().getBounds(); 
		Rectangle shellBounds = sShell.getBounds();
		sShell.setLocation((rec.width - shellBounds.width) / 2, (rec.height - shellBounds.height) / 2);
		
	}
	
	public void openShell(){
		sShell.open();
	}	
	

}
