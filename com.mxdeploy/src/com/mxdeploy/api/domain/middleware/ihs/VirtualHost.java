package com.mxdeploy.api.domain.middleware.ihs;

import java.util.ArrayList;
import java.util.List;

public class VirtualHost {

	private String sslEnabled;
	private String port;
	private String documentRoot;
	private List<LogConfig> logConfigs = new ArrayList<LogConfig>();
	
	public String getDocumentRoot() {
		return documentRoot;
	}

	public void setDocumentRoot(String documentRoot) {
		this.documentRoot = documentRoot;
	}

	public void addLogConfig(LogConfig logConfig){
		logConfigs.add(logConfig);
	}
	
	public List<LogConfig> getLogConfigs() {
		return logConfigs;
	}

	public void setLogConfigs(List<LogConfig> logConfigs) {
		this.logConfigs = logConfigs;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}
	private List<String> sslCiphers = new ArrayList<String>();
	private List<String> rewrite = new ArrayList<String>();
	private List<String> others = new ArrayList<String>();
	
	public void addSSLCipher(String cipher){
		sslCiphers.add(cipher);
	}
	
	public void addOther(String other){
		others.add(other);
	}
	
	public String getSslEnabled() {
		return sslEnabled;
	}
	public void setSslEnabled(String sslEnabled) {
		this.sslEnabled = sslEnabled;
	}
	public List<String> getSslCiphers() {
		return sslCiphers;
	}
	public void setSslCiphers(List<String> sslCiphers) {
		this.sslCiphers = sslCiphers;
	}
	public List<String> getRewrite() {
		return rewrite;
	}

	public void setRewrite(List<String> rewrite) {
		this.rewrite = rewrite;
	}

	public void setOthers(List<String> others) {
		this.others = others;
	}

	public List<String> getOthers() {
		return others;
	}
	public void setOther(List<String> others) {
		this.others = others;
	}
	
}
