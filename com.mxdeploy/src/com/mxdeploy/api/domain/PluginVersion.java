package com.mxdeploy.api.domain;

import java.util.ArrayList;
import java.util.List;

public class PluginVersion {
	
	private String version;
	private String name;
	private List<Dependence> dependenceList = new ArrayList<Dependence>();
	
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
	 * @return the prerequisiteList
	 */
	public List<Dependence> getDependenceList() {
		return dependenceList;
	}
	/**
	 * @param prerequisiteList the prerequisiteList to set
	 */
	public void addDependence(String name) {
		Dependence dependence = new Dependence();
		dependence.setName(name);
		this.dependenceList.add(dependence);
	}
	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

}
