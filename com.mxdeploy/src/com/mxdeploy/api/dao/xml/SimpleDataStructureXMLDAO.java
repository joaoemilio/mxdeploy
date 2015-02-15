package com.mxdeploy.api.dao.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.mxdeploy.api.dao.SimpleDataStructureDAO;
import com.mxdeploy.api.domain.Database;
import com.mxdeploy.api.domain.SimpleDataStructure;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class SimpleDataStructureXMLDAO extends XMLDAO implements SimpleDataStructureDAO {

	public void save(SimpleDataStructure domain) throws DAOException {
		String path = Database.WORKSPACE_PATH + "/" + Database.WORKSPACE_NAME + "/beanshell/simpledatastructure";
		File dir = new File(path);
		if(!dir.isDirectory()) {
			dir.mkdir();
		}
		
		File file = new File( path + "/" + domain.getName() + ".xml");
		this.create(domain, file);
	}

	public void saveInstance(SimpleDataStructure domain, HashMap<String, Object> instance) throws DAOException {
		String path = Database.WORKSPACE_PATH + "/" + Database.WORKSPACE_NAME + "/beanshell/simpledatastructure/" + domain.getName();
		File dir = new File(path);
		if(!dir.isDirectory()) {
			dir.mkdir();
		}
		UUID uuid = UUID.randomUUID();
		File file = new File( path + "/" + uuid.toString() + ".xml");
		this.create(instance, file);
	}

	
	public List<SimpleDataStructure> loadAll() {
		List<SimpleDataStructure> list = new ArrayList<SimpleDataStructure>();
		
		File file = new File(Database.WORKSPACE_PATH+"/"+Database.WORKSPACE_NAME+"/beanshell/simpledatastructure");
		File[] files = file.listFiles();
		if(files == null) {
			return null;
		}
		
		for(int i=0; i < files.length; i++){
			if(!files[i].isDirectory()){
				if(files[i].getName().endsWith(".xml")) {
					
					SimpleDataStructure domain = null;
					XStream xstream = new XStream(new DomDriver());
					try {
						domain = (SimpleDataStructure)xstream.fromXML(new FileInputStream(files[i]));
						list.add(domain);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		return list;
	}
	
	
	public List<HashMap<String, Object>> loadAllInstances(SimpleDataStructure dataStructure) {
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		
		File file = new File(Database.WORKSPACE_PATH+"/"+Database.WORKSPACE_NAME+"/beanshell/simpledatastructure/" + dataStructure.getName() );
		File[] files = file.listFiles();
		if(files == null) {
			return null;
		}
		
		for(int i=0; i < files.length; i++){
			if(!files[i].isDirectory()){
				if(files[i].getName().endsWith(".xml")) {
					
					HashMap<String, Object> domain = null;
					XStream xstream = new XStream(new DomDriver());
					try {
						domain = (HashMap<String, Object>)xstream.fromXML(new FileInputStream(files[i]));
						list.add(domain);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		return list;
	}
	
	
}
