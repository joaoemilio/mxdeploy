package com.mxterminal.swt.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import com.mxdeploy.api.domain.Project;
import com.mxdeploy.images.Constant;
import com.mxdeploy.swt.MainShell;
import com.mxdeploy.swt.command.CommandByServerFactory;
import com.mxterminal.swt.util.SWTUtils;
import com.mxterminal.swt.view.helper.ProcedureViewHelper;
import com.mxterminal.swt.view.helper.TerminalHelper;

/**
 * @author fbsantos
 *
 */
public class ViewComposite extends Composite implements MenuListener {

	public Tree tree = null;
	public Label label = null;
	public ProcedureViewHelper helper = null;
	public TreeColumn treeColumnDescription = null;
	public TreeColumn treeColumnRuntime = null;
	public Menu popupMenu = null;
	public Project project =null;  
	
	public CentralTabFolderView cTabFolder = null;
	
	public TerminalHelper viewHelper = null;
	
	public CTabItem procedureTabItem = null; 
	
	public CommandComposite  commandComposite = null;
	
	public ViewComposite(Composite parent, int style, Project project) {
		super(parent, style);
		this.project = project;
		initialize();
		//this.server = server;		
		
	}

	private void initialize() {
		SWTUtils.createGridDataMaximized(this);
		SWTUtils.createGridLayoutNoMargins(this);
		createCTabFolder();  	
	}
	
	private void createCTabFolder() {
		helper = new ProcedureViewHelper(this);
		
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.horizontalSpacing = 0;
		gridLayout1.marginWidth = 0;
		gridLayout1.marginHeight = 0;
		
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessVerticalSpace = true; 

		this.setLayout(gridLayout1);
		
	    cTabFolder = new CentralTabFolderView(this, SWT.BOTTOM );
	    cTabFolder.setLayoutData(gridData);
	    cTabFolder.setLayout(gridLayout1);
		
		if(project!=null){			
		   createProcedureTabItem(true);
		   createServerCommandTabItem(false);
		} else {
			createServerCommandTabItem(true);
		}

	}	
	
	public void createProcedureTabItem(boolean selectTabItem){
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessVerticalSpace = true; 

		CTabItem procedureTabItem = new CTabItem(cTabFolder, SWT.NONE);
		procedureTabItem.setText(project.getAlias()+" - Procedures");
		procedureTabItem.setImage(Constant.IMAGE_PROCEDURES);
		
		tree = new Tree(cTabFolder, SWT.FULL_SELECTION );
		tree.setHeaderVisible(true);
		tree.setLinesVisible(false);
		tree.setLayoutData(gridData);
		
		tree.addListener(SWT.Selection, new Listener() {
		   public void handleEvent(Event e) { 
			   helper.selectTreeItem(e);
		   }
	    });		
		
		treeColumnDescription = new TreeColumn(tree, SWT.NONE);
		treeColumnDescription.setWidth(580);
		treeColumnDescription.setMoveable(true);
		treeColumnDescription.setText("Description");
		treeColumnRuntime = new TreeColumn(getTreeProcedure(), SWT.NONE);
		treeColumnRuntime.setWidth(60);
		treeColumnRuntime.setText("Runtime");
		
		popupMenu = new Menu(tree);
	    tree.setMenu(popupMenu); 
	    
	    popupMenu.addMenuListener(this); 	    
		
		
	    procedureTabItem.setControl(tree);
	    
		if(selectTabItem){
		   cTabFolder.setSelection(procedureTabItem);
		}
		Display.getDefault().asyncExec(new Runnable(){
			public void run(){ 
				Cursor cursorArrow = new Cursor(MainShell.display,SWT.CURSOR_ARROW);
				
				helper.loadTable(project,getTerminalHelper().getServer());

				MainShell.sShell.setCursor(cursorArrow);
			}
		});	
	}
	
	public void createServerCommandTabItem(boolean selectTabItem){
		commandComposite = new CommandComposite(cTabFolder, SWT.NONE);
		CTabItem commandCompositeTabItem = new CTabItem(cTabFolder, SWT.NONE);
		commandCompositeTabItem.setText(" Notes");
		commandCompositeTabItem.setImage(Constant.IMAGE_SERVER_COMMAND); 
		commandCompositeTabItem.setControl(commandComposite);   
		if(selectTabItem){
		   cTabFolder.setSelection(commandCompositeTabItem);
		}
	}

	/**
	 * @return the procedureTreeHelper
	 */
	public ProcedureViewHelper getHelper() {
		return helper;
	}
   
	public Tree getTreeProcedure(){
		return tree;
	}

