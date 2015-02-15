 
 
  
  
  
  
  
  
  
  
  
  
 
  

package com.mxsecurity.x509;

import com.mxsecurity.asn1.ASN1BitString;
import com.mxsecurity.asn1.ASN1Sequence;

public class SubjectPublicKeyInfo extends ASN1Sequence {

    public AlgorithmIdentifier algorithm;
    public ASN1BitString       subjectPublicKey;

    public SubjectPublicKeyInfo() {
        algorithm        = new AlgorithmIdentifier();
        subjectPublicKey = new ASN1BitString();
        addComponent(algorithm);
        addComponent(subjectPublicKey);
    }

}
