package com.mxdeploy.swt.command;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

public class CopyCommandProjectServerComposite extends Composite {

	protected Tree resultServerTree = null;
	protected Composite selectButtonComposite = null;
	protected Button rightButton = null;
	protected Button leftButton = null;
	protected Tree serverSelectedTree = null;
	protected Composite buttonComposite = null;
	protected Button saveButton = null;
	protected Button cancelButton = null;
	protected Composite searchComposite = null;
	protected Text nameSearchText = null;
	protected Button searchButton = null;  
	
	protected CCombo columnSearchCCombo = null;
	
	protected CopyCommandProjectServerHelper helper = null;
	
	private Label searchLabel = null;
	private int itemCount = 0;
	private Label label = null;
	private Label label1 = null;

	public CopyCommandProjectServerComposite(Composite parent, int style) {
		super(parent, style);
		initialize();
	}
	
	private void initialize() {
		helper = new CopyCommandProjectServerHelper(this);
		//itemCount = MainShell.sashFormComposite.getTabFolderServidor().getItemCount();

		GridData gridData5 = new GridData();
		gridData5.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData5.grabExcessHorizontalSpace = true;
		gridData5.grabExcessVerticalSpace = true;
		gridData5.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData4 = new GridData();
		gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData4.grabExcessHorizontalSpace = true;
		gridData4.grabExcessVerticalSpace = true;
		gridData4.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		createSearchComposite();
		resultServerTree = new Tree(this, SWT.BORDER );
		resultServerTree.setHeaderVisible(false);
		resultServerTree.setLayoutData(gridData4);
		resultServerTree.setLinesVisible(false);
		resultServerTree.addMouseListener(new org.eclipse.swt.events.MouseListener() {
			public void mouseDoubleClick(org.eclipse.swt.events.MouseEvent e) {
				helper.addItemServerSelectedTable();
			}
			public void mouseDown(org.eclipse.swt.events.MouseEvent e) {
			}
			public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
			}
		});
		TreeColumn tableColumn = new TreeColumn(resultServerTree, SWT.NONE);
		tableColumn.setWidth(200);
		this.setLayout(gridLayout);
		createSelectButtonComposite();
		setSize(new Point(468, 335));
		serverSelectedTree = new Tree(this, SWT.BORDER);
		serverSelectedTree.setHeaderVisible(false);
		serverSelectedTree.setLayoutData(gridData5);
		serverSelectedTree.setLinesVisible(false);
		serverSelectedTree.addMouseListener(new org.eclipse.swt.events.MouseListener() {
			public void mouseDoubleClick(org.eclipse.swt.events.MouseEvent e) {
				helper.remove();
			}
			public void mouseDown(org.eclipse.swt.events.MouseEvent e) {
			}
			public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
			}
		});
		TreeColumn tableColumn1 = new TreeColumn(serverSelectedTree, SWT.NONE);
		tableColumn1.setWidth(200);
		@SuppressWarnings("unused")
		Label filler1 = new Label(this, SWT.NONE);
		@SuppressWarnings("unused")
		Label filler = new Label(this, SWT.NONE);
		createButtonComposite();
		
		//helper.loadComboBox();
	}

	/**
	 * This method initializes selectButtonComposite	
	 *
	 */
	private void createSelectButtonComposite() {
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData = new GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData.grabExcessHorizontalSpace = false;
		selectButtonComposite = new Composite(this, SWT.NONE);
		selectButtonComposite.setLayout(new GridLayout());
		selectButtonComposite.setLayoutData(gridData);
		rightButton = new Button(selectButtonComposite, SWT.NONE);
		rightButton.setText(">");
		rightButton.setLayoutData(gridData1);  
		rightButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				helper.addItemServerSelectedTable();
			}
		});
		leftButton = new Button(selectButtonComposite, SWT.NONE);
		leftButton.setText("<");
		leftButton.setLayoutData(gridData2);
		leftButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {   
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {    
				helper.remove();
			}
		
		});
		if(itemCount>0){
		   leftButton.setEnabled(true);
		}
	}

	/**
	 * This method initializes buttonComposite	
	 *
	 */
	private void createButtonComposite() {
		GridData gridData10 = new GridData();
		gridData10.grabExcessHorizontalSpace = true;
		gridData10.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData9 = new GridData();
		gridData9.grabExcessHorizontalSpace = true;
		gridData9.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 2;
		GridData gridData3 = new GridData();
		gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		buttonComposite = new Composite(this, SWT.NONE);
		buttonComposite.setLayoutData(gridData3);
		buttonComposite.setLayout(gridLayout1);
		saveButton = new Button(buttonComposite, SWT.NONE);
		saveButton.setText("Save");
		saveButton.setLayoutData(gridData9);
		saveButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				helper.save();
				helper.commandByServerHelper.removeTreeItemProjectServer();
				//helper.commandByServerHelper.loadTreeItemProjectServer(helper.getCommand());
				helper.close();
			}
		});
		cancelButton = new Button(buttonComposite, SWT.NONE);
		cancelButton.setText("Cancel");
		cancelButton.setLayoutData(gridData10);
		cancelButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				helper.close();
			}
		});
	}

	/**
	 * This method initializes searchComposite	
	 *
	 */
