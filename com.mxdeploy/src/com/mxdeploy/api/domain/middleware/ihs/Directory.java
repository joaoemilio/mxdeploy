package com.mxdeploy.api.domain.middleware.ihs;

public class Directory {

	private String allowOverride;
	private String options;
	private String order;
	private String allow;
	private String addHandler;
	private String setHandler;
	private String path;
	private Boolean allowScripts = false;
	
	public String getSetHandler() {
		return setHandler;
	}
	public void setSetHandler(String setHandler) {
		if(setHandler.equals("cgi-script")){
			this.allowScripts = true;
		}
		this.setHandler = setHandler;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getAddHandler() {
		return addHandler;
	}
	public void setAddHandler(String addHandler) {
		if(addHandler.equals("cgi-script")){
			this.allowScripts = true;
		}
		this.addHandler = addHandler;
	}
	public String getAllowOverride() {
		return allowOverride;
	}
	public void setAllowOverride(String allowOverride) {
		this.allowOverride = allowOverride;
	}
	public String getOptions() {
		return options;
	}
	public void setOptions(String options) {
		this.options = options;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getAllow() {
		return allow;
	}
	public void setAllow(String allow) {
		this.allow = allow;
	}
	
	public boolean allowScripts(){
		return this.allowScripts;
	}
	
}
