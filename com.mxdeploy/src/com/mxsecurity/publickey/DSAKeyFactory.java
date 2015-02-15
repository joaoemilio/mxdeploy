package com.mxsecurity.publickey;


import com.mxsecurity.jca.InvalidKeyException;
import com.mxsecurity.jca.Key;
import com.mxsecurity.jca.KeyFactorySpi;
import com.mxsecurity.jca.PrivateKey;
import com.mxsecurity.jca.PublicKey;
import com.mxsecurity.jca.spec.DSAPrivateKeySpec;
import com.mxsecurity.jca.spec.DSAPublicKeySpec;
import com.mxsecurity.jca.spec.InvalidKeySpecException;
import com.mxsecurity.jca.spec.KeySpec;



public class DSAKeyFactory extends KeyFactorySpi {

    protected PublicKey engineGeneratePublic(KeySpec keySpec)
    throws InvalidKeySpecException {
        if(!(keySpec instanceof DSAPublicKeySpec)) {
            throw new InvalidKeySpecException("KeySpec " + keySpec +
                                              ", not supported");
        }
        DSAPublicKeySpec dsaPub = (DSAPublicKeySpec)keySpec;
        return new DSAPublicKey(dsaPub.getY(),
                                dsaPub.getP(), dsaPub.getQ(), dsaPub.getG());
    }

    protected PrivateKey engineGeneratePrivate(KeySpec keySpec)
    throws InvalidKeySpecException {
        if(!(keySpec instanceof DSAPrivateKeySpec)) {
            throw new InvalidKeySpecException("KeySpec " + keySpec +
                                              ", not supported");
        }
        DSAPrivateKeySpec dsaPrv = (DSAPrivateKeySpec)keySpec;
        return new DSAPrivateKey(dsaPrv.getX(),
                                 dsaPrv.getP(), dsaPrv.getQ(), dsaPrv.getG());
    }

    protected KeySpec engineGetKeySpec(Key key, Class keySpec)
    throws InvalidKeySpecException {
        // !!! TODO
        return null;
    }

    protected Key engineTranslateKey(Key key)
    throws InvalidKeyException {
        // !!! TODO
        return null;
    }

}
