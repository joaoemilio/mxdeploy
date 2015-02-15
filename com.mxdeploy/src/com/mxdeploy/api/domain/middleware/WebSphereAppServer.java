package com.mxdeploy.api.domain.middleware;


public class WebSphereAppServer extends Middleware {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int fixPackLevel;
	private String wasHome;
	private String version;
	private UserID processID;
	private String maintPack;
	
	public String getMaintPack(){
		return this.maintPack;
	}
	
	public void setMaintPack(String value){
		this.maintPack = value;
	}

	public UserID getProcessID() {
		return processID;
	}

	public void setProcessID(UserID processID) {
		this.processID = processID;
	}

	public void setFixPackLevel(int fixPackLevel) {
		this.fixPackLevel = fixPackLevel;
	}

	public int getFixPackLevel() {
		return fixPackLevel;
	}

	public void setWasHome(String wasHome) {
		this.wasHome = wasHome;
	}

	public String getWasHome() {
		return wasHome;
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
	
	public boolean isMaintPackInstalled(String pk){
		return maintPack.contains(pk);
	}

}
