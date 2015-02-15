
package com.mxsecurity.jca.interfaces;

import java.math.BigInteger;


public interface RSAPrivateCrtKey extends RSAPrivateKey {
    public BigInteger getCrtCoefficient();
    public BigInteger getPrimeExponentP();
    public BigInteger getPrimeExponentQ();
    public BigInteger getPrimeP();
    public BigInteger getPrimeQ();
    public BigInteger getPublicExponent();
}
