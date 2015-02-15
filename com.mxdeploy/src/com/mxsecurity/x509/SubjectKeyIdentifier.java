package com.mxsecurity.x509;

import com.mxsecurity.asn1.ASN1OctetString;


/**
 * Represents a SubjectKeyIdentifier.
 *
 * <pre>
 *  SubjectKeyIdentifier ::= KeyIdentifier
 *  KeyIdentifier ::= OCTET STRING
 * </pre>
 */

public class SubjectKeyIdentifier extends ASN1OctetString {

    public SubjectKeyIdentifier() {
        super();
    }

    public String toString() {
        byte[] b = getRaw();
        return "subjectKeyIdentifier: 0x" + com.mxsecurity.util.HexDump.toString(b, 0, b.length);
    }
}
