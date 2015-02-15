package com.mxdeploy.api.domain;

import java.util.Comparator;

public class Template implements  Comparator<Template> {

	private String title;
	private String value;
	private String fileName;
	
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	public int compare(Template o1, Template o2) {
		// TODO Auto-generated method stub
		return o1.getTitle().compareTo(o2.getTitle());
	}
	
	
}
