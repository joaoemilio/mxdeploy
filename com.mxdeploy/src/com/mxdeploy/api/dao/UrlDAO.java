package com.mxdeploy.api.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mxdeploy.api.domain.Database;
import com.mxdeploy.api.domain.Project;
import com.mxdeploy.api.domain.Url;


public class UrlDAO {

    // Cache contents:
    private boolean cacheOk;
    private List cacheData;

    /**
     * Constructor for Dao. This constructor will only reset cache.
     * If extended Dao classes are generated, it is important to
     * make sure resetCache() will be called in constructor.
     */
    public UrlDAO() {
        resetCache();
    }

    /**
     * resetCache-method. This is important method when caching is used
     * to keep data in Dao instance. This method must be called whenever
     * the data in table has been changed. This method will mark current
     * cache to be outdated and next time when cacheable data will be
     * retrieved from database, the cache will be rebuilt. Please note
     * that using Dao-cache can have remarkable performace boost or it may
     * not help at all. It all depends on the amount of data and the rate
     * that data is being changed.
     */
    public void resetCache() {
        cacheOk = false;
        cacheData = null;
    }



    /**
     * createValueObject-method. This method is used when the Dao class needs
     * to create new value object instance. The reason why this method exists
     * is that sometimes the programmer may want to extend also the valueObject
     * and then this method can be overrided to return extended valueObject.
     * NOTE: If you extend the valueObject class, make sure to override the
     * clone() method in it!
     */
    public Url createValueObject() {
          return new Url();
    }


    /**
     * getObject-method. This will create and load valueObject contents from database 
     * using given Primary-Key as identifier. This method is just a convenience method 
     * for the real load-method which accepts the valueObject as a parameter. Returned
     * valueObject will be created using the createValueObject() method.
     */
    public Url getObject(Connection conn, String id) throws NotFoundException, SQLException {

          Url valueObject = createValueObject();
          valueObject.setId(id);
          load(conn, valueObject);
          return valueObject;
    }


    /**
     * load-method. This will load valueObject contents from database using
     * Primary-Key as identifier. Upper layer should use this so that valueObject
     * instance is created and only primary-key should be specified. Then call
     * this method to complete other persistent information. This method will
     * overwrite all other fields except primary-key and possible runtime variables.
     * If load can not find matching row, NotFoundException will be thrown.
     *
     * @param conn         This method requires working database connection.
     * @param valueObject  This parameter contains the class instance to be loaded.
     *                     Primary-key field must be set for this to work properly.
     */
    public void load(Connection conn, Url valueObject) throws NotFoundException, SQLException {

          if (valueObject.getId() == null) {
               //System.out.println("Can not select without Primary-Key!");
               throw new NotFoundException("Can not select without Primary-Key!");
          }

          String sql = "SELECT * FROM TB_URL WHERE (ID = ? ) "; 
          PreparedStatement stmt = null;

          try {
               stmt = conn.prepareStatement(sql);
               stmt.setString(1, valueObject.getId()); 

               singleQuery(conn, stmt, valueObject);

          } finally {
              if (stmt != null)
                  stmt.close();
          }
    }


    /**
     * LoadAll-method. This will read all contents from database table and
     * build a List containing valueObjects. Please note, that this method
     * will consume huge amounts of resources if table has lot's of rows. 
     * This should only be used when target tables have only small amounts
     * of data.
     *
     * @param conn         This method requires working database connection.
     */
    public List loadAll(Connection conn) throws SQLException {

          // Check the cache status and use Cache if possible.
          if (cacheOk) {
              return cacheData;
          }

          String sql = "SELECT * FROM TB_URL ORDER BY ID ASC ";
          List searchResults = listQuery(conn, conn.prepareStatement(sql));

          // Update cache and mark it ready.
          cacheData = searchResults;
          cacheOk = true;

          return searchResults;
    }

    public synchronized void createDB(Connection conn, Url valueObject) throws SQLException {

          String sql = "";
          PreparedStatement stmt = null;

          try {
               sql = "INSERT INTO TB_URL ( ID, ID_PROJECT, NAME, URL) VALUES (?, ?, ?, ?) ";
               stmt = conn.prepareStatement(sql);

               stmt.setString(1, valueObject.getId()); 
               stmt.setString(2, valueObject.getIdProject()); 
               stmt.setString(3, valueObject.getName()); 
               stmt.setString(4, valueObject.getUrl()); 

               int rowcount = databaseUpdate(conn, stmt);
               if (rowcount != 1) {
                    //System.out.println("PrimaryKey Error when updating DB!");
                    throw new SQLException("PrimaryKey Error when updating DB!");
               }

          } finally {
              if (stmt != null)
                  stmt.close();
          }


    }

