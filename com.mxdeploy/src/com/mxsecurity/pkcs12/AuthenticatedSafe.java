 
 
  
  
  
  
  
  
  
  
  
  
 
  

package com.mxsecurity.pkcs12;


import com.mxsecurity.asn1.ASN1SequenceOf;
import com.mxsecurity.pkcs7.ContentInfo;

public final class AuthenticatedSafe extends ASN1SequenceOf {

    public AuthenticatedSafe() {
        super(ContentInfo.class);
    }

    public ContentInfo getContentInfo(int index) {
        return (ContentInfo)getComponent(index);
    }

}

