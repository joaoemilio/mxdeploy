
package com.mxsecurity.jce.spec;

import com.mxsecurity.jca.spec.AlgorithmParameterSpec;

public class IvParameterSpec implements AlgorithmParameterSpec {
    byte[] iv;

    public IvParameterSpec(byte[] iv) {
        this(iv, 0, iv.length);
    }

    public IvParameterSpec(byte[] iv, int offset, int len) {
        this.iv = new byte[len];
        System.arraycopy(iv, offset, this.iv, 0, len);
    }

    public byte[] getIV() {
        byte[] ivc = new byte[iv.length];
        System.arraycopy(iv, 0, ivc, 0, iv.length);
        return ivc;
    }
}
