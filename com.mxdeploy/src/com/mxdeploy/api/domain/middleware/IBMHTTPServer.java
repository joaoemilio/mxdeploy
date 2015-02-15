package com.mxdeploy.api.domain.middleware;

import com.mxdeploy.api.domain.TransferObject;


public class IBMHTTPServer extends TransferObject {

	public UserID getProcessID() {
		return processID;
	}

	public void setProcessID(UserID processID) {
		this.processID = processID;
	}

	private int fixPackLevel;
	private String serverRoot;
	private String version;
	private UserID processID;
	private String gskitVersion;
	private IHSConfig ihsConfig;

	public void setFixPackLevel(int fixPackLevel) {
		this.fixPackLevel = fixPackLevel;
	}

	public int getFixPackLevel() {
		return fixPackLevel;
	}

	public void setServerRoot(String serverRoot) {
		this.serverRoot = serverRoot;
	}

	public String getServerRoot() {
		return serverRoot;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getVersion() {
		return version;
	}

	public void setProcessID(String processID, String userIDGroups) {
		if(this.processID == null){
			this.processID = new UserID();
		}
		this.processID.setGroups(userIDGroups);
		this.processID.setName(processID);
	}

	public void setGskitVersion(String gskitVersion) {
		this.gskitVersion = gskitVersion;
	}

	public String getGskitVersion() {
		return gskitVersion;
	}
	
	public void setIHSConfig(IHSConfig config){
		this.ihsConfig = config;
	}
	
	public boolean isServerStatusEnabled(){
		return this.ihsConfig.isServerStatusEnabled();
	}
	
	
}
