package com.mxdeploy.plugin.graphic.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.mxdeploy.images.Constant;
import com.mxdeploy.swt.MainShell;

public class ActivityDialog {

	protected Object result;
	protected Shell shell;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public ActivityDialog() {
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = shell.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
	
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
		shell = new Shell(MainShell.sShell, SWT.CLOSE | SWT.DIALOG_TRIM);
		shell.setText("Perform commands in all servers connected");
		shell.setSize(new Point(737, 294));
		shell.setLayout(new GridLayout());
		ActivityTableComposite composite = new ActivityTableComposite(shell, SWT.NONE); 
		composite.setLayout(gridLayout);
		composite.setLayoutData(gridData);
		shell.setImage(Constant.IMAGE_PROCEDURES);
	}
	
	public static void main(String args[]){
		ActivityDialog dialog = new ActivityDialog();
		dialog.open();
	}	

}
