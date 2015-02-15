package com.mxdeploy.swt.dialogs.authentication;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.mxdeploy.api.domain.Database;
import com.mxdeploy.api.domain.PasswordManager;
import com.mxdeploy.swt.dialogs.authentication.event.AutenticationEvent;

public class SshAuthenticationComposite extends Composite {

	private Composite topComposite = null;
	private Label topImage = null;
	private Composite composite = null;
	private Label titleLabel = null;
	private Label label = null;
	private Composite composite1 = null;
	private Composite composite2 = null;
	private Composite composite3 = null;
	private Button okButton = null;
	private Button cancelButton = null;
	private Composite composite4 = null;
	private Label label1 = null;
	private Text usernameText = null;
	private Label passwordLabel = null;
	private Text passwordText = null;
	private Label warnigLabel = null;
	private Shell sShell;
	
	private Button checkBox = null;

	private Label label2 = null;
	private Combo serverText = null;
	
	private SshAuthenticationDialog dialog;
	
	private boolean notLoadMethod = true;
	private boolean isFirst = true;
	private Text hostnameText;
	  //  @jve:decl-index=0:
	
	public SshAuthenticationComposite(Composite parent, SshAuthenticationDialog dialog, int style) {
		super(parent, style);
		this.sShell = (Shell)parent;
		this.dialog = dialog;
		initialize();
	}

