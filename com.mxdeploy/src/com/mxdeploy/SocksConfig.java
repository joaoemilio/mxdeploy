package com.mxdeploy;

import java.util.UUID;

public class SocksConfig {
	
	private String uuid;
	private String name;
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}



	private String socksProxyHost;
	private Boolean authenticationRequired;
	private Boolean enabled = false;
	private String username;
	private String encodedPassword;
	
	public String getEncondedPassword() {
		return encodedPassword;
	}

	public void setEncodedPassword(String encodedPassword) {
		this.encodedPassword = encodedPassword;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getAuthenticationRequired() {
		return authenticationRequired;
	}

	public void setAuthenticationRequired(Boolean authenticationRequired) {
		this.authenticationRequired = authenticationRequired;
	}

	public String getSocksProxyHost() {
		return socksProxyHost;
	}

	public void setSocksProxyHost(String socksProxyHost) {
		this.socksProxyHost = socksProxyHost;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

	public static void main(String[] args) {
		/**
		 * 
		 
		XStream xstream = new XStream(new DomDriver());
		try {
			SocksConfig config = new SocksConfig();
			config.setAuthenticationRequired(true);
			config.setName("AZL");
			config.setSocksProxyHost("167.210.219.60");
			config.setUsername("");
			config.setEncodedPassword("");
			xstream.toXML(config, new FileWriter("c:/MXTerminal/config.xml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		
		System.out.println(UUID.randomUUID());
	}
 
}
