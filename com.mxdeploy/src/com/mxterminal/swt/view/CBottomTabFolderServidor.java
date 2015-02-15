package com.mxterminal.swt.view;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

public class CBottomTabFolderServidor extends CTabFolder {

	public CBottomTabFolderServidor(Composite parent, int style) {
		super(parent, style);
		initialize();
	}
	
	public void initialize(){
		GridData gridData = new GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		this.setLayoutData(gridData);
	}

}
