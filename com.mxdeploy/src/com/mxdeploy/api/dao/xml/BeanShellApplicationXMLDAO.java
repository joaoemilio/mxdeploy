package com.mxdeploy.api.dao.xml;

import java.io.File;
import java.util.UUID;

import com.mxdeploy.MXTerminal;
import com.mxdeploy.api.dao.BeanShellApplicationDAO;
import com.mxdeploy.api.domain.BeanShellApplication;

public class BeanShellApplicationXMLDAO extends XMLDAO implements BeanShellApplicationDAO {

	public void create(BeanShellApplication domain) throws DAOException {
		String path = BeanShellApplication.BEANSHELLAPPLICATIONS;
		domain.setUuid(UUID.randomUUID());
		File file = new File( MXTerminal.WORKSPACE + "/" + path + "/" + domain.getUuid().toString());
		this.create(domain, file);
	}
	
}
