package com.mxdeploy.api.domain;

import java.io.Serializable;

public class WindowsServer implements Cloneable, Serializable {

    /** 
     * Persistent Instance variables. This data is directly 
     * mapped to the columns of database table.
     */
    private String  domain;
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

    public WindowsServer () {

    }

    public WindowsServer (String serverId) {

          this.serverId = serverId;

    }

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}


    /** 
     * Get- and Set-methods for persistent variables. The default
     * behaviour does not make any checks against malformed data,
     * so these might require some manual additions.
     */


}


