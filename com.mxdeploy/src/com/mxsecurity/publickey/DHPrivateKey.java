package com.mxsecurity.publickey;

import java.math.BigInteger;

public class DHPrivateKey extends DHKey
    implements com.mxsecurity.jce.interfaces.DHPrivateKey {
    protected BigInteger x;

    public DHPrivateKey(BigInteger x, BigInteger p, BigInteger g) {
        super(p, g);
        this.x = x;
    }

    public BigInteger getX() {
        return x;
    }
}
