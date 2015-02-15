package com.mxdeploy.api.util.io;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class FileUtilities {
	
	private static FileUtilities me = new FileUtilities();
	
	private FileUtilities() {
		
	}
	
	public static FileUtilities getInstance() {
		if(me == null) {
			me = new FileUtilities();
		}
		return me;
	}
	
	public File backupFile(File srcFile) throws IOException {
		File dir = srcFile.getParentFile();
		String pathDirBackup = dir.getAbsolutePath() + "/backup";
		String pathBackupFile = pathDirBackup + "/" + srcFile.getName() + "_" + System.currentTimeMillis() + ".bkp";
		File backupDir = new File(pathDirBackup);
		if(!backupDir.exists() ) {
			createDir(backupDir);
		}
		File destFile = new File(pathBackupFile);
		FileUtils.copyFile(srcFile, destFile);
		return destFile;
	}
	
	public void createDir(File dir) throws IOException {
		dir.createNewFile();
	}

}
