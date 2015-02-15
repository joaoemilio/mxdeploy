package com.mxdeploy.launcher.splash;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

public class SplashDialog {

	public Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"
	private Display display = Display.getDefault();  //  @jve:decl-index=0:
	
	private SplashComposite composite = null;
	
	public SplashDialog(){
		createSShell();
	}
	/**
	 * This method initializes sShell
	 */
	private void createSShell() {
		GridData gridData = new GridData();
		gridData.horizontalSpan = 2;
		gridData.verticalSpan = 2;
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.numColumns = 2;
		gridLayout.marginHeight = 0;
		sShell = new Shell(SWT.CENTER );
		sShell.setText("Shell");
		sShell.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		sShell.setLayout(gridLayout);
		sShell.setSize(new Point(649, 490));
		 
		composite =  new SplashComposite(sShell,SWT.NONE);
		composite.setLayoutData(gridData);
		
//		center the dialog screen to the monitor
		Monitor primary = display.getPrimaryMonitor ();
		Rectangle bounds = primary.getBounds ();
		Rectangle rect = sShell.getBounds ();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		sShell.setLocation (x, y);		
		
	}
	
	public void openShell(){
		sShell.open();
	}
	
	public void closeShell(){
		sShell.close();
	}	
	
	public ProgressBar getProgressBar(){
		return composite.getProgressBar();
	}		
	
	public Label getLabel(){
		return composite.getTitle();
	}

}

