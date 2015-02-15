package com.mxsecurity.publickey;

import java.math.BigInteger;

import com.mxsecurity.jca.InvalidAlgorithmParameterException;
import com.mxsecurity.jca.KeyPair;
import com.mxsecurity.jca.KeyPairGenerator;
import com.mxsecurity.jca.SecureRandom;
import com.mxsecurity.jca.spec.AlgorithmParameterSpec;
import com.mxsecurity.jce.spec.DHParameterSpec;





public class DHKeyPairGenerator extends KeyPairGenerator {

    protected DHParameterSpec params;
    protected SecureRandom    random;

    public DHKeyPairGenerator() {
        super("DH");
    }

    public void initialize(int keysize, SecureRandom random) {
        throw new Error("Not implemented: " +
                        "'DHKeyPairGenerator.initialize(int, SecureRandom)'");
    }

    public void initialize(AlgorithmParameterSpec params, SecureRandom random)
    throws InvalidAlgorithmParameterException {
        if(!(params instanceof DHParameterSpec)) {
            throw new InvalidAlgorithmParameterException("Invalid params: " +
                    params);
        }
        this.params = (DHParameterSpec)params;
        this.random = random;
    }

    public KeyPair generateKeyPair() {
        BigInteger g = params.getG();
        BigInteger p = params.getP();
        int        l = params.getL();

        if(l == 0) {
            l = p.bitLength();
        }

        if(random == null) {
            random = new SecureRandom();
        }

        BigInteger x = DiffieHellman.generateX(p, l, random);
        BigInteger y = DiffieHellman.generateY(x, g, p);

        return new KeyPair(new DHPublicKey(y, p, g), new DHPrivateKey(x, p, g));
    }

}
