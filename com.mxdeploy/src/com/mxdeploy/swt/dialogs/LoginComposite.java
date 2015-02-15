package com.mxdeploy.swt.dialogs;

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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class LoginComposite extends Composite {

	private Composite topComposite = null;
	private Label imageLabel = null;
	private Composite mainComposite = null;
	private Label monitorLabel = null;
	private Composite usernameComposite = null;
	private Label userLabel = null;
	protected Text userText = null;
	private Label passLabel = null;
	protected Text passwordText = null;
	private Composite buttonComposite = null;
	protected Button okButton = null; 
	protected Button cancelButton = null;
	protected Shell  shell = null;
	
	protected int result = LoginHelper.MSG_CANCEL;
	
	protected LoginHelper helper = null;
	
	public LoginComposite(Composite parent, int style) {
		super(parent, style);
		shell = (Shell)parent;
		initialize();
	}

	private void initialize() {
		helper = new LoginHelper(this);
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.numColumns = 1;
		gridLayout.marginHeight = 0;
		this.setLayout(gridLayout);
		createTopComposite();
		createMainComposite();
		setSize(new Point(434, 263));
	}

	/**
	 * This method initializes topComposite	
	 *
	 */
	private void createTopComposite() {
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.horizontalSpacing = 0;
		gridLayout1.marginWidth = 0;
		gridLayout1.verticalSpacing = 0;
		gridLayout1.marginHeight = 0;
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		topComposite = new Composite(this, SWT.NONE);
		topComposite.setLayoutData(gridData);
		topComposite.setLayout(gridLayout1);
		topComposite.setBackground(new Color(Display.getCurrent(),68,119,187));
		imageLabel = new Label(topComposite, SWT.NONE);
		imageLabel.setText("Label");
		imageLabel.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream("/id-w3-sitemark-large.gif")));
			gridLayout1.numColumns = 2;
	}

	/**
	 * This method initializes mainComposite	
	 *
	 */
	private void createMainComposite() { 
		GridData gridData2 = new GridData();
		gridData2.verticalSpan = 10;
		gridData2.grabExcessHorizontalSpace = true;
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.marginWidth = 10;
		gridLayout2.numColumns = 1;
		GridData gridData1 = new GridData();
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData1.grabExcessVerticalSpace = true;
		mainComposite = new Composite(this, SWT.NONE);
		mainComposite.setLayoutData(gridData1);
		mainComposite.setLayout(gridLayout2);
		monitorLabel = new Label(mainComposite, SWT.NONE);
		monitorLabel.setText("Monitoring Access");
		monitorLabel.setFont(new Font(Display.getDefault(), "Tahoma", 8, SWT.BOLD));
		monitorLabel.setLayoutData(gridData2);
		createUsernameComposite();
		createButtonComposite();
		userText.forceFocus();
	}

	/**
	 * This method initializes usernameComposite	
	 *
	 */
	private void createUsernameComposite() {
		GridData gridData6 = new GridData();
		gridData6.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData6.grabExcessHorizontalSpace = true;
		GridData gridData5 = new GridData();
		gridData5.grabExcessHorizontalSpace = true;
		gridData5.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData4 = new GridData();
		gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData4.grabExcessHorizontalSpace = false;
		GridLayout gridLayout3 = new GridLayout();
		gridLayout3.marginHeight = 0;
		gridLayout3.marginWidth = 0;
		gridLayout3.verticalSpacing = 8;
		gridLayout3.numColumns = 2;
		gridLayout3.horizontalSpacing = 0;
		GridData gridData3 = new GridData();
		gridData3.grabExcessHorizontalSpace = true;
		gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		usernameComposite = new Composite(mainComposite, SWT.NONE);
		usernameComposite.setLayoutData(gridData3);
		usernameComposite.setLayout(gridLayout3);
		userLabel = new Label(usernameComposite, SWT.NONE);
		userLabel.setText("User name :             ");
		userLabel.setFont(new Font(Display.getDefault(), "Tahoma", 8, SWT.BOLD));
		userText = new Text(usernameComposite, SWT.BORDER);
		userText.setLayoutData(gridData5);
		passLabel = new Label(usernameComposite, SWT.NONE);
		passLabel.setText("Password :");
		passLabel.setFont(new Font(Display.getDefault(), "Tahoma", 8, SWT.BOLD));
		passLabel.setLayoutData(gridData4);
		passwordText = new Text(usernameComposite, SWT.BORDER | SWT.PASSWORD);
		passwordText.setLayoutData(gridData6);
		passwordText.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
        	public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
        		switch (e.character){
    				case SWT.CR :{
    					if(helper.connect()!=LoginHelper.MSG_ERROR){
    					   shell.close();
    					}
    				}
        		}
        	}
        });
	}

	/**
	 * This method initializes buttonComposite	
	 *
	 */
	private void createButtonComposite() {
		GridLayout gridLayout4 = new GridLayout();
		gridLayout4.numColumns = 2;
		GridData gridData7 = new GridData();
		gridData7.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		gridData7.grabExcessHorizontalSpace = true;
		buttonComposite = new Composite(mainComposite, SWT.NONE);
		buttonComposite.setLayoutData(gridData7);
		buttonComposite.setLayout(gridLayout4);
		okButton = new Button(buttonComposite, SWT.FLAT);
		okButton.setText("        OK          ");
		okButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if(helper.connect()!=LoginHelper.MSG_ERROR){
				   shell.close();
				}
			}
		});
		cancelButton = new Button(buttonComposite, SWT.FLAT);
		cancelButton.setText("       Cancel        ");
		cancelButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
						result=LoginHelper.MSG_CANCEL;
						shell.close();
					}
				});
		
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
