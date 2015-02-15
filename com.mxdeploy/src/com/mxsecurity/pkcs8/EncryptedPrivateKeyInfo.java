 
 
  
  
  
  
  
  
  
  
  
  
 
  

package com.mxsecurity.pkcs8;


import com.mxsecurity.asn1.ASN1OctetString;
import com.mxsecurity.asn1.ASN1Sequence;
import com.mxsecurity.x509.AlgorithmIdentifier;

public class EncryptedPrivateKeyInfo extends ASN1Sequence {

    public AlgorithmIdentifier encryptionAlgorithm;
    public ASN1OctetString     encryptedData;

    public EncryptedPrivateKeyInfo() {
        encryptionAlgorithm = new AlgorithmIdentifier();
        encryptedData       = new ASN1OctetString();
        addComponent(encryptionAlgorithm);
        addComponent(encryptedData);
    }

}
