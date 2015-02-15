package com.mxdeploy.swt.dialogs.authentication.event;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;

import com.mxdeploy.swt.dialogs.authentication.SshAuthenticationComposite;

public class AutenticationEvent {
	
	private SshAuthenticationComposite composite;
	
	public void validate(org.eclipse.swt.events.TypedEvent e){
		composite = (SshAuthenticationComposite)((Control)e.widget).getParent().getParent().getParent();
		composite.getWarnigLabel().setText("");
		if( composite.getUsernameText().getText().trim().length()==0 ){
			composite.getWarnigLabel().setText("   * Username is empty !");
			return;
		}
		
		if( composite.getPasswordText().getText().trim().length()==0 ){
			composite.getWarnigLabel().setText("   * Password is empty !");
			return;
		}		
		
		if( composite.getHostnameText().getText().trim().length()==0 ){
			composite.getWarnigLabel().setText("   * Hostname is empty !");
			return;
		}			
		
	}
	
	public void close(org.eclipse.swt.events.SelectionEvent e){
		SshAuthenticationComposite composite = (SshAuthenticationComposite)((Button)e.widget).getParent().getParent().getParent();
		composite.getSShell().close();
	}
	
	
	
}
