
package com.mxsecurity.jce;

import com.mxsecurity.jca.InvalidKeyException;
import com.mxsecurity.jca.Key;
import com.mxsecurity.jca.SecureRandom;
import com.mxsecurity.jca.spec.AlgorithmParameterSpec;



public abstract class CipherSpi {

    public CipherSpi() {}

    /*
    protected abstract byte[] engineDoFinal(byte[] input, int inputOffset,
    	    int inputLen);
    */

    protected abstract int engineDoFinal(byte[] input, int inputOffset,
                                         int inputLen,
                                         byte[] output, int outputOffset);

    protected abstract int engineGetBlockSize();

    protected abstract byte[] engineGetIV();

    protected abstract int engineGetOutputSize(int inputLen);

    /*
    protected abstract AlgorithmParameters engineGetParameters();
    */

    /*
    protected abstract void engineInit(int opmode, Key key,
           AlgorithmParameters params,
           SecureRandom random);
    */

    protected abstract void engineInit(int opmode, Key key,
                                       AlgorithmParameterSpec params,
                                       SecureRandom random) throws
                InvalidKeyException;

    protected abstract void engineInit(int opmode, Key key,
                                       SecureRandom random) throws
                InvalidKeyException;

    protected abstract void engineSetMode(String mode);

    protected abstract void engineSetPadding(String padding);

    /*
    protected abstract byte[] engineUpdate(byte[] input, int inputOffset,
    	   int inputLen);

    protected abstract int engineUpdate(byte[] input, int inputOffset,
    	int inputLen,
    	byte[] output, int outputOffset);
    */

}
