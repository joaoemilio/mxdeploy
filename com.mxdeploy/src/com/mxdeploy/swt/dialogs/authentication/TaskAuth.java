package com.mxdeploy.swt.dialogs.authentication;

public class TaskAuth {

	
	private String username;
	private String password;
	private String hostname;
	private int    indexItem;
	private Boolean isFirst;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public int getIndexItem() {
		return indexItem;
	}
	public void setIndexItem(int indexItem) {
		this.indexItem = indexItem;
	}
	
	public Boolean isFirst(){
		return this.isFirst;
	}
	
	public void setFirst(Boolean isFirst){
		this.isFirst = isFirst;
	}
}
