package com.mxdeploy.swt.command;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

public class CommandItemComposite extends Composite {

	protected Label commandSSHLable = null;
	protected Composite buttonComposite = null;
	protected Button saveButton = null;
	protected Button cancelButton = null;
	protected Label label = null;
	protected StyledText commandSSHStyleText = null;
	
	protected CommandItemHelper helper = null;  
	
	public CommandItemComposite(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		helper = new CommandItemHelper(this);
		
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData2.grabExcessVerticalSpace = true;
		gridData2.horizontalSpan = 2;
		gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.marginHeight = 5;
		gridLayout.marginWidth = 5;
		gridLayout.verticalSpacing = 5;
		gridLayout.horizontalSpacing = 5;
		commandSSHLable = new Label(this, SWT.NONE);
		commandSSHLable.setText("Command SSH :");
		label = new Label(this, SWT.NONE);
		label.setText("                                                                                         ");
		commandSSHStyleText = new StyledText(this, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		commandSSHStyleText.setFont(new Font(Display.getDefault(), "Tahoma", 10, SWT.NORMAL));
		commandSSHStyleText.setLayoutData(gridData2);
		createButtonComposite();
		this.setLayout(gridLayout);
		setSize(new Point(786, 396));
		//helper.checkPermission();
	}

	/**
	 * This method initializes buttonComposite	
	 *
	 */
	private void createButtonComposite() {
		GridData gridData4 = new GridData();
		gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData4.grabExcessHorizontalSpace = true;
		GridData gridData3 = new GridData();
		gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData3.grabExcessHorizontalSpace = true;
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 2;
		GridData gridData = new GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		buttonComposite = new Composite(this, SWT.NONE);
		buttonComposite.setLayoutData(gridData);
		buttonComposite.setLayout(gridLayout1);
		saveButton = new Button(buttonComposite, SWT.NONE);
		saveButton.setText("Save");
		saveButton.setLayoutData(gridData3);
		saveButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
//				if(helper.create()){
//				   helper.refreshServerCommandTable();
//				   helper.close();
//				}
				helper.addCommandItem(); 
				helper.close();
			}
		});
		cancelButton = new Button(buttonComposite, SWT.NONE);
		cancelButton.setText("Cancel");
		cancelButton.setLayoutData(gridData4);
		cancelButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				helper.close();
			}
		});
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
