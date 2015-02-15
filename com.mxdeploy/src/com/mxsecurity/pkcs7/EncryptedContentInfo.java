 
 
  
  
  
  
  
  
  
  
  
  
 
  

package com.mxsecurity.pkcs7;


import com.mxsecurity.asn1.ASN1Implicit;
import com.mxsecurity.asn1.ASN1OID;
import com.mxsecurity.asn1.ASN1OctetString;
import com.mxsecurity.asn1.ASN1Sequence;
import com.mxsecurity.x509.AlgorithmIdentifier;

public final class EncryptedContentInfo extends ASN1Sequence {

    public ASN1OID             contentType;
    public AlgorithmIdentifier contentEncryptionAlgorithm;
    public ASN1OctetString     encryptedContent;

    public EncryptedContentInfo() {
        contentType                = new ASN1OID();
        contentEncryptionAlgorithm = new AlgorithmIdentifier();
        encryptedContent           = new ASN1OctetString();
        addComponent(contentType);
        addComponent(contentEncryptionAlgorithm);
        addOptional(new ASN1Implicit(0, encryptedContent));
    }

}

