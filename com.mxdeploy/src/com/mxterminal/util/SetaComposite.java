package com.mxterminal.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class SetaComposite extends Composite {

	private ToolBar toolBar = null;
	public SetaComposite(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.numColumns = 1;
		gridLayout.horizontalSpacing = 0;
		this.setLayout(gridLayout);
		createToolBar();
		this.setSize(new Point(185, 27));
	}

	/**
	 * This method initializes toolBar	
	 *
	 */
	private void createToolBar() {
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		toolBar = new ToolBar(this, SWT.FLAT);
		toolBar.setLayoutData(gridData);
		ToolItem toolItem = new ToolItem(toolBar, SWT.PUSH);
		toolItem.setHotImage(new Image(Display.getCurrent(), getClass().getResourceAsStream("/enabled/hide.gif")));
		toolItem.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream("/enabled/hide.gif")));
	}

}  //  @jve:decl-index=0:visual-constraint="-1,49"
