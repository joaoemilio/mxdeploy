package com.mxterminal.swt.view;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.mxdeploy.api.domain.Project;
import com.mxdeploy.api.domain.Server;
import com.mxdeploy.images.Constant;
import com.mxdeploy.plugin.swt.PluginConsoleComposite;
import com.mxterminal.swt.TerminalFace;
import com.mxterminal.swt.util.SWTUtils;
import com.mxterminal.swt.view.helper.ServerHelper;
import com.mxterminal.swt.view.helper.TerminalHelper;

public class CentralTabItemFactory {
	
	//static Logger logger = Logger.getLogger(ServerTabItemFactory.class);
	
	public static CTabItem createTabItemServer(Project project, Server server, final CTabFolder cTabFolder){
		
		//Cursor cursorWait = new Cursor(MainShell.display,SWT.CURSOR_WAIT);
		//MainShell.sShell.setCursor(cursorWait);
		
		CentralComposite centralComposite = new CentralComposite(cTabFolder,SWT.NONE,project,server);
		final ServerHelper serverHelper = centralComposite.getServerHelper();
		
		TerminalHelper consoleHelper = serverHelper.getTerminalHelper();
		consoleHelper.setServerHelper(serverHelper);
		consoleHelper.setServer(server);
		consoleHelper.setProject(project);
		centralComposite.setData(consoleHelper);
		
		CTabItem serverTabItem = new CTabItem(cTabFolder, SWT.CLOSE);
		if( server != null ){
			serverTabItem.setText(server.getName());
		} else {
			serverTabItem.setText("Hostname");
		}
		serverTabItem.setControl(centralComposite);

		serverTabItem.setImage(Constant.IMAGE_SERVER_CONNECT);
		
		cTabFolder.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent event) {
		    	  CTabFolder tabFolder = (CTabFolder) event.widget;
		    	  if( tabFolder.getSelection().getControl() instanceof CentralComposite ){
		    		  CentralComposite centralComposite = (CentralComposite)tabFolder.getSelection().getControl();
		    		  TerminalHelper consoleHelper2 = (TerminalHelper)centralComposite.getData();
		    	      if( consoleHelper2.getSSHInteractiveClient()!=null){
		    	    	  consoleHelper2.getSSHInteractiveClient().getTerminalWin().requestFocus();
		    	      }
		    	  }
		      }
	    });			
		
		cTabFolder.setSelection(serverTabItem);
		
		serverTabItem.setData(Constant.KEY_SERVER_HELPER,serverHelper);
		
		return serverTabItem;
	}

	public static CTabItem createTabItemServer(CTabFolder cTabFolder){
		CentralComposite serverComposite = new CentralComposite(cTabFolder,SWT.NONE,null,null);
		final ServerHelper serverHelper = serverComposite.getServerHelper();
		
		TerminalHelper consoleHelper = serverHelper.getTerminalHelper();
		consoleHelper.setServerHelper(serverHelper);
		consoleHelper.setServer(null);
		consoleHelper.setProject(null);
		serverComposite.setData(consoleHelper);
		
		CTabItem serverTabItem = new CTabItem(cTabFolder, SWT.CLOSE);
		
		serverTabItem.setText("Hostname");
		serverTabItem.setControl(serverComposite);

		serverTabItem.setImage(Constant.IMAGE_SERVER_CONNECT);
		
		cTabFolder.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent event) {
		    	  CTabFolder tabFolder = (CTabFolder) event.widget;
		    	  if( tabFolder.getSelection().getControl() instanceof CentralComposite ){
		    		  CentralComposite serverComposite = (CentralComposite)tabFolder.getSelection().getControl();
		    		  TerminalHelper consoleHelper2 = (TerminalHelper)serverComposite.getData();
		    	      if(consoleHelper2.getSSHInteractiveClient()!=null){
		    	    	 consoleHelper2.getSSHInteractiveClient().getTerminalWin().requestFocus();
		    	      }
		    	  }
		      }
	    });			
		
		serverTabItem.setData(Constant.KEY_SERVER_HELPER,serverHelper);
		
		cTabFolder.setSelection(serverTabItem);
		
		return serverTabItem;
	}
	
	public static CTabItem createTabItemServer(CTabFolder cTabFolder, String hostname){
		CentralComposite serverComposite = new CentralComposite(cTabFolder,SWT.NONE,null,null);
		final ServerHelper serverHelper = serverComposite.getServerHelper();
		
		TerminalHelper consoleHelper = serverHelper.getTerminalHelper();
		consoleHelper.setServerHelper(serverHelper);
		consoleHelper.setServer(null);
		consoleHelper.setProject(null);
		serverComposite.setData(consoleHelper);
		
		
		CTabItem serverTabItem = new CTabItem(cTabFolder, SWT.CLOSE);
		
		serverTabItem.setText("Hostname");
		serverTabItem.setControl(serverComposite);

		serverTabItem.setImage(Constant.IMAGE_SERVER_CONNECT);
		
		cTabFolder.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent event) {
		    	  CTabFolder tabFolder = (CTabFolder) event.widget;
		    	  if( tabFolder.getSelection().getControl() instanceof CentralComposite ){
		    		  CentralComposite serverComposite = (CentralComposite)tabFolder.getSelection().getControl();
		    		  TerminalHelper consoleHelper2 = (TerminalHelper)serverComposite.getData();
		    	      if(consoleHelper2.getSSHInteractiveClient()!=null){
		    	    	 consoleHelper2.getSSHInteractiveClient().getTerminalWin().requestFocus();
		    	      }
		    	  }
		      }
	    });			
		
		serverTabItem.setData(Constant.KEY_SERVER_HELPER,serverHelper);
		
		cTabFolder.setSelection(serverTabItem);
		
		return serverTabItem;
	}

	public static CTabItem createTabITerminal(final CTabFolder cTabFolder,String hostname){
		TerminalFace terminalFace = new TerminalFace(cTabFolder,SWT.NONE, hostname);
//		terminalFace.getCombo().setText(hostname);
		SWTUtils.createGridLayoutNoMargins(terminalFace);
		SWTUtils.createGridDataMaximized(terminalFace);		

		CTabItem serverTabItem = new CTabItem(cTabFolder,SWT.CLOSE);
		if(hostname!=null){ 
		   String regex = "\\d?\\d?\\d[.]\\d?\\d?\\d[.]\\d?\\d?\\d[.]\\d?\\d?\\d";  
		   Pattern pattern = Pattern.compile( regex );  
		   Matcher matcher = pattern.matcher( hostname );         
		    
		   if( hostname.matches( regex ) ) {
		     if( matcher.matches() ) {  
		    	 serverTabItem.setText("Terminal");
		     }
		   } else {
			    int indexof = hostname.indexOf(".");
			    String host = hostname.substring(0,indexof);
			    serverTabItem.setText(host);
		   } 
//		   terminalFace.duplicateConnectionToolItem.setEnabled(true);		   
//		   terminalFace.connectToolItem.setEnabled(false);
		} else {
			serverTabItem.setText("Terminal"); 			
		}
		serverTabItem.setControl(terminalFace);
		serverTabItem.setImage(Constant.IMAGE_CONSOLE);
		
		cTabFolder.setSelection(serverTabItem);
		
		
//		terminalFace.setComboFocus();
		return serverTabItem;
	}


	
	/**
	 * Create new window for the Plugin Console
	 * @param cTabFolder
	 * @param hostname
	 * @return
	 */
	public static CTabItem createTabItemPluginConsole(final CTabFolder cTabFolder){
		PluginConsoleComposite pluginComposite = new PluginConsoleComposite(cTabFolder, SWT.NONE);

		//SWTUtils.createGridLayoutNoMargins(pluginComposite);
		//SWTUtils.createGridDataMaximized(pluginComposite);		

		CTabItem tabItem = new CTabItem(cTabFolder,SWT.CLOSE);
		tabItem.setControl(pluginComposite);
		tabItem.setImage(Constant.IMAGE_CONFIG_TERMINAL);
		
		cTabFolder.setSelection(tabItem);
		return tabItem;
	}

	
	
}
