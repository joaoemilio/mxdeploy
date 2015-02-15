package com.mxdeploy.api.service;

import java.util.HashMap;
import java.util.List;

import com.mxdeploy.api.dao.DAOFactory;
import com.mxdeploy.api.dao.SimpleDataStructureDAO;
import com.mxdeploy.api.domain.SimpleDataStructure;

public class SimpleDataStructureService {

	public void save(SimpleDataStructure domain) {
		try{
			SimpleDataStructureDAO dao = DAOFactory.getInstance().createSimpleDataStructureDAO();
			dao.save(domain);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<SimpleDataStructure> getAll() {
		try{
			SimpleDataStructureDAO dao = DAOFactory.getInstance().createSimpleDataStructureDAO();
			return dao.loadAll();
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<HashMap<String, Object>> getAllInstances(SimpleDataStructure dataStructure) {
		try{
			SimpleDataStructureDAO dao = DAOFactory.getInstance().createSimpleDataStructureDAO();
			return dao.loadAllInstances(dataStructure);
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void saveInstance(SimpleDataStructure dataStructure, HashMap<String, Object> instance) {
		try{
			SimpleDataStructureDAO dao = DAOFactory.getInstance().createSimpleDataStructureDAO();
			dao.saveInstance(dataStructure, instance);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
