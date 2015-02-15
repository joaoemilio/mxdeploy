
package com.mxsecurity.x509;

import com.mxsecurity.asn1.ASN1BitString;
import com.mxsecurity.asn1.ASN1Sequence;

public class CertificateList extends ASN1Sequence {

    public TBSCertificateList  tbsCertificateList;
    public AlgorithmIdentifier signatureAlgorithm;
    public ASN1BitString       signatureValue;

    public CertificateList() {
        tbsCertificateList = new TBSCertificateList();
        signatureAlgorithm = new AlgorithmIdentifier();
        signatureValue     = new ASN1BitString();
        addComponent(tbsCertificateList);
        addComponent(signatureAlgorithm);
        addComponent(signatureValue);
    }

    //     public String toString() {
    //         return
    //             "Algorithm= " + signatureAlgorithm.algorithmName() + NL +
    //             "TBSCertificateList= " + tbsCertificateList;
    //     }
}
