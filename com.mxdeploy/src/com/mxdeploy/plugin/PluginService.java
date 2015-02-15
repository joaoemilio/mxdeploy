package com.mxdeploy.plugin;

import java.util.ArrayList;

import com.mxdeploy.UserContext;
import com.mxdeploy.api.domain.Project;
import com.mxdeploy.api.domain.Server;
import com.mxdeploy.api.domain.User;

public abstract class PluginService {

	protected ArrayList<PluginListener> listeners = new ArrayList<PluginListener>();
	
	public static PluginService getPlugin(String name) throws PluginException {
		
		if(name.equals("healthcheck")){
			name = "com.ds.plugin.healthcheck.HealthCheckPlugin";
		}
		
		PluginService pluginService = null;
		try {
			pluginService = (PluginService) Class.forName(name).newInstance();
		} catch (ClassNotFoundException e) {
			throw new PluginException("ClassNotFound: " + name);
		} catch (IllegalAccessException e) {
			throw new PluginException("IllegalAccess: " + name);
		} catch (InstantiationException e) {
			throw new PluginException("InstantiationException: " + name);
		}
		
		return pluginService;
	}
	
	public abstract void execute(Project project, Server server, String method) throws PluginException;
	
	protected User getUser(){
		return UserContext.getUser();
	}
	
	public void addListener(PluginListener listener){
		listeners.add(listener);
	}
	
}

