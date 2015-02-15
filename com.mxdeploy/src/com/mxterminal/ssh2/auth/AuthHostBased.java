package com.mxterminal.ssh2.auth;

import org.apache.log4j.Logger;

import com.mxterminal.ssh2.PKISignerSSH2;
import com.mxterminal.ssh2.TransportPDUSSH2;
import com.mxterminal.ssh2.exception.ExceptionSSH2;
import com.mxterminal.ssh2.exception.FatalExceptionSSH2;
import com.mxterminal.ssh2.key.BTSignatureException;

/**
 * This class implements a module for publickey authentication as defined in the
 * userauth protocol spec. It uses the interface <code>SSH2PKISigner</code> to
 * access an abstract PKI signing mechanism (e.g. implemented with simple file
 * based public/private keys without certificates).
 *
 * @see AuthModule
 * @see PKISignerSSH2
 */
public class AuthHostBased implements AuthModule {
	static Logger logger = Logger.getLogger(AuthHostBased.class);
    private PKISignerSSH2 signer;

    public final static String STANDARD_NAME = "hostbased";

    public AuthHostBased(PKISignerSSH2 signer) {
        this.signer = signer;
    }

    protected PKISignerSSH2 getSigner() {
        return signer;
    }

    public String getStandardName() {
        return STANDARD_NAME;
    }

    public TransportPDUSSH2 processMethodMessage(UserAuth userAuth,
                                                 TransportPDUSSH2 pdu)
        throws ExceptionSSH2 {
//        switch(pdu.getType()) {                
//            default:
    	        logger.warn("received unexpected packet of type: " + pdu.getType());
                pdu = null;
                throw new FatalExceptionSSH2("SSH2AuthHostBased: got unexpected " +
                                             "packet of type: " + pdu.getType());
//        }
//        return pdu;
    }

    public TransportPDUSSH2 startAuthentication(UserAuth userAuth)
        throws BTSignatureException {
        TransportPDUSSH2 pdu     = userAuth.createUserAuthRequest(STANDARD_NAME);
        PKISignerSSH2    signer  = getSigner();
        byte[]           keyBlob = signer.getPublicKeyBlob();

        pdu.writeString(signer.getAlgorithmName());
        pdu.writeString(keyBlob);
        pdu.writeString(userAuth.getTransport().getLocalHostName());
        pdu.writeString(System.getProperty("user.name", ""));
        
        signPDU(userAuth, pdu, signer, keyBlob);

        return pdu;
    }

    private void signPDU(UserAuth userAuth, TransportPDUSSH2 targetPDU,
                         PKISignerSSH2 signer, byte[] keyBlob)
        throws BTSignatureException {
        TransportPDUSSH2 sigPDU = targetPDU;

        byte[] sessionId = userAuth.getTransport().getSessionId();

        int    payloadLength = sigPDU.wPos - sigPDU.getPayloadOffset();
        byte[] signData      = new byte[payloadLength + sessionId.length];

        System.arraycopy(sessionId, 0, signData, 0, sessionId.length);
        System.arraycopy(sigPDU.data, sigPDU.getPayloadOffset(),
                         signData, sessionId.length, payloadLength);

        byte[] sig = signer.sign(signData);
        targetPDU.writeString(sig);
    }

    public void clearSensitiveData() {
        signer.clearSensitiveData();
    }

    public boolean retryPointless() {
        return true;
    }
}
