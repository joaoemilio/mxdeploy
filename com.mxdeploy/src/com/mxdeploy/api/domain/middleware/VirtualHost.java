package com.mxdeploy.api.domain.middleware;

import java.util.ArrayList;
import java.util.List;

public class VirtualHost {

	private String sslEnabled;
	private List<String> sslCiphers = new ArrayList<String>();
	private List<String> others = new ArrayList<String>();
	
	public void addSSLCipher(String cipher){
		sslCiphers.add(cipher);
	}
	
	public void addOther(String other){
		others.add(other);
	}
	
	public String getSslEnabled() {
		return sslEnabled;
	}
	public void setSslEnabled(String sslEnabled) {
		this.sslEnabled = sslEnabled;
	}
	public List<String> getSslCiphers() {
		return sslCiphers;
	}
	public void setSslCiphers(List<String> sslCiphers) {
		this.sslCiphers = sslCiphers;
	}
	public List<String> getOthers() {
		return others;
	}
	public void setOther(List<String> others) {
		this.others = others;
	}
	
}
