package com.mxsecurity.publickey;

import java.math.BigInteger;

public class DHPublicKey extends DHKey
    implements com.mxsecurity.jce.interfaces.DHPublicKey {
    protected BigInteger y;

    public DHPublicKey(BigInteger y, BigInteger p, BigInteger g) {
        super(p, g);
        this.y = y;
    }

    public BigInteger getY() {
        return y;
    }
}
