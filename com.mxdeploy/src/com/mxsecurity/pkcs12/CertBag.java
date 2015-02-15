 
 
  
  
  
  
  
  
  
  
  
  
 
  

package com.mxsecurity.pkcs12;

import com.mxsecurity.asn1.ASN1AnyDefinedBy;
import com.mxsecurity.asn1.ASN1Explicit;
import com.mxsecurity.asn1.ASN1OID;
import com.mxsecurity.asn1.ASN1Sequence;

public final class CertBag extends ASN1Sequence {

    public ASN1OID          certId;
    public ASN1AnyDefinedBy certValue;

    public CertBag() {
        certId    = new ASN1OID();
        certValue = new ASN1AnyDefinedBy(certId);

        addComponent(certId);
        addComponent(new ASN1Explicit(0, certValue));
    }

}

