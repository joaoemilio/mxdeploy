 
 
  
  
  
  
  
  
  
  
  
  
 
  

package com.mxsecurity.x509;

import com.mxsecurity.asn1.ASN1SetOf;

/**
 * <pre>
 * RelativeDistinguishedName       ::=
 *                SET SIZE (1 .. MAX) OF AttributeTypeAndValue
 * </pre>
 */
public final class RelativeDistinguishedName extends ASN1SetOf {

    public RelativeDistinguishedName() {
        super(AttributeTypeAndValue.class);
    }

    public AttributeTypeAndValue getTypeAndValue(int i) {
        return (AttributeTypeAndValue)getComponent(i);
    }

    public String getRFC2253Value() {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < getCount(); i++) {
            if(i > 0) {
                sb.append('+');
            }
            sb.append(getTypeAndValue(i).getRFC2253Value());
        }
        return sb.toString();
    }

}

