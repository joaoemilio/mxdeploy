package com.mxterminal.ssh2;

import com.mxterminal.ssh2.key.BTSignature;


/**
 * This class is an adapter for the interface
 * <code>SSH2TransportEventHandler</code>.
 *
 * @see TransportEventHandlerSSH2
 */
public class TransportEventAdapterSSH2 implements TransportEventHandlerSSH2 {
    public void gotConnectInfoText(TransportSSH2 tp, String text) {}
    public void gotPeerVersion(TransportSSH2 tp, String versionString,
                               int major, int minor, String packageVersion) {}

    public void kexStart(TransportSSH2 tp) {}
    public void kexAgreed(TransportSSH2 tp,
                          PreferencesSSH2 ourPrefs, PreferencesSSH2 peerPrefs) {}
    public boolean kexAuthenticateHost(TransportSSH2 tp,
                                       BTSignature serverHostKey) {
        return true;
    }
    public void kexComplete(TransportSSH2 tp) {}

    public void msgDebug(TransportSSH2 tp, boolean alwaysDisplay, String message,
                         String languageTag) {}
    public void msgIgnore(TransportSSH2 tp, byte[] data) {}
    public void msgUnimplemented(TransportSSH2 tp, int rejectedSeqNum) {}

    public void peerSentUnknownMessage(TransportSSH2 tp, int pktType) {}

    public void normalDisconnect(TransportSSH2 tp, String description,
                                 String languageTag) {}
    public void fatalDisconnect(TransportSSH2 tp, int reason,
                                String description, String languageTag) {}
    public void peerDisconnect(TransportSSH2 tp, int reason,
                               String description, String languageTag) {}

}
