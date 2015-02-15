
package com.mxsecurity.jca.spec;

import java.math.BigInteger;

public class RSAPrivateKeySpec extends RSAKeyImpl implements KeySpec {

    protected BigInteger privateExponent;

    public RSAPrivateKeySpec(BigInteger modulus, BigInteger privateExponent) {
        super(modulus);
        this.privateExponent = privateExponent;
    }

    public BigInteger getPrivateExponent() {
        return privateExponent;
    }

}
