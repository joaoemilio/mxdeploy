package com.mxterminal.ssh;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.ProxySOCKS5;
import com.mxdeploy.AccountConfig;
import com.mxdeploy.SocksConfig;
import com.mxdeploy.api.domain.Database;
import com.mxdeploy.api.domain.Server;
import com.mxsecurity.jca.MessageDigest;
import com.mxsecurity.jca.SignatureException;
import com.mxsecurity.jca.interfaces.RSAPrivateCrtKey;
import com.mxsecurity.jca.interfaces.RSAPublicKey;
import com.mxsecurity.publickey.RSAAlgorithm;
import com.mxsecurity.util.SecureRandomAndPad;
import com.mxssh.ManageGateway;
import com.mxterminal.channel.ChannelController;
import com.mxterminal.console.ConsoleWindows;
import com.mxterminal.net.SocketMainFactory;
import com.mxterminal.swt.util.Socks5Property;
import com.mxterminal.swt.util.TerminalDomainRule;
import com.mxterminal.swt.util.TerminalProperty;

public class ClientSSH extends SSH {

	static public class AuthFailException extends IOException {
		public AuthFailException(String msg) {
			super(msg);
		}

		public AuthFailException() {
			this("permission denied");
		}
	}

	static public class ExitMonitor implements Runnable {
		ClientSSH client;
		long msTimeout;

		public ExitMonitor(ClientSSH client, long msTimeout) {
			this.msTimeout = msTimeout;
			this.client = client;
		}

		public ExitMonitor(ClientSSH client) {
			this(client, 0);
		}

		public void run() {
			client.waitForExit(msTimeout);
			// If we have allready exited gracefully don't report...
			//
			if (!client.gracefulExit)
				client.disconnect(false);
		}
	}

	private class KeepAliveThread extends Thread {
		int interval;

		public KeepAliveThread(int i) {
			super("SSH1KeepAlive");
			interval = i;
		}

		public synchronized void setInterval(int i) {
			interval = i;
		}

		public void run() {
			int i;
			PduOutputStreamSSH ignmsg;
			while (true) {
				try {
					synchronized (this) {
						i = interval;
					}
					sleep(1000 * i);
					if (ClientSSH.this.controller != null) {
						ignmsg = new PduOutputStreamSSH(MSG_DEBUG,
								controller.sndCipher, rand);
						ignmsg.writeString("heartbeat");
						controller.transmit(ignmsg);
					}
				} catch (Exception e) {
					
					// !!!
				}
			}
		}
	}

	// Local port forwarding
	//
	public static class LocalForward {
		protected String localHost;
		protected int localPort;
		protected String remoteHost;
		protected int remotePort;
		protected String plugin;

		public LocalForward(String localHost, int localPort, String remoteHost,
				int remotePort, String plugin) {
			this.localHost = localHost;
			this.localPort = localPort;
			this.remoteHost = remoteHost;
			this.remotePort = remotePort;
			this.plugin = plugin;
		}
	}

	// Remote port forwarding
	//
	public static class RemoteForward {
		protected String remoteHost;
		protected int remotePort;
		protected String localHost;
		protected int localPort;
		protected String plugin;

		public RemoteForward(String remoteHost, int remotePort,
				String localHost, int localPort, String plugin) {
			this.remoteHost = remoteHost;
			this.remotePort = remotePort;
			this.localHost = localHost;
			this.localPort = localPort;
			this.plugin = plugin;
		}
	}

	protected Thread myThread;
	protected KeepAliveThread keepAliveThread;
	protected SecureRandomAndPad rand;

	// protected InetAddress serverAddr;
	protected InetAddress serverRealAddr = null;
	protected InetAddress localAddr;
	protected String srvVersionStr;
	protected int srvVersionMajor;
	protected int srvVersionMinor;

	protected Vector localForwards;
	protected Vector remoteForwards;
	protected String commandLine;

	protected ChannelController controller;
	protected ConsoleSSH console;
	protected Authenticator authenticator;
	public ClientUserSSH user;
	protected InteractorSSH interactor;

	protected Socket sshSocket;
	protected BufferedInputStream sshIn;
	protected BufferedOutputStream sshOut;

	protected boolean gracefulExit;
	protected boolean isConnected;
	protected boolean isOpened;

	boolean usedOTP;

	protected int refCount;

	// !!! KLUDGE
	public boolean havePORTFtp = false;
	public int firstFTPPort = 0;
	public boolean activateTunnels = true;

	// !!! KLUDGE

	public ClientSSH(Authenticator authenticator, ClientUserSSH user) {
		this.user = user;
		this.authenticator = authenticator;
		this.interactor = user.getInteractor();
		this.srvVersionStr = null;
		this.refCount = 0;
		this.usedOTP = false;

		try {
			this.localAddr = InetAddress.getByName("0.0.0.0");
		} catch (UnknownHostException e) {
			if (interactor != null)
				interactor.alert("FATAL: Could not create local InetAddress: "
						+ e.getMessage());
		}
		clearAllForwards();
	}

	public void setConsole(ConsoleSSH console) {
		this.console = console;
		if (controller != null)
			controller.console = console;
	}

	public ConsoleSSH getConsole() {
		return console;
	}

	// public InetAddress getServerAddr() {
	// return serverAddr;
	// }
	//
	// public InetAddress getServerRealAddr() {
	// if(serverRealAddr == null)
	// return serverAddr;
	// return serverRealAddr;
	// }

	public void setServerRealAddr(InetAddress realAddr) {
		serverRealAddr = realAddr;
	}

	public InetAddress getLocalAddr() {
		return localAddr;
	}

	public void setLocalAddr(String addr) throws UnknownHostException {
		localAddr = InetAddress.getByName(addr);
	}

	public String getServerVersion() {
		return srvVersionStr;
	}

