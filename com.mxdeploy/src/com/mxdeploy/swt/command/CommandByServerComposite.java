package com.mxdeploy.swt.command;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import com.mxdeploy.swt.MainShell;

public class CommandByServerComposite extends Composite {

	protected Group commandGroup = null;
	protected Group commandItemGroup = null;
	protected Group projectServerGroup = null;
	protected Composite bottomComposite = null;
	protected CLabel nameCLabel = null;
	protected Text nmText = null;
	protected CLabel descCLabel = null;
	protected Text descritionTextArea = null;
	protected Table commandItemTable = null;
	protected Button editCommandItemButton = null;
	protected Tree projectServerTree = null;
	protected Composite rightBottomComposite = null;
	protected Button addServerButton = null;
	protected Button removeServerButton = null;
	protected Button saveButton = null;
	protected Button cancelButton = null;
	
	protected CommandByServerHelper helper;
	
	public CommandByServerComposite(Composite parent, int style) {
		super(parent, style); 
		initialize();
	}

	private void initialize() {
		helper = new CommandByServerHelper(this); 
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		gridLayout.horizontalSpacing = 4;
		gridLayout.verticalSpacing = 0;
		createCommandGroup();
		this.setLayout(gridLayout);
		createProjectServerGroup();
		setSize(new Point(685, 532));
		createCommandItemGroup();
		@SuppressWarnings("unused")
		Label filler = new Label(this, SWT.NONE);
		createBottomComposite();
	}

