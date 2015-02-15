package com.mxdeploy.plugin.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import com.mxdeploy.images.Constant;

public class PluginConsoleComposite extends Composite {

	private SashForm sashForm = null;
	private Composite tableComposite = null;
	private Composite consoleComposite = null;
	private Table table = null;
	private CTabFolder cTabFolder = null;
	private CTabItem consoleCTabItem = null;
	private Text textArea = null;

	public PluginConsoleComposite(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginHeight = 0;
		createSashForm();
		this.setLayout(gridLayout);
		setSize(new Point(300, 200));
	}

	/**
	 * This method initializes sashForm	
	 *
	 */
	private void createSashForm() {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		sashForm = new SashForm(this, SWT.NONE);
		sashForm.setOrientation(SWT.VERTICAL);
		sashForm.setLayoutData(gridData);
		createTableComposite();
		createConsoleComposite();
	}

	/**
	 * This method initializes tableComposite	
	 *
	 */
	private void createTableComposite() {
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.horizontalSpacing = 0;
		gridLayout1.marginWidth = 0;
		gridLayout1.verticalSpacing = 0;
		gridLayout1.marginHeight = 0;
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData1.grabExcessVerticalSpace = true;
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		tableComposite = new Composite(sashForm, SWT.NONE);
		tableComposite.setLayout(gridLayout1);
		table = new Table(tableComposite, SWT.NONE);
		table.setLinesVisible(true);
		table.setLayoutData(gridData1);
		table.setHeaderVisible(true);
	}

	/**
	 * This method initializes consoleComposite	
	 *
	 */
	private void createConsoleComposite() {
		consoleComposite = new Composite(sashForm, SWT.NONE);
		consoleComposite.setLayout(new GridLayout());
		createCTabFolder();
	}

	/**
	 * This method initializes cTabFolder	
	 *
	 */
	private void createCTabFolder() {
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.grabExcessVerticalSpace = true;
		gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		cTabFolder = new CTabFolder(consoleComposite, SWT.CLOSE | SWT.BORDER);
		cTabFolder.setLayoutData(gridData2);
		textArea = new Text(cTabFolder, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		
		consoleCTabItem = new CTabItem(this.cTabFolder, SWT.NONE);
		consoleCTabItem.setText("Console");
		consoleCTabItem.setImage(Constant.IMAGE_CONSOLE);
		CTabItem cTabItem = new CTabItem(cTabFolder, SWT.CLOSE);
		cTabItem.setControl(textArea);
	}

}
