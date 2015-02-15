
package com.mxsecurity.jce.spec;



import com.mxsecurity.jca.spec.KeySpec;
import com.mxsecurity.jce.SecretKey;




public class SecretKeySpec implements KeySpec, SecretKey {

    private byte[] key;
    private String algorithm;

    public SecretKeySpec(byte[] key, int offset, int len, String algorithm) {
        this.key = new byte[len];
        System.arraycopy(key, offset, this.key, 0, len);
        this.algorithm = algorithm;
    }

    public SecretKeySpec(byte[] key, java.lang.String algorithm) {
        this(key, 0, key.length, algorithm);
    }

    public boolean equals(java.lang.Object obj) {
        return false;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public byte[] getEncoded() {
        return key;
    }

    public String getFormat() {
        return null;
    }

    public int hashCode() {
        return 0;
    }
}
