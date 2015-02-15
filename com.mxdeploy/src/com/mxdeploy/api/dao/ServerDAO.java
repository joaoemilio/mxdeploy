package com.mxdeploy.api.dao;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.mxdeploy.api.domain.Database;
import com.mxdeploy.api.domain.Project;
import com.mxdeploy.api.domain.Server;
import com.mxdeploy.api.service.ProjectService;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class ServerDAO {

    public Server createValueObject() {
          return new Server();
    }


    public synchronized void create(Server valueObject) {
    	writeXML(valueObject);
		Database database = Database.getInstance();
		database.getServerList().add(valueObject);
    }
    
    private void writeXML(Server valueObject ){
		XStream xstream = new XStream(new DomDriver());
		try { 
			xstream.toXML(valueObject, new FileWriter( Database.getServerPath()+"/"+valueObject.getHostname()+".xml" ));
		} catch (IOException e) {
			e.printStackTrace(); 
		} 
    }
    
    public synchronized void createDB(Connection conn, Server valueObject) throws SQLException {

          String sql = "";
          PreparedStatement stmt = null;

          try {
               sql = "INSERT INTO TB_SERVER ( ID, NAME, HOSTNAME,PORT, TYPE ) VALUES (?, ?, ?, ?, ?) ";
               stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS); 

               stmt.setString(1, valueObject.getId()); 
               stmt.setString(2, valueObject.getName()); 
               stmt.setString(3, valueObject.getHostname()); 
               stmt.setObject(4, valueObject.getPort(), Types.INTEGER); 
               stmt.setObject(5, valueObject.getType(), Types.INTEGER);
               
               int rowcount = databaseUpdate(conn, stmt);
               if (rowcount != 1) {
                    throw new SQLException("PrimaryKey Error when updating DB!");
               }

          } finally {
              if (stmt != null)
                  stmt.close();
          }


    }


    public void save(Server valueObject) {
    	ProjectService projectService = new ProjectService();

    	List<Server> servers = Database.getInstance().getServerList();
    	for(Server server : servers ){
    		if(server.getId().equals(valueObject.getId())){
				server.setName(valueObject.getName());
				server.setPort(valueObject.getPort());
				server.setType(valueObject.getType());
				if(valueObject.getAddress()!=null){
					server.setAddress(valueObject.getAddress());
				}
				if(valueObject.getAlias()!=null){
					server.setAlias(valueObject.getAlias());
				}				
				if(valueObject.getProperty()!=null){
				   server.setProperty( valueObject.getProperty() );
				}
				if(valueObject.getDefaultSocksUUID() != null && !valueObject.getDefaultSocksUUID().equals("")) {
					server.setDefaultSocksUUID(valueObject.getDefaultSocksUUID());
				}

				writeXML(server);
    		}
    	}
    	
    	List<Project> projects = Database.getInstance().getProjects();
    	for(Project project : projects ){
    		for(Server server : project.getServers() ){
    			if(server.getHostname().equals(valueObject.getHostname())){
    				server.setName( valueObject.getName() );
    				server.setPort( valueObject.getPort() );
    				server.setType( valueObject.getType() );
    				if(valueObject.getAddress()!=null){
    					server.setAddress(valueObject.getAddress());
    				}
    				if(valueObject.getAlias()!=null){
    					server.setAlias(valueObject.getAlias());
    				}
    				server.setProperty(null);
    				projectService.update(project);
    			}
    		}
    	}
    }

    public void saveDB(Connection conn, Server valueObject) 
          throws NotFoundException, SQLException {

          String sql = "UPDATE TB_SERVER SET NAME = ?, HOSTNAME = ?, PORT = ?, TYPE = ? WHERE (ID = ? ) ";
          PreparedStatement stmt = null;

          try {
              stmt = conn.prepareStatement(sql);
              stmt.setString(1, valueObject.getName()); 
              stmt.setString(2, valueObject.getHostname()); 
              stmt.setObject(3, valueObject.getPort(), Types.INTEGER);
              stmt.setObject(4, valueObject.getType(), Types.INTEGER);
              stmt.setString(5, valueObject.getId()); 

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


    public void delete(Connection conn, Server valueObject) 
          throws NotFoundException, SQLException {

          if (valueObject.getId() == null) {
               throw new NotFoundException("Can not delete without Primary-Key!");
          }

          String sql = "DELETE FROM TB_SERVER WHERE (ID = ? ) ";
          PreparedStatement stmt = null;

          try {
              stmt = conn.prepareStatement(sql);
              stmt.setString(1, valueObject.getId()); 

              int rowcount = databaseUpdate(conn, stmt);
              if (rowcount == 0) {
                   throw new NotFoundException("Object could not be deleted! (PrimaryKey not found)");
              }
          } finally {
              if (stmt != null)
                  stmt.close();
          }
    }

    public void deleteByID(Connection conn, String id)
			throws NotFoundException, SQLException {

		if (id == null) {
			throw new NotFoundException("Can not delete without Primary-Key!");
		}

		String sql = "DELETE FROM TB_SERVER WHERE (ID = ? ) ";
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, id);

			databaseUpdate(conn, stmt);
		} finally {
			if (stmt != null)
				stmt.close();
		}
	}

    public List<Server> searchMatchingDB(Connection conn, Server valueObject) throws SQLException {

          List<Server> searchResults;

          boolean first = true;
          StringBuffer sql = new StringBuffer("SELECT * FROM TB_SERVER WHERE 1=1 ");

          if(valueObject!=null){
	          if (valueObject.getId() != null) {
	              if (first) { first = false; }
	              sql.append("AND ID = '").append(valueObject.getId().trim()).append("' ");
	          }
	
	          if (valueObject.getName() != null) {
	              if (first) { first = false; }
	              sql.append("AND NAME LIKE '").append(valueObject.getName()).append("%' ");
	          }
	
	          if (valueObject.getHostname() != null) {
	              if (first) { first = false; }
	              sql.append("AND HOSTNAME LIKE '").append(valueObject.getHostname()).append("%' ");
	          }
	
	        
	          if (valueObject.getType() != null) {
	              if (first) { first = false; }
	              sql.append("AND TYPE = ").append(valueObject.getType()).append(" ");
	          }
          }
          sql.append("ORDER BY NAME ASC ");

          // Prevent accidential full table results.
          // Use loadAll if all rows must be returned.
//          if (first)
//               searchResults = new ArrayList<Server>();
//          else
               searchResults = listQuery(conn, conn.prepareStatement(sql.toString()));

          return searchResults;
    }

    public List<Server> searchMatching(Server valueObject) {

    	Database database = Database.getInstance();
    	List<Server> allServers = database.getServerList();
    	
        List<Server> searchResults = new ArrayList<Server>();
        
      	for(Server server : allServers){
      		boolean addThis = false;
      		if(valueObject!=null){
      		  if (valueObject.getId() != null) {
    		     if(server.getId().equals(valueObject.getId()))
    		    	addThis = true;
    	         else
     	        	addThis = false;   
      		  }
      		  
    		  if (valueObject.getName() != null) {
    	         if( server.getName().startsWith(valueObject.getName()) )
    	        	addThis = true;
    	         else
    	        	addThis = false; 
    		  }
    		  
	          if (valueObject.getHostname() != null) {
        		 if(server.getHostname().startsWith(valueObject.getHostname()))
      	        	addThis = true;
     	         else
     	        	addThis = false; 
	          }
	          
	          if (valueObject.getType() != null) {
	             if(server.getType().equals(valueObject.getType()))
      	        	addThis = true;
     	         else
     	        	addThis = false;		            	
		      }
	          
      		} else {
      			addThis = true;
      		}
	        
      		if(addThis)
     		   searchResults.add(server);
        }

        return searchResults;
  }
    
    public List<Server> searchByProject(String idProject) {
    	Database database = Database.getInstance();
    	for(Project project : database.getProjects() ){
    		if(project.getId().equals(idProject)){
    		   return project.getServers();	
    		} 
    	}
    	return null;
    }
    
    public List<Server> searchByProjectDB(Connection conn, String idProject) throws SQLException {

        List<Server> searchResults;

        String sql = " SELECT SERVER.ID, SERVER.NAME " +
        		     "       ,SERVER.HOSTNAME, SERVER.PORT, SERVER.TYPE "+
                     " FROM  TB_SERVER SERVER, TB_PROJECT_SERVER PROJECT_SERVER "+
                     " WHERE SERVER.ID = PROJECT_SERVER.ID_SERVER "+
                     "   AND PROJECT_SERVER.ID_PROJECT = '"+idProject+"' "+
                     " ORDER BY SERVER.NAME "; 
        
        searchResults = listQuery(conn, conn.prepareStatement(sql));

        return searchResults;
   }


    /**
     * databaseUpdate-method. This method is a helper method for internal use. It will execute
     * all database handling that will change the information in tables. SELECT queries will
     * not be executed here however. The return value indicates how many rows were affected.
     * This method will also make sure that if cache is used, it will reset when data changes.
     *
     * @param conn         This method requires working database connection.
     * @param stmt         This parameter contains the SQL statement to be excuted.
     */
    protected int databaseUpdate(Connection conn, PreparedStatement stmt) throws SQLException {

          int result = stmt.executeUpdate();

          return result;
    }




    /**
     * databaseQuery-method. This method is a helper method for internal use. It will execute
     * all database queries that will return multiple rows. The resultset will be converted
     * to the List of valueObjects. If no rows were found, an empty List will be returned.
     *
     * @param conn         This method requires working database connection.
     * @param stmt         This parameter contains the SQL statement to be excuted.
     */
    protected List<Server> listQuery(Connection conn, PreparedStatement stmt) throws SQLException {

          ArrayList<Server> searchResults = new ArrayList<Server>();
          ResultSet result = null;

          try {
              result = stmt.executeQuery();

              while (result.next()) {
                   Server temp = createValueObject();

                   temp.setId(result.getString("ID")); 
                   temp.setName(result.getString("NAME")); 
                   temp.setHostname(result.getString("HOSTNAME")); 
                   temp.setPort((Integer)result.getObject("PORT"));
                   temp.setType((Integer)result.getObject("TYPE")); 
                   
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

