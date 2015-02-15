package com.mxdeploy.api.dao;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mxdeploy.api.domain.Command;
import com.mxdeploy.api.domain.CommandItem;
import com.mxdeploy.api.domain.Database;
import com.mxdeploy.api.domain.Project;
import com.mxdeploy.api.domain.Server;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class CommandDAO {

    public Command createValueObject() {
          return new Command();
    }

    private void writeXML(Project project, Command command ){
		XStream xstream = new XStream(new DomDriver());
		try {
			xstream.toXML(command, new FileWriter(Database.getProjectPath()+"/"+project.getAlias()+"/commands/"+command.getId()+".xml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public synchronized void create(Project project, Command command) {
    	writeXML(project, command);
    }
    
    public synchronized void createDB(Connection conn, Command valueObject) throws SQLException {

          String sql = "";
          PreparedStatement stmt = null;
          ResultSet result = null;
          
          try {
               sql = " INSERT INTO TB_COMMAND ( ID, NAME, DESCRIPTION, RUNTIME) "+
                     " VALUES ( ?, ?, ?, ?) ";
               stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

               stmt.setString(1, valueObject.getId()); 
               stmt.setString(2, valueObject.getName()); 
               stmt.setString(3, valueObject.getDescription()); 
               stmt.setString(4, valueObject.getRuntime());

               int rowcount = stmt.executeUpdate();
               if (rowcount != 1) {
                    throw new SQLException("PrimaryKey Error when updating DB!");
               }
               
               result = stmt.getGeneratedKeys();  
               result.next(); 
               String str = String.valueOf(result.getLong(1));
               valueObject.setId(str);  
             

          } finally {
              if (stmt != null)
                  stmt.close();
          }


    }


    public void save(Project project, Command valueObject){
    	writeXML(project, valueObject);
    }
    
    public void saveDB(Connection conn, Command valueObject) 
          throws NotFoundException, SQLException {

          String sql = "UPDATE TB_COMMAND SET NAME = ?, DESCRIPTION = ?, RUNTIME = ? WHERE (ID = ? ) ";
          PreparedStatement stmt = null;

          try {
              stmt = conn.prepareStatement(sql);
              stmt.setString(1, valueObject.getName()); 
              stmt.setString(2, valueObject.getDescription()); 

              stmt.setString(3, valueObject.getRuntime());
              stmt.setString(4, valueObject.getId());              

              int rowcount = databaseUpdate(conn, stmt);
              if (rowcount == 0) {
                   throw new NotFoundException("Object could not be saved! (PrimaryKey not found)");
              }
              if (rowcount > 1) {
                   throw new SQLException("PrimaryKey Error when updating DB! (Many objects were affected!)");
              }
          } finally {
              if (stmt != null)
                  stmt.close();
          }
    }


    public void delete(Project project, String IdCommand){
    	File file = new File(Database.getProjectPath()+"/"+project.getAlias()+"/commands/"+IdCommand+".xml");
    	file.delete();
    }
    
    public void deleteDB(Connection conn, Command command) 
          throws NotFoundException, SQLException {

          if (command.getId() == null) {
               throw new NotFoundException("Can not delete without Primary-Key!");
          }

          String sql = "DELETE FROM TB_COMMAND WHERE (ID = ? ) ";
          PreparedStatement stmt = null;

          try {
              stmt = conn.prepareStatement(sql);
              stmt.setString(1, command.getId()); 

              int rowcount = databaseUpdate(conn, stmt);
              if (rowcount == 0) {
                   throw new NotFoundException("Object could not be deleted! (PrimaryKey not found)");
              }
          } finally {
              if (stmt != null)
                  stmt.close();
          }
    }

    protected int databaseUpdate(Connection conn, PreparedStatement stmt) throws SQLException {

          int result = stmt.executeUpdate();

          return result;
    }

    public List<Command> searchCommandByProjectAndServer(Server serverValue, Project projectValue) {
    	Database database = Database.getInstance();
    	
    	List<Project> projects = database.getProjects();
    	for(Project project : projects){
    		if( project.getId().equals(projectValue.getId()) ){
    			for( Server server : project.getServers() ){
    				 if(server.getId().equals(serverValue.getId())){
    					return server.getCommandByProjectAndServer(project);
    				 }
    			}
    		}
    	}
    	return null;
    }
    
    public List<Command> searchAllByServerDB(Connection conn, Server server, Project project) throws SQLException {

        String sql =  " SELECT TB_COMMAND.ID"+ 
        			  "    , TB_COMMAND.NAME ,TB_COMMAND.DESCRIPTION, TB_COMMAND.RUNTIME "+
        			  "    , TB_COMMAND_ITEM.ID AS ID_ITEM, TB_COMMAND_ITEM.ID_COMMAND, TB_COMMAND_ITEM.COMMAND_SSH "+ 
        			  " FROM  TB_SERVER_COMMAND "+
        			  "    , TB_COMMAND LEFT OUTER JOIN TB_COMMAND_ITEM  "+
        			  "      ON TB_COMMAND.ID = TB_COMMAND_ITEM.ID_COMMAND "+
        			  " WHERE TB_COMMAND.ID = TB_SERVER_COMMAND.ID_COMMAND "+
        			  " AND TB_SERVER_COMMAND.ID_SERVER = ? "+
        			  " AND TB_SERVER_COMMAND.ID_PROJECT = ? "+
        			  " ORDER BY TB_COMMAND.NAME, TB_COMMAND_ITEM.ID ";         
        
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, server.getId());
        stmt.setString(2, project.getId());        
        
        List<Command> searchResults = new ArrayList<Command>();
        ResultSet result = null;

        try {
            result = stmt.executeQuery();
            String commandIdNew = null; 
            Command commandCurrent = null;
            while (result.next()) {
            	 commandIdNew = result.getString("ID");
            		 if( (commandCurrent!=null)&&(commandCurrent.getId().equals(commandIdNew)) ){
                        CommandItem commandItem = new CommandItem();
                        commandItem.setId((Integer)result.getObject("ID_ITEM"));
                        commandItem.setIdCommand(result.getString("ID_COMMAND"));
                        commandItem.setCommandSSH((String)result.getObject("COMMAND_SSH"));
                        commandCurrent.addCommandItem(commandItem);
            		 } else {
            			 commandCurrent = createValueObject();
                    	 
            			 commandCurrent.setId(commandIdNew); 
                    	 commandCurrent.setName(result.getString("NAME")); 
                    	 commandCurrent.setDescription(result.getString("DESCRIPTION"));
                    	 commandCurrent.setRuntime(result.getString("RUNTIME"));

                    	 Integer idItem = (Integer)result.getObject("ID_ITEM");
                    	 if(idItem!= null){
	                         CommandItem commandItem = new CommandItem();
	                         commandItem.setId(idItem);
	                         commandItem.setIdCommand(result.getString("ID_COMMAND"));
	                         commandItem.setCommandSSH(result.getString("COMMAND_SSH"));
	                         commandCurrent.addCommandItem(commandItem);	                         
                    	 }
                         
                    	 searchResults.add(commandCurrent);
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
