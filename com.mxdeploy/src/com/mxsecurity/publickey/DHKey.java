package com.mxsecurity.publickey;

import java.math.BigInteger;

import com.mxsecurity.jca.Key;
import com.mxsecurity.jce.spec.DHParameterSpec;




public class DHKey extends DHParameterSpec
    implements com.mxsecurity.jce.interfaces.DHKey, Key {

    protected DHKey(BigInteger p, BigInteger g) {
        super(p, g);
    }

    public String getAlgorithm() {
        return "DiffieHellman";
    }

    public String getFormat() {
        return null;
    }

    public byte[] getEncoded() {
        return null;
    }

    public DHParameterSpec getParams() {
        return this;
    }

}
