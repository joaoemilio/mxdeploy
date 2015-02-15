package com.mxsecurity.publickey;

import java.math.BigInteger;

public class RSAPublicKey extends RSAKey
    implements com.mxsecurity.jca.interfaces.RSAPublicKey {

    protected BigInteger publicExponent;

    public RSAPublicKey(BigInteger modulus, BigInteger publicExponent) {
        super(modulus);
        this.publicExponent = publicExponent;
    }

    public BigInteger getPublicExponent() {
        return publicExponent;
    }

}
