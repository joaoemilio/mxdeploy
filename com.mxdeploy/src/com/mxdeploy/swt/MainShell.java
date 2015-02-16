package com.mxdeploy.swt;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;

import com.mxdeploy.api.domain.Database;
import com.mxdeploy.api.service.KeepassxService;
import com.mxdeploy.images.Constant;
import com.mxdeploy.launcher.splash.SplashDialog;
import com.mxdeploy.plugin.domain.Event;
import com.mxdeploy.plugin.event.EventHandler;
import com.mxdeploy.plugin.event.EventPlugin;
import com.mxdeploy.plugin.util.URLClassLoaderPlugin;
import com.mxdeploy.swt.explorer.event.LoadMyProjecTreeEventHandler;
import com.mxdeploy.swt.explorer.helper.ControlPanelHelper;
import com.mxdeploy.swt.menu.MenuBarUtils;
import com.mxdeploy.swt.menu.event.CloseAllTerminalsEventHandler;
import com.mxdeploy.swt.menu.event.NewTerminalEventHandler;
import com.mxdeploy.swt.menu.event.WindownApplicationEventHandler;
import com.mxssh.ManageGateway;
import com.mxterminal.swt.util.TerminalProperty;
import com.mxterminal.swt.view.CTopTabFolder;
import com.mxterminal.swt.view.helper.ToolBarViewHelper;


public class MainShell {
	
	static Logger logger = Logger.getLogger(MainShell.class);
	
	private static MainHelper helper = null;
	
	public static Display display = null;  //  @jve:decl-index=0:
	
	public static Shell sShell = null;//  @jve:decl-index=0:visual-constraint="10,10"
	
	public static TopBarComposite topBarComposite = null;
	public static ToolBarFooterComposite toolBarFooterComposite = null;
	
	private static SashFormComposite sashFormComposite = null;
	
    private Menu menuBar = null; 
	public  static  Composite centroComposite = null;
	
	public static Image IMAGE_LOGO;
	
	//public static List<ConsoleHelper> SSHPoolList = new ArrayList<ConsoleHelper>(); 
	
	public MainShell(){ }
	
