package com.mxscript.swt;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import xml.module;
import bsh.ParseException;
import bsh.Parser;
import bsh.TokenMgrError;

import com.mxdeploy.api.domain.Database;
import com.mxdeploy.images.Constant;
import com.mxdeploy.swt.MainShell;
import com.mxdeploy.swt.dialogs.authentication.SshAuthenticationDialog;
import com.mxdeploy.swt.dialogs.authentication.TaskAuth;
import com.mxscript.swt.event.ExportToExcelEvent;
import com.mxscript.swt.event.LauncherBeanShellEvent;
import com.mxscript.swt.event.StopSelectedTaskEvent;
import com.mxscript.swt.helper.ToolBarViewHelper;

public class ToolBarViewComposite extends Composite {
    protected ToolItem saveToolItem = null;
    protected ToolItem runToolItem = null;    
    protected ToolItem exportToExcelToolItem = null;
    protected ToolItem clearLogToolItem = null;
    protected ToolItem scrollLockToolItem = null;
    protected ToolItem stopToolItem = null;
    protected ToolItem stopSelectedToolItem = null;
    
    //protected ToolItem importToolItem = null;
    protected LauncherBeanShellEvent bsEvent;
    
    protected ToolBar toolBar = null;
    protected ToolBarViewHelper helper = null;
	private Combo combo = null;
	private Label label = null;
	private Label label1 = null;
	private Combo logLevelCombo = null;
	//private ThreadGroup threadGoup = null;
	
	//private PasswordManager passwordManager;
	
	private Thread threadRun = null;
	
    
    public ToolBarViewComposite(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		label = new Label(this, SWT.NONE);
		label.setText("Threads : ");
		createCombo();
		this.setSize(new Point(525, 32));
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 5;
		gridLayout1.marginHeight = 1;
		gridLayout1.marginWidth = 10;
		gridLayout1.verticalSpacing = 1;
		gridLayout1.horizontalSpacing = 5;
		this.setLayout(gridLayout1);
		
		//this.setSize(new Point(246, 54)); 
		helper = new ToolBarViewHelper(this);

		label1 = new Label(this, SWT.NONE);
		label1.setText("  Log Level :");
		createLogLevelCombo();
		createToolBar();
	}
	
	/**
	 * @return the toolBarConsoleHelper
	 */
	public ToolBarViewHelper getToolBarConsoleHelper() {
		return helper;
	}

