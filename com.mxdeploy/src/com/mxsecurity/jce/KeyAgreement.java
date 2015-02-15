
package com.mxsecurity.jce;


import com.mxsecurity.jca.InvalidAlgorithmParameterException;
import com.mxsecurity.jca.InvalidKeyException;
import com.mxsecurity.jca.Key;
import com.mxsecurity.jca.NoSuchAlgorithmException;
import com.mxsecurity.jca.NoSuchProviderException;
import com.mxsecurity.jca.Provider;
import com.mxsecurity.jca.ProviderLookup;
import com.mxsecurity.jca.SecureRandom;
import com.mxsecurity.jca.spec.AlgorithmParameterSpec;


public class KeyAgreement {

    protected KeyAgreementSpi keyAgreementSpi;
    Provider  provider;
    String    algorithm;

    protected KeyAgreement(KeyAgreementSpi keyAgreeSpi, Provider provider,
                           String algorithm) {
        this.keyAgreementSpi = keyAgreeSpi;
        this.provider        = provider;
        this.algorithm       = algorithm;
    }

    public final String getAlgorithm() {
        return algorithm;
    }

    public static final KeyAgreement getInstance(String algorithm)
    throws NoSuchAlgorithmException {
        try {
            String provider =
                ProviderLookup.findImplementingProvider("KeyAgreement",
                                                        algorithm);
            return getInstance(algorithm, provider);
        } catch (NoSuchProviderException e) {
            throw new Error("Error in Signature: " + e);
        }

    }

    public static final KeyAgreement getInstance(String algorithm,
            String provider)
    throws NoSuchAlgorithmException, NoSuchProviderException {
        ProviderLookup pl = ProviderLookup.getImplementation("KeyAgreement",
                            algorithm,
                            provider);
        return new KeyAgreement((KeyAgreementSpi)pl.getImpl(),
                                pl.getProvider(), algorithm);
    }

    public final Provider getProvider() {
        return provider;
    }

    public final void init(Key key) throws InvalidKeyException {
        init(key, (SecureRandom)null);
    }

    public final void init(Key key, SecureRandom random)
    throws InvalidKeyException {
        keyAgreementSpi.engineInit(key, random);
    }

    public final void init(Key key, AlgorithmParameterSpec params)
    throws InvalidKeyException, InvalidAlgorithmParameterException {
        init(key, params, null);
    }

    public final void init(Key key, AlgorithmParameterSpec params,
                           SecureRandom random)
    throws InvalidKeyException, InvalidAlgorithmParameterException {
        keyAgreementSpi.engineInit(key, params, random);
    }

    public final Key doPhase(Key key, boolean lastPhase)
    throws InvalidKeyException, IllegalStateException {
        return keyAgreementSpi.engineDoPhase(key, lastPhase);
    }

    public final byte[] generateSecret() throws IllegalStateException {
        return keyAgreementSpi.engineGenerateSecret();
    }

    public final int generateSecret(byte[] sharedSecret, int offset)
    throws IllegalStateException, ShortBufferException {
        return keyAgreementSpi.engineGenerateSecret(sharedSecret, offset);
    }

    public final SecretKey generateSecret(String algorithm)
    throws IllegalStateException, NoSuchAlgorithmException,
        InvalidKeyException {
        return keyAgreementSpi.engineGenerateSecret(algorithm);
    }

}
