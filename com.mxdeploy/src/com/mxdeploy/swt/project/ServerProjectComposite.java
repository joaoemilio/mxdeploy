package com.mxdeploy.swt.project;

import org.eclipse.swt.SWT;
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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import com.mxdeploy.images.Constant;
import com.mxdeploy.swt.project.helper.AddServerProjectHelper;

public class ServerProjectComposite extends Composite {

	public Composite topComposite = null;

	public Label imageTop = null;

	public Composite centerComposite = null;

	public Composite botomComposite = null;

	public Label topLabel = null;

	public Label label1 = null;

	public Composite composite = null;

	public Button OKButton = null;

	public Button cancelButton = null;

	public Composite composite1 = null;

	public Label errorLabel = null;

	public Shell sShell = null;

	public String retorno = null;

	public Text nameSearchText;
	
	public Combo columnSearchCCombo = null;
	public Table resultServerTable;
	public Table serverSelectedTable = null;
    
	public AddServerProjectHelper helper = null;
    
	public ServerProjectComposite(Composite parent, int style) {
		super(parent, style);
		sShell = (Shell) parent;
		initialize();
	}

	/**
	 * @return the sShell
	 */
	public Shell getSShell() {
		return sShell;
	}

	private void initialize() {
		helper = new AddServerProjectHelper(this);
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.horizontalSpacing = 0;
		gridLayout1.marginWidth = 0;
		gridLayout1.verticalSpacing = 0;
		gridLayout1.marginHeight = 0;
		createTopComposite();
		this.setLayout(gridLayout1);
		createCenterComposite();
		createBotomComposite();
		this.setSize(new Point(499, 434));
		helper.loadComboBox();
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
		topComposite.setBackground(new Color(Display.getCurrent(), 255, 255,
				255));
		topComposite.setLayout(gridLayout);
		topComposite.setLayoutData(gridData);
		@SuppressWarnings("unused")
		Label filler2 = new Label(topComposite, SWT.NONE);
		imageTop = new Label(topComposite, SWT.NONE);
		imageTop.setText("");
		imageTop.setLayoutData(gridData1);
		imageTop.setImage(Constant.IMAGE_MXDEPLOY_MEDIO);
		topLabel = new Label(topComposite, SWT.NONE);
		topLabel.setText("  Add Server to Project");
		topLabel.setFont(new Font(Display.getDefault(), "Tahoma", 12,
				SWT.NORMAL));
		topLabel.setLayoutData(gridData11);
		topLabel.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		label1 = new Label(topComposite, SWT.NONE);
		label1.setText("        Put server(s) to right side and Save");
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
				helper.save();
				helper.refreshTree();
				helper.close();
			}
		});		
		cancelButton = new Button(composite, SWT.NONE);
		cancelButton.setText("    Cancel    ");
		cancelButton.setFont(new Font(Display.getDefault(), "Tahoma", 10,SWT.NORMAL));
		cancelButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				helper.refreshTree();
				helper.close();
			}
		});
	}

	/**
	 * This method initializes composite1
	 * 
	 */
	private void createComposite1() {
		GridData gridData5 = new GridData();
		gridData5.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData5.grabExcessVerticalSpace = true;
		gridData5.grabExcessHorizontalSpace = true;
		gridData5.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout5 = new GridLayout();
		gridLayout5.marginRight = 5;
		gridLayout5.marginBottom = 5;
		gridLayout5.marginLeft = 5;
		gridLayout5.marginTop = 5;
		gridLayout5.verticalSpacing = 5;
		gridLayout5.horizontalSpacing = 5;
		composite1 = new Composite(centerComposite, SWT.NONE);
		composite1.setLayout(gridLayout5);
		composite1.setLayoutData(gridData5);
		
		Composite composite_1 = new Composite(composite1, SWT.NONE);
		composite_1.setLayout(new GridLayout(3, false));
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		{
			Label lblSearch = new Label(composite_1, SWT.NONE);
			lblSearch.setBounds(0, 0, 65, 16);
			lblSearch.setText("Search :");
			
		}
		
		Label label = new Label(composite_1, SWT.NONE);
		label.setText("                                                            ");
		new Label(composite_1, SWT.NONE);
		
		columnSearchCCombo = new Combo(composite_1, SWT.NONE);
		columnSearchCCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		nameSearchText = new Text(composite_1, SWT.BORDER);
		nameSearchText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnSearch = new Button(composite_1, SWT.NONE);
		btnSearch.setText("Search");
		btnSearch.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				resultServerTable.removeAll();
				helper.search();
			}
		});		
		
		Composite composite_2 = new Composite(composite1, SWT.NONE);
		composite_2.setLayout(new GridLayout(3, false));
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label lblChooseServer = new Label(composite_2, SWT.NONE);
		lblChooseServer.setText("Server(s) found  ");
		new Label(composite_2, SWT.NONE);
		
		Label lblServerChoosen = new Label(composite_2, SWT.NONE);
		lblServerChoosen.setText("Server(s) Choosen");
		
		resultServerTable = new Table(composite_2, SWT.BORDER | SWT.MULTI);
		resultServerTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		resultServerTable.setHeaderVisible(false);
		resultServerTable.setLinesVisible(false);
		resultServerTable.addMouseListener(new org.eclipse.swt.events.MouseListener() {
			public void mouseDoubleClick(org.eclipse.swt.events.MouseEvent e) {
				helper.addItemServerSelectedTable();
			}
			public void mouseDown(org.eclipse.swt.events.MouseEvent e) {
			}
			public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
			}
		});		
		
		Composite composite_3 = new Composite(composite_2, SWT.NONE);
		GridLayout gl_composite_3 = new GridLayout(1, false);
		gl_composite_3.marginWidth = 0;
		composite_3.setLayout(gl_composite_3);
		
		Button btnAdd = new Button(composite_3, SWT.NONE);
		btnAdd.setBounds(0, 0, 82, 28);
		btnAdd.setText("  >  ");
		btnAdd.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				helper.addItemServerSelectedTable();
			}
		});		
		Button btnRemove = new Button(composite_3, SWT.NONE);
		btnRemove.setBounds(0, 0, 82, 28);
		btnRemove.setText("  <  ");
		btnRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				helper.removeItemServerSelectedTable();
			}
		});
		
		serverSelectedTable = new Table(composite_2, SWT.BORDER | SWT.MULTI);
		serverSelectedTable.setHeaderVisible(false);
		serverSelectedTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		serverSelectedTable.setLinesVisible(false);
		serverSelectedTable.addMouseListener(new org.eclipse.swt.events.MouseListener() {
			public void mouseDoubleClick(org.eclipse.swt.events.MouseEvent e) {
				helper.removeItemServerSelectedTable();
			}
			public void mouseDown(org.eclipse.swt.events.MouseEvent e) {
			}
			public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
			}
		});		
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
	 * @param retorno
	 *            the retorno to set
	 */
	public void setRetorno(String retorno) {
		this.retorno = retorno;
	}
} // @jve:decl-index=0:visual-constraint="10,10"