	public void addLocalPortForward(int localPort, String remoteHost,
			int remotePort, String plugin) throws IOException {
		addLocalPortForward(localAddr.getHostAddress(), localPort, remoteHost,
				remotePort, plugin);
	}

	public void addLocalPortForward(String localHost, int localPort,
			String remoteHost, int remotePort, String plugin)
			throws IOException {
		delLocalPortForward(localHost, localPort);
		localForwards.addElement(new LocalForward(localHost, localPort,
				remoteHost, remotePort, plugin));
		if (isOpened) {
			try {
				requestLocalPortForward(localHost, localPort, remoteHost,
						remotePort, plugin);
			} catch (IOException e) {
				delLocalPortForward(localHost, localPort);
				throw e;
			}
		}
	}

	public void delLocalPortForward(String localHost, int port) {
		if (port == -1) {
			if (isOpened)
				controller.killListenChannels();
			localForwards = new Vector();
		} else {
			for (int i = 0; i < localForwards.size(); i++) {
				LocalForward fwd = (LocalForward) localForwards.elementAt(i);
				if (fwd.localPort == port && fwd.localHost.equals(localHost)) {
					localForwards.removeElementAt(i);
					if (isOpened)
						controller.killListenChannel(fwd.localHost,
								fwd.localPort);
					break;
				}
			}
		}
	}

	public void addRemotePortForward(String remoteHost, int remotePort,
			String localHost, int localPort, String plugin) {
		delRemotePortForward(remoteHost, remotePort);
		remoteForwards.addElement(new RemoteForward(remoteHost, remotePort,
				localHost, localPort, plugin));
	}

	public void delRemotePortForward(String remoteHost, int port) {
		if (port == -1) {
			remoteForwards = new Vector();
		} else {
			for (int i = 0; i < remoteForwards.size(); i++) {
				RemoteForward fwd = (RemoteForward) remoteForwards.elementAt(i);
				if (fwd.remotePort == port && fwd.remoteHost.equals(remoteHost)) {
					remoteForwards.removeElementAt(i);
					break;
				}
			}
		}
	}

	public void delRemotePortForward(String plugin) {
		for (int i = 0; i < remoteForwards.size(); i++) {
			RemoteForward fwd = (RemoteForward) remoteForwards.elementAt(i);
			if (fwd.plugin.equals(plugin)) {
				remoteForwards.removeElementAt(i);
				i--;
			}
		}
	}

	public void clearAllForwards() {
		this.localForwards = new Vector();
		this.remoteForwards = new Vector();
	}

	public void startExitMonitor() {
		startExitMonitor(0);
	}

	public void startExitMonitor(long msTimeout) {
		Thread t = new Thread(new ExitMonitor(this, msTimeout));
		t.setName("ExitMonitor");
		t.start();
	}

	public synchronized int addRef() {
		return ++refCount;
	}

	public void forcedDisconnect() {
		if (controller != null) {
			controller.sendDisconnect("exit");
			controller.killAll();
		} else if (interactor != null) {
			interactor.disconnected(this, false);
		}
	}

	public synchronized int delRef() {
		if (--refCount <= 0) {
			forcedDisconnect();
			waitForExit(2000);
		}
		return refCount;
	}

	public void waitForExit(long msTimeout) {
		try {
			controller.waitForExit(msTimeout);
		} catch (InterruptedException e) {
			if (interactor != null)
				interactor.alert("Error when shutting down SSHClient: "
						+ e.getMessage());
			controller.killAll();
		}
		try {
			if (sshSocket != null)
				sshSocket.close();
		} catch (IOException e) {
			// !!!
		}
	}

	public void doSingleCommand(String commandLine, boolean background,
			long msTimeout) throws IOException {
		this.commandLine = commandLine;
		bootSSH(false);
		if (background)
			startExitMonitor(msTimeout);
		else
			waitForExit(msTimeout);
	}

	public long getConnectTimeout() {
		return 60 * 1000;
	}

	public long getProxyConnectTimeout() {
		return 10 * 1000;
	}

	public long getHelloTimeout() {
		return 10 * 1000;
	}

	public void bootSSH(boolean haveCnxWatch) throws IOException {
		bootSSH(haveCnxWatch, false);
	}

