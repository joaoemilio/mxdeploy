package com.mxdeploy.api.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mxdeploy.api.domain.WindowsService;

public class WindowsServicesDAO {


    public WindowsService createValueObject() {
          return new WindowsService();
    }


    public WindowsService getObject(Connection conn, String id) throws NotFoundException, SQLException {

          WindowsService valueObject = createValueObject();
          valueObject.setId(id);
          load(conn, valueObject);
          return valueObject;
    }


    public void load(Connection conn, WindowsService valueObject) throws NotFoundException, SQLException {

          if (valueObject.getId() == null) {
               throw new NotFoundException("Can not select without Primary-Key!");
          }

          String sql = "SELECT * FROM TB_WINDOWS_SERVICES WHERE (ID = ? ) "; 
          PreparedStatement stmt = null;

          try {
               stmt = conn.prepareStatement(sql);
               stmt.setString(1,valueObject.getId()); 

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
    public List<WindowsService> loadAll(Connection conn) throws SQLException {

          String sql = "SELECT * FROM TB_WINDOWS_SERVICES ORDER BY NAME ASC ";
          List<WindowsService> searchResults = listQuery(conn, conn.prepareStatement(sql));

          return searchResults;
    }

    /**
     * LoadByServer-method. This will read all contents from database table and
     * build a List containing valueObjects. Filtering by the Server ID
     *
     * @param conn         This method requires working database connection.
     * @param serverId     this method requires a server ID
     */
    public List<WindowsService> loadByServerId(Connection conn, String serverId) throws SQLException {

          String sql = "SELECT * FROM TB_WINDOWS_SERVICES WHERE ID_SERVER = ? ORDER BY NAME ASC ";
          List<WindowsService> searchResults = listQuery(conn, conn.prepareStatement(sql));

          return searchResults;
    }



    /**
     * create-method. This will create new row in database according to supplied
     * valueObject contents. Make sure that values for all NOT NULL columns are
     * correctly specified. Also, if this table does not use automatic surrogate-keys
     * the primary-key must be specified. After INSERT command this method will 
     * read the generated primary-key back to valueObject if automatic surrogate-keys
     * were used. 
     *
     * @param conn         This method requires working database connection.
     * @param valueObject  This parameter contains the class instance to be created.
     *                     If automatic surrogate-keys are not used the Primary-key 
     *                     field must be set for this to work properly.
     */
    public synchronized void create(Connection conn, WindowsService valueObject) throws SQLException {

          String sql = "";
          PreparedStatement stmt = null;
          //ResultSet result = null;

          try {
               sql = "INSERT INTO TB_WINDOWS_SERVICES ( ID, NAME, DESCRIPTION, ID_SERVER ) VALUES (?, ?, ?, ?) ";
               stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS); 

               stmt.setString(1, valueObject.getId()); 
               stmt.setString(2, valueObject.getName()); 
               stmt.setString(3, valueObject.getDescription()); 
               stmt.setString(4, valueObject.getServerId()); 
               
               int rowcount = databaseUpdate(conn, stmt);
               if (rowcount != 1) {
                    throw new SQLException("PrimaryKey Error when updating DB!");
               }
               
//               result = stmt.getGeneratedKeys();  
//               result.next(); 
//               String str = String.valueOf(result.getLong(1));
//               valueObject.setId(str);  
               

          } finally {
              if (stmt != null)
                  stmt.close();
          }


    }


    /**
     * save-method. This method will save the current state of valueObject to database.
     * Save can not be used to create new instances in database, so upper layer must
     * make sure that the primary-key is correctly specified. Primary-key will indicate
     * which instance is going to be updated in database. If save can not find matching 
     * row, NotFoundException will be thrown.
     *
     * @param conn         This method requires working database connection.
     * @param valueObject  This parameter contains the class instance to be saved.
     *                     Primary-key field must be set for this to work properly.
     */
    public void save(Connection conn, WindowsService valueObject) 
          throws NotFoundException, SQLException {

          String sql = "UPDATE TB_WINDOWS_SERVICES SET NAME = ?, DESCRIPTION = ?, ID_SERVER = ? WHERE (ID = ? ) ";
          PreparedStatement stmt = null;

          try {
              stmt = conn.prepareStatement(sql);
              stmt.setString(1, valueObject.getName()); 
              stmt.setString(2, valueObject.getDescription()); 
              stmt.setString(3, valueObject.getServerId()); 
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


    /**
     * delete-method. This method will remove the information from database as identified by
     * by primary-key in supplied valueObject. Once valueObject has been deleted it can not 
     * be restored by calling save. Restoring can only be done using create method but if 
     * database is using automatic surrogate-keys, the resulting object will have different 
     * primary-key than what it was in the deleted object. If delete can not find matching row,
     * NotFoundException will be thrown.
     *
     * @param conn         This method requires working database connection.
     * @param valueObject  This parameter contains the class instance to be deleted.
     *                     Primary-key field must be set for this to work properly.
     */
    public void delete(Connection conn, WindowsService valueObject) 
          throws NotFoundException, SQLException {

          if (valueObject.getId() == null) {
               throw new NotFoundException("Can not delete without Primary-Key!");
          }

          String sql = "DELETE FROM TB_WINDOWS_SERVICES WHERE (ID = ? ) ";
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

		String sql = "DELETE FROM TB_WINDOWS_SERVICES WHERE (ID = ? ) ";
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

    /**
	 * deleteAll-method. This method will remove all information from the table
	 * that matches this Dao and ValueObject couple. This should be the most
	 * efficient way to clear table. Once deleteAll has been called, no
	 * valueObject that has been created before can be restored by calling save.
	 * Restoring can only be done using create method but if database is using
	 * automatic surrogate-keys, the resulting object will have different
	 * primary-key than what it was in the deleted object. (Note, the
	 * implementation of this method should be different with different DB
	 * backends.)
	 * 
	 * @param conn
	 *            This method requires working database connection.
	 */
    public void deleteAll(Connection conn) throws SQLException {

          String sql = "DELETE FROM TB_WINDOWS_SERVICES";
          PreparedStatement stmt = null;

          try {
              stmt = conn.prepareStatement(sql);
              databaseUpdate(conn, stmt);
          } finally {
              if (stmt != null)
                  stmt.close();
          }
    }


    /**
     * coutAll-method. This method will return the number of all rows from table that matches
     * this Dao. The implementation will simply execute "select count(primarykey) from table".
     * If table is empty, the return value is 0. This method should be used before calling
     * loadAll, to make sure table has not too many rows.
     *
     * @param conn         This method requires working database connection.
     */
    public int countAll(Connection conn) throws SQLException {

          String sql = "SELECT count(*) FROM TB_WINDOWS_SERVICES";
          PreparedStatement stmt = null;
          ResultSet result = null;
          int allRows = 0;

          try {
              stmt = conn.prepareStatement(sql);
              result = stmt.executeQuery();
 
              if (result.next())
                  allRows = result.getInt(1);
          } finally {
              if (result != null)
                  result.close();
              if (stmt != null)
                  stmt.close();
          }
          return allRows;
    }


    /** 
     * searchMatching-Method. This method provides searching capability to 
     * get matching valueObjects from database. It works by searching all 
     * objects that match permanent instance variables of given object.
     * Upper layer should use this by setting some parameters in valueObject
     * and then  call searchMatching. The result will be 0-N objects in a List, 
     * all matching those criteria you specified. Those instance-variables that
     * have NULL values are excluded in search-criteria.
     *
     * @param conn         This method requires working database connection.
     * @param valueObject  This parameter contains the class instance where search will be based.
     *                     Primary-key field should not be set.
     */
    public List<WindowsService> searchMatching(Connection conn, WindowsService valueObject) throws SQLException {

          List<WindowsService> searchResults;

          boolean first = true;
          StringBuffer sql = new StringBuffer("SELECT * FROM TB_WINDOWS_SERVICES WHERE 1=1 ");

          if (valueObject.getId() != null) {
              if (first) { first = false; }
              sql.append("AND ID = '").append(valueObject.getId().trim()).append("' ");
          }

          if (valueObject.getName() != null) {
              if (first) { first = false; }
              sql.append("AND NAME LIKE '").append(valueObject.getName()).append("%' ");
          }

          if (valueObject.getName() != null) {
              if (first) { first = false; }
              sql.append("AND DESCRIPTION LIKE '").append(valueObject.getName()).append("%' ");
          }

          if (valueObject.getServerId() != null) {
              if (first) { first = false; }
              sql.append("AND ID_SERVER LIKE '").append(valueObject.getServerId()).append("%' ");
          }

          sql.append("ORDER BY NAME ASC ");

          // Prevent accidential full table results.
          // Use loadAll if all rows must be returned.
          if (first)
               searchResults = new ArrayList<WindowsService>();
          else
               searchResults = listQuery(conn, conn.prepareStatement(sql.toString()));

          return searchResults;
    }

    /** 
     * getDaogenVersion will return information about
     * generator which created these sources.
     */
    public String getDaogenVersion() {
        return "DaoGen version 2.4.1";
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
     * all database queries that will return only one row. The resultset will be converted
     * to valueObject. If no rows were found, NotFoundException will be thrown.
     *
     * @param conn         This method requires working database connection.
     * @param stmt         This parameter contains the SQL statement to be excuted.
     * @param valueObject  Class-instance where resulting data will be stored.
     */
    protected void singleQuery(Connection conn, PreparedStatement stmt, WindowsService valueObject) 
          throws NotFoundException, SQLException {

          ResultSet result = null;

          try {
              result = stmt.executeQuery();

              if (result.next()) {

                   valueObject.setId(result.getString("ID")); 
                   valueObject.setName(result.getString("NAME")); 
                   valueObject.setDescription(result.getString("DESCRIPTION")); 
                   valueObject.setServerId(result.getString("ID_SERVER")); 
                   

              } else {
                    throw new NotFoundException("WindowsService Object Not Found!");
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
    protected List<WindowsService> listQuery(Connection conn, PreparedStatement stmt) throws SQLException {

          ArrayList<WindowsService> searchResults = new ArrayList<WindowsService>();
          ResultSet result = null;

          try {
              result = stmt.executeQuery();

              while (result.next()) {
                   WindowsService temp = createValueObject();

                   temp.setId(result.getString("ID")); 
                   temp.setName(result.getString("NAME")); 
                   temp.setDescription(result.getString("DESCRIPTION")); 
                   temp.setServerId(result.getString("ID_SERVER")); 
                   
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

