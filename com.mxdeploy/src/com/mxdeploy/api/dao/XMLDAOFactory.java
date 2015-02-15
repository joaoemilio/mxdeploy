package com.mxdeploy.api.dao;

import com.mxdeploy.api.dao.xml.BeanShellApplicationXMLDAO;
import com.mxdeploy.api.dao.xml.PreferencesXMLDAO;
import com.mxdeploy.api.dao.xml.ProjectLibraryXMLDAO;
import com.mxdeploy.api.dao.xml.SimpleDataStructureXMLDAO;

public class XMLDAOFactory extends DAOFactory {

	public BeanShellApplicationDAO createBeanShellApplicationDAO() {
		return new BeanShellApplicationXMLDAO();
	}

	@Override
	public ProjectLibraryDAO createProjectLibraryDAO() {
		return new ProjectLibraryXMLDAO();
	}

	public PreferencesDAO createPreferencesDAO() {
		return new PreferencesXMLDAO();
	}

	public SimpleDataStructureDAO createSimpleDataStructureDAO() {
		return new SimpleDataStructureXMLDAO();
	}

}
