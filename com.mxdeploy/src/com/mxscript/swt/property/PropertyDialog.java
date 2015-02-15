package com.mxscript.swt.property;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

import xml.properties;

import com.mxdeploy.api.domain.Database;
import com.mxdeploy.swt.MainShell;

public class PropertyDialog {

	private Shell sShell = null; // @jve:decl-index=0:visual-constraint="10,10"
	private PropertyComposite composite;
	private Display display = Display.getDefault(); // @jve:decl-index=0:
	private String value; // @jve:decl-index=0:
	private String name; // @jve:decl-index=0:
	private String type;
	private Boolean canceled = new Boolean(false);
	private String labelWarningValue = null;
	private properties propXML;

	public PropertyDialog() {
		createSShell();
	}

	public Boolean getCanceled() {
		return canceled;
	}

	public void setCanceled(Boolean canceled) {
		this.canceled = canceled;
	}

	private void createSShell() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				createSShell(null);
			}
		});
	}

	private void createSShell(String msg) {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginHeight = 0;
		sShell = new Shell(MainShell.sShell, SWT.APPLICATION_MODAL
				| SWT.DIALOG_TRIM);
		sShell.setText("SSH Authentication - " + Database.WORKSPACE_NAME);
		sShell.setImage(new Image(Display.getCurrent(), getClass()
				.getResourceAsStream("/enabled/alt16.gif")));
		sShell.setLayout(gridLayout);
		sShell.setSize(new Point(524, 327));

		composite = new PropertyComposite(sShell, this, SWT.NONE);
		composite.setLayoutData(gridData);

		if (msg != null) {
			composite.getWarnigLabel().setText(" * " + msg);
		}

		// center the dialog screen to the monitor
		Monitor primary = display.getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = sShell.getBounds();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		sShell.setLocation(x, y);

	}

	public void openShell() {
		if( getType()!=null && getType().equals("Global")){
			composite.getTypeCombo().select(1);
		} else if( getType()!=null && getType().equals("Local")){
			composite.getTypeCombo().select(0);
		} else {
			composite.getTypeCombo().select(0);	
		}
		
		if( getName()!=null ){
			composite.getNameText().setText( getName() );
		}
		
		if( getValue()!=null ){
			composite.getValueText().setText( getValue() );
		} 		
		
		sShell.open();

		Display display = MainShell.sShell.getDisplay();
		while (!sShell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.update();
	}

	public void closeShell() {
		sShell.close();
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static void main(String args[]) {
		PropertyDialog dialog = new PropertyDialog();
		dialog.openShell();

		try {
			while (!dialog.sShell.isDisposed()) {
				if (!dialog.sShell.getDisplay().readAndDispatch())
					dialog.sShell.getDisplay().sleep();
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}

	}

	public void setLabelTitle(String value) {
		composite.setLabelTitle(value);
	}

	public void setNameComposite(String name) {
		composite.getNameText().setText(name);
	}

	public PropertyComposite getComposite() {
		return composite;
	}

	public String getLabelWarningValue() {
		return labelWarningValue;
	}

	public void setLabelWarningValue(String labelWarningValue) {
		this.labelWarningValue = labelWarningValue;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public properties getPropXML() {
		return propXML;
	}

	public void setPropXML(properties propXML) {
		this.propXML = propXML;
	}
	
	

}
