
package com.mxsecurity.jca.spec;

import java.math.BigInteger;

public class DSAPrivateKeySpec extends DSAParamsImpl
    implements KeySpec {

    protected BigInteger x;

    public DSAPrivateKeySpec(BigInteger x,
                             BigInteger p, BigInteger q, BigInteger g) {
        super(p, q, g);
        this.x = x;
    }

    public BigInteger getX() {
        return x;
    }

}
