package com.mxdeploy.api.dao;


public abstract class DAOFactory  {

	public static DAOFactory getInstance() {
		//retornando fixo o DAOFactory de XMLs. Um properties ou outro argumento deveria ser passado
		return new XMLDAOFactory();
	}
	
	public abstract BeanShellApplicationDAO createBeanShellApplicationDAO();
	public abstract ProjectLibraryDAO createProjectLibraryDAO();
	public abstract PreferencesDAO createPreferencesDAO();
	public abstract SimpleDataStructureDAO createSimpleDataStructureDAO();

}
