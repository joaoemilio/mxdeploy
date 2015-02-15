package com.mxsecurity.x509;

import com.mxsecurity.x509.Extension;
import com.mxsecurity.asn1.ASN1SequenceOf;

public class Extensions extends ASN1SequenceOf {

    public Extensions() {
        super(Extension.class);
    }

}

