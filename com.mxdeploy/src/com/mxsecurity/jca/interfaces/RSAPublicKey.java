
package com.mxsecurity.jca.interfaces;

import java.math.BigInteger;

import com.mxsecurity.jca.PublicKey;

public interface RSAPublicKey extends RSAKey, PublicKey {
    public BigInteger getPublicExponent();
}
