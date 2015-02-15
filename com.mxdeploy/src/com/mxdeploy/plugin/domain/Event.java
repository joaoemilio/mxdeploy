package com.mxdeploy.plugin.domain;

public class Event {

	/* You can find this event when you are connection using terminal.
	 * It is used to AMEX Account doing a gateway*/
	public static final int TYPE_AFTER_START_TERMINAL_EVENT = 1;
	/* Before the event TYPE_AFTER_START_TERMINAL_EVENT to be executed you need first load the property.
	 * You can find this in MainHelper*/	
	public static final int TYPE_LOAD_PROPERTIES_EVENT = 2;
	/* This Event will be used in SysOps to check CPU, Memory and etc... 
	 * The JSCH lib will execute some shell commands in backgroup and fill up the window wit result*/	
	public static final int TYPE_AFTER_START_GATEWAY_EVENT = 3;
	
	private String classEvent;
    private int type;
    
	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the classEvent
	 */
	public String getClassEvent() {
		return classEvent;
	}

	/**
	 * @param classEvent the classEvent to set
	 */
	public void setClassEvent(String classEvent) {
		this.classEvent = classEvent;
	}
	
}
