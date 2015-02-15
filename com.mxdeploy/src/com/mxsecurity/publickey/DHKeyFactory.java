package com.mxsecurity.publickey;


import com.mxsecurity.jca.InvalidKeyException;
import com.mxsecurity.jca.Key;
import com.mxsecurity.jca.KeyFactorySpi;
import com.mxsecurity.jca.PrivateKey;
import com.mxsecurity.jca.PublicKey;
import com.mxsecurity.jca.spec.InvalidKeySpecException;
import com.mxsecurity.jca.spec.KeySpec;
import com.mxsecurity.jce.spec.DHPrivateKeySpec;
import com.mxsecurity.jce.spec.DHPublicKeySpec;





public final class DHKeyFactory extends KeyFactorySpi {

    protected PublicKey engineGeneratePublic(KeySpec keySpec)
    throws InvalidKeySpecException {
        if(!(keySpec instanceof DHPublicKeySpec)) {
            throw new InvalidKeySpecException("KeySpec " + keySpec +
                                              ", not supported");
        }
        DHPublicKeySpec dhPub = (DHPublicKeySpec)keySpec;
        return new DHPublicKey(dhPub.getY(), dhPub.getP(), dhPub.getG());
    }

    protected PrivateKey engineGeneratePrivate(KeySpec keySpec)
    throws InvalidKeySpecException {
        if(!(keySpec instanceof DHPrivateKeySpec)) {
            throw new InvalidKeySpecException("KeySpec " + keySpec +
                                              ", not supported");
        }
        DHPrivateKeySpec dhPrv = (DHPrivateKeySpec)keySpec;
        return new DHPrivateKey(dhPrv.getX(), dhPrv.getP(), dhPrv.getG());
    }

    protected KeySpec engineGetKeySpec(Key key, Class keySpec)
    throws InvalidKeySpecException {
        // !!! TODO
        throw new Error("DHKeyFactory.engineGetKeySpec() not implemented");
    }

    protected Key engineTranslateKey(Key key) throws InvalidKeyException {
        // !!! TODO
        throw new Error("DHKeyFactory.engineTranslateKey() not implemented");
    }

}
