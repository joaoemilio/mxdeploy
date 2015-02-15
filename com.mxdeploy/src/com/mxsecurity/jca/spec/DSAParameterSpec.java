
package com.mxsecurity.jca.spec;

import java.math.BigInteger;

import com.mxsecurity.jca.interfaces.DSAParams;



public class DSAParameterSpec extends DSAParamsImpl
    implements AlgorithmParameterSpec, DSAParams {
    public DSAParameterSpec(BigInteger p, BigInteger q, BigInteger g) {
        super(p, q, g);
    }

}
