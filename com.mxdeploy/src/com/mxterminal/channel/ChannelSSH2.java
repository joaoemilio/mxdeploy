package com.mxterminal.channel;

import java.util.Enumeration;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.mxterminal.ssh2.ConnectionSSH2;
import com.mxterminal.ssh2.ListenerSSH2;
import com.mxterminal.ssh2.PreferencesSSH2;
import com.mxterminal.ssh2.SSH2;
import com.mxterminal.ssh2.TransportPDUSSH2;


/**
 * This is the abstract base class for all channels as defined in the connection
 * protocol spec. Each channel has a specific type (e.g. session). Each channel
 * type is implemented by a subclass to this class. This base class makes no
 * assumptions as to how data is handled by the channel it only implements
 * methods which are used from the multiplexing code in
 * <code>SSH2Connection</code>.
 * <p>
 * When implementing a new channel type or implementing an existing one
 * differently from what available one typically subclasses
 * <code>SSH2StreamChannel</code> instead of <code>SSH2Channel</code> directly
 * since it implements the notion of streams (through use of
 * <code>java.io.InputStream</code> and <code>java.io.OutputStream</code>) and
 * flow control. Only a very specific implementation of a channel would need to
 * subclass <code>SSH2Channel</code> directly.
 
 *
 * @see ConnectionSSH2
 * @see StreamChannel
 */
public abstract class ChannelSSH2 {
	
	static Logger logger = Logger.getLogger(ChannelSSH2.class);
	
    public final static int STATUS_UNDEFINED = 0;
    public final static int STATUS_OPEN      = 1;
    public final static int STATUS_CLOSED    = 2;
    public final static int STATUS_FAILED    = 3;

    public ConnectionSSH2 connection;

    public Vector closeListeners;

    public int channelType;

    public int channelId;
    public int peerChanId;

    public int rxMaxPktSz;
    public int rxInitWinSz;
    public int rxCurrWinSz;

    public int txInitWinSz;
    public int txCurrWinSz;
    public int txMaxPktSz;

    protected volatile boolean eofSent;
    protected volatile boolean eofReceived;
    protected volatile boolean closeReceived;
    protected volatile boolean closeSent;
    protected volatile boolean deleted;

    private final Object statusMonitor = new Object();

    protected Object creator;

    protected final Object openMonitor = new Object();
    protected int    openStatus;

    /**
     * Create a new channel of the given type. The channel is
     * associated with an ssh connections.
     *
     * @param channelType Type of channel to create. Channel types are
     * defined in <code>SSH2Connection</code> and starts with
     * <code>CH_TYPE</code>.
     * @param connection The ssh connection to associate the channel with.
     * @param creator The object the channel is created from.
     */
    protected ChannelSSH2(int channelType, ConnectionSSH2 connection,
                          Object creator) {
        this.channelType = channelType;
        this.connection  = connection;
        this.creator     = creator;
        this.rxInitWinSz = connection.getPreferences().
                           getIntPreference(PreferencesSSH2.RX_INIT_WIN_SZ);
        this.rxCurrWinSz = this.rxInitWinSz;
        this.rxMaxPktSz  = connection.getPreferences().
                           getIntPreference(PreferencesSSH2.RX_MAX_PKT_SZ);
        this.openStatus  = STATUS_UNDEFINED;
        connection.addChannel(this);
    }

    /**
     * Handle the open confirmation packet from the transport layer.
     *
     * @param pdu Confirmation packet.
     */
    public final void openConfirmation(TransportPDUSSH2 pdu) {
        int peerChanId  = pdu.readInt();
        int txInitWinSz = pdu.readInt();
        int txMaxPktSz  = pdu.readInt();

        synchronized (this) {
            init(peerChanId, txInitWinSz, txMaxPktSz);
            openConfirmationImpl(pdu);
        }
        
        switch(channelType) {
        case ConnectionSSH2.CH_TYPE_FWD_TCPIP:
            connection.getEventHandler().localForwardedConnect(connection,
                    (ListenerSSH2)creator,
                    this);
            break;
        case ConnectionSSH2.CH_TYPE_DIR_TCPIP:
            // Must check if creator is a SSH2Listener since it can be created
            // without a socket (i.e. an "internal" connection)
            //
            if(creator instanceof ListenerSSH2) {
                connection.getEventHandler().localDirectConnect(connection,
                        (ListenerSSH2)creator,
                        this);
            }
            break;
        case ConnectionSSH2.CH_TYPE_SESSION:
            connection.getEventHandler().localSessionConnect(connection,
                    this);
            break;
        case ConnectionSSH2.CH_TYPE_X11:
            connection.getEventHandler().localX11Connect(connection,
                    (ListenerSSH2)creator,
                    this);
            break;
            /* !!! TODO
               case SSH2Connection.CH_TYPE_AUTH_AGENT:
                   connection.getEventHandler().localDirectConnect(connection,
                       (SSH2Listener)creator,
                this);
                   break; */
        }

        synchronized (openMonitor) {
            this.openStatus = STATUS_OPEN;
            openMonitor.notifyAll();
        }

        logger.info("open confirmation, ch. #" + channelId +
        		    ", init-winsz = " + this.txInitWinSz +
                    ", max-pktsz = " + this.txMaxPktSz);
    }

