package com.mxdeploy;

import jsocks.socks.CProxy;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.ProxySOCKS5;
import com.jcraft.jsch.Session;
import com.mxssh.DynamicForwarder;

public class DynamicPort implements Runnable { 
	
	public static void main(String args[]){
		DynamicPort dport = new DynamicPort();
		Thread thread = new Thread(dport, "DynamicForwader");
		thread.start();
	}

	@Override
	public void run() {
		//ssh -f -nN -D 8890 -i /tmp/id_rsa_fsilva fsilva@159.202.162.233 -o NumberOfPasswordPrompts=1 -o "ProxyCommand connect-proxy -R both -5 -S 129.39.94.82:1080 %h %p"
		JSch jsch = new JSch();
		try {
//			Session session = jsch.getSession("fsilva", "167.210.245.161");
//	        session.setConfig("StrictHostKeyChecking", "no");
//	        session.setPassword("8#hLdDfk");
//	        session.connect();
//
//	        DynamicForwarder dynamic = new DynamicForwarder(8888,session);

			System.setProperty("socksProxyHost", "129.39.94.82");
	        System.setProperty("socksProxyPort", "1080");
	        
			Session session2 = jsch.getSession("fsilva", "159.202.162.233");
//			ProxySOCKS5 proxy = new ProxySOCKS5("129.39.94.82", 1080);
//			session2.setProxy(proxy);
			
			session2.setConfig("StrictHostKeyChecking", "no");
			session2.setPassword("p@dv3xkm");
			
			session2.connect();
			
	        DynamicForwarder dynamic2 = new DynamicForwarder(8890,session2);
	        Thread thread = new Thread(dynamic2,"DynamicForwarder");
	        thread.start();
	        
//        	session.setProxy(proxySOCKS5);
//
//			ssh.connect("167.210.245.158", "fsilva", "8#hLdDfk");
//			
//			DynamicForwarder dynamic = new DynamicForwarder(8888,ssh.getSshSession());
//			//dynamic.getDynamicForward().getProxy()
//			
//			ProxySOCKS5 proxy = new ProxySOCKS5("127.0.0.1", 8888);
//			
//			
//			SSHServiceNew ssh2 = new SSHServiceNew();
//			ssh2.setProxySOCKS5(proxy);
//			
//			ssh2.connect("148.173.219.135", "fsilva", "8#hLdDfk");
//			DynamicForwarder dynamic2 = new DynamicForwarder(8889,ssh2.getSshSession());
			
			
//			while(true){
//				Thread.sleep(3000);
//			}
		} catch (JSchException e) {
			e.printStackTrace();
		}
		
	}

}
