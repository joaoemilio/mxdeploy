package com.mxdeploy.api.dao;

import java.io.FileWriter;
import java.io.IOException;

import com.mxdeploy.api.domain.BeanShell;
import com.mxdeploy.api.domain.Database;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class BeanShellDAO {

    private void writeXML(BeanShell bsh ){
		XStream xstream = new XStream(new DomDriver());
		try {
			xstream.toXML(bsh, new FileWriter(Database.getBeanShellPath()+"/"+bsh.getUuid()+".xml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public synchronized void save(BeanShell bsh) {
    	writeXML(bsh);
    }	    
}
