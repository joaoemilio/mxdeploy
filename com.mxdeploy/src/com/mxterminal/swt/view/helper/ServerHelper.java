package com.mxterminal.swt.view.helper;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
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
import com.mxterminal.swt.view.ViewComposite;
import com.mxterminal.swt.view.CentralComposite;

public class ServerHelper {
	//static Logger logger = Logger.getLogger(ServerHelper.class);
	
	private CentralComposite serverComposite = null;
	//private WebSphere websphere;
	
	public ServerHelper (CentralComposite serverComposite){
		this.serverComposite = serverComposite;
	} 

	public CentralComposite getServerComposite() {
		return serverComposite;
	}
	
	public TerminalHelper getTerminalHelper(){ 
		return serverComposite.serverSashFormComposite.getServerSashFormHelper().getConsoleComposite().getConsoleHelper();
	}
	
	public ProcedureViewHelper getViewHelper(){
		return serverComposite.serverSashFormComposite.getServerSashFormHelper().getViewComposite().getHelper();
	}

	public ViewComposite getProcedureTreeComposite(){
		return serverComposite.serverSashFormComposite.getServerSashFormHelper().getViewComposite();
	}
	
	public SashForm getSashFormExplorerServer(){
		return (SashForm)serverComposite.getParent().getParent();
	}
	
	public int getTreeItemCount(){
		return getViewHelper().getTreeProcedure().getItemCount();
	}
	
	public void loadTable(){
		Server server = getTerminalHelper().getServer();
		Project project = getTerminalHelper().getProject();
		
		CommandService service =  new CommandService();
		
		List<Command> list;
		try {
			list = service.searchCommandByProjectAndServer(server,project);

			if( (list!=null)&&(!list.isEmpty()) ){
				Command commandSelected = null;
				TreeItem treeItemSelected = null;
				if(getViewHelper().getTreeProcedure().getItemCount()>0){
					TreeItem item = getViewHelper().getTreeProcedure().getSelection()[0];
					if(item.getData() instanceof CommandItem){
						commandSelected = (Command)item.getParent().getData();
					} else {
						commandSelected = (Command)item.getData();
					}
				}
				getViewHelper().getTreeProcedure().removeAll();
				TreeItem treeItemCommand = null;
				for(Command command : list ) {
					treeItemCommand = getViewHelper().addTreeItem(null,command.getName());
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
						 TreeItem item2 = getViewHelper().addTreeItem(treeItemCommand,itemComando.getCommandSSH());
						 item2.setData(itemComando);
					  }
					}
				}
				
				if(treeItemSelected!=null){
				   //treeItemSelected.setExpanded(true);
				   getViewHelper().getTreeProcedure().setSelection(treeItemSelected);
				} else {
				   getViewHelper().getTreeProcedure().setSelection(getViewHelper().getTreeProcedure().getItem(0));
				}
			} else {
				getViewHelper().getTreeProcedure().removeAll();
			}
		} catch (ServiceException e) {
			//logger.error(e.getMessage(),e);
			MainShell.sendMessage(Constant.MSN_SEE_LOG_ERROR, SWT.OK | SWT.ICON_ERROR);
			e.printStackTrace();
		}
		
	}
	
	public void addCommand(Command commandValue){
		TreeItem treeItemCommand = getViewHelper().addTreeItem(null,commandValue.getName());
		treeItemCommand.setData(commandValue);
		if(commandValue.getRuntime()!=null){
		   treeItemCommand.setText(1, commandValue.getRuntime());
		}

		List<CommandItem> itemList = commandValue.getCommandItens();
		if(itemList!=null && !itemList.isEmpty()){
		  for(CommandItem itemComando : itemList ) {
			 TreeItem item = getViewHelper().addTreeItem(treeItemCommand,itemComando.getCommandSSH());
			 item.setData(itemComando);
		  }
		}
	}
	
	public Command getCommandSelected(TreeItem treeItem){
		if (treeItem.getData() instanceof Command){
			return (Command)treeItem.getData();
		} else {
			return getCommandSelected(treeItem.getParentItem());
		}
	}	
	
	public TreeItem getTreeItemSelected(){
		Tree tree = getViewHelper().getTreeProcedure();
		return tree.getSelection()[0];
	}

	/**
	 * @return the websphere
	 */
//	public WebSphere getWebsphere() {
//		return websphere;
//	}

	/**
	 * @param websphere the websphere to set
	 */
//	public void setWebsphere(WebSphere websphere) {
//		this.websphere = websphere;
//	}
	

//	public BeanShellViewHelper getBeanShellViewHelper(){
//		return this.serverComposite.beanShellSashFormComposite.getViewComposite().getHelper();
//	}
//	
//	public void reloadBeanShell(){
//		getBeanShellViewHelper().loadBeanShellTable();
//	}
	
	
}
