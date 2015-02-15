package com.mxterminal.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

public class VTComposite extends Composite {

	private Group group1 = null;
	private Button enablePassthroughPrintCheckBox = null;
	private Button copyOnSelectCheckBox = null;
	private Button CopyCRNLLineEndsCheckBox = null;
	private Button InsertModeCheckBox = null;
	private Button ReverseVideoCheckBox = null;
	private Button AutoWraparoundcheckBox = null;
	private Button reverseWraproundCheckBox = null;
	private Button autoLinefeedCheckBox = null;
	private Group group2 = null;
	private Button visibleCursorCheckBox = null;
	private Button localEchoCheckBox = null;
	private Button visualBellCheckBox = null;
	private Button mapCTRLSPCToCheckBox = null;
	private Button localPguppgdownCheckBox = null;
	private Button useASCIIcheckBox = null;
	private Label backspaceLabel = null;
	private CCombo backspaceCCombo = null;
	private Label deleteSendLabel = null;
	private CCombo deleteSendsCCombo = null;
	public VTComposite(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		createGroup1();
		this.setLayout(gridLayout);
		createGroup2();
		setSize(new Point(359, 275));
		backspaceLabel = new Label(this, SWT.NONE);
		backspaceLabel.setText("Backspace Sends :");
		backspaceLabel.setLayoutData(gridData1);
		backspaceCCombo = new CCombo(this, SWT.BORDER);
		backspaceCCombo.add("del");
		backspaceCCombo.add("bs");
		backspaceCCombo.add("erase");
		deleteSendLabel = new Label(this, SWT.NONE);
		deleteSendLabel.setText("Delete Sends :");
		deleteSendLabel.setLayoutData(gridData2);
		deleteSendsCCombo = new CCombo(this, SWT.BORDER);
		deleteSendsCCombo.add("del");
		deleteSendsCCombo.add("bs");
		deleteSendsCCombo.add("erase");
	}

	/**
	 * This method initializes group1	
	 *
	 */
	private void createGroup1() {
		group1 = new Group(this, SWT.NONE);
		group1.setLayout(new GridLayout());
		group1.setText("Group 1");
		enablePassthroughPrintCheckBox = new Button(group1, SWT.CHECK);
		enablePassthroughPrintCheckBox.setText("Enable Passthrough Print");
		copyOnSelectCheckBox = new Button(group1, SWT.CHECK);
		copyOnSelectCheckBox.setText("Copy on Select");
		CopyCRNLLineEndsCheckBox = new Button(group1, SWT.CHECK);
		CopyCRNLLineEndsCheckBox.setText("Copy <cr><nl> line ends");
		InsertModeCheckBox = new Button(group1, SWT.CHECK);
		InsertModeCheckBox.setText("Insert Mode");
		AutoWraparoundcheckBox = new Button(group1, SWT.CHECK);
		AutoWraparoundcheckBox.setText("Auto Wraparound");
		reverseWraproundCheckBox = new Button(group1, SWT.CHECK);
		reverseWraproundCheckBox.setText("Reverse Wraparound");
		ReverseVideoCheckBox = new Button(group1, SWT.CHECK);
		ReverseVideoCheckBox.setText("Reverse Video");
		autoLinefeedCheckBox = new Button(group1, SWT.CHECK);
		autoLinefeedCheckBox.setText("Auto Linefeed");
	}

	/**
	 * This method initializes group2	
	 *
	 */
	private void createGroup2() {
		GridData gridData = new GridData();
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		group2 = new Group(this, SWT.NONE);
		group2.setLayout(new GridLayout());
		group2.setLayoutData(gridData);
		group2.setText("Group 2");
		visibleCursorCheckBox = new Button(group2, SWT.CHECK);
		visibleCursorCheckBox.setText("Visible Cursor");
		localEchoCheckBox = new Button(group2, SWT.CHECK);
		localEchoCheckBox.setText("Local Echo");
		visualBellCheckBox = new Button(group2, SWT.CHECK);
		visualBellCheckBox.setText("Visual Bell");
		mapCTRLSPCToCheckBox = new Button(group2, SWT.CHECK);
		mapCTRLSPCToCheckBox.setText("Map <CTRL>+SPC To ^@");
		localPguppgdownCheckBox = new Button(group2, SWT.CHECK);
		localPguppgdownCheckBox.setText("Local PgUp/PgDown");
		useASCIIcheckBox = new Button(group2, SWT.CHECK);
		useASCIIcheckBox.setText("Use ASCII for line draw");
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
