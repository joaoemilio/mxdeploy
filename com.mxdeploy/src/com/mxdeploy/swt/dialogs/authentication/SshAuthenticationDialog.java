package com.mxdeploy.swt.dialogs.authentication;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

import com.mxdeploy.api.domain.Database;
import com.mxdeploy.api.domain.PasswordManager;
import com.mxdeploy.swt.MainShell;

public class SshAuthenticationDialog { 

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"
	private SshAuthenticationComposite composite;
	private Display display = Display.getDefault();  //  @jve:decl-index=0:
	private String password;  //  @jve:decl-index=0:
	private String username;  //  @jve:decl-index=0:
	private Boolean canceled = new Boolean(false);
	private String hostname;
	private Boolean isLDAPUser = false;
	private String labelWarningValue = null;

	
	public SshAuthenticationDialog(String hostname){
		this.hostname = hostname;
		createSShell();
	}
	
	public Boolean getCanceled() {
		return canceled;
	}

	public void setCanceled(Boolean canceled) {
		this.canceled = canceled;
	}

	private void createSShell() {
		Display.getDefault().syncExec(new Runnable(){
			public void run(){ 
				createSShell(null,null);	
			}
		});			
	}
	
	private void createSShell(PasswordManager pm, String msg) {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginHeight = 0;
		sShell = new Shell(MainShell.sShell,SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM); 
		sShell.setText("SSH Authentication - "+Database.WORKSPACE_NAME); 
		sShell.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream("/enabled/alt16.gif")));
		sShell.setLayout(gridLayout);
		sShell.setSize(new Point(435, 292));
		 
		composite = new SshAuthenticationComposite(sShell,this,SWT.NONE);
		composite.setLayoutData(gridData);
		
//		if(pm!=null){
//		   composite.getCheckBox().setSelection(true);
//		   composite.getUsernameText().setText(pm.getUsername());
//		   composite.getPasswordText().setText(pm.getPassword());
//		   composite.getRegExpText().setText(pm.getRegex());
//		   passwordManager = pm;
//		} else {
//			passwordManager = new PasswordManager();
//		}
		
		if(msg!=null){
			composite.getWarnigLabel().setText(" * "+msg);
		}
		
//		center the dialog screen to the monitor
		Monitor primary = display.getPrimaryMonitor ();
		Rectangle bounds = primary.getBounds ();
		Rectangle rect = sShell.getBounds ();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		sShell.setLocation (x, y);			
		
	}
	
	public void openShell(){  
		//createSShell();	
		sShell.open();
		
		Display display = MainShell.sShell.getDisplay();
		while (!sShell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}		
		display.update();
		//return passwordManager;
	}
	
	public void closeShell(){
		sShell.close();
	}
	
//	public PasswordManager getPasswordManager() {
//		return passwordManager;
//	}
	
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public static void main(String args[]){
		SshAuthenticationDialog dialog = new SshAuthenticationDialog("localhost");
		dialog.openShell();
		
		try {
			while (!dialog.sShell.isDisposed()) {
				if (!dialog.sShell.getDisplay().readAndDispatch())
					dialog.sShell.getDisplay().sleep();
			}
		} catch (Throwable e) {
			e.printStackTrace();			
		} finally {
			System.exit(0);
		}
		
		
	}
	
	public void setLabelTitle(String value){
		composite.setLabelTitle(value);
	}	
	
	public void setUsernameComposite(String value){
		composite.setUsername(value);
	}	

	public String getHostName(){
		return this.hostname;
	}
	
	public Boolean isLDAPUser(){
		return this.isLDAPUser;
	}
	
	protected void setLDAPUser(Boolean isLDAPUser){
		this.isLDAPUser = isLDAPUser;
	}
	
	public void disableUsername(){
		composite.getUsernameText().setEnabled(false);
	}
	
	public void disableLDAPCheckBox(){
		composite.getCheckBox().setEnabled(false);
	}
	
	public void disableMethod(){
		composite.getComboBox().setEnabled(false);
	}
	
	public void enableMethod(){
		composite.getComboBox().setEnabled(true);
	}

	public SshAuthenticationComposite getComposite() {
		return composite;
	}

	public String getLabelWarningValue() {
		return labelWarningValue;
	}

	public void setLabelWarningValue(String labelWarningValue) {
		this.labelWarningValue = labelWarningValue;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}	
	
	
}

