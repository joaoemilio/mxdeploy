package com.mxterminal.ssh2;

import com.mxsecurity.jca.PublicKey;
import com.mxsecurity.jca.interfaces.DSAPublicKey;
import com.mxsecurity.jca.interfaces.RSAPublicKey;
import com.mxterminal.ssh2.key.BTSignature;
import com.mxterminal.ssh2.key.BTSignatureException;
import com.mxterminal.ssh2.key.KeyFingerprint;

/**
 * This class is an adapter for the interface
 * <code>SSH2TransportEventHandler</code>.
 *
 * @see TransportEventHandlerSSH2
 */
public class HostKeyVerifierSSH2 extends TransportEventAdapterSSH2 {

    protected String    fingerprint;
    protected PublicKey publickey;

    /**
     * Create an instance which will verify that the hostkey matches
     * the given public key.
     *
     * @param publickey The public key to verify against.
     */
    public HostKeyVerifierSSH2(PublicKey publickey) {
        this.publickey = publickey;
    }

    /**
     * Create an instance which will verify that the hostkey matches
     * a public key with the given fingerprint.
     *
     * @param fingerprint The fingerprint which should match the public key.
     */
    public HostKeyVerifierSSH2(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    /**
     * Perform the authenticate host stage of key exchange.
     *
     * @param tp Indentifies the <code>SSH2Transport</code> object
     * handling the connection.
     * @param serverHostKey Signature object which holds the server keys.
     *
     * @return True if the keys match.
     */
    public boolean kexAuthenticateHost(TransportSSH2 tp,
                                       BTSignature serverHostKey) {
        boolean authenticated = false;
        if(publickey != null) {
            try {
                authenticated = comparePublicKeys(publickey,
                                                  serverHostKey.getPublicKey());
            } catch (BTSignatureException e) {
                authenticated = false;
            }
        } else if(fingerprint != null) {
            authenticated = compareFingerprints(fingerprint, serverHostKey);
        }
        return authenticated;
    }

    /**
     * Static utility function which can be used to compare a server key
     * against a fingerprint.
     *
     * @param fingerprint The fingerprint to check.
     * @param serverHostKey Signature object which holds the server keys.
     *
     * @return True if the server key generates an identical
     *         fingerprint as the one we are comparing against.
     */
    public static boolean compareFingerprints(String fingerprint,
            BTSignature serverHostKey) {
        byte[] blob  = null;
        try {
            blob = serverHostKey.getPublicKeyBlob();
        } catch (BTSignatureException e) {
            return false;
        }
        String fpMD5Hex = KeyFingerprint.md5Hex(blob);
        String fpBubble = KeyFingerprint.bubbleBabble(blob);
        if(fpMD5Hex.equalsIgnoreCase(fingerprint) ||
                fpBubble.equalsIgnoreCase(fingerprint)) {
            return true;
        }
        return false;
    }

    /**
     * Static utility functions which can compare two public keys.
     *
     * @param p1 Public key to compare.
     * @param p2 Public key to compare.
     *
     * @return True if they are identical.
     */
    public static boolean comparePublicKeys(PublicKey p1, PublicKey p2) {
        if((p1 instanceof DSAPublicKey) &&
                (p2 instanceof DSAPublicKey)) {
            DSAPublicKey dsa1 = (DSAPublicKey)p1;
            DSAPublicKey dsa2 = (DSAPublicKey)p2;
            if(dsa1.getY().equals(dsa2.getY()) &&
                    dsa1.getParams().getG().equals(dsa2.getParams().getG())
                    &&
                    dsa1.getParams().getP().equals(dsa2.getParams().getP())
                    &&
                    dsa1.getParams().getQ().equals(dsa2.getParams().getQ()))
                return true;
        } else if((p1 instanceof RSAPublicKey) &&
                  (p2 instanceof RSAPublicKey)) {
            RSAPublicKey rsa1 = (RSAPublicKey)p1;
            RSAPublicKey rsa2 = (RSAPublicKey)p2;
            if(rsa1.getPublicExponent().equals(rsa2.getPublicExponent()) &&
                    rsa1.getModulus().equals(rsa2.getModulus()))
                return true;
        }
        return false;
    }


}
