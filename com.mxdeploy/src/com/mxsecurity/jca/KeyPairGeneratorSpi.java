
package com.mxsecurity.jca;

import com.mxsecurity.jca.spec.AlgorithmParameterSpec;

public abstract class KeyPairGeneratorSpi {

    public KeyPairGeneratorSpi() {}

    public abstract void initialize(int keysize, SecureRandom random);

    public void initialize(AlgorithmParameterSpec params, SecureRandom random)
    throws InvalidAlgorithmParameterException {
        throw new UnsupportedOperationException(
            "'initialize(AlgorithmParameterSpec, SecureRandom)' not supported");
    }

    public abstract KeyPair generateKeyPair();

}
