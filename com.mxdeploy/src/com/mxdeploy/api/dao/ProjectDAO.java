package com.mxdeploy.api.dao;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mxdeploy.api.domain.Database;
import com.mxdeploy.api.domain.Project;
import com.mxdeploy.api.domain.Server;
import com.mxdeploy.api.domain.ServerCommand;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;


public class ProjectDAO {
	
	private static List<Project> projectList = new ArrayList<Project>();
	
    public Project createValueObject() {
          return new Project();
    }

	public void createProjectDirectory(Project project){
		File file = new File(Database.getProjectPath()+"/"+project.getAlias());
		file.mkdir();
		file = new File(Database.getProjectPath()+"/"+project.getAlias()+"/commands");
		file.mkdir();
		file = new File(Database.getProjectPath()+"/"+project.getAlias()+"/serverCommands");
		file.mkdir();
		file = new File(Database.getProjectPath()+"/"+project.getAlias()+"/procedure");
		file.mkdir();
	}
	
    public synchronized void create(Project valueObject) {
    	createProjectDirectory(valueObject);
    	writeXML(valueObject);
		Database database = Database.getInstance();
		database.getProjects().add(valueObject);
    }

    public void save(Project valueObject) {
   	
    	Project project = getProject( valueObject.getId() );
 		project.setDescription(valueObject.getDescription());
    	project.setIsOpen(valueObject.isOpen());
    	project.setName(valueObject.getName());
    	
    	for( Server server : project.getServers() ){
    		 server.setNullToServerCommands();
    		 server.setProperty(null);
    	}
    			
    	writeXML(project);
    }

    public void createProjectServer(Project projectValue, Server serverValue) {
    	Project project = getProject(projectValue.getId());
    	project.addServer(serverValue);
    	writeXML(project);
    }
    
    public Project getProject(String valueObject) {
    	Database database = Database.getInstance();
    	Project result = null;
    	for(Project project : database.getProjects()){
    		if( valueObject.equals(project.getId()) ){
    			result = project;
    			break;
    		}
    	}
    	return result;
    }
    
