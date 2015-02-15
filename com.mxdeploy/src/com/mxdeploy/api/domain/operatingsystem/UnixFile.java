package com.mxdeploy.api.domain.operatingsystem;

public class UnixFile {

	private String permissions;
	private String name;
	private String path;
	private String owner;
	private String group;
	private String lastUpdate;
	private String actualPath;
	private Double size;
	private String pid;
	private String pidgroup;
	
	public Double getSize() {
		return size;
	}

	public void setSize(Double size) {
		this.size = size;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getPidgroup() {
		return pidgroup;
	}

	public void setPidgroup(String pidgroup) {
		this.pidgroup = pidgroup;
	}

	public String getFullPath() {
		return this.getPath() + "/" + this.getName();
	}
	
	public String getActualPath() {
		return actualPath;
	}
	public void setActualPath(String actualPath) {
		this.actualPath = actualPath;
	}
	public String getPermissions() {
		return permissions;
	}
	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
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

	public String getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	public int hasGeneralUsersWriteAccess(){
		if(this.permissions == null || this.permissions.length() < 10) {
			return -2;
		}
		
		if(this.permissions.substring(0,1).equals("l")){
			return -1;
		}
		
		if(this.permissions.substring(8, 9).equals("w")) {
			return 1;
		}else{
			return 0;
		}
	}
	
	public boolean isDirectory(){
		if(this.permissions == null || this.permissions.length() < 10) {
			return false;
		}
		
		if(this.permissions.substring(0,1).equals("d")){
			return true;
		}else{
			return false;
		}
		
	}
	
	public boolean isSymlink(){
		if(this.permissions == null || this.permissions.length() < 10) {
			return false;
		}
		
		if(this.permissions.substring(0,1).equals("l")){
			return true;
		}else{
			return false;
		}
		
	}
	
	public boolean isWorldWritable(){
		String other = this.permissions.substring(7, 8);
		if(other.equals("w")){
			return true;
		}else{
			return false;
		}
	}
	
}
