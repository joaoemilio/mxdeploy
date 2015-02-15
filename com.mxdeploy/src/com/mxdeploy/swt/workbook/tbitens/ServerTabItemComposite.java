package com.mxdeploy.swt.workbook.tbitens;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class ServerTabItemComposite extends Composite {
	private Text initialHeapSizeText;
	private Text maxHeapSizeText;
	private Text genericJVMArgumentsText;
	private Text cookieNameText;
	private Text sessionTimeoutText;
	private Combo appClassLoaderPolicyCombo;
	private Combo appClassLoaderModeCombo;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ServerTabItemComposite(Composite parent, int style) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		gridLayout.horizontalSpacing = 0;
		setLayout(gridLayout);
		
		Composite composite = new Composite(this, SWT.NONE);
		GridData gd_composite = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_composite.heightHint = 220;
		composite.setLayoutData(gd_composite);
		GridLayout gl_composite = new GridLayout(4, false);
		gl_composite.marginWidth = 20;
		gl_composite.marginHeight = 20;
		composite.setLayout(gl_composite);
		
		Label label = new Label(composite, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label.setText("Initial Heap Size :");
		
		initialHeapSizeText = new Text(composite, SWT.BORDER);
		initialHeapSizeText.setText("512");
		initialHeapSizeText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		initialHeapSizeText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				if(!verifyNumber(e))
					e.doit = false;
			}
		});
		
		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_1.setText("Max Heap Size :");
		
		maxHeapSizeText = new Text(composite, SWT.BORDER);
		maxHeapSizeText.setText("512");
		maxHeapSizeText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		maxHeapSizeText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				if(!verifyNumber(e))
					e.doit = false;
			}
		});
		
		Label label_2 = new Label(composite, SWT.NONE);
		label_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_2.setText("Generic Jvm Arguments :");
		
		genericJVMArgumentsText = new Text(composite, SWT.BORDER);
		genericJVMArgumentsText.setText("-Xlp -Dcom.ibm.cacheLocalHost=true -Djava.net.preferIPv4Stack=true -Dibm.dg.trc.print=st_verify");
		genericJVMArgumentsText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		Label label_5 = new Label(composite, SWT.NONE);
		label_5.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_5.setText("Application Class Loader Policy :");
		
		appClassLoaderPolicyCombo = new Combo(composite, SWT.READ_ONLY);
		appClassLoaderPolicyCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		appClassLoaderPolicyCombo.add("MULTIPLE");
		appClassLoaderPolicyCombo.add("SINGLE");
		appClassLoaderPolicyCombo.select(0);
		
		Label label_6 = new Label(composite, SWT.NONE);
		label_6.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_6.setText("Application Class Loading Mode");
		
		appClassLoaderModeCombo = new Combo(composite, SWT.READ_ONLY);
		appClassLoaderModeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		appClassLoaderModeCombo.add("PARENT_FIRST");
		appClassLoaderModeCombo.add("PARENT_LAST");
		appClassLoaderModeCombo.select(0);
		
		Label label_7 = new Label(composite, SWT.NONE);
		label_7.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_7.setText("Session Timeout :");
		
		sessionTimeoutText = new Text(composite, SWT.BORDER);
		sessionTimeoutText.setText("30");
		sessionTimeoutText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		sessionTimeoutText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				if(!verifyNumber(e))
					e.doit = false;
			}
		});		
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Label label_3 = new Label(composite, SWT.NONE);
		label_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_3.setText("Cookie Name :");
		label_3.setAlignment(SWT.RIGHT);
		
		cookieNameText = new Text(composite, SWT.BORDER);
		cookieNameText.setText("JSESSIONID");
		cookieNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public Text getInitialHeapSizeText() {
		return initialHeapSizeText;
	}

	public void setInitialHeapSizeText(Text initialHeapSizeText) {
		this.initialHeapSizeText = initialHeapSizeText;
	}

	public Text getGenericJVMArgumentsText() {
		return genericJVMArgumentsText;
	}

	public void setGenericJVMArgumentsText(Text genericJVMArgumentsText) {
		this.genericJVMArgumentsText = genericJVMArgumentsText;
	}

	public Text getCookieNameText() {
		return cookieNameText;
	}

	public void setCookieNameText(Text cookieNameText) {
		this.cookieNameText = cookieNameText;
	}

	public Text getSessionTimeoutText() {
		return sessionTimeoutText;
	}

	public void setSessionTimeoutText(Text sessionTimeoutText) {
		this.sessionTimeoutText = sessionTimeoutText;
	}

	public Text getMaxHeapSizeText() {
		return maxHeapSizeText;
	}

	public void setMaxHeapSizeText(Text maxHeapSizeText) {
		this.maxHeapSizeText = maxHeapSizeText;
	}

	public Combo getAppClassLoaderPolicyCombo() {
		return appClassLoaderPolicyCombo;
	}

	public void setAppClassLoaderPolicyCombo(Combo appClassLoaderPolicyCombo) {
		this.appClassLoaderPolicyCombo = appClassLoaderPolicyCombo;
	}

	public Combo getAppClassLoaderModeCombo() {
		return appClassLoaderModeCombo;
	}

	public void setAppClassLoaderModeCombo(Combo appClassLoaderModeCombo) {
		this.appClassLoaderModeCombo = appClassLoaderModeCombo;
	}
	
    public boolean verifyNumber(VerifyEvent e){
		boolean isInteger = true;
		if( e.keyCode == 8 || e.keyCode == 127 ){
			return isInteger;
		} else {
			try{
				Integer.parseInt(e.text);
			} catch(NumberFormatException ex ){
				isInteger = false;
			}
		}
		return isInteger;
    }
	

}
