
package com.mxsecurity.asn1;

import java.io.UnsupportedEncodingException;

public class ASN1UTF8String extends ASN1CharString {

    public ASN1UTF8String() {
        super(ASN1.TAG_UTF8STRING);
    }

    public void setValue(String string) {
        try {
            byte[] tmp = string.getBytes("UTF8");
            setRaw(tmp);
        } catch (UnsupportedEncodingException e) {
            throw new Error("Can't handle UTF8String");
        }
    }

    public String getValue() {
        try {
            return new String(value, "UTF8");
        } catch (UnsupportedEncodingException e) {
            throw new Error("Can't handle UTF8String");
        }
    }

}

