package com.mxsecurity.cipher;

import com.mxsecurity.jca.InvalidKeyException;
import com.mxsecurity.jca.Key;
import com.mxsecurity.jca.SecureRandom;
import com.mxsecurity.jca.spec.AlgorithmParameterSpec;
import com.mxsecurity.jce.CipherSpi;
import com.mxsecurity.jce.spec.SecretKeySpec;




public final class ArcFourSkip extends CipherSpi {
    ArcFour rc4;

    public ArcFourSkip() {
        rc4 = new ArcFour();
    }

    protected int engineDoFinal(byte[] input, int inputOffset, int inputLen,
                                byte[] output, int outputOffset) {
        return rc4.engineDoFinal(input, inputOffset, inputLen,
                                 output, outputOffset);
    }

    public void initializeKey(byte[] key) {
        rc4.initializeKey(key);

        byte[] buf = new byte[1536];
        for (int i=0; i<1536; i++) {
            buf[i] = (byte)rc4.arcfour_byte();
        }
    }

    protected int engineGetBlockSize() {
        return rc4.engineGetBlockSize();
    }

    protected byte[] engineGetIV() {
        return rc4.engineGetIV();
    }

    protected int engineGetOutputSize(int inputLen) {
        return rc4.engineGetOutputSize(inputLen);
    }

    protected void engineInit(int opmode, Key key,
                              AlgorithmParameterSpec params,
                              SecureRandom random)
        throws InvalidKeyException {
        initializeKey(((SecretKeySpec)key).getEncoded());
    }

    protected void engineInit(int opmode, Key key,
                              SecureRandom random) throws InvalidKeyException {
        engineInit(opmode, key, (AlgorithmParameterSpec)null, random);
    }

    protected void engineSetMode(String mode) {
        rc4.engineSetMode(mode);
    }

    protected void engineSetPadding(String padding) {
        rc4.engineSetPadding(padding);
    }

}
