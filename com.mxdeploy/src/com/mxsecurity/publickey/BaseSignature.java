package com.mxsecurity.publickey;

import com.mxsecurity.jca.InvalidAlgorithmParameterException;
import com.mxsecurity.jca.InvalidKeyException;
import com.mxsecurity.jca.InvalidParameterException;
import com.mxsecurity.jca.MessageDigest;
import com.mxsecurity.jca.NoSuchAlgorithmException;
import com.mxsecurity.jca.PrivateKey;
import com.mxsecurity.jca.PublicKey;
import com.mxsecurity.jca.SecureRandom;
import com.mxsecurity.jca.SignatureException;
import com.mxsecurity.jca.SignatureSpi;
import com.mxsecurity.jca.spec.AlgorithmParameterSpec;



public abstract class BaseSignature extends SignatureSpi {
    protected MessageDigest digest;
    protected PublicKey     publicKey;
    protected PrivateKey    privateKey;

    protected BaseSignature(String digAlg) {
        super();
        try {
            this.digest = MessageDigest.getInstance(digAlg);
        } catch (NoSuchAlgorithmException e) {
            throw new Error("Error in BaseSignature, no " + digAlg + " found");
        }
    }

    protected final void engineInitVerify(PublicKey publicKey)
    throws InvalidKeyException {
        this.publicKey = publicKey;
        initVerify();
    }

    protected final void engineInitSign(PrivateKey privateKey)
    throws InvalidKeyException {
        engineInitSign(privateKey, null);
    }

    protected final void engineInitSign(PrivateKey privateKey,
                                        SecureRandom random)
    throws InvalidKeyException {
        this.privateKey = privateKey;
        this.appRandom  = random;
        initSign();
    }

    protected void engineUpdate(byte b)	throws SignatureException {
        digest.update(b);
    }

    protected void engineUpdate(byte[] b, int off, int len)
    throws SignatureException {
        digest.update(b, off, len);
    }

    protected byte[] engineSign() throws SignatureException {
        return sign(digest.digest());
    }

    protected int engineSign(byte[] outbuf, int offset, int len)
    throws SignatureException {
        byte[] signature = sign(digest.digest());
        if(signature.length < len) {
            len = signature.length;
        }
        System.arraycopy(signature, 0, outbuf, offset, len);
        return len;
    }

    protected boolean engineVerify(byte[] sigBytes) throws SignatureException {
        return verify(sigBytes, digest.digest());
    }

    protected void engineSetParameter(String param, Object value)
    throws InvalidParameterException {
        throw new InvalidParameterException("Param not supported: " + param);
    }

    protected void engineSetParameter(AlgorithmParameterSpec params)
    throws InvalidAlgorithmParameterException {
        throw new InvalidAlgorithmParameterException("Param not supported: " +
                params);
    }

    protected Object engineGetParameter(String param)
    throws InvalidParameterException {
        throw new InvalidParameterException("Param not supported: " + param);
    }

    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    protected final SecureRandom getRandom() {
        if(appRandom == null) {
            appRandom = new SecureRandom();
        }
        return appRandom;
    }

    protected abstract void initSign() throws InvalidKeyException;
    protected abstract void initVerify() throws InvalidKeyException;
    protected abstract byte[] sign(byte[] data) throws SignatureException;
    protected abstract boolean verify(byte[] signature, byte[] data)
    throws SignatureException;

}