    /**
     * Called when the channel open failed.
     *
     * @param reasonCode Code which tells why the open failed. See the
     * ssh protocol drafts for values.
     * @param reasonText A text explaining why the open failed.
     * @param langTag Tag identifying the language of the reason text.
     */
    public final void openFailure(int reasonCode,
            String reasonText,
            String langTag) {

        synchronized (statusMonitor) {
            closeSent = true;
            eofSent   = true;
        }

        boolean keepChannel;

        synchronized (this) {
            keepChannel = openFailureImpl(reasonCode, reasonText, langTag);
        }

        connection.getEventHandler().localChannelOpenFailure
            (connection, this, reasonCode, reasonText, langTag);
        if(!keepChannel) {
            connection.delChannel(this);
        }

        synchronized (openMonitor) {
            this.openStatus  = STATUS_FAILED;
            openMonitor.notifyAll();
        }

        logger.info("open failure on ch. #" +
                                 channelId + ", reason: " + reasonText);
    }

    /**
     * Handle a window adjust message.
     *
     * @param pdu The window adjust packet.
     */
    public final void windowAdjust(TransportPDUSSH2 pdu) {
        int inc = pdu.readInt();
        windowAdjustImpl(inc);
    }

    /**
     * Handle incoming data on the channel.
     *
     * @param pdu The data packet.
     */
    public void data(TransportPDUSSH2 pdu) {}

    /**
     * Handle incoming extended data on the channel.
     *
     * @param pdu The data packet.
     */
    public void extData(TransportPDUSSH2 pdu) {}

    /**
     * Handle incoming channel request.
     *
     * @param pdu The request packet.
     */
    public final void handleRequest(TransportPDUSSH2 pdu) {
        String  reqType   = pdu.readJavaString();
        boolean wantReply = pdu.readBoolean();
        handleRequestImpl(reqType, wantReply, pdu);
    }

    /**
     * Handle positive request response.
     *
     * @param pdu The response packet.
     */
    public void requestSuccess(TransportPDUSSH2 pdu) {}

    /**
     * Handle negative request response.
     *
     * @param pdu The response packet.
     */
    public void requestFailure(TransportPDUSSH2 pdu) {}

    /**
     * Check if this channel can be terminated. The channel can be
     * terminated when it had sent and received the close request.
     */
    private final void checkTermination() {
        synchronized (statusMonitor) {
            if(closeSent && closeReceived && !deleted) {
                deleted = true;
            } else {
                return;
            }
        }

        connection.delChannel(this);

        synchronized(this) {
            if(closeListeners != null) {
                Enumeration e = closeListeners.elements();
                while(e.hasMoreElements()) {
                    ChannelCloseListener closeListener =
                        (ChannelCloseListener)e.nextElement();
                    closeListener.closed(this);
                }
            }
        }

        synchronized (openMonitor) {
            openStatus = STATUS_CLOSED;
            openMonitor.notifyAll();
        }
    }

    /**
     * Handle the recipent of an EOF.
     */
    public final void recvEOF() {
        if(eofReceived) {
        	logger.debug( "ch. # " + channelId +
                                      " received multiple EOFs");
        }
        eofReceived = true;
        eofImpl();
        if(eofSent) {
            sendClose();
        }
    }

    /**
     * Handle the recipent of an channel close message.
     */
    public final void recvClose() {
        boolean dosend = false;
        synchronized (statusMonitor) {
            if(!closeReceived) {
                closeReceived = true;
                eofSent       = true;
                dosend = true;
            }
        }
        if (dosend) {
            closeImpl();
            sendClose();
            logger.debug("closing ch. #" + channelId +
                                      " (" + getType() + ")");
            connection.getEventHandler().channelClosed(connection, this);
        }
        checkTermination();
    }

    /**
     * Send channel EOF.
     */
    public final void sendEOF() {
        synchronized (statusMonitor) {
            if(!eofSent && !closeSent) {
                eofSent = true;

                TransportPDUSSH2 pdu =
                    TransportPDUSSH2.createOutgoingPacket(SSH2.MSG_CHANNEL_EOF);
                pdu.writeInt(peerChanId);
                connection.transmit(pdu);
            }
        }
    }

