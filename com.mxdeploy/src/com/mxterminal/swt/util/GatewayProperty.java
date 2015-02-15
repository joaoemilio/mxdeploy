package com.mxterminal.swt.util;

public class GatewayProperty {

    private String hostname = null; 	
    private String dinamicPort = null;
    private String destPort = null;
    private String proxyHost = null;
    private String proxyPort = null;
    private String puttySession = null;

    private String username  = null;
    private String privateKey = null;
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public String getDinamicPort() {
		return dinamicPort;
	}
	public void setDinamicPort(String dinamicPort) {
		this.dinamicPort = dinamicPort;
	}
	public String getDestPort() {
		return destPort;
	}
	public void setDestPort(String destPort) {
		this.destPort = destPort;
	}
	public String getProxyHost() {
		return proxyHost;
	}
	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}
	public String getProxyPort() {
		return proxyPort;
	}
	public void setProxyPort(String proxyPort) {
		this.proxyPort = proxyPort;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
 
}
