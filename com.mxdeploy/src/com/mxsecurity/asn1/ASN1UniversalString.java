
package com.mxsecurity.asn1;

import java.io.UnsupportedEncodingException;

public class ASN1UniversalString extends ASN1CharString {

    public ASN1UniversalString() {
        super(ASN1.TAG_UNIVERSALSTRING);
    }

    public void setValue(String string) {
        try {
            byte[] tmp = string.getBytes("ISO-10646");
            setRaw(tmp);
        } catch (UnsupportedEncodingException e) {
            throw new Error("Can't handle UniversalString");
        }
    }

    public String getValue() {
        try {
            return new String(value, "ISO-10646");
        } catch (UnsupportedEncodingException e) {
            throw new Error("Can't handle UniversalString");
        }
    }

}
