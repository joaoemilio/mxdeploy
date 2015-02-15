package com.mxterminal.ssh;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import com.mxterminal.channel.ChannelController;
import com.mxterminal.channel.ChannelSSH;

public class ListenChannelSSH extends ChannelSSH {
    static boolean allowRemoteConnect = false;

    static final int LISTEN_QUEUE_SIZE = 16;

    ChannelController controller;

    ServerSocket listenSocket;
    String       remoteHost;
    int          remotePort;
    InetAddress  localHost1;
    InetAddress  localHost2;

    boolean temporaryListener;

    public ListenChannelSSH(String localHost, int localPort,
                            String remoteHost, int remotePort,
                            ChannelController controller)
    throws IOException {
        super(SSH.LISTEN_CHAN_NUM);
        this.controller         = controller;
        try {
            this.listenSocket = new ServerSocket(localPort, LISTEN_QUEUE_SIZE,
                                                 InetAddress.getByName(localHost));
        } catch (IOException e) {
            throw new IOException("Error setting up local forward on port " +
                                  localPort + ", " + e.getMessage());
        }
        this.remoteHost         = remoteHost;
        this.remotePort         = remotePort;

        this.localHost1 = InetAddress.getLocalHost();
        this.localHost2 = InetAddress.getByName("127.0.0.1");
    }

    public int getListenPort() {
        return listenSocket.getLocalPort();
    }

    public String getListenHost() {
        return listenSocket.getInetAddress().getHostAddress();
    }

    public static synchronized void setAllowRemoteConnect(boolean val) {
        allowRemoteConnect = val;
    }

    static synchronized boolean getAllowRemoteConnect() {
        return allowRemoteConnect;
    }

    public TunnelSSH newTunnel(Socket ioSocket, int channelId, int remoteChannelId,
                               ChannelController controller) throws IOException {
        return new TunnelSSH(ioSocket, channelId, remoteChannelId, controller);
    }

    public void setTemporaryListener(boolean val) {
        temporaryListener = val;
    }

    public void serviceLoop() throws IOException {

        SSH.log("Starting listen-chan: " + listenSocket.getLocalPort());

        try {
            for(;;) {
                Socket  fwdSocket = listenSocket.accept();

                if(!getAllowRemoteConnect() &&
                        !(fwdSocket.getInetAddress().equals(localHost1) ||
                          fwdSocket.getInetAddress().equals(localHost2))) {
                    controller.alert("Remote connect to local tunnel rejected: " + fwdSocket.getInetAddress());
                    fwdSocket.close();
                    continue;
                }

                PduOutputStreamSSH respPdu =
                    new PduOutputStreamSSH(SSH.MSG_PORT_OPEN,
                                           controller.sndCipher,
                                             controller.secureRandom());

                int newChan      = controller.newChannelId();
                TunnelSSH tunnel = newTunnel(fwdSocket,
                                             newChan, SSH.UNKNOWN_CHAN_NUM,
                                             controller);
                controller.addTunnel(tunnel);
                tunnel.setRemoteDesc(remoteHost + ":" + remotePort);

                respPdu.writeInt(newChan);
                respPdu.writeString(remoteHost);
                respPdu.writeInt(remotePort);

                SSH.log("got connect for: " + remoteHost + " : " + remotePort + ", " + newChan);

                //
                // !!! TODO: check this !!! if(controller.haveHostInFwdOpen())
                //
                respPdu.writeString(fwdSocket.getInetAddress().getHostAddress());

                controller.transmit(respPdu);

                if(temporaryListener)
                    break;
            }
        } finally {
            listenSocket.close();
        }
    }

    public void forceClose() {
        if(isAlive()) {
            stop();
        }
        try {
            listenSocket.close();
        } catch (IOException e) {}
    }

}
