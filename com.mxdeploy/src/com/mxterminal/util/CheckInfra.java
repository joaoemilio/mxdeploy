package com.mxterminal.util;

import java.io.File;

import org.apache.log4j.Logger;

import com.mxdeploy.api.util.LogAppender;

public class CheckInfra {		
	
	static Logger LOGGER = Logger.getLogger(CheckInfra.class);	
	
	
	public static boolean  checkSLD(){		
			
		LOGGER.removeAllAppenders();
		LOGGER.addAppender(LogAppender.setFileAppender("HCI.log"));
		
		boolean isValid = false;
		
		File SLD = new File(Parameter.SERVER_LIST_DIRECTORY);
		if(SLD.exists()){
			
			LOGGER.info("SLD Checked");
			isValid = true;
						
		}else{
			
			LOGGER.info("SLD Do not exist");
			if(SLD.mkdir()){
				
				LOGGER.info("SLD Created");
				isValid = true;
				
				
			}else{
				
				LOGGER.error("SLD Could not be Created");
				isValid = false;
				
			};
			
		};		
		
		return isValid;
	}
	
	public static String[] getServerList(){
				
		LOGGER.removeAllAppenders();	
		LOGGER.addAppender(LogAppender.setFileAppender("HCI.log"));
			
		File SLD = new File(Parameter.SERVER_LIST_DIRECTORY);
		
		String[] serverList =  SLD.list();
		
		LOGGER.info( serverList.length + " Server list on " + Parameter.SERVER_LIST_DIRECTORY);
		
		return serverList;
		
	}

}
