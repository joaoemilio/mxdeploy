package com.mxdeploy.api.domain.operatingsystem;

public class UnixFileSystem {
	String name;
	String _1024blocks;
	String free;
	String used;
	String iused;
	String percentIused;
	String mountedOn;
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getUsed() {
		return this.used;
	}
	
	public void setUsed(String used) {
		this.used = used;
	}
	
	public String getMountedOn(){
		return this.mountedOn;
	}
	
	public void setMountedOn(String mountedOn) {
		this.mountedOn = mountedOn;
	}

}
