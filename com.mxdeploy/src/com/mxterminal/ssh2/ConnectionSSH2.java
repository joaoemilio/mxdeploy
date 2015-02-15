package com.mxterminal.ssh2;

import java.io.IOException;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.mxsecurity.jca.SecureRandom;
import com.mxsecurity.util.SecureRandomAndPad;
import com.mxterminal.channel.ChannelSSH2;
import com.mxterminal.channel.InternalChannel;
import com.mxterminal.channel.SessionChannel;
import com.mxterminal.console.charset.ConsoleCharsetFilter;
import com.mxterminal.ssh2.auth.UserAuth;


public final class ConnectionSSH2 implements Runnable {
	static Logger logger = Logger.getLogger(ConnectionSSH2.class);
	
    public final static int MAX_ACTIVE_CHANNELS = 1024;

    public final static String GL_REQ_START_FORWARD  = "tcpip-forward";
    public final static String GL_REQ_CANCEL_FORWARD = "cancel-tcpip-forward";

    public final static String CH_REQ_PTY         = "pty-req";
    public final static String CH_REQ_X11         = "x11-req";
    public final static String CH_REQ_ENV         = "env";
    public final static String CH_REQ_SHELL       = "shell";
    public final static String CH_REQ_EXEC        = "exec";
    public final static String CH_REQ_SUBSYSTEM   = "subsystem";
    public final static String CH_REQ_WINCH       = "window-change";
    public final static String CH_REQ_XONOFF      = "xon-xoff";
    public final static String CH_REQ_SIGNAL      = "signal";
    public final static String CH_REQ_EXIT_STAT   = "exit-status";
    public final static String CH_REQ_EXIT_SIG    = "exit-signal";
    public final static String CH_REQ_AUTH_AGENT  = "auth-agent-req";
    public final static String CH_REQ_AUTH_AGENT1 = "auth-ssh1-agent-req";

    // draft-ietf-secsh-break
    public final static String CH_REQ_BREAK       = "break";

    // old OpenSSH keepalive request
    public final static String CH_REQ_OPENSSH_KEEPALIVE = "keepalive@openssh.com";

    public final static String CHAN_FORWARDED_TCPIP = "forwarded-tcpip";
    public final static String CHAN_DIRECT_TCPIP    = "direct-tcpip";
    public final static String CHAN_SESSION         = "session";
    public final static String CHAN_X11             = "x11";
    public final static String CHAN_AUTH_AGENT      = "auth-agent";

    public final static int CH_TYPE_FWD_TCPIP  = 0;
    public final static int CH_TYPE_DIR_TCPIP  = 1;
    public final static int CH_TYPE_SESSION    = 2;
    public final static int CH_TYPE_X11        = 3;
    public final static int CH_TYPE_AUTH_AGENT = 4;

    public final static String[] channelTypes = {
                                             CHAN_FORWARDED_TCPIP,
                                             CHAN_DIRECT_TCPIP,
                                             CHAN_SESSION,
                                             CHAN_X11,
                                             CHAN_AUTH_AGENT
                                         };

    private TransportSSH2 transport;
    private UserAuth  userAuth;

    private ConnectionEventHandlerSSH2 eventHandler;

    private ChannelSSH2[] channels;
    private int           totalChannels;
    private int           nextEmptyChan;

    private ConnectorSSH2 connector;

    private Hashtable remoteForwards;
    private Hashtable remoteFilters;
    private Hashtable localForwards;

    private byte[]    x11RealCookie;
    private byte[]    x11FakeCookie;
    private boolean   x11Single;
    private int       x11Mappings;

    private Object    reqMonitor;
    private boolean   reqStatus;

    //private Log connLog;
    
    private ConsoleCharsetFilter  filter;
    
    

    /**
     * Basic constructor used when there is no need for event handler.
     *
     * @param userAuth  the authentication layer
     * @param transport the transport layer
     */
    public ConnectionSSH2(UserAuth userAuth, TransportSSH2 transport) {
        this(userAuth, transport, null);
    }

