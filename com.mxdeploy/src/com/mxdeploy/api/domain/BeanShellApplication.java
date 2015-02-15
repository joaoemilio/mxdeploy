package com.mxdeploy.api.domain;

import java.util.List;
import java.util.UUID;

import com.mxdeploy.api.domain.TransferObject;

public class BeanShellApplication extends TransferObject {

	public static final String BEANSHELLAPPLICATIONS = "beanshellapplications";
	
	private UUID uuid;
	private String name;
	private String description;
	private String longDescription;
	private List<BeanShell> BeanShellFiles;
	private List<Image> screenShot;
	
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
	public List<Image> getScreenShot() {
		return screenShot;
	}
	public void setScreenShot(List<Image> screenShot) {
		this.screenShot = screenShot;
	}
	
	
}
