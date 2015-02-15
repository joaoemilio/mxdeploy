package com.mxdeploy.launcher.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.swt.widgets.Display;

import com.mxdeploy.api.domain.Database;
import com.mxdeploy.launcher.Launcher;
import com.mxdeploy.launcher.splash.SplashDialog;

public class SynchronizeFiles {

	private static String backupFolder = null;
	
	public static void synchronize(final SplashDialog splashDialog) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				splashDialog.getLabel().setText("   Updating Library...");
			}
		});
  
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		Date today = Calendar.getInstance().getTime();       
		backupFolder = df.format(today);
		
		syncLibrary(splashDialog);
		syncPlugins(splashDialog);
	}
	
	public static void syncLibrary(final SplashDialog splashDialog){
		File file = new File(Database.LAUNCHER_PATH + "/tmp/lib");
		File[] files = file.listFiles();

		if(!file.exists()){
		  return;	
		}
		
		if (files != null || files.length > 0) {
			splashDialog.getProgressBar().setMaximum( files.length );
			for (int i = 0; i < files.length; i++) {
				if (!files[i].isDirectory()) {
					if (!files[i].getName().startsWith(".")){
						
						backupLibFiles( files[i].getName() );
						
						SynchronizeFiles.copyfile(Database.LAUNCHER_PATH + "/tmp/lib/"+ files[i].getName()
								                , Database.LIBRARY_PATH	+"/"+ files[i].getName());
					    
						removefile(Database.LAUNCHER_PATH + "/tmp/lib/"+ files[i].getName());
					}
				} else if (files[i].getName().equals("shared_library")) {
					File[] shared_files = files[i].listFiles();

					for (int j = 0; j < shared_files.length; j++) {
						if (!shared_files[j].isDirectory()) {
							if (!shared_files[j].getName().startsWith(".")){
								
								backupSharedLibFiles(shared_files[j].getName());
								
								copyfile(Database.LAUNCHER_PATH+ "/tmp/lib/shared_library/"+ shared_files[j].getName()
										, Database.HOME	+ "/lib/shared_library/" + shared_files[j].getName());
							    
								removefile(Database.LAUNCHER_PATH+ "/tmp/lib/shared_library/"+ shared_files[j].getName());
							}
						}
					}
				}
				splashDialog.getProgressBar().setSelection(i);
			}
		}
	}
	
	public static void syncPlugins(final SplashDialog splashDialog){
		File file = new File(Database.LAUNCHER_PATH + "/tmp/plugins");
		File[] files = file.listFiles();

		if(!file.exists()){
		  return;	
		}
		
		if (files != null || files.length > 0) {
			splashDialog.getProgressBar().setMaximum( files.length );
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory() && !files[i].getName().equals(".svn")) {
					createDir(Database.PLUGINS_PATH+"/"+files[i].getName());
					
					String dir = files[i].getName();
					File[] pluginFile = files[i].listFiles();
					for (int j = 0; j < pluginFile.length; j++) {
						if (!pluginFile[j].isDirectory()) {
							
							backupPluginFiles(files[i].getName(),pluginFile[j].getName());
							
							copyfile(Database.LAUNCHER_PATH+ "/tmp/plugins/"+dir+"/"+pluginFile[j].getName()
									                , Database.PLUGINS_PATH	+"/"+dir+"/"+ pluginFile[j].getName());
						    
							removefile(Database.LAUNCHER_PATH+ "/tmp/plugins/"+dir+"/"+pluginFile[j].getName());
						    removefile(Database.LAUNCHER_PATH+ "/tmp/plugins/"+dir);
						}
					}
				}
				splashDialog.getProgressBar().setSelection(i);
			}
		}
	}	

	public static void backupLibFiles(String srFile) {
		createDir(Database.LAUNCHER_PATH+"/backup/"+backupFolder);
		createDir(Database.LAUNCHER_PATH+"/backup/"+backupFolder+"/lib");
		copyfile(Database.LIBRARY_PATH+"/"+srFile , Database.LAUNCHER_PATH+"/backup/"+backupFolder + "/lib/"+srFile);
	}
	
	public static void backupSharedLibFiles(String srFile) {
		createDir(Database.LAUNCHER_PATH+"/backup/"+backupFolder);
		createDir(Database.LAUNCHER_PATH+"/backup/"+backupFolder+"/lib/shared_library");
		copyfile(Database.LIBRARY_PATH+"/shared_library/"+srFile , Database.LAUNCHER_PATH+"/backup/"+backupFolder + "/lib/shared_library/"+srFile);
	}	
	
	public static void backupPluginFiles(String folder,String srFile) {
		createDir(Database.LAUNCHER_PATH+"/backup/"+backupFolder+"/plugins");
		createDir(Database.LAUNCHER_PATH+"/backup/"+backupFolder+"/plugins/"+folder);		

		copyfile(Database.PLUGINS_PATH+"/"+folder+"/"+srFile , Database.LAUNCHER_PATH+"/backup/"+backupFolder+"/plugins/"+folder+"/"+srFile);
	}
	
	public static void copyfile(String srFile, String dtFile) {
		try {
			File f1 = new File(srFile);
			File f2 = new File(dtFile);
			InputStream in = new FileInputStream(f1);

			// For Append the file.
			// OutputStream out = new FileOutputStream(f2,true);

			// For Overwrite the file.
			OutputStream out = new FileOutputStream(f2);

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
			//System.out.println("File copied.");
		} catch (FileNotFoundException ex) {
			System.out.println(ex.getMessage() + " in the specified directory.");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void removefile(String srFile) {
		File f1 = new File(srFile);
		f1.delete();
	}
	
	public static String createDir(String srFile) {
		File f1 = new File(srFile);
		if(!f1.exists()){
			f1.mkdir();
		}
		return srFile;
	}	
	
}