//	private void createSearchComposite() {
//		GridData gridData8 = new GridData();
//		gridData8.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
//		gridData8.horizontalSpan = 2;
//		gridData8.grabExcessHorizontalSpace = false;
//		GridData gridData7 = new GridData();
//		gridData7.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
//		gridData7.horizontalSpan = 2;
//		gridData7.grabExcessHorizontalSpace = false;
//		GridLayout gridLayout2 = new GridLayout();
//		gridLayout2.numColumns = 4;
//		gridLayout2.marginWidth = 0;
//		gridLayout2.verticalSpacing = 0;
//		GridData gridData6 = new GridData();
//		gridData6.grabExcessHorizontalSpace = true;
//		gridData6.horizontalSpan = 3;
//		gridData6.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
//		searchComposite = new Composite(this, SWT.NONE);
//		searchComposite.setLayoutData(gridData6);
//		searchComposite.setLayout(gridLayout2);
//		searchLabel = new Label(searchComposite, SWT.NONE);
//		searchLabel.setText("Project Name :");
//		label = new Label(searchComposite, SWT.NONE);
//		label.setText("                                                          ");
//		@SuppressWarnings("unused")
//		Label filler3 = new Label(searchComposite, SWT.NONE);
//		label1 = new Label(searchComposite, SWT.NONE);
//		label1.setText("                                 ");
//		nameSearchText = new Text(searchComposite, SWT.BORDER);
//		nameSearchText.setLayoutData(gridData7);
//		searchButton = new Button(searchComposite, SWT.NONE);
//		searchButton.setText("Search");
//		searchButton.setLayoutData(gridData8);
//		searchButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
//			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
//				resultServerTree.removeAll();
//				helper.search(); 
//			}
//		});
//		nameSearchText.setFocus();
//	}
	
	private void createSearchComposite() {
		GridData gridData8 = new GridData();
		gridData8.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData8.grabExcessHorizontalSpace = true;
		GridData gridData7 = new GridData();
		gridData7.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData7.grabExcessHorizontalSpace = true;
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 3;
		gridLayout2.marginWidth = 0;
		gridLayout2.verticalSpacing = 0;
		GridData gridData6 = new GridData();
		gridData6.grabExcessHorizontalSpace = true;
		gridData6.horizontalSpan = 3;
		gridData6.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		searchComposite = new Composite(this, SWT.NONE);
		searchComposite.setLayoutData(gridData6);
		searchComposite.setLayout(gridLayout2);
		searchLabel = new Label(searchComposite, SWT.NONE);
		searchLabel.setText("Search :");
		@SuppressWarnings("unused")
		Label filler2 = new Label(searchComposite, SWT.NONE);
		@SuppressWarnings("unused")
		Label filler3 = new Label(searchComposite, SWT.NONE);
		columnSearchCCombo = new CCombo(searchComposite, SWT.BORDER);
		columnSearchCCombo.setEditable(false);
		nameSearchText = new Text(searchComposite, SWT.BORDER);
		nameSearchText.setLayoutData(gridData7);
		searchButton = new Button(searchComposite, SWT.NONE);
		searchButton.setText("Search");
		searchButton.setLayoutData(gridData8);
		searchButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				resultServerTree.removeAll();
				helper.search();
			}
		}); 
		nameSearchText.setFocus();
	}
	

}
