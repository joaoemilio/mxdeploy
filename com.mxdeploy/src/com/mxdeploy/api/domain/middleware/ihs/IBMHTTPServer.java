package com.mxdeploy.api.domain.middleware.ihs;

import com.mxdeploy.api.domain.TransferObject;
import com.mxdeploy.api.domain.operatingsystem.UserID;

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
	private String runAsUser;
	private String runAsGroup;
	private String documentRoot;
	
	public String getConfFileDir() {
		String confFileDir = this.ihsConfig.getPath();
		int endIndex = confFileDir.lastIndexOf("/");
		confFileDir = confFileDir.substring(0, endIndex);
		return confFileDir;
	}
	
	public IHSConfig getIhsConfig() {
		return ihsConfig;
	}

	public void setIhsConfig(IHSConfig ihsConfig) {
		this.ihsConfig = ihsConfig;
	}

	public String getDocumentRoot() {
		return documentRoot;
	}

	public void setDocumentRoot(String documentRoot) {
		this.documentRoot = documentRoot;
	}

	public String getRunAsUser() {
		return runAsUser;
	}

	public void setRunAsUser(String runAsUser) {
		this.runAsUser = runAsUser;
	}

	public String getRunAsGroup() {
		return runAsGroup;
	}

	public void setRunAsGroup(String runAsGroup) {
		this.runAsGroup = runAsGroup;
	}

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
	
	public IHSConfig getIHSConfig(){
		return this.ihsConfig;
	}
	
	public boolean isServerStatusEnabled(){
		return this.ihsConfig.isServerStatusEnabled();
	}

}
