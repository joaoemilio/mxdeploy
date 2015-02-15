
package com.mxsecurity.jce.spec;

import java.math.BigInteger;

import com.mxsecurity.jca.spec.KeySpec;



public class DHPublicKeySpec extends DHParamsImpl implements KeySpec {

    protected BigInteger y;

    public DHPublicKeySpec(BigInteger y, BigInteger p, BigInteger g) {
        super(p, g);
        this.y = y;
    }

    public BigInteger getY() {
        return y;
    }

}
