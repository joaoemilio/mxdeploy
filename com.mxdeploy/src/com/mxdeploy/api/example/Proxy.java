package com.mxdeploy.api.example;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.ProxySOCKS5;
import com.jcraft.jsch.Session;

public class Proxy {

	/**
	 * @param args
	 * @throws JSchException 
	 */
	public static void main(String[] args) throws JSchException {
		JSch jsch=new JSch();
		Session session=jsch.getSession("fsilva", "159.202.157.196", 22);
		session.setProxy(new ProxySOCKS5("129.39.12.174"));
		session.setConfig("StrictHostKeyChecking", "no");
		session.setPassword("Yc35FgGL");
		session.connect();
		
	    Channel channel=session.openChannel("shell");

	    channel.setInputStream(System.in);
	    channel.setOutputStream(System.out);

	    channel.connect();

	}
	
}
