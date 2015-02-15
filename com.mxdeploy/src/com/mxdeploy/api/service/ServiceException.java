package com.mxdeploy.api.service;

public class ServiceException extends Exception {

	/**
	 * Constructor for NotFoundException. The input message is returned in
	 * toString() message.
	 */
	public ServiceException(String msg) {
		super(msg);
	}

}
