package com.mxdeploy.api.domain;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class Version {
	
	private String version;
	private String name;
	
	private List<PluginVersion> pluginList =  new ArrayList<PluginVersion>();

	public static void main(String args[]){
		Version version = new Version();
		version.setName("MXTerminal");
		version.setVersion("7.0.0");
		
		PluginVersion pluginUpdater = new PluginVersion();
		pluginUpdater.setName("com.ds.plugin.updater");
		pluginUpdater.setVersion("7.0.0");
		pluginUpdater.addDependence("cwa2.jar");
		pluginUpdater.addDependence("svnkit.jar");
		pluginUpdater.addDependence("trilead.jar");
		
		version.addPlugin(pluginUpdater);
		
		PluginVersion pluginRaptor = new PluginVersion();
		pluginRaptor.setName("com.ds.plugin.raptor");
		pluginRaptor.setVersion("7.0.0");	
		
		version.addPlugin(pluginRaptor);
		
		XStream xstream = new XStream(new DomDriver()); 
		try {
			xstream.toXML(version, new FileWriter("C:/release/hmg/launcher/tmp/updater/version.xml"));
		} catch (IOException e) {
			e.printStackTrace();
		}			
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the pluginList
	 */
	public List<PluginVersion> getPluginList() {
		return pluginList;
	}

	/**
	 * @param pluginList the pluginList to set
	 */
	public void addPlugin(PluginVersion plugin) {
		this.pluginList.add( plugin );
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

}