    /**
     * Constructor used when there need for event handler.
     *
     * @param userAuth     the authentication layer
     * @param transport    the transport layer
     * @param eventHandler the event handler (may be <code>null</code>)
     *
     * @see ConnectionEventHandlerSSH2
     */
    public ConnectionSSH2(UserAuth userAuth, TransportSSH2 transport,
                          ConnectionEventHandlerSSH2 eventHandler) {
        this.userAuth       = userAuth;
        this.transport      = transport;
        this.eventHandler   = (eventHandler != null ? eventHandler :
                               new ConnectionEventAdapterSSH2());
        this.channels       = new ChannelSSH2[64];
        this.totalChannels  = 0;
        this.nextEmptyChan  = 0;
        this.remoteForwards = new Hashtable();
        this.remoteFilters  = new Hashtable();
        this.localForwards  = new Hashtable();

        this.x11RealCookie  = null;
        this.x11FakeCookie  = null;
        this.x11Single      = false;
        this.x11Mappings    = 0;

        //this.connLog        = transport.getLog();

        this.reqMonitor     = new Object();

        if(transport.incompatibleBuggyChannelClose) {
            startChannelReaper();
        }
    }

    void processGlobalMessage(TransportPDUSSH2 pdu) {
        switch(pdu.pktType) {
        case SSH2.MSG_GLOBAL_REQUEST:
            String  type      = pdu.readJavaString();
            boolean wantReply = pdu.readBoolean();
            if(type.equals(GL_REQ_START_FORWARD)) {
            }
            else if(type.equals(GL_REQ_CANCEL_FORWARD)) {
            }
            else {
            }
            break;

        case SSH2.MSG_REQUEST_SUCCESS:
            synchronized(reqMonitor) {
                reqStatus = true;
                reqMonitor.notify();
            }
            break;

        case SSH2.MSG_REQUEST_FAILURE:
            synchronized(reqMonitor) {
                reqStatus = false;
                reqMonitor.notify();
            }
            break;

        case SSH2.MSG_CHANNEL_OPEN:
            getConnector().channelOpen(pdu);
            pdu = null;
            break;
        }

        if(pdu != null) {
            pdu.release();
        }
    }

    void processChannelMessage(TransportPDUSSH2 pdu) {
        int channelId       = pdu.readInt();
        ChannelSSH2 channel = channels[channelId];

        if(channel == null) {
            String msg = "Error, received message to non-existent channel";
            logger.error( msg);
//            logger.debug(
//                           "got message of type: " +
//                           SSH2.msgTypeString(pdu.pktType),
//                           pdu.getData(),
//                           pdu.getPayloadOffset(),
//                           pdu.getPayloadLength());
            if (!transport.incompatibleMayReceiveDataAfterClose)
                fatalDisconnect(SSH2.DISCONNECT_PROTOCOL_ERROR, msg);
            return;
        }

        switch(pdu.pktType) {
        case SSH2.MSG_CHANNEL_OPEN_CONFIRMATION:
            channel.openConfirmation(pdu);
            break;

        case SSH2.MSG_CHANNEL_OPEN_FAILURE:
            int    reasonCode = pdu.readInt();
            String reasonText;
            String langTag;
            if(transport.incompatibleChannelOpenFail) {
                reasonText = "";
                langTag    = "";
            } else {
                reasonText = pdu.readJavaString();
                langTag    = pdu.readJavaString();
            }
            channel.openFailure(reasonCode, reasonText, langTag);
            break;

        case SSH2.MSG_CHANNEL_WINDOW_ADJUST:
            channel.windowAdjust(pdu);
            break;

        case SSH2.MSG_CHANNEL_DATA:
            channel.data(pdu);
            pdu = null;
            break;

        case SSH2.MSG_CHANNEL_EXTENDED_DATA:
            channel.extData(pdu);
            pdu = null;
            break;

        case SSH2.MSG_CHANNEL_EOF:
            channel.recvEOF();
            break;

        case SSH2.MSG_CHANNEL_CLOSE:
            channel.recvClose();
            break;

        case SSH2.MSG_CHANNEL_REQUEST:
            channel.handleRequest(pdu);
            break;

        case SSH2.MSG_CHANNEL_SUCCESS:
            channel.requestSuccess(pdu);
            break;

        case SSH2.MSG_CHANNEL_FAILURE:
            channel.requestFailure(pdu);
            break;
        }

        if(pdu != null) {
            pdu.release();
        }
    }

