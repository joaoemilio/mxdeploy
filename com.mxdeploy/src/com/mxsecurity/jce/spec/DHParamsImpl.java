
package com.mxsecurity.jce.spec;

import java.math.BigInteger;


public abstract class DHParamsImpl {
    protected BigInteger p;
    protected BigInteger g;

    protected DHParamsImpl(BigInteger p, BigInteger g) {
        this.p = p;
        this.g = g;
    }

    public final BigInteger getP() {
        return p;
    }

    public final BigInteger getG() {
        return g;
    }

}
