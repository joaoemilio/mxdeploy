
package com.mxsecurity.asn1;

public class ASN1TeletexString extends ASN1CharString {

    public ASN1TeletexString() {
        super(ASN1.TAG_TELETEXSTRING);
    }

    public void setValue(String string) {
        byte[] tmp = string.getBytes();
        setRaw(tmp);
    }

    public String getValue() {
        return new String(value);
    }

}
