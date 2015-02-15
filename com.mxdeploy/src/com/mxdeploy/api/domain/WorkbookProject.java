package com.mxdeploy.api.domain;

public class WorkbookProject {
	
    private String id;
    private String id_project;	
	private String name;
	private String nodes;
	private String webservers;
	private String vertical="0";
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdProject() {
		return id_project;
	}

	public void setIdProject(String id_project) {
		this.id_project = id_project;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNodes() {
		return nodes;
	}

	public void setNodes(String nodes) {
		this.nodes = nodes;
	}

	public String getWebservers() {
		return webservers;
	}

	public void setWebservers(String webservers) {
		this.webservers = webservers;
	}

	public String getVertical() {
		return vertical;
	}

	public void setVertical(String vertical) {
		this.vertical = vertical;
	}
	
	

}
