package com.mxdeploy.swt.menu;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import com.mxdeploy.AccountConfig;
import com.mxdeploy.SocksConfig;
import com.mxdeploy.images.Constant;
import com.mxdeploy.plugin.event.EventHandler;
import com.mxdeploy.plugin.menu.FileMenu;
import com.mxdeploy.plugin.menu.HelpMenu;
import com.mxdeploy.plugin.menu.MenuBT;
import com.mxdeploy.plugin.menu.MenuPlugin;
import com.mxdeploy.plugin.menu.SubMenu;
import com.mxdeploy.plugin.menu.TerminalMenu;
import com.mxdeploy.plugin.menu.WindowMenu;
import com.mxdeploy.swt.dialogs.AboutDialog;
import com.mxdeploy.swt.menu.event.CloseAllTerminalsEventHandler;
import com.mxdeploy.swt.menu.event.CloseTerminalEventHandler;
import com.mxdeploy.swt.menu.event.ExitEventHandler;
import com.mxdeploy.swt.menu.event.FocusTerminalEventHandler;
import com.mxdeploy.swt.menu.event.NewTerminalEventHandler;
import com.mxdeploy.swt.menu.event.NextTerminalEventHandler;
import com.mxdeploy.swt.menu.event.SearchProjectEventHandler;
import com.mxdeploy.swt.menu.event.StartGatewayHandler;
import com.mxdeploy.swt.menu.event.StopGatewayHandler;
import com.mxdeploy.swt.menu.event.TerminalEventHandler;
import com.mxdeploy.swt.menu.event.WinConsoleNavigateApplicationEventHandler;
import com.mxdeploy.swt.menu.event.WindowConsoleApplicationEventHandler;
import com.mxdeploy.swt.menu.event.WindowEventHandler;
import com.mxdeploy.swt.menu.event.WindownApplicationEventHandler;
import com.mxterminal.swt.util.TerminalProperty;

public class MenuBarUtils {

	//static Logger logger = Logger.getLogger(MainShell.class);
	static MenuItem winConsoleApplicationSubMenu = null;
	static MenuItem closeTerminalSubMenu = null;
	static MenuItem closeAllTerminalSubMenu = null;
	static MenuItem focusTerminalSubMenu = null;
	static MenuItem winConsoleNavigateApplicationSubMenu = null;
	static MenuItem ripplySubMenu = null;
	static MenuItem gateway = null;
	static MenuItem tool = null;
	
	private static MenuBT menuBt = null;
	private static Menu menuBar = null;
	
	public static Menu createMenuBar(Decorations parent) {
		createMenuBT();
		
		menuBar = new Menu(parent, SWT.BAR);
		
		for(SubMenu menuItemBT : menuBt.getSubMenuList() ){
			
			MenuItem menuItem = new MenuItem(menuBar, menuItemBT.getSWT_Style());
			
			menuItem.setText(menuItemBT.getText());
			if(menuItemBT.getImage()!=null){
				menuItem.setImage(menuItemBT.getImage());
			}
			
			if(menuItemBT.getEventHandler()!=null){
				menuItem.setData("EventHandler",menuItemBT.getEventHandler());
				menuItem.addListener(SWT.Arm, new Listener() {
			      	  public void handleEvent(Event event) { 
			      		EventHandler eventHandler = (EventHandler)event.widget.getData("EventHandler");
			      		eventHandler.execute();
			      	  }
		        });				
			}
			
			if(menuItemBT.getSubMenuList()!=null && !menuItemBT.getSubMenuList().isEmpty()){
				Menu subMenu = new Menu(menuItem);
				
				for(SubMenu subMenuBT : menuItemBT.getSubMenuList() ){

					if(subMenuBT.isCreateSeparatorBefore()){
						@SuppressWarnings("unused")
						MenuItem separator = new MenuItem(subMenu, SWT.SEPARATOR);						
					}
					
					MenuItem subMenuItem = new MenuItem(subMenu, subMenuBT.getSWT_Style());
					if(subMenuBT.getText().equals("Socks")) {
						Menu m1 = new Menu(subMenuItem);
						subMenuItem.setMenu(m1);
						addSocksMenu(m1);
					}					
					
					subMenuItem.setText(subMenuBT.getText());
					if(subMenuBT.getImage()!=null){
						subMenuItem.setImage(subMenuBT.getImage());
					}				
					
					if(subMenuBT.getEventHandler()!=null){
						subMenuItem.setData("EventHandler",subMenuBT.getEventHandler());
						subMenuItem.addListener(SWT.Selection, new Listener() {
					      	  public void handleEvent(Event event) { 
					      		EventHandler eventHandler = (EventHandler)event.widget.getData("EventHandler");
					      		eventHandler.execute();
					      	  }
				        });				
					}	
					
					if(subMenuBT.getAccelerator()!=0){
						subMenuItem.setAccelerator(subMenuBT.getAccelerator());
					}
					
				}
				menuItem.setMenu(subMenu);
			}
			
			if(menuItemBT.getAccelerator()!=0){
				menuItem.setAccelerator(menuItemBT.getAccelerator());
			}			
		}
		
		return menuBar;
	}
	
