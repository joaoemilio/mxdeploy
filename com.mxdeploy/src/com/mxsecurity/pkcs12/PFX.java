package com.mxsecurity.pkcs12;


import com.mxsecurity.pkcs12.MacData;
import com.mxsecurity.asn1.ASN1Integer;
import com.mxsecurity.asn1.ASN1OctetString;
import com.mxsecurity.asn1.ASN1Sequence;
import com.mxsecurity.pkcs7.ContentInfo;

public final class PFX extends ASN1Sequence {

    public ASN1Integer version;
    public ContentInfo authSafe;
    public MacData     macData;

    public PFX() {
        version  = new ASN1Integer();
        authSafe = new ContentInfo();
        macData  = new MacData();
        addComponent(version);
        addComponent(authSafe);
        addOptional(macData);
    }

    public ASN1OctetString getDataContent() {
        return (ASN1OctetString)authSafe.content.getValue();
    }

}
