package com.mxdeploy.api.domain.operatingsystem;

import java.util.ArrayList;
import java.util.List;

public class UnixGroup {

	private String name;
	private String sudoers;
	private List<UnixUser> users;

	public void addUser(UnixUser user){
		if(users == null){
			users = new ArrayList<UnixUser>();
		}
		
		users.add(user);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSudoers() {
		return sudoers;
	}
	public void setSudoers(String sudoers) {
		this.sudoers = sudoers;
	}
	public List<UnixUser> getUsers() {
		return users;
	}
	public void setUsers(List<UnixUser> users) {
		this.users = users;
	}
	
	
	
}