	private static void addSocksMenu(Menu menuItem) {
		
		List<SocksConfig> list = AccountConfig.getInstance().getSocksList();
		
		for(SocksConfig cfg: list) {
			MenuItem socksMenu = new MenuItem(menuItem, SWT.NONE);
			socksMenu.setText(cfg.getName());	
			socksMenu.setImage(Constant.IMAGE_RUN_CONSOLE);
			socksMenu.setData(cfg);

			socksMenu.addListener(SWT.Selection, new Listener() {
		      	  public void handleEvent(Event event) { 
		      		MenuItem mi = (MenuItem)event.widget;
		      		SocksConfig cfg2 = (SocksConfig)event.widget.getData();
		      		if(cfg2.getEnabled() == null) {
			    		cfg2.setEnabled(true);
			    		AccountConfig.getInstance().setActiveSocks(cfg2);
		      		}else{
			    		cfg2.setEnabled(!cfg2.getEnabled());
			    		if(cfg2.getEnabled()) {
			    			AccountConfig.getInstance().setActiveSocks(cfg2);
			    		}else{
			    			AccountConfig.getInstance().setActiveSocks(null);
			    		}
		      		}
		    		if(mi.getImage().equals(Constant.IMAGE_RUN_CONSOLE)) {
		    			mi.setImage(Constant.IMAGE_STOP_CONSOLE);
		    		}else{
		    			mi.setImage(Constant.IMAGE_RUN_CONSOLE);
		    		}
		      	  }
	        });				
		}
	}

	/**
	 * @return the menuBt
	 */
	public static MenuBT getMenuBt() {
		return menuBt;
	}

	/**
	 * @param menuBt the menuBt to set
	 */
	public static void setMenuBt(MenuBT menuBt) {
		MenuBarUtils.menuBt = menuBt;
	}

	public static void createMenuBT(){
		menuBt = new MenuBT();
		menuBt.addSubMenu(createFileMenu());
		menuBt.addSubMenu(createTerminalMenu());
		MenuPlugin.addSubMenuCustom(menuBt);
		menuBt.addSubMenu(createToolsMenu());
		menuBt.addSubMenu(createWindowsMenu());
		menuBt.addSubMenu(createHelpMenu());
	}
	
	public static SubMenu createToolsMenu(){
		SubMenu toolSubMenu =  new SubMenu();
		
		toolSubMenu.setText("Tools");
		toolSubMenu.setSWT_Style(SWT.CASCADE);
		
		createGatewayMenu(toolSubMenu);
		
		return toolSubMenu;
	}
	
	public static void createGatewayMenu(SubMenu toolSubMenu){
		SubMenu startGatewaySubMenu =  new SubMenu();
		toolSubMenu.addSubMenu(startGatewaySubMenu);
		
		startGatewaySubMenu.setText("Start Gateway");
		startGatewaySubMenu.setEventHandler(new StartGatewayHandler());
		startGatewaySubMenu.setEnabled(false);

		SubMenu stopGatewaySubMenu =  new SubMenu();
		toolSubMenu.addSubMenu(stopGatewaySubMenu);
		
		stopGatewaySubMenu.setText("Stop Gateway");
		stopGatewaySubMenu.setEventHandler(new StopGatewayHandler());
		stopGatewaySubMenu.setEnabled(false);
		
		if( System.getProperty("os.name").contains("Linux")){
			if( TerminalProperty.getSocks5PropertyList()!=null && TerminalProperty.getSocks5PropertyList().size()>0 ){
				stopGatewaySubMenu.setEnabled(true);
				startGatewaySubMenu.setEnabled(true);
			}
		}
		
	}
	
