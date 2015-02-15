
package com.mxsecurity.jce;

import com.mxsecurity.jca.InvalidAlgorithmParameterException;
import com.mxsecurity.jca.InvalidKeyException;
import com.mxsecurity.jca.Key;
import com.mxsecurity.jca.spec.AlgorithmParameterSpec;



public abstract class MacSpi {

    public MacSpi() {}

    public Object clone() throws CloneNotSupportedException {
        if(this instanceof Cloneable) {
            return super.clone();
        } else {
            throw new CloneNotSupportedException();
        }
    }

    protected abstract byte[] engineDoFinal();

    protected abstract int engineGetMacLength();

    protected abstract void engineInit(Key key, AlgorithmParameterSpec params)
    throws InvalidKeyException, InvalidAlgorithmParameterException;

    protected abstract void engineReset();

    protected abstract void engineUpdate(byte input);

    protected abstract void engineUpdate(byte[] input, int offset, int len);

}
