 
 
  
  
  
  
  
  
  
  
  
  
 
  

package com.mxsecurity.x509;

import com.mxsecurity.asn1.ASN1Boolean;
import com.mxsecurity.asn1.ASN1OID;
import com.mxsecurity.asn1.ASN1OctetString;
import com.mxsecurity.asn1.ASN1Sequence;

public class Extension extends ASN1Sequence {

    public ASN1OID         extnID;
    public ASN1Boolean     critical;
    public ASN1OctetString extnValue;

    public Extension() {
        extnID    = new ASN1OID();
        critical  = new ASN1Boolean();
        extnValue = new ASN1OctetString();
        addComponent(extnID);
        addOptional(critical, false);
        addComponent(extnValue);
    }

}
