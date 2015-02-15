package com.mxdeploy.api.util;

import java.io.IOException;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class LogAppender {
	
	private static final Logger LOGGER = Logger.getLogger(LogAppender.class);
	
	public static Appender setFileAppender(String file){
		
		Appender fileAppender = null;
		
		try {
			fileAppender = new FileAppender(new PatternLayout("%d [%t] %-5p %c - %m%n"),file);
		} catch (IOException e) {
			LOGGER.error(e);
			return null;
		}
		
		return fileAppender;
		
	}
	
	

}
