 
 
  
  
  
  
  
  
  
  
  
  
 
  

package com.mxsecurity.pkcs7;

import com.mxsecurity.asn1.ASN1AnyDefinedBy;
import com.mxsecurity.asn1.ASN1Explicit;
import com.mxsecurity.asn1.ASN1OID;
import com.mxsecurity.asn1.ASN1Sequence;


public class ContentInfo extends ASN1Sequence {

    public ASN1OID          contentType;
    public ASN1AnyDefinedBy content;

    public ContentInfo() {
        contentType = new ASN1OID();
        content     = new ASN1AnyDefinedBy(contentType);
        addComponent(contentType);
        addOptional(new ASN1Explicit(0, content));
    }

}

