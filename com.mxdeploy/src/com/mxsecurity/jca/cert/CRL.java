
package com.mxsecurity.jca.cert;

public abstract class CRL {
    private String type;

    protected CRL(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public abstract boolean isRevoked(Certificate cert);
    public abstract String toString();
}

