package com.mxterminal.ssh2.sha;


import com.mxsecurity.jca.MessageDigest;
import com.mxterminal.ssh2.exception.ExceptionSSH2;
import com.mxterminal.ssh2.exception.KEXFailedExceptionSSH2;

/**
 * Implements diffie hellman key exchange with group negotiation. This
 * algorithm is known as 'diffie-hellman-group-exchange-sha256'
 */
public class KEXDHGroupXSHA256 extends KEXDHGroupXSHA1 {

    protected MessageDigest createHash() throws ExceptionSSH2 {
        try {
            return MessageDigest.getInstance("SHA256");
        } catch (Exception e) {
	    e.printStackTrace();
            throw new KEXFailedExceptionSSH2("SHA256 not implemented", e);
        }
    }
}
