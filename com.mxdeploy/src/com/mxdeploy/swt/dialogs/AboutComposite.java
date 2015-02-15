package com.mxdeploy.swt.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.mxdeploy.api.util.Util;

public class AboutComposite extends Composite {

	private Label label = null;
	private Label versioLabel = null;
	private Label buildLlabel = null;
	private Label brazilLabel = null;
	private Label fabioLabel = null;
	private Label joaoLabel = null;
	private Button button = null;
	public AboutComposite(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		GridData gridData3 = new GridData();
		gridData3.grabExcessVerticalSpace = true;
		gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.END;
		GridData gridData = new GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.END;
		GridLayout gridLayout = new GridLayout();
		gridLayout.verticalSpacing = 10;
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData2.grabExcessHorizontalSpace = true;
		label = new Label(this, SWT.NONE);
		label.setText("Deployment Suite");
		label.setForeground(new Color(Display.getCurrent(), 0, 128, 192));
		label.setLayoutData(gridData2);
		label.setFont(new Font(Display.getDefault(), "Arial", 10, SWT.BOLD));
		versioLabel = new Label(this, SWT.NONE);
		versioLabel.setText("Version : "+Util.getCurrVersion());
		versioLabel.setFont(new Font(Display.getDefault(), "Tahoma", 8, SWT.BOLD));
		buildLlabel = new Label(this, SWT.NONE);
		buildLlabel.setText("Build id : 02/09/2014");
		buildLlabel.setFont(new Font(Display.getDefault(), "Tahoma", 8, SWT.BOLD));
		brazilLabel = new Label(this, SWT.NONE);
		brazilLabel.setText("Powered by :");
		brazilLabel.setFont(new Font(Display.getDefault(), "Tahoma", 8, SWT.BOLD));
		fabioLabel = new Label(this, SWT.NONE);
		fabioLabel.setText("Fabio Santos - fsbsilva@gmail.com");
		fabioLabel.setFont(new Font(Display.getDefault(), "Tahoma", 8, SWT.BOLD));
		button = new Button(this, SWT.NONE);
		button.setText("           OK            ");
		button.setLayoutData(gridData);
		button.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				Shell shell = (Shell)getParent();
				shell.close();
				shell.dispose();
			}
		});
		this.setLayout(gridLayout);
		setSize(new Point(367, 236));
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
