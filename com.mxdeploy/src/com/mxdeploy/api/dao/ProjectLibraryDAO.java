package com.mxdeploy.api.dao;

import com.mxdeploy.api.dao.xml.DAOException;
import com.mxdeploy.api.domain.BeanShellProject;

public interface ProjectLibraryDAO {

	public BeanShellProject getDefaultLibrary() throws DAOException;
	
	public void createDefaultLibrary(BeanShellProject domain) throws DAOException;
	
	
	
}
