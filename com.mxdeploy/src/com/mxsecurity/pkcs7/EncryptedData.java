package com.mxsecurity.pkcs7;

import com.mxsecurity.pkcs7.EncryptedContentInfo;
import com.mxsecurity.asn1.ASN1Integer;
import com.mxsecurity.asn1.ASN1Sequence;


public final class EncryptedData extends ASN1Sequence {

    public ASN1Integer          version;
    public EncryptedContentInfo encryptedContentInfo;

    public EncryptedData() {
        version              = new ASN1Integer();
        encryptedContentInfo = new EncryptedContentInfo();
        addComponent(version);
        addComponent(encryptedContentInfo);
    }

}

