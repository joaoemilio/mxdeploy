package com.mxssh.service;

import java.io.IOException;
import java.util.List;

import com.mxdeploy.api.domain.middleware.Middleware;

public abstract class MiddlewareService {

	/**
	 * Verify if the server has an instance of this product running and/or installed
	 * @throws InterruptedException 
	 * @throws IOException 
	 * 
	 */
	public abstract List<Middleware> retrieveMiddlewareList() throws IOException, InterruptedException;
	

	
}
