
package com.mxsecurity.jca.spec;

import java.math.BigInteger;

public class RSAPrivateCrtKeySpec extends RSAPrivateKeySpec {

    protected BigInteger publicExponent;
    protected BigInteger primeP;
    protected BigInteger primeQ;
    protected BigInteger primeExponentP;
    protected BigInteger primeExponentQ;
    protected BigInteger crtCoefficient;

    public RSAPrivateCrtKeySpec(BigInteger modulus,
                                BigInteger publicExponent,
                                BigInteger privateExponent,
                                BigInteger primeP, BigInteger primeQ,
                                BigInteger primeExponentP,
                                BigInteger primeExponentQ,
                                BigInteger crtCoefficient) {
        super(modulus, privateExponent);
        this.publicExponent = publicExponent;
        this.primeP         = primeP;
        this.primeQ         = primeQ;
        this.primeExponentP = primeExponentP;
        this.primeExponentQ = primeExponentQ;
        this.crtCoefficient = crtCoefficient;
    }

    public BigInteger getPublicExponent() {
        return publicExponent;
    }

    public BigInteger getPrimeP() {
        return primeP;
    }

    public BigInteger getPrimeQ() {
        return primeQ;
    }

    public BigInteger getPrimeExponentP() {
        return primeExponentP;
    }

    public BigInteger getPrimeExponentQ() {
        return primeExponentQ;
    }

    public BigInteger getCrtCoefficient() {
        return crtCoefficient;
    }

}
