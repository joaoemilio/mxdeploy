package com.mxscript;

import java.io.Serializable;

public class MXBSMessage implements Serializable{

	public static final Integer LOG = 1;
	public static final Integer OBJECT = 2;
	public static final Integer GRID = 3;
	public static final Integer STATUS = 4;
	
	private Integer type;
	private String key;
	private Object data;
	
	public MXBSMessage(Integer type, String key, Object data) {
		this.key = key;
		this.data = data;
		this.type = type;
	}
	
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	
	
}
