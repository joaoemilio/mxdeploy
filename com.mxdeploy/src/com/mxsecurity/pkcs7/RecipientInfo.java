package com.mxsecurity.pkcs7;


import com.mxsecurity.pkcs7.IssuerAndSerialNumber;
import com.mxsecurity.asn1.ASN1Integer;
import com.mxsecurity.asn1.ASN1OctetString;
import com.mxsecurity.asn1.ASN1Sequence;
import com.mxsecurity.x509.AlgorithmIdentifier;

public final class RecipientInfo extends ASN1Sequence {

    ASN1Integer            version;
    IssuerAndSerialNumber  issuerAndSerialNumber;
    AlgorithmIdentifier    keyEncryptionAlgorithm;
    ASN1OctetString        encryptedKey;

    public RecipientInfo() {
        version                = new ASN1Integer();
        issuerAndSerialNumber  = new IssuerAndSerialNumber();
        keyEncryptionAlgorithm = new AlgorithmIdentifier();
        encryptedKey           = new ASN1OctetString();
        addComponent(version);
        addComponent(issuerAndSerialNumber);
        addComponent(keyEncryptionAlgorithm);
        addComponent(encryptedKey);
    }

}

