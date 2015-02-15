package com.mxdeploy.api.domain;

import java.util.ArrayList;
import java.util.List;


public class Profile {

	private String name;
	private String cell;
	private String node;
	private String path;
	private String transportBufferSize;
	private List<ApplicationServer> applicationServer = new ArrayList<ApplicationServer>();
	private List<Coregroup> coregroups = new ArrayList<Coregroup>();
	private List<Node> nodes  = new ArrayList<Node>();
	
	/**
	 * @return the nodes
	 */
	public List<Node> getNodes() {
		return nodes;
	}
	/**
	 * @param nodes the nodes to set
	 */
	public void addNodes(Node node) {
		this.nodes.add(node);
	}
	
	/**
	 * @return the applicationServer
	 */
	public List<ApplicationServer> getApplicationServerList() {
		return applicationServer;
	}
	/**
	 * @param applicationServer the applicationServer to set
	 */
	public void addApplicationServer(ApplicationServer applicationServer) {
		this.applicationServer.add(applicationServer);
	}
	/**
	 * @return the cell
	 */
	public String getCell() {
		return cell;
	}
	/**
	 * @param cell the cell to set
	 */
	public void setCell(String cell) {
		this.cell = cell;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the node
	 */
	public String getNode() {
		return node;
	}
	/**
	 * @param node the node to set
	 */
	public void setNode(String node) {
		this.node = node;
	}
	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}	
	
	public List<Coregroup> getCoregroup() {
		return coregroups;
	}
	/**
	 * @param nodes the nodes to set
	 */
	public void addCoregroup(Coregroup coregroup) {
		this.coregroups.add(coregroup);
	}
	/**
	 * @return the transportBufferSize
	 */
	public String getTransportBufferSize() {
		return transportBufferSize;
	}
	/**
	 * @param transportBufferSize the transportBufferSize to set
	 */
	public void setTransportBufferSize(String transportBufferSize) {
		this.transportBufferSize = transportBufferSize;
	}	
	
}
