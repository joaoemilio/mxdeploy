package com.mxdeploy.api.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mxdeploy.api.domain.Database;
import com.mxdeploy.api.domain.Project;
import com.mxdeploy.api.domain.ProjectServer;
import com.mxdeploy.api.domain.Server;

public class ProjectServerDAO {

    public ProjectServer createValueObject() {
          return new ProjectServer();
    }

    public void deleteDB(Connection conn, ProjectServer valueObject) 
          throws NotFoundException, SQLException {

          if (valueObject.getIdProject() == null) {
               throw new NotFoundException("Can not delete without Primary-Key!");
          }

          String sql = "DELETE FROM TB_PROJECT_SERVER WHERE (ID_PROJECT = ? AND ID_SERVER = ? ) ";
          PreparedStatement stmt = null;

          try {
              stmt = conn.prepareStatement(sql);
              stmt.setString(1, valueObject.getIdProject()); 
              stmt.setString(2, valueObject.getIdServer());

              int rowcount = databaseUpdateDB(conn, stmt);
              if (rowcount == 0) {
                   throw new NotFoundException("Object could not be deleted! (PrimaryKey not found)");
              }
          } finally {
              if (stmt != null)
                  stmt.close();
          }
    }

    public void delete(ProjectServer valueObject) { 
         Database database = Database.getInstance();
         List<Project> projects = database.getProjects();
         Server removeServer = null;
         for(Project project : projects){
        	 if(project.getId().equals(valueObject.getIdProject())){
        		for(Server server : project.getServers() ){
        			if(server.getId().equals(valueObject.getIdServer())){
        				removeServer = server;
        				break;
        			}
        		}
        		project.getServers().remove(removeServer);
                ProjectDAO dao = new ProjectDAO();
                dao.save(project);
                break;
        	 }
         }
    }
    
    public void deleteByProject(String idProject) {
        Database database = Database.getInstance();
        List<Project> projects = database.getProjects();

        for(Project project : projects){
       	 if(project.getId().equals(idProject)){
       		for(Server server : project.getServers() ){
           		project.getServers().remove(server);
       		}
            ProjectDAO dao = new ProjectDAO();
            dao.save(project);
            break;
       	 }
        }    	
    }
    
    public void deleteByProjectDB(Connection conn, String idProject)   throws NotFoundException, SQLException {
	    if (idProject == null) {
	         throw new NotFoundException("Can not delete without Primary-Key!");
	    }
	
	    String sql = "DELETE FROM TB_PROJECT_SERVER WHERE (ID_PROJECT = ? ) ";
	    PreparedStatement stmt = null;
	
	    try {
	        stmt = conn.prepareStatement(sql);
	        stmt.setString(1, idProject); 
	
	        //int rowcount = databaseUpdate(conn, stmt);
	        databaseUpdateDB(conn, stmt);
	    } finally {
	        if (stmt != null)
	            stmt.close();
	    }
    }

    protected int databaseUpdateDB(Connection conn, PreparedStatement stmt) throws SQLException {

          int result = stmt.executeUpdate();

          return result;
    }

    protected void singleQueryDB(Connection conn, PreparedStatement stmt, ProjectServer valueObject) 
          throws NotFoundException, SQLException {

          ResultSet result = null;

          try {
              result = stmt.executeQuery();

              if (result.next()) {

                   valueObject.setIdProject(result.getString("ID_PROJECT")); 
                   valueObject.setIdServer(result.getString("ID_SERVER")); 

              } else {
                    throw new NotFoundException("ProjectServer Object Not Found!");
              }
          } finally {
              if (result != null)
                  result.close();
              if (stmt != null)
                  stmt.close();
          }
    }

    protected List<ProjectServer> listQueryDB(Connection conn, PreparedStatement stmt) throws SQLException {

          ArrayList<ProjectServer> searchResults = new ArrayList<ProjectServer>();
          ResultSet result = null;

          try {
              result = stmt.executeQuery();

              while (result.next()) {
                   ProjectServer temp = createValueObject();

                   temp.setIdProject(result.getString("ID_PROJECT")); 
                   temp.setIdServer(result.getString("ID_SERVER")); 

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
    
  
	
}
