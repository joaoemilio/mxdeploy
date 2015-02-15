package com.mxdeploy.swt.dialogs.authentication;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

import com.mxdeploy.swt.MainShell;

public class HostnameDialog {

	protected Object result;
	protected Shell shell;
	protected HostnameComposite composite;
	private Display display = Display.getDefault();
	private String hostname;
	
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public HostnameDialog() {
		display = MainShell.sShell.getDisplay();
		createContents();
	}

	public void openShell(){  
		//createSShell();	
		shell.open();
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}		
		display.update();
		//return passwordManager;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(MainShell.sShell, SWT.NO_TRIM | SWT.PRIMARY_MODAL);
		shell.setSize(641, 41);
		//shell.setText(getText());
		GridLayout gl_shell = new GridLayout(1, false);
		gl_shell.verticalSpacing = 0;
		gl_shell.marginWidth = 0;
		gl_shell.marginHeight = 0;
		gl_shell.horizontalSpacing = 0;
		shell.setLayout(gl_shell);
	   
		composite = new HostnameComposite(this, shell,SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
//		center the dialog screen to the monitor
		Monitor primary = display.getPrimaryMonitor ();
		Rectangle bounds = primary.getBounds ();
		Rectangle rect = shell.getBounds ();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shell.setLocation (x, y);			

	}
	
	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

}
