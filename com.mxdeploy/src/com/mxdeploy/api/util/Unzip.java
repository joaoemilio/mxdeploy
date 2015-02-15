package com.mxdeploy.api.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.mxdeploy.api.domain.Database;


public class Unzip {

  public static final void copyInputStream(InputStream in, OutputStream out)
  throws IOException
  {
    byte[] buffer = new byte[1024];
    int len;

    while((len = in.read(buffer)) >= 0)
      out.write(buffer, 0, len);

    in.close();
    out.close();
  }

  public static void main(String args[]){
	  (new Unzip()).execute("C:/Projects/MXTerminal/releases/MXTerminal/update.zip");
  }
  
  public void execute(String fileName) {
    Enumeration entries;
    ZipFile zipFile;

    try {
      zipFile = new ZipFile(fileName);

      entries = zipFile.entries();

      while(entries.hasMoreElements()) {
        ZipEntry entry = (ZipEntry)entries.nextElement();
 
        if(entry.isDirectory()) {
          // Assume directories are stored parents first then children.
          System.err.println("Extracting directory: " + entry.getName());
          // This is not robust, just for demonstration purposes.
          (new File(Database.HOME+"/"+entry.getName())).mkdirs(); 
          continue;
        } 

        System.err.println("Extracting file: " + Database.HOME+"/"+entry.getName());
        copyInputStream(zipFile.getInputStream(entry),
           new BufferedOutputStream(new FileOutputStream(Database.HOME+"/"+entry.getName())));
      }

      zipFile.close();
    } catch (IOException ioe) {
      System.err.println("Unhandled exception:");
      ioe.printStackTrace();
      return;
    }
  }

}

