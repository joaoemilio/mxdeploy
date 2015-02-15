package com.mxdeploy.api.domain;

import java.util.ArrayList;
import java.util.List;

public class SimpleDataStructure extends TransferObject {

	private List<SimpleDataField> fieldList = new ArrayList<SimpleDataField>();
	private String name;
	private String description;
	
	public List<SimpleDataField> getFieldList() {
		return fieldList;
	}
	public void setFieldList(List<SimpleDataField> fieldList) {
		this.fieldList = fieldList;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void addField(SimpleDataField field) {
		fieldList.add(field);
	}
	
	public void removeField(SimpleDataField field) {
		fieldList.remove(field);
	}
	
	public void updateField(SimpleDataField field) {
		removeField(field);
		addField(field);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