    public void saveDB(Connection conn, Url valueObject) 
          throws NotFoundException, SQLException {

          String sql = "UPDATE TB_URL SET ID_PROJECT = ?, NAME = ?, URL = ? WHERE (ID = ? ) ";
          PreparedStatement stmt = null;

          try {
              stmt = conn.prepareStatement(sql);
              stmt.setString(1, valueObject.getIdProject()); 
              stmt.setString(2, valueObject.getName()); 
              stmt.setString(3, valueObject.getUrl()); 

              stmt.setString(4, valueObject.getId()); 

              int rowcount = databaseUpdate(conn, stmt);
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

    public void deleteDB(Connection conn, Url valueObject) 
          throws NotFoundException, SQLException {

          if (valueObject.getId() == null) {
               //System.out.println("Can not delete without Primary-Key!");
               throw new NotFoundException("Can not delete without Primary-Key!");
          }

          String sql = "DELETE FROM TB_URL WHERE (ID = ? ) ";
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

    public void deleteAllDB(Connection conn) throws SQLException {

          String sql = "DELETE FROM TB_URL";
          PreparedStatement stmt = null;

          try {
              stmt = conn.prepareStatement(sql);
              databaseUpdate(conn, stmt);
          } finally {
              if (stmt != null)
                  stmt.close();
          }
    }

    public List<Url> searchMatching(Url valueObject) {

    	Database database = Database.getInstance();
    	List<Project> allProjects = database.getProjects();
    	
        List<Url> searchResults = new ArrayList<Url>();
        
      	for(Project project : allProjects){
      		for(Url url : project.getUrls()){
	      		boolean addThis = false;
	      		if(valueObject!=null){
	      		  if (valueObject.getId() != null) {
	    		     if(project.getId().equals(valueObject.getId()))
	    		    	addThis = true;
	    	         else
	     	        	addThis = false;   
	      		  }
	      		  
	      		  if (valueObject.getIdProject() != null) {
		    		     if(project.getId().equals(valueObject.getIdProject()))
		    		    	addThis = true;
		    	         else
		     	        	addThis = false;   
	      		  }	      		  
		          
	      		} else {
	      			addThis = true;
	      		}
		        
	      		if(addThis)
	     		   searchResults.add(url);
      		}
      	}

        return searchResults;
  }
    
    
    public List<Url> searchMatchingDB(Connection conn, Url valueObject) throws SQLException {

          List<Url> searchResults;

          boolean first = true;
          StringBuffer sql = new StringBuffer("SELECT * FROM TB_URL WHERE 1=1 ");

          if (valueObject.getId() != null) {
              if (first) { first = false; }
              sql.append("AND ID = '").append(valueObject.getId()).append("' ");
          }

          if (valueObject.getIdProject() != null) {
              if (first) { first = false; }
              sql.append("AND ID_PROJECT = '").append(valueObject.getIdProject()).append("' ");
          }

          if (valueObject.getName() != null) {
              if (first) { first = false; }
              sql.append("AND NAME LIKE '").append(valueObject.getName()).append("%' ");
          }

          if (valueObject.getUrl() != null) {
              if (first) { first = false; }
              sql.append("AND URL LIKE '").append(valueObject.getUrl()).append("%' ");
          }


          sql.append("ORDER BY ID ASC ");

          // Prevent accidential full table results. 
          // Use loadAll if all rows must be returned.
          if (first)
               searchResults = new ArrayList<Url>();
          else
               searchResults = listQuery(conn, conn.prepareStatement(sql.toString()));

          return searchResults;
    }


    protected int databaseUpdate(Connection conn, PreparedStatement stmt) throws SQLException {

          int result = stmt.executeUpdate();

          resetCache();

          return result;
    }



    /**
     * databaseQuery-method. This method is a helper method for internal use. It will execute
     * all database queries that will return only one row. The resultset will be converted
     * to valueObject. If no rows were found, NotFoundException will be thrown.
     *
     * @param conn         This method requires working database connection.
     * @param stmt         This parameter contains the SQL statement to be excuted.
     * @param valueObject  Class-instance where resulting data will be stored.
     */
    protected void singleQuery(Connection conn, PreparedStatement stmt, Url valueObject) 
          throws NotFoundException, SQLException {

          ResultSet result = null;

          try {
              result = stmt.executeQuery();

              if (result.next()) {

                   valueObject.setId(result.getString("ID")); 
                   valueObject.setIdProject(result.getString("ID_PROJECT")); 
                   valueObject.setName(result.getString("NAME")); 
                   valueObject.setUrl(result.getString("URL")); 

              } else {
                    //System.out.println("Url Object Not Found!");
                    throw new NotFoundException("Url Object Not Found!");
              }
          } finally {
              if (result != null)
                  result.close();
              if (stmt != null)
                  stmt.close();
          }
    }


    /**
     * databaseQuery-method. This method is a helper method for internal use. It will execute
     * all database queries that will return multiple rows. The resultset will be converted
     * to the List of valueObjects. If no rows were found, an empty List will be returned.
     *
     * @param conn         This method requires working database connection.
     * @param stmt         This parameter contains the SQL statement to be excuted.
     */
    protected List<Url> listQuery(Connection conn, PreparedStatement stmt) throws SQLException {

    	  List<Url> searchResults = new ArrayList<Url>();
          ResultSet result = null;

          try {
              result = stmt.executeQuery();

              while (result.next()) {
                   Url temp = createValueObject();

                   temp.setId(result.getString("ID")); 
                   temp.setIdProject(result.getString("ID_PROJECT")); 
                   temp.setName(result.getString("NAME")); 
                   temp.setUrl(result.getString("URL")); 

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

