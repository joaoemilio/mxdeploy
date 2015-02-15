package com.mxdeploy.api.domain;

import java.util.List;
import java.util.UUID;

import com.mxdeploy.api.domain.TransferObject;

public class BeanShellProject extends TransferObject {

	private UUID uuid;
	private String name;
	private String description;
	private String longDescription;
	private BeanShell mainBeanShell;
	private List<BeanShell> BeanShellFiles;
	private List<Resource> resources;
	private BeanShellProjectType type;
	
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
	public List<BeanShell> getBeanShellFiles() {
		return BeanShellFiles;
	}
	public void setBeanShellFiles(List<BeanShell> beanShellFiles) {
		BeanShellFiles = beanShellFiles;
	}
	public BeanShell getMainBeanShell() {
		return mainBeanShell;
	}
	public void setMainBeanShell(BeanShell mainBeanShell) {
		this.mainBeanShell = mainBeanShell;
	}
	public List<Resource> getResources() {
		return resources;
	}
	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}
	public BeanShellProjectType getType() {
		return type;
	}
	public void setType(BeanShellProjectType type) {
		this.type = type;
	}

	
	
}
