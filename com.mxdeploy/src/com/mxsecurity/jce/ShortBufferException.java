
package com.mxsecurity.jce;

import com.mxsecurity.jca.GeneralSecurityException;

public class ShortBufferException extends GeneralSecurityException {
    public ShortBufferException(String msg) {
        super(msg);
    }
}
