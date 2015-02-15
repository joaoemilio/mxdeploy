package com.mxdeploy.swt.project;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.mxdeploy.images.Constant;
import com.mxdeploy.swt.project.helper.ProjectHelper;

public class ProjectComposite extends Composite {

	public Label nameLabel = null; 
	public Text nmText = null;
	public Label aliasLabel = null;
	public Text aliasText = null;
	public Label descriptionLabel = null;
	public Text descriptionTextArea = null;
	public Composite footerComposite = null;
	private Label label1 = null;
	public Button saveButton = null;
	
	public Button cancelButton = null;
	private Label topLabel = null;
	public Composite topComposite = null;
	private Label imageTop = null;
	public Composite centerComposite = null;
	public Composite botomComposite = null;	
	public Composite composite1 = null;
	
	private Composite composite = null;
	private Button OKButton = null;
	
	public ProjectHelper helper = null;
	//private Label typeLabel = null;
	public Text typeText = null;
	
	public ProjectComposite(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() { 
		helper = new ProjectHelper(this);
		
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.horizontalSpacing = 0;
		gridLayout1.marginWidth = 0;
		gridLayout1.verticalSpacing = 0;
		gridLayout1.marginHeight = 0;
		createTopComposite();
		this.setLayout(gridLayout1);
		createCenterComposite();
		createBotomComposite();
	}
	
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
				   helper.refreshTreeItem();
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
		imageTop.setImage(Constant.IMAGE_MXDEPLOY_MEDIO);
		topLabel = new Label(topComposite, SWT.NONE);
		topLabel.setText("  Project Form"); 
		topLabel.setFont(new Font(Display.getDefault(), "Tahoma", 12, SWT.NORMAL));
		topLabel.setLayoutData(gridData11);
		topLabel.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		label1 = new Label(topComposite, SWT.NONE);
		label1.setText("        Fill up all fields and Save");
		label1.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
	}
	
	
	private void createCenterComposite() {
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData2.grabExcessVerticalSpace = true;
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.horizontalSpacing = 0;
		gridLayout2.marginWidth = 0;
		gridLayout2.verticalSpacing = 0;
		gridLayout2.marginHeight = 0;
		centerComposite = new Composite(this, SWT.NONE);
		centerComposite.setLayout(gridLayout2);
		createComposite1();
		centerComposite.setLayoutData(gridData2);
	}
	
	private void createComposite1() {
		GridData gridData9 = new GridData();
		gridData9.grabExcessHorizontalSpace = true;
		gridData9.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData8 = new GridData();
		gridData8.grabExcessHorizontalSpace = true;
		gridData8.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData7 = new GridData();
		gridData7.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData7.grabExcessHorizontalSpace = true;
		GridData gridData6 = new GridData();
		gridData6.grabExcessHorizontalSpace = true;
		gridData6.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData5 = new GridData();
		gridData5.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData5.grabExcessVerticalSpace = true;
		gridData5.grabExcessHorizontalSpace = true;
		gridData5.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout5 = new GridLayout();
		gridLayout5.marginHeight = 20;
		gridLayout5.marginWidth = 20;
		gridLayout5.verticalSpacing = 0;
		gridLayout5.horizontalSpacing = 0;
		composite1 = new Composite(centerComposite, SWT.NONE);
		composite1.setLayout(gridLayout5);
		composite1.setLayoutData(gridData5);
		nameLabel = new Label(composite1, SWT.NONE);
		nameLabel.setText("Name :");
		nmText = new Text(composite1, SWT.BORDER);
		nmText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		nmText.setTextLimit(250);
		aliasLabel = new Label(composite1, SWT.NONE);
		aliasLabel.setText("Alias :");
		aliasText = new Text(composite1, SWT.BORDER);
		aliasText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		aliasText.setTextLimit(100);
		
				descriptionLabel = new Label(composite1, SWT.NONE);
				descriptionLabel.setText("Description :");
		descriptionTextArea = new Text(composite1, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		descriptionTextArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		descriptionTextArea.setTextLimit(500);
	}
	
	
	

}  //  @jve:decl-index=0:visual-constraint="10,10"
