package com.mxdeploy.images;

import java.net.URL;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class Constant {

	public static final String VERSION = "1.0.0";  
	public static final String MSN_SEE_LOG_ERROR = "Please see the log entries at the end of the file '<MX_HOME>\\bin\\SystemOut.log' for more information";
	/**
	 * As Vari�veis abaixo est�o no TreeItem de servidor, os que tem icone de server
	 * KEY_SERVIDOR = treeItemExplorer.getData(KEY_SERVIDOR)
	 * KEY_TABITEM_SERVER = serverTabItem.getData(KEY_TABITEM_SERVER)
	 */
	public static final String KEY_SERVIDOR = "KEY_SERVIDOR";
	public static final String KEY_TABITEM_SERVER = "KEY_TABITEM_SERVER";
	public static final String KEY_TABITEM_PROJECT_PROCEDURES = "KEY_TABITEM_PROJECT_PROCEDURES";	
	public static final String KEY_PROJECT = "KEY_PROJECT";	
	public static final String KEY_ITEM_CONNECTED = "KEY_ITEM_CONNECTED";	
	public static final String KEY_PROPERTY_LIST = "KEY_PROPERTY_LIST";
	public static final String KEY_SFTP_SERVER = "KEY_SFTP_SERVER";
	
	
	/**
	 * As Vari�veis abaixo est�o na TabItem do Servidor
	 * KEY_TREEITEM_EXPLORER = serverTabItem.getData(KEY_TREEITEM_EXPLORER)
	 * KEY_SERVER_HELPER = serverTabItem.getData(KEY_SERVER_HELPER)
	 */
	public static final String KEY_TREEITEM_EXPLORER = "KEY_TREEITEM_EXPLORER";	
	public static final String KEY_SERVER_HELPER = "KEY_CONSOLE_HELPER";
	
	//public static final String KEY_TABITEM_TREEITEM = "KEY_TABITEM_TREEITEM";
	//public static final String KEY_TABITEM_CONSOLE = "KEY_TABITEM_CONSOLE";
	//public static final String KEY_CONSOLE_TEXTARE = "consoleTextArea";
	//public static final String KEY_TREE_PROJETO = "treeProjeto";	
	//public static final String KEY_CONSOLE_COMPOSITE = "consoleComposite";
	//public static final String KEY_DYNIN = "dynIn";
	//public static final String KEY_DYNOUT = "dynOut";
	//public static final String KEY_CONSOLE_HELPER = "KEY_CONSOLE_HELPER";

	public static final Image IMAGE_WORKBOOK = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/system_persp.gif"));
	public static final Image IMAGE_PROJECT_EXPLORER = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/connectionFolder.gif"));
	public static final Image IMAGE_MODULE_VIEWER = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/selectbean_view.gif"));
	public static final Image IMAGE_MODULE_ITEM = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/threadgroup_obj.gif"));
	public static final Image IMAGE_SERVER_STOPPED = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/server_stopped.gif"));
	//public static final Image IMAGE_CONNECT = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/connect.gif"));
	public static final Image IMAGE_SERVER_CONNECTED = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/sevidor_connected.gif"));
	public static final Image IMAGE_CONSOLE = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/host_obj.gif"));
	public static final Image IMAGE_NEW_JAVA = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/newjworkingSet_wiz.gif"));
	public static final Image IMAGE_JAVABEAN = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/javavisualeditor_obj.gif"));
	public static final Image IMAGE_MXDEPLOY_MEDIO =  new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/mxdeploy_medio.gif"));
	
	public static final Image CHECKED = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/icon_checked.png"));
	public static final Image UNCHECKED = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/icon_unchecked.png"));
	
	public static final Image IMAGE_JAVA = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/jcu_obj.gif"));
	public static final Image IMAGE_SPLASH = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/id-w3-sitemark-large.gif"));
	public static final Image IMAGE_TREE_ITEM_RUNNING = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/trace_start.gif"));
	public static final Image IMAGE_COMMAND = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/inew_obj.gif"));
	public static final Image IMAGE_COMMAND_ITEM = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/setnext_co.gif"));
	
//	public static final Image IMAGE_NEW_WORKING_SET = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/new_wiz.gif"));
//	public static final Image IMAGE_WORKING_SET = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/defaultview_misc.gif"));	
	public static final Image IMAGE_NEW_SERVER = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/wiz_new_server.gif"));
	public static final Image IMAGE_ADD_SERVER_IN_PROJECT = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/wiz_new_server_project.gif"));
	public static final Image IMAGE_PROCEDURES = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/procedure_view.gif"));
	 
	public static final Image IMAGE_APPLICATION = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/defaultview_misc.gif"));
	public static final Image IMAGE_FILTER  = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/filter.gif"));
	public static final Image IMAGE_COLLAPSEALL  = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/collapseall.gif"));	
	public static final Image IMAGE_DISCONECT  = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/disconnect_server.gif"));
	public static final Image IMAGE_CONNECT  = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/connection.gif"));
	public static final Image IMAGE_DB_CONNECT  = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/server_explorer.gif")); 
	public static final Image IMAGE_DUPLICATE_CONNECT  = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/nav_duplicate.gif"));
	public static final Image IMAGE_DISABLE_DUPLICATE_CONNECT  = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/disable/nav_duplicate.gif"));
	public static final Image IMAGE_TUNNEL  = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/tunnel.gif"));
	public static final Image IMAGE_CONFIG_TERMINAL  = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/config_terminal.gif"));
	public static final Image IMAGE_MENU_TERMINAL  = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/new_wiz.gif"));
	public static final Image IMAGE_DISCOVER  = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/discover.gif"));
	public static final Image IMAGE_ABOUT  = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/about.gif"));	
	
	
	public static final Image IMAGE_CLEAR_CONSOLE  = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/clear_co.gif"));	
	public static final Image IMAGE_TELNET  = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/environment_co.gif"));
	public static final Image IMAGE_SERVER  = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/servidor.gif"));	
	public static final Image IMAGE_SERVER_WINDOWS  = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/windows_start.jpg"));
	public static final Image IMAGE_CLOSE_TERMINAL  = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/close_terminal.gif"));	
	public static final Image IMAGE_CLOSE_ALL_TERMINAL  = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/close_all_terminals.gif"));	
	public static final Image IMAGE_NEXT_TERMINAL  = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/next_terminal.gif"));
	public static final Image IMAGE_WATCH_PROBLEM  = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/jwatch_obj.gif"));
	
	public static final Image IMAGE_RUN_CONSOLE  = new Image(Display.getCurrent(), Constant.class.getResourceAsStream("/enabled/resume_co.gif"));
	public static final Image IMAGE_RUN2_CONSOLE  = new Image(Display.getCurrent(), Constant.class.getResourceAsStream("/enabled/start_task.gif"));	
	public static final Image IMAGE_PAUSE_CONSOLE  = new Image(Display.getCurrent(), Constant.class.getResourceAsStream("/enabled/suspend_co.gif"));
	public static final Image IMAGE_STOP_CONSOLE  = new Image(Display.getCurrent(), Constant.class.getResourceAsStream("/enabled/terminate_co.gif")); 
	public static final Image IMAGE_STOP_ALL_CONSOLE  = new Image(Display.getCurrent(), Constant.class.getResourceAsStream("/enabled/terminate_all_co.gif"));	
	public static final Image IMAGE_REFRESH  = new Image(Display.getCurrent(), Constant.class.getResourceAsStream("/enabled/refresh.gif"));
	public static final Image IMAGE_REMOVE_COMMAND  = new Image(Display.getCurrent(), Constant.class.getResourceAsStream("/enabled/delete.gif"));	
	//public static final Image IMAGE_EXIT  = new Image(Display.getCurrent(), Constant.class.getResourceAsStream("/delete.gif"));	
	
//    public static final Image IMAGE_ADD_EXPLORER = new Image(Display.getCurrent(), PowerSearchExplorerComposite.class.getClass().getResourceAsStream("/enabled/e_search_results_view.gif"));  //  @jve:decl-index=0:
//	public static final Image IMAGE_ROOT = new Image(Display.getCurrent(), PowerSearchExplorerComposite.class.getClass().getResourceAsStream("/enabled/server_explorer.gif"));  
//	public static final Image IMAGE_APPLICATION = new Image(Display.getCurrent(), PowerSearchExplorerComposite.class.getClass().getResourceAsStream("/enabled/layout_co.gif")); 
//	public static final Image IMAGE_PWD_PROFILE = new Image(Display.getCurrent(), PowerSearchExplorerComposite.class.getClass().getResourceAsStream("/enabled/primaryKey.gif"));  //  @jve:decl-index=0:
	
	public static final Image IMAGE_NOTES = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/notes.gif"));

	public static final Image IMAGE_BLUEBANNER = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/banner_blue.jpg"));
	//public static final Image IMAGE_COOLBAR = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/banner.jpg"));
	public static final Image IMAGE_IMPORT_SERVER   = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/import_servers.gif"));
	public static final Image IMAGE_WEBSPHERE       = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/websphere.gif"));
	public static final Image IMAGE_LOGO  = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/alt32.gif"));
	public static final Image IMAGE_TRAY  = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/alt16.gif"));
	
	public static final Image IMAGE_PROPERTY        = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/prop_ps.gif"));
	public static final Image IMAGE_SERVER_COMMAND  = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/file.gif"));
	public static final Image IMAGE_SAVE    = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/save.gif"));
	public static final Image IMAGE_FOLDER  = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/folder.gif"));	
	public static final Image IMAGE_FOLDER_OPENED  = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/folderOpened.gif"));	
	public static final Image IMAGE_BROWSER = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/browser.gif"));	
	public static final Image IMAGE_FILE    = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/file.gif"));
	public static final Image IMAGE_WORD    = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/word.gif"));  
	public static final Image IMAGE_EXCEL   = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/excel.gif"));
	public static final Image IMAGE_SEARCH  = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/e_search_results_view.gif"));
	public static final Image IMAGE_SERVER_CONNECT    = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/system_view.gif"));	
	public static final Image IMAGE_SFTP_EXPLORER       = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/system_persp.gif"));
	public static final Image IMAGE_SFTP_OPEN_EXPLORER  = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/team_view.gif"));
	public static final Image IMAGE_GATEWAY_YELLOW  = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/yellow.gif"));
	public static final Image IMAGE_GATEWAY_GREEN  = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/green.gif"));
	public static final Image IMAGE_GATEWAY_RED  = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/red.gif"));
	
	public static final Image IMAGE_GEANY = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/Geany_logo.png"));
	public static final Image IMAGE_ACTION = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/action/organization.png"));	
	
	public static final URL EXEC_PUTTY = Constant.class.getClass().getResource("/enabled/putty.exe"); 
		
	//public static final URL EXEC_IEXPLORER = Constant.class.getClass().getResource("/IEXPLORE.EXE");
	 
//	public static final Image IMAGE_BUSY1 = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/busy1.gif"));
//	public static final Image IMAGE_BUSY2 = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/busy2.gif"));
//	public static final Image IMAGE_BUSY3 = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/busy3.gif"));
//	public static final Image IMAGE_BUSY4 = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/busy4.gif"));
//	public static final Image IMAGE_BUSY5 = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/busy5.gif"));
//	public static final Image IMAGE_BUSY6 = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/busy6.gif"));
//	public static final Image IMAGE_BUSY7 = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/busy7.gif"));
//	public static final Image IMAGE_BUSY8 = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/busy8.gif"));
//	public static final Image IMAGE_BUSY9 = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/busy9.gif"));
//	public static final Image IMAGE_BUSY10 = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/busy10.gif"));
//	public static final Image IMAGE_BUSY11 = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/busy11.gif"));
//	public static final Image IMAGE_BUSY12 = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/busy12.gif"));
//	public static final Image IMAGE_BUSY13 = new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/enabled/busy13.gif"));
	
	
}
