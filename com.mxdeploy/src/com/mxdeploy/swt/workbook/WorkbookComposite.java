package com.mxdeploy.swt.workbook;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class WorkbookComposite extends Composite {

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

	protected WorkbookHelper helper = null;
	
	protected Text nameText;
	protected Text nodeNameText;
	protected Table nodeTable;
	protected Combo webServerCombo;
	protected Combo verticalCombo;
	
	public WorkbookComposite(Composite parent, int style) {
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
		helper = new WorkbookHelper(this);
		
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.horizontalSpacing = 0;
		gridLayout1.marginWidth = 0;
		gridLayout1.verticalSpacing = 0;
		gridLayout1.marginHeight = 0;
		createTopComposite();
		this.setLayout(gridLayout1);
		createCenterComposite();
		createBotomComposite();
		this.setSize(new Point(536, 517));
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
		gridData1.verticalSpan = 4;
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
		topComposite = new Composite(this, SWT.BORDER);
		topComposite.setBackground(new Color(Display.getCurrent(), 255, 255,255));
		topComposite.setLayout(gridLayout);
		topComposite.setLayoutData(gridData);
		@SuppressWarnings("unused")
		Label filler2 = new Label(topComposite, SWT.NONE);
		imageTop = new Label(topComposite, SWT.NONE);
		imageTop.setText("");
		imageTop.setLayoutData(gridData1);
		imageTop.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream("/MXTerminalMedio.jpg")));
		topLabel = new Label(topComposite, SWT.NONE);
		topLabel.setText("  Workbook Creation"); 
		topLabel.setFont(new Font(Display.getDefault(), "Tahoma", 12, SWT.NORMAL));
		topLabel.setLayoutData(gridData11);
		topLabel.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		label1 = new Label(topComposite, SWT.NONE);
		label1.setText("        Fill up all fields and Save");
		label1.setBackground(new Color(Display.getCurrent(), 255, 255, 255));

		errorLabel = new Label(topComposite, SWT.NONE);
		errorLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT));
		errorLabel.setText("                                                                                    ");
		GridData gd_errorLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
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
		centerComposite = new Composite(this, SWT.BORDER);
		centerComposite.setLayout(gridLayout2);
		createComposite1();
		centerComposite.setLayoutData(gridData2);
		{
			Composite composite_1 = new Composite(centerComposite, SWT.NONE);
			GridLayout gl_composite_1 = new GridLayout(4, false);
			gl_composite_1.marginWidth = 20;
			composite_1.setLayout(gl_composite_1);
			composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
			{
				Label lblNode = new Label(composite_1, SWT.NONE);
				lblNode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
				lblNode.setBounds(0, 0, 65, 16);
				lblNode.setText("Node : ");
			}
			{
				nodeNameText = new Text(composite_1, SWT.BORDER);
				nodeNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			}
			{
				Button btnNodeAdd = new Button(composite_1, SWT.NONE);
				btnNodeAdd.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						if( nodeNameText.getText().length() > 0 ){
							TableItem tableItem = new TableItem(nodeTable, SWT.NONE);
							tableItem.setText(nodeNameText.getText());
						}
					}
				});
				btnNodeAdd.setText("Add");
			}
			{
				Button btnNodeRemove = new Button(composite_1, SWT.NONE);
				btnNodeRemove.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						if( nodeTable.getSelectionIndex() != -1 ){
							nodeTable.remove(nodeTable.getSelectionIndex());
						}
					}
				});
				btnNodeRemove.setText("Remove");
			}
		}
		
		Composite composite_2 = new Composite(centerComposite, SWT.BORDER);
		GridLayout gl_composite_2 = new GridLayout(1, false);
		gl_composite_2.marginWidth = 0;
		gl_composite_2.marginHeight = 0;
		gl_composite_2.verticalSpacing = 0;
		composite_2.setLayout(gl_composite_2);
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		nodeTable = new Table(composite_2, SWT.BORDER | SWT.FULL_SELECTION);
		nodeTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		nodeTable.setBounds(0, 0, 30, 44);
		nodeTable.setHeaderVisible(true);
		nodeTable.setLinesVisible(true);
		
		TableColumn tblclmnName = new TableColumn(nodeTable, SWT.NONE);
		tblclmnName.setWidth(100);
		tblclmnName.setText("Name");
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
		botomComposite = new Composite(this, SWT.BORDER);
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
				   //helper.refreshServerTreeItem();				   
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
		GridData gridData5 = new GridData();
		gridData5.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData5.grabExcessHorizontalSpace = true;
		gridData5.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout5 = new GridLayout();
		gridLayout5.numColumns = 4;
		gridLayout5.marginWidth = 20;
		gridLayout5.verticalSpacing = 5;
		gridLayout5.horizontalSpacing = 5;
		composite1 = new Composite(centerComposite, SWT.NONE);
		composite1.setLayout(gridLayout5);
		composite1.setLayoutData(gridData5);
		
		Label lblNewLabel = new Label(composite1, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Name :");
		{
			nameText = new Text(composite1, SWT.BORDER);
			nameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		}
		
		Label lblVertical = new Label(composite1, SWT.NONE);
		lblVertical.setBounds(0, 0, 65, 16);
		lblVertical.setText("Vertical :");
		
		verticalCombo = new Combo(composite1, SWT.READ_ONLY);
		verticalCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		verticalCombo.add("0");
		verticalCombo.add("2");
		verticalCombo.add("3");
		verticalCombo.add("4");
		verticalCombo.select(0);
		{
			Label lblWebserver = new Label(composite1, SWT.NONE);
			lblWebserver.setBounds(0, 0, 65, 16);
			lblWebserver.setText("WebServer :");
		}
		
		webServerCombo = new Combo(composite1, SWT.READ_ONLY);
		webServerCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		webServerCombo.add("Intranet");
		webServerCombo.add("Internet");
		webServerCombo.add("WebServices");
		webServerCombo.add("Intranet and WebServices");
		webServerCombo.add("Internet and WebServices");
		webServerCombo.select(0);
		new Label(composite1, SWT.NONE);
		new Label(composite1, SWT.NONE);

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