	private void createCommandGroup() {
		GridData gridData5 = new GridData();
		gridData5.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData4 = new GridData();
		gridData4.grabExcessHorizontalSpace = true;
		gridData4.grabExcessVerticalSpace = true;
		gridData4.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 1;
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.grabExcessVerticalSpace = true;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		commandGroup = new Group(this, SWT.NONE);
		commandGroup.setLayoutData(gridData1);
		commandGroup.setLayout(gridLayout1);
		commandGroup.setText("Command :");
		nameCLabel = new CLabel(commandGroup, SWT.NONE);
		nameCLabel.setText("Name :");
		nmText = new Text(commandGroup, SWT.BORDER);
		nmText.setTextLimit(60);
		nmText.setLayoutData(gridData5);
		descCLabel = new CLabel(commandGroup, SWT.NONE);
		descCLabel.setText("Description :");
		descritionTextArea = new Text(commandGroup, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		descritionTextArea.setTextLimit(250);
		descritionTextArea.setLayoutData(gridData4);
	}

	/**
	 * This method initializes commandItemGroup	
	 *
	 */
	private void createCommandItemGroup() {
		GridData gridData6 = new GridData();
		gridData6.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData6.grabExcessHorizontalSpace = true;
		gridData6.grabExcessVerticalSpace = true;
		gridData6.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.grabExcessVerticalSpace = true;
		gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		commandItemGroup = new Group(this, SWT.NONE);
		commandItemGroup.setLayout(new GridLayout());
		commandItemGroup.setLayoutData(gridData2);
		commandItemGroup.setText("Command Lines :");
		commandItemTable = new Table(commandItemGroup, SWT.BORDER | SWT.FULL_SELECTION);
		commandItemTable.setHeaderVisible(false);
		commandItemTable.setLayoutData(gridData6);
		commandItemTable.setLinesVisible(true);
		editCommandItemButton = new Button(commandItemGroup, SWT.NONE);
		editCommandItemButton.setText("Add/Edit Command Lines");
		editCommandItemButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				helper.openAddCommandItemDialog();
			}
		});
		TableColumn tableColumn = new TableColumn(commandItemTable, SWT.NONE);
		tableColumn.setWidth(455);
	} 

	/**
	 * This method initializes projectServerGroup	
	 *
	 */
	private void createProjectServerGroup() {
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 2;
		GridData gridData7 = new GridData();
		gridData7.grabExcessVerticalSpace = true;
		gridData7.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData7.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData7.grabExcessHorizontalSpace = true;
		GridData gridData = new GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.verticalSpan = 2;
		gridData.grabExcessHorizontalSpace = false;
		gridData.grabExcessVerticalSpace = true;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		projectServerGroup = new Group(this, SWT.NONE);
		projectServerGroup.setLayoutData(gridData);
		projectServerGroup.setLayout(gridLayout2);
		projectServerGroup.setText("Project and Server :");
		projectServerTree = new Tree(projectServerGroup, SWT.BORDER);
		projectServerTree.setHeaderVisible(false);
		projectServerTree.setLinesVisible(false);
		projectServerTree.setLayoutData(gridData7);
		TreeColumn treeColumn = new TreeColumn(projectServerTree, SWT.NONE);
		treeColumn.setWidth(150);
		createRightBottomComposite();
	}

	/**
	 * This method initializes bottomComposite	
	 *
	 */
	private void createBottomComposite() {
		GridData gridData11 = new GridData();
		gridData11.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData11.grabExcessHorizontalSpace = true;
		GridData gridData10 = new GridData();
		gridData10.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData10.grabExcessHorizontalSpace = true;
		GridLayout gridLayout4 = new GridLayout();
		gridLayout4.numColumns = 2;
		gridLayout4.marginWidth = 0;
		GridData gridData3 = new GridData();
		gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		bottomComposite = new Composite(this, SWT.NONE);
		bottomComposite.setLayoutData(gridData3);
		bottomComposite.setLayout(gridLayout4);
		saveButton = new Button(bottomComposite, SWT.NONE);
		saveButton.setText("Save");
		saveButton.setLayoutData(gridData11);
		saveButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				Cursor cursorWait = new Cursor(MainShell.display,SWT.CURSOR_WAIT);
				MainShell.sShell.setCursor(cursorWait);

				if( projectServerTree.getItemCount()>0){
					if(helper.save()){
					   helper.refreshServerCommandTable();
					   helper.close();
					}
				} else {
					if( helper.confirm("There are no server related. Do you want delete this Command ?") ){
						helper.deleteCommand(helper.getServerHelper().getTerminalHelper().getProject(),helper.getServerHelper().getTerminalHelper().getServer()); 
						helper.refreshServerCommandTable();
						helper.close();
					}
				}
				Cursor cursorArrow = new Cursor(MainShell.display,SWT.CURSOR_ARROW);
				MainShell.sShell.setCursor(cursorArrow);
			}
		});
		cancelButton = new Button(bottomComposite, SWT.NONE);
		cancelButton.setText("Cancel");
		cancelButton.setLayoutData(gridData10);
		cancelButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if( projectServerTree.getItemCount()>0){
				    helper.close();
				} else {
					Cursor cursorWait = new Cursor(MainShell.display,SWT.CURSOR_WAIT);
					MainShell.sShell.setCursor(cursorWait);
					
					if( helper.confirm("There are no server related. Do you want delete this Command ?") ){
						helper.deleteCommand(helper.getServerHelper().getTerminalHelper().getProject(),helper.getServerHelper().getTerminalHelper().getServer()); 
						helper.refreshServerCommandTable();
						helper.close();
					}
					
					Cursor cursorArrow = new Cursor(MainShell.display,SWT.CURSOR_ARROW);
					MainShell.sShell.setCursor(cursorArrow);
				}
			}
		});
	}

	/**
	 * This method initializes rightBottomComposite	
	 *
	 */
	private void createRightBottomComposite() {
		GridData gridData12 = new GridData();
		gridData12.grabExcessHorizontalSpace = true;
		gridData12.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData9 = new GridData();
		gridData9.grabExcessHorizontalSpace = true;
		gridData9.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout3 = new GridLayout();
		gridLayout3.numColumns = 1;
		gridLayout3.marginHeight = 0;
		gridLayout3.marginWidth = 0;
		gridLayout3.horizontalSpacing = 0;
		GridData gridData8 = new GridData();
		gridData8.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		rightBottomComposite = new Composite(projectServerGroup, SWT.NONE);
		rightBottomComposite.setLayoutData(gridData8);
		rightBottomComposite.setLayout(gridLayout3);
		addServerButton = new Button(rightBottomComposite, SWT.NONE);
		addServerButton.setText("Add/Edit Server");
		addServerButton.setLayoutData(gridData12);
		addServerButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				helper.openAddCommandProjectServerDialog();
			}
		});
		removeServerButton = new Button(rightBottomComposite, SWT.NONE);
		removeServerButton.setText("Remove Server");
		removeServerButton.setLayoutData(gridData9);
		removeServerButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				helper.removeProjectServer();
			}
		});
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
