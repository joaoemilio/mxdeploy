
package com.mxsecurity.jca;

public class NoSuchProviderException extends GeneralSecurityException {
    public NoSuchProviderException() {
        super();
    }

    public NoSuchProviderException(String msg) {
        super(msg);
    }
}
