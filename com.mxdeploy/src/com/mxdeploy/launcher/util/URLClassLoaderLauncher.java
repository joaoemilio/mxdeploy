package com.mxdeploy.launcher.util;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import com.mxdeploy.api.domain.Database;
import com.mxdeploy.launcher.Launcher;

public class URLClassLoaderLauncher {

	public static void addPath(String s) throws Exception {
		  File f = new File(s);
		  URL u = f.toURL();
		  URLClassLoader urlClassLoader = (URLClassLoader)ClassLoader.getSystemClassLoader();
		  Class urlClass = URLClassLoader.class;
		  Method method = urlClass.getDeclaredMethod("addURL", new Class[]{URL.class});
		  method.setAccessible(true);
		  method.invoke(urlClassLoader, new Object[]{u});
	}	
	
	public static void loadSharedLibrary(){
		System.out.println("LIBRARY PATH: " + Database.LIBRARY_PATH);
		File dir = new File(Database.LIBRARY_PATH);
		File[] files = dir.listFiles();
		for(int i=0; i < files.length; i++){
			if(files[i].isDirectory()){
				File jarFile = new File(files[i].toString());
				File[] jars = jarFile.listFiles();					
				for(int j=0; j < jars.length; j++){
					if(!jars[j].isDirectory()){
						if(jars[j].getName().endsWith(".jar"))
							try {
								System.out.println(Database.LIBRARY_PATH+"/"+files[i].getName()+"/"+jars[j].getName());
								String path = Database.LIBRARY_PATH+"/"+files[i].getName()+"/"+jars[j].getName();
								System.out.println(path);
								addPath(path);
							} catch (Exception e) {
								e.printStackTrace();
							}						
					}
				}
			} else {
				try {
					if(files[i].getName().endsWith(".jar")){
						//System.out.println(Launcher.LIBRARY_PATH+"/"+files[i].getName());
						String path = Database.LIBRARY_PATH+"/"+files[i].getName();
						System.out.println(path);
					   addPath(path);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}	

}
