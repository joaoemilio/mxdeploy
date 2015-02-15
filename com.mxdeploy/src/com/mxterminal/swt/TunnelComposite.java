package com.mxterminal.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

public class TunnelComposite extends Composite {

	protected Group group = null;
	protected Composite composite1 = null;
	protected Button checkBox = null;
	protected Button checkBox1 = null;
	protected Composite composite = null;
	protected CLabel cLabel = null;
	protected Button removeBotton = null;
	protected List list = null;
	protected CLabel cLabel1 = null;
	protected Composite composite2 = null;
	protected CLabel cLabel2 = null;
	protected Text bindPortText = null;
	protected Composite composite3 = null;
	protected Button addButton = null;
	protected Label label = null;
	protected CLabel cLabel3 = null;
	protected Text destinationText = null;
	protected Composite composite4 = null;
	protected Button okButton = null;
	protected Button cancelButton = null;
	protected CLabel cLabel4 = null;
	protected Text destPortText = null;
	
	private TunnelHelper helper = null;
	
	public TunnelComposite(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		helper = new TunnelHelper(this);
		GridLayout gridLayout6 = new GridLayout();
		gridLayout6.numColumns = 2;
		createGroup();
		this.setLayout(gridLayout6);
		setSize(new Point(375, 335));
		@SuppressWarnings("unused")
		Label filler = new Label(this, SWT.NONE);
		createComposite4();
	}

	/**
	 * This method initializes group	
	 *
	 */
	private void createGroup() {
		
		GridLayout gridLayout3 = new GridLayout();
		gridLayout3.numColumns = 1;
		GridData gridData4 = new GridData();
		gridData4.grabExcessHorizontalSpace = true;
		gridData4.grabExcessVerticalSpace = true;
		gridData4.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.horizontalSpan = 2;
		gridData.grabExcessVerticalSpace = true;
		group = new Group(this, SWT.NONE);
		group.setText("Port forwarding");
		createComposite1();
		group.setLayoutData(gridData);
		group.setLayout(gridLayout3);
		createComposite();
		list = new List(group, SWT.BORDER | SWT.V_SCROLL);
		list.setLayoutData(gridData4);
		cLabel1 = new CLabel(group, SWT.NONE);
		cLabel1.setText("Add new forwarded port:");
		createComposite2();
	}

