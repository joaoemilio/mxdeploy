
package com.mxsecurity.jca.interfaces;

import java.math.BigInteger;

import com.mxsecurity.jca.PrivateKey;



public interface RSAPrivateKey extends RSAKey, PrivateKey {
    public BigInteger getPrivateExponent();
}
