package com.mxdeploy.api.dao;

import java.util.HashMap;
import java.util.List;

import com.mxdeploy.api.dao.xml.DAOException;
import com.mxdeploy.api.domain.SimpleDataStructure;

public interface SimpleDataStructureDAO {
	
	public void save(SimpleDataStructure domain) throws DAOException;

	public List<SimpleDataStructure> loadAll();

	public void saveInstance(SimpleDataStructure dataStructure,
			HashMap<String, Object> instance) throws DAOException;

	List<HashMap<String, Object>> loadAllInstances(
			SimpleDataStructure dataStructure);

}