	public void start(SplashDialog splashDialog, String account){  
				
		Display.getDefault().syncExec(new Runnable() { 
			public void run() {
				display = Display.getDefault();
			}
		});		 
		
		URLClassLoaderPlugin.loadPlugins();
		
		helper = new MainHelper(); 
		helper.loadGlobalSystemProps(account);
		helper.loadSystemProps(account);
		helper.loadGlobalGatewaySystemProps(account);
		helper.loadGatewaySystemProps(account);
		helper.loadAccountConfig();
		
		EventHandler eventHandler = EventPlugin.getEventHandler(Event.TYPE_LOAD_PROPERTIES_EVENT);
		if(eventHandler!=null){ 
		   eventHandler.execute();
		}
		
		String path = Database.HOME+"/log4j.properties";
		PropertyConfigurator.configure(path);
		logger.debug("Iniciar Log4j"); 
		
		createSShell();
		
		if(splashDialog!=null){
			LoadXML loadXML = new LoadXML();
			loadXML.loadAllProjectXML(splashDialog.getProgressBar(), splashDialog.getLabel());
		    Thread threadRun = new Thread() {
			    public void run() {
			    	Database.getInstance().loadServerXML();
			    }
		    };
		    threadRun.start();			
			
			//loadXML.loadAllPropertyXML(splashDialog.getProgressBar(), splashDialog.getLabel());
		}
		
		LoadMyProjecTreeEventHandler.execute();
		
		(new KeepassxService()).load();
		
		sShell.open();

		final ToolTip tip = new ToolTip(MainShell.sShell, SWT.BALLOON | SWT.ICON_INFORMATION);
		tip.setMessage("MXDeploy !");
		tip.setText("MXDeploy.");
		
		Tray tray = display.getSystemTray();
		logger.debug("get the tray");
		if(tray != null) {
			TrayItem trayItem = new TrayItem(tray, SWT.NONE);
			trayItem.setImage(Constant.IMAGE_TRAY);
			final Menu menu = new Menu(MainShell.sShell, SWT.POP_UP);
			
			if( System.getProperty("os.name").contains("Linux")){
			
				if( TerminalProperty.getSocks5PropertyList()!=null && TerminalProperty.getSocks5PropertyList().size()>0 ){
					MainShell.getToolBarFooterComposite().getToolBar().setVisible(true);
					
					MenuItem menuItemStartGateway = new MenuItem(menu, SWT.PUSH);
					menuItemStartGateway.setText("Start gateway");
					menuItemStartGateway.setImage(Constant.IMAGE_GATEWAY_GREEN);
					menuItemStartGateway.addListener (SWT.Selection, new Listener() {
						public void handleEvent (org.eclipse.swt.widgets.Event e) {
							tip.setText("Starting Gateway");
							tip.setVisible(true);
							Display.getDefault().syncExec(new Runnable() {
								public void run() {
									toolBarFooterComposite.getGatewayItem().setImage(Constant.IMAGE_GATEWAY_YELLOW);
								}
							});
						    Thread threadRun = new Thread() {
							    public void run() {
									ManageGateway manageGateway = new ManageGateway();
									manageGateway.start();
									Display.getDefault().syncExec(new Runnable() {
										public void run() {
											toolBarFooterComposite.getGatewayItem().setImage(Constant.IMAGE_GATEWAY_GREEN);
										}
									});								
							    }
						    };	
						    threadRun.start();
						}
					});	
					
					MenuItem menuItemStopGateway = new MenuItem(menu, SWT.PUSH);
					menuItemStopGateway.setText("Stop gateway");
					menuItemStopGateway.setImage(Constant.IMAGE_GATEWAY_RED);
					menuItemStopGateway.addListener (SWT.Selection, new Listener() {
						public void handleEvent (org.eclipse.swt.widgets.Event e) {
							tip.setText("Starting Gateway");
							tip.setVisible(true);
							Display.getDefault().syncExec(new Runnable() {
								public void run() {
									toolBarFooterComposite.getGatewayItem().setImage(Constant.IMAGE_GATEWAY_YELLOW);
								}
							});
						    Thread threadRun = new Thread() {
							    public void run() {
									ManageGateway manageGateway = new ManageGateway();
									manageGateway.stop();
									Display.getDefault().syncExec(new Runnable() {
										public void run() {
											toolBarFooterComposite.getGatewayItem().setImage(Constant.IMAGE_GATEWAY_RED);
										}
									});								
							    }
						    };	
						    threadRun.start();						
						}
					});				
				}
			}
			
			trayItem.addListener (SWT.MenuDetect, new Listener () {
				public void handleEvent (org.eclipse.swt.widgets.Event event) {
					menu.setVisible (true);
				}
			});		
			
			trayItem.addListener(SWT.DefaultSelection, new Listener() {
				public void handleEvent(org.eclipse.swt.widgets.Event event) {
					System.out.println("Systray double click");
					if(MainShell.sShell.getVisible()){
						MainShell.sShell.setVisible(false);
						MainShell.sShell.setMinimized(true);						
					} else {
						MainShell.sShell.setVisible(true);
						MainShell.sShell.setMinimized(false);						
					}

				}
			});	
			
//			sShell.addShellListener(new ShellAdapter() {		
//				public void shellIconified(ShellEvent e) {
//					System.out.println("Iconify");
//					sShell.setVisible(false);
//				}
//			});
		

		}
		( new NewTerminalEventHandler() ).execute();
		(new WindownApplicationEventHandler()).execute();
		//(new LoadComboBoxModuleEvent()).execute();
		
		

		
	}	
	 
	public void waitWhileRunning(String title){
		try {
			while (!MainShell.sShell.isDisposed()) {
				if (!MainShell.display.readAndDispatch())
					MainShell.display.sleep();
			}
		} catch (Throwable e) {
			logger.error(e.getMessage(),e);
			e.printStackTrace();
			MainShell.sendMessage(Constant.MSN_SEE_LOG_ERROR, SWT.OK | SWT.ICON_ERROR);
			waitWhileRunning("Phenix");
		}	
	}
	
    public static CTopTabFolder getCTopTabFolder(){
    	return sashFormComposite.getCTopTabFolder();
    }
    
    public static SashFormComposite getSashFormComposite(){
    	return sashFormComposite;
    }    
    
	/**
	 * This method initializes centroComposite	
	 *
	 */
	private void createCentroComposite() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginHeight = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginWidth = 0; 
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		centroComposite = new Composite(sShell, SWT.BORDER);
		centroComposite.setLayoutData(gridData);
		centroComposite.setLayout(gridLayout);
		
