package com.mxdeploy.api.domain;

import com.mxsecurity.util.PWSec;

public class PasswordManager {
	
	private String account;
	private String username;
	private String password;
	private String regex;
	private String email;	
	private String passwordDecrypted;
	private String title;
	private int id;
	
	/**
	 * @return the passwordDecrypted
	 */
	public String getPasswordDecrypted() {
		
		return passwordDecrypted;
	}
	/**
	 * @param passwordDecrypted the passwordDecrypted to set
	 */
	public void setPasswordDecrypted(String passwordDecrypted) {
		this.passwordDecrypted = passwordDecrypted;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the account
	 */
	public String getAccount() {
		return account;
	}
	/**
	 * @param account the account to set
	 */
	public void setAccount(String account) {
		this.account = account;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the regex
	 */
	public String getRegex() {
		return regex;
	}
	/**
	 * @param regex the regex to set
	 */
	public void setRegex(String regex) {
		this.regex = regex;
	}
	
	public static void main(String[] args){
		
		
		
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	

}
