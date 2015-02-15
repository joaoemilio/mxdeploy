 
 
  
  
  
  
  
  
  
  
  
  
 
  

package com.mxsecurity.publickey;


import com.mxsecurity.jca.InvalidAlgorithmParameterException;
import com.mxsecurity.jca.KeyPair;
import com.mxsecurity.jca.KeyPairGenerator;
import com.mxsecurity.jca.SecureRandom;
import com.mxsecurity.jca.spec.AlgorithmParameterSpec;


public class RSAKeyPairGenerator extends KeyPairGenerator {

    protected SecureRandom random;
    protected int          keysize;

    public RSAKeyPairGenerator() {
        super("RSA");
    }

    public void initialize(int keysize, SecureRandom random) {
        this.random  = random;
        this.keysize = keysize;
    }

    public void initialize(AlgorithmParameterSpec params, SecureRandom random)
    throws InvalidAlgorithmParameterException {
        throw new Error("Not implemented: " +
                        "'RSAKeyPairGenerator.initialize(int, SecureRandom)'");
    }

    public KeyPair generateKeyPair() {
        if(random == null) {
            random = new SecureRandom();
        }
        return RSAAlgorithm.generateKeyPair(keysize, random);
    }

}
