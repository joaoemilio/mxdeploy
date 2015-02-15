package com.mxterminal.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class MiscComposite extends Composite {

	private Label pasteButtonLabel = null;
	private CCombo pasteButtonCCombo = null;
	private Label selectDelimLabel = null;
	private Text selectDelimText = null;
	private Button ignoreNullBytesCheckBox = null;
	public MiscComposite(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		pasteButtonLabel = new Label(this, SWT.NONE);
		pasteButtonLabel.setText("Paste button :");
		pasteButtonCCombo = new CCombo(this, SWT.BORDER);
		pasteButtonCCombo.add("middle");
		pasteButtonCCombo.add("right");
		pasteButtonCCombo.add("shift+left");
		selectDelimLabel = new Label(this, SWT.NONE);
		selectDelimLabel.setText("Select delim. :");
		selectDelimText = new Text(this, SWT.BORDER);
		@SuppressWarnings("unused")
		Label filler = new Label(this, SWT.NONE);
		ignoreNullBytesCheckBox = new Button(this, SWT.CHECK);
		ignoreNullBytesCheckBox.setText("Ignore null bytes");
		this.setLayout(gridLayout);
		setSize(new Point(300, 200));
	}

}
