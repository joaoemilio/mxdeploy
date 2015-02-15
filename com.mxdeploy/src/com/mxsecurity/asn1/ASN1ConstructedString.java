package com.mxsecurity.asn1;

public class ASN1ConstructedString extends ASN1Structure {

    protected ASN1ConstructedString(int tag, Class ofType) {
        super(tag, 5);
        this.ofType = ofType;
    }

}
