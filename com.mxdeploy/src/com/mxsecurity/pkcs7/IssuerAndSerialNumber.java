 
 
  
  
  
  
  
  
  
  
  
  
 
  

package com.mxsecurity.pkcs7;


import com.mxsecurity.asn1.ASN1Integer;
import com.mxsecurity.asn1.ASN1Sequence;
import com.mxsecurity.x509.Name;

public final class IssuerAndSerialNumber extends ASN1Sequence {

    public Name        issuer;
    public ASN1Integer serialNumber;

    public IssuerAndSerialNumber() {
        issuer       = new Name();
        serialNumber = new ASN1Integer();
        addComponent(issuer);
        addComponent(serialNumber);
    }

}