	public static FileMenu createFileMenu(){
		FileMenu fileMenu = new FileMenu();
		
//		SubMenu newProject = new SubMenu();
//		newProject.setText("New &Project");		
//		newProject.setImage(Constant.IMAGE_PROJECT);
//		newProject.setEventHandler(new NewProjectEventHandler());
//		newProject.setSWT_Style(SWT.PUSH);
//		
//		fileMenu.addSubMenu(newProject);
		
		SubMenu searchSubMenu = new SubMenu();
		searchSubMenu.setText("&Search Project\t Ctrl+F");		
		searchSubMenu.setImage(Constant.IMAGE_SEARCH);
		searchSubMenu.setEventHandler(new SearchProjectEventHandler());
		searchSubMenu.setSWT_Style(SWT.NONE);
		searchSubMenu.setAccelerator (SWT.CTRL+'F');
		
		fileMenu.addSubMenu(searchSubMenu);

//		SubMenu preferencesSubMenu = new SubMenu();
//		preferencesSubMenu.setText("Preferences");		
//		preferencesSubMenu.setEventHandler(new PreferencesEventHandler());
//		preferencesSubMenu.setSWT_Style(SWT.PUSH);
//		preferencesSubMenu.setImage(Constant.IMAGE_MENU_TERMINAL);
//		fileMenu.addSubMenu(preferencesSubMenu);
		
		
		SubMenu exitSubMenu = new SubMenu();
		exitSubMenu.setCreateSeparatorBefore(true);
		exitSubMenu.setText("Exit");
		exitSubMenu.setEventHandler(new ExitEventHandler());
 
		fileMenu.addSubMenu(exitSubMenu);
		
		return fileMenu;
	}

	public static TerminalMenu createTerminalMenu(){
		
		TerminalMenu terminalMenu =  new TerminalMenu();
		terminalMenu.setEventHandler(new TerminalEventHandler());
		
		SubMenu newTerminalSubMenu = new SubMenu();
		newTerminalSubMenu.setText("&New Terminal SSH2\tCtrl+T");		
		newTerminalSubMenu.setImage(Constant.IMAGE_SERVER);
		newTerminalSubMenu.setEventHandler(new NewTerminalEventHandler());
		newTerminalSubMenu.setSWT_Style(SWT.PUSH);
		newTerminalSubMenu.setAccelerator (SWT.CTRL+'T');	
		
		terminalMenu.addSubMenu(newTerminalSubMenu);
		
		SubMenu nextTerminalSubMenu = new SubMenu();
		nextTerminalSubMenu.setText("Next &Terminal\tCtrl+N");		
		nextTerminalSubMenu.setImage(Constant.IMAGE_NEXT_TERMINAL);
		nextTerminalSubMenu.setEventHandler(new NextTerminalEventHandler());
		nextTerminalSubMenu.setSWT_Style(SWT.PUSH);
		nextTerminalSubMenu.setAccelerator (SWT.CTRL+'N');	
		
		terminalMenu.addSubMenu(nextTerminalSubMenu);	
		
		SubMenu focusTerminalSubMenu = new SubMenu();
		focusTerminalSubMenu.setText("Terminal &Focus\tCtrl+A");		
		focusTerminalSubMenu.setEventHandler(new FocusTerminalEventHandler());
		focusTerminalSubMenu.setSWT_Style(SWT.PUSH);
		focusTerminalSubMenu.setAccelerator (SWT.CTRL+'A');	
		
		terminalMenu.addSubMenu(focusTerminalSubMenu);	
		
		/*
		SubMenu ripplySubMenu = new SubMenu();
		ripplySubMenu.setText("Ripply");		
		ripplySubMenu.setEventHandler(new RipplyEventHandler());
		ripplySubMenu.setSWT_Style(SWT.PUSH);
		ripplySubMenu.setImage(Constant.IMAGE_CONFIG_TERMINAL);		
		ripplySubMenu.setAccelerator (SWT.CTRL+'R');	
		ripplySubMenu.setCreateSeparatorBefore(true);
		
		terminalMenu.addSubMenu(ripplySubMenu);
		*/		
		
		SubMenu closeTerminalSubMenu = new SubMenu();
		closeTerminalSubMenu.setText("&Close Terminal\tCtrl+W");		
		closeTerminalSubMenu.setEventHandler(new CloseTerminalEventHandler());
		closeTerminalSubMenu.setSWT_Style(SWT.PUSH);
		closeTerminalSubMenu.setImage(Constant.IMAGE_CLOSE_TERMINAL);		
		closeTerminalSubMenu.setAccelerator (SWT.CTRL+'W');	
		closeTerminalSubMenu.setCreateSeparatorBefore(true);
		
		terminalMenu.addSubMenu(closeTerminalSubMenu);	
		
		SubMenu closeAllTerminalSubMenu = new SubMenu();
		closeAllTerminalSubMenu.setText("&Close All Terminals");		
		closeAllTerminalSubMenu.setEventHandler(new CloseAllTerminalsEventHandler());
		closeAllTerminalSubMenu.setSWT_Style(SWT.PUSH);
		closeAllTerminalSubMenu.setImage(Constant.IMAGE_CLOSE_ALL_TERMINAL);		
		
		terminalMenu.addSubMenu(closeAllTerminalSubMenu);			
		
		return terminalMenu;
	}	
	
