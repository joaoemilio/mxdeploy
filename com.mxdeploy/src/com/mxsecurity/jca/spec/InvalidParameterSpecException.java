
package com.mxsecurity.jca.spec;

import com.mxsecurity.jca.GeneralSecurityException;

public class InvalidParameterSpecException extends GeneralSecurityException {

    public InvalidParameterSpecException() {
        super();
    }

    public InvalidParameterSpecException(String msg) {
        super(msg);
    }

}
