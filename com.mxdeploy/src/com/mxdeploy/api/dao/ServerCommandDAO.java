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
import java.util.UUID;

import com.mxdeploy.api.domain.Database;
import com.mxdeploy.api.domain.Project;
import com.mxdeploy.api.domain.ServerCommand;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class ServerCommandDAO {

    public ServerCommand createValueObject() {
          return new ServerCommand();
    } 

    public synchronized void create(Project project, ServerCommand valueObject) {
    	valueObject.setId(UUID.randomUUID().toString());
    	writeXML(project,valueObject);
    }
    
    private void writeXML(Project project, ServerCommand serverCommand ){
		XStream xstream = new XStream(new DomDriver());
		try {
			xstream.toXML(serverCommand, new FileWriter(Database.getProjectPath()+"/"+project.getAlias()+"/serverCommands/"+serverCommand.getId()+".xml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }    
    
    public synchronized void createDB(Connection conn, ServerCommand valueObject) throws SQLException {

          String sql = "";
          PreparedStatement stmt = null;

          try {
               sql = "INSERT INTO TB_SERVER_COMMAND ( ID_SERVER, ID_COMMAND, ID_PROJECT) VALUES (?, ?, ?) ";
               stmt = conn.prepareStatement(sql);

               stmt.setString(1, valueObject.getIdServer()); 
               stmt.setString(2, valueObject.getIdCommand()); 
               stmt.setString(3, valueObject.getIdProject());

               int rowcount = databaseUpdateDB(conn, stmt);
               if (rowcount != 1) {
                    //System.out.println("PrimaryKey Error when updating DB!");
                    throw new SQLException("PrimaryKey Error when updating DB!");
               }

          } finally {
              if (stmt != null)
                  stmt.close();
          }
    }

    public synchronized void createByFileDB(Connection conn, String valueObject) throws SQLException {

        String sql = "";
        PreparedStatement stmt = null;
        //ResultSet result = null;

        try {
             sql = "INSERT INTO TB_SERVER_COMMAND ( ID_SERVER, ID_PROJECT, ID_COMMAND ) VALUES ("+valueObject+") ";
             stmt = conn.prepareStatement(sql);
             System.out.println(sql);
             int rowcount = databaseUpdateDB(conn, stmt);
             if (rowcount != 1) {
                  throw new SQLException("PrimaryKey Error when updating DB!");
             }
        } finally {
            if (stmt != null)
                stmt.close();
        }


  }    

    public void saveDB(Connection conn, ServerCommand valueObject) 
          throws NotFoundException, SQLException {

          String sql = "UPDATE TB_SERVER_COMMAND SET ID_COMMAND = ? WHERE (ID_SERVER = ? AND ID_PROJECT = ?) ";
          PreparedStatement stmt = null;

          try {
              stmt = conn.prepareStatement(sql);
              stmt.setString(1, valueObject.getIdCommand()); 

              stmt.setString(2, valueObject.getIdServer()); 
              stmt.setString(3, valueObject.getIdProject()); 

              int rowcount = databaseUpdateDB(conn, stmt);
              if (rowcount == 0) {
                   //System.out.println("Object could not be saved! (PrimaryKey not found)");
                   throw new NotFoundException("Object could not be saved! (PrimaryKey not found)");
              }
              if (rowcount > 1) {
                   //System.out.println("PrimaryKey Error when updating DB! (Many objects were affected!)");
                   throw new SQLException("PrimaryKey Error when updating DB! (Many objects were affected!)");
              }
          } finally {
              if (stmt != null)
                  stmt.close();
          }
    }

    public void delete(Project project, String idServerCommand){
    	File file = new File(Database.getProjectPath()+"/"+project.getAlias()+"/serverCommands/"+idServerCommand+".xml");
    	file.delete();
    }
    
    public void deleteDB(Connection conn, ServerCommand valueObject) 
          throws NotFoundException, SQLException {

          if (valueObject.getIdServer() == null || valueObject.getIdProject() == null || valueObject.getIdCommand() == null) {
               throw new NotFoundException("Can not delete without Primary-Key!");
          }

          String sql = "DELETE FROM TB_SERVER_COMMAND WHERE (ID_SERVER = ? AND ID_PROJECT = ? AND ID_COMMAND = ?) ";
          PreparedStatement stmt = null;

          try {
              stmt = conn.prepareStatement(sql);
              stmt.setString(1, valueObject.getIdServer()); 
              stmt.setString(2, valueObject.getIdProject());
              stmt.setString(3, valueObject.getIdCommand());

              int rowcount = databaseUpdateDB(conn, stmt);
              if (rowcount == 0) {
                   //System.out.println("Object could not be deleted (PrimaryKey not found)");
                   throw new NotFoundException("Object could not be deleted! (PrimaryKey not found)");
              }
          } finally {
              if (stmt != null)
                  stmt.close();
          }
    }

    public void deleteServerCommandByServerDB(Connection conn, String idServer) 
          throws NotFoundException, SQLException {

          if (idServer == null ) {
               throw new NotFoundException("Can not delete without Primary-Key!");
          }

          String sql = "DELETE FROM TB_SERVER_COMMAND WHERE ( ID_SERVER = ? ) ";
          PreparedStatement stmt = null;

          try {
              stmt = conn.prepareStatement(sql);
              stmt.setString(1, idServer); 
              
              databaseUpdateDB(conn, stmt);
          } finally {
              if (stmt != null)
                  stmt.close();
          }
    }
 
    public void deleteAllDB(Connection conn) throws SQLException {

          String sql = "DELETE FROM TB_SERVER_COMMAND";
          PreparedStatement stmt = null;

          try {
              stmt = conn.prepareStatement(sql);
              //int rowcount = 
              databaseUpdateDB(conn, stmt);
          } finally {
              if (stmt != null)
                  stmt.close();
          }
    }

    public List<ServerCommand> searchMatchingDB(Connection conn, ServerCommand valueObject) throws SQLException {

    	  List<ServerCommand> searchResults;

          boolean first = true;
          StringBuffer sql = new StringBuffer("SELECT * FROM TB_SERVER_COMMAND WHERE 1=1 ");

          if (valueObject.getIdServer() != null) {
              if (first) { first = false; }
              sql.append("AND ID_SERVER = '").append(valueObject.getIdServer().trim()).append("' ");
          }

          if (valueObject.getIdCommand() != null) {
              if (first) { first = false; }
              sql.append("AND ID_COMMAND = '").append(valueObject.getIdCommand().trim()).append("' ");
          }

          if (valueObject.getIdProject() != null) {
              if (first) { first = false; }
              sql.append("AND ID_PROJECT = '").append(valueObject.getIdProject().trim()).append("' ");
          }

          sql.append("ORDER BY ID_SERVER ASC ");

          // Prevent accidential full table results.
          // Use loadAll if all rows must be returned.
//          if (first)
//               searchResults = new ArrayList<ServerCommand>();
//          else
               searchResults = listQueryDB(conn, conn.prepareStatement(sql.toString()));

          return searchResults;
    }

    protected int databaseUpdateDB(Connection conn, PreparedStatement stmt) throws SQLException {

          int result = stmt.executeUpdate();

          return result;
    }

    protected void singleQueryDB(Connection conn, PreparedStatement stmt, ServerCommand valueObject) 
          throws NotFoundException, SQLException {

          ResultSet result = null;

          try {
              result = stmt.executeQuery();

              if (result.next()) {

                   valueObject.setIdServer(result.getString("ID_SERVER")); 
                   valueObject.setIdCommand(result.getString("ID_COMMAND")); 
                   valueObject.setIdProject(result.getString("ID_PROJECT"));

              } else {
                    //System.out.println("ServerCommand Object Not Found!");
                    throw new NotFoundException("ServerCommand Object Not Found!");
              }
          } finally {
              if (result != null)
                  result.close();
              if (stmt != null)
                  stmt.close();
          }
    }

    protected List<ServerCommand> listQueryDB(Connection conn, PreparedStatement stmt) throws SQLException {

          ArrayList<ServerCommand> searchResults = new ArrayList<ServerCommand>();
          ResultSet result = null;

          try {
              result = stmt.executeQuery();

              while (result.next()) {
                   ServerCommand temp = createValueObject();

                   temp.setIdServer(result.getString("ID_SERVER")); 
                   temp.setIdCommand(result.getString("ID_COMMAND")); 
                   temp.setIdProject(result.getString("ID_PROJECT"));

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

    public List<ServerCommand> searchProjectServerByCommandDB(Connection conn, String idCommand) throws NotFoundException, SQLException {
   	
        if (idCommand == null) {
             throw new NotFoundException("Can not select without Primary-Key!");
        }

        String sql = "SELECT * FROM TB_SERVER_COMMAND WHERE (ID_COMMAND = ? ) ORDER BY ID_PROJECT, ID_SERVER ASC "; 
        PreparedStatement stmt = null;

        try {
             stmt = conn.prepareStatement(sql);
             stmt.setString(1, idCommand); 
             
            return listQueryDB(conn,stmt);  
        } finally {
            if (stmt != null)
                stmt.close();
        }
  }
    
}