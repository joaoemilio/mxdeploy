package com.mxterminal.swt.util;

import java.util.ArrayList;
import java.util.List;

public class Update {
    
	private static Update update;
	private String dir;
	private String server;
	private List<String> files;
	private Integer checkDays;
	private Boolean enable;
	
	private Update(){}
	
	public static Update getInstance(){
		if(update==null){
		   update = new Update();
		}
		return update;
	}
	/**
	 * @return the checkDays
	 */
	public Integer getCheckDays() {
		return checkDays;
	}
	/**
	 * @param checkDays the checkDays to set
	 */
	public void setCheckDays(Integer checkDays) {
		this.checkDays = checkDays;
	}
	/**
	 * @return the dir
	 */
	public String getDir() {
		return dir;
	}
	/**
	 * @param dir the dir to set
	 */
	public void setDir(String dir) {
		this.dir = dir;
	}
	/**
	 * @return the enable
	 */
	public Boolean getEnable() {
		return enable;
	}
	/**
	 * @param enable the enable to set
	 */
	public void setEnable(Boolean enable) {
		this.enable = enable;
	}
	/**
	 * @return the files
	 */
	public List<String> getFiles() {
		if(files==null){
		   files = new ArrayList<String>();
		}
		return files;
	}
	/**
	 * @param files the files to set
	 */
	public void setFiles(List<String> files) {
		this.files = files;
	}
	/**
	 * @return the server
	 */
	public String getServer() {
		return server;
	}
	/**
	 * @param server the server to set
	 */
	public void setServer(String server) {
		this.server = server;
	}
	
	
}
