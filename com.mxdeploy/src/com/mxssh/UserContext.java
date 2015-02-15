package com.mxssh;

public class UserContext {

	private static String user;
	private static String password;
	private static String newPassword;
	
	public static String getNewPassword() {
		return newPassword;
	}
	public static void setNewPassword(String newPassword) {
		UserContext.newPassword = newPassword;
	}
	public static String getUser() {
		return user;
	}
	public static void setUser(String user) {
		UserContext.user = user;
	}
	public static String getPassword() {
		return password;
	}
	public static void setPassword(String password) {
		UserContext.password = password;
	}

	
	
}
