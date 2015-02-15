package com.mxdeploy.api.domain.operatingsystem;

import java.util.ArrayList;
import java.util.List;

public class GSAFile {
	private String path;
	private String name;
	private List<GSAPermission> listPermission = new ArrayList<GSAPermission>();
	private String owner;
	private String group;
	private String perm;
	
	public String getPerm() {
		return perm;
	}

	public void setPerm(String perm) {
		this.perm = perm;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public void addPermission(GSAPermission permission){
		listPermission.add(permission);
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<GSAPermission> getListPermission() {
		return listPermission;
	}
	public void setListPermission(List<GSAPermission> listPermission) {
		this.listPermission = listPermission;
	}
	
	
}
