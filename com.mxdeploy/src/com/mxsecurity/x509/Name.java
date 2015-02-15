 
 
  
  
  
  
  
  
  
  
  
  
 
  

package com.mxsecurity.x509;

import com.mxsecurity.asn1.ASN1SequenceOf;

/**
 * <pre>
 * NOTE: Shortened here, but RDNSequence feels superfluos until more choices
 * are available... 
 * should be: Name ::= CHOICE { -- only one possibility for
 *                              now -- rdnSequence RDNSequence }
 *
 * RDNSequence       ::= SEQUENCE OF RelativeDistinguishedName
 *
 * DistinguishedName ::=   RDNSequence
 * </pre>
 */
public final class Name extends ASN1SequenceOf {

    public Name() {
        super(RelativeDistinguishedName.class);
    }

    public RelativeDistinguishedName getRDN(int i) {
        return (RelativeDistinguishedName)getComponent(i);
    }

    public void setRFC2253Value(String rfc2253DN) {
        // TODO
    }

    public String getRFC2253Value() {
        StringBuffer sb = new StringBuffer();
        int last = getCount() - 1;
        for(int i = last; i >= 0; i--) {
            if(i < last) {
                sb.append(',');
            }
            RelativeDistinguishedName rdn = getRDN(i);
            sb.append(rdn.getRFC2253Value());
        }
        return sb.toString();
    }

}