    private void writeXML(Project valueObject ){
		XStream xstream = new XStream(new DomDriver());
		try {
			xstream.toXML(valueObject, new FileWriter(Database.getProjectPath()+"/"+valueObject.getAlias()+"/project.xml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public void delete(Project project){
    	File file = new File(Database.getProjectPath()+"/"+project.getAlias());
		
		File[] files = file.listFiles();
		
		if( files.length > 0 ){
			for(int i = 0; i < files.length ; i++){
				if(files[i].isDirectory()){
				   File[] files2 = files[i].listFiles();
				   for(int j = 0; j < files2.length ; j++){
					   files2[j].delete();
				   }
				   files[i].delete();
				} else {
				   files[i].delete();
				}
			}
		}   
		file.delete();
    }
    
    public void deleteDB(Connection conn, Project valueObject) 
          throws NotFoundException, SQLException {

          if (valueObject.getId() == null) {
               //System.out.println("Can not delete without Primary-Key!");
               throw new NotFoundException("Can not delete without Primary-Key!");
          }

          String sql = "DELETE FROM TB_PROJECT WHERE (ID = ? ) ";
          PreparedStatement stmt = null;

          try {
              stmt = conn.prepareStatement(sql);
              stmt.setString(1, valueObject.getId()); 

              int rowcount = databaseUpdate(conn, stmt);
              if (rowcount == 0) {
                   //System.out.println("Object could not be deleted (PrimaryKey not found)");
                   throw new NotFoundException("Object could not be deleted! (PrimaryKey not found)");
              }
              if (rowcount > 1) {
                   //System.out.println("PrimaryKey Error when updating DB! (Many objects were deleted!)");
                   throw new SQLException("PrimaryKey Error when updating DB! (Many objects were deleted!)");
              }
          } finally {
              if (stmt != null)
                  stmt.close();
          }
    }


    public List<Project> searchMatching(Project valueObject) {

    	Database database = Database.getInstance();
    	List<Project> allProjects = database.getProjects();
    	
        List<Project> searchResults = new ArrayList<Project>();
        
      	for(Project project : allProjects){
      		boolean addThis = false;
      		if(valueObject!=null){
      		  if (valueObject.getId() != null) {
    		     if(project.getId().equals(valueObject.getId()))
    		    	addThis = true;
    	         else
     	        	addThis = false;   
      		  }
      		  
    		  if (valueObject.getName() != null) {
    	         if( project.getName().startsWith(valueObject.getName()) )
    	        	addThis = true;
    	         else
    	        	addThis = false; 
    		  }
    		  
	          if (valueObject.getAlias() != null) {
        		 if(project.getAlias().startsWith(valueObject.getAlias()))
      	        	addThis = true;
     	         else
     	        	addThis = false; 
	          }
	          
	          if (valueObject.isOpen() != null) {
	             if(project.isOpen().equals(valueObject.isOpen()))
      	        	addThis = true;
     	         else
     	        	addThis = false;		            	
		      }
	          
      		} else {
      			addThis = true;
      		}
	        
      		if(addThis)
     		   searchResults.add(project);
        }

        return searchResults;
  }
    

    public List<Project> searchMatchingDB(Connection conn, Project valueObject) throws SQLException {

          List<Project> searchResults;

          boolean first = true;
          StringBuffer sql = new StringBuffer("SELECT * FROM TB_PROJECT WHERE 1=1 ");

          if(valueObject!=null){
	          if (valueObject.getId() != null) {
	              if (first) { first = false; }
	              sql.append("AND ID = '").append(valueObject.getId()).append("' ");
	          }
	
	          if (valueObject.getName() != null) {
	              if (first) { first = false; }
	              sql.append("AND NAME LIKE '%").append(valueObject.getName()).append("%' ");
	          }
	
	          if (valueObject.getAlias() != null) {
	              if (first) { first = false; }
	              sql.append("AND ALIAS LIKE '%").append(valueObject.getAlias()).append("%' ");
	          }
	
	          if (valueObject.isOpen() != null) {
	              if (first) { first = false; }
	              sql.append("AND IS_OPEN = ").append(valueObject.isOpen()).append(" ");
	          }
          }
//          if (valueObject.getDescription() != null) {
//              if (first) { first = false; }
//              sql.append("AND DESCRIPTION LIKE '").append(valueObject.getDescription()).append("%' ");
//          }


          sql.append("ORDER BY ALIAS ASC "); 

          // Prevent accidential full table results.
          // Use loadAll if all rows must be returned.
//          if (first)
//               searchResults = new ArrayList<Project>();
//          else
          searchResults = listQuery(conn, conn.prepareStatement(sql.toString()));

          return searchResults;
    }

    public List<Project> searchProjectByServer(String hostname){
    	Database database = Database.getInstance();
    	List<Project> searchResults=new ArrayList<Project>();
    	for(Project project : database.getProjects() ){
    		for(Server server : project.getServers()){
    			if(server.getHostname().equals(hostname)){
    				searchResults.add(project);
    				break;
    			}
    		}
    	}
    	return searchResults;    	
    }
    
    public List<Project> searchProjectByServerDB(Connection conn, String hostname) throws SQLException {

        List<Project> searchResults;
        String sql = " SELECT P.ID, P.NAME, P.ALIAS, P.DESCRIPTION, P.DOC_LINK, P.IS_OPEN "+
        			 " FROM TB_PROJECT P   "+
		        	 "     ,TB_PROJECT_SERVER LEFT JOIN TB_SERVER ON (TB_PROJECT_SERVER.ID_SERVER = TB_SERVER.ID) "+
		        	 " WHERE P.ID = TB_PROJECT_SERVER.ID_PROJECT   "+
		        	 " AND   TB_SERVER.HOSTNAME LIKE '%"+hostname+"%'   "+
		        	 " GROUP BY P.ID, P.NAME, P.ALIAS, P.DESCRIPTION, P.DOC_LINK, P.IS_OPEN "+
		        	 " ORDER BY P.ALIAS ASC ";
        searchResults = listQuery(conn, conn.prepareStatement(sql.toString()));

        return searchResults;
    }
    
    protected int databaseUpdate(Connection conn, PreparedStatement stmt) throws SQLException {

          int result = stmt.executeUpdate();

          return result;
    }


    protected List<Project> listQuery(Connection conn, PreparedStatement stmt) throws SQLException {

          ArrayList<Project> searchResults = new ArrayList<Project>();
          ResultSet result = null;

          try {
              result = stmt.executeQuery();

              while (result.next()) {
                   Project temp = createValueObject();

                   temp.setId(result.getString("ID")); 
                   temp.setName(result.getString("NAME")); 
                   temp.setAlias(result.getString("ALIAS")); 
                   temp.setDescription(result.getString("DESCRIPTION")); 
                   temp.setDocLink(result.getString("DOC_LINK"));
                   temp.setIsOpen((Integer)result.getObject("IS_OPEN"));

                   searchResults.add(temp);
              }

          } finally {
              if (result != null)
                  result.close();
              if (stmt != null)
                  stmt.close();
          }

          return searchResults;
    }
    
   
    public Project obtemProjectAndServerByCommand(Project projectValue, String idCommand) {
    	Project projectResult = new Project();
    	projectResult.setId(projectValue.getId());
    	projectResult.setAlias(projectValue.getAlias());
    	projectResult.setDescription(projectValue.getDescription());
    	projectResult.setIsOpen(projectValue.isOpen());
    	projectResult.setName(projectValue.getName());
    	
    	List<Server> serverList=new ArrayList<Server>();
    	
		for(Server server : projectValue.getServers()){
			List<ServerCommand> serverCommands = server.getServerCommandByProject(projectValue,true);
			if(serverCommands!=null && !serverCommands.isEmpty()){
    			for(ServerCommand serverCommand : serverCommands ){
    				if(serverCommand.getIdCommand().equals(idCommand)){
    				   serverList.add(server);
    				   break;
    				}
    			}
		    }
		}
		
		if(!serverList.isEmpty())
		for(Server server : serverList){
			projectResult.addServer(server);
		}
    	return projectResult;      	
    }
    
    public List<Project> obtemProjectAndServerByCommandDB(Connection conn, String idCommand) throws SQLException {
  	  String sql =   " SELECT TB_PROJECT.ID PROJECT_ID, TB_PROJECT.ALIAS " +
  	  		         "       ,TB_PROJECT.NAME PROJECT_NAME, TB_PROJECT.DESCRIPTION " +
  	  		         "       ,TB_PROJECT.DOC_LINK, TB_PROJECT.IS_OPEN "+
                     "       ,TB_SERVER.ID SERVER_ID, TB_SERVER.NAME SERVER_NAME, TB_SERVER.HOSTNAME " +
                     "       ,TB_SERVER.PORT "+
                     " FROM TB_SERVER_COMMAND, "+
                     " TB_PROJECT, "+ 
                     " TB_SERVER "+ 
                     " WHERE TB_SERVER_COMMAND.ID_PROJECT = TB_PROJECT.ID "+
                     "   AND TB_SERVER_COMMAND.ID_SERVER  = TB_SERVER.ID "+
                     "   AND TB_SERVER_COMMAND.ID_COMMAND = ? ";	  
	  

  	      PreparedStatement stmt;
  		  stmt = conn.prepareStatement(sql);
          stmt.setString(1, idCommand);

          List<Project> searchResults = new ArrayList<Project>();
          ResultSet result = null;
          
  	    try {
              result = stmt.executeQuery();
              String projectIdNew = null;  
              Project projectCurrent = null;
              while (result.next()) {
              	 projectIdNew = result.getString("PROJECT_ID");
             		 if( (projectCurrent!=null)&&(projectCurrent.getId().equals(projectIdNew)) ){
                         Server server = new Server();
                         server.setId(result.getString("SERVER_ID"));
                         server.setName((String)result.getObject("SERVER_NAME"));
                         server.setHostname((String)result.getObject("HOSTNAME"));
                         server.setPort((Integer)result.getObject("PORT"));
                         projectCurrent.addServer(server);
             		 } else {
             			 projectCurrent = new Project();
                     	 
             			 projectCurrent.setId(projectIdNew); 
             			 projectCurrent.setName(result.getString("PROJECT_NAME")); 
             			 projectCurrent.setAlias(result.getString("ALIAS"));
             			 projectCurrent.setDescription(result.getString("DESCRIPTION"));
             			 projectCurrent.setDocLink(result.getString("DOC_LINK"));
             			 projectCurrent.setIsOpen((Integer)result.getObject("IS_OPEN"));
             			 
                     	 String idItem = result.getString("SERVER_ID");
                     	 if(idItem!= null){
                           Server server = new Server();
                           server.setId(idItem);
                           server.setName(result.getString("SERVER_NAME"));
                           server.setHostname(result.getString("HOSTNAME"));
                           server.setPort((Integer)result.getObject("PORT"));
                           projectCurrent.addServer(server);
                     	 }
                     	 searchResults.add(projectCurrent);
             		 }
             }
          } finally {
              if (result != null)
                  result.close();
              if (stmt != null)
                  stmt.close();
          }
          return searchResults;
    }
    
    public List<Project> obtemProjectAndServer(Project project) {
    	return searchMatching(project);
    }
    
    public List<Project> obtemProjectAndServerDB(Connection conn, Project project) throws SQLException {
    	  String sql =   " SELECT TB_PROJECT.ID PROJECT_ID, TB_PROJECT.ALIAS" +
    	  		         "       ,TB_PROJECT.NAME PROJECT_NAME, TB_PROJECT.DESCRIPTION" +
    	  		         "       ,TB_PROJECT.DOC_LINK , TB_PROJECT.IS_OPEN  "+
                         "       ,TB_SERVER.ID SERVER_ID, TB_SERVER.NAME SERVER_NAME, TB_SERVER.HOSTNAME" +
                         "       ,TB_SERVER.PORT "+
                         " FROM TB_PROJECT_SERVER, "+
                         "      TB_PROJECT, "+ 
                         "      TB_SERVER "+ 
                         " WHERE TB_PROJECT_SERVER.ID_PROJECT = TB_PROJECT.ID "+
                         "   AND TB_PROJECT_SERVER.ID_SERVER  = TB_SERVER.ID ";
    	  
    	    StringBuffer sqlBuffer = new StringBuffer(sql);
    	    if(project!=null){
	    	    if(project.getAlias()!=null){
	    	    	sqlBuffer.append("AND TB_PROJECT.ALIAS LIKE '").append(project.getAlias()).append("%' ");
	    	    } else if (project.getName()!=null){
	    	    	sqlBuffer.append("AND TB_PROJECT.NAME LIKE '").append(project.getName()).append("%' ");
	    	    }
	    	    
	    	    if(project.isOpen()!=null){ 
	    	    	sqlBuffer.append("AND TB_PROJECT.IS_OPEN = "+project.isOpen());
	    	    }
	    	    if(project.getId()!=null){
	    	    	sqlBuffer.append("AND TB_PROJECT.ID = '").append(project.getId().trim()).append("' ");
	    	    }
    	    }
    	    sqlBuffer.append(" ORDER BY TB_PROJECT.NAME ASC ");
    	    
    	    PreparedStatement stmt; 
    		stmt = conn.prepareStatement(sqlBuffer.toString());
    		//stmt.setObject(1, project.getIdWorkingSet(), Types.INTEGER);
    		
            List<Project> searchResults = new ArrayList<Project>();
            ResultSet result = null;
             
    	    try {
                result = stmt.executeQuery();
                String projectIdNew = null; 
                Project projectCurrent = null;
                while (result.next()) {
                	 projectIdNew = result.getString("PROJECT_ID");
               		 if( (projectCurrent!=null)&&(projectCurrent.getId().equals(projectIdNew)) ){
                           Server server = new Server();
                           server.setId(result.getString("SERVER_ID"));
                           server.setName((String)result.getObject("SERVER_NAME"));
                           server.setHostname((String)result.getObject("HOSTNAME"));
                           server.setPort((Integer)result.getObject("PORT"));
                           projectCurrent.addServer(server);
               		 } else {
               			 projectCurrent = new Project();
                       	 
               			 projectCurrent.setId(projectIdNew); 
               			 projectCurrent.setName(result.getString("PROJECT_NAME")); 
               			 projectCurrent.setAlias(result.getString("ALIAS"));
               			 projectCurrent.setDescription(result.getString("DESCRIPTION"));
               			 projectCurrent.setDocLink(result.getString("DOC_LINK"));
               			 projectCurrent.setIsOpen((Integer)result.getObject("IS_OPEN"));
               			 
                       	 String idItem = result.getString("SERVER_ID");
                       	 if(idItem!= null){
                             Server server = new Server();
                             server.setId(result.getString("SERVER_ID"));
                             server.setName((String)result.getObject("SERVER_NAME"));
                             server.setHostname((String)result.getObject("HOSTNAME"));
                             server.setPort((Integer)result.getObject("PORT"));
                             projectCurrent.addServer(server);
                       	 }
                       	 searchResults.add(projectCurrent);
               		 }
               }
            } finally {
                if (result != null)
                    result.close();
                if (stmt != null)
                    stmt.close();
            }
            return searchResults;
      }

    
}

