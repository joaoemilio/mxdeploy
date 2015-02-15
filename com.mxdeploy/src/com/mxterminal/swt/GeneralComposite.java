package com.mxterminal.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class GeneralComposite extends Composite {

	private Label terminalTypeLabel = null;
	private Combo terminalTypeCombo = null;
	private Label columnsLabel = null;
	private Text columnsText = null;
	private Label encodingLabel = null;
	private Label fontLabel = null;
	private Combo encodingCombo = null;
	private Label rowsLabel = null;
	private Text rowsText = null;
	private Combo fontCombo = null;
	private Label sizeLabel = null;
	private Text sizeText = null;
	private Label scrollbackBufferLabel = null;
	private Text scrollBufferText = null;
	public GeneralComposite(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 2;
		this.setLayout(gridLayout1);
		this.setSize(new Point(278, 248));
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		terminalTypeLabel = new Label(this, SWT.NONE);
		terminalTypeLabel.setText("Terminal Type :");
		createTerminalTypeCombo();
		this.setLayout(gridLayout);
		columnsLabel = new Label(this, SWT.NONE);
		columnsLabel.setText("Columns :");
		columnsText = new Text(this, SWT.BORDER);
		rowsLabel = new Label(this, SWT.NONE);
		rowsLabel.setText("Rows :");
		rowsText = new Text(this, SWT.BORDER);
		encodingLabel = new Label(this, SWT.NONE);
		encodingLabel.setText("Encoding :");
		createEncodingCombo();
		fontLabel = new Label(this, SWT.NONE);
		fontLabel.setText("Font :");
		createFontCombo();
		sizeLabel = new Label(this, SWT.NONE);
		sizeLabel.setText("Size :");
		sizeText = new Text(this, SWT.BORDER);
		scrollbackBufferLabel = new Label(this, SWT.NONE);
		scrollbackBufferLabel.setText("Scrollback buffer :");
		scrollBufferText = new Text(this, SWT.BORDER);
		setSize(new Point(347, 276));
	}

	/**
	 * This method initializes terminalTypeCombo	
	 *
	 */
	private void createTerminalTypeCombo() {
		terminalTypeCombo = new Combo(this, SWT.NONE);
		terminalTypeCombo.add("xterm");
		terminalTypeCombo.add("linux");
		terminalTypeCombo.add("scoansi");
		terminalTypeCombo.add("att6386");
		terminalTypeCombo.add("sun");
		terminalTypeCombo.add("aixterm");
		terminalTypeCombo.add("vt220");
		terminalTypeCombo.add("vt100");
	}

	/**
	 * This method initializes encodingCombo	
	 *
	 */
	private void createEncodingCombo() {
		encodingCombo = new Combo(this, SWT.NONE);
		encodingCombo.add("iso-8859-1");
		encodingCombo.add("utf-8");
		encodingCombo.add("euc-ip");
		encodingCombo.add("euc-kr");
		encodingCombo.add("us-ascii");
	}

	/**
	 * This method initializes fontCombo	
	 *
	 */
	private void createFontCombo() {
		fontCombo = new Combo(this, SWT.NONE);
		fontCombo.add("Monospaced");
		fontCombo.add("Dialog");
		fontCombo.add("SansSerif");
		fontCombo.add("Serif");
		fontCombo.add("DialogInput");
		fontCombo.add("Courier New");
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
