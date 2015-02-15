package com.mxdeploy.api.domain;

import java.util.ArrayList;
import java.util.List;

public class Coregroup {

	public String name;
	public String IBM_CS_WIRE_FORMAT_VERSION;
	public String IBM_CS_DATASTACK_ME;
	public List<CoregroupServer> coregroupServers = new ArrayList<CoregroupServer>();
	
	/**
	 * @return the coregroupServer
	 */
	public List<CoregroupServer> getCoregroupServer() {
		return coregroupServers;
	}
	
	/**
	 * @param coregroupServer the coregroupServer to set
	 */
	public void addCoregroupServer(CoregroupServer coregroupServer) {
		this.coregroupServers.add( coregroupServer );
	}
	
	/**
	 * @return the iBM_CS_DATASTACK_ME
	 */
	public String getDatastackMe() {
		return IBM_CS_DATASTACK_ME;
	}
	
	/**
	 * @param ibm_cs_datastack_me the iBM_CS_DATASTACK_ME to set
	 */
	public void setDatastackMe(String ibm_cs_datastack_me) {
		IBM_CS_DATASTACK_ME = ibm_cs_datastack_me;
	}
	
	/**
	 * @return the iBM_CS_WIRE_FORMAT_VERSION
	 */
	public String getWireFormatVersion() {
		return IBM_CS_WIRE_FORMAT_VERSION;
	}
	/**
	 * @param ibm_cs_wire_format_version the iBM_CS_WIRE_FORMAT_VERSION to set
	 */
	public void setWireFormatVersion(String ibm_cs_wire_format_version) {
		IBM_CS_WIRE_FORMAT_VERSION = ibm_cs_wire_format_version;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
