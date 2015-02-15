package com.mxdeploy;

import com.mxdeploy.api.domain.User;

public class UserContext {
	
	private static User user;
	
	public static void setUser(User value){
		user = value;
	}
	
	public static User getUser(){
		return user;
	}

}
