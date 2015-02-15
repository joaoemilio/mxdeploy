package com.mxterminal.ssh2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.mxsecurity.jca.InvalidKeyException;
import com.mxsecurity.jca.MessageDigest;
import com.mxsecurity.jca.SecureRandom;
import com.mxsecurity.jce.Cipher;
import com.mxsecurity.jce.Mac;
import com.mxsecurity.jce.ShortBufferException;
import com.mxsecurity.jce.spec.IvParameterSpec;
import com.mxsecurity.jce.spec.SecretKeySpec;
import com.mxsecurity.util.SecureRandomAndPad;
import com.mxterminal.ssh2.auth.UserAuth;
import com.mxterminal.ssh2.exception.CompressionException;
import com.mxterminal.ssh2.exception.ConnectException;
import com.mxterminal.ssh2.exception.CorruptPacketException;
import com.mxterminal.ssh2.exception.EOFException;
import com.mxterminal.ssh2.exception.ExceptionSSH2;
import com.mxterminal.ssh2.exception.FatalExceptionSSH2;
import com.mxterminal.ssh2.exception.MacCheckExceptionSSH2;
import com.mxterminal.ssh2.key.BTSignature;
import com.mxterminal.ssh2.key.BTSignatureException;
import com.mxterminal.ssh2.key.KeyExchanger;
import com.mxterminal.util.Trigger;

public class TransportSSH2 {
	static Logger logger = Logger.getLogger(TransportSSH2.class);
	
    private class KeepAliveThread implements Runnable {
        private volatile int     interval;
        private volatile boolean keepRunning;

        protected KeepAliveThread(int interval) {
            this.interval    = interval;
            this.keepRunning = true;
            Thread heartbeat = new Thread(this, "SSH2TransportKeepAlive");
            heartbeat.setDaemon(true);
            heartbeat.setPriority(Thread.MIN_PRIORITY);
            heartbeat.start();
        }

        protected synchronized void setInterval(int interval) {
            if(interval < 1) {
                stop();
            } else {
                this.interval = interval;
            }
        }

        protected int getInterval() {
            return interval;
        }

        public void run() {
            int inactive = 0;
            int totalInactive = 0;
            while (keepRunning) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) { /* ignore */
                }
                if (activity) {
                    inactive = 0;
                    totalInactive = 0;
                    activity = false;
                } else {
                    reportInactivity(++totalInactive);
                    if (++inactive >= interval) {
                        sendIgnore("heartbeat".getBytes());
                        Thread.yield();
                        inactive = 0;
                        activity = false;
                    }
                }
            }
        }

        protected void stop() {
            keepRunning = false;
        }

        public boolean isRunning() {
            return keepRunning;
        }

    }

    /**
     * Context for transport tranceiver/receiver. Holds the encryption
     * and compression states.
     */
    public static class TranceiverContext {

        protected Mac                mac;
        protected Cipher             cipher = null;
        //protected CompressorSSH2     compressor;

        public int getCipherBlockSize() {
            if (cipher != null) {
                return cipher.getBlockSize();
            } else {
                return 1;
            }
        }

        public void init(byte[] cKey, byte[] iv, byte[] mKey, int compLevel,
                         boolean transmitter)
            throws ExceptionSSH2 {
            try {
                if(cipher != null) {
                    cipher.init(transmitter ? Cipher.ENCRYPT_MODE :
                                Cipher.DECRYPT_MODE,
                                new SecretKeySpec(cKey, cipher.getAlgorithm()),
                                new IvParameterSpec(iv));
                }
                if(mac != null) {
                    mac.init(new SecretKeySpec(mKey, mac.getAlgorithm()));
                }
//                if(compressor != null) {
//                    compressor.init((transmitter ?
//                                     CompressorSSH2.COMPRESS_MODE :
//                                     CompressorSSH2.UNCOMPRESS_MODE),
//                                     compLevel);
//                }
            } catch (InvalidKeyException e) {
                throw new FatalExceptionSSH2("Invalid key in TranceiverContext.init");
			}
        }

        /**
         * Called when the authentication step has finished
         */
//        public void authSucceeded() {
//            if (compressor != null) {
//                compressor.authSucceeded();
//            }
//        }
    }

    private final static boolean DEBUG_ALL_TX = false;
    private final static boolean DEBUG_ALL_RX = false;

    private boolean weAreAServer;

    private String clientVersion;
    private String serverVersion;

    protected PreferencesSSH2 ourPrefs;
    protected PreferencesSSH2 peerPrefs;

    protected TransportEventHandlerSSH2 eventHandler;

    private          KeyExchanger keyExchanger;
    private volatile UserAuth     userAuth;
    private volatile ConnectionSSH2   connection;
    private boolean  authenticated = false;

    //protected Log tpLog;

    protected Socket       tpSocket;
    protected InputStream  tpIn;
    protected OutputStream tpOut;

    private Thread transmitter;
    private Thread receiver;
    private Trigger  txQueue;

    protected SecureRandomAndPad tpRand;

    private KeepAliveThread heartbeat;

    private byte[]           sessionId;
    private volatile boolean keyExchangeInProgress;
    private          boolean keyExchangeOk;
    private          Object  keyExchangeMonitor;
    private TransportPDUSSH2 clientKEXINITPkt;
    private TransportPDUSSH2 serverKEXINITPkt;
    private byte[]           serverPublicKeyBlob;

    private TranceiverContext rxContext;
    private TranceiverContext txContext;
    private int               rxSeqNum;
    private int               txSeqNum;
    private int               rxNumPacketsSinceKEX;
    private int               txNumPacketsSinceKEX;
    private int               rxNumBlocksSinceKEX;
    private int               txNumBlocksSinceKEX;
    private final static int  PACKETS_BEFORE_REKEY = 2147483647; // 2^31
    private long              blocks_before_rekey = 1073741824;   // 1Gb

    private          Object  disconnectMonitor;
    private volatile boolean isConnected;
    private volatile boolean isTxUp;
    private volatile boolean isRxUp;
    private          String  disconnectMessage;
    protected        boolean activity;

    // Incompatibility flags (peer's incompatibility of course :-)
    //
    public boolean incompatibleSignature;
    public boolean incompatiblePublicKeyAuth;
    public boolean incompatibleHMACKeyLength;
    public boolean incompatiblePublicKeyUserId;
    public boolean incompatibleChannelOpenFail;
    public boolean incompatibleRijndael;
    public boolean incompatibleCantReKey;
    public boolean incompatibleBuggyChannelClose;
    public boolean incompatibleMayReceiveDataAfterClose;
    public boolean incompatibleOldDHGex;

    /**
     * This is the basic constructor used when default preferences is ok and no
     * logging or event handling is needed.
     *
     * @param tpSocket the connection to the ssh2 server
     * @param rand the source of randomness for keys and padding
     */
    public TransportSSH2(Socket tpSocket, SecureRandomAndPad rand) {
        this(tpSocket, new PreferencesSSH2(), rand);
    }

    /**
     * This is the basic constructor used when no logging or event handling is
     * needed.
     *
     * @param tpSocket the connection to the ssh2 server
     * @param prefs    the protocol preferences
     * @param rand     the source of randomness for keys and padding
     */
    public TransportSSH2(Socket tpSocket, PreferencesSSH2 prefs, SecureRandomAndPad rand) {
        this(tpSocket, prefs, null, rand);
    }

    /**
     * This is the constructor used when an event handler is needed but no
     * logging.
     *
     * @param tpSocket     the connection to the ssh2 server
     * @param prefs        the protocol preferences
     * @param eventHandler the event handler which receives callbacks
     * @param rand         the source of randomness for keys and padding
     */
