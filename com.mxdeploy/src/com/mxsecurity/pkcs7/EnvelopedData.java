package com.mxsecurity.pkcs7;

import com.mxsecurity.pkcs7.EncryptedContentInfo;
import com.mxsecurity.asn1.ASN1Integer;
import com.mxsecurity.asn1.ASN1Sequence;


public class EnvelopedData extends ASN1Sequence {

    public ASN1Integer          version;
    public RecipientInfos       recipientInfos;
    public EncryptedContentInfo encryptedContentInfo;

    public EnvelopedData() {
        version              = new ASN1Integer();
        recipientInfos       = new RecipientInfos();
        encryptedContentInfo = new EncryptedContentInfo();
        addComponent(version);
        addComponent(recipientInfos);
        addComponent(encryptedContentInfo);
    }

}

