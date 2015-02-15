package com.mxterminal.swt.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.mxdeploy.images.Constant;
import com.mxdeploy.swt.preferences.event.OpenPreferenceEventHandler;
import com.mxterminal.swt.view.event.OpenTunnelDialogEventHandler;
import com.mxterminal.swt.view.helper.ToolBarViewHelper;

public class ToolBarViewComposite extends Composite {

	public ToolItem connectToolItem = null;
	public ToolItem tunnelToolItem = null;
    public ToolItem saveToolItem = null;
    public ToolItem runToolItem = null;    
    public ToolItem stopToolItem = null;
    public ToolItem pauseToolItem = null;
    //protected ToolItem refreshToolItem = null;
    public ToolItem createProcedureToolItem = null;
    
    public ToolBar toolBar = null;
    public ToolBarViewHelper helper = null;
    
    public Label lblHostname;
    public Combo combo = null;
    public ToolBar connectionToolBar;
    
    
    public ToolBarViewComposite(Composite parent, int style) {
		super(parent, SWT.NONE);
		initialize();
	}

	public Combo getCombo() {
		return combo;
	}

	public void setCombo(Combo combo) {
		this.combo = combo;
	}

	private void initialize() {
		this.setSize(new Point(593, 37));
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.marginHeight = 3;
		gridLayout1.numColumns = 4;
		gridLayout1.marginWidth = 0;
		gridLayout1.verticalSpacing = 0;
		gridLayout1.horizontalSpacing = 0;
		this.setLayout(gridLayout1);
		
		//this.setSize(new Point(246, 54));
		helper = new ToolBarViewHelper(this);
		createToolBar();
	}
	
	/**
	 * @return the toolBarConsoleHelper
	 */
	public ToolBarViewHelper getToolBarViewHelper() {
		return helper;
	}

	/**
	 * This method initializes toolBar	
	 *
	 */
	private void createToolBar() {
	    
		lblHostname = new Label(this, SWT.NONE);
		lblHostname.setText("    Hostname :  ");
		
		combo = new Combo(this, SWT.FLAT);	
		combo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 0, 1));
		combo.addKeyListener( new KeyListener() {
            public void keyPressed( KeyEvent e ) {
            }

            public void keyReleased( KeyEvent e ) {
            	if( e.keyCode == SWT.CR ){ 
            		helper.connect();
    				return;
            	}
            	if( e.keyCode == SWT.ARROW_LEFT || e.keyCode == SWT.ARROW_RIGHT || 
                	    e.keyCode == SWT.ARROW_UP || e.keyCode == SWT.ARROW_DOWN || 
                	    e.keyCode == SWT.HOME || e.keyCode == SWT.END ||
                	    e.keyCode == SWT.DEL || e.keyCode == SWT.BS  ||
                	    e.keyCode == SWT.SHIFT || e.keyCode == SWT.CTRL ||
                	    e.keyCode == SWT.ALT ){
                		return;
                	}
                	String[] servidores = ((Combo)e.widget).getItems(); 
                	String text = ((Combo)e.widget).getText();
                	for (int i = 0; i < servidores.length; i++) {
                		if(servidores[i].startsWith(text) ){
                			combo.setText(servidores[i]); //setSelection(selection)select(i);
                			combo.setSelection(new Point(text.length(),servidores[i].length()));
                			break;
                		}
                	}            	
            }
        });
		
		connectionToolBar = new ToolBar(this, SWT.FLAT);
		
		connectToolItem = new ToolItem(connectionToolBar, SWT.PUSH);
		connectToolItem.setImage(Constant.IMAGE_CONNECT);
		connectToolItem.setHotImage(Constant.IMAGE_CONNECT);
		connectToolItem.setEnabled(true);
		connectToolItem.setToolTipText("Connect Server");
		connectToolItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				getToolBarViewHelper().connect();
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		
		toolBar = new ToolBar(this, SWT.FLAT);
		toolBar.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));

		tunnelToolItem = new ToolItem(toolBar, SWT.PUSH);
		tunnelToolItem.setImage(Constant.IMAGE_TUNNEL);
		tunnelToolItem.setHotImage(Constant.IMAGE_TUNNEL);
		tunnelToolItem.setSelection(true);
		tunnelToolItem.setEnabled(false);
		tunnelToolItem.setToolTipText("Config Tunnel");
		tunnelToolItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				//(new OpenTunnelDialogEventHandler()).execute();
				(new OpenPreferenceEventHandler()).execute();
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});

		new ToolItem(toolBar, SWT.SEPARATOR);
		
		saveToolItem = new ToolItem(toolBar, SWT.PUSH);
		saveToolItem.setImage(Constant.IMAGE_SAVE);
		saveToolItem.setHotImage(Constant.IMAGE_SAVE);
		saveToolItem.setSelection(true);
		saveToolItem.setEnabled(false);
		saveToolItem.setToolTipText("Save Server Command");
		saveToolItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				getToolBarViewHelper().save();
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});

		new ToolItem(toolBar, SWT.SEPARATOR);
		
		createProcedureToolItem = new ToolItem(toolBar, SWT.PUSH);
		createProcedureToolItem.setImage(Constant.IMAGE_PROCEDURES);
		createProcedureToolItem.setHotImage(Constant.IMAGE_PROCEDURES);
		createProcedureToolItem.setEnabled(false);
		createProcedureToolItem.setToolTipText("New Procedure with commands selected");
		createProcedureToolItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(SelectionEvent e) { 
				getToolBarViewHelper().createProcedure();
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		runToolItem = new ToolItem(toolBar, SWT.PUSH);
		runToolItem.setImage(Constant.IMAGE_RUN_CONSOLE);
		runToolItem.setHotImage(Constant.IMAGE_RUN_CONSOLE);
		runToolItem.setToolTipText("Run Procedure");
		runToolItem.setSelection(true);
		runToolItem.setEnabled(false);

		stopToolItem = new ToolItem(toolBar, SWT.PUSH);
		stopToolItem.setImage(Constant.IMAGE_STOP_CONSOLE); 
		stopToolItem.setEnabled(false);
		stopToolItem.setToolTipText("Stop Procedure");		

		pauseToolItem = new ToolItem(toolBar, SWT.PUSH);
		pauseToolItem.setImage(Constant.IMAGE_PAUSE_CONSOLE);
		pauseToolItem.setEnabled(false);
		pauseToolItem.setToolTipText("Pause Procedure");
		
		runToolItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				getToolBarViewHelper().run();
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		
		pauseToolItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				getToolBarViewHelper().pause();
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		

		stopToolItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				getToolBarViewHelper().stop();
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		
	
	}
		

	/**
	 * @return the helper
	 */
	public ToolBarViewHelper getHelper() {
		return helper;
	}
	
}  //  @jve:decl-index=0:visual-constraint="151,22"
