package com.mxdeploy.launcher.account;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
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

import com.mxdeploy.images.Constant;
import com.mxdeploy.launcher.account.event.AccountEvent;

public class AccountComposite extends Composite {

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

	private Label label = null;
	private Label lblSelectTheWorkspace;

	private CCombo cCombo = null;

	//private Button createButton = null;

	private Label errorLabel = null;
	
	private Shell sShell = null;
	
	private String retorno = null;
	 
	public AccountComposite(Composite parent, int style) {
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
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.horizontalSpacing = 0;
		gridLayout1.marginWidth = 0;
		gridLayout1.verticalSpacing = 0;
		gridLayout1.marginHeight = 0;
		createTopComposite();
		this.setLayout(gridLayout1);
		createCenterComposite();
		createBotomComposite();
		this.setSize(new Point(624, 217));
		cCombo.setFocus();
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
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		gridData1.grabExcessVerticalSpace = true;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData1.horizontalIndent = 0;
		gridData1.verticalSpan = 3;
		gridData1.grabExcessHorizontalSpace = true;
		GridData gridData = new GridData();
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
		//imageTop.setImage(Constant.IMAGE_MXDEPLOY_MEDIO);
		topLabel = new Label(topComposite, SWT.NONE);
		topLabel.setText("  Workspace"); 
		topLabel.setFont(new Font(Display.getDefault(), "Tahoma", 12, SWT.NORMAL));
		topLabel.setLayoutData(gridData11);
		topLabel.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		label1 = new Label(topComposite, SWT.NONE);
		label1.setText("        Select the workspace");
		label1.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
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
		gridLayout4.numColumns = 3;
		GridData gridData4 = new GridData();
		gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		gridData4.grabExcessHorizontalSpace = true;
		composite = new Composite(botomComposite, SWT.NONE);
		composite.setLayoutData(gridData4);
		composite.setLayout(gridLayout4);
		
		OKButton = new Button(composite, SWT.NONE);
		OKButton.setText("      OK      ");
		OKButton.setFont(new Font(Display.getDefault(), "Tahoma", 10, SWT.NORMAL));
		OKButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				AccountComposite composite = (AccountComposite)((Button)e.widget).getParent().getParent().getParent();
				AccountEvent.execute(composite);
			}
		});
		cancelButton = new Button(composite, SWT.NONE);
		cancelButton.setText("    Cancel    ");
		cancelButton.setFont(new Font(Display.getDefault(), "Tahoma", 10,SWT.NORMAL));
		cancelButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				System.exit(0);
			}
		});
	}

	/**
	 * This method initializes composite1
	 * 
	 */
	private void createComposite1() {
		GridData gridData6 = new GridData();
		gridData6.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData6.grabExcessHorizontalSpace = true;
		GridData gridData5 = new GridData();
		gridData5.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData5.grabExcessVerticalSpace = true;
		gridData5.grabExcessHorizontalSpace = true;
		gridData5.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout5 = new GridLayout();
		gridLayout5.numColumns = 1;
		gridLayout5.marginHeight = 20;
		gridLayout5.marginWidth = 20;
		gridLayout5.verticalSpacing = 5;
		gridLayout5.horizontalSpacing = 5;
		composite1 = new Composite(centerComposite, SWT.NONE);
		composite1.setLayout(gridLayout5);
		composite1.setLayoutData(gridData5);
		lblSelectTheWorkspace = new Label(composite1, SWT.NONE);
		lblSelectTheWorkspace.setText("Select the workspace");
		cCombo = new CCombo(composite1, SWT.BORDER);
		cCombo.setLayoutData(gridData6);
		
		cCombo.addKeyListener( new KeyListener() {
            
            public void keyPressed( KeyEvent e ) {
            }

            public void keyReleased( KeyEvent e ) {
            	if( e.keyCode == SWT.CR ){ 
            		AccountComposite composite = (AccountComposite)((CCombo)e.widget).getParent().getParent().getParent();
            		AccountEvent.execute(composite);
            	}
            }
        });
		

		errorLabel = new Label(composite1, SWT.NONE);
		errorLabel.setText("");
		errorLabel.setForeground(new Color(Display.getCurrent(), 255, 0, 0));
	}

	/**
	 * @return the cCombo
	 */
	public CCombo getCCombo() {
		return cCombo;
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

//	/**
//	 * @return the createButton
//	 */
//	public Button getCreateButton() {
//		return createButton;
//	}

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
