
package com.mxsecurity.asn1;

public class ASN1SetOf extends ASN1Set {

    public ASN1SetOf(Class type) {
        super();
        this.ofType = type;
    }

    protected ASN1SetOf() {
        super();
    }

}