    /**
     * Gets our transport layer.
     *
     * @return the transport layer
     */
    public TransportSSH2 getTransport() {
        return transport;
    }

    /**
     * Sets the event handler to use.
     *
     * @param eventHandler the event handler to use
     */
    public void setEventHandler(ConnectionEventHandlerSSH2 eventHandler) {
        if(eventHandler != null) {
            this.eventHandler = eventHandler;
        }
    }

    /**
     * Gets the event handler currently in use.
     *
     * @return the event handler currently in use
     */
    public ConnectionEventHandlerSSH2 getEventHandler() {
        return eventHandler;
    }

    /**
     * Gets the preferences set in the transport layer.
     *
     * @return the preferences
     */
    public PreferencesSSH2 getPreferences() {
        return transport.getOurPreferences();
    }

    /**
     * Gets the <code>SecureRandom</code> currently in use. That is from the
     * transport layer.
     *
     * @return the <code>SecureRandom</code> in use
     */
    public SecureRandom getSecureRandom() {
        return transport.getSecureRandom();
    }

    /**
     * Transmits the given PDU (by sending it to the transport layer, no
     * processing is needed at this point).
     *
     * @param pdu packet to send
     */
    public void transmit(TransportPDUSSH2 pdu) {
        transport.transmit(pdu);
    }

    /**
     * Disconnects from peer using the DISCONNECT packet type with the given
     * reason and description. See the class <code>SSH2</code> for reason codes.
     * This is only a convenience method which calls the same method on the
     * transport layer.
     *
     * @param reason      the reason code
     * @param description the textual description for the cause of disconnect
     *
     * @see SSH2
     */
    public void fatalDisconnect(int reason, String description) {
        transport.fatalDisconnect(reason, description);
    }

    /**
     * Gets the singleton instance of the <code>SSH2Connector</code> which is
     * used by the connection layer to connect remote forwards through to local
     * hosts when they are opened.
     *
     * @return the singleton connector
     */
    public ConnectorSSH2 getConnector() {
        if(connector == null) {
            connector = new ConnectorSSH2(this);
        }
        return connector;
    }

    /**
     * Gets the local target host address and port pair of a remote forward
     * identified by the given remote address and port pair. This function is
     * used to locate the local target of a remote forward when it is opened.
     *
     * @param remoteAddr the remote address of the forward
     * @param remotePort the remote port of the forward
     *
     * @return the address and port, the address beeing at index 0 and the port
     * beeing at inde 1.
     */
    public synchronized String[]
    getForwardTarget(String remoteAddr,
                     int remotePort) {
        String[] target = null;
        String tgStr = (String)remoteForwards.get(remoteAddr + ":" +
                       remotePort);
        if(tgStr != null) {
            target = new String[2];
            int i = tgStr.indexOf(":");
            target[0] = tgStr.substring(0, i);
            target[1] = tgStr.substring(i + 1);
        }

        return target;
    }

    /**
     * Gets the filter factory instance for a remote forward identified by the
     * given remote address and port pair.
     *
     * @param remoteAddr the remote address of the forward
     * @param remotePort the remote port of the forward
     *
     * @return the stream filter factory instance
     */
    public synchronized StreamFilterFactory
    getForwardFilterFactory(String remoteAddr, int remotePort) {
        return (StreamFilterFactory)remoteFilters.get(remoteAddr + ":" +
                remotePort);
    }

