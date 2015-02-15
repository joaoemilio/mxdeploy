package com.mxdeploy.api.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import com.mxdeploy.api.domain.Database;
import com.mxdeploy.api.domain.Version;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class Util { 

	public static Version getNewVersion(){
		String path = Database.HOME+"/version.xml";
		
		XStream xstream = new XStream(new DomDriver());
		Version version = null;
		try {
			version = (Version)xstream.fromXML(new FileReader(path ));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return version;
	}
	
	public static String getURL(){
		Properties sysProps = new Properties();
		
		String path = Database.HOME+"/updater.properties";
		
		FileInputStream finp;
		try {
			finp = new FileInputStream(path);
			sysProps.load(finp);
			return sysProps.getProperty("url");			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  
		
		return null;
	}	

	public static String getCurrVersion(){
		Properties sysProps = new Properties();
		
		String path = Database.HOME+"/version.properties";
		
		FileInputStream finp;
		try {
			finp = new FileInputStream(path);
			sysProps.load(finp);
			return sysProps.getProperty("version");			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  
		
		return null;
	}	
	

}
