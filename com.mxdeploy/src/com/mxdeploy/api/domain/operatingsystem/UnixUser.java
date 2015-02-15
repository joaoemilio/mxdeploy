package com.mxdeploy.api.domain.operatingsystem;

import java.util.ArrayList;
import java.util.List;

public class UnixUser {

	private String name;
	private String realName;
	private String id;
	private String defaultGroup;
	private List<UnixGroup> groups;
	private String sudoers;
	
	public void addGroup(UnixGroup group){
		if(groups == null){
			groups = new ArrayList<UnixGroup>();
		}
		
		groups.add(group);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDefaultGroup() {
		return defaultGroup;
	}
	public void setDefaultGroup(String defaultGroup) {
		this.defaultGroup = defaultGroup;
	}
	public List<UnixGroup> getGroups() {
		return groups;
	}
	public void setGroups(List<UnixGroup> groups) {
		this.groups = groups;
	}
	public String getSudoers() {
		return sudoers;
	}
	public void setSudoers(String sudoers) {
		this.sudoers = sudoers;
	}
	
	public boolean isGroupMember(String group) {
		boolean result = false;
		if(this.defaultGroup.equals(group)) {
			System.out.println("user: " + this.getId() + "'s default group is: " + this.defaultGroup);
			return true;
		}
		
		for(UnixGroup ugroup: this.groups){
			System.out.println("user: " + this.getId() + " - group: " + ugroup.getName() );
			if(	ugroup.getName().equals(group) ) {
				System.out.println("user: " + this.getId() + " is part of group: " + ugroup.getName() );
				result = true;
				break;
			}
		}
		return result;
	}
	
}
