package com.mxscript.swt;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import xml.properties;

import com.mxdeploy.images.Constant;
import com.mxscript.swt.event.styledText.OpenGeanyEvent;
import com.mxscript.swt.event.styledText.SaveBeanShellScriptEvent;
import com.mxscript.swt.event.styledText.StyleTextLineGetStyleEvent;
import com.mxscript.swt.event.styledText.StyledTextKeyPressedEvent;
import com.mxscript.swt.event.styledText.StyledTextModifyEvent;
import com.mxterminal.swt.util.TerminalProperty;

public class BeanShellScriptComposite extends Composite {

	private StyledText styledText = null;
	private StyledText styledText1 = null;
	private Label label = null;
	private Composite composite = null;
	private ToolBar toolBar = null;
	private ToolBar toolBar_1;
	private ToolItem saveToolItem;

	
	static Logger logger = Logger.getLogger(BeanShellScriptComposite.class);
	
	public BeanShellScriptComposite(Composite parent, int style) {
		super(parent, style);
		initialize();
		styledText.setFocus();
		saveToolItem.setEnabled(false);
		
		ToolItem tltmNewItem = new ToolItem(toolBar_1, SWT.NONE);
		tltmNewItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				OpenGeanyEvent open = new OpenGeanyEvent();
				open.execute();
			}
		});
		if( TerminalProperty.getBeanshellGeanyEnable().contains("true") 
		 || TerminalProperty.getBeanshellGeanyEnable().contains("True") ){
			tltmNewItem.setEnabled(true);
		} else {
			tltmNewItem.setEnabled(false);
		}
		tltmNewItem.setImage(Constant.IMAGE_GEANY);
	}

	private void initialize() {
        GridData gridData1 = new GridData();
        gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData1.grabExcessVerticalSpace = true; 
        GridData gridData = new GridData();
        gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData.grabExcessVerticalSpace = true;
        gridData.grabExcessHorizontalSpace = true;
        gridData.verticalSpan = 2; 
        gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
        createComposite();
        styledText1 = new StyledText(this, SWT.READ_ONLY);
        styledText1.setFont(new Font(Display.getDefault(), "Courier New", 10, SWT.NORMAL));
        styledText1.setText("    ");
        styledText1.setForeground(new Color(Display.getCurrent(), 192, 192, 192));
        styledText1.setLayoutData(gridData1);
        styledText1.setAlignment(SWT.RIGHT);  
        styledText = new StyledText(this, SWT.V_SCROLL | SWT.H_SCROLL); 
        styledText.setFont(new Font(Display.getDefault(), "Courier New", 10, SWT.NORMAL));
        styledText.setLayoutData(gridData);
        styledText.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
        	public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
        		BeanShellScriptComposite composite = (BeanShellScriptComposite)styledText.getParent();
           		(new StyledTextModifyEvent(composite, e)).execute();        			
        	}   
        }); 
        styledText.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
        	public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
        		BeanShellScriptComposite composite = (BeanShellScriptComposite)styledText.getParent();
        		(new StyledTextKeyPressedEvent(composite,e)).execute();
        	}
        });

        styledText.addLineStyleListener(new org.eclipse.swt.custom.LineStyleListener() {
        	public void lineGetStyle(org.eclipse.swt.custom.LineStyleEvent e) {
        		BeanShellScriptComposite composite = (BeanShellScriptComposite)styledText.getParent();
        		(new StyleTextLineGetStyleEvent(composite,e)).execute();
        	}
        });
        label = new Label(this, SWT.NONE); 
        label.setText(" ");
        label.setFont(new Font(Display.getDefault(), "Tahoma", 11, SWT.NORMAL));
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0; 
        gridLayout.verticalSpacing = 0;
        gridLayout.horizontalSpacing = 1;
        this.setLayout(gridLayout);
        this.setSize(new Point(342, 227)); 
          
	    final ScrollBar vBar1 = styledText.getVerticalBar();
	    //final ScrollBar vBar2 = styledText1.getVerticalBar();
	    SelectionListener listener1 = new SelectionAdapter() { 
	        public void widgetSelected(SelectionEvent e) {
	          styledText1.setTopPixel(styledText.getTopPixel());
	        }
	      };	
	      vBar1.addSelectionListener(listener1);        
	}


	/**
	 * @return the styledTextLine
	 */
	public StyledText getStyledText() {
		return styledText; 
	}

	/**
	 * @return the styledText1
	 */
	public StyledText getStyledText1() {
		return styledText1;
	}

	/**
	 * This method initializes composite	
	 *
	 */
	private void createComposite() {
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.horizontalSpacing = 0;
		gridLayout1.marginWidth = 0;
		gridLayout1.verticalSpacing = 0;
		gridLayout1.numColumns = 1;
		gridLayout1.marginHeight = 0;
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData2.horizontalSpan = 2;
		gridData2.grabExcessHorizontalSpace = true;
		composite = new Composite(this, SWT.BORDER);
		composite.setLayout(gridLayout1);
		composite.setLayoutData(gridData2);
		createToolBar();
	}

	/**
	 * This method initializes toolBar	
	 *
	 */
	private void createToolBar() { 
		toolBar_1 = new ToolBar(composite, SWT.NONE);
		toolBar_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		saveToolItem = new ToolItem(toolBar_1, SWT.PUSH);
		saveToolItem.setHotImage(new Image(Display.getCurrent(), getClass().getResourceAsStream("/enabled/save.gif")));
		saveToolItem.setToolTipText("Save BeanShell Script - CTRL + S");
		saveToolItem.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream("/enabled/save.gif")));
		saveToolItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
					public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
						BeanShellScriptComposite composite = (BeanShellScriptComposite)styledText.getParent();
						(new SaveBeanShellScriptEvent(composite)).execute();
					}
					public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
					}
				});
		@SuppressWarnings("unused")
		ToolItem sepToolItem01 = new ToolItem(toolBar_1, SWT.SEPARATOR);
	}

	public ToolItem getSaveToolItem() {
		return saveToolItem;
	}
	
	public void loadProperties(properties proXML){
		
	}
	

}  //  @jve:decl-index=0:visual-constraint="10,10"



























