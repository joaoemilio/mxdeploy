
package com.mxsecurity.jca.spec;

import java.math.BigInteger;

public class DSAPublicKeySpec extends DSAParamsImpl
    implements KeySpec {

    protected BigInteger y;

    public DSAPublicKeySpec(BigInteger y,
                            BigInteger p, BigInteger q, BigInteger g) {
        super(p, q, g);
        this.y = y;
    }

    public BigInteger getY() {
        return y;
    }

}
