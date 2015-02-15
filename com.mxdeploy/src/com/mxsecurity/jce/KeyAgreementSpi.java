
package com.mxsecurity.jce;


import com.mxsecurity.jca.InvalidAlgorithmParameterException;
import com.mxsecurity.jca.InvalidKeyException;
import com.mxsecurity.jca.Key;
import com.mxsecurity.jca.NoSuchAlgorithmException;
import com.mxsecurity.jca.SecureRandom;
import com.mxsecurity.jca.spec.AlgorithmParameterSpec;



public abstract class KeyAgreementSpi {

    public KeyAgreementSpi() {}

    protected abstract void engineInit(Key key, SecureRandom random)
    throws InvalidKeyException;

    protected abstract void engineInit(Key key, AlgorithmParameterSpec params,
                                       SecureRandom random)
    throws InvalidKeyException, InvalidAlgorithmParameterException;

    protected abstract Key engineDoPhase(Key key, boolean lastPhase)
    throws InvalidKeyException, IllegalStateException;

    protected abstract byte[] engineGenerateSecret()
    throws IllegalStateException;

    protected abstract int engineGenerateSecret(byte[] sharedSecret, int offset)
    throws IllegalStateException, ShortBufferException;

    protected abstract SecretKey engineGenerateSecret(String algorithm)
    throws IllegalStateException, NoSuchAlgorithmException,
                InvalidKeyException;

}
