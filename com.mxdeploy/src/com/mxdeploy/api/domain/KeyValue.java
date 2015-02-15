package com.mxdeploy.api.domain;

public class KeyValue implements Comparable {
	
	private String name;
	private String value;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public int compareTo(Object arg0) {
		KeyValue k1 = (KeyValue)arg0;
		
		return this.getName().compareTo(k1.getName());
	}
	
	

}
