package com.mxdeploy.api.domain.middleware.ihs;

import java.util.ArrayList;
import java.util.List;

public class Location {

	protected String setHandler;
	protected String order;
	protected String deny;
	protected String allow;
	protected List<String> rewrites = new ArrayList<String>();
	protected String ldapConfigFile;
	protected String authName;
	protected String authType;
	protected String require;
	protected String satisfy;
	protected String uri;
	protected boolean serverStatusEnabled;
	protected String options;
	
	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public boolean isServerStatusEnabled() {
		return serverStatusEnabled;
	}

	public void setServerStatusEnabled(boolean serverStatusEnabled) {
		this.serverStatusEnabled = serverStatusEnabled;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getSatisfy() {
		return satisfy;
	}

	public void setSatisfy(String satisfy) {
		this.satisfy = satisfy;
	}

	public void addRewrite(String value){
		rewrites.add(value);
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
	public List<String> getRewrites() {
		return rewrites;
	}
	public void setRewrites(List<String> rewrites) {
		this.rewrites = rewrites;
	}
	public String getLdapConfigFile() {
		return ldapConfigFile;
	}
	public void setLdapConfigFile(String ldapConfigFile) {
		this.ldapConfigFile = ldapConfigFile;
	}
	public String getAuthName() {
		return authName;
	}
	public void setAuthName(String authName) {
		this.authName = authName;
	}
	public String getAuthType() {
		return authType;
	}
	public void setAuthType(String authType) {
		this.authType = authType;
	}
	public String getRequire() {
		return require;
	}
	public void setRequire(String require) {
		this.require = require;
	}
	
}