	public void bootSSH(boolean haveCnxWatch, boolean releaseConnection)
			throws IOException {
		try {
			myThread = Thread.currentThread();

			rand = secureRandom();

			// Give the interactor a chance to hold us until the user wants to
			// "connect" (e.g. with a dialog with server, username, password,
			// proxy-info)
			//
			if (interactor != null)
				interactor.startNewSession(this);

			// We first ask for the ssh server address since this might
			// typically be a prompt in the SSHClientUser
			//
			String serverAddrStr = user.getSrvHost();
			if (!ManageGateway.isStarted) {
				if (AccountConfig.getInstance() != null
						&& AccountConfig.getInstance().isConnectViaIP()) {
					for (Server server : Database.getInstance().getServerList()) {
						if (server.getHostname().contains(serverAddrStr)) {
							if (server.getAddress() != null
									&& server.getAddress().trim().length() > 0) {
								serverAddrStr = server.getAddress();
								break;
							}
						}
					}
				}
			}

			// When the SSHClientUser has reported which host to
			// connect to we report this to the interactor as
			// sessionStarted
			//
			if (interactor != null)
				interactor.sessionStarted(this);

			sshSocket = null;

			if (serverAddrStr.contains(":")) {
				StringTokenizer tokenPort = new StringTokenizer(serverAddrStr,":");
				String serverName = tokenPort.nextToken().trim();
				String portNumber = tokenPort.nextToken().trim();

				Integer intPort = new Integer(portNumber);
				if (intPort != 22) {
					if( connectGateway(serverName, intPort) ){
						return;
					}
					//IF Proxy didn't work. Try to use without proxy to avoid NullPointer Exception
					sshSocket = SocketMainFactory.newSocket(serverAddrStr,user.getSrvPort(), getConnectTimeout());					
				} else {
					connect(serverAddrStr);
				}

			} else if (serverAddrStr.equals("localhost") || serverAddrStr.equals("127.0.0.1")) {
				sshSocket = SocketMainFactory.newSocket(serverAddrStr,user.getSrvPort(), getConnectTimeout());
			} else if (ManageGateway.isStarted) {
				if (TerminalProperty.getSocks5PropertyList() != null && TerminalProperty.getSocks5PropertyList().size() > 0) {
					Integer port = 22;
					for (Socks5Property socksprop : TerminalProperty.getSocks5PropertyList()) {
						String regex = socksprop.getRegexp();
						Pattern pattern = Pattern.compile(regex);

						Matcher matcher = pattern.matcher(serverAddrStr);

						if( serverAddrStr.matches(regex) && matcher.matches() && socksprop.getPort() != null) {
							port = Integer.valueOf(socksprop.getPort());
							if( connectGateway(serverAddrStr, port) ){
								return;
							}
						}
					}
					//IF Proxy didn't work. Try to use without proxy to avoid NullPointer Exce
					sshSocket = SocketMainFactory.newSocket(serverAddrStr,user.getSrvPort(), getConnectTimeout());
				} else {
					connect(serverAddrStr);
				}
			} else if (AccountConfig.getInstance() != null 
					&& AccountConfig.getInstance().getSocksList() != null
					&& AccountConfig.getInstance().getSocksList().size() > 0) {
				// Check if there is any PROXY configured
				for( SocksConfig socksConfig : AccountConfig.getInstance().getSocksList()) {
					 if( connectProxy(socksConfig.getSocksProxyHost(), serverAddrStr) ){
						 return;
					 }
					//IF Proxy didn't work. Try to use without proxy to avoid NullPointer Exception
					sshSocket = SocketMainFactory.newSocket(serverAddrStr,user.getSrvPort(), getConnectTimeout());
				}
			} else {
				try {
					sshSocket = SocketMainFactory.newSocket(serverAddrStr,	user.getSrvPort(), getConnectTimeout());
				} catch (IOException e) {
					if (TerminalProperty.getTerminalDomainRuleList() != null && TerminalProperty.getTerminalDomainRuleList().size() > 0) {
						connectDomainRule(serverAddrStr);
					} else {
						try {
							if( sshSocket!=null && sshSocket.isConnected() ){ 
								sshSocket.close();
							}
						} catch (IOException e1) {
							e.printStackTrace();
						}
						e.printStackTrace();
					}
				}				
				
			}

//			if (releaseConnection) {
//				return;
//			}
//
//			boot(haveCnxWatch, sshSocket);

		} catch (IOException e) {
			if (sshSocket != null)
				sshSocket.close();
			disconnect(false);
			if (controller != null) {
				controller.killListenChannels();
			}
			controller = null;
			throw e;
		}
	}

	public boolean connectGateway(String serverAddrStr, int port) {
		ProxySOCKS5 proxySOCKS5new = new ProxySOCKS5("localhost", port);
		try {
			proxySOCKS5new.connect(null, serverAddrStr, 22, 0);
			sshSocket = proxySOCKS5new.getSocket();
            sshSocket = new VersionSpySocketSSH(sshSocket);
            ((VersionSpySocketSSH)sshSocket).getMajorVersion();

			return true;
		} catch (JSchException e) {
			if (TerminalProperty.getTerminalDomainRuleList() != null && TerminalProperty.getTerminalDomainRuleList().size() > 0) {
				return connectDomainRuleGateway(serverAddrStr, port, proxySOCKS5new);
			} else {
				try {
					sshSocket.close();
				} catch (IOException e1) {
					e.printStackTrace();
					return false;
				}
				e.printStackTrace();
				return false;
			}
		} catch (IOException e) {
			try {
				sshSocket.close();
			} catch (IOException e1) {
				e.printStackTrace();
				return false;
			}			
			if (TerminalProperty.getTerminalDomainRuleList() != null && TerminalProperty.getTerminalDomainRuleList().size() > 0) {
				return connectDomainRuleGateway(serverAddrStr, port, proxySOCKS5new);
			} else {
				try {
					sshSocket.close();
				} catch (IOException e1) {
					e.printStackTrace();
					return false;
				}
				e.printStackTrace();
				return false;
			}
		}
	}
	
