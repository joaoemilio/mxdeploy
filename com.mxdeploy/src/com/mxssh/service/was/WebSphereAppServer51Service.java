package com.mxssh.service.was;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mxdeploy.api.domain.middleware.Middleware;
import com.mxdeploy.api.domain.middleware.WebSphereAppServer;
import com.mxssh.SSHServiceNew;

public class WebSphereAppServer51Service { //extends WebSphereAppServerService {
//
//	public WebSphereAppServer51Service(SSHServiceNew sshClient) {
//		super(sshClient);
//		// TODO Auto-generated constructor stub
//	}
//	
//	/**
//	 * This method is meant to retrieve a list of WAS 5.1 instances installed
//	 * It will look for WAS instances currently running and also look at common directories 
//	 * @return
//	 * @throws InterruptedException 
//	 * @throws IOException 
//	 */
//	public List<Middleware> retrieveMiddlewareList() throws IOException, InterruptedException {
//		List<Middleware> list = new ArrayList<Middleware>();
//		
//		List<String> homes = getWasHomes();
//		for(String wasHome: homes) {
//			//checking WAS version
//			String version = getWAS5Version(wasHome);
//			if(!(version != null && version.startsWith("5.1")) ) {
//				continue;
//			}
//			WebSphereAppServer was = new WebSphereAppServer();
//			was.setWasHome(wasHome);
//			list.add(was);
//		}
//		
//		return list;
//	}
//
}
