package com.mxterminal.util;

public class Parameter {
	
	public static final String SERVER_LIST_DIRECTORY  = FileParameterReader.readParameter("etc/properties", "ServerListDirectory");
	public static final String JAVA_PATH = FileParameterReader.readParameter("etc/properties", "JavaPath");
	
}
