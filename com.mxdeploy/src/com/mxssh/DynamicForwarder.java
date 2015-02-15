package com.mxssh;


import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import jsocks.socks.CProxy;
import jsocks.socks.ProxyMessage;
import jsocks.socks.ProxyServer;
import jsocks.socks.Socks4Message;
import jsocks.socks.Socks5Message;
import jsocks.socks.server.ServerAuthenticatorNone;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelDirectTCPIP;
import com.jcraft.jsch.Session;

public class DynamicForwarder implements Runnable { 
	
	public class DynamicForward extends ProxyServer {
		Session session;
		int port;
		
		public DynamicForward(Session session, int port) {
			super(new ServerAuthenticatorNone());
			this.session = session;
			this.port = port;
		}
		
		public DynamicForward(Socket s, Session session, int port){
			super(new ServerAuthenticatorNone(), s);
			this.session = session;
			this.port = port;
		}

		@Override
		public void start(int port,int backlog,InetAddress localIP) throws IOException{
			CProxy.setDefaultProxy("129.39.94.82",1080);
			
			ss = new ServerSocket(port,backlog,localIP);
			while(true){
				Socket s = ss.accept();
				
				DynamicForward df = new DynamicForward(s,session,port);
				(new Thread(df)).start();
			}				
			
            //noinspection InfiniteLoopStatement

		}

		@Override
		protected void onConnect(ProxyMessage msg) throws IOException {
			ProxyMessage response = null;
			int iSock5Cmd = CProxy.SOCKS_FAILURE;    //defaulting to failure
			int iSock4Msg = Socks4Message.REPLY_NO_CONNECT;
			InetAddress sIp = null; int iPort = 0;
			Channel channel = null;
			
			try {
				channel=session.openChannel("direct-tcpip");
				channel.setInputStream(in);
				channel.setOutputStream(out);
				((ChannelDirectTCPIP)channel).setHost(msg.host);
				((ChannelDirectTCPIP)channel).setPort(msg.port);
				channel.connect();			

				iSock5Cmd = CProxy.SOCKS_SUCCESS; iSock4Msg = Socks4Message.REPLY_OK;
				sIp = msg.ip; iPort = msg.port;

			}
			catch (Exception sE) {
				log("Failed connecting to remote socket. Exception: " + sE.getLocalizedMessage());

				//TBD Pick proper socks error for corresponding socket error, below is too generic
				iSock5Cmd = CProxy.SOCKS_CONNECTION_REFUSED; iSock4Msg = Socks4Message.REPLY_NO_CONNECT;
			}

			if (msg instanceof Socks5Message) {
				response = new Socks5Message(iSock5Cmd, sIp, iPort);
			} else {
				response = new Socks4Message(iSock4Msg, sIp, iPort);
			}

			response.write(out);
			try {
                if (channel != null) {
                    while (channel.isConnected()) {Thread.sleep(100);}
                }
            } catch (InterruptedException ignored) {
			}
        }
		
		void start () throws IOException {
			start (port);
		}
	}

	DynamicForward df = null;
	Thread ServerThread = null;
	Exception e = null;

    public DynamicForwarder (int port, Session session) {
		df = new DynamicForward(session, port );
		//DynamicForward.setLog(System.out);
		ServerThread = new Thread(this, "DynamicForwader");
		ServerThread.start ();
	}

	public void run() {
		try {
			df.start();
		} catch (IOException e) {
			this.e = e;
		}
	}
	
	public void stop() {
		df.stop();
		if (!ServerThread.isInterrupted()) {
			ServerThread.interrupt();
		}
	}
	
	public DynamicForward getDynamicForward(){
		return df;
	}
}
