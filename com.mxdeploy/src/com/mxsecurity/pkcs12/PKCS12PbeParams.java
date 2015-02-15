 
 
  
  
  
  
  
  
  
  
  
  
 
  

package com.mxsecurity.pkcs12;

import com.mxsecurity.asn1.ASN1Integer;
import com.mxsecurity.asn1.ASN1OctetString;
import com.mxsecurity.asn1.ASN1Sequence;

public final class PKCS12PbeParams extends ASN1Sequence {

    public ASN1OctetString salt;
    public ASN1Integer     iterations;

    public PKCS12PbeParams() {
        salt       = new ASN1OctetString();
        iterations = new ASN1Integer();
        addComponent(salt);
        addComponent(iterations);
    }

}

