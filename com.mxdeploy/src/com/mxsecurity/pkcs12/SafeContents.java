package com.mxsecurity.pkcs12;

import com.mxsecurity.pkcs12.SafeBag;
import com.mxsecurity.asn1.ASN1SequenceOf;

public final class SafeContents extends ASN1SequenceOf {

    public SafeContents() {
        super(SafeBag.class);
    }

    public SafeBag getSafeBag(int index) {
        return (SafeBag)getComponent(index);
    }

}

