package com.mxdeploy.api.domain;

import java.io.Serializable;

public class WindowsService implements Cloneable, Serializable {

    /** 
     * Persistent Instance variables. This data is directly 
     * mapped to the columns of database table.
     */
    private String id;
    private String  name;
    private String  description;
    private String serverId;

    public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	/** 
     * Constructors. DaoGen generates two constructors by default.
     * The first one takes no arguments and provides the most simple
     * way to create object instance. The another one takes one
     * argument, which is the primary key of the corresponding table.
     */

    public WindowsService () {

    }

    public WindowsService (String idIn) {

          this.id = idIn;

    }


    /** 
     * Get- and Set-methods for persistent variables. The default
     * behaviour does not make any checks against malformed data,
     * so these might require some manual additions.
     */

    public String getId() {
          return this.id;
    }
    public void setId(String idIn) {
          this.id = idIn;
    }

    public String getName() {
          return this.name;
    }
    public void setName(String nameIn) {
          this.name = nameIn;
    }

    public String getDescription() {
          return this.description;
    }
    public void setDescription(String description) {
          this.description = description;
    }

}


