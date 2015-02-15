package com.mxscript.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import xml.properties;

import com.mxscript.swt.event.LoadBeanShellScriptEvent;

public class DiscoverComposite extends Composite {

	protected CTabFolder cTabFolder = null;
    protected BeanShellFormComposite beanShellFormComposite = null;
    private BeanShellScriptComposite beanShellScriptComposite = null;
    private SelectionAdapter selectionAdapter;
    
	public DiscoverComposite(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginHeight = 0;
		createCTabFolder();
		createTabItem();
		this.setLayout(gridLayout);
		setSize(new Point(417, 202));
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
		cTabFolder = new CTabFolder(this, SWT.FLAT | SWT.BOTTOM);
		cTabFolder.setLayoutData(gridData);
		cTabFolder.setMinimized(false);
		cTabFolder.setMinimizeVisible(false);
		cTabFolder.setMRUVisible(false);
		cTabFolder.setLayoutData(gridData);
		cTabFolder.setUnselectedImageVisible(false);
		cTabFolder.setUnselectedCloseVisible(false);
		cTabFolder.setSimple(false);			
	} 
	
	private void createTabItem(){
		CTabItem discoverTabItem = new CTabItem(cTabFolder,SWT.NONE);
		discoverTabItem.setText("Interface  ");
		beanShellFormComposite = new BeanShellFormComposite(cTabFolder,SWT.NONE);
		discoverTabItem.setControl(beanShellFormComposite);
		
		CTabItem scritpTabItem = new CTabItem(cTabFolder,SWT.NONE);
		scritpTabItem.setText("Source Code  ");
		beanShellScriptComposite = new BeanShellScriptComposite(cTabFolder,SWT.NONE); 
		scritpTabItem.setControl(beanShellScriptComposite);
		 
		selectionAdapter = new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent event) {
		    	  CTabFolder tabFolder = (CTabFolder) event.widget;
		    	  if( tabFolder.getSelection().getText().equals("Source Code  ") ){
		    	     (new LoadBeanShellScriptEvent(beanShellScriptComposite)).execute();
		    	     tabFolder.removeSelectionListener( selectionAdapter );
		    	  }
		      }
		};
		
		cTabFolder.addSelectionListener(selectionAdapter);
		
		discoverTabItem.setControl(beanShellFormComposite);
		
		cTabFolder.setSelection(discoverTabItem);
	}
	
	public void loadProperties(){
		beanShellFormComposite.loadProperties();
	}	
	

	
}
