package com.mxdeploy.api.domain.middleware;

import java.util.ArrayList;
import java.util.List;

public class WebSphere60 extends WebSphereAppServer {
	
	protected List<WebSphereAppServerProfile> profiles = new ArrayList<WebSphereAppServerProfile>();
	
	public void addProfile(WebSphereAppServerProfile profile) {
		profiles.add(profile);
	}
	
	public List<WebSphereAppServerProfile> getProfiles() {
		return this.profiles;
	}

}
