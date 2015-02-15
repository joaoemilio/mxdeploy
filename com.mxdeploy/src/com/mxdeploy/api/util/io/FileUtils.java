package com.mxdeploy.api.util.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

	private static FileUtils me;
	
	private FileUtils(){
		
	}
	
	public static FileUtils getInstance(){
		if( me == null ){
			me =  new FileUtils();
		}
		return me;
	}
	
	public List getContentFiles(File file){
		return getContents(file);
	}
	
	public List<String> getContents(String filePath){
		return getContents(new File(filePath));
	}
	
	public List<String> getContents(File aFile) {
		List<String> list = new ArrayList<String>();
	    BufferedReader input = null;
	    try {
	    	input = new BufferedReader( new FileReader(aFile) );
		    String line = null; //not declared within while loop
		    while (( line = input.readLine()) != null){
				//System.out.println(" -> getContents - line: " + line );
		    	list.add(line);
		    }
	    }catch (FileNotFoundException ex) {
			System.out.println(" -> error: FileNotFoundException: " + ex.getMessage() );
	    	ex.printStackTrace();
	    }catch (IOException ex){
			System.out.println(" -> error: IOException: " + ex.getMessage() );
	    	ex.printStackTrace();
	    } finally {
	    	try {
	    		if (input!= null) {
	    			input.close();
		        }
	    	}catch (IOException ex) {
				System.out.println(" -> error 2: IOException: " + ex.getMessage() );
	    		ex.printStackTrace();
	    	}
	    }
	    return list;
	}	

}