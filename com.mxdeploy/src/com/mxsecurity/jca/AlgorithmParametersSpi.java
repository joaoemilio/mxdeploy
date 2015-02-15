
package com.mxsecurity.jca;

import java.io.IOException;

import com.mxsecurity.jca.spec.AlgorithmParameterSpec;
import com.mxsecurity.jca.spec.InvalidParameterSpecException;



// !!! TODO

public abstract class AlgorithmParametersSpi {

    public AlgorithmParametersSpi() {}

    protected abstract byte[] engineGetEncoded() throws IOException;

    protected abstract byte[] engineGetEncoded(String format)
    throws IOException;

    protected abstract AlgorithmParameterSpec
    engineGetParameterSpec(Class paramSpec)
    throws InvalidParameterSpecException;

    protected abstract void engineInit(AlgorithmParameterSpec paramSpec)
    throws InvalidParameterSpecException;

    protected abstract void engineInit(byte[] params) throws IOException;

    protected abstract void engineInit(byte[] params, String format)
    throws IOException;

    protected abstract String engineToString();

}
