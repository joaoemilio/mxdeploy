package com.mxdeploy.launcher.splash;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;

public class SplashComposite extends Composite {

	private Label label = null;
	private ProgressBar progressBar = null;
	private Label title = null;
    
	public SplashComposite(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		GridData gridData2 = new GridData();
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		GridData gridData1 = new GridData();
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.numColumns = 1;
		gridLayout.marginHeight = 0;
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		label = new Label(this, SWT.NONE);
		label.setText("Label");
		label.setLayoutData(gridData);
		label.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream("/splash.gif")));
		title = new Label(this, SWT.NONE);
		title.setText("    ");
		title.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		title.setLayoutData(gridData2);
		progressBar = new ProgressBar(this, SWT.NONE);
		progressBar.setLayoutData(gridData1);
		progressBar.setVisible(false);
		this.setLayout(gridLayout);
		this.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		setSize(new Point(647, 487));
	}

	/**
	 * @return the progressBar
	 */
	public ProgressBar getProgressBar() {
		return progressBar;
	}

	/**
	 * @return the title
	 */
	public Label getTitle() {
		return title;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
