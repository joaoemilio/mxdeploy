package com.mxdeploy.api.service;

import java.util.List;
import java.util.UUID;

import com.mxdeploy.api.dao.CommandDAO;
import com.mxdeploy.api.dao.ServerCommandDAO;
import com.mxdeploy.api.domain.Command;
import com.mxdeploy.api.domain.Database;
import com.mxdeploy.api.domain.Project;
import com.mxdeploy.api.domain.Server;
import com.mxdeploy.api.domain.ServerCommand;

public class CommandService {

	public String createCommand(Project project, Command command) {
		CommandDAO dao = new CommandDAO();
		String idCommand = UUID.randomUUID().toString();
		command.setId(idCommand);
		dao.create(project, command); 
		return idCommand;
	}
	
	public void updateCommand(Project project, Command command) throws ServiceException {
		CommandDAO dao = new CommandDAO();
		dao.save(project, command);
	}

	public void addServerCommand(Project projectValue, ServerCommand serverCommand) {
		ServerCommandDAO dao = new ServerCommandDAO();
		dao.create(projectValue, serverCommand);
    	ProjectService pService = new ProjectService();
    	Project project = pService.obtemProject(projectValue.getId());
    	
    	for(Server server : project.getServers()){
    		if( serverCommand.getIdServer().equals(server.getId()) ){
    			server.addServerCommand(serverCommand);
    			break;
    		}
    	}
	}


	public List<Command> searchCommandByProjectAndServer(Server server, Project project) throws ServiceException {
		CommandDAO dao = new CommandDAO();
		return dao.searchCommandByProjectAndServer(server, project);
	}
	


	public void deleteCommand(Project project, Server server, Command command ) {
		ServerCommand serverCommand = getServerCommand(project,server, command );
		
		ServerCommandDAO serverCommandDAO = new ServerCommandDAO();
		serverCommandDAO.delete(project, serverCommand.getId());
		
		CommandDAO comandDAO = new CommandDAO();
		comandDAO.delete(project, command.getId());
	}
	
	public void deleteServerCommand(Project project, Server server, Command command ) {
		ServerCommand serverCommand = getServerCommand(project,server, command );
		ServerCommandDAO serverCommandDAO = new ServerCommandDAO();
		serverCommandDAO.delete(project, serverCommand.getId());		
	}
	
	public ServerCommand getServerCommand(Project projectValue, Server serverValue, Command commandValue){
		Database database = Database.getInstance();
		for(Project project : database.getProjects() ){
			if(project.getId().equals(projectValue.getId())){
			   for(Server server : project.getServers() ){
				   if( server.getHostname().equals(serverValue.getHostname())){
					   for(ServerCommand serverCommand : server.getServerCommandByProject(project,true)){
						   if(serverCommand.getIdCommand().equals(commandValue.getId())){
							   return serverCommand;
						   }
					   }
				   }
			   }
			}
		}
		return null;
	}

}