	public void menuHidden(MenuEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void menuShown(MenuEvent e) {
	    //User user = MainShell.getMainHelper().getUser(); 

		MenuItem[] items = popupMenu.getItems();
        for (int i = 0; i < items.length; i++) {
           ((MenuItem) items[i]).dispose();
        }

        MenuItem refreshMItem = new MenuItem(popupMenu, SWT.NONE);
        refreshMItem.setText("Refresh");
        refreshMItem.setImage(Constant.IMAGE_REFRESH);
        
        refreshMItem.addListener(SWT.Selection, new Listener() {
    	  public void handleEvent(Event event) {
    		  helper.getTreeProcedure().removeAll();
    		  helper.getConsoleHelper().getServerHelper().loadTable(); 
    	  }
        });

        if(helper.getTreeProcedure().getItems().length>0){
			
        	if(helper.getConsoleHelper().getSSHInteractiveClient()!=null){ 
	            @SuppressWarnings("unused")
				MenuItem sep1MItem = new MenuItem(popupMenu, SWT.SEPARATOR);
	            
	            MenuItem runMItem = new MenuItem(popupMenu, SWT.NONE);
	            runMItem.setText("Run Procedure");
	            runMItem.setImage(Constant.IMAGE_RUN_CONSOLE);
	            
	            runMItem.addListener(SWT.Selection, new Listener() {
	        	  public void handleEvent(Event event) {
	        		  helper.getToolBarViewHelper().run();
	        	  }
	            });
        	}
        }
        @SuppressWarnings("unused")
		MenuItem sep1MItem = new MenuItem(popupMenu, SWT.SEPARATOR);
        
        MenuItem newMItem = new MenuItem(popupMenu, SWT.NONE);
        newMItem.setText("New Procedure");
        newMItem.setImage(Constant.IMAGE_COMMAND);
        
        newMItem.addListener(SWT.Selection, new Listener() {
    	  public void handleEvent(Event event) {
    		  //Server server = helper.getConsoleHelper().getServer();
    		  CommandByServerFactory.openDialogCreateCommand(null);
    	  }
        });        	
        
//	    if( (user==null)||(user.getIdUserGroup().equals(User.GROUP_USER))||(user.getIdUserGroup().equals(User.GROUP_EDITOR)) ){
//	    	newMItem.setEnabled(false);
//	    }
        
        if(helper.getTreeProcedure().getItems().length>0){
			
	        MenuItem editMItem = new MenuItem(popupMenu, SWT.NONE);
	        editMItem.setText("Edit Procedure");
	        editMItem.setImage(Constant.IMAGE_COMMAND_ITEM);
	        
	        editMItem.addListener(SWT.Selection, new Listener() {
	    	  public void handleEvent(Event event) {
	    		  if(helper.getTreeProcedure().getSelection().length>0){
	    			  if( helper.getTreeProcedure().getSelection()[0].getItems().length > 0){
	    				  helper.getToolBarViewHelper().openEditCommandDialog(false);
	    			  } else {
	    				  helper.getToolBarViewHelper().openEditCommandDialog(true);
	    			  }
	    		  }
	    	  }
	        });

	        MenuItem removeMItem = new MenuItem(popupMenu, SWT.NONE);
	        removeMItem.setText("Remove Procedure");
	        removeMItem.setImage(Constant.IMAGE_REMOVE_COMMAND);
	        
	        removeMItem.addListener(SWT.Selection, new Listener() {
	    	  public void handleEvent(Event event) {
	    		  helper.getToolBarViewHelper().removeServerCommand();
        		  helper.getTreeProcedure().removeAll();
        		  helper.getConsoleHelper().getServerHelper().loadTable(); 
	    	  }
	        });
 
//	        if( (user==null)||(user.getIdUserGroup().equals(User.GROUP_USER))||(user.getIdUserGroup().equals(User.GROUP_EDITOR)) ){
//	        	editMItem.setText("View Procedure");
//	        	removeMItem.setEnabled(false);
//		    }
	         
        }
	}

	/**
	 * @return the consoleHelper
	 */
	public TerminalHelper getTerminalHelper() {
		return viewHelper;
	}

	/**
	 * @param consoleHelper the consoleHelper to set
	 */
	public void setTerminalHelper(TerminalHelper viewHelper) {
		this.viewHelper = viewHelper;
	}

	/**
	 * @return the cTabFolder
	 */
	public CentralTabFolderView getCTabFolder() {
		return cTabFolder;
	}

	/**
	 * @return the project
	 */
	public Project getProject() {
		return project;
	}

	/**
	 * @param project the project to set
	 */
	public void setProject(Project project) {
		this.project = project;
	}
	


}  //  @jve:decl-index=0:visual-constraint="10,10"
