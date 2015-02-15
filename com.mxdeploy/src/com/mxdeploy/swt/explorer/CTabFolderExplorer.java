package com.mxdeploy.swt.explorer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class CTabFolderExplorer extends CTabFolder{

	public CTabFolderExplorer(Composite parent, int style) {
		super(parent, style);
		initialize();
	}
	
	public void initialize(){
		setSimple(false);
		setUnselectedImageVisible(false);
		setUnselectedCloseVisible(false);
		
		Display display = Display.getDefault();
		setSelectionBackground(new Color[] {
	        display.getSystemColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT),
	        display.getSystemColor(SWT.COLOR_WHITE), 
	        display.getSystemColor(SWT.COLOR_INFO_FOREGROUND)},  new int[] { 50, 100});
	}

}
