package com.mxsecurity.pkcs12;

import com.mxsecurity.asn1.ASN1OIDRegistry;

public class RegisteredTypes extends ASN1OIDRegistry {

    public RegisteredTypes() {
        /*
         * We are dependent on pkcs7/1
         */
        ASN1OIDRegistry.addModule("com.mxsecurity.pkcs7");
        ASN1OIDRegistry.addModule("com.mxsecurity.pkcs1");

        // Should be moved to pkcs9
        // (might want to have implementations of Attribute types?)
        //
        // pkcs-9-at-friendlyName
        put("1.2.840.113549.1.9.20", "ASN1BMPString");
        // pkcs-9-at-localKeyId
        put("1.2.840.113549.1.9.21", "ASN1OctetString");

        // !!! TODO defined in pkcs9 (HOW represent?)
        // X509 cert
        put("1.2.840.113549.1.9.22.1", "ASN1OctetString");

        putName("1.2.840.113549.1.9.20", "friendlyName");
        putName("1.2.840.113549.1.9.21", "localKeyId");

        // KeyBag
        put("1.2.840.113549.1.12.10.1.1",
            "com.mxsecurity.pkcs8.PrivateKeyInfo");
        // PKCS8ShroudedKeyBag
        put("1.2.840.113549.1.12.10.1.2",
            "com.mxsecurity.pkcs8.EncryptedPrivateKeyInfo");
        put("1.2.840.113549.1.12.10.1.3",
            "com.mxsecurity.pkcs12.CertBag");

        put("1.2.840.113549.1.12.1.1",
            "com.mxsecurity.pkcs12.PKCS12PbeParams");
        put("1.2.840.113549.1.12.1.2",
            "com.mxsecurity.pkcs12.PKCS12PbeParams");
        put("1.2.840.113549.1.12.1.3",
            "com.mxsecurity.pkcs12.PKCS12PbeParams");
        put("1.2.840.113549.1.12.1.4",
            "com.mxsecurity.pkcs12.PKCS12PbeParams");
        put("1.2.840.113549.1.12.1.5",
            "com.mxsecurity.pkcs12.PKCS12PbeParams");
        put("1.2.840.113549.1.12.1.6",
            "com.mxsecurity.pkcs12.PKCS12PbeParams");

        putName("1.2.840.113549.1.12.1.1", "pbeWithSHAAnd128BitRC4");
        putName("1.2.840.113549.1.12.1.2", "pbeWithSHAAnd40BitRC4");
        putName("1.2.840.113549.1.12.1.3", "pbeWithSHAAnd3-KeyTripleDES-CBC");
        putName("1.2.840.113549.1.12.1.4", "pbeWithSHAAnd2-KeyTripleDES-CBC");
        putName("1.2.840.113549.1.12.1.5", "pbeWithSHAAnd128BitRC2-CBC");
        putName("1.2.840.113549.1.12.1.6", "pbeWithSHAAnd40BitRC2-CBC");


        // pkcs5 v1.5
        put("1.2.840.113549.1.5.1",
            "com.mxsecurity.pkcs12.PKCS12PbeParams");
        put("1.2.840.113549.1.5.3",
            "com.mxsecurity.pkcs12.PKCS12PbeParams");

        putName("1.2.840.113549.1.5.1", "pbeWithMD2AndDES-CBC"); // DES 56-bit
        putName("1.2.840.113549.1.5.3", "pbeWithMD5AndDES-CBC"); // DES 56-bit

        // pkcs5 v2.0
        put("1.2.840.113549.1.5.4",
            "com.mxsecurity.pkcs12.PKCS12PbeParams");
        put("1.2.840.113549.1.5.6",
            "com.mxsecurity.pkcs12.PKCS12PbeParams");
        put("1.2.840.113549.1.5.10",
            "com.mxsecurity.pkcs12.PKCS12PbeParams");
        put("1.2.840.113549.1.5.11",
            "com.mxsecurity.pkcs12.PKCS12PbeParams");

        putName("1.2.840.113549.1.5.4",  "pbeWithMD2AndRC2-CBC");  // RC2 64-bit
        putName("1.2.840.113549.1.5.6",  "pbeWithMD5AndRC2-CBC");  // RC2 64-bit
        putName("1.2.840.113549.1.5.10", "pbeWithSHA1AndDES-CBC"); // DES 56-bit
        putName("1.2.840.113549.1.5.11", "pbeWithSHA1AndRC2-CBC"); // RC2 64-bit
    }
}