	/**
	 * This method initializes toolBar	
	 *
	 */
	private void createToolBar() {
		
		GridData gridData = new GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = false;
		toolBar = new ToolBar(this, SWT.FLAT );
		toolBar.setLayoutData(gridData);

	 
		@SuppressWarnings("unused")
		ToolItem toolItem02 = new ToolItem(toolBar, SWT.SEPARATOR);
		
		clearLogToolItem = new ToolItem(toolBar, SWT.PUSH);
		clearLogToolItem.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream("/enabled/clear_co.gif")));
		clearLogToolItem.setHotImage(new Image(Display.getCurrent(), getClass().getResourceAsStream("/enabled/clear_co.gif")));
		clearLogToolItem.setToolTipText("Clear Console");
		clearLogToolItem.setSelection(false);
		clearLogToolItem.setEnabled(true);
		clearLogToolItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				BeanShellFormComposite composite = (BeanShellFormComposite)getParent().getParent().getParent().getParent();
				composite.getStyledText().setText("");
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});	
		
		scrollLockToolItem = new ToolItem(toolBar, SWT.PUSH);
		scrollLockToolItem.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream("/enabled/lock_co.gif")));
		scrollLockToolItem.setHotImage(new Image(Display.getCurrent(), getClass().getResourceAsStream("/enabled/lock_co.gif")));
		scrollLockToolItem.setDisabledImage(new Image(Display.getCurrent(), getClass().getResourceAsStream("/enabled/lock_co.gif")));
		scrollLockToolItem.setToolTipText("Scroll Lock/UnLock");
		scrollLockToolItem.setSelection(false);
		scrollLockToolItem.setEnabled(true);
		
		scrollLockToolItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				BeanShellFormComposite composite = (BeanShellFormComposite)getParent().getParent().getParent().getParent();
				if( composite.isLockConsole()){
					composite.setLockConsole(false); 
				} else {
					composite.setLockConsole(true);
				}
				
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});		
		
		@SuppressWarnings("unused")
		ToolItem toolItem1 = new ToolItem(toolBar, SWT.SEPARATOR);
		
		exportToExcelToolItem = new ToolItem(toolBar, SWT.PUSH);
		exportToExcelToolItem.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream("/enabled/table.gif")));
		exportToExcelToolItem.setHotImage(new Image(Display.getCurrent(), getClass().getResourceAsStream("/enabled/table.gif")));
		exportToExcelToolItem.setToolTipText("Export to Excel");
		exportToExcelToolItem.setSelection(false);
		exportToExcelToolItem.setEnabled(true);
		exportToExcelToolItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				BeanShellFormComposite composite = (BeanShellFormComposite)getParent().getParent().getParent().getParent();
				(new ExportToExcelEvent(composite)).execute();
				
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});			
		@SuppressWarnings("unused")
		ToolItem toolItem2 = new ToolItem(toolBar, SWT.SEPARATOR);
		
		//exportToExcelButton.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream("/enabled/excel.gif")));
				
		runToolItem = new ToolItem(toolBar, SWT.PUSH);
		runToolItem.setImage(Constant.IMAGE_RUN_CONSOLE);
		runToolItem.setHotImage(Constant.IMAGE_RUN_CONSOLE); 
		runToolItem.setToolTipText("Start All Threads");
		runToolItem.setSelection(false);
		runToolItem.setEnabled(false);
		runToolItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
					final BeanShellFormComposite composite = (BeanShellFormComposite)getParent().getParent().getParent().getParent();
					composite.getStyledText().setText("");
					
					String filePath = null;
					String scriptName = null;
					if ( MainShell.getCTopTabFolder().getSelection().getData()!=null && MainShell.getCTopTabFolder().getSelection().getData() instanceof module){
						module __module__ = (module)MainShell.getCTopTabFolder().getSelection().getData();
						filePath = __module__.getFullpath();
						scriptName = __module__.getFile();
					} else {
						scriptName = MainShell.getCTopTabFolder().getSelection().getText();
						filePath = Database.WORKSPACE_PATH+"/"+Database.WORKSPACE_NAME+"/beanshell/bsh/"+scriptName;
					}
					
					try {
						Parser parser = new Parser(new FileReader(filePath));
						while (!(parser.Line())) {
							parser.popNode();
						}
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (ParseException e1) {
						composite.getStyledText().append("======================================================================================================\n");
						composite.getStyledText().append("File Path : "+filePath+"\n");
						composite.getStyledText().append(e1.getMessage()+"\n");
						composite.getStyledText().append("======================================================================================================\n");
						return;
					} catch ( TokenMgrError te){
						composite.getStyledText().append("======================================================================================================\n");
						composite.getStyledText().append("File Path : "+filePath+"\n");
						composite.getStyledText().append(te.getMessage()+"\n");
						composite.getStyledText().append("======================================================================================================\n");
						return;
					}
					disableToolBar();

					int itemCount = composite.getTable().getItemCount();
					Boolean openAuthAgain = true;
					String usernameLDAP="";
					String passwordLDAP="";
					
					List<TaskAuth> taskAuthList = new ArrayList<TaskAuth>();
					int count = 0;
					for(count =0; count < itemCount; count++){
						String server = composite.getTable().getItem(count).getText(0);

						TaskAuth taskAuth = new TaskAuth();
						if ( openAuthAgain ){
							
							SshAuthenticationDialog dialog = new SshAuthenticationDialog(server);
							dialog.enableMethod();
							dialog.openShell();
		
							if ( dialog.getCanceled() ){
								 break;
							}
							
							usernameLDAP = dialog.getUsername();
							passwordLDAP = dialog.getPassword();
							
							taskAuthList = new ArrayList<TaskAuth>();
							taskAuth.setHostname(server);
							taskAuth.setPassword(passwordLDAP);
							taskAuth.setUsername(usernameLDAP);
							taskAuth.setIndexItem(count);
							taskAuthList.add(taskAuth);	
							taskAuth.setFirst(false);
							if( count == 0 ){
								taskAuth.setFirst(true);
							}
							
							if( dialog.isLDAPUser()){
								openAuthAgain = false;

								continue;
							} 
							
							if( dialog.getLabelWarningValue()!=null && dialog.getLabelWarningValue().trim().length()>0 ){
								composite.getStyledText().append("ERROR - ["+server+"] "+dialog.getLabelWarningValue());
							}
							
							bsEvent = new LauncherBeanShellEvent(composite, filePath, scriptName, taskAuthList, false);
						    threadRun = new Thread(bsEvent);
						    threadRun.start();						
						} else {
							taskAuth.setHostname(server);
							taskAuth.setPassword(passwordLDAP);
							taskAuth.setUsername(usernameLDAP);
							taskAuth.setIndexItem(count);
							taskAuthList.add(taskAuth);	
							taskAuth.setHostname(server);
							taskAuth.setFirst(false);
						}
					}
	
					if( !openAuthAgain && !taskAuthList.isEmpty() ){
						bsEvent = new LauncherBeanShellEvent(composite, filePath, scriptName, taskAuthList, false);
					    threadRun = new Thread(bsEvent);
					    threadRun.start();
					    
					    Thread threadRunController = new Thread() {
						    public void run() {
						    	try {
									while( threadRun.isAlive() ){
										Thread.sleep(1000);
										if( composite.isStopBeanShell() ){
											threadRun.interrupt(); 
											bsEvent.getTaskManager().stopTasks();
										}
									}
								} catch (InterruptedException e) {
									e.printStackTrace();
								}finally {
									enableToolBar();
								}
							} 
					    };
					    threadRunController.start();
					} else {
					    Thread threadRunController = new Thread() {
						    public void run() {
						    	try {
						    		if( threadRun !=null)
									while( threadRun.isAlive() ){
										Thread.sleep(1000);
										if( composite.isStopBeanShell() ){
											threadRun.interrupt(); 
											bsEvent.getTaskManager().stopTasks();
										}
									}
								} catch (InterruptedException e) {
									e.printStackTrace();
								}finally {
									enableToolBar();
								}
							} 
					    };
					    threadRunController.start();
					}
			} 
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		
		stopToolItem = new ToolItem(toolBar, SWT.PUSH);
		stopToolItem.setImage(Constant.IMAGE_STOP_ALL_CONSOLE);
		stopToolItem.setHotImage(Constant.IMAGE_STOP_ALL_CONSOLE);
		stopToolItem.setToolTipText("Stop All Threads");
		stopToolItem.setSelection(false); 
		stopToolItem.setEnabled(false);
		stopToolItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				BeanShellFormComposite composite = (BeanShellFormComposite)getParent().getParent().getParent().getParent();
				composite.getToolBarConsoleComposite().getStopToolItem().setEnabled(false);
				composite.setStopBeanShell(true);
