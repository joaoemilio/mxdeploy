package com.mxsecurity.publickey;


import com.mxsecurity.jca.InvalidKeyException;
import com.mxsecurity.jca.Key;
import com.mxsecurity.jca.KeyFactorySpi;
import com.mxsecurity.jca.PrivateKey;
import com.mxsecurity.jca.PublicKey;
import com.mxsecurity.jca.spec.InvalidKeySpecException;
import com.mxsecurity.jca.spec.KeySpec;
import com.mxsecurity.jca.spec.RSAPrivateCrtKeySpec;
import com.mxsecurity.jca.spec.RSAPrivateKeySpec;
import com.mxsecurity.jca.spec.RSAPublicKeySpec;



public class RSAKeyFactory extends KeyFactorySpi {

    protected PublicKey engineGeneratePublic(KeySpec keySpec)
    throws InvalidKeySpecException {
        if(!(keySpec instanceof RSAPublicKeySpec)) {
            throw new InvalidKeySpecException("KeySpec " + keySpec +
                                              ", not supported");
        }
        RSAPublicKeySpec rsaPub = (RSAPublicKeySpec)keySpec;
        return new RSAPublicKey(rsaPub.getModulus(),
                                rsaPub.getPublicExponent());
    }

    protected PrivateKey engineGeneratePrivate(KeySpec keySpec)
    throws InvalidKeySpecException {
        if(!(keySpec instanceof RSAPrivateKeySpec)) {
            throw new InvalidKeySpecException("KeySpec " + keySpec +
                                              ", not supported");
        }

        if(keySpec instanceof RSAPrivateCrtKeySpec) {
            RSAPrivateCrtKeySpec rsaPrv = (RSAPrivateCrtKeySpec)keySpec;
            return new RSAPrivateCrtKey(rsaPrv.getModulus(),
                                        rsaPrv.getPublicExponent(),
                                        rsaPrv.getPrivateExponent(),
                                        rsaPrv.getPrimeP(),
                                        rsaPrv.getPrimeQ(),
                                        rsaPrv.getPrimeExponentP(),
                                        rsaPrv.getPrimeExponentQ(),
                                        rsaPrv.getCrtCoefficient());
        } else {
            RSAPrivateKeySpec rsaPrv = (RSAPrivateKeySpec)keySpec;
            return new RSAPrivateKey(rsaPrv.getModulus(),
                                     rsaPrv.getPrivateExponent());
        }
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
