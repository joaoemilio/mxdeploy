
package com.mxsecurity.jca.spec;

import java.math.BigInteger;

public abstract class RSAKeyImpl {

    protected BigInteger modulus;

    protected RSAKeyImpl(BigInteger modulus) {
        this.modulus = modulus;
    }

    public BigInteger getModulus() {
        return modulus;
    }

}
