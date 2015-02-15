package com.mxdeploy.api.domain.operatingsystem;

public class GSAPermission {

	private String name;
	private String permission;
	private String type;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}
	
	public void parsePermission(String perm) {
		String aux[] = perm.split(":");
		if(aux != null && aux.length >= 3){ 
			this.setType(aux[0]);
			this.setName(aux[1]);
			this.setPermission(aux[2]);
		}
	}
	
}
