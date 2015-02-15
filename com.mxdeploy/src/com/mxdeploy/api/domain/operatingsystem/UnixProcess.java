package com.mxdeploy.api.domain.operatingsystem;

public class UnixProcess {
	String name;
	String id;
	String details;
	Double cpuUsage;
	private String owner;
	Double memUsage;
	
	public Double getMemUsage() {
		return memUsage;
	}

	public void setMemUsage(Double memUsage) {
		this.memUsage = memUsage;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getName() {
		return this.name;
	}
	
	public String getId() {
		return this.id;
	}
	
	public String getDetails() {
		return this.details;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setDetails(String details) {
		this.details = details;
	}
	
	public Double getCpuUsage() {
		return this.cpuUsage;
	}
	
	public void setCpuUsage(Double cpuUsage) {
		this.cpuUsage = cpuUsage;
	}

}
