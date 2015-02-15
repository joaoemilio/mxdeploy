package com.mxssh;

import org.apache.log4j.Logger;

public class LogService {
	static Logger LOGGER = Logger.getLogger(LogService.class);
	private static LogService me;
	private int level = 3; //default only log errors and regular outputs
	
	public static LogService getInstance() {
		if(me == null){
			me = new LogService();
		}
		
		return me;
	}
	
	public void setLevel(int level){
		this.level = level;
	}
	
	public void debug(String log){
		if(level == 0){
			LOGGER.debug(log);
		}
	}

	public void info(String log){
		if(level <= 1){
			LOGGER.info(log);
		}
	}

	public void warning(String log){
		if(level <= 2){
			LOGGER.warn(log);
		}
	}

	public void error(String log){
		if(level <= 3){
			LOGGER.error(log);
		}
	}

	public void output(String log){
		System.out.println(log);
	}
	
}
