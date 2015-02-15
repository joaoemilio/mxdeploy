package com.mxscript.swt.event;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.apache.log4j.Logger;

import xml.modulegroups;

import com.mxdeploy.api.domain.Database;
import com.mxdeploy.plugin.event.EventHandler;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class LoadModuleViewerEvent implements EventHandler { 

	static Logger logger = Logger.getLogger(LoadModuleViewerEvent.class);
	
	public void execute() {
		File file = null; 
		try{
			file = new File(Database.MODULE_PATH);
			if ( !file.exists() ){
				 logger.info("Can not load Module Viewer, because Module Path doesn't exist");
				 return;
			}
			file = new File(Database.MODULE_GROUP_FILE);
			if ( !file.exists() ){
				 logger.info("Can not load Module Viewer, because the file module-groups.xml doesn't exist");
				 return;
			}	
			
			modulegroups _modulegroups_ = loadXML();
			Database.getInstance().set_modules(_modulegroups_);
		} finally{

		}
	}

	
	public modulegroups loadXML() { 
		modulegroups _modulegroups_;
		XStream xstream = new XStream(new DomDriver());
		try {
			_modulegroups_ = (modulegroups) xstream.fromXML(new FileReader( Database.MODULE_GROUP_FILE));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return _modulegroups_;
	}	

}
