package com.mxdeploy.api.domain;

public class Node {

	private String nodeDir;
	private String hostname;
	/**
	 * @return the hostname
	 */
	public String getHostname() {
		return hostname;
	}
	/**
	 * @param hostname the hostname to set
	 */
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	/**
	 * @return the nodeDir
	 */
	public String getNodeDir() {
		return nodeDir;
	}
	/**
	 * @param nodeDir the nodeDir to set
	 */
	public void setNodeDir(String nodeDir) {
		this.nodeDir = nodeDir;
	}
	
}
