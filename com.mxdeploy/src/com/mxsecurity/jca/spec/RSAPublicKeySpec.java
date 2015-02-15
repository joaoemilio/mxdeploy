
package com.mxsecurity.jca.spec;

import java.math.BigInteger;

public class RSAPublicKeySpec extends RSAKeyImpl implements KeySpec {

    protected BigInteger publicExponent;

    public RSAPublicKeySpec(BigInteger modulus, BigInteger publicExponent) {
        super(modulus);
        this.publicExponent = publicExponent;
    }

    public BigInteger getPublicExponent() {
        return publicExponent;
    }

}
