package com.mxdeploy.api.dao.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.mxdeploy.Preferences;
import com.mxdeploy.api.dao.PreferencesDAO;
import com.mxdeploy.api.domain.Database;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class PreferencesXMLDAO extends XMLDAO implements PreferencesDAO {

	public void save(Preferences domain) throws DAOException {
		String path = Database.WORKSPACE_PATH + "/" + Database.WORKSPACE_NAME;
		File file = new File( path + "/preferences.xml");
		this.create(domain, file);
	}
	
	
	public Preferences loadPreferences() {
		String path = Database.WORKSPACE_PATH + "/" + Database.WORKSPACE_NAME;
		File file = new File( path + "/preferences.xml");
		XStream xstream = new XStream(new DomDriver());
		Preferences prefs;
		try {
			prefs = (Preferences)xstream.fromXML(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return prefs;
	}
}
