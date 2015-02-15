package com.mxdeploy.swt.command;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;

import com.mxdeploy.api.domain.Command;
import com.mxdeploy.api.domain.CommandItem;
import com.mxdeploy.api.domain.Project;
import com.mxdeploy.api.domain.Server;
import com.mxdeploy.api.domain.ServerCommand;
import com.mxdeploy.api.service.CommandService;
import com.mxdeploy.api.service.ProjectService;
import com.mxdeploy.api.service.ServiceException;
import com.mxdeploy.images.Constant;
import com.mxdeploy.swt.MainShell;
import com.mxterminal.swt.view.helper.ServerHelper;

public class CommandByServerHelper {
	//static Logger logger = Logger.getLogger(CommandByServerHelper.class);
	
	private CommandByServerComposite composite = null;
	
	private ServerHelper serverHelper = null;	
	
	public static final int STATUS_CREATE = 1;
	public static final int STATUS_UPDATE = 2;

	private int status = STATUS_CREATE;
	private String idCommand = null;
	
	protected Command commandEdit = null;
	
	public void setStatusUpdate(String idCommand){
		status = STATUS_UPDATE;
		this.idCommand = idCommand;
	}
	
	public CommandByServerHelper(CommandByServerComposite composite){
		this.composite = composite;
 	    serverHelper = MainShell.getCTopTabFolder().getServerHelper();
	}
	
	public void addNewTreeItemProjectServer(){
		TreeItem treeItemProject = new TreeItem(composite.projectServerTree,SWT.NONE);
		treeItemProject.setText(serverHelper.getTerminalHelper().getProject().getAlias());
		treeItemProject.setImage(Constant.IMAGE_APPLICATION);
		treeItemProject.setData(serverHelper.getTerminalHelper().getProject());
		
		TreeItem treeItemServer = new TreeItem(treeItemProject,SWT.NONE);
		treeItemServer.setText(serverHelper.getTerminalHelper().getServer().getName());
		treeItemServer.setImage(Constant.IMAGE_SERVER);
		treeItemServer.setData(serverHelper.getTerminalHelper().getServer());
		
		treeItemProject.setExpanded(true);
	}

	public void addTreeItemProjectServer(Project project){
		TreeItem treeItemProject = new TreeItem(composite.projectServerTree,SWT.NONE);
		treeItemProject.setText(project.getAlias());
		treeItemProject.setImage(Constant.IMAGE_APPLICATION);
		treeItemProject.setData(project);
		
		List<Server> listServer = project.getServers();
		if(listServer !=null && !listServer.isEmpty()){
			for(Server server : listServer){
				TreeItem treeItemServer = new TreeItem(treeItemProject,SWT.NONE);
				treeItemServer.setText(server.getName());
				treeItemServer.setImage(Constant.IMAGE_SERVER);
				treeItemServer.setData(server); 
			}
		}
		
		treeItemProject.setExpanded(true);
	}
	
