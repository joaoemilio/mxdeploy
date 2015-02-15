package com.mxdeploy.api.domain;

import java.util.UUID;

import com.mxdeploy.api.domain.TransferObject;

public class BeanShellProjectType extends TransferObject {

	private UUID uuid;
	private String name;
	
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
	
}
