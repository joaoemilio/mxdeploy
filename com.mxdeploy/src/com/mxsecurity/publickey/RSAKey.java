package com.mxsecurity.publickey;

import java.math.BigInteger;

import com.mxsecurity.jca.Key;

public abstract class RSAKey
    implements com.mxsecurity.jca.interfaces.RSAKey, Key {

    protected BigInteger modulus;

    protected RSAKey(BigInteger modulus) {
        this.modulus = modulus;
    }

    public BigInteger getModulus() {
        return modulus;
    }

    public String getAlgorithm() {
        return "RSA";
    }

    public byte[] getEncoded() {
        return null;
    }

    public String getFormat() {
        return null;
    }

}
