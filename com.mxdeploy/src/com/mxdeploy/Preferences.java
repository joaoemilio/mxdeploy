package com.mxdeploy;

import java.util.TreeSet;

import com.mxdeploy.VariableExistsException;
import com.mxdeploy.api.domain.KeyValue;
import com.mxdeploy.api.domain.TransferObject;

public class Preferences extends TransferObject {

	private TreeSet<KeyValue> environmentVariables = new TreeSet<KeyValue>();
	private String editorPath;
	
	public void addEnvironmentVariable(KeyValue keyValue) throws VariableExistsException {
		if(getEnvVar(keyValue.getName()) != null) {
			throw new VariableExistsException("Variable " + keyValue.getName() + " already exists.");
		}
		environmentVariables.add(keyValue);
	}
	
	public void removeEnvironmentVariable(KeyValue keyValue) {
		environmentVariables.remove(keyValue);
	}
	
	public String getEnvVar(String key) {
		KeyValue result = null;
		for(KeyValue kv: environmentVariables) {
			if(kv.getName().equals(key) ) {
				result = kv;
				break;
			}
		}
		
		if(result != null) {
			return result.getValue(); 
		} else {
			return null;
		}
	}

	public void editVariable(KeyValue variable) {
		KeyValue result = null;
		for(KeyValue kv: environmentVariables) {
			if(kv.getName().equals(variable.getName()) ) {
				result = kv;
				break;
			}
		}
		
		if(result != null) {
			environmentVariables.remove(variable);
			environmentVariables.add(result);
		}
	}
	
	public TreeSet<KeyValue> getVariables() {
		return this.environmentVariables;
	}

	public String getEditorPath() {
		return editorPath;
	}

	public void setEditorPath(String editorPath) {
		this.editorPath = editorPath;
	}
	
}
