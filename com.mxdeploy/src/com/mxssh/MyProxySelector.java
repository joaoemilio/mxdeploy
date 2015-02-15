package com.mxssh;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

public class MyProxySelector extends ProxySelector {
	// Keep a reference on the previous default
    ProxySelector defsel = null;
	
	/*
	 * Inner class representing a Proxy and a few extra data
	 */
	class InnerProxy {
    	Proxy proxy;
		SocketAddress addr;
		// How many times did we fail to reach this proxy?
		int failedCount = 0;
		
		InnerProxy(InetSocketAddress a) {
			addr = a;
			proxy = new Proxy(Proxy.Type.SOCKS, a);
		}
		
		SocketAddress address() {
			return addr;
		}
		
		Proxy toProxy() {
			return proxy;
		}
		
		int failed() {
			return ++failedCount;
		}
	}
	
	/*
	 * A list of proxies, indexed by their address.
	 */
	HashMap<SocketAddress, InnerProxy> proxies = new HashMap<SocketAddress, InnerProxy>();

	public MyProxySelector(ProxySelector def) {
	  // Save the previous default
	  defsel = def;
	  
	  // Populate the HashMap (List of proxies)
	  InnerProxy i = new InnerProxy(new InetSocketAddress("129.39.94.82", 1080));
	  proxies.put(i.address(), i);	  
	  i = new InnerProxy(new InetSocketAddress("129.39.12.174", 1080));
	  proxies.put(i.address(), i);
	  i = new InnerProxy(new InetSocketAddress("129.39.26.90", 1080));
	  proxies.put(i.address(), i);

	  i = new InnerProxy(new InetSocketAddress("129.39.12.175", 1080));
	  proxies.put(i.address(), i);	 
	  i = new InnerProxy(new InetSocketAddress("129.39.94.83", 1080));
	  proxies.put(i.address(), i);	  
	}
	  
	  /*
	   * This is the method that the handlers will call.
	   * Returns a List of proxy.
	   */
	  public java.util.List<Proxy> select(URI uri) {
	    System.setProperty("socksProxyHost", "129.39.94.82" );
		// Let's stick to the specs. 
		if (uri == null) {
			throw new IllegalArgumentException("URI can't be null.");
		}
		
		ArrayList<Proxy> l = new ArrayList<Proxy>();
		for (InnerProxy p : proxies.values()) {
		  l.add(p.toProxy());
		}
		return l;
		
	}
	
	/*
	 * Method called by the handlers when it failed to connect
	 * to one of the proxies returned by select().
	 */
	public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
		// Let's stick to the specs again.
		if (uri == null || sa == null || ioe == null) {
			throw new IllegalArgumentException("Arguments can't be null.");
		}
		
		/*
		 * Let's lookup for the proxy 
		 */
		InnerProxy p = proxies.get(sa); 
			if (p != null) {
				/*
				 * It's one of ours, if it failed more than 3 times
				 * let's remove it from the list.
				 */
				if (p.failed() >= 3)
					proxies.remove(sa);
			} else {
				/*
				 * Not one of ours, let's delegate to the default.
				 */
				if (defsel != null)
				  defsel.connectFailed(uri, sa, ioe);
			}
     }
}
