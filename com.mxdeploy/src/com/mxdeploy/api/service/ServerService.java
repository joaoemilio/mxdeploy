package com.mxdeploy.api.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.mxdeploy.api.dao.ServerDAO;
import com.mxdeploy.api.domain.Database;
import com.mxdeploy.api.domain.Project;
import com.mxdeploy.api.domain.Server;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class ServerService {
	
	  //final String serverPath = Database.HOME+"/workspace/servers";
	
	  public boolean create(Server server) { 
		    ServerDAO dao = new ServerDAO();
			server.setId(UUID.randomUUID().toString());
			dao.create(server);
			
			return true;
	  }	
	  
	  public Server obtemServer(Server serverValue){
		  Database database = Database.getInstance();
		  for(Server server : database.getServerList()){
			  if(server.getHostname().equals(serverValue.getHostname())){
				 return server;
			  }
		  }
		  
		  //Sen�o achar o servidor, ent�o crie-o
		  create(serverValue);
		  return serverValue;
	  }
	  
	  public Server getServer(String hostname){
		  Database database = Database.getInstance();
		  for(Server server : database.getServerList()){
			  if(server.getHostname().equals(hostname)){
				 return server;
			  }
		  }
		  
		  return null;
	  }
	  
	  public List<Server> search(Server server) throws ServiceException {
		  ServerDAO dao = new ServerDAO();
		  return dao.searchMatching(server);
	  }

	  public List<Server> searchByProject(String idproject) throws ServiceException {
		  Database database = Database.getInstance();
		  List<Project> projects = database.getProjects();
		  for(Project project : projects){
			  if( project.getId().equals(idproject) ){
				  return project.getServers();
			  }
		  }
		  
		  return null;
	  }
	  
	  public void update(Server server) {
		  ServerDAO dao = new ServerDAO();
		  dao.save(server);
	  }
	  
	  public void removeServer(Server server) {
			File file = new File(Database.getServerPath()+"/"+server.getId()+".xml");
			file.delete();
	  }
	  
		public List<Server> loadServerXML() {
			List<Server> servers = new ArrayList<Server>();
			File file = new File(Database.getServerPath());

			File[] files = file.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					if (name.toLowerCase().endsWith(".xml")) {
						return true;
					}
					return false;
				}
			});

			if (files!= null && files.length > 0) {
				XStream xstream = new XStream(new DomDriver());
				for (int i = 0; i < files.length; i++) {
					try {
						// System.out.println(files[i].getName());
						Server server = (Server) xstream.fromXML(new FileReader(Database.getServerPath() + "/" + files[i].getName()));
						servers.add(server);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
			return servers;
		}

	  
}
