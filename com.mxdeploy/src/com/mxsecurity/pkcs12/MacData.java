 
 
  
  
  
  
  
  
  
  
  
  
 
  

package com.mxsecurity.pkcs12;


import com.mxsecurity.asn1.ASN1Integer;
import com.mxsecurity.asn1.ASN1OctetString;
import com.mxsecurity.asn1.ASN1Sequence;
import com.mxsecurity.pkcs7.DigestInfo;

public final class MacData extends ASN1Sequence {

    public DigestInfo      mac;
    public ASN1OctetString macSalt;
    public ASN1Integer     iterations;

    public MacData() {
        mac        = new DigestInfo();
        macSalt    = new ASN1OctetString();
        iterations = new ASN1Integer();
        addComponent(mac);
        addComponent(macSalt);
        addOptional(iterations, 1);
    }

    public int getIterations() {
        ASN1Integer iter = (iterations.isSet() ?
                            iterations : (ASN1Integer)getDefault(2));
        return iter.getValue().intValue();
    }

}

