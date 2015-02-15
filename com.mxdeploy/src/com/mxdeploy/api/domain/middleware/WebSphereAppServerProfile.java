package com.mxdeploy.api.domain.middleware;

import com.mxdeploy.api.domain.TransferObject;

public class WebSphereAppServerProfile extends TransferObject {
	
	protected String path;
	protected String name;
	protected String cellName;
	protected String serverID;
	protected String serverIDPassword;
	protected String soapPort;
	
	public String getCellName() {
		return cellName;
	}
	public void setCellName(String cellName) {
		this.cellName = cellName;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getServerID() {
		return serverID;
	}
	public void setServerID(String serverID) {
		this.serverID = serverID;
	}
	public String getServerIDPassword() {
		return serverIDPassword;
	}
	public void setServerIDPassword(String serverIDPassword) {
		this.serverIDPassword = serverIDPassword;
	}
	public String getSoapPort() {
		return soapPort;
	}
	public void setSoapPort(String soapPort) {
		this.soapPort = soapPort;
	}

	
}
