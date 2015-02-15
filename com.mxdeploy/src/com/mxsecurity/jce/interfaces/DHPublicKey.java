package com.mxsecurity.jce.interfaces;

import java.math.BigInteger;

import com.mxsecurity.jca.PublicKey;

public interface DHPublicKey extends DHKey, PublicKey {
    public BigInteger getY();
}