    /**
     * Send a channel close request.
     */
    public final void sendClose() {
        boolean dosend = false;
        synchronized (statusMonitor) {
            if(!closeSent) {
                closeSent = true;
                dosend = true;
            }
        }

        if (dosend) {
            TransportPDUSSH2 pdu =
                TransportPDUSSH2.createOutgoingPacket(SSH2.MSG_CHANNEL_CLOSE);
            pdu.writeInt(peerChanId);
            connection.transmit(pdu);
        }
        checkTermination();
    }

    public void init(int peerChanId, int txInitWinSz, int txMaxPktSz) {
        int prefTxMaxPktSz = connection.getPreferences().
                             getIntPreference(PreferencesSSH2.TX_MAX_PKT_SZ);
        txMaxPktSz =
            (txMaxPktSz > prefTxMaxPktSz ? prefTxMaxPktSz : txMaxPktSz);
        this.peerChanId  = peerChanId;
        this.txInitWinSz = txInitWinSz;
        this.txMaxPktSz  = txMaxPktSz;
        this.txCurrWinSz = txInitWinSz;
    }

    public void transmit(TransportPDUSSH2 pdu) {
        if(!closeSent) {
            connection.transmit(pdu);
        }
    }

    /**
     * Checks if the channel currently is open or not.
     *
     * @return The status. One of <code>STATUS_OPEN</code>,
     * <code>STATUS_CLOSED</code> or <code>STATUS_FAILED</code>
     */
    public int openStatus() {
        synchronized (openMonitor) {
            if(openStatus == STATUS_UNDEFINED) {
                try {
                    openMonitor.wait();
                } catch (InterruptedException e) {
                    /* don't care, someone interrupted us on purpose */
                }
            }
            return openStatus;
        }
    }

    /**
     * Wait until the channel is closed.
     */
    public void waitUntilClosed() {
        synchronized (openMonitor) {
            while(openStatus != STATUS_CLOSED) {
                try {
                    openMonitor.wait();
                } catch (InterruptedException e) {
                    /* don't care, someone interrupted us on purpose */
                }
            }
        }
    }

    /**
     * Close the channel.
     */
    public final void close() {
        if(!connection.getTransport().isConnected()) {
            recvClose();
        }
        sendClose();
    }

    /**
     * Get the type of channel.
     *
     * @return The channel type.
     */
    public String getType() {
        return ConnectionSSH2.channelTypes[channelType];
    }

    /**
     * Get the local id of the channel.
     *
     * @return The local channel id.
     */
    public int getChannelId() {
        return channelId;
    }

    /**
     * Get the peer if of the channel.
     *
     * @return The peer channel id.
     */
    public int getPeerId() {
        return peerChanId;
    }

    /**
     * Get the channel creator object reference.
     *
     * @return The channel creator.
     */
    public Object getCreator() {
        return creator;
    }

    /**
     * Get the connection a channel is using.
     *
     * @return The connection object.
     */
    public ConnectionSSH2 getConnection() {
        return connection;
    }

    /**
     * Channel specific handling of open confirmations.
     *
     * @param pdu Confirmation packet.
     */
    protected abstract void openConfirmationImpl(TransportPDUSSH2 pdu);

    /**
     * Channel specific handling of open failures.
     *
     * @param reasonCode Code which tells why the open failed. See the
     * ssh protocol drafts for values.
     * @param reasonText A text explaining why the open failed.
     * @param langTag Tag identifying the language of the reason text.
     */
    protected abstract boolean openFailureImpl(int reasonCode,
            String reasonText,
            String langTag);

    /**
     * Channel specific implementation of window adjust messages.
     *
     * @param inc The amount to increase the window with.
     */
    protected abstract void windowAdjustImpl(int inc);

    /**
     * Channel specific handler for the recipent of an EOF.
     */
    protected abstract void eofImpl();

    /**
     * Channel specific handler for the recipent of a channel close message.
     */
    protected abstract void closeImpl();

    /**
     * Channel specific handler for incoming channel requests.
     *
     * @param reqType The type of request.
     * @param wantReply True if an reply is expected.
     * @param pdu The actual channel request.
     */
    protected abstract void handleRequestImpl(String reqType, boolean wantReply,
            TransportPDUSSH2 pdu);

    /**
     * Add a listener which is notified when the channel is closed.
     *
     * @param closeListener The listener to add.
     */
	public final void addCloseListener(ChannelCloseListener closeListener) {
        if(closeListeners == null) {
            closeListeners = new Vector();
        }
        closeListeners.removeElement(closeListener);
        closeListeners.addElement(closeListener);
    }

    /**
     * Remove a listener which were to be notified when the channel
     * were closed.
     *
     * @param closeListener The listener to remove.
     */
    public void removeCloseListener(ChannelCloseListener closeListener) {
        if(closeListeners != null) {
            closeListeners.removeElement(closeListener);
            if(closeListeners.size() == 0) {
                closeListeners = null;
            }
        }
    }

}
