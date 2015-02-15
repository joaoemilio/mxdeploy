package com.mxdeploy.api.domain.operatingsystem;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class Config {
	
	public static final String TEMP_DIR = "TEMP_DIR";
	public static final String GSA_ROOT = "GSA_ROOT";
	
	private static Config me; 
	private String tempDir;
	private String gsaRoot;
	
	private Config(){
		//URL url = Config.class.getResource("/config/config.properties");
		Properties props = new Properties();
		props.setProperty(TEMP_DIR, "/tmp/");
			//props.load(url.openStream());
			this.tempDir = props.getProperty(TEMP_DIR);
			if(this.tempDir == null){
				System.err.println("Error while getting temp directory information. Exiting application.");
				System.exit(-1);
			}
			
			this.gsaRoot = props.getProperty(GSA_ROOT);
			System.out.println("GSA ROOT: " + this.gsaRoot);
	}
	
	public static Config getInstance(){
		if(me == null){
			me = new Config();
		}
		return me;
	}

	public String getTempDir(){
		return this.tempDir;
	}
	
	public String getIDOwner(){
		return "root";
	}
	
	public String getGroupOwner(){
		return "root";
	}

	public String getGsaRoot() {
		return gsaRoot;
	}

	public void setGsaRoot(String gsaRoot) {
		this.gsaRoot = gsaRoot;
	}

	public void setTempDir(String tempDir) {
		this.tempDir = tempDir;
	}
	
}
