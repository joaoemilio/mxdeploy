
package com.mxsecurity.jca.spec;

import com.mxsecurity.jca.GeneralSecurityException;

public class InvalidKeySpecException extends GeneralSecurityException {

    public InvalidKeySpecException() {
        super();
    }

    public InvalidKeySpecException(String msg) {
        super(msg);
    }

}