    /**
     * Gets the <code>SSH2Listener</code> instance of a local forward if it is
     * set up.
     *
     * @param localAddr the local address of the forward
     * @param localPort the local port of the forward
     *
     * @return the listener instance for the given forward or <code>null</code>
     * if none is set
     */
    public synchronized ListenerSSH2 getLocalListener(String localAddr,
            int localPort) {
        return (ListenerSSH2)localForwards.get(localAddr + ":" + localPort);
    }

    /**
     * Creates a new remote forward from the given remote address and port on
     * the server to the local address and port.
     *
     * @param remoteAddr the remote address where the server listens
     * @param remotePort the remote port where the server listens
     * @param localAddr  the local address to connect through to
     * @param localPort the local port to connect through to
     */
    public synchronized void newRemoteForward(String remoteAddr,
            int remotePort,
            String localAddr,
            int localPort) {
        newRemoteForward(remoteAddr, remotePort, localAddr, localPort,
                         (StreamFilterFactory)null);
    }

    /**
     * Creates a new remote forward from the given remote address and port on
     * the server to the local address and port using the given filter factory
     * to insert filters in the input/output streams of the forwarded channels.
     *
     * @param remoteAddr    the remote address where the server listens
     * @param remotePort    the remote port where the server listens
     * @param localAddr     the local address to connect through to
     * @param localPort     the local port to connect through to
     * @param filterFactory the filter factory instance to use for producing
     * filters.
     */
    public synchronized void
    newRemoteForward(String remoteAddr, int remotePort,
                     String localAddr, int localPort,
                     StreamFilterFactory filterFactory) {
        newRemoteForward(remoteAddr, remotePort, localAddr, localPort,
                         filterFactory, false);
    }

    /**
     * Creates a new remote forward from the given remote address and port on
     * the server to the local address and port using the given filter factory
     * to insert filters in the input/output streams of the forwarded
     * channel. This is a blocking version of the method
     * <code>newRemoteForward</code> with the same parameters which waits until
     * a result is reported from the server which indicates whether the forward
     * could be set up or not.
     *
     * @param remoteAddr    the remote address where the server listens
     * @param remotePort    the remote port where the server listens
     * @param localAddr     the local address to connect through to
     * @param localPort     the local port to connect through to
     * @param filterFactory the filter factory instance to use for producing
     * filters.
     */
    public boolean newRemoteForwardBlocking(String remoteAddr, int remotePort,
                                            String localAddr, int localPort,
                                            StreamFilterFactory
                                            filterFactory) {
        synchronized(reqMonitor) {
            newRemoteForward(remoteAddr, remotePort, localAddr, localPort,
                             filterFactory, true);
            try {
                reqMonitor.wait();
            } catch (InterruptedException e) {
                /* don't care, someone interrupted us on purpose */
            }
            return reqStatus;
        }
    }

    private synchronized void
    newRemoteForward(String remoteAddr, int remotePort,
                     String localAddr, int localPort,
                     StreamFilterFactory filterFactory,
                     boolean wantReply) {
    	logger.debug(
                      remoteAddr + ":" + remotePort + "->" +
                      localAddr + ":" + localPort);

        remoteForwards.put(remoteAddr + ":" + remotePort,
                           localAddr  + ":" + localPort);
        if(filterFactory != null) {
            remoteFilters.put(remoteAddr + ":" + remotePort,
                              filterFactory);
        }
        TransportPDUSSH2 pdu =
            TransportPDUSSH2.createOutgoingPacket(SSH2.MSG_GLOBAL_REQUEST);
        pdu.writeString(GL_REQ_START_FORWARD);
        pdu.writeBoolean(wantReply);
        pdu.writeString(remoteAddr);
        pdu.writeInt(remotePort);
        transport.transmit(pdu);
    }

