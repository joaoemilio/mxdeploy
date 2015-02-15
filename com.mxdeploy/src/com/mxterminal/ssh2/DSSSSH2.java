package com.mxterminal.ssh2;

import java.math.BigInteger;

import com.mxsecurity.jca.KeyFactory;
import com.mxsecurity.jca.PublicKey;
import com.mxsecurity.jca.interfaces.DSAParams;
import com.mxsecurity.jca.interfaces.DSAPublicKey;
import com.mxsecurity.jca.spec.DSAPublicKeySpec;
import com.mxterminal.ssh2.exception.ExceptionSSH2;
import com.mxterminal.ssh2.exception.FatalExceptionSSH2;

/**
 * Implements "ssh-dss" signatures according to the ssh standard.
 */
public final class DSSSSH2 extends SimpleSignatureSSH2 {
    public final static String SSH2_KEY_FORMAT = "ssh-dss";

    /**
     * Constructor.
     */
    public DSSSSH2() {
        super("SHA1withRawDSA", SSH2_KEY_FORMAT);
    }

    public byte[] encodePublicKey(PublicKey publicKey) throws ExceptionSSH2 {
        DataBufferSSH2 buf = new DataBufferSSH2(8192);

        if(!(publicKey instanceof DSAPublicKey)) {
            throw new FatalExceptionSSH2("SSH2DSS, invalid public key type: " +
                                         publicKey);
        }

        DSAPublicKey dsaPubKey = (DSAPublicKey)publicKey;
        DSAParams    dsaParams = dsaPubKey.getParams();

        buf.writeString(SSH2_KEY_FORMAT);
        buf.writeBigInt(dsaParams.getP());
        buf.writeBigInt(dsaParams.getQ());
        buf.writeBigInt(dsaParams.getG());
        buf.writeBigInt(dsaPubKey.getY());

        return buf.readRestRaw();
    }

    public PublicKey decodePublicKey(byte[] pubKeyBlob) throws ExceptionSSH2 {
        BigInteger p, q, g, y;
        DataBufferSSH2 buf = new DataBufferSSH2(pubKeyBlob.length);

        buf.writeRaw(pubKeyBlob);

        String type = buf.readJavaString();
        if(!type.equals(SSH2_KEY_FORMAT)) {
            throw new FatalExceptionSSH2("SSH2DSS, keyblob type mismatch, got '"
                                         + type + ", (execpted + '" +
                                         SSH2_KEY_FORMAT + "')");
        }

        p = buf.readBigInt();
        q = buf.readBigInt();
        g = buf.readBigInt();
        y = buf.readBigInt();

        try {
            KeyFactory       dsaKeyFact = KeyFactory.getInstance("DSA");
            DSAPublicKeySpec dsaPubSpec = new DSAPublicKeySpec(y, p, q, g);

            return dsaKeyFact.generatePublic(dsaPubSpec);

        } catch (Exception e) {
            throw new FatalExceptionSSH2("SSH2DSS, error decoding public key blob: " +
                                         e);
        }
    }

}
