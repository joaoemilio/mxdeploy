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

public class AddCommandProjectServerHelper {
	//static Logger logger = Logger.getLogger(AddCommandProjectServerHelper.class);
	
	private AddCommandprojectServerComposite2 composite = null;
	
	protected CommandByServerHelper commandByServerHelper = null;
	
	private Command command = null;
	private Project project = null;
	
	private List<Project> listProject = null;
	
	private String[] itens = {"ALIAS","NAME"};
	
	private final int COLUMN_ALIAS = 0;
	private final int COLUMN_NAME = 1;	 
	private final int COLUMN_SERVER_NAME = 2;
	
	public AddCommandProjectServerHelper(AddCommandprojectServerComposite2 composite){
		this.composite = composite;
	}
	
	public void setCommandByServerHelper(CommandByServerHelper commandByServerHelper) {
		this.commandByServerHelper = commandByServerHelper;
	}
	
//	public void loadComboBox(){
//		composite.columnSearchCCombo.setItems(itens);
//		composite.columnSearchCCombo.select(0);
//	}	

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

	protected void search(String idProject){
		//int index = composite.columnSearchCCombo.getSelectionIndex();
		ProjectService service = new ProjectService();

		Project objectValue = new Project();

		if(idProject!=null) {
		   //composite.nameSearchText.setText(idProject);
		   objectValue.setId(idProject);
//		} else	if ((composite.nameSearchText.getText() != null)
//				&& (composite.nameSearchText.getText().trim().length() > 0)) {
//			if (!composite.nameSearchText.getText().trim().equals("*")) {
//				switch (index) {
//				case COLUMN_ALIAS:
//					objectValue.setAlias(composite.nameSearchText.getText());					
//					break;
//				case COLUMN_NAME:{
//					objectValue.setName(composite.nameSearchText.getText());
//					break;
//				}
//				default:
//					objectValue.setName(composite.nameSearchText.getText());
//					break;
//				}
//			}
		}

		List<Project> list;
		try {
			list = service.obtemProjectAndServer(objectValue);
			if ((list != null) && (!list.isEmpty())) {
				for (Project project : list) {
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
		if(composite.serverSelectedTree.getSelection().length==0)
			return;
		
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
		boolean existServidor = false;
		boolean existProjeto = false;
        CommandService service = new CommandService();
        
		if(treeItemSelect.length>0){
		    for (int i = 0; treeItemSelect.length > i; i++) {
		    	if(treeItemSelect[i].getData() instanceof Project){
		    		Project projectSelect = (Project)treeItemSelect[i].getData();
		    		if(listProject!=null && !listProject.isEmpty()){
		    		   for(Project project : listProject ){
		    			   
		    			  if( project.getId().equals(projectSelect.getId()) ) {
			    			  existProjeto=true;
		    				  TreeItem[] treeItemServer = treeItemSelect[i].getItems();
		    				  List<Server> listServer = project.getServers(); 
		    				  for(int j=0;treeItemServer.length>j;j++){
		    					  Server serverSelect = (Server)treeItemServer[j].getData();
		    					  if(listServer!=null && !listServer.isEmpty()){
		    						  for(Server server : listServer){
		    							  if( server.getId().equals(serverSelect.getId()) ){
		    								  existServidor=true;
		    								  break;
		    							  } else {
		    								  existServidor=false;
		    							  }
		    						  }
		    						  if(!existServidor){
		    							  ServerCommand serverCommand =  new ServerCommand();
		    							  serverCommand.setIdProject(project.getId());
		    							  serverCommand.setIdServer(serverSelect.getId());
		    							  serverCommand.setIdCommand(command.getId());
										  
		    							  service.addServerCommand(project,serverCommand);
		    						  }
		    					  }
		    				  }
		    				  
		    				  existServidor=false;
	    					  if(listServer!=null && !listServer.isEmpty()){		    				  
		    				     for(Server server : listServer){
		    						  for(int m=0;treeItemServer.length>m;m++){
				    					  Server serverSelect = (Server)treeItemServer[m].getData();		    							  
		    							  if( server.getId().equals(serverSelect.getId()) ){
		    								  existServidor=true;
		    								  break;
		    							  } else {
		    								  existServidor=false;
		    							  }
		    						  }
		    						  if(!existServidor){
										 service.deleteServerCommand(project,server,command);
		    						  }
		    					  }
		    				  }
		    				  
		    				  break; 
		    				  
		    			  } else {
		    				  existProjeto=false;
		    			  }
		    			  
		    		   }
		    		}
		    		
	    		    if(!existProjeto){
	    			   
	    			  TreeItem[] treeItemServer = treeItemSelect[i].getItems(); 
    				  for(int k=0;treeItemServer.length>k;k++){
    					  Server serverSelect = (Server)treeItemServer[k].getData();
						  ServerCommand serverCommand =  new ServerCommand();
						  serverCommand.setIdProject(projectSelect.getId());
						  serverCommand.setIdServer(serverSelect.getId());
						  serverCommand.setIdCommand(command.getId());
						  
						  service.addServerCommand(projectSelect,serverCommand);
    				  }
	    		    }

		    	}
			}
		}
		
		if (listProject!=null && !listProject.isEmpty()){ 
			for(Project project : listProject ){
			    for (int l = 0; treeItemSelect.length > l; l++) {
		    		if(treeItemSelect[l].isDisposed()){
		    		   existProjeto=false;
		    		} else if(treeItemSelect[l].getData() instanceof Project){
			    	   Project projectSelect = (Project)treeItemSelect[l].getData();
			    	   if( projectSelect.getId().equals(project.getId()) ){
				    	   existProjeto=true;
			    	       break;
			    	   } else {
			    		   existProjeto=false;
			    	   }
		    		}
			    }
			    if(!existProjeto){
			    	List<Server> listServer = project.getServers();
			    	for(Server server : listServer){
						service.deleteServerCommand(project,server,command);
			    	}
			    }
			}
		}
		
		
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
	
}
