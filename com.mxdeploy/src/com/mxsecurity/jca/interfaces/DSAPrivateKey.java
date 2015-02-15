
package com.mxsecurity.jca.interfaces;

import java.math.BigInteger;

import com.mxsecurity.jca.PrivateKey;



public interface DSAPrivateKey extends DSAKey, PrivateKey {
    public BigInteger getX();
}