	public static WindowMenu createWindowsMenu(){
		WindowMenu windowMenu = new WindowMenu();
		windowMenu.setEventHandler(new WindowEventHandler());
		
		SubMenu winApplicationSubMenu = new SubMenu();
		winApplicationSubMenu.setText("Tab &Servers - Maximize/Minimize\tCtrl+J");		
		winApplicationSubMenu.setEventHandler(new WindownApplicationEventHandler());
		winApplicationSubMenu.setSWT_Style(SWT.PUSH);
		winApplicationSubMenu.setAccelerator (SWT.CTRL+'J');	
		
		windowMenu.addSubMenu(winApplicationSubMenu);
		
		SubMenu winConsoleApplicationSubMenu = new SubMenu();
		winConsoleApplicationSubMenu.setText("Tab &Views - Maximize/Minimize\tCtrl+K");		
		winConsoleApplicationSubMenu.setEventHandler(new WindowConsoleApplicationEventHandler());
		winConsoleApplicationSubMenu.setSWT_Style(SWT.PUSH);
		winConsoleApplicationSubMenu.setAccelerator (SWT.CTRL+'K');	
		
		windowMenu.addSubMenu(winConsoleApplicationSubMenu);
		
		SubMenu winConsoleNavigateApplicationSubMenu = new SubMenu();
		winConsoleNavigateApplicationSubMenu.setText("Tab &Views - Navigate\tCtrl+L");		
		winConsoleNavigateApplicationSubMenu.setEventHandler(new WinConsoleNavigateApplicationEventHandler());
		winConsoleNavigateApplicationSubMenu.setSWT_Style(SWT.PUSH);
		winConsoleNavigateApplicationSubMenu.setAccelerator (SWT.CTRL+'L');	
		
		windowMenu.addSubMenu(winConsoleNavigateApplicationSubMenu);
		
		return windowMenu;
	}

	public static HelpMenu createHelpMenu(){
		HelpMenu helpMenu = new HelpMenu();
		
		SubMenu aboutSubMenu = new SubMenu();
		aboutSubMenu.setText("About");		
		aboutSubMenu.setEventHandler(new EventHandler() {
				public void execute() {
					  AboutDialog dialog = new AboutDialog();
			  		  dialog.createSShell();
			  	  	  dialog.openShell();
				}			
		});
		aboutSubMenu.setSWT_Style(SWT.PUSH);
		aboutSubMenu.setImage(Constant.IMAGE_ABOUT);		
		
		helpMenu.addSubMenu(aboutSubMenu);	
		
		MenuPlugin.addSubMenuHelp(helpMenu);
		
		return helpMenu;
	}
	
	/**
	 * @return the menuBar
	 */
	public static Menu getMenuBar() {
		return menuBar;
	}

	/**
	 * @param menuBar the menuBar to set
	 */
	public static void setMenuBar(Menu menuBar) {
		MenuBarUtils.menuBar = menuBar;
	}
}
