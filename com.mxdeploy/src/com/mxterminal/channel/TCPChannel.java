package com.mxterminal.channel;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import com.mxterminal.ssh2.ConnectionSSH2;
import com.mxterminal.ssh2.ListenerSSH2;


/**
 * This is a subclass of <code>SSH2StreamChannel</code> which
 * implements channels which are connected to TCP streams at both
 * ends.
 */
public class TCPChannel extends StreamChannel {

    protected Socket endpoint;
    protected String originAddr;
    protected int    originPort;
    protected String remoteAddr;
    protected int    remotePort;

    public TCPChannel(int channelType, ConnectionSSH2 connection,
                          Object creator,
                          Socket endpoint,
                          String remoteAddr, int remotePort,
                          String originAddr, int originPort)
    throws IOException {
        super(channelType, connection, creator,
              endpoint.getInputStream(), endpoint.getOutputStream());
        this.endpoint   = endpoint;
        this.remoteAddr = remoteAddr;
        this.remotePort = remotePort;
        this.originAddr = originAddr;
        this.originPort = originPort;
    }

    protected void outputClosed() {
        if(endpoint != null) {
            try {
                endpoint.close();
            } catch (IOException e) { /* don't care */
            }
        }
        endpoint = null;
    }

    protected boolean openFailureImpl(int reasonCode, String reasonText,
                                      String langTag) {
        outputClosed();
        return false;
    }

    /**
     * Gets the address of the enpoint. That is the address to which
     * the local TCP socket is connected.
     */
    public InetAddress getAddress() {
        return endpoint.getInetAddress();
    }

    /**
     * Gets the port of the enpoint. That is the port to which
     * the local TCP socket is connected.
     */
    public int getPort() {
        return endpoint.getPort();
    }

    /**
     * Get the address the server is supposed to be connected to.
     */
    public String getRemoteAddress() {
        return remoteAddr;
    }

    /**
     * Get the port the server is supposed to be connected to.
     */
    public int getRemotePort() {
        return remotePort;
    }

    /**
     * Gets the origin address which was given in the
     * constructor. This should return the same address as returned by
     * <code>getAddress</code>.
     */
    public String getOriginAddress() {
        return originAddr;
    }

    /**
     * Gets the origin port which was given in the
     * constructor. This should return the same port as returned by
     * <code>getPort</code>.
     */
    public int getOriginPort() {
        return originPort;
    }

    /**
     * Create a string representation of this object.
     *
     * @return A string describing this instance.
     */
    public String toString() {
        String desc = "<N/A>";
        switch(channelType) {
        case ConnectionSSH2.CH_TYPE_FWD_TCPIP:
            desc = "[remote] " + originAddr + ":" + originPort + " <--> " +
                   getRemoteAddress() + ":" + getRemotePort() + " <--ssh2--> " +
                   getAddress().getHostAddress() + ":" + getPort();
            break;
        case ConnectionSSH2.CH_TYPE_DIR_TCPIP:
            ListenerSSH2 l = (ListenerSSH2)creator;
            desc = "[local] " + originAddr + ":" + originPort + " <--> " +
                   l.getListenHost() + ":" + l.getListenPort() + " <--ssh2--> " +
                   getRemoteAddress() + ":" + getRemotePort();
            break;
        default:
            System.out.println("!!! NOT SUPPORTED IN SSH2TCPChannel.toString !!!");
            break;
        }
        return desc;
    }

}
