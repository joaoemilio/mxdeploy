 
 
  
  
  
  
  
  
  
  
  
  
 
  

package com.mxsecurity.publickey;

import java.math.BigInteger;

public class DSAPrivateKey extends DSAKey
    implements com.mxsecurity.jca.interfaces.DSAPrivateKey {
    protected BigInteger x;

    public DSAPrivateKey(BigInteger x, BigInteger p, BigInteger q, BigInteger g) {
        super(p, q, g);
        this.x = x;
    }

    public BigInteger getX() {
        return x;
    }

}
