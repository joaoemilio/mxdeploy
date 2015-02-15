package com.mxterminal.ssh2;

import java.net.Socket;

import com.mxterminal.channel.ChannelSSH2;
import com.mxterminal.ssh2.exception.ExceptionSSH2;


public interface ConnectionEventHandlerSSH2 {
    // !!! TODO add globalRequest<type> calls... ???
    // public void globalRequest(SSH2Connection conn, String type);

    /**
     * Called when a new channel is added. That is when a new channel
     * has been opened through a port forward.
     *
     * @param connection the connection layer responsible
     * @param channel    the channel which was added
     *
     */
    public void channelAdded(ConnectionSSH2 connection, ChannelSSH2 channel);

    /**
     * Called when a channel is deleted. That is when a channel has
     * been finally removed.
     *
     * @param connection the connection layer responsible
     * @param channel    the channel which was deleted
     */
    public void channelDeleted(ConnectionSSH2 connection, ChannelSSH2 channel);

    /**
     * Called when a channel is connected to a <code>Socket</code>.
     *
     * @param originator the responsible listener/connector
     * @param channel    the created channel
     * @param fwdSocket  the socket which is connected to
     */
    public void channelConnect(Object originator, ChannelSSH2 channel,
                               Socket fwdSocket);

    /**
     * Called when a channel is closed. That is when the channel has
     * been closed and will be flushed and then removed.
     *
     * @param connection the connection layer responsible
     * @param channel the channel which was deleted
     */
    public void channelClosed(ConnectionSSH2 connection, ChannelSSH2 channel);

    /**
     * Called when a listener accepts a new connection. That is when a
     * local forward is opened. This callback indicates if a connection
     * should be handled or not by returning <code>true</code> or
     * <code>false</code>.
     *
     * @param  listener  the responsible listener
     * @param  fwdSocket the socket which resulted
     * @return <code>boolean</code> indicating wether to process connection or
     *         not.
     */
    public boolean listenerAccept(ListenerSSH2 listener, Socket fwdSocket);

    /**
     * Called on the server side when a remote forward channel is confirmed to
     * be open.
     *
     * @param connection the connection layer responsible
     * @param listener   the responsible listener
     * @param channel the channel which was opened
     */
    public void localForwardedConnect(ConnectionSSH2 connection,
                                      ListenerSSH2 listener,
                                      ChannelSSH2 channel);
    /**
     * Called on the client side when a local forward channel is confirmed to
     * be open.
     *
     * @param connection the connection layer responsible
     * @param listener   the responsible listener
     * @param channel the channel which was opened
     */
    public void localDirectConnect(ConnectionSSH2 connection,
                                   ListenerSSH2 listener,
                                   ChannelSSH2 channel);

    /**
     * Called on the client side when a session channel is confirmed to be open.
     *
     * @param connection the connection layer responsible
     * @param channel the channel which was opened
     */
    public void localSessionConnect(ConnectionSSH2 connection,
    		ChannelSSH2 channel);

    /**
     * Called on the server side when an X11 channel is confirmed to be open.
     *
     * @param connection the connection layer responsible
     * @param listener   the responsible listener
     * @param channel the channel which was opened
     */
    public void localX11Connect(ConnectionSSH2 connection,
                                ListenerSSH2 listener,
                                ChannelSSH2 channel);

    /**
     * Called on either side when a locally originating channel gets a channel
     * open failure indication from peer. See the class <code>SSH2</code> for
     * reason codes.
     *
     * @param connection  the connection layer responsible
     * @param channel     the channel which was opened
     * @param reasonCode  the reason code 
     * @param reasonText
     * @param languageTag
     *
     * @see SSH2
     */
    public void localChannelOpenFailure(ConnectionSSH2 connection,
    		                            ChannelSSH2 channel,
                                        int reasonCode, String reasonText,
                                        String languageTag);

    /**
     * Called on the client side when a remote forward channel has been
     * confirmed to be open.
     *
     * @param connection the connection layer responsible
     * @param remoteAddr
     * @param remotePort
     * @param channel    the channel which was opened
     */
    public void remoteForwardedConnect(ConnectionSSH2 connection,
                                       String remoteAddr, int remotePort,
                                       ChannelSSH2 channel);

    /**
     * Called on the client side when a remote direct channel has been
     * confirmed to be open.
     *
     * @param connection the connection layer responsible
     * @param channel    the channel which was opened
     */
    public void remoteDirectConnect(ConnectionSSH2 connection,
    		                        ChannelSSH2 channel);

    /**
     * Called on the client side when a remote session channel has been
     * confirmed to be open.
     *
     * @param connection the connection layer responsible
     * @param remoteAddr
     * @param remotePort
     * @param channel    the channel which was opened
     */
    public void remoteSessionConnect(ConnectionSSH2 connection,
                                     String remoteAddr, int remotePort,
                                     ChannelSSH2 channel);

    /**
     * Called on the client side when a remote X11 channel has been
     * confirmed to be open.
     *
     * @param connection the connection layer responsible
     * @param channel    the channel which was opened
     */
    public void remoteX11Connect(ConnectionSSH2 connection,
    		                     ChannelSSH2 channel);

    /**
     * Called on either side when there is a problem opening a remotely
     * originating channel resulting in a channel open failure indication beeing
     * sent back to peer. The exception which was the cause of the problem is
     * provided aswell as the type of channel and relevant addresses and ports.
     *
     * @param connection  the connection layer responsible
     * @param channelType the type of channel
     * @param targetAddr  the address which should have been connected to
     * @param targetPort  the port which should have been connected to
     * @param originAddr  the address where the channel originated (depends on type)
     * @param originPort  the port where the channel originated (depends on type)
     * @param cause       the exception which was the cause of the problem
     */
    public void remoteChannelOpenFailure(ConnectionSSH2 connection,
                                         String channelType,
                                         String targetAddr, int targetPort,
                                         String originAddr, int originPort,
                                         ExceptionSSH2 cause);

    /**
     * Called to set socket options on newly connected port forward channels
     *
     * @param channelType the type of the channel
     * @param s           socket to manipulate
     */
    public void setSocketOptions(int channelType, Socket s);

}
