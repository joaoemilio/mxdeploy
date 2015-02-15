package com.mxdeploy.swt.command;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
 
public class AddCommandprojectServerComposite2 extends Composite {

	private Composite topComposite = null;
	private Label imageLabel = null;
	private Composite topComposite2 = null;
	private Label TitleLabel = null;
	private Label descriptionLabel = null;
	private Composite centerComposite = null;
	private Composite composite = null;
	private Composite composite1 = null;
	protected Tree resultServerTree = null;
	private Composite composite2 = null;
	protected Tree serverSelectedTree = null;
	protected Button rightButton = null;
	protected Button leftButton = null;
	private Composite composite3 = null;
	protected Button cancelButton = null;
	protected Button saveButton = null;
	protected AddCommandProjectServerHelper helper = null;
	
	public AddCommandprojectServerComposite2(Composite parent, int style) {
		super(parent, style);
		initialize();
	}
  
	private void initialize() { 
		helper = new AddCommandProjectServerHelper(this);		
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginHeight = 0;
		createTopComposite();
		this.setLayout(gridLayout);
		createComposite();
		createComposite1();
		setSize(new Point(488, 373));
	}

	/**
	 * This method initializes topComposite	
	 *
	 */
	private void createTopComposite() {
		GridData gridData1 = new GridData();
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		GridData gridData = new GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.horizontalSpacing = 0;
		gridLayout1.marginWidth = 0;
		gridLayout1.verticalSpacing = 0;
		gridLayout1.numColumns = 2;
		gridLayout1.marginHeight = 0;
		topComposite = new Composite(this, SWT.BORDER);
		topComposite.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		topComposite.setLayoutData(gridData);
		createTopComposite2();
		topComposite.setLayout(gridLayout1);
		imageLabel = new Label(topComposite, SWT.NONE);
		imageLabel.setText("Label");
		imageLabel.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		imageLabel.setLayoutData(gridData1);
		imageLabel.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream("/defcon_wiz.png")));
	}

	/**
	 * This method initializes topComposite2	
	 *
	 */
	private void createTopComposite2() {
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData2.grabExcessHorizontalSpace = true;
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.marginHeight = 5;
		gridLayout2.marginWidth = 5;
		gridLayout2.verticalSpacing = 5;
		gridLayout2.numColumns = 1;
		gridLayout2.horizontalSpacing = 5;
		topComposite2 = new Composite(topComposite, SWT.NONE);
		topComposite2.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		topComposite2.setLayout(gridLayout2);
		topComposite2.setLayoutData(gridData2);
		TitleLabel = new Label(topComposite2, SWT.NONE);
		TitleLabel.setText("Add Command on Server");
		TitleLabel.setFont(new Font(Display.getDefault(), "Tahoma", 10, SWT.BOLD));
		TitleLabel.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		descriptionLabel = new Label(topComposite2, SWT.NONE);
		descriptionLabel.setText("    Select the server(s) on the left side and click on > button to push to right side.");
		descriptionLabel.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
	}

	/**
	 * This method initializes composite	
	 *
	 */
	private void createComposite() {
		GridData gridData8 = new GridData();
		gridData8.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData8.grabExcessVerticalSpace = true;
		gridData8.grabExcessHorizontalSpace = true;
		gridData8.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData7 = new GridData();
		gridData7.grabExcessVerticalSpace = true;
		gridData7.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData7.grabExcessHorizontalSpace = true;
		gridData7.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout5 = new GridLayout();
		gridLayout5.numColumns = 3;
		GridData gridData5 = new GridData();
		gridData5.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData5.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData5.grabExcessVerticalSpace = true;
		gridData5.grabExcessHorizontalSpace = true;
		composite = new Composite(this, SWT.BORDER);
		composite.setLayoutData(gridData5); 
		composite.setLayout(gridLayout5); 
		resultServerTree = new Tree(composite, SWT.BORDER);
		resultServerTree.setLayoutData(gridData7);
		createComposite2();
		serverSelectedTree = new Tree(composite, SWT.BORDER);
		serverSelectedTree.setLayoutData(gridData8);
	}

	/**
	 * This method initializes composite1	
	 *
	 */
	private void createComposite1() {
		GridLayout gridLayout4 = new GridLayout();
		GridData gridData6 = new GridData();
		gridData6.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData6.grabExcessHorizontalSpace = true;
		composite1 = new Composite(this, SWT.BORDER);
		composite1.setLayoutData(gridData6);
		createComposite3();
		composite1.setLayout(gridLayout4);
	}

	/**
	 * This method initializes composite2	
	 *
	 */
	private void createComposite2() {
		composite2 = new Composite(composite, SWT.NONE);
		composite2.setLayout(new GridLayout());
		rightButton = new Button(composite2, SWT.NONE);
		rightButton.setText(" > ");
		rightButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				helper.addItemServerSelectedTable();
			}
		});		
		rightButton.setFont(new Font(Display.getDefault(), "Tahoma", 10, SWT.NORMAL));
		leftButton = new Button(composite2, SWT.NONE);
		leftButton.setText(" < ");
		leftButton.setFont(new Font(Display.getDefault(), "Tahoma", 10, SWT.NORMAL));
		leftButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {   
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {    
				helper.remove();
			}
		});		
	}

	/**
	 * This method initializes composite3	
	 *
	 */
	private void createComposite3() {
		GridLayout gridLayout6 = new GridLayout();
		gridLayout6.numColumns = 2;
		gridLayout6.horizontalSpacing = 10; 
		GridData gridData9 = new GridData();
		gridData9.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		gridData9.grabExcessHorizontalSpace = true;
		composite3 = new Composite(composite1, SWT.NONE);
		composite3.setLayoutData(gridData9);
		composite3.setLayout(gridLayout6);
		saveButton = new Button(composite3, SWT.NONE);
		saveButton.setText("   Save   ");
		saveButton.setFont(new Font(Display.getDefault(), "Tahoma", 10, SWT.NORMAL));
		saveButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				helper.save();
				helper.commandByServerHelper.removeTreeItemProjectServer();
				helper.commandByServerHelper.loadTreeItemProjectServer( helper.getProject().getId() , helper.getCommand());
				helper.close();
			}
		});
		cancelButton = new Button(composite3, SWT.NONE);
		cancelButton.setText("  Cancel  ");
		cancelButton.setFont(new Font(Display.getDefault(), "Tahoma", 10, SWT.NORMAL));
		cancelButton
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
						helper.close();
					}
				});
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
