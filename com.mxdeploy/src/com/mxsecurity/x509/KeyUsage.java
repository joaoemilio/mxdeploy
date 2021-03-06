
package com.mxsecurity.x509;

import com.mxsecurity.asn1.ASN1BitString;

/**
 * Represents the key usage parameters of a certificate.
 * 
 * <pre>
 * KeyUsage ::= BIT STRING {
	digitalSignature(0),
	nonRepudiation(1),
	keyEncipherment(2),
	dataEncipherment(3),
	keyAgreement(4),
	keyCertSign(5),
	cRLSign(6),
        encipherOnly(7),
        decipherOnly(8)
}
 * </pre>
 */

public class KeyUsage extends ASN1BitString {

    public final static int DigitalSignature = 0;
    public final static int NonRepudiation   = 1;
    public final static int KeyEncipherment  = 2;
    public final static int DataEncipherment = 3;
    public final static int KeyAgreement     = 4;
    public final static int KeyCertSign      = 5;
    public final static int CRLSign          = 6;
    public final static int EncipherOnly     = 7;
    public final static int DecipherOnly     = 8;

    public KeyUsage() {
        super();
    }

    public boolean getField(int i) {
        if (i > getBitCount()-1)
            return false;

        boolean[] b = getBooleanArray();
        return b[i];
    }

    public String toString() {
        return "keyUsage: " + getValue();
    }

    public String getValue() {
        String s = "";
        boolean comma = false;
        String names[] =
            { "digitalSignature", "nonRepudiation", "keyEncipherment",
              "dataEncipherment", "keyAgreement", "keyCertSign",
              "cRLSign", "encipherOnly", "decipherOnly" };

        for (int i=0; i<names.length; i++) {
            if (getField(i)) {
                if (comma)
                    s += ",";
                s += names[i];
                comma = true;
            }
        }

        return s;
    }
}
