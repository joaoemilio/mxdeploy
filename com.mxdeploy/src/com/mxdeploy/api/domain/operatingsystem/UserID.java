package com.mxdeploy.api.domain.operatingsystem;

import com.mxdeploy.api.domain.TransferObject;

public class UserID extends TransferObject {
	
	private String name;
	private String groups;
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setGroups(String groups) {
		this.groups = groups;
	}
	public String getGroups() {
		return groups;
	}

}
