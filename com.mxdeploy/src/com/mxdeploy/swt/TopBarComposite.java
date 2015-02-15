package com.mxdeploy.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

public class TopBarComposite extends Composite {

	private Label label = null;
	private Label label1 = null;
	private Label label2 = null;

	public TopBarComposite(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		
		setBackground(new Color(Display.getCurrent(),68,119,187));
		
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData2.grabExcessVerticalSpace = false;
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		GridData gridData1 = new GridData();
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		GridData gridData = new GridData();
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.horizontalSpacing = 0;
		label = new Label(this, SWT.NONE);
		label.setText("Label");
		label.setBackground(new Color(Display.getCurrent(),68,119,187));
		label.setLayoutData(gridData);
		label.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream("/enabled/id-w3-sitemark-small.gif")));
		label2 = new Label(this, SWT.NONE);
		label2.setText("");
		label2.setBackground(new Color(Display.getCurrent(),68,119,187));
		
		label2.setLayoutData(gridData2);
		label1 = new Label(this, SWT.NONE);
		label1.setText("Label");
		label1.setLayoutData(gridData1);
		label1.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream("/enabled/banner_blue.jpg")));
		this.setLayout(gridLayout);
		setSize(new Point(779, 33));
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
