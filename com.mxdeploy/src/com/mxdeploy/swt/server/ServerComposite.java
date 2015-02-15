package com.mxdeploy.swt.server;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.mxdeploy.images.Constant;

public class ServerComposite extends Composite {

	private Composite topComposite = null;

	private Label imageTop = null;

	private Composite centerComposite = null;

	private Composite botomComposite = null;

	private Label topLabel = null;

	private Label label1 = null;

	private Composite composite = null;

	private Button OKButton = null;

	private Button cancelButton = null; 

	private Composite composite1 = null;

	private Label errorLabel = null;
	
	private Shell sShell = null;
	
	private String retorno = null;

	protected Label nameLabel = null;

	protected Text nameServerText = null;

	protected Label hostnameLabel = null;

	protected Text hostnameText = null;

	protected Label addressLabel = null;

	protected Text addressText = null;
	 
	protected ServerEditHelper helper = null;
	
	public ServerComposite(Composite parent, int style) {
		super(parent, style);
		sShell = (Shell)parent;
		initialize();
	}
	
	/**
	 * @return the sShell
	 */
	public Shell getSShell() {
		return sShell;
	}

	private void initialize() { 
		helper = new ServerEditHelper(this);
		
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.horizontalSpacing = 0;
		gridLayout1.marginWidth = 0;
		gridLayout1.verticalSpacing = 0;
		gridLayout1.marginHeight = 0;
		createTopComposite();
		this.setLayout(gridLayout1);
		createCenterComposite();
		createBotomComposite();
		this.setSize(new Point(447, 486));
	}

