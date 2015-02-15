package com.mxdeploy.api.domain.middleware.ihs;

public class LogConfig {
	
	public static final int ERROR_LOG = 0;
	public static final int TRANSFER_LOG = 1;
	public static final int CUSTOM_LOG = 2;
	public static final int SCRIPT_LOG = 3;
	public static final int REWRITE_LOG = 4;
	
	private int type;
	private String logPath;
	private IHSConfig config;
	
	public LogConfig(IHSConfig config){
		this.config = config;
	}

	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getLogPath() {
		return logPath;
	}
	public void setLogPath(String logPath) {
		this.logPath = logPath;
	}
	public static int getErrorLog() {
		return ERROR_LOG;
	}
	public static int getTransferLog() {
		return TRANSFER_LOG;
	}
	public static int getCustomLog() {
		return CUSTOM_LOG;
	}
	public static int getScriptLog() {
		return SCRIPT_LOG;
	}
	public static int getRewriteLog() {
		return REWRITE_LOG;
	}
	
	public String getLogFullPath(){
		String fullPath = "";
		if(logPath.trim().substring(0,1).equals("/")){
			fullPath = this.logPath;
		}else{
			fullPath = this.config.getIHS().getServerRoot() + this.logPath;
		}
		return fullPath;
	}
	
	public String getLogDir(){
		String fullPath = getLogFullPath();
		
		int index = fullPath.lastIndexOf("/");
		fullPath = fullPath.substring(0, index+1);

		return fullPath;
	}
	
	public static int getLogConfigType(String string) {
		if(string.equals("RewriteLog")){
			return LogConfig.REWRITE_LOG;
		} else if(string.equals("TransferLog")){
			return LogConfig.TRANSFER_LOG;
		} else if(string.equals("CustomLog")){
			return LogConfig.CUSTOM_LOG;
		} else if(string.equals("TransferLog")){
			return LogConfig.TRANSFER_LOG;
		} else if(string.equals("ScriptLog")){
			return LogConfig.SCRIPT_LOG;
		} else if(string.equals("ErrorLog")){
			return LogConfig.ERROR_LOG;
		} else {
			return -1;
		}
	}
	
	public static String getLogTypeDescription(int type2) {
		if(type2 == LogConfig.REWRITE_LOG){
			return "RewriteLog";
		} else if( type2 == LogConfig.TRANSFER_LOG){
			return "TransferLog";
		} else if(type2 == LogConfig.CUSTOM_LOG){
			return "CustomLog";
		} else if(type2 == LogConfig.TRANSFER_LOG){
			return "TransferLog";
		} else if(type2 == LogConfig.SCRIPT_LOG){
			return "ScriptLog";
		} else if(type2 == LogConfig.ERROR_LOG){
			return "ErrorLog";
		} else {
			return "Unknown";
		}
	}

}
