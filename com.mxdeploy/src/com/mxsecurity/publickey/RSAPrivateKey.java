 
 
  
  
  
  
  
  
  
  
  
  
 
  

package com.mxsecurity.publickey;


import java.math.BigInteger;

public class RSAPrivateKey extends RSAKey
    implements com.mxsecurity.jca.interfaces.RSAPrivateKey {

    protected BigInteger privateExponent;

    public RSAPrivateKey(BigInteger modulus, BigInteger privateExponent) {
        super(modulus);
        this.privateExponent = privateExponent;
    }

    public BigInteger getPrivateExponent() {
        return privateExponent;
    }

}