	/**
	 * This method initializes topComposite
	 * 
	 */ 
	private void createTopComposite() {
		GridData gridData11 = new GridData(); 
		gridData11.horizontalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		gridData11.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = SWT.RIGHT;
		gridData1.grabExcessVerticalSpace = true;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData1.horizontalIndent = 0;
		gridData1.verticalSpan = 4;
		gridData1.grabExcessHorizontalSpace = true;
		GridData gridData = new GridData();
		gridData.heightHint = 71;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.numColumns = 2;
		gridLayout.marginHeight = 0;
		topComposite = new Composite(this, SWT.NONE);
		topComposite.setBackground(new Color(Display.getCurrent(), 255, 255,255));
		topComposite.setLayout(gridLayout);
		topComposite.setLayoutData(gridData);
		@SuppressWarnings("unused")
		Label filler2 = new Label(topComposite, SWT.NONE);
		imageTop = new Label(topComposite, SWT.NONE);
		imageTop.setText("");
		imageTop.setLayoutData(gridData1);
		imageTop.setImage(Constant.IMAGE_MXDEPLOY_MEDIO);
		topLabel = new Label(topComposite, SWT.NONE);
		topLabel.setText("  Server Form"); 
		topLabel.setFont(new Font(Display.getDefault(), "Tahoma", 12, SWT.NORMAL));
		topLabel.setLayoutData(gridData11);
		topLabel.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		label1 = new Label(topComposite, SWT.NONE);
		label1.setText("        Fill up all fields and Save");
		label1.setBackground(new Color(Display.getCurrent(), 255, 255, 255));

		errorLabel = new Label(topComposite, SWT.NONE);
		errorLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT));
		errorLabel.setText("                                                                                    ");
		GridData gd_errorLabel = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_errorLabel.verticalIndent = 5;
		errorLabel.setLayoutData(gd_errorLabel);
		errorLabel.setForeground(new Color(Display.getCurrent(), 255, 0, 0));
		new Label(topComposite, SWT.NONE);
		new Label(topComposite, SWT.NONE);
	}

	/**
	 * This method initializes centerComposite
	 * 
	 */
	private void createCenterComposite() {
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData2.grabExcessVerticalSpace = true;
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.horizontalSpacing = 1;
		gridLayout2.marginWidth = 1;
		gridLayout2.verticalSpacing = 1;
		gridLayout2.marginHeight = 1;
		centerComposite = new Composite(this, SWT.NONE);
		centerComposite.setLayout(gridLayout2);
		createComposite1();
		centerComposite.setLayoutData(gridData2);
	}

	/**
	 * This method initializes botomComposite
	 * 
	 */
	private void createBotomComposite() {
		GridData gridData3 = new GridData();
		gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData3.grabExcessHorizontalSpace = true;
		gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.END;
		GridLayout gridLayout3 = new GridLayout();
		gridLayout3.horizontalSpacing = 0;
		gridLayout3.marginWidth = 2;
		gridLayout3.verticalSpacing = 0;
		gridLayout3.numColumns = 2;
		gridLayout3.marginHeight = 2;
		botomComposite = new Composite(this, SWT.NONE);
		botomComposite.setLayout(gridLayout3);
		botomComposite.setLayoutData(gridData3);
		@SuppressWarnings("unused")
		Label filler = new Label(botomComposite, SWT.NONE);
		createComposite();
	}

	/**
	 * This method initializes composite
	 * 
	 */
	private void createComposite() {
		GridLayout gridLayout4 = new GridLayout();
		gridLayout4.numColumns = 2;
		GridData gridData4 = new GridData();
		gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		gridData4.grabExcessHorizontalSpace = true;
		composite = new Composite(botomComposite, SWT.NONE);
		composite.setLayoutData(gridData4);
		composite.setLayout(gridLayout4);
		
		OKButton = new Button(composite, SWT.NONE);
		OKButton.setText("      Save      ");
		OKButton.setFont(new Font(Display.getDefault(), "Tahoma", 10, SWT.NORMAL));
		OKButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if(helper.save()){
				   helper.refreshServerTreeItem();
				   helper.close(); 
				}
			}
		}); 		
		cancelButton = new Button(composite, SWT.NONE);
		cancelButton.setText("    Cancel    ");
		cancelButton.setFont(new Font(Display.getDefault(), "Tahoma", 10,SWT.NORMAL));
		cancelButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				helper.close();
			}
		});
	}

	/**
	 * This method initializes composite1
	 * 
	 */
	private void createComposite1() {
		GridData gridData6 = new GridData();
		gridData6.horizontalSpan = 2;
		gridData6.grabExcessHorizontalSpace = true;
		gridData6.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData5 = new GridData();
		gridData5.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData5.grabExcessVerticalSpace = true;
		gridData5.grabExcessHorizontalSpace = true;
		gridData5.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout5 = new GridLayout();
		gridLayout5.numColumns = 2;
		gridLayout5.marginHeight = 20;
		gridLayout5.marginWidth = 20;
		gridLayout5.verticalSpacing = 5;
		gridLayout5.horizontalSpacing = 5;
		composite1 = new Composite(centerComposite, SWT.NONE);
		composite1.setLayout(gridLayout5);
		composite1.setLayoutData(gridData5);
		nameLabel = new Label(composite1, SWT.NONE);
		nameLabel.setText("*Name :");
		new Label(composite1, SWT.NONE);
		nameServerText = new Text(composite1, SWT.BORDER);
		nameServerText.setLayoutData(gridData6);
		hostnameLabel = new Label(composite1, SWT.NONE);
		hostnameLabel.setText("*Hostname :");
		new Label(composite1, SWT.NONE);
		GridData gridData7 = new GridData();
		gridData7.horizontalSpan = 2;
		gridData7.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData7.grabExcessHorizontalSpace = true;
		hostnameText = new Text(composite1, SWT.BORDER);
		hostnameText.setLayoutData(gridData7);
		addressLabel = new Label(composite1, SWT.NONE);
		addressLabel.setText("IP Address");
		new Label(composite1, SWT.NONE);
		GridData gridData9 = new GridData();
		gridData9.horizontalSpan = 2;
		gridData9.grabExcessHorizontalSpace = true;
		gridData9.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		addressText = new Text(composite1, SWT.BORDER);
		addressText.setLayoutData(gridData9);
	}

	/**
	 * @return the oKButton
	 */
	public Button getOKButton() {
		return OKButton;
	}

	/**
	 * @return the errorLabel
	 */
	public Label getErrorLabel() {
		return errorLabel;
	}

	/**
	 * @return the retorno
	 */
	public String getRetorno() {
		return retorno;
	}

	/**
	 * @param retorno the retorno to set
	 */
	public void setRetorno(String retorno) {
		this.retorno = retorno;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
