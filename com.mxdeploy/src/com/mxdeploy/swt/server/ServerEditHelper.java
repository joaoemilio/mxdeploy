package com.mxdeploy.swt.server;

import java.util.List;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;

import com.mxdeploy.api.domain.Project;
import com.mxdeploy.api.domain.ProjectServer;
import com.mxdeploy.api.domain.Server;
import com.mxdeploy.api.service.ProjectService;
import com.mxdeploy.api.service.ServerService;
import com.mxdeploy.api.service.ServiceException;
import com.mxdeploy.swt.MainShell;
import com.mxdeploy.swt.explorer.event.AddServerTreeItemEventHandler;
import com.mxdeploy.swt.explorer.helper.ControlPanelHelper;

public class ServerEditHelper {
	//static Logger logger = Logger.getLogger(ServerEditHelper.class);
	
	private ServerComposite composite = null;

	private String idServer = null;
	
	private TreeItem serverTreeItem = null;
	
	private Server server = null;
	
	private Project project = null;
	
	public ServerEditHelper(ServerComposite composite) {
		this.composite = composite;
	}

	protected void loadFields(Server serverOfItem){
		ServerService serverService = new ServerService();
		Server server = serverService.obtemServer(serverOfItem);
		
		idServer = server.getId();
		
		if( (server.getHostname()!=null)&&(server.getHostname().trim().length()>0) ){
			composite.hostnameText.setText(server.getHostname());
			composite.hostnameText.setEditable(false);
		}
		
		if( (server.getName()!=null)&&(server.getName().trim().length()>0) ){
			composite.nameServerText.setText(server.getName());
		}
		
		if( (server.getAddress()!=null)&&(server.getAddress().trim().length()>0) ){
			composite.addressText.setText(server.getAddress());
		}	
		        
        //composite.setSocks(server.getDefaultSocksUUID());
	}
	
	protected void close(){
		Shell shell = (Shell)composite.getParent();
		shell.close();
		shell.dispose();
	}
	
	protected boolean save(){
		this.server = new Server();
		ServerService service = new ServerService();
		
		if( (composite.hostnameText.getText()!=null)&&(composite.hostnameText.getText().trim().length()>0) ){
			this.server.setHostname(composite.hostnameText.getText());
		} else {
			//MainShell.sendMessage("Hostname is mandatory !");
			composite.getErrorLabel().setText("        Hostname is mandatory !");
			composite.hostnameText.setFocus();
			return false;
		}
		 
		if(idServer==null){
			try { 
				List<Server> list = service.search(this.server);
				if(!list.isEmpty()){
					//MainShell.sendMessage("Hostname already exist !");
					composite.getErrorLabel().setText("        Hostname already exist !");
					composite.hostnameText.setFocus();
					return false;
				}
			} catch (ServiceException e1) {
				e1.printStackTrace();
			}
		}
		
		if( (composite.nameServerText.getText()!=null)&&(composite.nameServerText.getText().trim().length()>0) ){
			this.server.setName(composite.nameServerText.getText());
		} else {
			composite.getErrorLabel().setText("        Name is mandatory !");
			//MainShell.sendMessage("Name is mandatory !");
			return false;
		}

		Integer port = 22;
		this.server.setPort( port );
		this.server.setType( 0 );
				
		if(composite.addressText.getText()!=null && composite.addressText.getText().trim().length()>0){
		   this.server.setAddress(composite.addressText.getText());
		}

		ProjectService projectService = new ProjectService();
		
		if(idServer!=null){
		   this.server.setId(idServer);	
		   service.update(this.server);
		   //projectService.update(project);
		} else { 

			    service.create(this.server); 
				

				ProjectServer projectServer = new ProjectServer();
				projectServer.setIdServer(this.server.getId());
				projectServer.setIdProject(project.getId());
				
				projectService.createProjectServer(project,this.server);

				TreeItem treeItem = MainShell.getControlPanelHelper().getTreeItemProjectSelected(MainShell.getControlPanelHelper().getMyProjectTreeItemSelected());
				AddServerTreeItemEventHandler.execute(treeItem, server);

		}
		return true;
		
	}

	/**
	 * @return the serverTreeItem
	 */
	public TreeItem getServerTreeItem() {
		return serverTreeItem;
	}

	/**
	 * @param serverTreeItem the serverTreeItem to set
	 */
	public void setServerTreeItem(TreeItem serverTreeItem) {
		this.serverTreeItem = serverTreeItem;
	}
	
	public void refreshServerTreeItem(){
		
		TreeItem[] itemProjects = getWorkingSetHelper().getMyProjectTree().getItems();
		for (int i=0; i < itemProjects.length ; i++ ) {
			 if( itemProjects[i].getData() instanceof Project ){
				 TreeItem[] itemServers = itemProjects[i].getItems();
				 
				 for (int j=0; j < itemServers.length ; j++ ) {
					 if( itemServers[j].getData() instanceof Server ){
						 Server server = (Server)itemServers[j].getData();
						 if( server.getHostname().equals(this.server.getHostname()) ){
							 itemServers[j].setText(composite.nameServerText.getText());
							 itemServers[j].setData(this.server);
						 }
					 }
				 }
			 }
		}
		
//		if(getServerTreeItem()!=null){
//		   getServerTreeItem().setText(composite.nmText.getText());
//		   getServerTreeItem().setData(this.server);
//		}
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

	/**
	 * @return the workingSetHelper
	 */
	public ControlPanelHelper getWorkingSetHelper() {
		return MainShell.getControlPanelHelper();
	}

}
