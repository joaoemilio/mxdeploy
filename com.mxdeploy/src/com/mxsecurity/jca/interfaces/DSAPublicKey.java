
package com.mxsecurity.jca.interfaces;

import java.math.BigInteger;

import com.mxsecurity.jca.PublicKey;


public interface DSAPublicKey extends DSAKey, PublicKey {
    public BigInteger getY();
}
