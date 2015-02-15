package com.mxsecurity.jca;

public interface Key extends java.io.Serializable {
    public String getAlgorithm();
    public String getFormat();
    public byte[] getEncoded();
}

