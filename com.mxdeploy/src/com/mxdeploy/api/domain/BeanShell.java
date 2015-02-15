package com.mxdeploy.api.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.mxdeploy.api.domain.TransferObject;

public class BeanShell extends TransferObject {
	
	private UUID uuid;
	private String name;
	private String scriptName;
	private String folder;
	private List<Property> properties;
	
	public String getFolder() {
		return folder;
	}
	public void setFolder(String folder) {
		this.folder = folder;
	}
	public UUID getUuid() {
		return uuid;
	}
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Property> getProperties() {
		return properties;
	}
	public void addProperties(Property property) {
		if( this.properties==null ){
			this.properties = new ArrayList<Property>();
		}
		this.properties.add(property);
	}
	public String getScriptName() {
		return scriptName;
	}
	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}
}
