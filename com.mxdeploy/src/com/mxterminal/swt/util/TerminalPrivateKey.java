package com.mxterminal.swt.util;

public class TerminalPrivateKey {

    private String id;
    private String username;
    private String privateKey;
    private String regexp;
    
	public String getId() {
		return id;
	}
	public String getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
	public String getRegexp() {
		return regexp;
	}
	public void setRegexp(String regexp) {
		this.regexp = regexp;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setId(String id) {
		this.id = id;
	}
    
    

}
