package com.mxdeploy.api.domain.middleware;

import java.util.List;

import com.mxdeploy.api.domain.TransferObject;

public class IHSConfig extends TransferObject {
	
	private List<IHSConfigLocation> configLocations;
	private List<IHSConfigLocation> configLocationsMatch;
	private List<VirtualHost> virtualHosts;

	public void setIHSConfigLocations(List<IHSConfigLocation> configLocations){
		this.configLocations = configLocations;
	}
	
	public void setIHSConfigLocationsMatch(List<IHSConfigLocation> configLocationsMatch){
		this.configLocationsMatch = configLocationsMatch;
	}
	
	public void setVirtualHosts(List<VirtualHost> virtualHosts){
		this.virtualHosts = virtualHosts;
	}
	
	public boolean isServerStatusEnabled(){
		boolean result = false;
		for(IHSConfigLocation configLoc:configLocations ){
			if(configLoc.isServerStatusEnabled()){
				result = true;
				break;
			}
		}
		return result;
	}
	
}
