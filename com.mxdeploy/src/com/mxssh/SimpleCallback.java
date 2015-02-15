package com.mxssh;

public class SimpleCallback implements Callback {

	private String message;
	
	public void sendMessage(String message) {
		//this.message = message;
	}

	public void sendMessageEnd(String message) {
		this.message = message;
	}
	
	public String getMessage(){
		return this.message;
	}
	
	

}
