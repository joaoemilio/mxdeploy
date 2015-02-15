package com.mxterminal.ssh2.key;

import java.util.Hashtable;

import com.mxsecurity.jca.MessageDigest;
import com.mxterminal.ssh2.TransportPDUSSH2;
import com.mxterminal.ssh2.TransportSSH2;
import com.mxterminal.ssh2.exception.ExceptionSSH2;
import com.mxterminal.ssh2.exception.KEXFailedExceptionSSH2;

/**
 * Base class for implementing ssh key exchange algorithms.
 */
public abstract class KeyExchanger {

    private static Hashtable algorithms;

    static {
        algorithms = new Hashtable();

        algorithms.put("diffie-hellman-group1-sha1",
                       "com.mxterminal.ssh2.sha.KEXDHGroup1SHA1");
        algorithms.put("diffie-hellman-group14-sha1",
                       "com.mxterminal.ssh2.sha.KEXDHGroup14SHA1");
        algorithms.put("diffie-hellman-group-exchange-sha1",
                       "com.mxterminal.ssh2.sha.KEXDHGroupXSHA1");
        algorithms.put("diffie-hellman-group-exchange-sha256",
                       "com.mxterminal.ssh2.sha.KEXDHGroupXSHA256");
    }

    protected KeyExchanger() {}

    public static KeyExchanger getInstance(String algorithm)
    throws KEXFailedExceptionSSH2 {
        String           alg = (String)algorithms.get(algorithm);
        KeyExchanger kex = null;
        if(alg != null) {
            try {
                Class c = Class.forName(alg);
                kex = (KeyExchanger)c.newInstance();
            } catch (Throwable t) {
                kex = null;
            }
        }
        if(kex == null) {
            throw new KEXFailedExceptionSSH2("Unknown kex algorithm: " +
                                             algorithm);
        }
        return kex;
    }

    public abstract void init(TransportSSH2 transport) throws ExceptionSSH2;

    public abstract void processKEXMethodPDU(TransportPDUSSH2 pdu)
    throws ExceptionSSH2;

    public abstract MessageDigest getExchangeHashAlgorithm();

    public abstract byte[] getSharedSecret_K();

    public abstract byte[] getExchangeHash_H();

    public abstract String getHostKeyAlgorithms();

}
