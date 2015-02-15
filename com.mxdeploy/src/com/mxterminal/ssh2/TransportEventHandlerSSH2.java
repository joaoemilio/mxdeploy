package com.mxterminal.ssh2;

import com.mxterminal.ssh2.key.BTSignature;

public interface TransportEventHandlerSSH2 {

    /**
     * Called when an info text is received in the version negotiation stage (as
     * defined in the transport protocol spec.).
     *
     * @param tp   the transport layer
     * @param text the info text received
     */
    public void gotConnectInfoText(TransportSSH2 tp, String text);

    /**
     * Called in the version negotiation stage when the peer version is received
     * (as defined in the transport protocol spec.).
     *
     * @param tp             the transport layer
     * @param versionString  the version string of peer
     * @param major          the major protocol version of peer
     * @param minor          the minor protocol version of peer
     * @param packageVersion the package version of peer
     */
    public void gotPeerVersion(TransportSSH2 tp, String versionString,
                               int major, int minor, String packageVersion);

    /**
     * Called when key exchange starts. That is when a KEXINIT message
     * is sent to the peer.
     *
     * @param tp the transport layer
     */
    public void kexStart(TransportSSH2 tp);

    /**
     * Called when key exchange have agreed on algorithms. That is when
     * a KEXINIT message has been received and processed).
     *
     * @param tp        the transport layer
     * @param ourPrefs  our preferences
     * @param peerPrefs peer's preferences
     */
    public void kexAgreed(TransportSSH2 tp,
                          PreferencesSSH2 ourPrefs, PreferencesSSH2 peerPrefs);

    /**
     * Called to authenticate server's host key.
     *
     * @param tp            the transport layer
     * @param serverHostKey server's host key
     *
     * @return a boolean indicating if the server could be authenticated or not.
     */
    public boolean kexAuthenticateHost(TransportSSH2 tp,
                                       BTSignature serverHostKey);

    /**
     * Called when key exchange has been successfully completed. That
     * is when new keys and algorithms are now active.
     *
     * @param tp the transport layer
     */
    public void kexComplete(TransportSSH2 tp);

    /**
     * Called when a DEBUG message is received.
     *
     * @param tp            the transport layer
     * @param alwaysDisplay boolean flag indicating whether this message should
     * always be displayed or not.
     * @param message       debug message contained in the packet
     * @param languageTag   language tag
     */
    public void msgDebug(TransportSSH2 tp, boolean alwaysDisplay,
                         String message, String languageTag);

    /**
     * Called when an IGNORE message is received.
     *
     * @param tp   the transport layer
     * @param data byte array of data contained in packet
     */
    public void msgIgnore(TransportSSH2 tp, byte[] data);

    /**
     * Called when an UNIMPLEMENTED message is received.
     *
     * @param tp             the transport layer
     * @param rejectedSeqNum sequence number of packet which peer didn't
     * understnad
     */
    public void msgUnimplemented(TransportSSH2 tp, int rejectedSeqNum);

    /**
     * Called when an unimplemented message is received, and an UNIMPLEMENTED
     * message is sent to peer.
     *
     * @param tp   the transport layer
     * @param pktType type of message which we didn't understand
     */
    public void peerSentUnknownMessage(TransportSSH2 tp, int pktType);

    /**
     * Called when transport layer is disconnected gracefully by our side of
     * connection.
     *
     * @param tp          the transport layer
     * @param description textual description for reason of disconnect 
     * @param languageTag language tag
     */
    public void normalDisconnect(TransportSSH2 tp, String description,
                                 String languageTag);


    /**
     * Called when transport layer is disconnected for the given fatal reason by
     * our side of the connection. See the class <code>SSH2</code> for reason
     * codes.
     *
     * @param tp          the transport layer
     * @param reason      the reason code
     * @param description textual description for reason of disconnect 
     * @param languageTag language tag
     *
     * @see SSH2
     */
    public void fatalDisconnect(TransportSSH2 tp, int reason,
                                String description, String languageTag);

    /**
     * Called when peer disconnects the transport layer for some given
     * reason. See the class <code>SSH2</code> for reason codes.
     *
     * @param tp          the transport layer
     * @param reason      the reason code
     * @param description textual description for reason of disconnect 
     * @param languageTag language tag
     *
     * @see SSH2
     */
    public void peerDisconnect(TransportSSH2 tp, int reason,
                               String description, String languageTag);

}
