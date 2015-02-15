
package com.mxsecurity.jce.spec;

import com.mxsecurity.jca.spec.AlgorithmParameterSpec;

public class DHGenParameterSpec implements AlgorithmParameterSpec {

    protected int primeSize;
    protected int exponentSize;

    public DHGenParameterSpec(int primeSize, int exponentSize) {
        this.primeSize    = primeSize;
        this.exponentSize = exponentSize;
    }

    public int getPrimeSize() {
        return primeSize;
    }

    public int getExponentSize() {
        return exponentSize;
    }

}
