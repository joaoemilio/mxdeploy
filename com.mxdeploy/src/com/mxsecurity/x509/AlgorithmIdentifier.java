 
 
  
  
  
  
  
  
  
  
  
  
 
  

package com.mxsecurity.x509;

import com.mxsecurity.asn1.ASN1AnyDefinedBy;
import com.mxsecurity.asn1.ASN1OID;
import com.mxsecurity.asn1.ASN1OIDRegistry;
import com.mxsecurity.asn1.ASN1Sequence;

public class AlgorithmIdentifier extends ASN1Sequence {

    public ASN1OID          algorithm;
    public ASN1AnyDefinedBy parameters;

    public AlgorithmIdentifier() {
        algorithm  = new ASN1OID();
        parameters = new ASN1AnyDefinedBy(algorithm, true);
        addComponent(algorithm);
        addOptional(parameters);
    }

    public String algorithmName() {
        String oid  = algorithm.getString();
        String name = ASN1OIDRegistry.lookupName(oid);
        if(name == null) {
            name = oid;
        }
        return name;
    }

}

