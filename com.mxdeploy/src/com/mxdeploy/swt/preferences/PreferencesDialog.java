package com.mxdeploy.swt.preferences;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Shell;

import com.mxdeploy.images.Constant;
import com.mxdeploy.swt.MainShell;

public class PreferencesDialog {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"

	private PreferenceComposite preferenceComposite = null;
	/**
	 * This method initializes sShell
	 */
	public void createSShell(String title) {
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
		sShell = new Shell(MainShell.sShell,SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		sShell.setText(title);
		sShell.setImage(Constant.IMAGE_TUNNEL);
		sShell.setLayout(gridLayout);
		sShell.setSize(new Point(475, 450));
		
		preferenceComposite = new PreferenceComposite(sShell,SWT.NONE);
		preferenceComposite.setLayoutData(gridData);
		
		Rectangle rec = MainShell.display.getActiveShell().getBounds(); 
		Rectangle shellBounds = sShell.getBounds();
		sShell.setLocation((rec.width - shellBounds.width) / 2, (rec.height - shellBounds.height) / 2);
	}
	
	public void openShell(){
		sShell.open();
	}	
	
}
