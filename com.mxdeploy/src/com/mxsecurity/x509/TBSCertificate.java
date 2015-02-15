 
 
  
  
  
  
  
  
  
  
  
  
 
  

package com.mxsecurity.x509;

import com.mxsecurity.asn1.ASN1BitString;
import com.mxsecurity.asn1.ASN1Explicit;
import com.mxsecurity.asn1.ASN1Implicit;
import com.mxsecurity.asn1.ASN1Integer;
import com.mxsecurity.asn1.ASN1Sequence;

public class TBSCertificate extends ASN1Sequence {

    public ASN1Integer          version;
    public ASN1Integer          serialNumber;
    public AlgorithmIdentifier  signature;
    public Name                 issuer;
    public Validity             validity;
    public Name                 subject;
    public SubjectPublicKeyInfo subjectPublicKeyInfo;
    public ASN1BitString        issuerUniqueID;
    public ASN1BitString        subjectUniqueID;
    public Extensions           extensions;

    public final static int VER1 = 0;
    public final static int VER2 = 1;
    public final static int VER3 = 2;

    public TBSCertificate() {
        version              = new ASN1Integer();
        serialNumber         = new ASN1Integer();
        signature            = new AlgorithmIdentifier();
        issuer               = new Name();
        validity             = new Validity();
        subject              = new Name();
        subjectPublicKeyInfo = new SubjectPublicKeyInfo();
        issuerUniqueID       = new ASN1BitString();
        subjectUniqueID      = new ASN1BitString();
        extensions           = new Extensions();
        addOptional(new ASN1Explicit(0, version), VER1);
        addComponent(serialNumber);
        addComponent(signature);
        addComponent(issuer);
        addComponent(validity);
        addComponent(subject);
        addComponent(subjectPublicKeyInfo);
        addOptional(new ASN1Implicit(1, issuerUniqueID));
        addOptional(new ASN1Implicit(2, subjectUniqueID));
        addOptional(new ASN1Explicit(3, extensions));
    }

}
