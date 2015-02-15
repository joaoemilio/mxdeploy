 
 
  
  
  
  
  
  
  
  
  
  
 
  

package com.mxsecurity.x509;

import com.mxsecurity.asn1.ASN1Choice;
import com.mxsecurity.asn1.ASN1GeneralizedTime;
import com.mxsecurity.asn1.ASN1UTCTime;

public class Time extends ASN1Choice {

    public Time() {
        super();
        setMember(new ASN1UTCTime());
        setMember(new ASN1GeneralizedTime());
    }

}
