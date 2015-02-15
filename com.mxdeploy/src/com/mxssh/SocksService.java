package com.mxssh;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.mxdeploy.AccountConfig;
import com.mxdeploy.SocksConfig;
import com.mxdeploy.api.domain.Server;
import com.mxdeploy.api.service.ServerService;
import com.mxsecurity.util.PWSec;
import com.mxterminal.ssh.ProxyAuth;

public class SocksService {
	
	public void setupSocksByHostname(String hostname) {
		try {
			SocksConfig socksConfig = null;

			if(hostname != null) {
				ServerService sservice = new ServerService();
				Server server = sservice.getServer(hostname);
			
			
				if(server != null) {
					if(server.getDefaultSocksUUID() != null && !server.getDefaultSocksUUID().equals("")) {
						socksConfig = AccountConfig.getInstance().getSocksConfigByUUID(server.getDefaultSocksUUID());
					}
				} 
			}
			
			if(socksConfig == null && AccountConfig.getInstance()!=null ) {
				socksConfig = AccountConfig.getInstance().getActiveSocks();
			}

			if( socksConfig != null) {
	    		System.setProperty("socksProxyHost", socksConfig.getSocksProxyHost() );
	        	if(socksConfig.getAuthenticationRequired()) {
	        		String sockspassword = "";
					sockspassword = PWSec.decrypt(socksConfig.getEncondedPassword());
	                System.setProperty("java.net.socks.username", socksConfig.getUsername());
	                System.setProperty("java.net.socks.password", sockspassword);
	                java.net.Authenticator.setDefault(new ProxyAuth( socksConfig.getUsername(), sockspassword ));
	        	}
        	} else {
                System.setProperty("java.net.socks.username", "");
                System.setProperty("java.net.socks.password", "");
                java.net.Authenticator.setDefault(null);
        	}
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
	}

	public void setupSocks(SocksConfig socksConfig) {
		if(socksConfig == null) { 
			this.setupSocksByHostname(null);
			return;
		}
		
		try {
			System.out.println("socks config hostname: " + socksConfig.getSocksProxyHost());
    		System.setProperty("socksProxyHost", socksConfig.getSocksProxyHost() );
        	if(socksConfig.getAuthenticationRequired()) {
        		String sockspassword = "";
				sockspassword = PWSec.decrypt(socksConfig.getEncondedPassword());
                System.setProperty("java.net.socks.username", socksConfig.getUsername());
                System.setProperty("java.net.socks.password", sockspassword);
                java.net.Authenticator.setDefault(new ProxyAuth( socksConfig.getUsername(), sockspassword ));
        	} else {
    			System.out.println("socks config auth not required");
        		System.setProperty("java.net.socks.username", "");
        		System.setProperty("java.net.socks.password", "");
        		java.net.Authenticator.setDefault(null);
        	}
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
	}

}
