 
 
  
  
  
  
  
  
  
  
  
  
 
  

package com.mxsecurity.pkcs7;

import com.mxsecurity.asn1.ASN1OIDRegistry;

public class RegisteredTypes extends ASN1OIDRegistry {

    public RegisteredTypes() {
        /* !!! TODO Support these too
           signedData              OBJECT IDENTIFIER ::= { pkcs-7 2 }
           signedAndEnvelopedData  OBJECT IDENTIFIER ::= { pkcs-7 4 }
           digestedData            OBJECT IDENTIFIER ::= { pkcs-7 5 }
        */
        // !!! TODO find used PKI algorithms for EnvelopedData if needed

        put("1.2.840.113549.1.7.1", "ASN1OctetString");
        put("1.2.840.113549.1.7.3",
            "com.mxsecurity.pkcs7.EnvelopedData");
        put("1.2.840.113549.1.7.6",
            "com.mxsecurity.pkcs7.EncryptedData");

        // Move to somewhere else?
        // OIW.secsig SHA1
        put("1.3.14.3.2.26", "ASN1Null");
        // RSA MD5
        put("1.2.840.113549.2.5", "ASN1Null");
        // RSA MD2
        put("1.2.840.113549.2.2", "ASN1Null");
        // Teletrust.alg RIPEMD160
        put("1.3.36.3.2.1", "ASN1Null");

        putName("1.3.14.3.2.26", "SHA1");
        putName("1.2.840.113549.2.5", "MD5");
        putName("1.2.840.113549.2.2", "MD2");
        putName("1.3.36.3.2.1", "RIPEMD160");
    }

}
