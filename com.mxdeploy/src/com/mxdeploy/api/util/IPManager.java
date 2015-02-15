package com.mxdeploy.api.util;

import java.net.InetAddress;

import java.net.UnknownHostException;
import org.apache.log4j.Logger;


public class IPManager {
	
		
	//Return ip as string
	public static String getIP(String host){
		InetAddress ia = null;
		try {
			ia = InetAddress.getByName(host);
		} catch (UnknownHostException e) {
			//LOGIPMANAGER.error("Unknown Host: " + host);
			return null;
		}
		
		if(ia != null){
			
			//LOGIPMANAGER.info(host + "(" + ia.getHostAddress() + ")" );
			return ia.getHostAddress();
			
		}else{
			
			//LOGIPMANAGER.error("Unknown Host: " + host);
			return null;
			
		}
	}

}
