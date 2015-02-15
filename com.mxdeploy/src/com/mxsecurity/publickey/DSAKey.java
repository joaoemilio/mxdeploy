package com.mxsecurity.publickey;

import java.math.BigInteger;

import com.mxsecurity.jca.Key;
import com.mxsecurity.jca.interfaces.DSAParams;
import com.mxsecurity.jca.spec.DSAParameterSpec;



public class DSAKey extends DSAParameterSpec
    implements com.mxsecurity.jca.interfaces.DSAKey, Key {

    protected DSAKey(BigInteger p, BigInteger q, BigInteger g) {
        super(p, q, g);
    }

    public String getAlgorithm() {
        return "DSA";
    }

    public byte[] getEncoded() {
        return null;
    }

    public String getFormat() {
        return null;
    }

    public DSAParams getParams() {
        return this;
    }

}
