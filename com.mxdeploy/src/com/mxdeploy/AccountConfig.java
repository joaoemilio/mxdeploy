package com.mxdeploy;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class AccountConfig {
	
	private static AccountConfig me;
	private String name;
	private String description;
	private SocksConfig activeSocks;
	private List<SocksConfig> socksList = new ArrayList<SocksConfig>();
	private boolean connectViaIP = false;
	 
	/*
	 * this is a simulation of a singleton pattern, but it is needed 
	 * a constructor in order to grant access to Xstream to load the xml
	 * as an object
	 */
	public static void setInstance(AccountConfig acfg) {
		AccountConfig.me = acfg;
	}
	
	public static AccountConfig getInstance() {
		return me;
	}
	
	public void addSocksConfig(SocksConfig config) {
		socksList.add(config);
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<SocksConfig> getSocksList() {
		return socksList;
	}

	public void setSocksList(List<SocksConfig> socksList) {
		this.socksList = socksList;
	}
	
	public SocksConfig getActiveSocks() {
		return this.activeSocks;
	}
	
	public void setActiveSocks(SocksConfig cfg) {
		this.activeSocks = cfg;
	}

	public boolean isConnectViaIP() {
		return connectViaIP;
	}

	public void setConnectViaIP(boolean connectViaIP) {
		this.connectViaIP = connectViaIP;
	}
	
	public SocksConfig getSocksConfigByUUID(String uuid) {
		SocksConfig result = null;
		for(SocksConfig cfg: socksList) {
			if(cfg.getUuid().equals(uuid)){
				result = cfg;
				break;
			}
		}
		return result;
	}

}
