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

import com.mxdeploy.swt.MainShell;

public class ExpectComposite extends Composite {

	public Text variavelExpect = null;
	protected Button OK = null;
	protected Button buttonCancel = null;
	protected boolean returnOK = false;
	protected Label label = null;
	protected Shell  shell = null;
	protected String result;
	private Composite compositeButton = null;
	private Button okButton = null;
	private Button cancelButton = null;
	public ExpectComposite(Composite parent, int style) {
		super(parent, style);
		shell = (Shell)parent;
		initialize();
	}

	private void initialize() {
        GridData gridData21 = new GridData();
        gridData21.grabExcessHorizontalSpace = true;
        gridData21.horizontalSpan = 2;
        gridData21.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
        gridData21.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        GridData gridData = new GridData();
        gridData.grabExcessVerticalSpace = true;
        gridData.grabExcessHorizontalSpace = true;
        gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		
        GridData gridData3 = new GridData();
        gridData3.grabExcessHorizontalSpace = true;
        gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        
        GridData gridData1 = new GridData();
        gridData1.grabExcessVerticalSpace = true;
        gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData1.grabExcessHorizontalSpace = true;
        
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3; 
        this.setLayout(gridLayout);
        
		label = new Label(this, SWT.NONE);
		label.setFont(new Font(Display.getDefault(), "Tahoma", 12, SWT.NORMAL));
		label.setLayoutData(gridData21);
		label.setText("Value :");
		@SuppressWarnings("unused")
		Label filler2 = new Label(this, SWT.NONE);
		@SuppressWarnings("unused")
		Label filler1 = new Label(this, SWT.NONE);
		variavelExpect = new Text(this, SWT.BORDER);
		variavelExpect.setFont(new Font(Display.getDefault(), "Tahoma", 12, SWT.NORMAL));
		variavelExpect.setLayoutData(gridData3);
		createCompositeButton();
		
		variavelExpect.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
        	public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
        		switch (e.character){
    				case SWT.CR :{
    					result = variavelExpect.getText();
    					if(result.trim().length()==0){
    						MainShell.sendMessage("The "+label.getText().substring(0,label.getText().length()-1)+" can't be NULL !", SWT.ICON_WARNING);
    						result = null;    						
    						return;
    					}
    					shell.close();
    				}
        		}
        	}
        });

		
		this.setSize(new Point(324, 100));
		
		variavelExpect.forceFocus();
	}

	public boolean isReturnOK() {
		return returnOK;
	}

	public void setReturnOK(boolean returnOK) {
		this.returnOK = returnOK;
	}

	/**
	 * This method initializes compositeButton	
	 *
	 */
	private void createCompositeButton() {
		GridData gridData5 = new GridData();
		gridData5.grabExcessVerticalSpace = true;
		gridData5.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData5.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		gridData5.grabExcessHorizontalSpace = true;
		GridData gridData4 = new GridData();
		gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData4.grabExcessHorizontalSpace = true;
		gridData4.grabExcessVerticalSpace = true;
		gridData4.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 2;
		GridData gridData2 = new GridData();
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		gridData2.horizontalSpan = 2;
		gridData2.grabExcessVerticalSpace = false;
		compositeButton = new Composite(this, SWT.NONE);
		compositeButton.setLayoutData(gridData2);
		compositeButton.setLayout(gridLayout1);
		okButton = new Button(compositeButton, SWT.NONE);
		okButton.setText("OK");
		okButton.setLayoutData(gridData4);
		okButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				result = variavelExpect.getText();
				if(result.trim().length()==0){
					MainShell.sendMessage("The "+label.getText()+" can't be NULL !", SWT.ICON_WARNING);
					result = null;
					return;
				}
				shell.close();
			}
		});
		cancelButton = new Button(compositeButton, SWT.NONE);
		cancelButton.setText("Cancel");
		cancelButton.setLayoutData(gridData5);
		cancelButton
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
    					result = null;
    					shell.close();
					}
				});
	}


}  //  @jve:decl-index=0:visual-constraint="10,10"
