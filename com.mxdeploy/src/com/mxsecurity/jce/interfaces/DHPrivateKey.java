package com.mxsecurity.jce.interfaces;

import java.math.BigInteger;

import com.mxsecurity.jca.PrivateKey;



public interface DHPrivateKey extends DHKey, PrivateKey {
    public BigInteger getX();
}