//				if(bsEvent.getTaskManager() != null){
//				   bsEvent.getTaskManager().stopTasks();
//				   bsEvent.getTaskManager().interrupt();
//				}
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		
		new ToolItem(toolBar, SWT.SEPARATOR);

		stopSelectedToolItem = new ToolItem(toolBar, SWT.PUSH);
		stopSelectedToolItem.setImage(Constant.IMAGE_STOP_CONSOLE);
		stopSelectedToolItem.setHotImage(Constant.IMAGE_STOP_CONSOLE);
		stopSelectedToolItem.setToolTipText("Stop Selected Thread");
		stopSelectedToolItem.setSelection(false); 
		stopSelectedToolItem.setEnabled(false);
		stopSelectedToolItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				BeanShellFormComposite composite = (BeanShellFormComposite)getParent().getParent().getParent().getParent();
				//composite.getToolBarConsoleComposite().getStopToolItem().setEnabled(false);
				//composite.setStopBeanShell(true);
				StopSelectedTaskEvent stopSelected = new StopSelectedTaskEvent(composite);
				stopSelected.execute();
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		
	}
	
	public void disableToolBar(){
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				BeanShellFormComposite composite = (BeanShellFormComposite)getParent().getParent().getParent().getParent();
				composite.getToolBarConsoleComposite().getCombo().setEnabled(false);	
				composite.getToolBarConsoleComposite().getRunToolItem().setEnabled(false);
				composite.getToolBarConsoleComposite().getStopToolItem().setEnabled(true);
				composite.getToolBarConsoleComposite().getLogLevelCombo().setEnabled(false);
				composite.getServerCombo().setEnabled(false);
				composite.getAddServerToolItem().setEnabled(false);
				composite.getDeleteServerToolItem().setEnabled(false);
				composite.getImportServerToolItem().setEnabled(false);
			}
		});
	}
	
	public void enableToolBar(){
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				BeanShellFormComposite composite = (BeanShellFormComposite)getParent().getParent().getParent().getParent();
				composite.getToolBarConsoleComposite().getCombo().setEnabled(true);	
				composite.getToolBarConsoleComposite().getRunToolItem().setEnabled(true);
				composite.getToolBarConsoleComposite().getStopToolItem().setEnabled(false);
				composite.getToolBarConsoleComposite().getStopSelectedToolItem().setEnabled(false);
				composite.getToolBarConsoleComposite().getLogLevelCombo().setEnabled(true);
				composite.getServerCombo().setEnabled(true);
				composite.getAddServerToolItem().setEnabled(true);
				composite.getDeleteServerToolItem().setEnabled(true);
				composite.getImportServerToolItem().setEnabled(true);
			}
		});

	}	
	
	/**
	 * @return the helper
	 */
	public ToolBarViewHelper getHelper() {
		return helper;
	}

	/**
	 * This method initializes combo	
	 *
	 */
	private void createCombo() {
		combo = new Combo(this, SWT.NONE);
		combo.add("1");
		combo.add("5");
		combo.add("10");
		combo.add("20");
		combo.add("30");
		combo.select(2);
		
	}

	/**
	 * @return the combo
	 */
	public Combo getCombo() {
		return combo;
	}

	/**
	 * @return the exportToExcelToolItem
	 */
	public ToolItem getExportToExcelToolItem() {
		return exportToExcelToolItem;
	}

	/**
	 * @return the runToolItem
	 */
	public ToolItem getRunToolItem() {
		return runToolItem;
	}

	/**
	 * This method initializes logLevelCombo	
	 *
	 */
	private void createLogLevelCombo() {
		logLevelCombo = new Combo(this, SWT.NONE);
		logLevelCombo.add("DEBUG");
		logLevelCombo.add("INFO");
		logLevelCombo.add("WARN");
		logLevelCombo.add("ERROR"); 
		logLevelCombo.select(1);		
	}

	/**
	 * @return the clearLogToolItem
	 */
	public ToolItem getClearLogToolItem() {
		return clearLogToolItem;
	}

	/**
	 * @return the logLevelCombo
	 */
	public Combo getLogLevelCombo() {
		return logLevelCombo;
	}

	/**
	 * @return the saveToolItem
	 */
	public ToolItem getSaveToolItem() {
		return saveToolItem;
	}

	/**
	 * @return the scrollLockToolItem
	 */
	public ToolItem getScrollLockToolItem() {
		return scrollLockToolItem;
	}

	/**
	 * @return the stopToolItem
	 */
	public ToolItem getStopToolItem() {
		return stopToolItem;
	}
	
	public ToolItem getStopSelectedToolItem() {
		return stopSelectedToolItem;
	}	

	/**
	 * @return the toolBar
	 */
	public ToolBar getToolBar() {
		return toolBar;
	}
	
	

}  //  @jve:decl-index=0:visual-constraint="10,10"
