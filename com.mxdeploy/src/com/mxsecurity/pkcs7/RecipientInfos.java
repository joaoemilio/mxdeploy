 
 
  
  
  
  
  
  
  
  
  
  
 
  

package com.mxsecurity.pkcs7;

import com.mxsecurity.pkcs7.RecipientInfo;
import com.mxsecurity.asn1.ASN1Choice;
import com.mxsecurity.asn1.ASN1SequenceOf;
import com.mxsecurity.asn1.ASN1SetOf;

public final class RecipientInfos extends ASN1Choice {

    public ASN1SequenceOf       riSequence;
    public ASN1SetOf            riSet;

    public RecipientInfos() {
        riSequence = new ASN1SequenceOf(RecipientInfo.class);
        riSet      = new ASN1SetOf(RecipientInfo.class);
        setMember(riSequence);
        setMember(riSet);
    }

}

