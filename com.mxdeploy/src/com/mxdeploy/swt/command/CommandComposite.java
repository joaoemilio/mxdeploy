package com.mxdeploy.swt.command;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class CommandComposite extends Composite {

	protected Label nameLabel = null;
	protected Text nmText = null;
	protected Label descriptionLabel = null;
	protected Text descriptionTextArea = null;
	protected Composite footerComposite = null;
	protected Label label1 = null;
	protected Button okButton = null;
	protected Label label7 = null;
	protected Button cancelButton = null;
	protected Label label8 = null;
	
	protected CommandHelper helper = null;
	
	public CommandComposite(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		helper =  new CommandHelper(this);
		
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData2.grabExcessHorizontalSpace = true;
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData1.horizontalSpan = 3;
		gridData1.grabExcessVerticalSpace = true;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData1.grabExcessHorizontalSpace = true;
		@SuppressWarnings("unused")
		Label filler8 = new Label(this, SWT.NONE);
		@SuppressWarnings("unused")
		Label filler9 = new Label(this, SWT.NONE);
		@SuppressWarnings("unused")
		Label filler10 = new Label(this, SWT.NONE);
		@SuppressWarnings("unused")
		Label filler11 = new Label(this, SWT.NONE);
		nameLabel = new Label(this, SWT.NONE);
		nameLabel.setText("Name :");
		@SuppressWarnings("unused")
		Label filler7 = new Label(this, SWT.NONE);
		@SuppressWarnings("unused")
		Label filler4 = new Label(this, SWT.NONE);
		nmText = new Text(this, SWT.BORDER);
		nmText.setLayoutData(gridData2);
		label7 = new Label(this, SWT.NONE);
		label7.setText("         ");
		label1 = new Label(this, SWT.NONE);
		label1.setText("         ");
		descriptionLabel = new Label(this, SWT.NONE);
		descriptionLabel.setText("Description :");
		@SuppressWarnings("unused")
		Label filler = new Label(this, SWT.NONE);
		@SuppressWarnings("unused")
		Label filler1 = new Label(this, SWT.NONE);
		descriptionTextArea = new Text(this, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		descriptionTextArea.setLayoutData(gridData1);
		createFooterComposite();
		this.setLayout(gridLayout);
		this.setSize(new Point(304, 232));
		
	}

	/**
	 * This method initializes footerComposite	
	 *
	 */
	private void createFooterComposite() {
		GridData gridData6 = new GridData();
		gridData6.grabExcessHorizontalSpace = true;
		gridData6.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData5 = new GridData();
		gridData5.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData5.grabExcessHorizontalSpace = true;
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 3;
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = false;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		footerComposite = new Composite(this, SWT.NONE);
		footerComposite.setLayoutData(gridData);
		footerComposite.setLayout(gridLayout1);
		okButton = new Button(footerComposite, SWT.NONE);
		okButton.setText("OK"); 
		okButton.setLayoutData(gridData6);
		okButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if(helper.create()){
				   helper.refreshServerCommandTable();
				   helper.close();
				}
			}
		}); 
		cancelButton = new Button(footerComposite, SWT.NONE);
		cancelButton.setText("Cancel");
		cancelButton.setLayoutData(gridData5);
		cancelButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				helper.close();
			}
		});
		label8 = new Label(footerComposite, SWT.NONE);
		label8.setText("         ");
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
