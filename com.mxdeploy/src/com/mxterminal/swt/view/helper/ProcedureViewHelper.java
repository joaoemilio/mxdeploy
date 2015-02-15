package com.mxterminal.swt.view.helper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.mxdeploy.api.domain.Command;
import com.mxdeploy.api.domain.CommandItem;
import com.mxdeploy.api.domain.Project;
import com.mxdeploy.api.domain.Server;
import com.mxdeploy.api.service.CommandService;
import com.mxdeploy.api.service.ServiceException;
import com.mxdeploy.images.Constant;
import com.mxdeploy.swt.MainShell;
import com.mxterminal.swt.view.CommandComposite;
import com.mxterminal.swt.view.ViewComposite;
import com.mxterminal.swt.view.CentralSashFormComposite;

public class ProcedureViewHelper {
	//static Logger logger = Logger.getLogger(ProcedureTreeHelper.class);
	
	private Display display = Display.getDefault();
	private ViewComposite composite = null;
	private TextLayout textLayout = new TextLayout(display);
	
	public ProcedureViewHelper(ViewComposite procedureTreeComposite){
		this.composite = procedureTreeComposite;
	}

	public ToolBarViewHelper getToolBarViewHelper(){
		//ProcedureSashFormComposite procedureSashFormComposite = (ProcedureSashFormComposite)procedureTreeComposite.getParent().getParent();
		//return procedureSashFormComposite.getProcedureSashFormHelper().getConsoleComposite().getConsoleHelper().getToolBarConsoleComposite().getToolBarConsoleHelper();
		return composite.cTabFolder.getToolBarViewComposite().getToolBarViewHelper();
	}
	
	public TerminalHelper getConsoleHelper(){
		CentralSashFormComposite procedureSashFormComposite = (CentralSashFormComposite)composite.getParent().getParent();		
		return procedureSashFormComposite.getServerSashFormHelper().getConsoleComposite().getConsoleHelper();
	}
	
	public TreeItem addTreeItem(TreeItem itemPai, String descricao){
		TreeItem item = null;
		Tree tree = getTreeProcedure();
		if (itemPai==null){
		   item = new TreeItem(tree, SWT.SINGLE);
		   item.setImage(Constant.IMAGE_PROCEDURES);
		} else  {
		   item = new TreeItem(itemPai, SWT.SINGLE);
		   item.setImage(Constant.IMAGE_COMMAND_ITEM);
		}
		item.setText(0, descricao);
		return item;
	}
	
	public Tree getTreeProcedure(){
		return composite.getTreeProcedure();
	}
	
	public void selectTreeItem(final Event event){
		final Display display = Display.getDefault();
		display.syncExec(new Runnable(){
			public void run(){ 
			    if ( getConsoleHelper().getSSHInteractiveClient().isAuthenticated() ){ 
			    	composite.cTabFolder.getRunToolItem().setEnabled(true);
			    } else {
			    	composite.cTabFolder.getRunToolItem().setEnabled(false);	
			    }
			    Tree tree = getTreeProcedure();
			    TreeItem[] treeItens = tree.getItems();
			    List<TreeItem> list = Arrays.asList(treeItens);
		    	backOriginalColor(list);
			    //tree.getSelection()[0].setBackground(0,display.getSystemColor(SWT.COLOR_BLUE));
			    //tree.getSelection()[0].setFont(new Font(Display.getDefault(), "Tahoma", 8, SWT.BOLD));
			    Command comand = null;
			    if(tree.getSelection().length>0 && tree.getSelection()[0]!=null){
			    	if(tree.getSelection()[0].getData() instanceof CommandItem){
			    		comand = (Command)tree.getSelection()[0].getParentItem().getData();
//			    		if(comand.getDescription()!=null){
//			    		   String description = comand.getDescription().replace("\r\n", " ");
//				    	   MainShell.getMainHelper().setTextLabelFooter(description,false);
//			    		} else {
//			    		   MainShell.getMainHelper().setTextLabelFooter(" ",false);
//			    		}
			    	} else {
			    		comand = (Command)tree.getSelection()[0].getData();
//			    		if(comand.getDescription()!=null){
//			    		   String description = comand.getDescription().replace("\r\n", " ");
//				    	   MainShell.getMainHelper().setTextLabelFooter(description,false);
//			    		} else {
//			    			MainShell.getMainHelper().setTextLabelFooter(" ",false);
//			    		}
			    	}
			    }
			}
		});
	}
	
