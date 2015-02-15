 
 
  
  
  
  
  
  
  
  
  
  
 
  

package com.mxsecurity.x509;

import com.mxsecurity.asn1.ASN1BitString;
import com.mxsecurity.asn1.ASN1Sequence;

public class Certificate extends ASN1Sequence {

    public TBSCertificate      tbsCertificate;
    public AlgorithmIdentifier signatureAlgorithm;
    public ASN1BitString       signatureValue;

    public Certificate() {
        tbsCertificate     = new TBSCertificate();
        signatureAlgorithm = new AlgorithmIdentifier();
        signatureValue     = new ASN1BitString();
        addComponent(tbsCertificate);
        addComponent(signatureAlgorithm);
        addComponent(signatureValue);
    }

}
