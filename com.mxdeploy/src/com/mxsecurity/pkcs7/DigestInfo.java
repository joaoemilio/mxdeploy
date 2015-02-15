 
 
  
  
  
  
  
  
  
  
  
  
 
  

package com.mxsecurity.pkcs7;


import com.mxsecurity.asn1.ASN1OctetString;
import com.mxsecurity.asn1.ASN1Sequence;
import com.mxsecurity.x509.AlgorithmIdentifier;

public final class DigestInfo extends ASN1Sequence {

    public AlgorithmIdentifier digestAlgorithm;
    public ASN1OctetString     digest;

    public DigestInfo() {
        digestAlgorithm = new AlgorithmIdentifier();
        digest          = new ASN1OctetString();
        addComponent(digestAlgorithm);
        addComponent(digest);
    }

}