	public void backOriginalColor(List<TreeItem> list){
		//Display display = Display.getDefault();		
	    for(TreeItem item : list){
//	    	item.setBackground(0,display.getSystemColor(SWT.COLOR_WHITE));
	    	//item.setFont(new Font(Display.getDefault(), "Tahoma", 8, SWT.NONE));
//	    	item.setForeground(0,display.getSystemColor(SWT.COLOR_BLACK));

	    	if(item.getItemCount()>0){
		    	TreeItem[] treeItens = item.getItems();	    	
		    	List<TreeItem> list2 = Arrays.asList(treeItens);
		    	backOriginalColor(list2);
	    	}
	    }
	}
	
	public void setTitle(String title){
		//procedureTreeComposite.label.setText(title);
		//procedureTreeComposite.procedureTabItem.setText(title);
	}
	
	public void loadTable(Project project, Server server){
		CommandService service =  new CommandService();
		
		List<Command> list;
		try {
			list = service.searchCommandByProjectAndServer(server,project);

			if( (list!=null)&&(!list.isEmpty()) ){ 
				Command commandSelected = null;
				TreeItem treeItemSelected = null;
				if(composite.tree.getItemCount()>0){
					TreeItem item = composite.tree.getSelection()[0];
					if(item.getData() instanceof CommandItem){
						commandSelected = (Command)item.getParent().getData();
					} else {
						commandSelected = (Command)item.getData();
					}
				}
				composite.tree.removeAll();
				Collections.sort(list, new Command()); 

				TreeItem treeItemCommand = null;
				for(Command command : list ) {
					treeItemCommand = addTreeItem(null,command.getName());
					treeItemCommand.setData(command);
					if(command.getRuntime()!=null){
					   treeItemCommand.setText(1, command.getRuntime());
					}
					if(commandSelected!=null && commandSelected.getId().equals(command.getId())){
						treeItemSelected = treeItemCommand;
					}
					List<CommandItem> itemList = command.getCommandItens();
					if(itemList!=null && !itemList.isEmpty()){
					  for(CommandItem itemComando : itemList ) {
						 TreeItem item2 = addTreeItem(treeItemCommand,itemComando.getCommandSSH());
						 item2.setData(itemComando);
					  }
					}
				}
				
				if(treeItemSelected!=null){
				   //treeItemSelected.setExpanded(true);
					composite.tree.setSelection(treeItemSelected);
				} else {
					composite.tree.setSelection(composite.tree.getItem(0));
				}
			} else {
				composite.tree.removeAll();
			}
		} catch (ServiceException e) {
			//logger.error(e.getMessage(),e);
			MainShell.sendMessage(Constant.MSN_SEE_LOG_ERROR, SWT.OK | SWT.ICON_ERROR);
			e.printStackTrace();
		}
		
	}
	
	public void getProcedureView(){
		CTabItem[] item = composite.cTabFolder.getItems();
		for(int i=0;item.length>i;i++){
			if( item[i].getControl() instanceof Tree){
				composite.cTabFolder.setSelection(item[i]);
				return;
			}
		}
		composite.createProcedureTabItem(true);
	}
	
	public void getServerCommandView(){
		CTabItem[] item = composite.cTabFolder.getItems();
		for(int i=0;item.length>i;i++){
			if( item[i].getControl() instanceof CommandComposite){
				composite.cTabFolder.setSelection(item[i]);
				return;
			}
		}
		composite.createServerCommandTabItem(true);
		if(composite.cTabFolder.getItemCount()<=1){
		   composite.cTabFolder.reloadServerCommand();
		}
	}	

	
	public Project getProject(){
		return composite.project;
	}
	

}
