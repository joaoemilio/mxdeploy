package com.mxsecurity.jca;

public class UnrecoverableKeyException extends GeneralSecurityException {

    public UnrecoverableKeyException() {
        super();
    }

    public UnrecoverableKeyException(String msg) {
        super(msg);
    }

}
