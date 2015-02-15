package com.mxdeploy.api.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import xml.properties;

import com.mxdeploy.api.domain.Database;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;


public class PropertyService {
	
	public properties loadPropertyXML() { 
		String filePath = Database.getBeanShellScriptPath();
		
		filePath = filePath.replace(".bsh", ".xml");
		File file = new File(filePath);
		if( file.exists() ){
			properties _properties_;
			XStream xstream = new XStream(new DomDriver());
			try {
				_properties_ = (properties) xstream.fromXML(new FileReader( filePath ) );
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			}
			return _properties_;
		} else {
			return null;
		}
	}
	
    public void writeXML(properties valueObject ){
    	
		String filePath = Database.getBeanShellScriptPath();
		
		filePath = filePath.replace(".bsh", ".xml");
		XStream xstream = new XStream(new DomDriver());
		try {
			xstream.toXML(valueObject, new FileWriter( filePath ) );
		} catch (IOException e) {
			e.printStackTrace();
		}
    }		

}
