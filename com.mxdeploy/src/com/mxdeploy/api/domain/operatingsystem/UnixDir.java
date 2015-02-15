package com.mxdeploy.api.domain.operatingsystem;

import java.util.ArrayList;
import java.util.List;

public class UnixDir {

	private String permissions;
	private String name;
	private String path;
	private String owner;
	private String group;
	private String lastUpdate;
	private String actualPath;
	private List<UnixFile> files = new ArrayList<UnixFile>();
	private List<UnixDir> directories = new ArrayList<UnixDir>();
	
	public void addFile(UnixFile ufile){
		files.add(ufile);
	}
	
	public List<UnixDir> getDirectories() {
		return directories;
	}
	public void setDirectories(List<UnixDir> directories) {
		this.directories= directories;
	}
	public void addSubDir(UnixDir uDir){
		directories.add(uDir);
	}
	
	public List<UnixFile> getFiles() {
		return files;
	}
	public void setFiles(List<UnixFile> files) {
		this.files = files;
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
	
	public String getActualPath() {
		return actualPath;
	}

	public void setActualPath(String actualPath) {
		this.actualPath = actualPath;
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
	
}
