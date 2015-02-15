package com.mxscript;

import org.apache.log4j.Logger;

public abstract class MXBSListener {

	protected Logger logger = Logger.getRootLogger();
	protected Boolean debug;
	
	public MXBSListener() {
	}
	
	public void sendMessage(MXBSMessage message) {
		if(message.getType().equals(MXBSMessage.LOG)) {
			logger.info(message.getData());
		}
	}
	
}