    /**
     * Deletes the remote forward identified by the given remote address and
     * port pair. Note that the channels that was previously opened through this
     * forward are not deleted, only a CANCEL_FORWARD request is sent to the
     * server which deletes the forward on the server preventing further
     * channels to be opened through this forward.
     *
     * @param remoteAddr the remote address of the forward
     * @param remotePort the remote port of the forward
     */
    public synchronized void deleteRemoteForward(String remoteAddr,
            int remotePort) {
    	logger.debug(
                      remoteAddr + ":" + remotePort);

        String tgStr = (String)remoteForwards.get(remoteAddr + ":" +
                       remotePort);
        if(tgStr != null) {
            TransportPDUSSH2 pdu =
                TransportPDUSSH2.createOutgoingPacket(SSH2.MSG_GLOBAL_REQUEST);
            pdu.writeString(GL_REQ_CANCEL_FORWARD);
            pdu.writeBoolean(true);
            pdu.writeString(remoteAddr);
            pdu.writeInt(remotePort);
            transport.transmit(pdu);
            remoteForwards.remove(remoteAddr + ":" + remotePort);
        }
    }

    /**
     * Creates a new local forward from the given local address and port to
     * the remote address and port on the server side.
     *
     * @param localAddr     the local address to listen to
     * @param localPort     the local port to listen to
     * @param remoteAddr    the remote address where the connects to
     * @param remotePort    the remote port where the connects to
     *
     * @return a listener instance accepting connections to forward
     */
    public synchronized ListenerSSH2 newLocalForward(String localAddr,
            int localPort,
            String remoteAddr,
            int remotePort)
    throws IOException {
        return newLocalForward(localAddr, localPort, remoteAddr, remotePort,
                               (StreamFilterFactory)null);
    }

    /**
     * Creates a new local forward from the given local address and port to the
     * remote address and port on the server side using the given filter factory
     * to insert filters in the input/output streams of the forwarded channels.
     *
     * @param localAddr     the local address to listen to
     * @param localPort     the local port to listen to
     * @param remoteAddr    the remote address where the connects to
     * @param remotePort    the remote port where the connects to
     * @param filterFactory the filter factory instance to use for producing
     * filters.
     *
     * @return a listener instance accepting connections to forward
     */
    public synchronized ListenerSSH2
    newLocalForward(String localAddr, int localPort,
                    String remoteAddr, int remotePort,
                    StreamFilterFactory filterFactory)
    throws IOException {
    	logger.debug(
                      localAddr + ":" + localPort +  "->" +
                      remoteAddr + ":" + remotePort);

        ListenerSSH2 listener = new ListenerSSH2(localAddr, localPort,
                                remoteAddr, remotePort,
                                this, filterFactory);
        localForwards.put(localAddr + ":" + localPort, listener);

        return listener;
    }

    /**
     * Creates a new internal forward to
     * the remote address and port on the server side.
     *
     * @param remoteAddr    the remote address where the connects to
     * @param remotePort    the remote port where the connects to
     *
     * @return an internal channel
     */
    public synchronized InternalChannel  newLocalInternalForward(String remoteAddr, int remotePort) {
        return newLocalInternalForward(remoteAddr, remotePort, null);
    }

    /**
     * Creates a new internal forward to
     * remote address and port on the server side using the given filter
     * factory to insert filters in the input/output streams of the forwarded
     * channels.
     *
     * @param remoteAddr    the remote address where the connects to
     * @param remotePort    the remote port where the connects to
     * @param filterFactory the filter factory instance to use for producing
     * filters.
     *
     * @return an internal channel
     */
    public synchronized InternalChannel newLocalInternalForward(String remoteAddr, int remotePort,
                            StreamFilterFactory filterFactory) {
    	logger.debug( "->" + remoteAddr + ":" + remotePort);
        InternalChannel chan =  new InternalChannel(CH_TYPE_DIR_TCPIP, this);
        if(filterFactory != null) {
            chan.applyFilter(filterFactory.createFilter(this, chan));
        }
        connectLocalChannel(chan, remoteAddr, remotePort,
                            "127.0.0.1", remotePort);

        return chan;
    }

