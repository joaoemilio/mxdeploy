
package com.mxsecurity.jca;

import com.mxsecurity.jca.spec.AlgorithmParameterSpec;

// !!! TODO

public class AlgorithmParameters {
    protected AlgorithmParameters(AlgorithmParametersSpi paramSpi,
                                  Provider provider,
                                  String algorithm) {}

    public final String getAlgorithm() {
        return null;
    }

    public final byte[] getEncoded() {
        return null;
    }

    public final byte[] getEncoded(String format) {
        return null;
    }

    public final static AlgorithmParameters getInstance(String algorithm) {
        return null;
    }

    public final static AlgorithmParameters getInstance(String algorithm,
            String provider) {
        return null;
    }

    public final AlgorithmParameterSpec getParameterSpec(Class paramSpec) {
        return null;
    }

    public final Provider getProvider() {
        return null;
    }

    public final void init(AlgorithmParameterSpec paramSpec) {}

    public final void init(byte[] params) {}

    public final void init(byte[] params, String format) {}

    public final String toString() {
        return null;
    }
}
