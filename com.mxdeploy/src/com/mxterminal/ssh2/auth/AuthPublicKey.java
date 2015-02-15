package com.mxterminal.ssh2.auth;

import org.apache.log4j.Logger;

import com.mxterminal.ssh2.PKISignerSSH2;
import com.mxterminal.ssh2.SSH2;
import com.mxterminal.ssh2.TransportPDUSSH2;
import com.mxterminal.ssh2.exception.ExceptionSSH2;
import com.mxterminal.ssh2.exception.FatalExceptionSSH2;
import com.mxterminal.ssh2.exception.UserCancelException;
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
public class AuthPublicKey implements AuthModule {
	static Logger logger = Logger.getLogger(AuthPublicKey.class);
    private PKISignerSSH2 signer;
    private boolean       test;

    public final static String STANDARD_NAME = "publickey";

    public AuthPublicKey(PKISignerSSH2 signer) {
        this(signer, true);
    }

    public AuthPublicKey(PKISignerSSH2 signer, boolean test) {
        this.signer = signer;
        this.test   = test;
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
        switch(pdu.getType()) {
        case SSH2.MSG_USERAUTH_PK_OK:
            try {
                pdu = createRequest(userAuth, false);
            } catch (BTSignatureException e) {
                // !!! TODO how do we want to handle this?
                pdu = null;
                logger.warn(
                        "createRequest: " + e.getMessage());
                throw new UserCancelException(e.getMessage());
            }
            break;

        default:
        	logger.warn(
                    "received unexpected packet of type: " + pdu.getType());
            pdu = null;
            throw new FatalExceptionSSH2("SSH2AuthPublicKey: got unexpected " +
                                         "packet of type: " + pdu.getType());
        }
        return pdu;
    }

    public TransportPDUSSH2 startAuthentication(UserAuth userAuth)
    throws BTSignatureException {
        return createRequest(userAuth, test);
    }

    private TransportPDUSSH2 createRequest(UserAuth userAuth, boolean test)
    throws BTSignatureException {
        TransportPDUSSH2 pdu     = userAuth.createUserAuthRequest(STANDARD_NAME);
        PKISignerSSH2    signer  = getSigner();
        byte[]           keyBlob = signer.getPublicKeyBlob();

        pdu.writeBoolean(!test);
        pdu.writeString(signer.getAlgorithmName());
        pdu.writeString(keyBlob);

        if(!test) {
            signPDU(userAuth, pdu, signer, keyBlob);
        }

        return pdu;
    }

    private void signPDU(UserAuth userAuth, TransportPDUSSH2 targetPDU,
                         PKISignerSSH2 signer, byte[] keyBlob)
    throws BTSignatureException {
        TransportPDUSSH2 sigPDU = targetPDU;

        if(userAuth.getTransport().incompatiblePublicKeyAuth) {
            sigPDU =
                TransportPDUSSH2.createOutgoingPacket(SSH2.MSG_USERAUTH_REQUEST);
            sigPDU.writeString(userAuth.user);
            sigPDU.writeString("ssh-userauth");
            sigPDU.writeString(STANDARD_NAME);
            sigPDU.writeBoolean(true);
            sigPDU.writeString(signer.getAlgorithmName());
            sigPDU.writeString(keyBlob);
        }

        byte[] sessionId = userAuth.getTransport().getSessionId();

        int    payloadLength = sigPDU.wPos - sigPDU.getPayloadOffset();
        byte[] signData      = new byte[payloadLength + sessionId.length];

        System.arraycopy(sessionId, 0, signData, 0, sessionId.length);
        System.arraycopy(sigPDU.data, sigPDU.getPayloadOffset(),
                         signData, sessionId.length, payloadLength);

        signer.setIncompatibility(userAuth.getTransport());

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
