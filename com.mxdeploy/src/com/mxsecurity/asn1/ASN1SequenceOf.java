package com.mxsecurity.asn1;

public class ASN1SequenceOf extends ASN1Sequence {

    public ASN1SequenceOf(Class type) {
        super();
        this.ofType = type;
    }

}
