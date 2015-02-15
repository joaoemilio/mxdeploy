package com.mxdeploy.plugin.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.mxdeploy.api.domain.Database;
import com.mxdeploy.plugin.domain.Plugin;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class URLClassLoaderPlugin {

	private static List<Plugin> pluginList = new ArrayList<Plugin>();
	static Logger logger = Logger.getLogger(URLClassLoaderPlugin.class);
	
	public static void addPath(String s) throws Exception {
		  File f = new File(s);
		  URL u = f.toURL();
		  URLClassLoader urlClassLoader = (URLClassLoader)ClassLoader.getSystemClassLoader();
		  Class urlClass = URLClassLoader.class;
		  Method method = urlClass.getDeclaredMethod("addURL", new Class[]{URL.class});
		  method.setAccessible(true);
		  method.invoke(urlClassLoader, new Object[]{u});
	}	
	
	public static void loadPlugins(){ 

		getPlugins();
		
		File dir = new File(Database.PLUGINS_PATH);
		File[] dirs = dir.listFiles();
		for(int i=0; i < dirs.length; i++){
			if(dirs[i].isDirectory()){
				File jarFile = new File(dirs[i].toString());
				File[] jars = jarFile.listFiles();					
				for(int j=0; j < jars.length; j++){
					if(!jars[j].isDirectory()){
						if(jars[j].getName().endsWith(".jar"))
							try {
								addPath(dirs[i]+"/"+jars[j].getName());
							} catch (Exception e) {
								e.printStackTrace();
							}						
					}
				}
			}
		}
		
	}
	
	private static void getPlugins(){
		
		File dir = new File(Database.PLUGINS_PATH); 
		System.out.println(dir);
		File[] dirs = dir.listFiles();
		for(int i=0; i < dirs.length; i++){
			if(dirs[i].isDirectory()  && !dirs[i].getName().equals(".svn")){
				File checkExist = new File(dirs[i]+"/plugin.xml");
				if(!checkExist.exists()){
					continue;
				}
				XStream xstream = new XStream(new DomDriver());
				try {
					Plugin plugin = (Plugin)xstream.fromXML(new FileReader(dirs[i]+"/plugin.xml" ));
					pluginList.add(plugin);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}				
			}
		}
	}
	
	/**
	 * @return the pluginList
	 */
	public static List<Plugin> getPluginList() {
		return pluginList;
	}	
	
}
