
package com.mxsecurity.jca.spec;

import java.math.BigInteger;

import com.mxsecurity.jca.interfaces.DSAParams;



public abstract class DSAParamsImpl implements DSAParams {

    protected BigInteger p;
    protected BigInteger q;
    protected BigInteger g;

    protected DSAParamsImpl(BigInteger p, BigInteger q, BigInteger g) {
        this.p = p;
        this.q = q;
        this.g = g;
    }

    public BigInteger getG() {
        return g;
    }

    public BigInteger getP() {
        return p;
    }

    public BigInteger getQ() {
        return q;
    }

}
