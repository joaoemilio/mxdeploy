package com.mxdeploy.api.service;

import com.mxdeploy.Preferences;
import com.mxdeploy.api.dao.DAOFactory;
import com.mxdeploy.api.dao.PreferencesDAO;

public class PreferencesService {
	
	public void save(Preferences domain) {
		try{
			PreferencesDAO dao = DAOFactory.getInstance().createPreferencesDAO();
			dao.save(domain);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public Preferences loadPreferences() {
		try{
			PreferencesDAO dao = DAOFactory.getInstance().createPreferencesDAO();
			return dao.loadPreferences();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

}
