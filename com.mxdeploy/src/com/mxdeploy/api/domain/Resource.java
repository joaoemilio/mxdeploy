package com.mxdeploy.api.domain;

import java.util.UUID;

import com.mxdeploy.api.domain.TransferObject;

public class Resource extends TransferObject {

	private UUID uuid;
	private String name;
	private String description;
	private String longDescription;
	private String path;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLongDescription() {
		return longDescription;
	}
	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	
}
