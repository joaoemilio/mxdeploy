package com.mxdeploy.swt.command;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.mxdeploy.api.domain.Command;
import com.mxdeploy.api.domain.Project;
import com.mxdeploy.api.domain.Server;
import com.mxdeploy.api.domain.ServerCommand;
import com.mxdeploy.api.service.CommandService;
import com.mxdeploy.api.service.ProjectService;
import com.mxdeploy.api.service.ServiceException;
import com.mxdeploy.images.Constant;
import com.mxdeploy.swt.MainShell;

public class CopyCommandProjectServerHelper {
	//static Logger logger = Logger.getLogger(CopyCommandProjectServerHelper.class);
	
	public CopyCommandProjectServerHelper() {
		// TODO Auto-generated constructor stub
	}

	private CopyCommandProjectServerComposite composite = null;
	
	protected CommandByServerHelper commandByServerHelper = null;
	
	private Command command = null;
	private List<Project> listProject = null;
	
	private String[] itens = {"ALIAS","NAME"};
	
	private final int COLUMN_ALIAS = 0;
	private final int COLUMN_NAME = 1;	
	
	public CopyCommandProjectServerHelper(CopyCommandProjectServerComposite composite){
		this.composite = composite;
	}
	
	public void loadComboBox(){
		composite.columnSearchCCombo.setItems(itens);
		composite.columnSearchCCombo.select(0);
	}	

	protected void close(){
		Shell shell = (Shell)composite.getParent();
		shell.close();
		shell.dispose();
	}

	public Command getCommand(){
		return command;
	}

	public void setCommand(Command command) {
		this.command = command;
	}	
	
	
	/**
	 * @param listProject the listProject to set
	 */
	public void setListProject(List<Project> listProject) {
		this.listProject = listProject;
	}

