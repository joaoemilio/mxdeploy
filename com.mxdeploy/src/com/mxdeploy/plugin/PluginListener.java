package com.mxdeploy.plugin;

public interface PluginListener {
	
	public void setLogLevel(int logLevel);
	
	public void sendMessage(String message);

}
