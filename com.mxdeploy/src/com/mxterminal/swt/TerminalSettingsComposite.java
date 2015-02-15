package com.mxterminal.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class TerminalSettingsComposite extends Composite {

	private CTabFolder cTabFolder = null;
    protected TerminalSettingsHelper helper = null;
    
	public TerminalSettingsComposite(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		//helper = new TerminalSettingsHelper(this);
		
		createCTabFolder();
		setSize(new Point(362, 221));
		setLayout(new GridLayout());
	}

	/**
	 * This method initializes cTabFolder	
	 *
	 */
	private void createCTabFolder() {
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessVerticalSpace = true; 
		cTabFolder = new CTabFolder(this, SWT.NONE);
		cTabFolder.setLayoutData(gridData);
		createGeneralItem();
		createColorItem();
		createMiscItem();
		createVTItem();
	}
	
	private void createGeneralItem(){
		CTabItem item = new CTabItem(cTabFolder,SWT.NONE);
		item.setText("  Geral ");
		item.setControl(new GeneralComposite(cTabFolder,SWT.NONE));
	}

	private void createColorItem(){
		CTabItem item = new CTabItem(cTabFolder,SWT.NONE);
		item.setText("  Color ");
		item.setControl(new ColorsComposite(cTabFolder,SWT.NONE));
	}
	
	private void createMiscItem(){
		CTabItem item = new CTabItem(cTabFolder,SWT.NONE);
		item.setText("  Misc  ");
		item.setControl(new MiscComposite(cTabFolder,SWT.NONE));
	}	
	
	private void createVTItem(){
		CTabItem item = new CTabItem(cTabFolder,SWT.NONE);
		item.setText("   VT   ");
		item.setControl(new VTComposite(cTabFolder,SWT.NONE));
	}

	/**
	 * @return the helper
	 */
	public TerminalSettingsHelper getHelper() {
		return helper;
	}		
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
