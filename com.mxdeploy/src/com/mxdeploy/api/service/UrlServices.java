package com.mxdeploy.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.mxdeploy.api.dao.ProjectDAO;
import com.mxdeploy.api.dao.UrlDAO;
import com.mxdeploy.api.domain.Database;
import com.mxdeploy.api.domain.Project;
import com.mxdeploy.api.domain.Url;

public class UrlServices {

	public void create(Project projectValue, Url url){
		ProjectService projectService = new ProjectService();
		Project project = projectService.obtemProject(projectValue.getId());
		url.setId(UUID.randomUUID().toString());
		project.addUrl(url);
		projectService.update(project);
	}
	
	public void update(Url urlValue) {
		Database database = Database.getInstance();
		List<Project> projects = database.getProjects();
		for(Project project : projects){
			if(project.getId().equals(urlValue.getIdProject())){
				for(Url url : project.getUrls()){
					if( urlValue.getId().equals( url.getId()) ){
						url.setName(urlValue.getName());
						url.setUrl(urlValue.getUrl());
					}
				}
				ProjectDAO dao = new ProjectDAO();
				dao.save(project);
				break;
			}
		}
	}
	
	public List<Url> searchByProject(String id_projectIn) {
		UrlDAO dao = new UrlDAO();
		Url url = new Url();
		url.setIdProject(id_projectIn);
		return dao.searchMatching(url);
	}
	
	public void remover(Project projectValue, Url urlValue){
		ProjectService projectService = new ProjectService();
		List<Url> urls = new ArrayList<Url>();
		
		Project project = projectService.obtemProject(projectValue.getId());
		for( Url url : project.getUrls() ){
			 if( !url.getId().equals(urlValue.getId()) ){
				 urls.add(url);
			 }
		}
		
		if(!urls.isEmpty())
		for(Url url : urls){
			project.addUrl(url);
		}
		
		projectService.update(project);
	}
	
}