//    public TransportSSH2(Socket tpSocket, PreferencesSSH2 prefs,
//                         TransportEventHandlerSSH2 eventHandler,
//                         SecureRandomAndPad rand) {
//        this(tpSocket, prefs, eventHandler, rand, new Log(prefs.getIntPreference(PreferencesSSH2.LOG_LEVEL)));
//        String logFile = prefs.getPreference(PreferencesSSH2.LOG_FILE);
//        if(logFile != null) {
//            try {
//                boolean append = "true".equals(prefs.getPreference(PreferencesSSH2.LOG_APPEND));
//                FileOutputStream log = new FileOutputStream(logFile, append);
//                tpLog.setLogOutputStream(log);
//            } catch (IOException e) {
//                tpLog.error("SSH2Transport", "<init>", "could't open log file: " + e.getMessage());
//            }
//        }
//    }

    /**
     * This is the constructor used when both an event handler and logging is
     * needed.
     *
     * @param tpSocket     the connection to the ssh2 server
     * @param prefs        the protocol preferences
     * @param eventHandler the event handler which receives callbacks
     * @param rand         the source of randomness for keys and padding
     * @param log          the log handler which receives all logs
     */
    public TransportSSH2(Socket tpSocket, PreferencesSSH2 prefs,
                         TransportEventHandlerSSH2 eventHandler,
                         SecureRandomAndPad rand) { //, Log log) {
        this.disconnectMonitor  = new Object();
        this.keyExchangeMonitor = new Object();
        this.isConnected        = false;
        this.isTxUp             = false;
        this.isRxUp             = false;
        this.ourPrefs           = prefs;
        this.eventHandler       = (eventHandler != null ? eventHandler :
                                   new TransportEventAdapterSSH2());
        this.tpSocket           = tpSocket;
        this.tpRand             = rand;
        //this.tpLog              = log;

        TransportPDUSSH2.pktDefaultSize =
            ourPrefs.getIntPreference(PreferencesSSH2.DEFAULT_PKT_SZ);
        TransportPDUPoolSSH2.POOL_SIZE =
            ourPrefs.getIntPreference(PreferencesSSH2.PKT_POOL_SZ);

        try {
            setSocketOptions(PreferencesSSH2.SOCK_OPT_TRANSPORT, tpSocket);

            this.rxContext =
                TransportPDUSSH2.createTranceiverContext("none", "none", "none");
            this.txContext =
                TransportPDUSSH2.createTranceiverContext("none", "none", "none");
            this.tpIn  = tpSocket.getInputStream();
            this.tpOut = tpSocket.getOutputStream();
        } catch (Exception e) {
            // !!! TODO: pathological, fixit!!!
        }
    }

    /**
     * Starts the protocol engine and begins communication with the server. It
     * completes the version negotiation and starts two threads which handles
     * the protocol engine and all communication with the server. The key
     * exchange is started here.
     *
     * @exception ExceptionSSH2 if a fatal error occurs such as an I/O error or
     * a protocol mismatch.
     */
    public void boot() throws ExceptionSSH2 {
        boot(ourPrefs.getIntPreference(PreferencesSSH2.HELLO_TIMEOUT)*1000);
    }

    /**
     * Starts the protocol engine and begins communication with the server. It
     * completes the version negotiation and starts two threads which handles
     * the protocol engine and all communication with the server. The key
     * exchange is started here.
     *
     * @param timeout      handshake timeout in ms 
     *
     * @exception ExceptionSSH2 if a fatal error occurs such as an I/O error or
     * a protocol mismatch.
     */
    public void boot(int timeout) throws ExceptionSSH2 {
        synchronized(disconnectMonitor) {
            if(isConnected) {
                throw new FatalExceptionSSH2("Already booted");
            }
            isConnected = true;
        }
        try {
            int oldtimeout = tpSocket.getSoTimeout();
            if (timeout >= 0)
                tpSocket.setSoTimeout(timeout);
            negotiateVersion();
            if (timeout >= 0) 
                tpSocket.setSoTimeout(oldtimeout);
        } catch (IOException e) {
            throw new FatalExceptionSSH2("I/O error in version negotiation", e);
        }

        transmitter = new Thread(new Runnable() {
                                     public void run() {
                                         transportTransmitLoop();
                                     }
                                 }
                                 , "SSH2TransportTX");

        txQueue = new
                  Trigger(ourPrefs.getIntPreference(PreferencesSSH2.QUEUE_DEPTH),
                        ourPrefs.getIntPreference(PreferencesSSH2.QUEUE_HIWATER));
        transmitter.start();

        // Note we start the receiver AFTER we do startKeyExchange() to avoid
        // race with startKeyExchange() in receiver
        //
        startKeyExchange();

        receiver = new Thread(new Runnable() {
                                  public void run() {
                                      transportReceiveLoop();
                                  }
                              }
                              , "SSH2TransportRX");
        receiver.start();
    }

    public void setSocketOptions(String desc, Socket sock) throws IOException {
        String  prefix = PreferencesSSH2.SOCK_OPT + desc;
        String  val    = ourPrefs.getPreference(prefix + "." +
                                                PreferencesSSH2.SO_TCP_NODELAY);
        if(val != null) {
            sock.setTcpNoDelay(Boolean.valueOf(val).booleanValue());
        }
        /* TODO more socket options goes here... */
    }

    public String getLocalHostName() {
        return tpSocket.getLocalAddress().getHostName();
    }

    /**
     * Gets the session identifier calculated at key exchange as defined in the
     * protool spec.
     *
     * @return the session identifier as a byte array.
     */
    public byte[] getSessionId() {
        byte[] id = sessionId;
        if(!incompatiblePublicKeyUserId) {
            DataBufferSSH2 buf =
                new DataBufferSSH2(sessionId.length + 4);
            buf.writeString(sessionId);
            id = buf.readRestRaw();
        }
        return id;
    }

    /**
     * Gets the PDU containing the key exchange initialization (KEXINIT) sent by
     * the client.
     *
     * @return the PDU containing the KEXINIT packet
     */
    public TransportPDUSSH2 getClientKEXINITPDU() {
        return clientKEXINITPkt;
    }

    /**
     * Gets the PDU containing the key exchange initialization (KEXINIT) sent by
     * the server.
     *
     * @return the PDU containing the KEXINIT packet.
     */
    public TransportPDUSSH2 getServerKEXINITPDU() {
        return serverKEXINITPkt;
    }

    /**
     * Gets the client's version string
     *
     * @return the client's version string
     */
    public String getClientVersion() {
        return clientVersion;
    }

    /**
     * Gets the server's version string.
     *
     * @return the server's version string.
     */
    public String getServerVersion() {
        return serverVersion;
    }

    /**
     * Gets our preferences.
     *
     * @return our preferences.
     */
    public PreferencesSSH2 getOurPreferences() {
        return ourPrefs;
    }

    /**
     * Gets the preferences peer want.
     *
     * @return peer's preferences.
     */
    public PreferencesSSH2 getPeerPreferences() {
        return peerPrefs;
    }

    /**
     * Sets the event handler to use.
     *
     * @param eventHandler the event handler to use.
     */
    public void setEventHandler(TransportEventHandlerSSH2 eventHandler) {
        if(eventHandler != null) {
            this.eventHandler = eventHandler;
        }
    }

    /**
     * Gets the event handler currently in use.
     *
     * @return the event handler currently in use.
     */
    public TransportEventHandlerSSH2 getEventHandler() {
        return eventHandler;
    }

    /**
     * Gets the log handler currently in use.
     *
     * @return the log handler currently in use.
     */
