package com.mxdeploy.api.domain.operatingsystem;

import java.util.ArrayList;
import java.util.List;

public class Sudoers {

	private List<Hostalias> listHostaliases = new ArrayList<Hostalias>();
	private List<Runalias> listRunaliases = new ArrayList<Runalias>();
	private List<Useralias> listUseralias = new ArrayList<Useralias>();
	private List<Cmndalias> listCmndalias = new ArrayList<Cmndalias>();
	private List<Defaults> listDefaults = new ArrayList<Defaults>();
	private List<UserSpecification> listUserSpecification = new ArrayList<UserSpecification>();
	
	public void addUserSpecification(UserSpecification userSpecification){
		listUserSpecification.add(userSpecification);
	}
	
	public List<UserSpecification> getListUserSpecification() {
		return listUserSpecification;
	}

	public void setListUserSpecification(
			List<UserSpecification> listUserSpecification) {
		this.listUserSpecification = listUserSpecification;
	}

	public List<Hostalias> getListHostaliases() {
		return listHostaliases;
	}

	public void setListHostaliases(List<Hostalias> listHostaliases) {
		this.listHostaliases = listHostaliases;
	}

	public List<Runalias> getListRunaliases() {
		return listRunaliases;
	}

	public void setListRunaliases(List<Runalias> listRunaliases) {
		this.listRunaliases = listRunaliases;
	}

	public List<Useralias> getListUseralias() {
		return listUseralias;
	}

	public void setListUseralias(List<Useralias> listUseralias) {
		this.listUseralias = listUseralias;
	}

	public List<Cmndalias> getListCmndalias() {
		return listCmndalias;
	}

	public void setListCmndalias(List<Cmndalias> listCmndalias) {
		this.listCmndalias = listCmndalias;
	}

	public List<Defaults> getListDefaults() {
		return listDefaults;
	}

	public void setListDefaults(List<Defaults> listDefaults) {
		this.listDefaults = listDefaults;
	}

	public class UserSpecification {
		private String name;
		private String hostalias;
		private String operatorList;
		private List<String> commandList = new ArrayList<String>();
		
		public void addCommand(String command){
			commandList.add(command);
		}
		
		public List<String> getCommandList() {
			return commandList;
		}
		public void setCommandList(List<String> commandList) {
			this.commandList = commandList;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getHostalias() {
			return hostalias;
		}
		public void setHostalias(String hostalias) {
			this.hostalias = hostalias;
		}
		public String getOperatorList() {
			return operatorList;
		}
		public void setOperatorList(String value) {
			this.operatorList = value;
		}
		
	}
	
	public class Alias {
		protected String name;
		protected List<String> values = new ArrayList<String>();
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
		public void addValue(String value){
			values.add(value);
		}
		
		public List<String> getValues() {
			return values;
		}
		public void setValues(List<String> values) {
			this.values = values;
		}
		
	}
	
	public class Hostalias extends Sudoers.Alias {
		
	}
	
	public class Runalias  extends Sudoers.Alias {
		
	}
	
	public class Useralias  extends Sudoers.Alias {
		
	}
	
	public class Cmndalias  extends Sudoers.Alias {
		
	}
	
	public class Defaults  extends Sudoers.Alias {
		
	}
	
}
