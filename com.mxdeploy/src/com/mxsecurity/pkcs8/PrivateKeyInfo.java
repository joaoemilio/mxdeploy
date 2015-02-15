 
 
  
  
  
  
  
  
  
  
  
  
 
  

package com.mxsecurity.pkcs8;


import com.mxsecurity.asn1.ASN1Implicit;
import com.mxsecurity.asn1.ASN1Integer;
import com.mxsecurity.asn1.ASN1OctetString;
import com.mxsecurity.asn1.ASN1Sequence;
import com.mxsecurity.asn1.ASN1SetOf;
import com.mxsecurity.x509.AlgorithmIdentifier;
import com.mxsecurity.x509.Attribute;

public final class PrivateKeyInfo extends ASN1Sequence {

    public ASN1Integer         version;
    public AlgorithmIdentifier privateKeyAlgorithm;
    public ASN1OctetString     privateKey;
    public ASN1SetOf           attributes;

    public PrivateKeyInfo() {
        version             = new ASN1Integer();
        privateKeyAlgorithm = new AlgorithmIdentifier();
        privateKey          = new ASN1OctetString();
        attributes          = new ASN1SetOf(Attribute.class);
        addComponent(version);
        addComponent(privateKeyAlgorithm);
        addComponent(privateKey);
        addOptional(new ASN1Implicit(0, attributes));
    }

}