//    public Log getLog() {
//        return tpLog;
//    }

    /**
     * Sets the log handler to use.
     *
     * @param log the log handler to use
     */
//    public void setLog(Log log) {
//        tpLog = log;
//    }

    /**
     * Checks whether we are a server or a client.
     *
     * @return a boolean indicating if we are a server or not.
     */
    public boolean isServer() {
        return weAreAServer;
    }

    /**
     * Gets the <code>SecureRandom</code> currently in use.
     *
     * @return the <code>SecureRandom</code> in use.
     */
    public SecureRandom getSecureRandom() {
        return tpRand;
    }

    /**
     * Gets the <code>SSH2Compressor</code> currently in use for the
     * receiver.
     *
     * @return the <code>SSH2Compressor</code> in use,
     * <code>null</code> if none.
     */
//    public CompressorSSH2 getRxCompressor() {
//        return rxContext.compressor;
//    }

    /**
     * Gets the <code>SSH2Compressor</code> currently in use for the
     * transmitter.
     *
     * @return the <code>SSH2Compressor</code> in use,
     * <code>null</code> if none.
     */
//    public CompressorSSH2 getTxCompressor() {
//        return txContext.compressor;
//    }

    /**
     * Sets the <code>SSH2UserAuth</code> to use for the user authenticaton
     * stage. The user authentication service is started from the class
     * <code>SSH2UserAuth</code> through the method <code>requestService</code>.
     *
     * @param userAuth the userAuth layer
     */
    public void setUserAuth(UserAuth userAuth) {
        this.userAuth = userAuth;
    }

    /**
     * Requests the given service (currently the only service defined is the
     * "ssh-userauth" service which starts the user authentication).
     *
     * @param service the name of the service to request
     */
    public void requestService(String service) {
        TransportPDUSSH2 pdu =
            TransportPDUSSH2.createOutgoingPacket(SSH2.MSG_SERVICE_REQUEST);
        pdu.writeString(service);
        transmit(pdu);
    }

    /**
     * Sets the <code>SSH2Connection</code> to use for the connection layer. All
     * actions after this stage are made through the connection layer.
     *
     * @param connection the connection layer
     */
    public void setConnection(ConnectionSSH2 connection) {
        this.connection = connection;
    }

    /**
     * Starts a key exchange with the preferences set in the constructor. If a
     * key exchange is allready in progress this call have no effect.
     *
     * @exception ExceptionSSH2 if a fatal error occurs.
     */
    public void startKeyExchange() throws ExceptionSSH2 {
        this.startKeyExchange(ourPrefs);
    }

    /**
     * Starts a key exchange with the given preferences. That is change to new
     * preferences and negotiate these with the peer. If a key exchange is
     * allready in progress this call have no effect.
     *
     * @param newPrefs the new preferences to use
     *
     * @exception ExceptionSSH2 if a fatal error occurs
     */
    public void startKeyExchange(PreferencesSSH2 newPrefs)
    throws ExceptionSSH2 {
        synchronized(keyExchangeMonitor) {
            if(!keyExchangeInProgress) {
                if(incompatibleCantReKey && (peerPrefs != null)) {
                    throw new FatalExceptionSSH2("Error, peer '" +
                                                 (weAreAServer ? clientVersion :
                                                  serverVersion) +
                                                 "' doesn't support re-keying");
                }

                ourPrefs              = newPrefs;
                keyExchangeInProgress = true;

                if(incompatibleRijndael) {
                    removeRijndael();
                }

                txQueue.disable();

                rxNumPacketsSinceKEX = 0;
                txNumPacketsSinceKEX = 0;
                rxNumBlocksSinceKEX = 0;
                txNumBlocksSinceKEX = 0;

                sendKEXINIT();
            }
        }
    }

    /**
     * Waits (blocks) for key exchange to complete (if not in progress returns
     * immediately).
     *
     * @return a boolean indicating if key exchange was successful or not.  */
    public boolean waitForKEXComplete() {
        synchronized(keyExchangeMonitor) {
            if(keyExchangeInProgress) {
                try {
                    keyExchangeMonitor.wait
                        (ourPrefs.getIntPreference(PreferencesSSH2.KEX_TIMEOUT)*1000);
                } catch (InterruptedException e) {
                    /* don't care, someone interrupted us on purpose */
                }
            }
            return keyExchangeOk;
        }
    }

    /**
     * Checks if key exchange is currently in progress.
     *
     * @return a boolean indicating if key exchange is in progress or not.
     */
    public boolean keyExchangeInProgress() {
        return keyExchangeInProgress;
    }

    /**
     * Checks if currently connected to a server.
     *
     * @return a boolean indicating if we are connected or not.
     */
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * Gets the message describing why transport was disconnected. Useful when
     * an error occurs and we are not hooked up with an eventhandler to see it
     * (e.g. when an error occurs in the key exchange it's only reported as a
     * disconnect reason).
     *
     * @return the disconnect message or <code>null</code> if still connected
     */
    public String getDisconnectMessage() {
        String msg = null;
        synchronized(disconnectMonitor) {
            if(!isConnected) {
                msg = disconnectMessage;
            }
        }
        return msg;
    }

    /**
     * Sends an IGNORE packet (as defined in the protocol spec.) with the given
     * data as payload.
     *
     * @param data a byte array containing the payload
     */
    public void sendIgnore(byte[] data) {
        sendIgnore(data, 0, data.length);
    }

    /**
     * Sends an IGNORE packet (as defined in the protocol spec.) with the given
     * data as payload.
     *
     * @param data a byte array containing the payload
     * @param off  offset in <code>data</code> where payload starts
     * @param len  length of payload
     */
    public void sendIgnore(byte[] data, int off, int len) {
        TransportPDUSSH2 pdu =
            TransportPDUSSH2.createOutgoingPacket(SSH2.MSG_IGNORE);
        pdu.writeString(data, off, len);
        transmit(pdu);
    }

    /**
     * Sends a DEBUG packet (as defined in the protocol spec.) with the given
     * message.
     *
     * @param alwaysDisp boolean indicating whether this message must always be
     * displayed or not.
     * @param message    the debug message to send
     * @param language   the language tag to use
     */
    public void sendDebug(boolean alwaysDisp, String message, String language) {
        TransportPDUSSH2 pdu =
            TransportPDUSSH2.createOutgoingPacket(SSH2.MSG_DEBUG);
        pdu.writeBoolean(alwaysDisp);
        pdu.writeString(message);
        pdu.writeString(language);
        transmit(pdu);
    }

    /**
     * Enables keep-alive function which sends IGNORE packets on the given time
     * interval. This is to prevent the connection from beeing timed
     * out because of TCP connection subject to idle-timeouts in a
     * firewall.
     *
     * @param intervalSeconds interval time in seconds
     */
    public void enableKeepAlive(int intervalSeconds) {
        if(heartbeat != null && heartbeat.isRunning()) {
            heartbeat.setInterval(intervalSeconds);
        } else if(intervalSeconds > 0) {
            heartbeat = new KeepAliveThread(intervalSeconds);
        }
    }

    /**
     * Gets the keep-alive interval.
     *
     * @return the keep-alive interval, 0 means keep-alive is disabled
     */
    public int getKeepAliveInterval() {
        if(heartbeat != null) {
            return heartbeat.getInterval();
        }
        return 0;
    }

    /**
     * Disables keep-alive function.
     */
    public void disableKeepAlive() {
        if(heartbeat != null) {
            heartbeat.stop();
        }
        heartbeat = null;
    }

    private void kexComplete(boolean noError) {
        synchronized(keyExchangeMonitor) {
            keyExchangeInProgress = false;
            keyExchangeOk         = noError;
            keyExchangeMonitor.notifyAll();
            if(noError) {
                eventHandler.kexComplete(this);
            }
        }
    }

    private void authTerminate() {
        if(userAuth != null) {
            userAuth.terminate();
        }
    }

    /**
     * Transmits the given PDU if we are connected. The PDU is put in a queue
     * which is processed by the internal threads hence this call is can only
     * block in extreme cases since all internal queues are subject to flow
     * control to prevent memory from beeing exhausted.
     *
     * @param pdu PDU to send
     */
    public void transmit(TransportPDUSSH2 pdu) {
        if(isConnected) {
            txQueue.putLast(pdu);
        }
    }

    /**
     * Transmits the given PDU without checking if we are connected. This
     * version of transmit writes the PDU directly to the
     * <code>OutputStream</code> to the peer, hence it can only be used when
     * the transmitter is not running.
     *
     * @param pdu PDU to send
     *
     * @exception ExceptionSSH2 if an I/O exception or other fatal error occurs
     */
    public synchronized void transmitInternal(TransportPDUSSH2 pdu)
    throws ExceptionSSH2 {
        if(DEBUG_ALL_TX)
        	logger.debug("sending message of type: " +
                         SSH2.msgTypeString(pdu.pktType)+
                         pdu.getData()+
                         pdu.getPayloadOffset()+
                         pdu.getPayloadLength());
        try {
            pdu.writeTo(tpOut, txSeqNum++, txContext, tpRand);
        } catch (IOException e) {
            throw new FatalExceptionSSH2("Couldn't write packet of type " +
                                         SSH2.msgTypeString(pdu.pktType), e);
		} catch (ShortBufferException e) {
			e.printStackTrace();
		}
    }

    /**
     * Disconnects from peer using the DISCONNECT packet type with the given
     * reason and description. See the class <code>SSH2</code> for reason codes.
     *
     * @param reason      the reason code
     * @param description the textual description for the cause of disconnect
     *
     * @see SSH2
     */
    public void fatalDisconnect(int reason, String description) {
        disconnectInternal(reason, description,
                           /* !!! TODO: languageTag, from ourPrefs? */ "",
                           false);
    }

    /**
     * Disconnects from peer using the DISCONNECT packet type with the
     * reason code DISCONNECT_BY_APPLICATION and the given description.
     *
     * @param description the textual description for the cause of disconnect
     */
    public void normalDisconnect(String description) {
        disconnectInternal(SSH2.DISCONNECT_BY_APPLICATION, description,
                           /* !!! TODO: languageTag, from ourPrefs? */ "",
                           false);
    }

    protected void disconnectInternal(int reason, String description,
                                      String languageTag, boolean fromPeer) {
        synchronized(disconnectMonitor) {
            if(!isConnected) {
                return;
            }
            isConnected = false;
            disconnectMessage = description;
        }

        if(fromPeer) {
            eventHandler.peerDisconnect(this, reason, description, languageTag);
            logger.warn("disconnect by peer: " + description);
        } else if(reason == SSH2.DISCONNECT_BY_APPLICATION) {
            eventHandler.normalDisconnect(this, description, languageTag);
            logger.warn("disconnect by application: " + description);
        } else {
            eventHandler.fatalDisconnect(this, reason, description, languageTag);
            logger.error("disconnect: " + description);
        }

        if(!fromPeer && isTxUp) {
            //
            // !!! Pathological condition: tx may be exiting, will cause bug
            //
            txQueue.disable();

            TransportPDUSSH2 pdu =
                TransportPDUSSH2.createOutgoingPacket(SSH2.MSG_DISCONNECT);
            pdu.writeInt(reason);
            pdu.writeString(description);
            pdu.writeString(""); // !!! TODO: Handle the language

            try {
                transmitInternal(pdu);
            } catch (ExceptionSSH2 e) {
            	logger.warn("error writing disconnect msg: " + e);
            }
        }

        disableKeepAlive();

        shutdownRx();
        shutdownTx();

        if(connection != null) {
            connection.terminate();
        }

        //tpLog.close();
    }

    private void negotiateVersion() throws IOException, ExceptionSSH2 {
        String idString;
        int crudCount = 0;
        String ourVersion =
            SSH2.getVersionId(ourPrefs.getPreference(PreferencesSSH2.PKG_VERSION));

        if(weAreAServer) {
            serverVersion = ourVersion;
            idString =  serverVersion + "\r\n";
            tpOut.write(idString.getBytes());
            tpOut.flush();
            clientVersion = idString;
            logger.warn("peer's version is '" +
                         clientVersion + "'");
        } else {
            clientVersion = ourVersion;
            idString =  clientVersion + "\r\n";
            tpOut.write(idString.getBytes());
            tpOut.flush();
            while(!(idString = readIdString()).startsWith("SSH-")) {
                if (++crudCount > 5) {
                    throw new ConnectException("Not an SSH server");
                }
                eventHandler.gotConnectInfoText(this, idString);
            }
            serverVersion = idString;
            logger.warn("peer's version is '" +
                         serverVersion + "'");
        }

        checkPeerVersion(clientVersion, serverVersion);
    }

    private void checkPeerVersion(String clientVersion, String serverVersion)
    throws ExceptionSSH2 {
        String cliPackage = extractPackageVersion(clientVersion);
        String srvPackage = extractPackageVersion(serverVersion);
        int cliMajor = extractMajor(clientVersion);
        int cliMinor = extractMinor(clientVersion);
        int srvMajor = extractMajor(serverVersion);
        int srvMinor = extractMinor(serverVersion);

        if(weAreAServer) {
            eventHandler.gotPeerVersion(this, clientVersion,
                                        cliMajor, cliMinor, cliPackage);
        } else {
            eventHandler.gotPeerVersion(this, serverVersion,
                                        srvMajor, srvMinor, srvPackage);
        }

        if(cliMajor != srvMajor && !(srvMajor == 1 && srvMinor == 99)) {
            String msg;
            if(weAreAServer) {
                msg = "Can't serve a client with version " + clientVersion;
            } else {
                msg = "Can't connect to a server with version " + serverVersion;
            }
            throw new FatalExceptionSSH2(msg);
        }

        String peerPackage = (weAreAServer ? cliPackage : srvPackage);

        if(peerPackage.startsWith("2.0.7") ||
                peerPackage.startsWith("2.0.8") ||
                peerPackage.startsWith("2.0.9")) {
            throw new FatalExceptionSSH2("Peer's version is too old: " + peerPackage);
        }

        incompatiblePublicKeyAuth = peerPackage.startsWith("2.0.11") ||
                                    peerPackage.startsWith("2.0.12");

        incompatibleChannelOpenFail = incompatiblePublicKeyAuth ||
                                      peerPackage.startsWith("2.0.13") ||
                                      peerPackage.startsWith("2.0.14") ||
                                      peerPackage.startsWith("2.0.15") ||
                                      peerPackage.startsWith("2.0.16") ||
                                      peerPackage.startsWith("2.0.17") ||
                                      peerPackage.startsWith("2.0.18") ||
                                      peerPackage.startsWith("2.0.19");

        incompatibleMayReceiveDataAfterClose =
            (peerPackage.indexOf("F-SECURE") != -1) ||
            (peerPackage.indexOf("SSH Secure Shell") != -1);

        incompatibleSignature = peerPackage.startsWith("2.1.0 SSH") ||
                                (peerPackage.startsWith("2.1.0") &&
                                 peerPackage.indexOf("F-SECURE") != -1) ||
                                incompatiblePublicKeyAuth;

        incompatibleHMACKeyLength = incompatibleSignature ||
                                    peerPackage.startsWith("2.2.0 SSH") ||
                                    peerPackage.startsWith("2.3.0 SSH") ||
                                    ((peerPackage.startsWith("2.2.0") ||
                                      peerPackage.startsWith("2.3.0")) &&
                                     peerPackage.indexOf("F-SECURE") != -1);

        incompatibleBuggyChannelClose = incompatibleHMACKeyLength ||
                                        peerPackage.startsWith("2.4.0 SSH");

        incompatiblePublicKeyUserId = incompatibleSignature ||
                                      peerPackage.startsWith("OpenSSH_2.0") ||
                                      peerPackage.startsWith("OpenSSH_2.1") ||
                                      peerPackage.startsWith("OpenSSH_2.2");

        incompatibleRijndael = peerPackage.startsWith("OpenSSH_2.5.1p1") ||
                               peerPackage.startsWith("OpenSSH_2.5.0") ||
                               peerPackage.startsWith("OpenSSH_2.3");

        incompatibleCantReKey = incompatiblePublicKeyUserId ||
                                peerPackage.startsWith("OpenSSH_2.3")   ||
                                peerPackage.startsWith("OpenSSH_2.5.1") ||
                                peerPackage.startsWith("OpenSSH_2.5.2") ||
                                peerPackage.startsWith("Sun_SSH_1.0") ||
                                !("true".equals(ourPrefs.
                                                getPreference(PreferencesSSH2.QUEUED_RX_CHAN)));

        incompatibleOldDHGex = peerPackage.startsWith("OpenSSH_2.0") ||
            peerPackage.startsWith("OpenSSH_2.1") ||
            peerPackage.startsWith("OpenSSH_2.2") ||
            peerPackage.startsWith("OpenSSH_2.3") ||
            peerPackage.startsWith("OpenSSH_2.5.0") ||
            peerPackage.startsWith("OpenSSH_2.5.1") ||
            peerPackage.startsWith("OpenSSH_2.5.2");

        if(incompatiblePublicKeyAuth) {
        	logger.warn("enabling draft incompatible publickey method");
        }
        if (incompatibleChannelOpenFail) {
        	logger.warn(
                         "enabling draft incompatible SERVICE_ACCEPT");
        	logger.warn(
                         "enabling draft incompatible CHANNEL_OPEN_FAILURE");
        }
        if(incompatibleSignature) {
        	logger.warn(
                         "enabling draft incompatible signature format");
        }
        if(incompatibleHMACKeyLength) {
        	logger.warn(
                         "enabling rfc incompatible hmac key length");
        }
        if(incompatiblePublicKeyUserId) {
        	logger.warn(
                         "enabling draft incompatible session id for signature");
        }
        if(incompatibleRijndael) {
        	logger.warn(
                         "disabling aes/rijndael cipher, peer has buggy implementation");
        }
        if(incompatibleCantReKey) {
        	logger.warn(
                         "disabling key re-exchange, not implemented in peer");
        }
        if(incompatibleMayReceiveDataAfterClose) {
        	logger.warn(
                         "enabling workaround for buggy SSH servers that may send channel data after close");
        }
        if(incompatibleOldDHGex) {
        	logger.warn(
                         "enabling workaround for old DH GEX");
        }
    }

    /**
     * Extracts the major version from a version string (as defined in the
     * protocol spec.)
     *
     * @param versionStr the full version string
     *
     * @return the major version number
     *
     * @exception ExceptionSSH2 if there is a format error
     */
    public static int extractMajor(String versionStr) throws ExceptionSSH2 {
        try {
            int r = versionStr.indexOf('.', 4);
            return Integer.parseInt(versionStr.substring(4, r));
        } catch (NumberFormatException e) {
            throw new FatalExceptionSSH2("Corrupt version string: " +
                                         versionStr);
        }
    }

    /**
     * Extracts the minor version from a version string (as defined in the
     * protocol spec.)
     *
     * @param versionStr the full version string
     *
     * @return the minor version number
     *
     * @exception ExceptionSSH2 if there is a format error
     */
    public static int extractMinor(String versionStr) throws ExceptionSSH2 {
        try {
            int l = versionStr.indexOf('.', 4) + 1;
            int r = versionStr.indexOf('-', l);
            return Integer.parseInt(versionStr.substring(l, r));
        } catch (NumberFormatException e) {
            throw new FatalExceptionSSH2("Corrupt version string: " +
                                         versionStr);
        }
    }

    /**
     * Extracts the package version (defined as softwareversion and comments in
     * the protocol spec.) from a version string.
     *
     * @param versionStr the full version string
     *
     * @return the package version (i.e. software version and comments)
     *
     * @exception ExceptionSSH2 if there is a format error
     */
    public static String extractPackageVersion(String versionStr)
    throws ExceptionSSH2 {
        try {
            int i = versionStr.indexOf('-', 4) + 1;
            return versionStr.substring(i);
        } catch (Exception e) {
            throw new FatalExceptionSSH2("Corrupt version string: " +
                                         versionStr);
        }
    }

    private String readIdString() throws IOException, ExceptionSSH2 {
        byte[] buf = new byte[256];
        int    len = 0;
        int    c;

        while(true) {
            c = tpIn.read();
            if(c == -1) {
                throw new EOFException("Server closed connection before sending identification");
            }
            if(c == '\r')
                continue;
            if(c != '\n') {
                if (len >= buf.length) {
                    throw new FatalExceptionSSH2("Too long id string: " +
                                                 new String(buf));
                }
                buf[len++] = (byte)c;
            } else {
                return new String(buf, 0, len);
            }
        }
    }

    private void sendKEXINIT() throws ExceptionSSH2 {
        TransportPDUSSH2 pdu =
            TransportPDUSSH2.createOutgoingPacket(SSH2.MSG_KEXINIT);
        byte[] cookie = new byte[16];
        tpRand.nextBytes(cookie);
        pdu.writeRaw(cookie);
        ourPrefs.writeTo(pdu);
        pdu.writeBoolean(false);
        pdu.writeInt(0);

        if(weAreAServer) {
            serverKEXINITPkt = pdu.makeCopy();
        } else {
            clientKEXINITPkt = pdu.makeCopy();
        }

        transmitInternal(pdu);
        eventHandler.kexStart(this);
    }

    private void processKEXINIT(TransportPDUSSH2 pdu) throws ExceptionSSH2 {
        startKeyExchange();

        if(weAreAServer) {
            clientKEXINITPkt = pdu;
        } else {
            serverKEXINITPkt = pdu;
        }

        pdu.readRaw(16); // Cookie, we don't need it
        peerPrefs = new PreferencesSSH2();
        peerPrefs.readFrom(pdu);
        boolean firstKEXFollows = pdu.readBoolean();
        pdu.readInt(); // Reserved int, we don't need it

        logger.info("peer kex algorithms: " +
                   peerPrefs.getPreference(PreferencesSSH2.KEX_ALGORITHMS));
        logger.info("peer host key algorithms: " +
                   peerPrefs.getPreference(PreferencesSSH2.HOST_KEY_ALG));
        logger.info( "peer enc. alg. cli2srv: " +
                   peerPrefs.getPreference(PreferencesSSH2.CIPHERS_C2S));
        logger.info( "peer enc. alg. srv2cli: " +
                   peerPrefs.getPreference(PreferencesSSH2.CIPHERS_S2C));
        logger.info( "peer mac alg. cli2srv: " +
                   peerPrefs.getPreference(PreferencesSSH2.MACS_C2S));
        logger.info("peer mac alg. srv2cli: " +
                   peerPrefs.getPreference(PreferencesSSH2.MACS_S2C));
        logger.info( "peer comp. alg. cli2srv: " +
                   peerPrefs.getPreference(PreferencesSSH2.COMP_C2S));
        logger.info( "peer comp. alg. srv2cli: " +
                   peerPrefs.getPreference(PreferencesSSH2.COMP_S2C));
        logger.info( "our kex algorithms: " +
                   ourPrefs.getPreference(PreferencesSSH2.KEX_ALGORITHMS));
        logger.info( "our host key algorithms: " +
                   ourPrefs.getPreference(PreferencesSSH2.HOST_KEY_ALG));
        logger.info( "our enc. alg. cli2srv: " +
                   ourPrefs.getPreference(PreferencesSSH2.CIPHERS_C2S));
        logger.info( "our enc. alg. srv2cli: " +
                   ourPrefs.getPreference(PreferencesSSH2.CIPHERS_S2C));
        logger.info( "our mac alg. cli2srv: " +
                   ourPrefs.getPreference(PreferencesSSH2.MACS_C2S));
        logger.info( "our mac alg. srv2cli: " +
                   ourPrefs.getPreference(PreferencesSSH2.MACS_S2C));
        logger.info( "our comp. alg. cli2srv: " +
                   ourPrefs.getPreference(PreferencesSSH2.COMP_C2S));
        logger.info( "our comp. alg. srv2cli: " +
                   ourPrefs.getPreference(PreferencesSSH2.COMP_S2C));

        keyExchanger = ourPrefs.selectKEXAlgorithm(peerPrefs, weAreAServer);

        logger.info( "KEX algorithm chosen: " +
                   ourPrefs.getAgreedKEXAlgorithm());
        logger.info( "same KEX guessed? " +
                   ourPrefs.sameKEXGuess());
        logger.info( "first KEX follows? " + firstKEXFollows);

        if(!ourPrefs.canAgree(peerPrefs, weAreAServer)) {
            String errType = ourPrefs.getDisagreeType();
            String errtxt  = "No match in kex params '" +  errType +
                             "', our's: " + ourPrefs.getPreference(errType) +
                             ", peer's: " + peerPrefs.getPreference(errType);
            throw new FatalExceptionSSH2(errtxt);
        }

        if(firstKEXFollows && !ourPrefs.sameKEXGuess()) {
            // Discard next packet which is the incorrectly guessed KEX packet
            //
            try {
                receiveInternal();
            } catch (IOException e) {
                throw new FatalExceptionSSH2("I/O error when reading guessed " +
                                             "packet", e);
            } catch (ShortBufferException e) {
                throw new FatalExceptionSSH2("Internal error/bug: " +
                                             e.getMessage());
            }
            logger.info( "first KEX packet discarded, " +
                         "wrong initial guess");
        }

        eventHandler.kexAgreed(this, ourPrefs, peerPrefs);

        keyExchanger.init(this);
    }

    private void removeRijndael() {
        boolean removedAES = false;
        String l1, l2;
        l1 = ourPrefs.getPreference(PreferencesSSH2.CIPHERS_C2S);
        l2 = ourPrefs.getPreference(PreferencesSSH2.CIPHERS_S2C);

        int l1l = l1.length();
        int l2l = l2.length();

        l1 = ListUtilSSH2.removeAllPrefixFromList(l1, "aes");
        l1 = ListUtilSSH2.removeAllPrefixFromList(l1, "rijndael");
        l2 = ListUtilSSH2.removeAllPrefixFromList(l2, "aes");
        l2 = ListUtilSSH2.removeAllPrefixFromList(l2, "rijndael");

        if(l1.length() != l1l) {
            ourPrefs.setPreference(PreferencesSSH2.CIPHERS_C2S,
                                   l1);
            removedAES = true;
        }
        if(l2.length() != l2l) {
            ourPrefs.setPreference(PreferencesSSH2.CIPHERS_S2C,
                                   l2);
            removedAES = true;
        }
        if(removedAES) {
        	logger.warn(
                          "removed AES cipher from our preferences" +
                          " due to bug in peer's implementation");
        }
    }

    /**
     * Sends the NEWKEYS paket type and changes the transmitter keys according
     * to the current prefs (as negotiated before). Typically used from a
     * subclass to <code>SSH2KeyExchanger</code>.
     *
     * @exception ExceptionSSH2 if an error occurs while sending the packet.
     */
    public void sendNewKeys() throws ExceptionSSH2 {
        TransportPDUSSH2 pdu =
            TransportPDUSSH2.createOutgoingPacket(SSH2.MSG_NEWKEYS);
        transmitInternal(pdu);
        changeKeys(true);
        txQueue.enable();
    }

    /**
     * Authenticates the server through its host key. Typically used from a
     * subclass to <code>SSH2KeyExchanger</code>.
     *
     * @param serverHostKey byte array containing server's host key (e.g. a
     * public key blob or a certificate).
     * @param serverSigH    byte array containing server's signature of the
     * exchange hash which should be verified.
     * @param exchangeHash_H the exchange hash
     *
     * @exception ExceptionSSH2 if an error occurs
     */
    public void authenticateHost(byte[] serverHostKey, byte[] serverSigH,
                                 byte[] exchangeHash_H)
    throws ExceptionSSH2 {
    	logger.debug(
                     "Server's public host key: "+ serverHostKey);
    	logger.debug(
                     "Signature over H: "+ serverSigH);
    	logger.debug(
                     "Exchange hash H"+ exchangeHash_H);

        boolean       verified  = false;
        BTSignature signature =
            BTSignature.getInstance(ourPrefs.getAgreedHostKeyAlgorithm());

        signature.initVerify(serverHostKey);
        signature.setIncompatibility(this);

        verified = signature.verify(serverSigH, exchangeHash_H);

        if(verified) {
        	logger.info( "server's signature verified");
        } else {
            String msg = "server's signature didn't verify";
            logger.error(msg);
            fatalDisconnect(SSH2.DISCONNECT_HOST_KEY_NOT_VERIFIABLE, msg);
            throw new FatalExceptionSSH2(msg);
        }

        if (serverPublicKeyBlob == null) {
            if(!eventHandler.kexAuthenticateHost(this, signature)) {
                throw new BTSignatureException("Host authentication failed");
            }
            serverPublicKeyBlob = signature.getPublicKeyBlob();
        } else {
            byte[] blob = signature.getPublicKeyBlob();
            boolean equals = (blob.length == serverPublicKeyBlob.length);

            for (int i=0; equals && i < blob.length; i++) {
                equals = (blob[i] == serverPublicKeyBlob[i]);
            }
            if (!equals) {
                disconnectInternal(SSH2.DISCONNECT_CONNECTION_LOST,
                                   "Server host key changed", "", false);
            }
        }
    }

    private void transportTransmitLoop() {
        isTxUp = true;
        logger.debug("starting");
        try {
            TransportPDUSSH2 pdu;
            while((pdu = (TransportPDUSSH2)txQueue.getFirst()) != null) {

                if(DEBUG_ALL_TX)
                	logger.debug("sending message of type: " +
                                 SSH2.msgTypeString(pdu.pktType)+
                                 pdu.getData()+
                                 pdu.getPayloadOffset()+
                                 pdu.getPayloadLength());

                txNumPacketsSinceKEX++;
                txNumBlocksSinceKEX += pdu.getPayloadLength()/txContext.getCipherBlockSize();

                // Note, we don't use transmitInternal since we don't want to
                // loop over the exception handler here
                //
                pdu.writeTo(tpOut, txSeqNum++, txContext, tpRand);
                activity = true;

                // Initiate rekey if needed
                if (txNumPacketsSinceKEX >= PACKETS_BEFORE_REKEY
                        || txNumBlocksSinceKEX >= blocks_before_rekey) {
                    startKeyExchange();
                }
            }
        } catch (ShortBufferException e) {
            String msg = "Internal error/bug: " + e.getMessage();
            logger.error(msg);
            disconnectInternal(SSH2.DISCONNECT_CONNECTION_LOST, msg,
                               /* !!! TODO: languageTag, from ourPrefs? */ "",
                               false);
        } catch (IOException e) {
            String msg = "I/O error: " + e.getMessage();
            if(isTxUp) {
            	logger.error( msg);
            }
            disconnectInternal(SSH2.DISCONNECT_CONNECTION_LOST, msg,
                               /* !!! TODO: languageTag, from ourPrefs? */ "",
                               false);
        } catch (CompressionException e) {
            String msg = "Internal error/bug: " + e.getMessage();
            logger.error( msg);
            disconnectInternal(SSH2.DISCONNECT_COMPRESSION_ERROR, msg,
                               /* !!! TODO: languageTag, from ourPrefs? */ "",
                               false);
        } catch (ExceptionSSH2 e) {
            String msg = "Key reexchange failed: " + e.getMessage();
            logger.error( msg);
            disconnectInternal(SSH2.DISCONNECT_COMPRESSION_ERROR, msg,
                               /* !!! TODO: languageTag, from ourPrefs? */ "",
                               false);
        } finally {
            shutdownTx();
            kexComplete(false);
            authTerminate();
        }
        logger.debug(
                    "stopping");
    }

    private void transportReceiveLoop() {
        isRxUp = true;
        logger.debug("starting");

        try {

            while(isRxUp) {
                processRxPacket(receiveInternal());
            }
        } catch (ShortBufferException e) {
            String msg = "Internal error/bug: " + e.getMessage();
            disconnectInternal(SSH2.DISCONNECT_CONNECTION_LOST, msg,
                               /* !!! TODO: languageTag, from ourPrefs? */ "",
                               false);
        } catch (MacCheckExceptionSSH2 e) {
            String msg = e.getMessage();
            disconnectInternal(SSH2.DISCONNECT_MAC_ERROR, msg,
                               /* !!! TODO: languageTag, from ourPrefs? */ "",
                               false);
        } catch (CompressionException e) {
            String msg = e.getMessage();
            disconnectInternal(SSH2.DISCONNECT_COMPRESSION_ERROR, msg,
                               /* !!! TODO: languageTag, from ourPrefs? */ "",
                               false);
        } catch (BTSignatureException e) {
            String msg = e.getMessage();
            disconnectInternal(SSH2.DISCONNECT_KEY_EXCHANGE_FAILED, msg,
                               /* !!! TODO: languageTag, from ourPrefs? */ "",
                               false);
        } catch (ExceptionSSH2 e) {
            if(isRxUp) {
                String msg = e.getMessage();
                if(e.getRootCause() != null) {
                    msg += " (rootcause: " + e.getRootCause() + ")";
                }
                disconnectInternal(SSH2.DISCONNECT_PROTOCOL_ERROR, msg,
                                   /* !!! TODO: languageTag, from ourPrefs? */ "",
                                   false);
            }
        } catch (IOException e) {
            if(isRxUp) {
                String msg = "I/O error: " + e.getMessage();
                disconnectInternal(SSH2.DISCONNECT_CONNECTION_LOST, msg,
                                   /* !!! TODO: languageTag, from ourPrefs? */ "",
                                   false);
            }
        } finally {
            shutdownRx();
            kexComplete(false);
            authTerminate();
        }
        logger.debug("stopping");
    }

    private void processRxPacket(TransportPDUSSH2 pdu)
    throws ShortBufferException, IOException, ExceptionSSH2 {
        rxNumPacketsSinceKEX++;
        rxNumBlocksSinceKEX += 
            pdu.getPayloadLength()/rxContext.getCipherBlockSize();

        switch(pdu.pktType) {
        case SSH2.MSG_DISCONNECT: {
                int    reason      = pdu.readInt();
                String description = pdu.readJavaString();
                String languageTag = pdu.readJavaString();
                disconnectInternal(reason, description, languageTag, true);
                break;
            }

        case SSH2.MSG_IGNORE:
            byte[] data = pdu.readString();
            eventHandler.msgIgnore(this, data);
            break;

        case SSH2.MSG_UNIMPLEMENTED:
            int rejectedSeqNum = pdu.readInt();
            eventHandler.msgUnimplemented(this, rejectedSeqNum);
            break;

        case SSH2.MSG_DEBUG: {
                boolean alwaysDisplay = pdu.readBoolean();
                String  message       = pdu.readJavaString();
                String  languageTag   = pdu.readJavaString();
                eventHandler.msgDebug(this, alwaysDisplay, message,
                                      languageTag);
                break;
            }

        case SSH2.MSG_SERVICE_REQUEST:
            break;

        case SSH2.MSG_SERVICE_ACCEPT:
            userAuth.processMessage(pdu);
            pdu = null;
            break;

        case SSH2.MSG_KEXINIT:
            processKEXINIT(pdu);
            pdu = null;
            break;

        case SSH2.MSG_NEWKEYS:
            if(!keyExchangeInProgress)
                throw new CorruptPacketException(
                    "Received MSG_NEWKEYS while not doing key exchange");
            changeKeys(false);
            kexComplete(true);
            break;

        case SSH2.FIRST_KEX_PACKET:
        case 31:
        case 32:
        case 33:
        case 34:
        case 35:
        case 36:
        case 37:
        case 38:
        case 39:
        case 40:
        case 41:
        case 42:
        case 43:
        case 44:
        case 45:
        case 46:
        case 47:
        case 48:
        case SSH2.LAST_KEX_PACKET:
            if(!keyExchangeInProgress)
                throw new CorruptPacketException(
                    "Received KEX packet while not doing key exchange");
            keyExchanger.processKEXMethodPDU(pdu);
            break;

        case SSH2.MSG_USERAUTH_SUCCESS:
            userAuth.processMessage(pdu);
            pdu = null;
            authenticated = true;
//            rxContext.authSucceeded();
//            txContext.authSucceeded();
            break;

        case SSH2.MSG_USERAUTH_REQUEST:
        case SSH2.MSG_USERAUTH_FAILURE:
        case SSH2.MSG_USERAUTH_BANNER:
        case SSH2.FIRST_USERAUTH_METHOD_PACKET:
        case 61:
        case 62:
        case 63:
        case 64:
        case 65:
        case 66:
        case 67:
        case 68:
        case 69:
        case 70:
        case 71:
        case 72:
        case 73:
        case 74:
        case 75:
        case 76:
        case 77:
        case 78:
        case SSH2.LAST_USERAUTH_METHOD_PACKET:
            userAuth.processMessage(pdu);
            pdu = null;
            break;

        case SSH2.MSG_GLOBAL_REQUEST:
        case SSH2.MSG_REQUEST_SUCCESS:
        case SSH2.MSG_REQUEST_FAILURE:
        case SSH2.MSG_CHANNEL_OPEN:
            connection.processGlobalMessage(pdu);
            pdu = null;
            break;

        case SSH2.MSG_CHANNEL_DATA:
        case SSH2.MSG_CHANNEL_EXTENDED_DATA:
        case SSH2.MSG_CHANNEL_OPEN_CONFIRMATION:
        case SSH2.MSG_CHANNEL_OPEN_FAILURE:
        case SSH2.MSG_CHANNEL_WINDOW_ADJUST:
        case SSH2.MSG_CHANNEL_EOF:
        case SSH2.MSG_CHANNEL_CLOSE:
        case SSH2.MSG_CHANNEL_REQUEST:
        case SSH2.MSG_CHANNEL_SUCCESS:
        case SSH2.MSG_CHANNEL_FAILURE:
            connection.processChannelMessage(pdu);
            pdu = null;
            break;

        default:
            if (handleExtensionRxPacket(pdu)) {
                break;
            }
            logger.warn("received packet of unknown type: " + pdu.pktType);
            TransportPDUSSH2 pduUnimp =
                TransportPDUSSH2.createOutgoingPacket(SSH2.MSG_UNIMPLEMENTED);
            pduUnimp.writeInt(rxSeqNum);
            if(keyExchangeInProgress) {
                transmitInternal(pduUnimp);
            } else {
                transmit(pduUnimp);
            }
            eventHandler.peerSentUnknownMessage(this, pdu.pktType);
            break;
        }

        if(pdu != null) {
            pdu.release();
        }

        // Initiate rekey if needed
        if (rxNumPacketsSinceKEX >= PACKETS_BEFORE_REKEY
                || rxNumBlocksSinceKEX >= blocks_before_rekey) {
            startKeyExchange();
        }
    }

    /**
     * Function which can be overridden in subclasses to handle
     * extensions to the SSH2 protocol.
     *
     * @return true if the packet was handled
     */
    protected boolean handleExtensionRxPacket(TransportPDUSSH2 pdu)
    throws ShortBufferException, IOException, ExceptionSSH2 {
        return false;
    }

    /**
     * Receives a PDU directly from the <code>InputStream</code> from the peer
     * without checking if we are connected. This method can only be used when
     * the receiver is not running.
     *
     * @return the PDU which was read
     *
     * @exception ExceptionSSH2
     * @exception ShortBufferException
     * @exception IOException
     */
    public TransportPDUSSH2 receiveInternal()
    throws ExceptionSSH2, ShortBufferException, IOException {
        TransportPDUSSH2 pdu = TransportPDUSSH2.createIncomingPacket();
        pdu.readFrom(tpIn, rxSeqNum++, rxContext);
        activity = true;

        if(DEBUG_ALL_RX)
        	logger.debug("received message of type: " +
                         SSH2.msgTypeString(pdu.pktType)+
                         pdu.getData()+
                         pdu.getPayloadOffset()+
                         pdu.getPayloadLength());
        return pdu;
    }

    private void shutdownTx() {
        if(isTxUp) {
            isTxUp = false;
            try {
                tpOut.close();
            } catch (IOException e) { /* don't care */
            }
            txQueue.disable();
            txQueue.setBlocking(false);
        }
    }

    private void shutdownRx() {
        if(isRxUp) {
            isRxUp = false;
            try {
                tpIn.close();
            } catch (IOException e) { /* don't care */
            }
        }
    }

    private synchronized void changeKeys(boolean transmitter)
    throws ExceptionSSH2 {
        try {
            String cipherName = ourPrefs.getAgreedCipher(transmitter,
                                weAreAServer);
            String macName    = ourPrefs.getAgreedMac(transmitter,
                                weAreAServer);
            String compName   = ourPrefs.getAgreedCompression(transmitter,
                                weAreAServer);
            int    cKeyLen    = PreferencesSSH2.getCipherKeyLen(cipherName);
            int    mKeyLen    = PreferencesSSH2.getMacKeyLen(macName);

            logger.info("new " +
                       (transmitter ? "transmitter" : "receiver") +
                       " context (" + cipherName + "," + macName + "," +
                       compName + ")");

            cipherName = PreferencesSSH2.ssh2ToJCECipher(cipherName);
            macName    = PreferencesSSH2.ssh2ToJCEMac(macName);

            TranceiverContext ctx =
                TransportPDUSSH2.createTranceiverContext(cipherName,
                        macName,
                        compName);
            initTranceiverContext(ctx, cKeyLen,
                                  (incompatibleHMACKeyLength ? 16 : mKeyLen),
                                  transmitter);

            if(transmitter) {
                txContext = ctx;
            } else {
                rxContext = ctx;
            }

            /*
             * Set rekey limit according to the algorithm in the
             * newmodes draft.
             */
            int bs = txContext.getCipherBlockSize();
            if (bs == 0 || rxContext.getCipherBlockSize() < bs) {
                bs = rxContext.getCipherBlockSize();
            }
            if (bs >= 16) {
                blocks_before_rekey = (long)1<<(bs*2);
            } else {
                blocks_before_rekey = 1073741824/bs;   // 1Gb/bs
            }

        } catch (Exception e) {
            throw new FatalExceptionSSH2("Error in changeKeys", e);
        }
    }

    private void initTranceiverContext(TranceiverContext context, int ckLen,
                                       int mkLen, boolean transmitter)
    throws ExceptionSSH2 {
        byte[] iv, cKey, mKey;
        char[] ids;
        if(weAreAServer ^ transmitter) {
            ids = new char[] { 'A', 'C', 'E' };
        } else {
            ids = new char[] { 'B', 'D', 'F' };
        }

        iv   = deriveKey(ids[0], context.getCipherBlockSize());
        cKey = deriveKey(ids[1], ckLen);
        mKey = deriveKey(ids[2], mkLen);

        int compressionLevel = 6;
        try {
            compressionLevel =
                Integer.parseInt(ourPrefs.getPreference(PreferencesSSH2.
                                                        COMP_LEVEL));
        } catch (Exception e) {
            compressionLevel = 6;
        }

        context.init(cKey, iv, mKey, compressionLevel, transmitter);
//        if (authenticated) {
//            context.authSucceeded();
//        }
    }

    byte[] deriveKey(char id, int len) {
        byte[] key = new byte[len];

        byte[] sharedSecret_K = keyExchanger.getSharedSecret_K();
        byte[] exchangeHash_H = keyExchanger.getExchangeHash_H();

        if(sessionId == null) {
            sessionId = new byte[exchangeHash_H.length];
            System.arraycopy(exchangeHash_H, 0, sessionId, 0,
                             sessionId.length);
        }

        MessageDigest sha1 = keyExchanger.getExchangeHashAlgorithm();

        sha1.update(sharedSecret_K);
        sha1.update(exchangeHash_H);
        sha1.update(new byte[] { (byte)id });
        sha1.update(sessionId);
        byte[] material = sha1.digest();

        int curLen = material.length;
        System.arraycopy(material, 0, key, 0, (curLen < len ? curLen : len));

        while(curLen < len) {
            sha1.reset();
            sha1.update(sharedSecret_K);
            sha1.update(exchangeHash_H);
            sha1.update(key, 0, curLen);
            material = sha1.digest();
            if(len - curLen > material.length)
                System.arraycopy(material, 0, key, curLen, material.length);
            else
                System.arraycopy(material, 0, key, curLen, len - curLen);
            curLen += material.length;
        }

        logger.debug("key id " + id+" "+ key);

        return key;
    }

    /**
     * Called by the KeepAliveThread thread (if launched) to report when
     * the connection has been inactive for a time.
     *
     * @param duration How long (in seconds) the connection has been inactive
     */
    protected void reportInactivity(int duration) {}

}
