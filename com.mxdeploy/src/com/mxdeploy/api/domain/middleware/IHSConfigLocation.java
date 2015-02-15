package com.mxdeploy.api.domain.middleware;

import com.mxdeploy.api.domain.TransferObject;


public class IHSConfigLocation extends TransferObject {
	private String name;
	private String setHandler;
	private String order;
	private String deny;
	private String allow;
	private boolean serverStatusEnabled;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSetHandler() {
		return setHandler;
	}
	public void setSetHandler(String setHandler) {
		this.setHandler = setHandler;
		if(setHandler.contains("server-status")){
			setServerStatusEnabled(true);
		}
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getDeny() {
		return deny;
	}
	public void setDeny(String deny) {
		this.deny = deny;
	}
	public String getAllow() {
		return allow;
	}
	public void setAllow(String allow) {
		this.allow = allow;
	}
	public void setServerStatusEnabled(boolean serverStatusEnabled) {
		this.serverStatusEnabled = serverStatusEnabled;
	}
	public boolean isServerStatusEnabled() {
		return serverStatusEnabled;
	}

}
