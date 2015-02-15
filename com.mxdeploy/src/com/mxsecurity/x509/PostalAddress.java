 
 
  
  
  
  
  
  
  
  
  
  
 
  

package com.mxsecurity.x509;

import com.mxsecurity.asn1.ASN1SequenceOf;

/**
 * Reporesents a postal address.
 *
 * <pre>
 * PostalAddress ::= SEQUENCE SIZE (1..6) OF DirectoryString
 * </pre>
 */
public class PostalAddress extends ASN1SequenceOf {

    public PostalAddress() {
        super(DirectoryString.class);
    }

}
