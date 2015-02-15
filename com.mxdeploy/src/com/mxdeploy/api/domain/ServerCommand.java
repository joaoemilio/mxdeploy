package com.mxdeploy.api.domain;

import java.io.Serializable;

public class ServerCommand implements Cloneable, Serializable {

    /** 
     * Persistent Instance variables. This data is directly 
     * mapped to the columns of database table.
     */
	private String id;
    private String idServer;
    private String idCommand;
    private String idProject;
    private Command command;



    /**
	 * @return the command
	 */
	public Command getCommand() {
		return command;
	}

	/**
	 * @param command the command to set
	 */
	public void setCommand(Command command) {
		this.command = command;
	}

	/**
	 * @return the idProject
	 */
	public String getIdProject() {
		return idProject;
	}

	/**
	 * @param idProject the idProject to set
	 */
	public void setIdProject(String idProject) {
		this.idProject = idProject;
	}

	/** 
     * Constructors. DaoGen generates two constructors by default.
     * The first one takes no arguments and provides the most simple
     * way to create object instance. The another one takes one
     * argument, which is the primary key of the corresponding table.
     */

    public ServerCommand () {

    }

    public ServerCommand (String idServerIn) {

          this.idServer = idServerIn;

    }


    /** 
     * Get- and Set-methods for persistent variables. The default
     * behaviour does not make any checks against malformed data,
     * so these might require some manual additions.
     */

    public String getIdServer() {
          return this.idServer;
    }
    public void setIdServer(String idServerIn) {
          this.idServer = idServerIn;
    }

    public String getIdCommand() {
          return this.idCommand;
    }
    public void setIdCommand(String idCommandIn) {
          this.idCommand = idCommandIn;
    }



    /** 
     * setAll allows to set all persistent variables in one method call.
     * This is useful, when all data is available and it is needed to 
     * set the initial state of this object. Note that this method will
     * directly modify instance variales, without going trough the 
     * individual set-methods.
     */

    public void setAll(String idServerIn,
    		String idCommandIn) {
          this.idServer = idServerIn;
          this.idCommand = idCommandIn;
    }


    /** 
     * hasEqualMapping-method will compare two ServerCommand instances
     * and return true if they contain same values in all persistent instance 
     * variables. If hasEqualMapping returns true, it does not mean the objects
     * are the same instance. However it does mean that in that moment, they 
     * are mapped to the same row in database.
     */
    public boolean hasEqualMapping(ServerCommand valueObject) {

          if (this.idServer == null) {
                    if (valueObject.getIdServer() != null)
                           return(false);
          } else if (!this.idServer.equals(valueObject.getIdServer())) {
                    return(false);
          }
          if (this.idCommand == null) {
                    if (valueObject.getIdCommand() != null)
                           return(false);
          } else if (!this.idCommand.equals(valueObject.getIdCommand())) {
                    return(false);
          }

          return true;
    }



    /**
     * toString will return String object representing the state of this 
     * valueObject. This is useful during application development, and 
     * possibly when application is writing object states in textlog.
     */
    public String toString() {
        StringBuffer out = new StringBuffer(this.getDaogenVersion());
        out.append("\nclass ServerCommand, mapping to table TB_SERVER_COMMAND\n");
        out.append("Persistent attributes: \n"); 
        out.append("idServer = " + this.idServer + "\n"); 
        out.append("idCommand = " + this.idCommand + "\n"); 
        return out.toString();
    }


    /**
     * Clone will return identical deep copy of this valueObject.
     * Note, that this method is different than the clone() which
     * is defined in java.lang.Object. Here, the retuned cloned object
     * will also have all its attributes cloned.
     */
    public Object clone() {
        ServerCommand cloned = new ServerCommand();

        if (this.idServer != null)
             cloned.setIdServer(this.idServer); 
        if (this.idCommand != null)
             cloned.setIdCommand(this.idCommand); 
        return cloned;
    }



    /** 
     * getDaogenVersion will return information about
     * generator which created these sources.
     */
    public String getDaogenVersion() {
        return "DaoGen version 2.4.1";
    }

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

}

