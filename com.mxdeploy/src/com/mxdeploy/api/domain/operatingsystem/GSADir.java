package com.mxdeploy.api.domain.operatingsystem;

import java.util.ArrayList;
import java.util.List;

public class GSADir {

	private List<GSAFile> files = new ArrayList<GSAFile>();
	private List<GSAPermission> permissions = new ArrayList<GSAPermission>();
	private List<GSADir> directories = new ArrayList<GSADir>();
	private String path;
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void addGSAFile(GSAFile gsaFile){
		files.add(gsaFile);
	}
	
	public void addGSADir(GSADir gsaDir){
		directories.add(gsaDir);
	}
	
	public void addPermission(GSAPermission permission){
		permissions.add(permission);
	}
	
	public List<GSAFile> getFiles() {
		return files;
	}
	public void setFiles(List<GSAFile> files) {
		this.files = files;
	}
	public List<GSAPermission> getPermissions() {
		return permissions;
	}
	public void setPermissions(List<GSAPermission> permissions) {
		this.permissions = permissions;
	}
	public List<GSADir> getDirectories() {
		return directories;
	}
	public void setDirectories(List<GSADir> directories) {
		this.directories = directories;
	}
	
	
	
}
