package com.mxterminal.swt.view;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Listener;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TreeItem;

import com.mxdeploy.images.Constant;
import com.mxdeploy.swt.MainShell;
import com.mxterminal.ssh.InteractiveClientSSH;
import com.mxterminal.swt.TerminalFace;
import com.mxterminal.swt.util.SWTUtils;
import com.mxterminal.swt.view.helper.ServerHelper;
import com.mxterminal.swt.view.helper.TerminalHelper;

public class CTopTabFolder extends CTabFolder implements CTabFolder2Listener, MouseListener{
	//static Logger logger = Logger.getLogger(CTopTabFolderServidor.class);
	
	public SashForm sashForm = null;
	
	public CTopTabFolder(Composite parent, int style) {
		super(parent, style);
		sashForm =(SashForm)parent;
		initialize();
	}
	
	public void initialize(){
		setSimple(false);
		setUnselectedImageVisible(true);
		setUnselectedCloseVisible(true);
		setMaximizeVisible(true);
        setMRUVisible(true);
        setSize(400, 200);
        
		SWTUtils.createGridDataMaximized(this);
		
		this.addCTabFolder2Listener(this);
        setMaximized(true);
        sashForm.setMaximizedControl(this);
        //maximize(); 
        
        //ServerTabItemFactory.createTabWindowsServer(this,null);

        Display display = MainShell.sShell.getDisplay();
        
        
//		setSelectionBackground(new Color[] {
//				display.getSystemColor(SWT.COLOR_LIST_BACKGROUND),
//				display.getSystemColor(SWT.COLOR_LIST_BACKGROUND),
//				display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND)
//			}, new int[] {	10,	90	}, true);
		
	    setSelectionBackground(new Color[] {
	            display.getSystemColor(SWT.COLOR_LIST_BACKGROUND),
	            display.getSystemColor(SWT.COLOR_LIST_BACKGROUND),
	            display.getSystemColor(SWT.COLOR_LIST_BACKGROUND) }, new int[] { 20, 100 });		
        
		//creating TabFolder Listeners
		createListeners();

		update();         
        
	}
 
	public void close(CTabFolderEvent event) { 
  	    CTabItem tabItemServer = (CTabItem)event.item;
	    CTabFolder tabFolder = (CTabFolder)event.widget;
	    close(tabItemServer,tabFolder);
	}

