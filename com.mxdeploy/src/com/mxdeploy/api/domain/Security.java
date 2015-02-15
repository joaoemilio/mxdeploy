package com.mxdeploy.api.domain;

import java.util.ArrayList;
import java.util.List;

public class Security {

	private User user;
	private List<PasswordManager> passwordManagerList = new ArrayList<PasswordManager>();
	
	/**
	 * @return the passwordManagerList
	 */
	public List<PasswordManager> getPasswordManagerList() {
		return passwordManagerList;
	}
	/**
	 * @param passwordManagerList the passwordManagerList to set
	 */
	public void addPasswordManager(PasswordManager passwordManager) {
		this.passwordManagerList.add( passwordManager );
	}
	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
}