	protected boolean save(){
		Command command = new Command();
		
		if( (composite.nmText.getText()!=null)&&(composite.nmText.getText().length()>0) ){
		  command.setName(composite.nmText.getText());
		} else {
			MainShell.sendMessage("Name is mandatory !");
			return false;
		}
		 
		if( (composite.descritionTextArea.getText()!=null)&&(composite.descritionTextArea.getText().length()>0) ){
		  command.setDescription(composite.descritionTextArea.getText());
		}
		
		CommandService service =  new CommandService();
		switch (status) {
		case STATUS_CREATE:{

			    Server  server  = serverHelper.getTerminalHelper().getServer();
				Project project = serverHelper.getTerminalHelper().getProject();
				
				command.cleanCommandItemList();
				List<TableItem> listCommandItem = getTableItems();
				if(listCommandItem!=null && !listCommandItem.isEmpty() ){
				  for(TableItem tableItem : listCommandItem){
					CommandItem cmdItem = new CommandItem();
					cmdItem.setIdCommand(command.getId());
					cmdItem.setCommandSSH(tableItem.getText());
					command.addCommandItem(cmdItem);
				  }
				}
				
				String idCommand = service.createCommand(project,command);
				command.setId(idCommand);
				
				ServerCommand serverCommand = new ServerCommand();
				serverCommand.setIdCommand(command.getId());
				serverCommand.setIdServer(server.getId());
				serverCommand.setIdProject(project.getId());
				
				service.addServerCommand(project, serverCommand);
				break;
		}
		case STATUS_UPDATE:
			try {
				Project project = serverHelper.getTerminalHelper().getProject();
				
				command.setId(idCommand);
				command.setRuntime(null);
				command.cleanCommandItemList();
				List<TableItem> listCommandItem = getTableItems();
				for(TableItem tableItem : listCommandItem){
					CommandItem cmdItem = new CommandItem();
					cmdItem.setIdCommand(command.getId());
					cmdItem.setCommandSSH(tableItem.getText());
					command.addCommandItem(cmdItem);
				}
				service.updateCommand(project, command);
				//service.deleteCommandItemByIdCommand(command);
				
				break;
			} catch (ServiceException e) {
				//logger.error(e.getMessage(),e);
				MainShell.sendMessage(Constant.MSN_SEE_LOG_ERROR, SWT.OK | SWT.ICON_ERROR);
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	public void setEnableServerButton(boolean enabled){
		composite.addServerButton.setEnabled(enabled);
		composite.removeServerButton.setEnabled(enabled);
	}
	
	public void openAddCommandItemDialog(){
		CommandByServerFactory.openAddCommandItemDialog(this);
	}

	public void openAddCommandProjectServerDialog(){
		Project project = serverHelper.getTerminalHelper().getProject();
		CommandByServerFactory.openAddCommandProjectServerDialog(project.getId(), this);
	}
	
	protected void addCommandItemInTable(String text){
		TableItem tableItem = new TableItem(composite.commandItemTable,SWT.NONE);
		tableItem.setText(text);
		tableItem.setImage(Constant.IMAGE_COMMAND_ITEM);
	}
	
	protected List<TableItem> getTableItems(){
		TableItem[] tableItem = composite.commandItemTable.getItems();
		List<TableItem> listTableItem = null;
		if(tableItem.length>0){
			listTableItem = new ArrayList<TableItem>();
			for(int i=0;tableItem.length>i;i++){ 
				listTableItem.add(tableItem[i]);
			}
		}
		return listTableItem;
	}
	
	protected void refreshServerCommandTable(){
		serverHelper.loadTable();
	}
	
	protected void close(){
		Shell shell = (Shell)composite.getParent();
		shell.close();
		shell.dispose();
	}
	
	public void setCommand(Command command){
		this.commandEdit = command;
		if(command.getDescription()!=null){
		   composite.descritionTextArea.setText(command.getDescription());
		}
		composite.nmText.setText(command.getName());
	}
	
	public void removeAllCommandItemInTable(){
		composite.commandItemTable.removeAll();
	}
	
	public void loadTreeItemProjectServer(String idProject, Command command){
    	ProjectService projectService = new ProjectService();		
    	Project projectResult = projectService.obtemProjectAndServerByCommand(idProject, command.getId());
    	List<Project> listProject = new ArrayList<Project>();
    	listProject.add(projectResult);
    	
    	if(listProject!=null && !listProject.isEmpty()){
     	   for(Project project : listProject){
     		   addTreeItemProjectServer(project);
     	   }
     	} 
	}
	
	public void removeTreeItemProjectServer(){
		composite.projectServerTree.removeAll();
	}
	
	public void removeProjectServer(){
		TreeItem itemSelected = composite.projectServerTree.getSelection()[0];
        CommandService service = new CommandService();
        
		if(itemSelected.getData() instanceof Project){
		   Project projectSelected = (Project)itemSelected.getData();
		   TreeItem[] treeItemServer = itemSelected.getItems();

		   for(int i=0;treeItemServer.length>i;i++){ 
			   Server serverSelected = (Server)treeItemServer[i].getData();
		       service.deleteServerCommand(projectSelected,serverSelected,commandEdit);
		   }
   		   itemSelected.dispose();
		} else if(itemSelected.getData() instanceof Server){
			Project projectSelected = (Project)itemSelected.getParentItem().getData();
			Server serverSelected = (Server)itemSelected.getData();
			
		    service.deleteServerCommand(projectSelected,serverSelected,commandEdit);
		    
			//itemSelected.dispose();
			
			removeTreeItemProjectServer(); 

			loadTreeItemProjectServer(projectSelected.getId(), commandEdit);
		}
	}
	
    public boolean confirm(String commandName){
    	MessageBox messageBox = new MessageBox(MainShell.sShell, SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
        messageBox.setMessage(commandName);
        messageBox.setText("Confirm");
        if (messageBox.open() == SWT.CANCEL)
        {
          return false; 
        }
        return true;
    }
    
	public void deleteCommand(Project project, Server server){
		CommandService service = new CommandService();
		service.deleteCommand(project, server, commandEdit);
	}
    
	public void addCommandItemInTable(List<String> list){
		for(String command : list){
			addCommandItemInTable(command);
		}
	}

	/**
	 * @return the serverHelper
	 */
	public ServerHelper getServerHelper() {
		return serverHelper;
	}
	
}
