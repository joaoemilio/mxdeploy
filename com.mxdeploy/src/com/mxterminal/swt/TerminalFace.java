package com.mxterminal.swt;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.mxdeploy.AccountConfig;
import com.mxdeploy.api.domain.Database;
import com.mxdeploy.api.domain.Server;
import com.mxdeploy.swt.MainShell;
import com.mxssh.ManageGateway;
import com.mxterminal.console.ConsoleComposite;
import com.mxterminal.console.ConsoleWin;
import com.mxterminal.swt.util.SWTUtils;
import com.mxterminal.swt.view.CentralTabItemFactory;

public class TerminalFace extends Composite {
	//static Logger logger = Logger.getLogger(TerminalFace.class);
	
    private String    hostname = null;
    private ConsoleComposite termComposite = null;
        
    public static HashMap<String, List<String>> bindHash = new HashMap<String, List<String>>();  //  @jve:decl-index=0:
    
	public TerminalFace(Composite parent, int style, String hostname) {
		super(parent, style);
		this.hostname = hostname;
		initialize();
	}

	private void initialize() {
		createTerminalComposite();
		setSize(new Point(626, 475));
		setLayout(new GridLayout());
	}


	/**
	 * This method initializes terminalComposite	
	 *
	 */
	private void createTerminalComposite() {
		
		Composite scrollComposite = new Composite(this, SWT.V_SCROLL);
		SWTUtils.createGridDataMaximized(scrollComposite);
		SWTUtils.createGridLayoutNoMargins(scrollComposite);
		
		try{
			termComposite = new ConsoleComposite(scrollComposite, SWT.EMBEDDED );
			SWTUtils.createGridDataMaximized(termComposite);
			SWTUtils.createGridLayoutNoMargins(termComposite);

			if( (hostname!=null)&&(hostname.trim().length()!=0) ){
			   termComposite.setHostName(hostname); 
			   termComposite.setAutomaticLogin(Database.usePassSaved);
			   termComposite.run();
			}
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		
	}
	
//    public void connect() {
//       if( ! ManageGateway.isStarted ){
//	       if( AccountConfig.getInstance()!=null && AccountConfig.getInstance().isConnectViaIP() ){
//	    	   for(Server server : Database.getInstance().getServerList()){
//	    		   if(server.getHostname().contains(this.hostname)){
//	    			   if(server.getAddress()!=null && server.getAddress().trim().length()>0){
//	    				   this.hostname = server.getAddress();
//	    			      break;
//	    			   }
//	    		   }
//	    	   }
//	       }
//       }
//	   termComposite.setHostName(this.hostname);
//
//	   termComposite.run();
//	   MainShell.getToolBarViewHelper().configureToolBarView();
//	   
//	   CTabFolder tabFolderServer = MainShell.getCTopTabFolder();
//	   
//	   String regex = "\\d?\\d?\\d[.]\\d?\\d?\\d[.]\\d?\\d?\\d[.]\\d?\\d?\\d";  
//	   Pattern pattern = Pattern.compile( regex );  
//	   Matcher matcher = pattern.matcher( this.hostname );         
//	    
//	   if( this.hostname.matches( regex ) ) {
//	     if( matcher.matches() ) {    
//	    	 tabFolderServer.getSelection().setText(this.hostname);
//	     }
//	   } else {
//		    int indexof = this.hostname.indexOf(".");
//		    if(indexof==-1){
//		       tabFolderServer.getSelection().setText(this.hostname);
//		    } else {
//		       String host = hostname.substring(0,indexof);
//		       tabFolderServer.getSelection().setText(host);
//		    }
//	   } 
//	   
//	   tabFolderServer.setMaximized(true);
//	   MainShell.getSashFormComposite().sashForm.setMaximizedControl(tabFolderServer);
//	   
//    }
    
//	private void createTunnelItem( ToolBar toolbar ) {
//		
//		tunnelToolItem = new ToolItem( toolbar, SWT.NONE );
//		tunnelToolItem.setImage( Constant.IMAGE_TUNNEL );
//        tunnelToolItem.setDisabledImage( Constant.IMAGE_TUNNEL );
//        tunnelToolItem.setToolTipText( "Configure Tunnel" );
//
//        tunnelToolItem.addSelectionListener(new SelectionAdapter() {  
//            public void widgetSelected(SelectionEvent event) {
//            	if(termComposite.getSSHInteractiveClient()==null || !termComposite.getSSHInteractiveClient().isAuthenticated()){
//            		MainShell.sendMessage("Connect on Server first !");
//            	} else {
//	        		TunnelDialog dialog = new TunnelDialog();
//	        		dialog.createSShell("Configure Tunnel");
//	        		dialog.getHelper().setSSHInteractiveClient(termComposite.getSSHInteractiveClient());
//	        		dialog.getHelper().loadBindHash(hostname);
//	        		dialog.openShell();
//            	}
//            }
//        });
//	}
	
//	private void createConfigTerminalItem( ToolBar toolbar ) {
//		
//		configTerminalToolItem = new ToolItem( toolbar, SWT.NONE );
//		configTerminalToolItem.setImage( Constant.IMAGE_CONFIG_TERMINAL );
//		configTerminalToolItem.setDisabledImage( Constant.IMAGE_CONFIG_TERMINAL );
//		configTerminalToolItem.setToolTipText( "Configure Terminal" );
//
//		configTerminalToolItem.addSelectionListener(new SelectionAdapter() {  
//            public void widgetSelected(SelectionEvent event) {
//            	if(termComposite.getSSHInteractiveClient()==null || !termComposite.getSSHInteractiveClient().isAuthenticated()){
//            		MainShell.sendMessage("Connect on Server first !");
//            	} else {
//	        		TerminalSettingsDialog dialog = new TerminalSettingsDialog();
//	        		dialog.createSShell("Configure Terminal");
//	        		dialog.openShell();
//            	}
//            }
//        });
//		
//	}
	
//	private void createMenuTerminItem() {
//		
//		menuTerminalToolItem = new ToolItem( toolbar, SWT.DROP_DOWN );
//		menuTerminalToolItem.setImage( Constant.IMAGE_MENU_TERMINAL);
//		menuTerminalToolItem.setDisabledImage( Constant.IMAGE_MENU_TERMINAL );
//		menuTerminalToolItem.setToolTipText( "Menu Views" );
//
//		menuTerminalToolItem.addListener(SWT.Selection, new Listener() {
//		      public void handleEvent(Event event) {
//		    	  SashForm sashForm = (SashForm)getParent().getParent();
//		    	  final ProcedureViewComposite procedureTreeComposite = (ProcedureViewComposite) sashForm.getChildren()[1];
//
//	    		  if(termComposite.getSSHInteractiveClient()==null || !termComposite.getSSHInteractiveClient().isAuthenticated()){
//	    			  MainShell.sendMessage("Connect on Server first !");
//	    			  return;
//	    		  }
//	    		  
//	    		  if(procedureTreeComposite.getConsoleHelper().getServer()==null){
//	    			  ServerService serverService = new ServerService();
//	    			  hostname = procedureTreeComposite.getConsoleHelper().getTermianlFace().getCombo().getText();
//	    			  Server server = new Server();
//	    			  server.setHostname(hostname);
//	    			  try {
//	    					List<Server> list = serverService.search(server);
//	    					if(list!=null && !list.isEmpty()){
//	    					   server = list.get(0);
//	    					} else {
//	    						server.setPort(22);
//	    						server.setType(Server.SERVERTYPE_SSH);
//	    						server.setName(hostname);
//	    						serverService.create(server);
//	    					}
//	    					procedureTreeComposite.getConsoleHelper().setServer(server);			
//	    			  } catch (ServiceException e) {
//	    					e.printStackTrace();
//	    			  }
//	    		  } else {
//	    			  hostname = procedureTreeComposite.getConsoleHelper().getServer().getHostname();
//	    		  }
//		    	  
//		    	  if (event.detail != SWT.ARROW) {
//		    		  if(procedureTreeComposite.getConsoleHelper().getServer()==null){
//		        		 procedureTreeComposite.getCTabFolder().restore();
//		        		 procedureTreeComposite.getHelper().getServerCommandView();
//		    		  } else if(procedureTreeComposite.getCTabFolder().getSashForm().getMaximizedControl()==null){
//		    			 procedureTreeComposite.getCTabFolder().minimize(); 
//		    		  } else {
//			    	     procedureTreeComposite.getCTabFolder().restore();
//		    		  }
//		    	  } else if (event.detail == SWT.ARROW) {
//		    		  @SuppressWarnings("unused")
//					  Project project = null;
//		    		  @SuppressWarnings("unused")
//					  List<Project> listProject = null;
//		    		  if(procedureTreeComposite.getHelper().getProject()==null){
//		    			 ProjectService projectService = new ProjectService();
//	    				 listProject = projectService.searchProjectByServer(getCombo().getText());
//		    		  } else {
//		    			 project  = procedureTreeComposite.getHelper().getProject();
//		    		  }
//		    		  
//			          Rectangle rect = menuTerminalToolItem.getBounds();
//			          Point pt = new Point(rect.x, rect.y + rect.height);
//			          pt = toolbar.toDisplay(pt); 
//			          
//			          Menu menu = new Menu(MainShell.sShell, SWT.POP_UP);
//			          
//			          if (project!=null){
//				          MenuItem itemProcedures = new MenuItem(menu, SWT.PUSH);
//				          itemProcedures.setText(project.getAlias()+" - Procedures");
//				          itemProcedures.setImage(Constant.IMAGE_PROCEDURES);
//				          itemProcedures.addListener(SWT.Selection, new Listener() {
//					        	  public void handleEvent(Event event) {
//					        		  procedureTreeComposite.getCTabFolder().restore();
//					        		  procedureTreeComposite.getHelper().getProcedureView();
//					        	  }
//				          });
//			          } else if (listProject!=null && !listProject.isEmpty()){
//				          MenuItem itemProcedures = new MenuItem(menu, SWT.CASCADE);
//				          itemProcedures.setText("Projects");
//				          itemProcedures.setImage(Constant.IMAGE_APPLICATION);
//				          
//						  Menu itemProceduresSubMenu = new Menu(itemProcedures);
//						  itemProcedures.setMenu(itemProceduresSubMenu);
//				          for(Project projeto : listProject){
//					          MenuItem projectItem = new MenuItem(itemProceduresSubMenu, SWT.PUSH);
//					          projectItem.setText(projeto.getAlias());
//					          projectItem.setImage(Constant.IMAGE_APPLICATION);
//					          projectItem.setData(projeto);
//					          projectItem.addListener(SWT.Selection, new Listener() {
//					        	  public void handleEvent(Event event) {
//					        		  MenuItem item = (MenuItem)event.widget;
//					        		  Project projeto = (Project)item.getData();
//					        		  procedureTreeComposite.setProject(projeto);
//					        		  procedureTreeComposite.getConsoleHelper().setProject(projeto);
//					        		  procedureTreeComposite.setProject(projeto);
//					        		  procedureTreeComposite.getCTabFolder().restore();
//					        		  procedureTreeComposite.getHelper().getProcedureView();
//					        		  
//					        		  TreeItem[] itens = MainShell.getWorkingSetHelper().getMyProjectTree().getItems();
//					        		  int exist = -1;
//					        		  Project projectTreeItem = null;
//					        		  int count =0;
//					        		  for(count=0;itens.length>count;count++){
//					        			  projectTreeItem = (Project)itens[count].getData();					        			  
//					        			  if(projectTreeItem.getId().equals(projeto.getId())){
//					        				  exist = count;
//					        				  break;
//					        			  }
//					        		  }
//					        		  TreeItem[] serverItem = null;
//					        		  if(exist!=-1){
//				        				  itens[exist].setExpanded(true);
//				        				  serverItem = itens[exist].getItems();
//					        		  } else {
//					        			  MainShell.getWorkingSetHelper().addProjectInMyProjectTreeItem(projeto);
//					        			  itens = MainShell.getWorkingSetHelper().getMyProjectTree().getItems();
//					        			  itens[count].setExpanded(true);  
//					        			  serverItem = itens[count].getItems();
//					        		  }
//					        		  
//			        				  for(int j=0;serverItem.length>j;j++){
//			        					  if( serverItem[j].getData() instanceof Server ){
//			        						  Server server = (Server)serverItem[j].getData();
//			        						  if(server.getHostname().equals(hostname)){
//			        							  serverItem[j].setImage(Constant.IMAGE_SERVER_CONNECTED);
//			        							  break;
//			        						  }
//			        					  }
//			        				  }
//					        		  
//					        	  }
//					          });
//				          }
//			          }
//			          
////			          MenuItem itemProperties = new MenuItem(menu, SWT.PUSH);
////			          itemProperties.setText("Properties View");
////			          itemProperties.setImage(Constant.IMAGE_PROPERTY);
////			          itemProperties.addListener(SWT.Selection, new Listener() {
////				        	  public void handleEvent(Event event) {
////				        		  procedureTreeComposite.getCTabFolder().restore();
////				        		  procedureTreeComposite.getHelper().getPropertieView();
////				        	  }
////			          });
//	
//			          MenuItem itemServerCommands = new MenuItem(menu, SWT.PUSH);
//			          itemServerCommands.setText("Server Commands View");
//			          itemServerCommands.setImage(Constant.IMAGE_SERVER_COMMAND);
//			          itemServerCommands.addListener(SWT.Selection, new Listener() {
//				        	  public void handleEvent(Event event) {
//				        		  procedureTreeComposite.getCTabFolder().restore();
//				        		  procedureTreeComposite.getHelper().getServerCommandView();
//				        	  }
//			          });
//			          
////			          MenuItem itemSftpView = new MenuItem(menu, SWT.PUSH);
////			          itemSftpView.setText("Sftp Explorer");
////			          itemSftpView.setImage(Constant.IMAGE_SFTP_EXPLORER);
////			          itemSftpView.addListener(SWT.Selection, new Listener() {
////				        	  public void handleEvent(Event event) {
////				        		  procedureTreeComposite.getCTabFolder().restore();
////				        		  procedureTreeComposite.getHelper().getSftpView();
////				        	  }
////			          });
//			          
//
////			          MenuItem itemSendCommands = new MenuItem(menu, SWT.PUSH);
////			          itemSendCommands.setText("Watch Problem View");
////			          itemSendCommands.setImage(Constant.IMAGE_WATCH_PROBLEM);
////			          itemSendCommands.addListener(SWT.Selection, new Listener() {
////				        	  public void handleEvent(Event event) {
////				        		  procedureTreeComposite.getCTabFolder().restore();
////				        		  procedureTreeComposite.getHelper().getPropertieView();
////				        		  procedureTreeComposite.getHelper().getWatchProblemView();
////				        	  }
////			          });
//			          
//			          menu.setLocation(pt.x, pt.y);
//			          menu.setVisible(true);
//		        }
//		      }
//		    });
//		
//	}	
	
	
//	private void createDuplicateConnectionItem( ToolBar toolbar ) {
//		
//		duplicateConnectionToolItem = new ToolItem( toolbar, SWT.NONE );
//		duplicateConnectionToolItem.setImage( Constant.IMAGE_DUPLICATE_CONNECT);
//		duplicateConnectionToolItem.setDisabledImage( Constant.IMAGE_DISABLE_DUPLICATE_CONNECT);
//		duplicateConnectionToolItem.setToolTipText( "Duplicate Connection"  );
//		duplicateConnectionToolItem.setEnabled(false);
//
//		duplicateConnectionToolItem.addSelectionListener(new SelectionAdapter() {
//            public void widgetSelected(SelectionEvent event) {
//                  duplicateTerminal();
//            }
//        });
//	}

//	private void createLinkServerAccountItem( ToolBar toolbar ) {
//		
//		linkServerAccountToolItem = new ToolItem( toolbar, SWT.NONE );
//		linkServerAccountToolItem.setImage( Helper.createImageDescriptorFor("icons/enabled/linkwithdatabase.gif").createImage() );
//		linkServerAccountToolItem.setDisabledImage( Helper.createImageDescriptorFor("icons/disable/linkwithdatabase.gif").createImage());
//		linkServerAccountToolItem.setToolTipText( "Link Server with Database"  );
//
//		linkServerAccountToolItem.addSelectionListener(new SelectionAdapter() {
//            public void widgetSelected(SelectionEvent event) {
//                
//                  //duplicateTerminal();
//            }
//        });
//	}
	
	public void duplicateTerminal(){
		CTabFolder tabFolderServer = MainShell.getCTopTabFolder();
		if(getTerminalComposite().isAuthenticated())
		   CentralTabItemFactory.createTabITerminal(tabFolderServer, getTerminalComposite().getSSHInteractiveClient().getHost());
	}
	
	public ConsoleWin getTerminalWin(){
		return termComposite.getTerminalWin();
	}
	
	public ConsoleComposite getTerminalComposite(){
		return termComposite;
	}
	
    public List<String> getHostNames() {
    	List<String>        list = null;
    	File        tmpFile;
		String      fileName     = null;
		String sshHomeDir = Database.getServerPath(); //System.getProperty("user.dir");
	  
		fileName = sshHomeDir; //+ "/hostkeys";
		tmpFile = new File(fileName);

		if(!tmpFile.exists()) {
			try {
				tmpFile.mkdir();
			} catch (Throwable t) {
				//logger.error(t.getMessage());
			}
		}

		if(!tmpFile.exists() || !tmpFile.isDirectory()) {
		  //logger.warn("No hostkeys directory");
          return null;
		}
		File[] files = tmpFile.listFiles();
		list = new ArrayList<String>();
    	for (int i = 0; i < files.length; i++) {
    		if(files[i].getName().endsWith(".xml")){
	    		//int lengh = new Long(files[i].getName().length()-4).intValue();
	    		//String hostname = files[i].getName().substring(7,lengh);
    			String name = files[i].getName().replace(".xml", "");
	    		list.add(name);
    		}
    	}    
    	return list;
    }
    
//    public void setComboFocus(){
//    	combo.setFocus();
//    }

	/**
	 * @return the hostname
	 */
	public String getHostname() {
		return hostname;
	}

	/**
	 * @return the combo
	 */
//	public Combo getCombo() {
//		return combo;
//	}

	/**
	 * @return the busy
	 */
//	public BusyIndicator getBusy() {
//		return busy;
//	}

//	public Composite getToolbarComposite() {
//		return toolbarComposite;
//	}

}


