package com.mxdeploy.api.service;

import com.mxdeploy.api.dao.DAOFactory;
import com.mxdeploy.api.dao.ProjectLibraryDAO;
import com.mxdeploy.api.dao.xml.DAOException;
import com.mxdeploy.api.domain.BeanShellProject;

public class ProjectLibraryService {

	public BeanShellProject getDefaultLibrary() throws BusinessException {
		DAOFactory daof = DAOFactory.getInstance();
		ProjectLibraryDAO dao = daof.createProjectLibraryDAO();
		
		BeanShellProject domain = null;
		
		try{
			domain = dao.getDefaultLibrary();
		}catch(DAOException e) {
			throw new BusinessException("Error while loading Default Library Project", e);
		}
		
		return domain;
	}
	
}
