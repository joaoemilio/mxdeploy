package com.mxdeploy.api.domain;

import java.util.ArrayList;
import java.util.List;

import xml.module;
import xml.modulegroups;

import com.mxdeploy.api.service.ProjectService;
import com.mxdeploy.api.service.ServerService;
import com.mxdeploy.swt.MainShell;

public class Database {

	private List<Project> projects = new ArrayList<Project>();

	private List<Server> servers = null;

	private List<BeanShell> beanShellList = new ArrayList<BeanShell>();

	private List<Property> properties = new ArrayList<Property>();
	
	private static Database database = null;

	private List<PasswordManager> passwordManager = new ArrayList<PasswordManager>();
	
	private modulegroups _modulegroups_ = null;

	public static boolean usePassSaved = true;
	
	public static String WORKBOOK_FILE_NAME = "token.xml";
	public static String WORKBOOK_TEMPLATE_FILE_NAME = "workbookTemplate.xml";
	public static String HOME;
	static {
		    HOME = System.getenv("MXD_HOME");
			
			if(HOME == null) {
				HOME = Database.class.getResource("/").getPath();
				int index = HOME.lastIndexOf("lib");
				if( index >= 0){
				    HOME = HOME.substring(1, index-1);
				}
			}
			System.out.println("MXD_HOME=" + HOME);
	}

	public static String WORKSPACE_NAME;
	
	public static String WORKSPACE_PATH;
	static{
		String wp = System.getenv("MXD_WORKSPACE");
		if(wp == null) {
			wp = (String)System.getProperties().get("MXD_WORKSPACE");
		}
		
		if(wp == null) {
			wp = Database.class.getResource("/").getPath();
			int index = wp.lastIndexOf("/");
			if(index == -1) index = wp.lastIndexOf("\\");
			wp = wp.substring(1, index);
			WORKSPACE_PATH = HOME + "/workspace";
		}else {
			WORKSPACE_PATH = wp;
		}
		System.out.println("MXD_WORKSPACE=" + WORKSPACE_PATH);
	}
	
	public static String MODULE_PATH;
	public static String MODULE_GROUP_FILE;	

	public static String PLUGINS_PATH = Database.HOME + "/plugins";

	public static final String LIBRARY_PATH = Database.HOME + "/lib";
	
	public static String LAUNCHER_PATH = Database.HOME + "/launcher";

	public static String WDS_TOKEN_PATH = Database.HOME +"/"+WORKBOOK_FILE_NAME;
	
	public static String getProjectPath(){
		return WORKSPACE_PATH+"/"+WORKSPACE_NAME+"/projects";
	}
	
	public static String getPropertyPath(){
		  return WORKSPACE_PATH+"/"+WORKSPACE_NAME+"/servers/properties";
	}
	
	public static String getServerPath(){
		  return WORKSPACE_PATH+"/"+WORKSPACE_NAME+"/servers";
	}
	
	public static String getBeanShellPath(){
		  return WORKSPACE_PATH+"/"+WORKSPACE_NAME+"/beanshell";
	}	
	
	public static String getScriptsPath(){
		  return WORKSPACE_PATH+"/"+WORKSPACE_NAME+"/scripts";
	}	
	
	private Database() {
	}

	public static Database getInstance() {
		if (database == null) {
			database = new Database();
		}
		return database;
	}

	/**
	 * @return the projectList
	 */
	public List<Project> getProjects() {
		return projects;
	}

	/**
	 * @param projectList
	 *            the projectList to set
	 */
	public void setProjects(List<Project> list) {
		projects = list;
	}

	public synchronized void addProject(Project project) {
		projects.add(project);
	}
	
	/**
	 * @return the serverList
	 */
	public List<Server> getServerList() {
		if (servers == null) {
			servers = loadServerXML();
		}
		return servers;
	}
	
	/**
	 * @param serverList
	 *            the serverList to set
	 */
	public void setServers(List<Server> list) {
		servers = list;
	}

	/**
	 * @return the beanShells
	 */
	public List<BeanShell> getBeanShellList() {
		return beanShellList;
	}

	public List<Server> loadServerXML() {
		ServerService service = new ServerService();
		servers = service.loadServerXML();
		return servers;
	}

	/**
	 * @return the passwordManager
	 */
	public List<PasswordManager> getPasswordManagerList() {
		return passwordManager;
	}

	/**
	 * @param properties the properties to set
	 */
	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}
	
	public synchronized  void addProperty(Property property) {
		this.properties.add(property);
	}

	public void loadProjectXML(String projectName) {
		ProjectService service = new ProjectService();
		this.projects.add(service.loadProjectXML(projectName));
		
	}

	public modulegroups get_modulegroups() {
		return _modulegroups_;
	}

	public void set_modules(modulegroups _modulegroups_) {
		this._modulegroups_ = _modulegroups_;
	}
	
	public static String getBeanShellScriptPath(){
		String filePath = null;
		if ( MainShell.getCTopTabFolder().getSelection().getData()!=null && MainShell.getCTopTabFolder().getSelection().getData() instanceof module){
			module __module__ = (module)MainShell.getCTopTabFolder().getSelection().getData();
			filePath = __module__.getFullpath();
		} else {
			String scriptName = MainShell.getCTopTabFolder().getSelection().getText();
			filePath = Database.WORKSPACE_PATH+"/"+Database.WORKSPACE_NAME+"/beanshell/bsh/"+scriptName;
		}
		return filePath;
	}
		

}