    /**
     * Deletes the local forward identified by the given local address and port
     * pair. Note that the channels that was previously opened through this
     * forward are not deleted, only the corresponding listener is stopped
     * preventing further channels to be opened through this forward.
     *
     * @param localAddr     the local address of the forward
     * @param localPort     the local port of the forward
     */
    public synchronized void deleteLocalForward(String localAddr,
            int localPort) {
    	logger.debug(
                      localAddr + ":" + localPort);

        ListenerSSH2 listener =
            (ListenerSSH2)localForwards.get(localAddr + ":" +
                                            localPort);
        if(listener != null) {
            listener.stop();
            localForwards.remove(localAddr + ":" + localPort);
        }
    }

    /**
     * Creates a new session channel.
     *
     * @return the new session channel
     */
    public synchronized SessionChannel newSession() {
        return newSession((StreamFilterFactory)null);
    }

    /**
     * Creates a new session channel using the given filter factory 
     * for filtering the standard input/output streams of the session.
     *
     * @param filterFactory the filter factory to use
     *
     * @return the new session channel
     */
    public synchronized SessionChannel  newSession(StreamFilterFactory filterFactory) {
        if (!transport.isConnected())
            return null;
        SessionChannel channel = new SessionChannel(this);

        if(filterFactory != null) {
            channel.applyFilter(filterFactory.createFilter(this, channel));
        }

        TransportPDUSSH2 pdu = getChannelOpenPDU(channel);
        transmit(pdu);
        return channel;
    }

    /**
     * Creates a new session channel attaching its standard input/output streams
     * to the given terminal adapter. It is up to the terminal adapter
     * implementation to attach itself to the I/O streams of the session
     * channel. For this purpose the interface method <code>attach</code> is
     * called before the channel open message is sent to the server so the
     * terminal adapter is attached before I/O is started.
     *
     * @param termAdapter the terminal adapter to attach to the session
     *
     * @return the new session channel
     */
    public synchronized SessionChannel newTerminal(TerminalAdapterSSH2 termAdapter) {
        if (!transport.isConnected())
            return null;

        SessionChannel channel = new SessionChannel(this);
        TransportPDUSSH2   pdu     = getChannelOpenPDU(channel);

        termAdapter.attach(channel);

        transmit(pdu);
        return channel;
    }

    public void setSocketOptions(String desc, Socket sock) throws IOException {
        transport.setSocketOptions(desc, sock);
    }

    synchronized boolean hasX11Mapping() {
        boolean hasMapping = (x11Mappings > 0);
        if(x11Single) {
            x11Mappings--;
        }
        return hasMapping;
    }

    public synchronized void setX11Mapping(boolean single) {
        x11Single = single;
        x11Mappings++;
    }

    public synchronized void clearX11Mapping() {
        if(x11Mappings > 0) {
            x11Mappings--;
        }
    }

    public byte[] getX11FakeCookie() {
        if(x11FakeCookie == null) {
            x11FakeCookie = new byte[16];
            SecureRandomAndPad srap = (SecureRandomAndPad)
                                      transport.getSecureRandom();
            srap.nextPadBytes(x11FakeCookie, 0, 16);
        }
        return x11FakeCookie;
    }

    public void setX11RealCookie(byte[] cookie) {
        x11RealCookie = cookie;
    }

    byte[] getX11RealCookie() {
        if(x11RealCookie == null) {
            x11RealCookie = getX11FakeCookie();
        }
        return x11RealCookie;
    }

    synchronized void terminate() {
        if(connector != null) {
            connector.stop();
        }
        Enumeration listeners = localForwards.elements();
        while(listeners.hasMoreElements()) {
            ((ListenerSSH2)listeners.nextElement()).stop();
        }
        for(int i = 0; i < channels.length; i++) {
            if(channels[i] != null) {
                channels[i].close();
                channels[i] = null;
            }
        }
        if(reaperActive) {
            stopChannelReaper();
        }
    }

