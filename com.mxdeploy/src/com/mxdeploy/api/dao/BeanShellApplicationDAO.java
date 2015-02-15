package com.mxdeploy.api.dao;

import com.mxdeploy.api.dao.xml.DAOException;
import com.mxdeploy.api.domain.BeanShellApplication;

public interface BeanShellApplicationDAO {

	public void create(BeanShellApplication domain) throws DAOException;
	
}
