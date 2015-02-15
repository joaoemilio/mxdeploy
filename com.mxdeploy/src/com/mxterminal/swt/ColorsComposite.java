package com.mxterminal.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class ColorsComposite extends Composite {

	private Label ForegroundLabel = null;
	private CCombo feregroundColorCombo = null;
	private Text foregroundColorText = null;
	private Label BackgroundColorLabel = null;
	private CCombo backgroundColorCombo = null;
	private Text backgroundText = null;
	private Label cursorColorLabel = null;
	private CCombo cursorCombo = null;
	private Text cursorText = null;

	public ColorsComposite(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		ForegroundLabel = new Label(this, SWT.NONE);
		ForegroundLabel.setText("Foreground color :");
		this.setLayout(gridLayout);
		createFeregroundColorCombo();
		setSize(new Point(300, 200));
		foregroundColorText = new Text(this, SWT.BORDER);
		foregroundColorText.setEditable(false);
		foregroundColorText.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		foregroundColorText.setEnabled(false);
		BackgroundColorLabel = new Label(this, SWT.NONE);
		BackgroundColorLabel.setText("Background color :");
		createBackgroundColorCombo();
		backgroundText = new Text(this, SWT.BORDER);
		backgroundText.setBackground(new Color(Display.getCurrent(), 0, 255, 0));
		backgroundText.setEnabled(false);
		backgroundText.setEditable(false);
		cursorColorLabel = new Label(this, SWT.NONE);
		cursorColorLabel.setText("Cursor color :");
		createCursorCombo();
		cursorText = new Text(this, SWT.BORDER);
		cursorText.setBackground(new Color(Display.getCurrent(), 255, 128, 0));
		cursorText.setEnabled(false);
		cursorText.setEditable(false);
	}

	/**
	 * This method initializes feregroundColorCombo	
	 *
	 */
	private void createFeregroundColorCombo() {
		feregroundColorCombo = new CCombo(this, SWT.BORDER);
		addColors(feregroundColorCombo);
	}
	
	private void addColors(CCombo combo){
		combo.add("custom",0);
		combo.add("white",SWT.COLOR_WHITE);
		combo.add("black",SWT.COLOR_BLACK);
		combo.add("red",SWT.COLOR_RED);
		combo.add("dark red",SWT.COLOR_DARK_RED);
		combo.add("green",SWT.COLOR_GREEN);
		combo.add("dark green",SWT.COLOR_DARK_GREEN);
		combo.add("yellow",SWT.COLOR_YELLOW);
		combo.add("dark yellow",SWT.COLOR_DARK_YELLOW);
		combo.add("blue",SWT.COLOR_BLUE);
		combo.add("dark blue",SWT.COLOR_DARK_BLUE);
		combo.add("magenta",SWT.COLOR_MAGENTA);
		combo.add("dark magenta",SWT.COLOR_DARK_MAGENTA);
		combo.add("cyan",SWT.COLOR_CYAN);
		combo.add("dark cyan",SWT.COLOR_DARK_CYAN);
		combo.add("gray",SWT.COLOR_GRAY);
		combo.add("dark gray",SWT.COLOR_DARK_GRAY);
	}
	
	
	private Color getColor(int vColor){
		return Display.getCurrent().getSystemColor(vColor);
	}

	/**
	 * This method initializes backgroundColorCombo	
	 *
	 */
	private void createBackgroundColorCombo() {
		backgroundColorCombo = new CCombo(this, SWT.BORDER);
		addColors(backgroundColorCombo);
	}

	/**
	 * This method initializes cursorCombo	
	 *
	 */
	private void createCursorCombo() {
		cursorCombo = new CCombo(this, SWT.BORDER);
		addColors(cursorCombo);
	}

}