    public synchronized void addChannel(ChannelSSH2 channel) {
        int newChan = nextEmptyChan;
        if(nextEmptyChan < channels.length) {
            int i;
            for(i = nextEmptyChan + 1; i < channels.length; i++)
                if(channels[i] == null)
                    break;
            nextEmptyChan = i;
        } else {
            if(channels.length + 16 > MAX_ACTIVE_CHANNELS) {
                fatalDisconnect(SSH2.DISCONNECT_TOO_MANY_CONNECTIONS,
                                "Number of channels exceeded maximum");
                return;
            }
            ChannelSSH2[] tmp = new ChannelSSH2[channels.length + 16];
            System.arraycopy(channels, 0, tmp, 0, channels.length);
            channels = tmp;
            nextEmptyChan++;
        }
        channel.channelId = newChan;
        channels[newChan] = channel;
        totalChannels++;
        eventHandler.channelAdded(this, channel);
    }

    synchronized void killChannel(ChannelSSH2 channel) {
        if(channel == null || channel.channelId == -1 ||
                channel.channelId >= channels.length ||
                channels[channel.channelId] == null) {
        	logger.error(
                          "ch. # " + (channel != null ?
                                      String.valueOf(channel.getChannelId()) :
                                      "<null>") +
                          " not present");
            return;
        }
        totalChannels--;
        channels[channel.channelId] = null;
        if(channel.channelId < nextEmptyChan)
            nextEmptyChan = channel.channelId;
        eventHandler.channelDeleted(this, channel);
    }

    public void delChannel(ChannelSSH2 channel) {
        if(reaperActive) {
            life.addElement(channel);
        } else {
            killChannel(channel);
        }
    }

    TransportPDUSSH2 getChannelOpenPDU(ChannelSSH2 channel) {
        TransportPDUSSH2 pdu = TransportPDUSSH2.createOutgoingPacket(SSH2.MSG_CHANNEL_OPEN);
        pdu.writeString(channelTypes[channel.channelType]);
        pdu.writeInt(channel.channelId);
        pdu.writeInt(channel.rxInitWinSz);
        pdu.writeInt(channel.rxMaxPktSz);
        return pdu;
    }

    public void connectLocalChannel(ChannelSSH2 channel,
                                    String remoteAddr, int remotePort,
                                    String originAddr, int originPort) {
        TransportPDUSSH2 pdu = getChannelOpenPDU(channel);

        pdu.writeString(remoteAddr);
        pdu.writeInt(remotePort);
        pdu.writeString(originAddr);
        pdu.writeInt(originPort);

        transmit(pdu);
    }

    private Vector           life;
    private volatile boolean reaperActive;

    /**
     * Start a new thread which repeatedly tries to kill channels
     * which should be killed.
     */
    protected void startChannelReaper() {
        this.life = new Vector();
        this.reaperActive = true;
        Thread reaper = new Thread(this, "SSH2ChannelReaper");
        reaper.setDaemon(true);
        reaper.setPriority(Thread.MIN_PRIORITY);
        reaper.start();
    }

    /**
     * Stop the channel reaper.
     */
    protected void stopChannelReaper() {
        reaperActive = false;
    }

    /**
     * The <code>run</code> routine which implements the channel reaper.
     */
    public void run() {
        Vector death = new Vector();
        while(reaperActive) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                /* Don't care really, somebody interrupted us? */
            }
            while(!death.isEmpty()) {
                ChannelSSH2 toBeKilled = (ChannelSSH2)death.firstElement();
                killChannel(toBeKilled);
                death.removeElementAt(0);
            }
            Vector limbo = death;
            death = life;
            life  = limbo;
        }
    }

	/**
	 * @return the filter
	 */
	public ConsoleCharsetFilter getFilter() {
		return filter;
	}

	/**
	 * @param filter the filter to set
	 */
	public void setFilter(ConsoleCharsetFilter filter) {
		this.filter = filter;
	}

}
