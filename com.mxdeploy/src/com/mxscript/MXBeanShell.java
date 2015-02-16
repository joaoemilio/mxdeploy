package com.mxscript;

import java.io.IOException;
import java.net.InetAddress;

import org.apache.log4j.Logger;

import com.mxssh.SCPClient;

public abstract class MXBeanShell {

	protected Logger logger = Logger.getRootLogger();
	protected SCPClient scp = new SCPClient();
	protected String hostName = null;
	protected String ipAddress = null;
	protected String userName = null;
	protected String password = null;
	protected MXBSListener listener;
	protected MXBSDomain domain;
	
	public MXBeanShell(MXBSListener listener) {
		this.listener = listener;
	}
	
	protected void initialize(String hostName, String userName, String password) {
		this.hostName = hostName;
		this.userName = userName;
		this.password = password;
	}
		
	public abstract MXBSDomain execute(MXBSDomain domain) throws MXBeanShellException;

	protected String getIPAddress(String hostname) throws IOException {
		InetAddress ia = null;
		String ipAddress = null;
		ia = InetAddress.getByName(hostname);
		logger.debug( ""+ia.isReachable(5000) );
		logger.debug(""+ia.getHostAddress());
		ipAddress = ia.getHostAddress();
		return ipAddress;
	}
	
	public MXBSDomain getDomain() {
		return this.domain;
	}
}