	private void initialize() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginHeight = 0;
		createTopComposite();
		this.setLayout(gridLayout);
		createComposite1();
		createComposite2();
		setSize(new Point(364, 261));
	}

	/**
	 * This method initializes topComposite	
	 *
	 */
	private void createTopComposite() {
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData1.grabExcessVerticalSpace = true;
		gridData1.grabExcessHorizontalSpace = true;
		GridData gridData = new GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.horizontalSpacing = 0;
		gridLayout1.marginWidth = 0;
		gridLayout1.verticalSpacing = 0;
		gridLayout1.numColumns = 2;
		gridLayout1.marginHeight = 0;
		topComposite = new Composite(this, SWT.NONE);
		topComposite.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		topComposite.setLayout(gridLayout1);
		createComposite();
		topComposite.setLayoutData(gridData);
		topImage = new Label(topComposite, SWT.RIGHT);
		topImage.setText("Label");
		topImage.setLayoutData(gridData1);
		topImage.setImage(SWTResourceManager.getImage(SshAuthenticationComposite.class, "/enabled/defcon_wiz.png"));
	}

	/**
	 * This method initializes composite	
	 *
	 */
	private void createComposite() {
		GridData gridData9 = new GridData();
		gridData9.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData9.grabExcessHorizontalSpace = true;
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData2.grabExcessHorizontalSpace = true;
		composite = new Composite(topComposite, SWT.NONE);
		composite.setLayout(new GridLayout());
		composite.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		composite.setLayoutData(gridData2);
		titleLabel = new Label(composite, SWT.NONE);
		titleLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		titleLabel.setText("   Authentication");
		titleLabel.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		titleLabel.setFont(new Font(Display.getDefault(), "Tahoma", 10, SWT.BOLD));
		label = new Label(composite, SWT.NONE);
		label.setText("       Inform your Username and Password");
		label.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		warnigLabel = new Label(composite, SWT.NONE);
		warnigLabel.setText("");
		warnigLabel.setForeground(new Color(Display.getCurrent(), 255, 0, 0));
		warnigLabel.setLayoutData(gridData9);
		warnigLabel.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
	}
  
	/**
	 * This method initializes composite1	
	 *
	 */
	private void createComposite1() {
		GridData gridData3 = new GridData();
		gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData3.grabExcessHorizontalSpace = true;
		gridData3.grabExcessVerticalSpace = true;
		gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		composite1 = new Composite(this, SWT.BORDER);
		composite1.setLayout(new GridLayout());
		createComposite4(); 
		composite1.setLayoutData(gridData3); 
		checkBox = new Button(composite1, SWT.CHECK); 
		checkBox.setText("Use it for all");
		if( serverText.getItemCount() > 1){
			checkBox.setSelection(true);
		} else {
			checkBox.setSelection(false);
		}
	}

	/**
	 * This method initializes composite2	
	 *
	 */
	private void createComposite2() {
		GridData gridData4 = new GridData();
		gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData4.grabExcessHorizontalSpace = true;
		composite2 = new Composite(this, SWT.BORDER);
		composite2.setLayout(new GridLayout());
		createComposite3();
		composite2.setLayoutData(gridData4);
	}

	/**
	 * This method initializes composite3	
	 *
	 */
	private void createComposite3() {
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 2;
		GridData gridData5 = new GridData();
		gridData5.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		gridData5.grabExcessHorizontalSpace = true;
		composite3 = new Composite(composite2, SWT.NONE);
		composite3.setLayoutData(gridData5);
		composite3.setLayout(gridLayout2);
		okButton = new Button(composite3, SWT.NONE);
		okButton.setText("     OK     ");
		okButton.setFont(new Font(Display.getDefault(), "Tahoma", 10, SWT.NORMAL));
		okButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				(new AutenticationEvent()).validate(e);
				dialog.setHostname(getHostnameText().getText());
				dialog.setPassword(getPasswordText().getText());
				dialog.setUsername(getUsernameText().getText());
				dialog.setLDAPUser(getCheckBox().getSelection());
				if( getWarnigLabel().getText().trim().length()>0){
					dialog.setLabelWarningValue(getWarnigLabel().getText());
				}
				sShell.close();	
			}
		});
		cancelButton = new Button(composite3, SWT.NONE);
		cancelButton.setData(dialog);
		cancelButton.setText("   Cancel   ");
		cancelButton.setFont(new Font(Display.getDefault(), "Tahoma", 10, SWT.NORMAL));
		cancelButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				dialog.setCanceled(true);
				if( getWarnigLabel().getText().trim().length()>0){
					dialog.setLabelWarningValue(getWarnigLabel().getText());
				}				
				(new AutenticationEvent()).close(e);
			}
		});
	}

	/**
	 * @return the checkBox
	 */
	public Button getCheckBox() {
		return checkBox;
	}

	/**
	 * This method initializes composite4	
	 *
	 */
	private void createComposite4() {
		GridData gridData10 = new GridData();
		gridData10.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData8 = new GridData();
		gridData8.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData8.grabExcessHorizontalSpace = true;
		GridData gridData7 = new GridData();
		gridData7.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData7.grabExcessHorizontalSpace = true;
		GridLayout gridLayout3 = new GridLayout();
		gridLayout3.numColumns = 2;
		GridData gridData6 = new GridData();
		gridData6.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData6.grabExcessVerticalSpace = true;
		gridData6.grabExcessHorizontalSpace = true;
		composite4 = new Composite(composite1, SWT.NONE);
		composite4.setLayoutData(gridData6);
		composite4.setLayout(gridLayout3); 
		
		Label lblServer = new Label(composite4, SWT.NONE);
		lblServer.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblServer.setText("Server:");
		
		hostnameText = new Text(composite4, SWT.BORDER);
		hostnameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		label1 = new Label(composite4, SWT.NONE);
		label1.setText("Username :");
		usernameText = new Text(composite4, SWT.BORDER);
		usernameText.setLayoutData(gridData7);
		passwordLabel = new Label(composite4, SWT.NONE);
		passwordLabel.setText("Password  :");
		passwordText = new Text(composite4, SWT.BORDER | SWT.PASSWORD);
		passwordText.addKeyListener( new KeyListener() {
            public void keyPressed( KeyEvent e ) {
            }

            public void keyReleased( KeyEvent e ) {
            	if( e.keyCode == SWT.CR ){ 
    				(new AutenticationEvent()).validate(e);
    				dialog.setPassword(getPasswordText().getText());
    				dialog.setUsername(getUsernameText().getText());
    				dialog.setLDAPUser(getCheckBox().getSelection());
    				sShell.close();	
            	}
            }
        });		
		passwordText.setLayoutData(gridData8);
		passwordText.forceFocus();
		label2 = new Label(composite4, SWT.NONE);
		label2.setText("Method      :");
		serverText = new Combo(composite4, SWT.READ_ONLY);
		serverText.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = ((Combo)e.widget).getSelectionIndex();
				Object obj = ((Combo)e.widget).getData(String.valueOf(index));
				 if( obj != null && obj instanceof PasswordManager ){
					 passwordText.setText( ( (PasswordManager)obj ).getPassword() );
					 usernameText.setText( ( (PasswordManager)obj ).getUsername());
					 passwordText.forceFocus();
				 } else {
					 passwordText.setText("");
					 usernameText.setText("");
					 usernameText.forceFocus();
				 }
			}
		});
		serverText.setEnabled(false);
		serverText.setLayoutData(gridData10);
		
		if( !Database.getInstance().getPasswordManagerList().isEmpty() ){
			for( PasswordManager pass : Database.getInstance().getPasswordManagerList() ){
				 serverText.add( pass.getTitle() );
				 serverText.setData(String.valueOf(pass.getId()), pass);
				 if( isFirst ){ 
					 passwordText.setText( pass.getPassword() );
					 usernameText.setText( pass.getUsername());
					 
					 isFirst = false;
				 }
			}
		} else {
			usernameText.setFocus(); 
		}
		serverText.add(dialog.getHostName());
		serverText.select(0);
	}
 
	/**
	 * @return the cancelButton
	 */
	public Button getCancelButton() {
		return cancelButton;
	}

	/**
	 * @return the okButton
	 */
	public Button getOkButton() {
		return okButton;
	} 

	/**
	 * @return the regExpText
	 */
	public Combo getComboBox() {
		return serverText;
	}

	/**
	 * @return the passwordText
	 */
	public Text getPasswordText() {
		return passwordText;
	}

	/**
	 * @return the usernameText
	 */
	public Text getUsernameText() {
		return usernameText;
	}

	/**
	 * @return the warnigLabel
	 */
	public Label getWarnigLabel() {
		return warnigLabel;
	}

	/**
	 * @return the sShell
	 */
	public Shell getSShell() {
		return sShell;
	}
	
	public void setLabelTitle(String value){
		titleLabel.setText("   "+value);
	}
	
	
	public void setUsername(String value){
		usernameText.setText(value);
		usernameText.setEditable(false);
	}	
	
	public Text getHostnameText(){
	    return hostnameText;	
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"

