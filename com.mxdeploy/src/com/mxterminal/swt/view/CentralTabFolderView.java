package com.mxterminal.swt.view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Listener;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolItem;

import com.mxdeploy.api.domain.Database;
import com.mxdeploy.api.domain.Server;
import com.mxdeploy.api.service.ServerService;
import com.mxdeploy.api.service.ServiceException;
import com.mxterminal.swt.view.helper.TerminalHelper;

public class CentralTabFolderView extends CTabFolder implements CTabFolder2Listener{
	//static Logger logger = Logger.getLogger(CTabFolderConsole.class);
	
	public ToolBarViewComposite toolBarViewComposite = null;
	public Boolean saveEnable = false;
	public Boolean firstgetText = false;
	public Boolean firstgetProperty = false;
	public Properties commandProp = null;
	
	public CentralTabFolderView(Composite parent, int style) {
		super(parent, style);
		initialize();
	}
	
	/**
	 * @return the runToolItem
	 */
	public ToolItem getRunToolItem() {
		return toolBarViewComposite.runToolItem;
	}


	private void initialize() {
		toolBarViewComposite = new ToolBarViewComposite(this, SWT.NONE);
		
		setTopRight(toolBarViewComposite, SWT.FILL );
//		if( System.getProperty("os.name").equals("Windows XP") ){ 
//		    setTabHeight(21);
//		}
		setSimple(false);
		setUnselectedImageVisible(false);
		setUnselectedCloseVisible(false); 
		//setMaximizeVisible(true);
		setMinimizeVisible(true);
		setMRUVisible(false);
//		Display display = MainShell.sShell.getDisplay();
//		setSelectionBackground(new Color[] {
//				display.getSystemColor(SWT.COLOR_LIST_BACKGROUND),
//				display.getSystemColor(SWT.COLOR_LIST_BACKGROUND),
//				display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND)
//			}, new int[] {	10,	90	}, true);
		
		
		this.addCTabFolder2Listener(this);
		
		createListeners();
	}

	public void close(CTabFolderEvent event) {	
		if(getItemCount()==1){
			minimize();
			event.doit=false;
		}
	} 

	public SashForm getSashFormExplorerServer(){
		TerminalComposite consoleComposite = (TerminalComposite)getParent();
		return consoleComposite.getConsoleHelper().getServerHelper().getSashFormExplorerServer();
	}
	
	public SashForm getSashForm(){
		return (SashForm)getParent().getParent();
	}	
	
	public void maximize(CTabFolderEvent event) {  
        setMaximized(true);
        //getSashFormExplorerServer().setMaximizedControl(getParent());
        getSashForm().setMaximizedControl(getParent());
	}

	public void minimize(CTabFolderEvent event) {
		minimize();
	    
	    //getSashForm().setWeights(new int [] {100,0});
	}
	
	public void minimize(){
		ViewComposite viewComposite = (ViewComposite)getParent();
	    getSashForm().setMaximizedControl(viewComposite.getTerminalHelper().getComposite());
	}

	public void restore(){
        //setMaximized(true);
        //getSashFormExplorerServer().setMaximizedControl(getParent());
        getSashForm().setMaximizedControl(null);		
	}
	
	public void restore(CTabFolderEvent event) {
        setMaximized(false);
        setMinimized(false);
        getSashForm().setMaximizedControl(null);	
        getSashForm().setWeights(new int [] {65,35}); 
        //getSashFormExplorerServer().setMaximizedControl(null);
	}

	public void showList(CTabFolderEvent event) {
		
	}

	/**
	 * @return the toolBarConsoleComposite
	 */
	public ToolBarViewComposite getToolBarViewComposite() {
		return toolBarViewComposite;
	}

	public TerminalHelper getConsoleHelper() {
		return toolBarViewComposite.getToolBarViewHelper().getTerminalHelper();
	}
	
	private void createListeners(){	
		final CentralTabFolderView topTabFolder = this;		
		topTabFolder.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent event) {
		    	  CentralTabFolderView tabFolder = (CentralTabFolderView) event.widget;
	    		  tabFolder.getToolBarViewComposite().runToolItem.setEnabled(false);
	    		  tabFolder.getToolBarViewComposite().pauseToolItem.setEnabled(false);
	    		  tabFolder.getToolBarViewComposite().stopToolItem.setEnabled(false);
	    		  tabFolder.getToolBarViewComposite().saveToolItem.setEnabled(saveEnable);
	    		  
	    		  if (tabFolder.getSelection().getControl() instanceof CommandComposite){
	    			  reloadServerCommand();
    		          CommandComposite commandComposite = (CommandComposite)getSelection().getControl();
    		          if( commandComposite.getTextArea().getSelectionText()!=null 
    		          &&  commandComposite.getTextArea().getSelectionText().length()>0 ){
    		        	  tabFolder.getToolBarViewComposite().createProcedureToolItem.setEnabled(true);
    		          } else {
    		        	  tabFolder.getToolBarViewComposite().createProcedureToolItem.setEnabled(false);
	    			  }
	    		  } else {
	    			  toolBarViewComposite.saveToolItem.setEnabled(false);
		        	  tabFolder.getToolBarViewComposite().createProcedureToolItem.setEnabled(false);
	    		  }
	    	}
		 });			
	}
	
	public void reloadServerCommand(){
		  String hostname = getConsoleHelper().getTermianlFace().getHostname();
		  if(getConsoleHelper().getServer()==null){
			 ServerService serverService = new ServerService();			  
			 Server server = new Server();
			 server.setHostname(hostname);
			 try {
				List<Server> list = serverService.search(server);
				if(list!=null && !list.isEmpty()){
				   server = list.get(0);
				} else {
					server.setPort(22);
					server.setType(Server.SERVERTYPE_SSH);
					server.setName(hostname);
					serverService.create(server);
				}
				getConsoleHelper().setServer(server);			
			 } catch (ServiceException e) {
				e.printStackTrace();
			 }
		  }
		  
          CommandComposite commandComposite = (CommandComposite)getSelection().getControl();
          if(commandComposite.getTextArea().getText().trim().length()!=0){
        	  return;
          }
		  //loadPropertyCommand();
		  
		  String home = Database.getServerPath(); 
		  
		  
		  String filename = hostname+".txt";
		  File file = new File(home+"/"+filename);
		  if(!file.exists()){
			  return;
		  }
		  BufferedReader input;
		  try {
			  input = new BufferedReader(new FileReader(file));
			  try {
		          StringBuffer lineBuffer = new StringBuffer();
		          String line = null;
		          while (( line = input.readLine()) != null){
		        	  lineBuffer.append(line+"\r\n");
		          }
		          commandComposite.getTextArea().setText(lineBuffer.toString());
			  } catch (FileNotFoundException e) {
				e.printStackTrace();
			  } finally {
    		    try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
	          }
	      } catch (IOException e) {
			e.printStackTrace();
	      }					  
		  toolBarViewComposite.saveToolItem.setEnabled(false);
		  saveEnable=false; 
		  
	}
	
}