	public void addTreeItemSelectedTree(Project project){
		TreeItem treeItemProject = new TreeItem(composite.serverSelectedTree,SWT.NONE);
		treeItemProject.setText(project.getName()+" ["+project.getAlias()+"]");
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
	
	public void addTreeItemResultTree(Project project){
		TreeItem treeItemProject = new TreeItem(composite.resultServerTree,SWT.NONE);
		treeItemProject.setText(project.getName()+" ["+project.getAlias()+"]");
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

	protected void search(){
			 int index = composite.columnSearchCCombo.getSelectionIndex();
			 ProjectService service = new ProjectService();
			 
			 Project objectValue =  new Project();
		  
			 if ((composite.nameSearchText.getText() != null) && (composite.nameSearchText.getText().trim().length() > 0)) {
				if (!composite.nameSearchText.getText().trim().equals("*")) {
					switch (index) {
					case COLUMN_NAME:
						objectValue.setName(composite.nameSearchText.getText());
						break;
					default:
						objectValue.setAlias(composite.nameSearchText.getText());
						break;
					}
				}
			  }		     
		  
			  try {
			     List<Project> list = service.obtemProjectAndServer(objectValue);
		         if( (list!=null)&&(!list.isEmpty()) ){ 
			        for(Project project : list){
			        	addTreeItemResultTree(project);
			        }
			        composite.resultServerTree.setSelection(composite.resultServerTree.getItem(0));
			      }				
			  } catch (ServiceException e) {
				//logger.error(e.getMessage(),e);
				MainShell.sendMessage(Constant.MSN_SEE_LOG_ERROR, SWT.OK | SWT.ICON_ERROR);
				e.printStackTrace();
			  } 

	}
	
	protected void addItemServerSelectedTable(){
		TreeItem treeItemResult = composite.resultServerTree.getSelection()[0];
		TreeItem[] treeItemSelect = composite.serverSelectedTree.getItems();
		boolean existServidor = false;
		boolean existProjeto = false;
		Project projectResult = null;
		
		if( treeItemResult.getData() instanceof Project ){
			projectResult = (Project)treeItemResult.getData();
			for(int i=0;treeItemSelect.length>i;i++){
				if( treeItemSelect[i].getData() instanceof Project ){
					Project projSelect = (Project)treeItemSelect[i].getData();
					if(projSelect.getId().equals(projectResult.getId())){
						existProjeto=true;
						TreeItem[] treeItemServerResult = treeItemResult.getItems();
						TreeItem[] treeItemServerSelect = treeItemSelect[i].getItems();
						for(int j=0;treeItemServerResult.length>j;j++){
							Server serverResult = (Server)treeItemServerResult[j].getData();
							Server serverSelect = null;
							for(int k=0;treeItemServerSelect.length>k;k++){
								serverSelect = (Server)treeItemServerSelect[k].getData();
								if(serverResult.getId().equals(serverSelect.getId())){
									existServidor=true;
									break;
								} else {
									existServidor=false;
								}
							}
							if(!existServidor){
								addTreeItemServer(treeItemSelect[i],serverResult);
							}
						}
						break;
					} else {
						existProjeto = false;
					}
				}
			}
			if(!existProjeto){
				TreeItem treeItemProject = addTreeItemProject(composite.serverSelectedTree,projectResult);

				TreeItem[] treeItemServerResult = treeItemResult.getItems();
				for(int l=0;treeItemServerResult.length>l;l++){
					Server serverResult = (Server)treeItemServerResult[l].getData();
					addTreeItemServer(treeItemProject,serverResult);
				}
				
			}
		} else if(treeItemResult.getData() instanceof Server){
			Server serverResult = (Server)treeItemResult.getData();
			projectResult = (Project)treeItemResult.getParentItem().getData();
			TreeItem projectSelectTreeItem = thereIsProjectInTreeSelected(projectResult);
			if(projectSelectTreeItem==null){
				projectSelectTreeItem = addTreeItemProject(composite.serverSelectedTree,projectResult);
				addTreeItemServer(projectSelectTreeItem,serverResult);
			} else {
				TreeItem[] treeItemServerSelect = projectSelectTreeItem.getItems();
				Server serverSelect = null;
				for(int m=0;treeItemServerSelect.length>m;m++){
					serverSelect = (Server)treeItemServerSelect[m].getData();
					if(serverResult.getId().equals(serverSelect.getId())){
						existServidor=true;
						break;
					} else {
						existServidor=false;
					}
				}
				if(!existServidor){
					addTreeItemServer(projectSelectTreeItem,serverResult);
				}
			}
						
		}
		
		if( composite.serverSelectedTree.getItemCount()>0){
			composite.leftButton.setEnabled(true );
		}
		
	}
	
	protected TreeItem thereIsProjectInTreeSelected(Project projectResult){
		TreeItem[] treeItemSelect = composite.serverSelectedTree.getItems();
		for(int i=0;treeItemSelect.length>i;i++){
			if( treeItemSelect[i].getData() instanceof Project ){
				Project projSelect = (Project)treeItemSelect[i].getData();
				if(projSelect.getId().equals(projectResult.getId())){
					return treeItemSelect[i];
				}
			}
		}
		return null;
	}
	
	protected TreeItem addTreeItemProject(Tree tree,Project project){
		TreeItem treeItem = new TreeItem(tree,SWT.NONE);
		treeItem.setText(project.getName());
		treeItem.setImage(Constant.IMAGE_APPLICATION);
		treeItem.setData(project); 
		return treeItem;
	}

	protected TreeItem addTreeItemServer(TreeItem treeItemProject,Server server){
		TreeItem treeItem = new TreeItem(treeItemProject,SWT.NONE);
		treeItem.setText(server.getName());
		treeItem.setImage(Constant.IMAGE_SERVER);
		treeItem.setData(server); 
		return treeItem;
	}
	
	public void remove(){
		TreeItem treeItemSelect = composite.serverSelectedTree.getSelection()[0];
		
		if(treeItemSelect.getData() instanceof Project){
			TreeItem[] treeItemServers = treeItemSelect.getItems();
			for (int i=0; treeItemServers.length>i; i++){
				if(!treeItemServers[i].isDisposed()){
				   treeItemServers[i].dispose();
				}
			}
			treeItemSelect.dispose();
		} else {
			TreeItem[] treeItemServers = treeItemSelect.getParentItem().getItems();
			Server server1 = (Server)treeItemSelect.getData();
			for (int i=0; treeItemServers.length>i; i++){
				if(!treeItemServers[i].isDisposed()){
					Server server2 = (Server)treeItemServers[i].getData();
					if(server1.getId().equals(server2.getId())){
						treeItemServers[i].dispose();	
					}
				}
			}
		}
		
		if( composite.serverSelectedTree.getItemCount()<=0){
			composite.leftButton.setEnabled(false);
		}
		
	}
	
	public void save(){
		TreeItem[] treeItemSelect = composite.serverSelectedTree.getItems();

        CommandService service = new CommandService();
        Command commandToCopy = new Command();
        commandToCopy.setName(command.getName());
        commandToCopy.setDescription(command.getDescription());
		if(treeItemSelect.length>0){
		    for (int i = 0; treeItemSelect.length > i; i++) {
		    	if(treeItemSelect[i].getData() instanceof Project){
		    		Project projectSelect = (Project)treeItemSelect[i].getData();
		    		
					  TreeItem[] treeItemServer = treeItemSelect[i].getItems();
					  for(int j=0;treeItemServer.length>j;j++){
						  Server serverSelect = (Server)treeItemServer[j].getData();
						  ServerCommand serverCommand =  new ServerCommand();
						  serverCommand.setIdProject(projectSelect.getId());
						  serverCommand.setIdServer(serverSelect.getId());
						  serverCommand.setIdCommand(command.getId());

						  service.addServerCommand(projectSelect, serverCommand);
					  }

		    	}
			}
		}
		
		
	}
	
}
