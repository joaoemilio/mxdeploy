package com.mxdeploy.api.dao.xml;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.mxdeploy.MXTerminal;
import com.mxdeploy.api.dao.ProjectLibraryDAO;
import com.mxdeploy.api.domain.BeanShellProject;

public class ProjectLibraryXMLDAO extends XMLDAO implements ProjectLibraryDAO {
	
	public static final String DEFAULT_LIBRARY_PATH = MXTerminal.WORKSPACE + "/default_library.xml";
 
	public BeanShellProject getDefaultLibrary() throws DAOException {
		XStreamHelper xsHelper = new XStreamHelper();
		String xml = null;
		try {
			xml = FileUtils.readFileToString(new File(DEFAULT_LIBRARY_PATH) );
		} catch (IOException e) {
			throw new DAOException(e);
		}
		BeanShellProject domain = (BeanShellProject)xsHelper.xstream.fromXML(xml);
		return domain;
	}

	public void createDefaultLibrary(BeanShellProject domain) throws DAOException {
		File file = new File(DEFAULT_LIBRARY_PATH);
		try {
			this.create(domain, file);
		} catch (DAOException e) {
			throw new DAOException(e);
		}
	}
	
}