		sashFormComposite = new SashFormComposite(centroComposite,SWT.NONE);
		sashFormComposite.setLayoutData(gridData);
		sashFormComposite.setLayout(gridLayout); 
		
		//sashFormComposite.getTabFolderServidor()
	}
	
	//public static 
 
	public static void main(String[] args) {
		MainShell mainShell = new MainShell();
		mainShell.start(new SplashDialog(),null);
		
		//IMAGE_LOGO  = new Image(Display.getCurrent(), MainShell.class.getClass().getResourceAsStream("/alt32.gif"));

		
		try {
			while (!MainShell.sShell.isDisposed()) {
				if ( !display.readAndDispatch() )
					 display.sleep();
			}
		} catch (Throwable e) {
			//logger.error(e.getMessage(),e);
			e.printStackTrace();			
			sendMessage(Constant.MSN_SEE_LOG_ERROR, SWT.OK | SWT.ICON_ERROR);
		} finally {
			display.dispose();
			System.exit(0);
		}
	}

	/**
	 * This method initializes sShell
	 */
	private void createSShell() {		
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.horizontalSpacing = 0; 
		gridLayout.verticalSpacing = 0; 
				
		sShell = new Shell();
		sShell.setMaximized(true);
		sShell.setText(Database.WORKSPACE_NAME+" - MXDeploy"); 
		sShell.setImage(Constant.IMAGE_LOGO);
		sShell.setLayout(gridLayout);
		sShell.addListener(SWT.Close, new Listener() { 
	        public void handleEvent(org.eclipse.swt.widgets.Event event) {
	          int style = SWT.APPLICATION_MODAL | SWT.YES | SWT.NO;
	          MessageBox messageBox = new MessageBox(sShell, style);
	          messageBox.setText("Information");
	          messageBox.setMessage("Do you want to close the MXDeploy ?");
	          if(messageBox.open() == SWT.YES){
	        	  event.doit=true;
	        	  CloseAllTerminalsEventHandler eventClose = new CloseAllTerminalsEventHandler();
	        	  eventClose.execute();
		          if( ManageGateway.isStarted ){
		        	  ManageGateway manageGateway = new ManageGateway();
		        	  manageGateway.stop();
		          }
		          System.exit(0);
		          MainShell.sShell.dispose();
	          } else {
	        	  event.doit=false;
	          }
	        }
	      });

		//Adding Main Menu
		addMainMenu(sShell);

//		topBarComposite = new TopBarComposite(sShell, SWT.FLAT);
//		topBarComposite.setLayoutData(gridData2);
		//topBarComposite.setLayout(gridLayout3);
		
		createCentroComposite();

		//toolBarFooterComposite = new ToolBarFooterComposite(sShell, SWT.NONE);
		//toolBarFooterComposite.setLayoutData(gridData4);
		
		
		
	}

	/**
	 * 
	 * @param sShell
	 */
	private void addMainMenu(Shell sShell){
		menuBar = MenuBarUtils.createMenuBar(sShell);
		sShell.setMenuBar(menuBar);
	}
	
    public static void sendMessage(final String msg){
		Display.getDefault().asyncExec(new Runnable() {
		    public void run() {
		    	MessageBox msgBox = new MessageBox(MainShell.sShell, SWT.OK );
		    	msgBox.setMessage(msg);
		    	msgBox.open();
		    }
		});
    }	
    
    public static void sendMessage(final String msg, final int style){
		Display.getDefault().asyncExec(new Runnable() {
		    public void run() {
		    	MessageBox msgBox = new MessageBox(MainShell.sShell, style);
		    	msgBox.setMessage(msg);
		    	msgBox.open();
		    }
		});
    }
    
    public static ControlPanelHelper getControlPanelHelper(){
    	return MainShell.sashFormComposite.getWorkingSetComposite().getHelper();
    }
    
    public static MainHelper getMainHelper(){
    	return MainShell.helper; 
    }

	public static ToolBarFooterComposite getToolBarFooterComposite() {
		return toolBarFooterComposite;
	}
	
	public static ToolBarViewHelper  getToolBarViewHelper(){
		return MainShell.sashFormComposite.getCTopTabFolder().getServerHelper().getViewHelper().getToolBarViewHelper();
	}

}