	public boolean connectProxy(String proxyHost, String serverAddrStr) {
		ProxySOCKS5 proxySOCKS5new = new ProxySOCKS5(proxyHost, 1080);
		try {
			proxySOCKS5new.connect(null, serverAddrStr, 22, 500);
			proxySOCKS5new.getSocket().close();
			proxySOCKS5new.connect(null, serverAddrStr, 22, 0);
			sshSocket = proxySOCKS5new.getSocket();
			return true;
		} catch (JSchException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}	
	
	

	public boolean connect(String serverAddrStr) {
		try {
			sshSocket = SocketMainFactory.newSocket(serverAddrStr, 22, 500);
			sshSocket.close();
			sshSocket = SocketMainFactory.newSocket(serverAddrStr, 22, 0);
			return true;
		} catch (UnknownHostException e) {
			return connectDomainRule(serverAddrStr);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean connectDomainRuleGateway(String serverAddrStr, int port, ProxySOCKS5 proxySOCKS5new) {
	    String regexIP = "\\d?\\d?\\d[.]\\d?\\d?\\d[.]\\d?\\d?\\d[.]\\d?\\d?\\d";  
	    Pattern patternIP = Pattern.compile( regexIP );  
	    Matcher matcherIP = patternIP.matcher( serverAddrStr );         
	    
	    if( serverAddrStr.matches( regexIP ) && matcherIP.matches() ) {
	        return false; 
	    }

		for(TerminalDomainRule tdr : TerminalProperty.getTerminalDomainRuleList()) {
			StringTokenizer token = new StringTokenizer(serverAddrStr, ".");
			String serverName = token.nextToken();
			String regex = tdr.getRegexp();
			Pattern pattern = Pattern.compile(regex);

			Matcher matcher = pattern.matcher(serverName);

			if (serverName.matches(regex)) {
				if (matcher.matches()) {

					String domainName = tdr.getDomainName();
					if (domainName.startsWith(".")) {
						domainName = domainName.substring(1);
					}
					serverName = serverName + "." + domainName;
					/*
					 * If the gateway using TIMEOUT passed, we must disconnect
					 * and connect again without TIMEOUT
					 */
					try {
						proxySOCKS5new.connect(null, serverName, 22, 500);
						proxySOCKS5new.getSocket().close();						
						proxySOCKS5new.connect(null, serverName, 22, 0);
						sshSocket = proxySOCKS5new.getSocket();
			            sshSocket = new VersionSpySocketSSH(sshSocket);
			            ((VersionSpySocketSSH)sshSocket).getMajorVersion();
						return true;
					} catch (ConnectException e) {
						e.printStackTrace();
					} catch (IOException e) {
						try {
							proxySOCKS5new.getSocket().close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					} catch (JSchException e) {
						try {
							proxySOCKS5new.getSocket().close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}	
						e.printStackTrace();
					}
				}
			}
		}
		
		return false;
	}
	
	public boolean connectDomainRule(String serverAddrStr) {
	    String regexIP = "\\d?\\d?\\d[.]\\d?\\d?\\d[.]\\d?\\d?\\d[.]\\d?\\d?\\d";  
	    Pattern patternIP = Pattern.compile( regexIP );  
	    Matcher matcherIP = patternIP.matcher( serverAddrStr );         
	    
	    if( serverAddrStr.matches( regexIP ) && matcherIP.matches() ) {
	        return false; 
	    }

		for (TerminalDomainRule tdr : TerminalProperty.getTerminalDomainRuleList()) {
			StringTokenizer token = new StringTokenizer(serverAddrStr, ".");
			String serverName = token.nextToken();
			String regex = tdr.getRegexp();
			Pattern pattern = Pattern.compile(regex);

			Matcher matcher = pattern.matcher(serverName);

			if (serverName.matches(regex)) {
				if (matcher.matches()) {
					String domainName = tdr.getDomainName();
					if (domainName.startsWith(".")) {
						domainName = domainName.substring(1);
					}
					serverName = serverName + "." + domainName;

					/*
					 * If the gateway using TIMEOUT passed, we must disconnect
					 * and connect again without TIMEOUT
					 */
					try {
						sshSocket = SocketMainFactory.newSocket(serverName, 22,	500);
						sshSocket.close();
						sshSocket = SocketMainFactory.newSocket(serverName, 22,	0);
						return true;
					} catch (UnknownHostException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}
		}
		return false;
	} 

	public void boot(boolean haveCnxWatch, Socket tpSocket) throws IOException {
		try {
			this.sshSocket = tpSocket;

			sshIn = new BufferedInputStream(sshSocket.getInputStream(), 8192);
			sshOut = new BufferedOutputStream(sshSocket.getOutputStream());

			long hellotimeout = getHelloTimeout();
			int oldtimeout = tpSocket.getSoTimeout();
			if (hellotimeout > 0)
				tpSocket.setSoTimeout((int) hellotimeout);
			negotiateVersion();
			if (hellotimeout > 0)
				tpSocket.setSoTimeout(oldtimeout);

			// We now have a physical connection to a sshd, report this to the
			// SSHClientUser
			//
			isConnected = true;
			if (interactor != null)
				interactor.connected(this);

			String userName = authenticator.getUsername(user);

			receiveServerData();

			initiatePlugins();

			cipherType = authenticator.getCipher(user);

			// Check that selected cipher is supported by server
			//
			if (!isCipherSupported(cipherType))
				throw new IOException("Sorry, server does not support the '"
						+ getCipherName(authenticator.getCipher(user))
						+ "' cipher.");

			generateSessionId();
			generateSessionKey();

			initClientCipher();

			sendSessionKey(cipherType);

			// !!!
			// At this stage the communication is encrypted
			// !!!

			authenticateUser(userName);

			//
			//
			// requestCompression(user.getCompressionLevel());

			controller = new ChannelController(this, sshIn, sshOut, sndCipher,
					rcvCipher, console, haveCnxWatch);

			initiateSession();

			if (console != null)
				console.serverConnect(controller, sndCipher);

			// We now open the SSH-protocol fully, report to SSHClientUser
			//
			isOpened = true;
			if (interactor != null)
				interactor.open(this);

			// Start "heartbeat" if needed
			//
			setAliveInterval(user.getAliveInterval());

			controller.start();

		} catch (IOException e) {
			if (sshSocket != null)
				sshSocket.close();
			disconnect(false);
			if (controller != null) {
				controller.killListenChannels();
			}
			controller = null;
			throw e;
		}
	}

	public void disconnect(boolean graceful) {
		if (!isConnected)
			return;
		isConnected = false;
		isOpened = false;
		gracefulExit = graceful;
		srvVersionStr = null;
		setAliveInterval(0); // Stop "heartbeat"...
		if (interactor != null) {
			interactor.disconnected(this, graceful);
			String msg = "";
		}
	}

	void negotiateVersion() throws IOException {
		byte[] buf = new byte[256];
		int len;
		String verStr;

		try {
			len = sshIn.read(buf);
			srvVersionStr = new String(buf, 0, len);
		} catch (Throwable t) {
			throw new IOException("Could not negotiate SSH version with server");
		}

		try {
			int l = srvVersionStr.indexOf('-');
			int r = srvVersionStr.indexOf('.');
			srvVersionMajor = Integer.parseInt(srvVersionStr
					.substring(l + 1, r));
			l = r;
			r = srvVersionStr.indexOf('-', l);
			if (r == -1) {
				srvVersionMinor = Integer.parseInt(srvVersionStr
						.substring(l + 1));
			} else {
				srvVersionMinor = Integer.parseInt(srvVersionStr.substring(
						l + 1, r));
			}
		} catch (Throwable t) {
			throw new IOException("Server version string invalid: "
					+ srvVersionStr);
		}

		if (srvVersionMajor > 1) {
			throw new IOException("This server doesn't support ssh1, connect"
					+ " with ssh2 enabled");
		} else if (srvVersionMajor < 1 || srvVersionMinor < 5) {
			throw new IOException("Server's protocol version ("
					+ srvVersionMajor + "-" + srvVersionMinor
					+ ") is too old, please upgrade");
		}

		// Strip white-space
		srvVersionStr = srvVersionStr.trim();

		verStr = getVersionId(true);
		verStr += "\n";
		buf = verStr.getBytes();

		sshOut.write(buf);
		sshOut.flush();
	}

	void receiveServerData() throws IOException {
		PduInputStreamSSH pdu = new PduInputStreamSSH(SMSG_PUBLIC_KEY, null);
		pdu.readFrom(sshIn);
		int bits;
		BigInteger e, n;

		srvCookie = new byte[8];
		pdu.read(srvCookie, 0, 8);

		bits = pdu.readInt();
		e = pdu.readBigInteger();
		n = pdu.readBigInteger();
		srvServerKey = new com.mxsecurity.publickey.RSAPublicKey(n, e);

		bits = pdu.readInt();
		e = pdu.readBigInteger();
		n = pdu.readBigInteger();
		srvHostKey = new com.mxsecurity.publickey.RSAPublicKey(n, e);

		int keyLenDiff = Math.abs(srvServerKey.getModulus().bitLength()
				- srvHostKey.getModulus().bitLength());

		if (keyLenDiff < 24) {
			throw new IOException(
					"Invalid server keys, difference in sizes must be at least 24 bits");
		}

		if (!authenticator.verifyKnownHosts(srvHostKey)) {
			throw new IOException("Verification of known hosts failed");
		}

		protocolFlags = pdu.readInt();
		supportedCiphers = pdu.readInt();
		supportedAuthTypes = pdu.readInt();

		// OUCH: Support SDI patch from
		// ftp://ftp.parc.xerox.com://pub/jean/sshsdi/
		// (we want the types to be in sequence for simplicity, kludge but
		// simple)
		//
		if ((supportedAuthTypes & (1 << 16)) != 0) {
			supportedAuthTypes = ((supportedAuthTypes & 0xffff) | (1 << AUTH_SDI));
		}
	}

	void generateSessionKey() {
		sessionKey = new byte[SESSION_KEY_LENGTH / 8];
		rand.nextBytes(sessionKey);
	}

	void sendSessionKey(int cipherType) throws IOException {
		byte[] key = new byte[sessionKey.length + 1];
		BigInteger encKey;
		PduOutputStreamSSH pdu;

		key[0] = 0;
		System.arraycopy(sessionKey, 0, key, 1, sessionKey.length);

		for (int i = 0; i < sessionId.length; i++)
			key[i + 1] ^= sessionId[i];

		encKey = new BigInteger(key);

		int serverKeyByteLen = (srvServerKey.getModulus().bitLength() + 7) / 8;
		int hostKeyByteLen = (srvHostKey.getModulus().bitLength() + 7) / 8;

		try {
			if (serverKeyByteLen < hostKeyByteLen) {
				BigInteger padded;
				padded = RSAAlgorithm.addPKCS1Pad(encKey, 2, serverKeyByteLen,
						rand);
				encKey = RSAAlgorithm.doPublic(padded,
						srvServerKey.getModulus(),
						srvServerKey.getPublicExponent());
				padded = RSAAlgorithm.addPKCS1Pad(encKey, 2, hostKeyByteLen,
						rand);
				encKey = RSAAlgorithm.doPublic(padded, srvHostKey.getModulus(),
						srvHostKey.getPublicExponent());
			} else {
				BigInteger padded;
				padded = RSAAlgorithm.addPKCS1Pad(encKey, 2, hostKeyByteLen,
						rand);
				encKey = RSAAlgorithm.doPublic(padded, srvHostKey.getModulus(),
						srvHostKey.getPublicExponent());
				padded = RSAAlgorithm.addPKCS1Pad(encKey, 2, serverKeyByteLen,
						rand);
				encKey = RSAAlgorithm.doPublic(padded,
						srvServerKey.getModulus(),
						srvServerKey.getPublicExponent());
			}
		} catch (SignatureException e) {
			throw new IOException(e.getMessage());
		}

		pdu = new PduOutputStreamSSH(CMSG_SESSION_KEY, null, rand);
		pdu.writeByte((byte) cipherType);
		pdu.write(srvCookie, 0, srvCookie.length);
		pdu.writeBigInteger(encKey);
		// !!! TODO: check this pdu.writeInt(PROTOFLAG_SCREEN_NUMBER |
		// PROTOFLAG_HOST_IN_FWD_OPEN);
		pdu.writeInt(protocolFlags);
		pdu.writeTo(sshOut);

		// !!!
		// At this stage the communication is encrypted
		// !!!

		if (!isSuccess())
			throw new IOException("Error while sending session key!");
	}

	void authenticateUser(String userName) throws IOException {
		PduOutputStreamSSH outpdu;

		usedOTP = false;

		outpdu = new PduOutputStreamSSH(CMSG_USER, sndCipher, rand);
		outpdu.writeString(userName);
		outpdu.writeTo(sshOut);

		if (isSuccess()) {
			if (interactor != null)
				interactor
						.report("Authenticated directly by server, no other authentication required");
			return;
		}

		int[] authType = authenticator.getAuthTypes(user);

		for (int i = 0; i < authType.length; i++) {
			try {
				if (!isAuthTypeSupported(authType[i])) {
					throw new AuthFailException("server does not support '"
							+ authTypeDesc[authType[i]] + "'");
				}
				switch (authType[i]) {
				case AUTH_PUBLICKEY:
					doRSAAuth(false, userName);
					break;
				case AUTH_PASSWORD:
					doPasswdAuth(userName);
					break;
				case AUTH_RHOSTS_RSA:
					doRSAAuth(true, userName);
					break;
				case AUTH_TIS:
					doTISAuth(userName);
					break;
				case AUTH_RHOSTS:
					doRhostsAuth(userName);
					break;
				case AUTH_SDI:
					doSDIAuth(userName);
					usedOTP = true;
					break;
				case AUTH_KERBEROS:
				case PASS_KERBEROS_TGT:
				default:
					throw new IOException(
							"We do not support selected authentication type "
									+ authTypeDesc[authType[i]]);
				}
				return;
			} catch (AuthFailException e) {
				if (i == (authType.length - 1)) {
					throw e;
				}
				if (interactor != null) {
					interactor.report("Authenticating with "
							+ authTypeDesc[authType[i]] + " failed, "
							+ e.getMessage());
				}
			}
		}
	}

	void doPasswdAuth(String userName) throws IOException {
		PduOutputStreamSSH outpdu;
		String password;

		password = authenticator.getPassword(user);

		outpdu = new PduOutputStreamSSH(CMSG_AUTH_PASSWORD, sndCipher, rand);
		outpdu.writeString(password);
		outpdu.writeTo(sshOut);

		if (!isSuccess())
			throw new AuthFailException();
	}

	void doRhostsAuth(String userName) throws IOException {
		PduOutputStreamSSH outpdu;

		outpdu = new PduOutputStreamSSH(CMSG_AUTH_RHOSTS, sndCipher, rand);
		outpdu.writeString(userName);
		outpdu.writeTo(sshOut);

		if (!isSuccess())
			throw new AuthFailException();
	}

	void doTISAuth(String userName) throws IOException {
		PduOutputStreamSSH outpdu;
		String prompt;
		String response;

		outpdu = new PduOutputStreamSSH(CMSG_AUTH_TIS, sndCipher, rand);
		outpdu.writeTo(sshOut);
		PduInputStreamSSH inpdu = new PduInputStreamSSH(MSG_ANY, rcvCipher);
		inpdu.readFrom(sshIn);

		if (inpdu.type == SMSG_FAILURE)
			throw new AuthFailException(
					"TIS authentication server not reachable or user unknown");
		else if (inpdu.type != SMSG_AUTH_TIS_CHALLENGE)
			throw new IOException(
					"Protocol error, expected TIS challenge but got "
							+ inpdu.type);

		prompt = inpdu.readString();
		response = authenticator.getChallengeResponse(user, prompt);

		outpdu = new PduOutputStreamSSH(CMSG_AUTH_TIS_RESPONSE, sndCipher, rand);
		outpdu.writeString(response);
		outpdu.writeTo(sshOut);

		if (!isSuccess())
			throw new AuthFailException();
	}

	void doRSAAuth(boolean rhosts, String userName) throws IOException {
		PduOutputStreamSSH outpdu;
		RSAKeyFileSSH keyFile = authenticator.getIdentityFile(user);
		RSAPublicKey pubKey = keyFile.getPublic();

		if (rhosts) {
			outpdu = new PduOutputStreamSSH(CMSG_AUTH_RHOSTS_RSA, sndCipher,
					rand);
			outpdu.writeString(userName);
			outpdu.writeInt(pubKey.getModulus().bitLength());
			outpdu.writeBigInteger(pubKey.getPublicExponent());
			outpdu.writeBigInteger(pubKey.getModulus());
		} else {
			outpdu = new PduOutputStreamSSH(CMSG_AUTH_RSA, sndCipher, rand);
			outpdu.writeBigInteger(pubKey.getModulus());
		}
		outpdu.writeTo(sshOut);

		PduInputStreamSSH inpdu = new PduInputStreamSSH(MSG_ANY, rcvCipher);
		inpdu.readFrom(sshIn);
		if (inpdu.type == SMSG_FAILURE)
			throw new AuthFailException("server refused our key"
					+ (rhosts ? " or rhosts" : ""));
		else if (inpdu.type != SMSG_AUTH_RSA_CHALLENGE)
			throw new IOException(
					"Protocol error, expected RSA-challenge but got "
							+ inpdu.type);

		BigInteger challenge = inpdu.readBigInteger();

		// First try with an empty passphrase...
		//
		RSAPrivateCrtKey privKey = keyFile.getPrivate("");
		if (privKey == null)
			privKey = keyFile.getPrivate(authenticator
					.getIdentityPassword(user));
		else if (interactor != null)
			interactor.report("Authenticated with password-less rsa-key '"
					+ keyFile.getComment() + "'");

		if (privKey == null)
			throw new AuthFailException("invalid password for key-file '"
					+ keyFile.getComment() + "'");

		rsaChallengeResponse(privKey, challenge);
	}

	private final static int CANNOT_CHOOSE_PIN = 0;
	private final static int USER_SELECTABLE = 1;
	private final static int MUST_CHOOSE_PIN = 2;

	void doSDIAuth(String userName) throws IOException {
		PduOutputStreamSSH outpdu;
		String password;

		password = authenticator.getChallengeResponse(user, userName
				+ "'s SDI token passcode: ");

		outpdu = new PduOutputStreamSSH(CMSG_AUTH_SDI, sndCipher, rand);
		outpdu.writeString(password);
		outpdu.writeTo(sshOut);

		PduInputStreamSSH inpdu = new PduInputStreamSSH(MSG_ANY, rcvCipher);
		inpdu.readFrom(sshIn);
		switch (inpdu.type) {
		case SMSG_SUCCESS:
			interactor.report("SDI authentication accepted.");
			break;

		case SMSG_FAILURE:
			throw new AuthFailException("SDI authentication failed.");

		case CMSG_ACM_NEXT_CODE_REQUIRED:
			password = interactor.promptPassword("Next token required: ");
			outpdu = new PduOutputStreamSSH(CMSG_ACM_NEXT_CODE, sndCipher, rand);
			outpdu.writeString(password);
			outpdu.writeTo(sshOut);
			if (!isSuccess())
				throw new AuthFailException();
			break;

		case CMSG_ACM_NEW_PIN_REQUIRED:
			if (!interactor.askConfirmation(
					"New PIN required, do you want to continue?", false))
				throw new AuthFailException("new PIN not wanted");

			String type = inpdu.readString();
			String size = inpdu.readString();
			int userSelect = inpdu.readInt();

			switch (userSelect) {
			case CANNOT_CHOOSE_PIN:
				break;

			case USER_SELECTABLE:
			case MUST_CHOOSE_PIN:
				String pwdChk;
				do {
					password = interactor.promptPassword("Please enter new PIN"
							+ " containing " + size + " " + type);
					pwdChk = interactor
							.promptPassword("Please enter new PIN again");
				} while (!password.equals(pwdChk));

				outpdu = new PduOutputStreamSSH(CMSG_ACM_NEW_PIN, sndCipher,
						rand);
				outpdu.writeString(password);
				outpdu.writeTo(sshOut);

				inpdu = new PduInputStreamSSH(MSG_ANY, rcvCipher);
				inpdu.readFrom(sshIn);
				if (inpdu.type != CMSG_ACM_NEW_PIN_ACCEPTED) {
					throw new AuthFailException("PIN rejected by server");
				}
				throw new AuthFailException("new PIN accepted, "
						+ "wait for the code on your token to change");

			default:
				throw new AuthFailException("invalid response from server");
			}

			break;

		case CMSG_ACM_ACCESS_DENIED:
			// Fall through
		default:
			throw new AuthFailException();
		}
	}

	void rsaChallengeResponse(RSAPrivateCrtKey privKey, BigInteger challenge)
			throws IOException {
		MessageDigest md5;

		try {
			challenge = RSAAlgorithm.doPrivateCrt(challenge,
					privKey.getPrimeP(), privKey.getPrimeQ(),
					privKey.getPrimeExponentP(), privKey.getPrimeExponentQ(),
					privKey.getCrtCoefficient());
			challenge = RSAAlgorithm.stripPKCS1Pad(challenge, 2);
		} catch (Exception e) {
			throw new IOException(e.getMessage());
		}

		byte[] response = challenge.toByteArray();

		try {
			md5 = MessageDigest.getInstance("MD5");
			if (response[0] == 0)
				md5.update(response, 1, 32);
			else
				md5.update(response, 0, 32);
			md5.update(sessionId);
			response = md5.digest();
		} catch (Exception e) {
			throw new IOException(
					"MD5 not implemented, can't generate session-id");
		}

		PduOutputStreamSSH outpdu = new PduOutputStreamSSH(
				CMSG_AUTH_RSA_RESPONSE, sndCipher, rand);
		outpdu.write(response, 0, response.length);
		outpdu.writeTo(sshOut);

		if (!isSuccess())
			throw new AuthFailException();
	}

	void initiateSession() throws IOException {
		if (user.wantPTY())
			requestPTY();

		int maxPktSz = user.getMaxPacketSz();
		if (maxPktSz > 0)
			requestMaxPacketSz(maxPktSz);

		if (user.wantX11Forward())
			requestX11Forward();

		if (activateTunnels)
			initiateTunnels();

		if (commandLine != null)
			requestCommand(commandLine);
		else
			requestShell();

		// !!!
		// At this stage we can't send more options/forward-requests
		// the server has now entered it's service-loop.
	}

	void initiatePlugins() {
		ProtocolPluginSSH.initiateAll(this);
	}

	void initiateTunnels() throws IOException {
		int i;
		for (i = 0; i < localForwards.size(); i++) {
			LocalForward fwd = (LocalForward) localForwards.elementAt(i);
			requestLocalPortForward(fwd.localHost, fwd.localPort,
					fwd.remoteHost, fwd.remotePort, fwd.plugin);
		}
		for (i = 0; i < remoteForwards.size(); i++) {
			RemoteForward fwd = (RemoteForward) remoteForwards.elementAt(i);
			requestRemotePortForward(fwd.remotePort, fwd.localHost,
					fwd.localPort, fwd.plugin);
		}
	}

	// void requestCompression(int level) throws IOException {
	// if(level == 0) {
	// return;
	// }
	//
	// SSHPduOutputStream outpdu =
	// new SSHPduOutputStream(CMSG_REQUEST_COMPRESSION, sndCipher, sndComp,
	// rand);
	//
	// outpdu.writeInt(level);
	// outpdu.writeTo(sshOut);
	// if(!isSuccess() && interactor != null)
	// interactor.report("Error requesting compression level: " + level);
	//
	// sndComp = SSHCompressor.getInstance("zlib", SSHCompressor.COMPRESS_MODE,
	// level);
	// rcvComp = SSHCompressor.getInstance("zlib",
	// SSHCompressor.UNCOMPRESS_MODE,
	// level);
	// }

	void requestMaxPacketSz(int sz) throws IOException {
		PduOutputStreamSSH outpdu = new PduOutputStreamSSH(
				CMSG_MAX_PACKET_SIZE, sndCipher, rand);
		outpdu.writeInt(sz);
		outpdu.writeTo(sshOut);
		if (!isSuccess() && interactor != null)
			interactor.report("Error requesting max packet size: " + sz);
	}

	void requestX11Forward() throws IOException {
		PduOutputStreamSSH outpdu = new PduOutputStreamSSH(
				CMSG_X11_REQUEST_FORWARDING, sndCipher, rand);

		// !!!
		outpdu.writeString("MIT-MAGIC-COOKIE-1");
		outpdu.writeString("112233445566778899aabbccddeeff00");
		outpdu.writeInt(0);
		// !!!

		outpdu.writeTo(sshOut);

		if (!isSuccess() && interactor != null)
			interactor.report("Error requesting X11 forward");
	}

	void requestPTY() throws IOException {
		PduOutputStreamSSH outpdu = new PduOutputStreamSSH(CMSG_REQUEST_PTY,
				sndCipher, rand);
		ConsoleWindows myTerminal = null;
		if (console != null)
			myTerminal = console.getTerminal();
		if (myTerminal != null) {
			outpdu.writeString(myTerminal.terminalType());
			outpdu.writeInt(myTerminal.rows());
			outpdu.writeInt(myTerminal.cols());
			outpdu.writeInt(myTerminal.vpixels());
			outpdu.writeInt(myTerminal.hpixels());
		} else {
			outpdu.writeString("");
			outpdu.writeInt(0);
			outpdu.writeInt(0);
			outpdu.writeInt(0);
			outpdu.writeInt(0);
		}
		outpdu.writeByte((byte) TTY_OP_END);
		outpdu.writeTo(sshOut);

		if (!isSuccess() && interactor != null)
			interactor.report("Error requesting PTY");
	}

	void requestLocalPortForward(String localHost, int localPort,
			String remoteHost, int remotePort, String plugin)
			throws IOException {
		controller.newListenChannel(localHost, localPort, remoteHost,
				remotePort, plugin);
	}

	void requestRemotePortForward(int remotePort, String localHost,
			int localPort, String plugin) throws IOException {

		try {
			ProtocolPluginSSH.getPlugin(plugin).remoteListener(remotePort,
					localHost, localPort, controller);
		} catch (NoClassDefFoundError e) {
			throw new IOException("Plugins not available");
		}

		PduOutputStreamSSH outpdu = new PduOutputStreamSSH(
				CMSG_PORT_FORWARD_REQUEST, sndCipher, rand);
		outpdu.writeInt(remotePort);
		outpdu.writeString(localHost);
		outpdu.writeInt(localPort);
		outpdu.writeTo(sshOut);

		if (!isSuccess() && interactor != null) {
			interactor.report("Error requesting remote port forward: " + plugin
					+ "/" + remotePort + ":" + localHost + ":" + localPort);

		}
	}

	void requestCommand(String command) throws IOException {
		PduOutputStreamSSH outpdu = new PduOutputStreamSSH(CMSG_EXEC_CMD,
				sndCipher, rand);
		outpdu.writeString(command);
		outpdu.writeTo(sshOut);
	}

	void requestShell() throws IOException {
		PduOutputStreamSSH outpdu = new PduOutputStreamSSH(CMSG_EXEC_SHELL,
				sndCipher, rand);
		outpdu.writeTo(sshOut);
	}

	boolean isSuccess() throws IOException {
		boolean success = false;
		PduInputStreamSSH inpdu = null;
		inpdu = new PduInputStreamSSH(MSG_ANY, rcvCipher);
		inpdu.readFrom(sshIn);
		if (inpdu.type == SMSG_SUCCESS)
			success = true;
		else if (inpdu.type == SMSG_FAILURE)
			success = false;
		else if (inpdu.type == MSG_DISCONNECT)
			throw new IOException("Server disconnected: " + inpdu.readString());
		else
			throw new IOException("Protocol error: got " + inpdu.type
					+ " when expecting success/failure");
		return success;
	}

	void setInteractive() {
		try {
			sshSocket.setTcpNoDelay(true);
		} catch (SocketException e) {
			if (interactor != null)
				interactor.report("Error setting interactive mode: "
						+ e.getMessage());
		}
	}

	void setAliveInterval(int i) {
		if (i == 0) {
			if (keepAliveThread != null && keepAliveThread.isAlive())
				keepAliveThread.stop();
			keepAliveThread = null;
		} else {
			if (keepAliveThread != null) {
				keepAliveThread.setInterval(i);
			} else {
				keepAliveThread = new KeepAliveThread(i);
				keepAliveThread.setDaemon(true);
				keepAliveThread.start();
			}
		}
	}

	public boolean isOpened() {
		return isOpened;
	}

	public boolean isConnected() {
		return isConnected;
	}

	void stdinWriteChar(char c) throws IOException {
		stdinWriteString(String.valueOf(c));
	}

	void stdinWriteString(String str) throws IOException {
		stdinWriteString(str.getBytes(), 0, str.length());
	}

	void stdinWriteString(byte[] str) throws IOException {
		stdinWriteString(str, 0, str.length);
	}

	void stdinWriteString(byte[] str, int off, int len) throws IOException {
		PduOutputStreamSSH stdinPdu;
		if (isOpened && controller != null) {
			stdinPdu = new PduOutputStreamSSH(SSH.CMSG_STDIN_DATA, sndCipher,
					rand);
			stdinPdu.writeInt(len);
			stdinPdu.write(str, off, len);
			controller.transmit(stdinPdu);
		}
	}

	void signalWindowChanged(int rows, int cols, int vpixels, int hpixels) {
		if (isOpened && controller != null) {
			try {
				PduOutputStreamSSH pdu;
				pdu = new PduOutputStreamSSH(SSH.CMSG_WINDOW_SIZE, sndCipher,
						rand);
				pdu.writeInt(rows);
				pdu.writeInt(cols);
				pdu.writeInt(vpixels);
				pdu.writeInt(hpixels);
				controller.transmit(pdu);
			} catch (Exception ex) {
				if (interactor != null)
					interactor.alert("Error when sending sigWinch: "
							+ ex.toString());
			}
		}
	}

	public String getHostName() {
		try {
			return user.getSrvHost();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return " ";
	}

}