	public void close(CTabItem tabItemServer, CTabFolder tabFolder){
	    TreeItem treeItem = (TreeItem)tabItemServer.getData(Constant.KEY_TREEITEM_EXPLORER);
	    if(treeItem!=null && !treeItem.isDisposed()){
	       treeItem.setImage(Constant.IMAGE_SERVER_STOPPED);
	       treeItem.setData(Constant.KEY_TABITEM_SERVER, null); 
	       treeItem.setData(Constant.KEY_ITEM_CONNECTED,false);
	    } 
  	    tabItemServer.setData(Constant.KEY_SERVER_HELPER,null);
        tabItemServer.setData(Constant.KEY_TREEITEM_EXPLORER,null);
	  
	    InteractiveClientSSH client = null;
	    if( tabItemServer.getControl() instanceof CentralComposite ){
		  CentralComposite serverComposite = (CentralComposite)tabItemServer.getControl();
		  TerminalHelper consoleHelper2 = (TerminalHelper)serverComposite.getData();
	      if(consoleHelper2.getSSHInteractiveClient()!=null){
		    client = consoleHelper2.getSSHInteractiveClient();
	      }
	      
	      if(consoleHelper2.getServer()!=null && TerminalFace.bindHash !=null ) {
	    	  List<String> list = TerminalFace.bindHash.get(consoleHelper2.getServer().getHostname());
	    	  if(list!=null){
		    	  for (String bind : list){
		    		   int localPort = 0;
		    		   String sub = bind.substring(12);
		    		   int index = sub.indexOf(" ");
		    		   String var = sub.substring(0,index);
		    		   localPort = (new Integer(var)).intValue();
		    		   if (client.isConnected() && client.isAuthenticated()){ 
		    			   client.getConnection().deleteLocalForward("127.0.0.1", localPort);
		    		   }
		    	  }
	    	  }
	          TerminalFace.bindHash.remove(consoleHelper2.getServer().getHostname());
	      }
	      
	    } else if( tabItemServer.getControl() instanceof TerminalFace ){
		  TerminalFace face = (TerminalFace)tabItemServer.getControl();
	      if(face.getTerminalWin()!=null){
	          client = face.getTerminalComposite().getSSHInteractiveClient();
	      }
	    } 
	
	    int count = 0;
	    if(client==null){
	    	return;
	    }
		while( !client.isConnected() ){
			try {
				count++;
				Thread.sleep(500);
				
				if(client.isUnknownHost()){
					break; 
				}
				
				if(client.getConnection()==null){
					break;
				}
				//logger.debug("Thread.sleep(500)"); 
				if(count>=10) break;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	    
	
	    if (client!=null ){
	    	client.forcedDisconnect();
	    	client.getSSHStdIO().breakPromptLine();
	    	client.getTerminalWin().windowClosed();	     	
	    }
	    
	    
		
	}

	public ServerHelper getServerHelper(){
	  	  CTabItem tabItemServer = getSelection();
		  return (ServerHelper)tabItemServer.getData(Constant.KEY_SERVER_HELPER);
	}

	public void maximize(CTabFolderEvent event) {
		maximize();
	}
	
	public void maximize(){
        setMaximized(true);
        sashForm.setMaximizedControl(this);
	}

	public void minimize(CTabFolderEvent event) { }

	public void restore(CTabFolderEvent event) {
        setMaximized(false);
        sashForm.setMaximizedControl(null);
        //MainShell.sashFormComposite.getWorkingSetComposite().setFocusTabFolder();
	}

	public void showList(CTabFolderEvent event) { }

	public void mouseDoubleClick(MouseEvent e) {
		if(getMaximized()){
	        setMaximized(false);
	        sashForm.setMaximizedControl(null);
		} else {
	        setMaximized(true);
	        sashForm.setMaximizedControl(this);
		}
	}

	public void mouseDown(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseUp(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	private void createListeners(){
		
		final CTopTabFolder topTabFolder = this;
		topTabFolder.addMouseListener(new org.eclipse.swt.events.MouseAdapter() { 
			public void mouseDoubleClick(MouseEvent e) { 
				//logger.debug("mouseDoubleClick");				
				if(topTabFolder.getMaximized()){
					topTabFolder.setMaximized(false);
					MainShell.getSashFormComposite().sashForm.setMaximizedControl(null);
				} else {
					topTabFolder.setMaximized(true);
					MainShell.getSashFormComposite().sashForm.setMaximizedControl(topTabFolder);
				}
			}
		});
		
		
		topTabFolder.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent event) {
		    	  CTabFolder tabFolder = (CTabFolder) event.widget;
		    	  if( tabFolder.getSelection().getControl() instanceof CentralComposite ){
		    		  CentralComposite serverComposite = (CentralComposite)tabFolder.getSelection().getControl();
		    		  TerminalHelper consoleHelper2 = (TerminalHelper)serverComposite.getData();
		    	      if(consoleHelper2.getSSHInteractiveClient()!=null){
		    	    	  consoleHelper2.getSSHInteractiveClient().getTerminalWin().requestFocus();
		    	      }
		    	  } else if( tabFolder.getSelection().getControl() instanceof TerminalFace ){
		    		  TerminalFace face = (TerminalFace)tabFolder.getSelection().getControl();
		    	      if(face.getTerminalWin()!=null){
		    	    	  face.getTerminalWin().requestFocus();
		    	      }
		    	  }

		    	  
		      }
		   });			
		
	}

}
