package com.mxdeploy.api.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.UUID;

import com.mxdeploy.api.dao.ProjectDAO;
import com.mxdeploy.api.dao.ProjectServerDAO;
import com.mxdeploy.api.domain.Database;
import com.mxdeploy.api.domain.Project;
import com.mxdeploy.api.domain.ProjectServer;
import com.mxdeploy.api.domain.Server;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class ProjectService {

	public void create(Project project) {
		ProjectDAO dao = new ProjectDAO();
		project.setId(UUID.randomUUID().toString());
		project.setIsOpen(new Integer(1));
		dao.create(project);
	}

	public void update(Project project) {
		ProjectDAO dao = new ProjectDAO();
		dao.save( project);
	}

	public boolean createProjectServer(Project project, Server server) {
		ProjectDAO dao = new ProjectDAO();
		dao.createProjectServer(project,server);
		return true;
	}

	public List<Project> search(Project project) {
		ProjectDAO dao = new ProjectDAO();
		return dao.searchMatching(project);
	}
	
	public List<Project> searchProjectByServer(String hostname) {
		ProjectDAO dao = new ProjectDAO();
		return dao.searchProjectByServer(hostname);
	}
	

	public void removerProjectServer(ProjectServer projectServer) {
		ProjectServerDAO dao = new ProjectServerDAO();
		dao.delete(projectServer);
	}

	

	public void removerProjectServerByProject(String idProject) {
		ProjectServerDAO dao = new ProjectServerDAO();
		dao.deleteByProject(idProject);
	}

	public void removerProject(Project project) {
		ProjectDAO dao = new ProjectDAO();
		dao.delete(project);
	}
	
	public Project obtemProjectAndServerByCommand(String idProject, String idCommand){
		Project project = obtemProject(idProject);
		ProjectDAO dao = new ProjectDAO();
		return dao.obtemProjectAndServerByCommand(project, idCommand);
	}

	public List<Project> obtemProjectAndServer(Project project)	throws ServiceException {
		ProjectDAO dao = new ProjectDAO();
		return dao.obtemProjectAndServer(project);
	}
	
	public Project obtemProject( String idProject ){
		Database database = Database.getInstance();
		for( Project project : database.getProjects() ){
			if ( project.getId().equals(idProject) ){
				 return project;
			}
		}
		return null;
	}

//	public List<Property> loadPropertyXML() {
//		ArrayList<Property>properties = new ArrayList<Property>();
//		File file = new File(Database.getPropertyPath());
//
//		File[] files = file.listFiles(new FilenameFilter() {
//			public boolean accept(File dir, String name) {
//				if (name.toLowerCase().endsWith(".xml")) {
//					return true;
//				}
//				return false;
//			}
//		});
//
//		if (files != null && files.length > 0) {
//			XStream xstream = new XStream(new DomDriver());
//			for (int i = 0; i < files.length; i++) {
//				try {
//					Property property = (Property) xstream.fromXML(new FileReader(Database.getPropertyPath()+ "/" + files[i].getName()));
//					property.setHostname(files[i].getName().replace(".xml", ""));
//					properties.add(property);
//				} catch (FileNotFoundException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		return properties;
//	}

	public Project loadProjectXML(String projectName) {
		XStream xstream = new XStream(new DomDriver());
		Project project = null;
		;
		try {
			project = (Project) xstream.fromXML(new FileReader(Database.getProjectPath()+ "/" + projectName + "/project.xml"));
			
			loadBeanShellXML(project);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		
		return project;
	}

	public void loadBeanShellXML(Project project) {

		File dir = new File(Database.getProjectPath() + "/" + project.getName() + "/procedure");

		XStream xstream = new XStream(new DomDriver());

/*
		Collection<File> files = FileUtils.listFiles(dir, new String[]{"xml"}, false);
		for(File file1: files) {
			BeanShell beanShell = (BeanShell) xstream.fromXML(file1.getAbsolutePath());
			project.getRootFolder().addBeanShell(beanShell);
		}
		*/
		
		/*load sub folders
		// This filter only returns directories
		FileFilter fileFilter = new FileFilter() {
		    public boolean accept(File file) {
		        return file.isDirectory();
		    }
		};
		File[] subFolders = dir.listFiles(fileFilter);
		for(File subFolder: subFolders) {
			ProcedureFolder pFolder = new ProcedureFolder();
			pFolder.setName(subFolder.getName());
			project.addSubFolder(pFolder);
		}
		
		List<ProcedureFolder> pSubFolders = project.getSubFolders();
		for(ProcedureFolder subFolder: pSubFolders) {
	//		loadSubFolder(project, subFolder);
		}
		*/
	
	}
	
}
