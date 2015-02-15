package com.mxdeploy.api.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.mxdeploy.api.domain.Command;
import com.mxdeploy.api.domain.CommandItem;

public class CommandItemDAO {

    public CommandItem createValueObject() {
          return new CommandItem();
    }

    public synchronized void createDB(Connection conn, CommandItem valueObject) throws SQLException {

          String sql = "";
          PreparedStatement stmt = null;
          ResultSet result = null;

          try {
               sql = "INSERT INTO TB_COMMAND_ITEM ( ID_COMMAND, COMMAND_SSH ) VALUES (?, ?) ";
               stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

               //stmt.setObject(1, valueObject.getId(), Types.INTEGER); 
               stmt.setString(1, valueObject.getIdCommand()); 
               stmt.setString(2, valueObject.getCommandSSH()); 


               int rowcount = databaseUpdateDB(conn, stmt);
               if (rowcount != 1) {
                    throw new SQLException("PrimaryKey Error when updating DB!");
               }
               result = stmt.getGeneratedKeys();  
               result.next(); 
               String str = String.valueOf(result.getLong(1));
               valueObject.setId(new Integer(str));  

          } finally {
              if (stmt != null)
                  stmt.close();
          }


    }

    public void saveDB(Connection conn, CommandItem valueObject) 
          throws NotFoundException, SQLException {

          String sql = "UPDATE TB_COMMAND_ITEM SET ID_COMMAND = ?, COMMAND_SSH = ? WHERE (ID = ? ) ";
          PreparedStatement stmt = null;

          try {
              stmt = conn.prepareStatement(sql);
              stmt.setString(1, valueObject.getIdCommand()); 
              stmt.setString(2, valueObject.getCommandSSH()); 
              
              stmt.setObject(3, valueObject.getId(), Types.INTEGER);

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

    public void deleteDB(Connection conn, CommandItem valueObject) 
          throws NotFoundException, SQLException {

          if (valueObject.getId() == null) {
               //System.out.println("Can not delete without Primary-Key!");
               throw new NotFoundException("Can not delete without Primary-Key!");
          }

          String sql = "DELETE FROM TB_COMMAND_ITEM WHERE (ID = ? ) ";
          PreparedStatement stmt = null;

          try {
              stmt = conn.prepareStatement(sql);
              stmt.setObject(1, valueObject.getId(), Types.INTEGER);

              int rowcount = databaseUpdateDB(conn, stmt);
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

    public void deleteByCommandDB(Connection conn, Command command) throws NotFoundException, SQLException {
	    if (command.getId() == null) {
	         throw new NotFoundException("Can not delete without Primary-Key!");
	    }
	
	    String sql = "DELETE FROM TB_COMMAND_ITEM WHERE (ID_COMMAND = ? ) ";
	    PreparedStatement stmt = null;
	
	    try { 
	        stmt = conn.prepareStatement(sql);
	        stmt.setString(1, command.getId()); 
	
	        databaseUpdateDB(conn, stmt);
	    } finally {
	        if (stmt != null)
	            stmt.close();
	    }
    }

    public List<CommandItem> searchMatchingDB(Connection conn, CommandItem valueObject) throws SQLException {

          List<CommandItem> searchResults;

          boolean first = true;
          StringBuffer sql = new StringBuffer("SELECT * FROM TB_COMMAND_ITEM WHERE 1=1 ");

          if (valueObject.getId() != null) {
              if (first) { first = false; }
              sql.append("AND ID = ").append(valueObject.getId()).append(" ");
          }

          if (valueObject.getIdCommand() != null) {
              if (first) { first = false; }
              sql.append("AND ID_COMMAND = ").append(valueObject.getIdCommand()).append(" ");
          }

          if (valueObject.getCommandSSH() != null) {
              if (first) { first = false; }
              sql.append("AND COMMAND_SSH LIKE '").append(valueObject.getCommandSSH()).append("%' ");
          }

          sql.append("ORDER BY ID ASC ");

          // Prevent accidential full table results.
          // Use loadAll if all rows must be returned.
//          if (first)
//               searchResults = new ArrayList<CommandItem>();
//          else
               searchResults = listQueryDB(conn, conn.prepareStatement(sql.toString()));

          return searchResults;
    }

    protected int databaseUpdateDB(Connection conn, PreparedStatement stmt) throws SQLException {

          int result = stmt.executeUpdate();

          return result;
    }

    protected void singleQueryDB(Connection conn, PreparedStatement stmt, CommandItem valueObject) throws NotFoundException, SQLException {
          ResultSet result = null;
          try {
              result = stmt.executeQuery();

              if (result.next()) {

                   valueObject.setId((Integer)result.getObject("ID")); 
                   valueObject.setIdCommand(result.getString("ID_COMMAND")); 
                   valueObject.setCommandSSH(result.getString("COMMAND_SSH")); 

              } else {
                    //System.out.println("CommandItem Object Not Found!");
                    throw new NotFoundException("CommandItem Object Not Found!");
              }
          } finally {
              if (result != null)
                  result.close();
              if (stmt != null)
                  stmt.close();
          }
    }

    protected List<CommandItem> listQueryDB(Connection conn, PreparedStatement stmt) throws SQLException {

          ArrayList<CommandItem> searchResults = new ArrayList<CommandItem>();
          ResultSet result = null;

          try {
              result = stmt.executeQuery();

              while (result.next()) {
                   CommandItem temp = createValueObject();

                   temp.setId((Integer)result.getObject("ID")); 
                   temp.setIdCommand(result.getString("ID_COMMAND")); 
                   temp.setCommandSSH(result.getString("COMMAND_SSH")); 

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

