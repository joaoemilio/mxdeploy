package com.mxsecurity.jca;

import com.mxsecurity.jca.spec.AlgorithmParameterSpec;



public abstract class SignatureSpi {

    protected SecureRandom appRandom;

    public SignatureSpi() {}

    protected abstract void engineInitVerify(PublicKey publicKey)
    throws InvalidKeyException;

    protected abstract void engineInitSign(PrivateKey privateKey)
    throws InvalidKeyException;

    protected abstract void engineInitSign(PrivateKey privateKey,
                                           SecureRandom random)
    throws InvalidKeyException;

    protected abstract void engineUpdate(byte b)
    throws SignatureException;

    protected abstract void engineUpdate(byte[] b,
                                         int off,
                                         int len)
    throws SignatureException;

    protected abstract byte[] engineSign()
    throws SignatureException;

    protected int engineSign(byte[] outbuf, int offset, int len)
    throws SignatureException {
        byte[] signature = engineSign();
        if(signature.length < len) {
            len = signature.length;
        }
        System.arraycopy(signature, 0, outbuf, offset, len);
        return len;
    }

    protected abstract boolean engineVerify(byte[] sigBytes)
    throws SignatureException;

    protected abstract void engineSetParameter(String param,
            Object value)
    throws InvalidParameterException;

    protected void engineSetParameter(AlgorithmParameterSpec params)
    throws InvalidAlgorithmParameterException {
        throw new InvalidAlgorithmParameterException("Not supported: " +
                params);
    }

    protected abstract Object engineGetParameter(String param)
    throws InvalidParameterException;

    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

}
