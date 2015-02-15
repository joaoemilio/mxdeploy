package com.mxdeploy.swt.project;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.mxdeploy.swt.MainShell;
import com.mxdeploy.swt.project.helper.AddServerProjectHelper;

public class AddServerProjectComposite extends Composite {

	public Table resultServerTable = null;
	public Composite selectButtonComposite = null;
	public Button rightButton = null;
	public Button leftButton = null;
	public Table serverSelectedTable = null;
	public Composite buttonComposite = null;
	public Button saveButton = null;
	public Button cancelButton = null;
	public Composite searchComposite = null;
	public CCombo columnSearchCCombo = null;
	public Text nameSearchText = null;
	public Button searchButton = null;
	
	public AddServerProjectHelper helper = null;
	
	public Label searchLabel = null;
	private int itemCount = 0;
	
	public AddServerProjectComposite(Composite parent, int style) {
		super(parent, SWT.NONE);
		initialize();
	}

	private void initialize() {
		//helper = new AddServerProjectHelper(this);
		itemCount = MainShell.getCTopTabFolder().getItemCount();
		
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
		resultServerTable = new Table(this, SWT.BORDER | SWT.MULTI);
		resultServerTable.setHeaderVisible(false);
		resultServerTable.setLayoutData(gridData4);
		resultServerTable.setLinesVisible(false);
		resultServerTable.addMouseListener(new org.eclipse.swt.events.MouseListener() {
			public void mouseDoubleClick(org.eclipse.swt.events.MouseEvent e) {
				helper.addItemServerSelectedTable();
			}
			public void mouseDown(org.eclipse.swt.events.MouseEvent e) {
			}
			public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
			}
		});
		TableColumn tableColumn = new TableColumn(resultServerTable, SWT.NONE);
		tableColumn.setWidth(200);
		this.setLayout(gridLayout);
		createSelectButtonComposite();
		setSize(new Point(610, 446));
		serverSelectedTable = new Table(this, SWT.BORDER | SWT.MULTI);
		serverSelectedTable.setHeaderVisible(false);
		serverSelectedTable.setLayoutData(gridData5);
		serverSelectedTable.setLinesVisible(false);
		serverSelectedTable.addMouseListener(new org.eclipse.swt.events.MouseListener() {
					public void mouseDoubleClick(org.eclipse.swt.events.MouseEvent e) {
						helper.removeItemServerSelectedTable();
					}
					public void mouseDown(org.eclipse.swt.events.MouseEvent e) {
					}
					public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
					}
				});
		TableColumn tableColumn1 = new TableColumn(serverSelectedTable, SWT.NONE);
		tableColumn1.setWidth(200);
		createButtonComposite();
		
		helper.loadComboBox();
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
		GridLayout gl_selectButtonComposite = new GridLayout();
		gl_selectButtonComposite.marginWidth = 0;
		gl_selectButtonComposite.marginHeight = 0;
		selectButtonComposite.setLayout(gl_selectButtonComposite);
		selectButtonComposite.setLayoutData(gridData);
		rightButton = new Button(selectButtonComposite, SWT.NONE);
		rightButton.setText("  >  ");
		rightButton.setLayoutData(gridData1);  
		rightButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				helper.addItemServerSelectedTable();
			}
		});
		leftButton = new Button(selectButtonComposite, SWT.NONE);
		leftButton.setText("  <  ");
		leftButton.setLayoutData(gridData2);
		leftButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				helper.removeItemServerSelectedTable();
			}
		});
		if(itemCount>0){
		   leftButton.setEnabled(false);
		}
		
	}

	/**
	 * This method initializes buttonComposite	
	 *
	 */
	private void createButtonComposite() {
		buttonComposite = new Composite(this, SWT.NONE);
		buttonComposite.setLayout(null);
		saveButton = new Button(buttonComposite, SWT.NONE);
		saveButton.setBounds(5, 5, 119, 28);
		saveButton.setText("Save");
		saveButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				helper.save();
				helper.refreshTree();
				helper.close();
			}
		});
		cancelButton = new Button(buttonComposite, SWT.NONE);
		cancelButton.setBounds(129, 5, 131, 28);
		cancelButton.setText("Cancel");
		cancelButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				helper.refreshTree();
				helper.close();
			}
		});
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
	}

	/**
	 * This method initializes searchComposite	
	 *
	 */
	private void createSearchComposite() {
		GridData gridData8 = new GridData();
		gridData8.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData7 = new GridData();
		gridData7.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData7.grabExcessHorizontalSpace = true;
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 3;
		gridLayout2.marginWidth = 0;
		gridLayout2.verticalSpacing = 0;
		GridData gridData6 = new GridData();
		gridData6.heightHint = 58;
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
		filler2.setText("                                                       ");
		@SuppressWarnings("unused")
		Label filler3 = new Label(searchComposite, SWT.NONE);
		filler3.setText("                           ");
		columnSearchCCombo = new CCombo(searchComposite, SWT.BORDER);
		columnSearchCCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		columnSearchCCombo.setEditable(false);
		nameSearchText = new Text(searchComposite, SWT.BORDER);
		nameSearchText.setLayoutData(gridData7);
		searchButton = new Button(searchComposite, SWT.NONE);
		searchButton.setText("Search");
		searchButton.setLayoutData(gridData8);
		new Label(searchComposite, SWT.NONE);
		new Label(searchComposite, SWT.NONE);
		new Label(searchComposite, SWT.NONE);
		searchButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				resultServerTable.removeAll();
				helper.search();
			}
		});
		nameSearchText.setFocus();
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
