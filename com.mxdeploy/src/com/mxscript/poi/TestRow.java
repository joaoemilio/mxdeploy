package com.mxscript.poi;

import java.util.Date;

public class TestRow {

	private int index;
	private String string;
	private Long longInt;
	private Date date;
	
	public TestRow(int index, String string, Long longInt, Date date){
		this.date = date;
		this.index = index;
		this.longInt = longInt;
		this.string = string;
	}
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public Long getLongInt() {
		return longInt;
	}
	public void setLongInt(Long longInt) {
		this.longInt = longInt;
	}
	public String getString() {
		return string;
	}
	public void setString(String string) {
		this.string = string;
	}
	
	
	
}