	/**
	 * This method initializes composite1	
	 *
	 */
	private void createComposite1() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		composite1 = new Composite(group, SWT.NONE);
		composite1.setLayout(gridLayout);
		checkBox = new Button(composite1, SWT.CHECK);
		checkBox.setText("Local ports accept connections from other hosts");
		checkBox.setEnabled(false);
		checkBox.setSelection(true);
		checkBox1 = new Button(composite1, SWT.CHECK);
		checkBox1.setText("Remote ports do the same (SSH-2 only)");
		checkBox1.setEnabled(false);
	}

	/**
	 * This method initializes composite	
	 *
	 */
	private void createComposite() {
		GridData gridData3 = new GridData();
		gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData3.grabExcessHorizontalSpace = true;
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		GridData gridData1 = new GridData();
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 2;
		gridLayout1.marginHeight = 0;
		gridLayout1.marginWidth = 0;
		gridLayout1.verticalSpacing = 0;
		gridLayout1.horizontalSpacing = 0;
		composite = new Composite(group, SWT.NONE);
		composite.setLayout(gridLayout1);
		composite.setLayoutData(gridData1);
		cLabel = new CLabel(composite, SWT.NONE);
		cLabel.setText("Forwarded ports :");
		cLabel.setLayoutData(gridData3);
		removeBotton = new Button(composite, SWT.RIGHT);
		removeBotton.setText("Remove");
		removeBotton.setLayoutData(gridData2);
		removeBotton.setEnabled(false);
		removeBotton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				String bind = list.getSelection()[0];
				helper.deleteLocalForward(bind);
				list.remove( list.getSelectionIndex() );
			}
		});
	}

	/**
	 * This method initializes composite2	
	 *
	 */
	private void createComposite2() {
		GridData gridData9 = new GridData();
		gridData9.horizontalSpan = 2;
		gridData9.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData6 = new GridData();
		gridData6.grabExcessHorizontalSpace = true;
		gridData6.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 3;
		gridLayout2.marginHeight = 0;
		gridLayout2.marginWidth = 0;
		gridLayout2.verticalSpacing = 3;
		gridLayout2.horizontalSpacing = 0;
		composite2 = new Composite(group, SWT.NONE);
		composite2.setLayout(gridLayout2);
		composite2.setLayoutData(gridData6);
		cLabel2 = new CLabel(composite2, SWT.NONE);
		cLabel2.setText("Bind port");
		bindPortText = new Text(composite2, SWT.BORDER);
		bindPortText.setTextLimit(6);
		bindPortText.addVerifyListener(new org.eclipse.swt.events.VerifyListener() {
			public void verifyText(org.eclipse.swt.events.VerifyEvent event) {
				event.doit = false;
		        // Get the character typed
		        char myChar = event.character;
		        // Allow 0-9
		        if (Character.isDigit(myChar))
		          event.doit = true;
		        
		        // Allow backspace
		        if (myChar == '\b')
		          event.doit = true;		        
			}
		});
		createComposite3();
		cLabel3 = new CLabel(composite2, SWT.NONE);
		cLabel3.setText("Destination");
		destinationText = new Text(composite2, SWT.BORDER);
		destinationText.setLayoutData(gridData9);
		cLabel4 = new CLabel(composite2, SWT.NONE);
		cLabel4.setText("Dest. port");
		destPortText = new Text(composite2, SWT.BORDER);
		destPortText.addVerifyListener(new org.eclipse.swt.events.VerifyListener() {
			public void verifyText(org.eclipse.swt.events.VerifyEvent event) {
				event.doit = false;
		        // Get the character typed
		        char myChar = event.character;
		        // Allow 0-9
		        if (Character.isDigit(myChar))
		          event.doit = true;
		        
		        // Allow backspace
		        if (myChar == '\b')
		          event.doit = true;
		        
		        
			}
		});
	}

	/**
	 * This method initializes composite3	
	 *
	 */
	private void createComposite3() {
		GridData gridData8 = new GridData();
		gridData8.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData8.grabExcessHorizontalSpace = true;
		GridLayout gridLayout4 = new GridLayout();
		gridLayout4.numColumns = 2;
		gridLayout4.marginWidth = 0;
		gridLayout4.verticalSpacing = 0;
		gridLayout4.horizontalSpacing = 5;
		gridLayout4.marginHeight = 0;
		GridData gridData7 = new GridData();
		gridData7.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		GridData gridData5 = new GridData();
		gridData5.grabExcessHorizontalSpace = true;
		gridData5.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		composite3 = new Composite(composite2, SWT.NONE);
		composite3.setLayoutData(gridData5);
		composite3.setLayout(gridLayout4);
		label = new Label(composite3, SWT.NONE);
		label.setText("    ");
		label.setLayoutData(gridData8);
		addButton = new Button(composite3, SWT.NONE);
		addButton.setText("     Add     ");
		addButton.setLayoutData(gridData7);
		addButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				   helper.add();
			}
		});
	}

	/**
	 * This method initializes composite4	
	 *
	 */
	private void createComposite4() {
		GridLayout gridLayout5 = new GridLayout();
		gridLayout5.numColumns = 2;
		gridLayout5.marginWidth = 0;
		GridData gridData10 = new GridData();
		gridData10.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		gridData10.grabExcessHorizontalSpace = true;
		composite4 = new Composite(this, SWT.NONE);
		composite4.setLayoutData(gridData10);
		composite4.setLayout(gridLayout5);
		okButton = new Button(composite4, SWT.NONE);
		okButton.setText("      OK      ");
		okButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				helper.save();
			}
		});

		cancelButton = new Button(composite4, SWT.NONE);
		cancelButton.setText("    Cancel    ");
		cancelButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				helper.close();
			}
		});
		
	}

	/**
	 * @return the helper
	 */
	public TunnelHelper getHelper() {
		return helper;
	}
	
	

}  //  @jve:decl-index=0:visual-constraint="10,10"
