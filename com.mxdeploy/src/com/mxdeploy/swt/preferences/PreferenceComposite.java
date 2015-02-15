package com.mxdeploy.swt.preferences;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.mxdeploy.images.Constant;
import com.mxdeploy.swt.preferences.event.SelectEventHandler;

public class PreferenceComposite extends Composite {

	public Composite footerComposite = null;
	public Button saveButton = null;

	public Button cancelButton = null;
	private Label topLabel = null;
	public Composite topComposite = null;
	private Label imageTop = null;
	private Composite centerComposite;
	public Composite botomComposite = null;
	public Composite composite1 = null;

	private Composite composite = null;
	private Button OKButton = null;
	private Label label1;
	private Tree tree;
	private SashForm sashForm;
	private TreeItem trtmSsh;
	private TreeItem trtmTunnel;
	private TreeItem trtmProxy;
	private TreeItem trtmTunnels;
	private TreeItem trtmWindow;
	private Composite righComposite;
	private CTabFolder tabFolder;

	public CTabFolder getTabFolder(){
		return tabFolder;
	}
	
	public PreferenceComposite(Composite parent, int style) {
		super(parent, style);
		initialize();
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
		OKButton.setFont(new Font(Display.getDefault(), "Tahoma", 10,SWT.NORMAL));
		OKButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		
		cancelButton = new Button(composite, SWT.NONE);
		cancelButton.setText("    Cancel    ");
		cancelButton.setFont(new Font(Display.getDefault(), "Tahoma", 10, SWT.NORMAL));
		cancelButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {

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
		gridLayout.marginBottom = 20;
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
		topLabel.setText("  Preference");
		topLabel.setFont(new Font(Display.getDefault(), "Tahoma", 12, SWT.NORMAL));
		topLabel.setLayoutData(gridData11);
		topLabel.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		label1 = new Label(topComposite, SWT.NONE);
		label1.setText("        Fill up all fields and Save");
		label1.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
	}

	private void createCenterComposite() {
		GridData gd_centerComposite = new GridData();
		gd_centerComposite.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gd_centerComposite.grabExcessVerticalSpace = true;
		gd_centerComposite.grabExcessHorizontalSpace = true;
		gd_centerComposite.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		centerComposite = new Composite(this, SWT.NONE);
		createComposite1();
		centerComposite.setLayoutData(gd_centerComposite);
		
		sashForm = new SashForm(centerComposite, SWT.NONE);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 0, 0));
		tree = new Tree(sashForm, SWT.NONE);
		tree.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) { 
				SelectEventHandler handler = new SelectEventHandler(e);
				handler.execute();
			}
		});
			
		trtmWindow = new TreeItem(tree, SWT.NONE);
		trtmWindow.setText("Window");
			
		trtmSsh = new TreeItem(tree, SWT.NONE);
		trtmSsh.setText("Connection");
				
		trtmTunnel = new TreeItem(trtmSsh, SWT.NONE);
		trtmTunnel.setText("SSH");
					
		trtmProxy = new TreeItem(trtmTunnel, SWT.NONE);
		trtmProxy.setText("Proxy");
					
		trtmTunnels = new TreeItem(trtmTunnel, SWT.NONE);
		trtmTunnels.setText("Tunnels");			
		trtmTunnel.setExpanded(true);
				
		trtmSsh.setExpanded(true);
		
		righComposite = new Composite(sashForm, SWT.NONE);
		righComposite.setLayout(new GridLayout(1, false));
			
		tabFolder = new CTabFolder(righComposite, SWT.SINGLE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
		sashForm.setWeights(new int[] {174, 503});
		
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
		GridLayout gl_centerComposite = new GridLayout(1, false);
		gl_centerComposite.horizontalSpacing = 0;
		gl_centerComposite.verticalSpacing = 0;
		gl_centerComposite.marginWidth = 0;
		gl_centerComposite.marginHeight = 0;
		centerComposite.setLayout(gl_centerComposite);
	}
} // @jve:decl-index=0:visual-constraint="10,10"
