 
 
  
  
  
  
  
  
  
  
  
  
 
  

package com.mxsecurity.publickey;

import java.math.BigInteger;

import com.mxsecurity.jca.InvalidAlgorithmParameterException;
import com.mxsecurity.jca.KeyPair;
import com.mxsecurity.jca.KeyPairGenerator;
import com.mxsecurity.jca.SecureRandom;
import com.mxsecurity.jca.spec.AlgorithmParameterSpec;
import com.mxsecurity.jca.spec.DSAParameterSpec;



public class DSAKeyPairGenerator extends KeyPairGenerator {

    protected SecureRandom     random;
    protected int              keysize;
    protected DSAParameterSpec params;

    public DSAKeyPairGenerator() {
        super("DSA");
    }

    public void initialize(int keysize, SecureRandom random) {
        this.random  = random;
        this.keysize = keysize;
    }

    public void initialize(AlgorithmParameterSpec params, SecureRandom random)
    throws InvalidAlgorithmParameterException {
        if(!(params instanceof DSAParameterSpec)) {
            throw new InvalidAlgorithmParameterException("Invalid params: " +
                    params);
        }
        this.params = (DSAParameterSpec)params;
        this.random = random;
    }

    public KeyPair generateKeyPair() {
        if(random == null) {
            random = new SecureRandom();
        }
        if(params == null) {
            params = DSAAlgorithm.generateParams(keysize, 160, random);
        }

        BigInteger p = params.getP();
        BigInteger q = params.getQ();
        BigInteger g = params.getG();
        BigInteger x = DSAAlgorithm.generatePrivateKey(q, random);
        BigInteger y = DSAAlgorithm.generatePublicKey(g, p, x);

        return new KeyPair(new DSAPublicKey(y, p, q, g),
                           new DSAPrivateKey(x, p, q, g));
    }

}
