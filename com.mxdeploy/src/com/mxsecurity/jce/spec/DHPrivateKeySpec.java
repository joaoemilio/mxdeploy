
package com.mxsecurity.jce.spec;

import java.math.BigInteger;

import com.mxsecurity.jca.spec.KeySpec;



public class DHPrivateKeySpec extends DHParamsImpl implements KeySpec {

    protected BigInteger x;

    public DHPrivateKeySpec(BigInteger x, BigInteger p, BigInteger g) {
        super(p, g);
        this.x = x;
    }

    public BigInteger getX() {
        return x;
    }

}
