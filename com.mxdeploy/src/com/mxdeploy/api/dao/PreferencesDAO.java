package com.mxdeploy.api.dao;

import com.mxdeploy.Preferences;
import com.mxdeploy.api.dao.xml.DAOException;

public interface PreferencesDAO {
	
	public void save(Preferences domain) throws DAOException;

	public Preferences loadPreferences();

}
