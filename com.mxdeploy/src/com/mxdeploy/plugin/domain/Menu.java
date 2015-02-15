package com.mxdeploy.plugin.domain;

public class Menu {
	
	public static String SUBMENU_FILE = "File";
	public static String SUBMENU_TERMINAL = "Terminal";
	public static String SUBMENU_WINDOW = "Window";
	public static String SUBMENU_HELP = "Help";
	public static String SUBMENU_CUSTOM = "CUSTOM";
	
	public static String POPUP_PROJECT = "Project";
	public static String POPUP_SERVER = "Server";
	
	private String type;
    private String classAddOn;
	/**
	 * @return the classAddOn
	 */
	public String getClassAddOn() {
		return classAddOn;
	}
	/**
	 * @param classAddOn the classAddOn to set
	 */
	public void setClassAddOn(String classAddOn) {
		this.classAddOn = classAddOn;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}	
	

}
