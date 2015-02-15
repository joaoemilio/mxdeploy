package com.mxterminal.ssh2;

import java.math.BigInteger;

import com.mxsecurity.jca.KeyFactory;
import com.mxsecurity.jca.PublicKey;
import com.mxsecurity.jca.interfaces.RSAPublicKey;
import com.mxsecurity.jca.spec.RSAPublicKeySpec;
import com.mxterminal.ssh2.exception.ExceptionSSH2;
import com.mxterminal.ssh2.exception.FatalExceptionSSH2;

/**
 * Implements "ssh-rsa" signatures according to the ssh standard.
 */
public final class RSASSH2 extends SimpleSignatureSSH2 {
    public final static String SSH2_KEY_FORMAT = "ssh-rsa";

    public RSASSH2() {
        super("SHA1withRSA", SSH2_KEY_FORMAT);
    }

    /**
     * Encode the given public key according to the ssh standard.
     *
     * @param publicKey The public key to encode. Must be an instance of
     *                  <code>RSAPublicKey</code>.
     *
     * @return A byte array containing the key suitably encoded.
     */
    public byte[] encodePublicKey(PublicKey publicKey) throws ExceptionSSH2 {
        DataBufferSSH2 buf = new DataBufferSSH2(8192);

        if(!(publicKey instanceof RSAPublicKey)) {
            throw new FatalExceptionSSH2("SSH2RSA, invalid public key type: " +
                                         publicKey);
        }

        RSAPublicKey rsaPubKey = (RSAPublicKey)publicKey;

        buf.writeString(SSH2_KEY_FORMAT);
        buf.writeBigInt(rsaPubKey.getPublicExponent());
        buf.writeBigInt(rsaPubKey.getModulus());

        return buf.readRestRaw();
    }

    /**
     * Decode a public key encoded according to the ssh standard.
     *
     * @param pubKeyBlob A byte array containing a public key blob.
     *
     * @return A <code>Publickey</code> instance.
     */
    public PublicKey decodePublicKey(byte[] pubKeyBlob) throws ExceptionSSH2 {
        BigInteger e, n;
        DataBufferSSH2 buf = new DataBufferSSH2(pubKeyBlob.length);

        buf.writeRaw(pubKeyBlob);

        String type = buf.readJavaString();
        if(!type.equals(SSH2_KEY_FORMAT)) {
            throw new FatalExceptionSSH2("SSH2RSA, keyblob type mismatch, got '"
                                         + type + ", (execpted + '" +
                                         SSH2_KEY_FORMAT + "')");
        }

        e = buf.readBigInt();
        n = buf.readBigInt();

        try {
            KeyFactory       rsaKeyFact = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec rsaPubSpec = new RSAPublicKeySpec(n, e);

            return rsaKeyFact.generatePublic(rsaPubSpec);

        } catch (Exception ee) {
            throw new FatalExceptionSSH2("SSH2RSA, error decoding public key blob: " +
                                         ee);
        }
    }

}
