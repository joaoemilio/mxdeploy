package com.mxdeploy.swt.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class PasswordComposite extends Composite {
 
	public Text textPassword = null;
	protected Button OK = null;
	protected Button buttonCancel = null;
	protected boolean returnOK = false;
	protected Label label = null;
	protected Shell  shell = null;
	protected String result;
	
	public String getPassword(){
		return textPassword.getText();
	}

	public PasswordComposite(Composite parent, int style) {
		super(parent, style);
		shell = (Shell)parent;
		initialize();
	}	

	private void initialize() {
        GridData gridData = new GridData();
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		
        GridData gridData3 = new GridData();
        gridData3.grabExcessHorizontalSpace = true;
        gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        
        GridData gridData1 = new GridData();
        gridData1.grabExcessVerticalSpace = true;
        gridData1.grabExcessHorizontalSpace = true;
        
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        this.setLayout(gridLayout);
        
		label = new Label(this, SWT.NONE);
		label.setFont(new Font(Display.getDefault(), "Tahoma", 12, SWT.NORMAL));
		label.setText("Password:");
		textPassword = new Text(this, SWT.BORDER|SWT.PASSWORD);
		textPassword.setFont(new Font(Display.getDefault(), "Tahoma", 12, SWT.NORMAL));
		textPassword.setLayoutData(gridData3);
		
		textPassword.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
        	public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
        		switch (e.character){
    				case SWT.CR :{
    					result = textPassword.getText();
    					shell.close();
    				}
        		}
        	}
        });
		OK = new Button(this, SWT.NONE);
		OK.setFont(new Font(Display.getDefault(), "Tahoma", 12, SWT.NORMAL));
		OK.setText("LOGIN");
		OK.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
			public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
				//System.out.println("mouseUp()"); // TODO Auto-generated Event stub mouseUp()
			}
		});
		OK.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				result = textPassword.getText();
				shell.close();
			}
		});
		OK.setLayoutData(gridData);
		
		buttonCancel = new Button(this, SWT.NONE);
		buttonCancel.setFont(new Font(Display.getDefault(), "Tahoma", 12, SWT.NORMAL));
		buttonCancel.setText("EXIT");
		buttonCancel.setLayoutData(gridData1);
		buttonCancel.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				result = "STOP_PROCEDURE";
				shell.close();
			}
		});
		
		

		setSize(new Point(180, 71));
		
		textPassword.forceFocus();
	}

	public boolean isReturnOK() {
		return returnOK;
	}

	public void setReturnOK(boolean returnOK) {
		this.returnOK = returnOK;
	}
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
