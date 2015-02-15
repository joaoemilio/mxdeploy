
package com.mxsecurity.jca.interfaces;

import com.mxsecurity.jca.InvalidParameterException;
import com.mxsecurity.jca.SecureRandom;

public interface DSAKeyPairGenerator {
    public void initialize(DSAParams params, SecureRandom random)
    throws InvalidParameterException;
    public void initialize(int modlen, boolean genParams, SecureRandom random)
    throws InvalidParameterException;
}
