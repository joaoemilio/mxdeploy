package com.mxsecurity.jca;

public class SignatureException extends GeneralSecurityException {
    public SignatureException() {
        super();
    }

    public SignatureException(String msg) {
        super(msg);
    }
}
