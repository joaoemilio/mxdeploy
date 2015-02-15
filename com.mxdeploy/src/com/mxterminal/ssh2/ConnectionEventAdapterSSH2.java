package com.mxterminal.ssh2;

import java.net.Socket;

import com.mxterminal.channel.ChannelSSH2;
import com.mxterminal.ssh2.exception.ExceptionSSH2;


/**
 * This class is an adapter for the interface
 * <code>SSH2ConnectionEventHandler</code>.
 *
 * @see ConnectionEventHandlerSSH2
 */
public class ConnectionEventAdapterSSH2 implements ConnectionEventHandlerSSH2 {
    public void channelAdded(ConnectionSSH2 connection, ChannelSSH2 channel) {}
    public void channelDeleted(ConnectionSSH2 connection, ChannelSSH2 channel) {}
    public void channelConnect(Object originator, ChannelSSH2 channel,
                               Socket fwdSocket) {}
    public void channelClosed(ConnectionSSH2 connection, ChannelSSH2 channel) {}

    public boolean listenerAccept(ListenerSSH2 listener, Socket fwdSocket) {
        return true;
    }
    public void localForwardedConnect(ConnectionSSH2 connection,
                                      ListenerSSH2 listener,
                                      ChannelSSH2 channel) {}
    public void localDirectConnect(ConnectionSSH2 connection,
                                   ListenerSSH2 listener,
                                   ChannelSSH2 channel) {}
    public void localSessionConnect(ConnectionSSH2 connection,
    								ChannelSSH2 channel) {}
    public void localX11Connect(ConnectionSSH2 connection,
                                ListenerSSH2 listener,
                                ChannelSSH2 channel) {}
    public void localChannelOpenFailure(ConnectionSSH2 connection,
    									ChannelSSH2 channel,
                                        int reasonCode, String reasonText,
                                        String languageTag) {}

    public void remoteForwardedConnect(ConnectionSSH2 connection,
                                       String remoteAddr, int remotePort,
                                       ChannelSSH2 channel) {}
    public void remoteDirectConnect(ConnectionSSH2 connection,
    									ChannelSSH2 channel) {}
    public void remoteSessionConnect(ConnectionSSH2 connection,
                                     String remoteAddr, int remotePort,
                                     ChannelSSH2 channel) {}
    public void remoteX11Connect(ConnectionSSH2 connection,
    							 ChannelSSH2 channel) {}
    public void remoteChannelOpenFailure(ConnectionSSH2 connection,
                                         String channelType,
                                         String targetAddr, int targetPort,
                                         String originAddr, int originPort,
                                         ExceptionSSH2 cause) {}

    public void setSocketOptions(int channelType, Socket s) {
        // Do nothing by default, derive this class to change
    }

}
