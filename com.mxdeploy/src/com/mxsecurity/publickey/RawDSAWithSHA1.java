package com.mxsecurity.publickey;

import java.math.BigInteger;

import com.mxsecurity.jca.InvalidKeyException;
import com.mxsecurity.jca.interfaces.DSAParams;
import com.mxsecurity.jca.interfaces.DSAPrivateKey;
import com.mxsecurity.jca.interfaces.DSAPublicKey;



public class RawDSAWithSHA1 extends BaseSignature {

    public RawDSAWithSHA1() {
        super("SHA1");
    }

    protected void initVerify() throws InvalidKeyException {
        if(publicKey == null || !(publicKey instanceof DSAPublicKey)) {
            throw new InvalidKeyException("Wrong key for DSAWithSHA1 verify: " +
                                          publicKey);
        }
    }

    protected void initSign() throws InvalidKeyException {
        if(privateKey == null || !(privateKey instanceof DSAPrivateKey)) {
            throw new InvalidKeyException("Wrong key for DSAWithSHA1 sign: " +
                                          privateKey);
        }
    }

    protected byte[] sign(byte[] data) {
        DSAPrivateKey key  = (DSAPrivateKey)privateKey;
        DSAParams     parm = key.getParams();
        BigInteger    x    = key.getX();
        BigInteger    p    = parm.getP();
        BigInteger    q    = parm.getQ();
        BigInteger    g    = parm.getG();
        return DSAAlgorithm.sign(x, p, q, g, data);
    }

    protected boolean verify(byte[] signature, byte[] data) {
        DSAPublicKey key  = (DSAPublicKey)publicKey;
        DSAParams    parm = key.getParams();
        BigInteger   y    = key.getY();
        BigInteger   p    = parm.getP();
        BigInteger   q    = parm.getQ();
        BigInteger   g    = parm.getG();
        return DSAAlgorithm.verify(y, p, q, g, signature, data);
    }

}
