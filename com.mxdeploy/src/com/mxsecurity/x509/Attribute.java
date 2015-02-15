 
 
  
  
  
  
  
  
  
  
  
  
 
  

package com.mxsecurity.x509;

import com.mxsecurity.asn1.ASN1Any;
import com.mxsecurity.asn1.ASN1OID;
import com.mxsecurity.asn1.ASN1OIDRegistry;
import com.mxsecurity.asn1.ASN1Sequence;
import com.mxsecurity.asn1.ASN1SetOf;

public class Attribute extends ASN1Sequence {

    public  ASN1OID    type;
    public  ASN1SetOf  values;

    public Attribute() {
        type   = new ASN1OID();
        values = new ASN1SetOf() {
                     protected Class ofType() {
                         Class c = lookupAttr();
                         if(c == null) {
                             c = ASN1Any.class;
                         }
                         return c;
                     }
                 };
        addComponent(type);
        addComponent(values);
    }

    protected Class lookupAttr() {
        return ASN1OIDRegistry.lookupType(type);
    }

    public String attributeName() {
        String oid  = type.getString();
        String name = ASN1OIDRegistry.lookupName(oid);
        if(name == null) {
            name = oid;
        }
        return name;
    }

}

