package com.mxdeploy.swt.dialogs;

import org.eclipse.swt.SWT;

import com.mxdeploy.swt.MainShell;


public class LoginHelper {

	protected LoginComposite composite = null;
	
	public static final int MSG_OK = 0;
	public static final int MSG_CANCEL = 1;
	public static final int MSG_INCORRECT = 2;
	public static final int MSG_ERROR = 3;
	
	public LoginHelper(LoginComposite composite){
		this.composite = composite;
	}	
	
	protected int connect(){
		composite.okButton.setEnabled(false);
		composite.cancelButton.setEnabled(false);
		composite.passwordText.setEditable(false);
		composite.userText.setEditable(false);
	    try
	    {		
			if(composite.userText.getText().trim().length()==0 || composite.passwordText.getText().trim().length()==0){
				MainShell.sendMessage("Username or Password can't be null !", SWT.ICON_WARNING);
				composite.result= MSG_ERROR;
			} 
	    	  composite.result= MSG_OK;
	    	  MainShell.getMainHelper().intranetID = composite.userText.getText();
	    	  composite.result= MSG_INCORRECT;
	    }
	    catch(Exception e){
	    	//MainShell.sendMessage("Bluepages isn't reachable !", SWT.ICON_WARNING);
	    	e.printStackTrace();
	    	composite.result= MSG_INCORRECT;	    	
	    }finally{
			composite.okButton.setEnabled(true);
			composite.cancelButton.setEnabled(true);
			composite.passwordText.setEditable(true);
			composite.userText.setEditable(true);
	    }
	    
	    return composite.result;
		
	}
	
}
