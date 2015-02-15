package com.mxscript.swt.property;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.mxdeploy.api.domain.Property;
import com.mxscript.swt.property.event.PropertyEvent;

public class PropertyComposite extends Composite {

	private Composite topComposite = null;
	private Label topImage = null;
	private Composite composite = null;
	private Label titleLabel = null;
	private Label label = null;
	private Label lblPleaseFillUp;
	private Composite composite1 = null;
	private Composite composite2 = null;
	private Composite composite3 = null;
	private Button okButton = null;
	private Button cancelButton = null;
	private Composite composite4 = null;
	private Label label1 = null;
	private Text nameText = null;
	private Label valueLabel = null;
	private StyledText valueText = null;
	private Label warnigLabel = null;
	private Shell sShell;

	private Label label2 = null;
	private Combo typeCombo = null;

	private PropertyDialog dialog;
	



	public PropertyComposite(Composite parent, PropertyDialog dialog, int style) {
		super(parent, style);
		this.sShell = (Shell) parent;
		this.dialog = dialog;
		initialize();
	}

	private void initialize() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginHeight = 0;
		createTopComposite();
		this.setLayout(gridLayout);
		createComposite1();
		createComposite2();
		setSize(new Point(504, 327));
	}

	/**
	 * This method initializes topComposite
	 * 
	 */
	private void createTopComposite() {
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData1.grabExcessVerticalSpace = true;
		gridData1.grabExcessHorizontalSpace = true;
		GridData gridData = new GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.horizontalSpacing = 0;
		gridLayout1.marginWidth = 0;
		gridLayout1.verticalSpacing = 0;
		gridLayout1.numColumns = 2;
		gridLayout1.marginHeight = 0;
		topComposite = new Composite(this, SWT.NONE);
		topComposite.setBackground(new Color(Display.getCurrent(), 255, 255,
				255));
		topComposite.setLayout(gridLayout1);
		createComposite();
		topComposite.setLayoutData(gridData);
		topImage = new Label(topComposite, SWT.RIGHT);
		topImage.setText("Label");
		topImage.setLayoutData(gridData1);
		topImage.setImage(new Image(Display.getCurrent(), getClass()
				.getResourceAsStream("/MXTerminalMedio.jpg")));
	}

	/**
	 * This method initializes composite
	 * 
	 */
	private void createComposite() {
		GridData gridData9 = new GridData();
		gridData9.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData9.grabExcessHorizontalSpace = true;
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData2.grabExcessHorizontalSpace = true;
		composite = new Composite(topComposite, SWT.NONE);
		composite.setLayout(new GridLayout());
		composite.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		composite.setLayoutData(gridData2);
		titleLabel = new Label(composite, SWT.NONE);
		titleLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		titleLabel.setText("   Property");
		titleLabel
				.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		titleLabel.setFont(new Font(Display.getDefault(), "Tahoma", 10,
				SWT.BOLD));
		lblPleaseFillUp = new Label(composite, SWT.NONE);
		lblPleaseFillUp.setText("       Name and Type fields are riquired");
		lblPleaseFillUp.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		warnigLabel = new Label(composite, SWT.NONE);
		warnigLabel.setText("");
		warnigLabel.setForeground(new Color(Display.getCurrent(), 255, 0, 0));
		warnigLabel.setLayoutData(gridData9);
		warnigLabel.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
	}

	/**
	 * This method initializes composite1
	 * 
	 */
	private void createComposite1() {
		GridData gridData3 = new GridData();
		gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData3.grabExcessHorizontalSpace = true;
		gridData3.grabExcessVerticalSpace = true;
		gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		composite1 = new Composite(this, SWT.BORDER);
		composite1.setLayout(new GridLayout());
		createComposite4();
		composite1.setLayoutData(gridData3);
	}

	/**
	 * This method initializes composite2
	 * 
	 */
	private void createComposite2() {
		GridData gridData4 = new GridData();
		gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData4.grabExcessHorizontalSpace = true;
		composite2 = new Composite(this, SWT.BORDER);
		composite2.setLayout(new GridLayout());
		createComposite3();
		composite2.setLayoutData(gridData4);
	}

	/**
	 * This method initializes composite3
	 * 
	 */
	private void createComposite3() {
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 2;
		GridData gridData5 = new GridData();
		gridData5.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		gridData5.grabExcessHorizontalSpace = true;
		composite3 = new Composite(composite2, SWT.NONE);
		composite3.setLayoutData(gridData5);
		composite3.setLayout(gridLayout2);
		okButton = new Button(composite3, SWT.NONE);
		okButton.setText("     OK     ");
		okButton.setFont(new Font(Display.getDefault(), "Tahoma", 10,SWT.NORMAL));
		okButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				dialog.setLabelWarningValue("");
				(new PropertyEvent()).validate(e);
				dialog.setValue(getValueText().getText());
				dialog.setName(getNameText().getText());
				dialog.setType(dialog.getComposite().getTypeCombo().getText());
				
				if (getWarnigLabel().getText().trim().length() > 0) {
					dialog.setLabelWarningValue(getWarnigLabel().getText());
				} else {
					sShell.close();
				}
			}
		});
		cancelButton = new Button(composite3, SWT.NONE);
		cancelButton.setData(dialog);
		cancelButton.setText("   Cancel   ");
		cancelButton.setFont(new Font(Display.getDefault(), "Tahoma", 10,SWT.NORMAL));
		cancelButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						dialog.setCanceled(true);
						sShell.close();
					}
				});
	}

	/**
	 * This method initializes composite4
	 * 
	 */
	private void createComposite4() {
		GridData gridData7 = new GridData();
		gridData7.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData7.grabExcessHorizontalSpace = true;
		GridLayout gridLayout3 = new GridLayout();
		gridLayout3.numColumns = 2;
		GridData gridData6 = new GridData();
		gridData6.verticalAlignment = SWT.FILL;
		gridData6.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData6.grabExcessVerticalSpace = true;
		gridData6.grabExcessHorizontalSpace = true;
		composite4 = new Composite(composite1, SWT.NONE);
		composite4.setLayoutData(gridData6);
		composite4.setLayout(gridLayout3);
		label2 = new Label(composite4, SWT.NONE);
		label2.setText("Type  :");
		GridData gridData10 = new GridData();
		gridData10.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		typeCombo = new Combo(composite4, SWT.READ_ONLY);
		typeCombo.setLayoutData(gridData10);
		
				typeCombo.add("Local");
				typeCombo.add("Global");
		label1 = new Label(composite4, SWT.NONE);
		label1.setText("Name :");
		nameText = new Text(composite4, SWT.BORDER);
		nameText.setLayoutData(gridData7);
		valueLabel = new Label(composite4, SWT.NONE);
		valueLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		valueLabel.setText("Value :");
		GridData gridData8 = new GridData();
		gridData8.heightHint = 113;
		gridData8.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData8.grabExcessHorizontalSpace = true;
		valueText = new StyledText(composite4, SWT.BORDER | SWT.FULL_SELECTION | SWT.WRAP );
		valueText.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
			}

			public void keyReleased(KeyEvent e) {
				if (e.keyCode == SWT.CR) {
					(new PropertyEvent()).validate(e);
					dialog.setValue(getValueText().getText());
					dialog.setName(getNameText().getText());
					dialog.setType(dialog.getComposite().getTypeCombo().getText());
					if (getWarnigLabel().getText().trim().length() > 0) {
						dialog.setLabelWarningValue(getWarnigLabel().getText());
					} else {
						sShell.close();
					}					
				}
			}
		});
		valueText.setLayoutData(gridData8);
		
	}

	/**
	 * @return the cancelButton
	 */
	public Button getCancelButton() {
		return cancelButton;
	}

	/**
	 * @return the okButton
	 */
	public Button getOkButton() {
		return okButton;
	}

	/**
	 * @return the regExpText
	 */
	public Combo getTypeCombo() {
		return typeCombo;
	}

	/**
	 * @return the passwordText
	 */
	public StyledText getValueText() {
		return valueText;
	}

	/**
	 * @return the usernameText
	 */
	public Text getNameText() {
		return nameText;
	}

	/**
	 * @return the warnigLabel
	 */
	public Label getWarnigLabel() {
		return warnigLabel;
	}

	/**
	 * @return the sShell
	 */
	public Shell getSShell() {
		return sShell;
	}

	public void setLabelTitle(String value) {
		titleLabel.setText("   " + value);
	}

	public void setUsername(String value) {
		nameText.setText(value);
	}
	
	public PropertyDialog getDialog(){
		return dialog;
	}

} // @jve:decl-index=0:visual-constraint="10,10"

