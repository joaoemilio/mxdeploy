
package com.mxsecurity.jca;

public final class KeyPair {

    PrivateKey privateKey;
    PublicKey  publicKey;

    public KeyPair(PublicKey publicKey, PrivateKey privateKey) {
        this.publicKey  = publicKey;
        this.privateKey = privateKey;
    }

    public PublicKey getPublic() {
        return publicKey;
    }

    public PrivateKey getPrivate() {
        return privateKey;
    }

}
