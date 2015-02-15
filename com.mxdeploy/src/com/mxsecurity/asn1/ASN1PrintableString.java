
package com.mxsecurity.asn1;

public class ASN1PrintableString extends ASN1CharString {

    public ASN1PrintableString() {
        super(ASN1.TAG_PRINTABLESTRING);
    }

    // !!! TODO, should have check here, this is only a subset of Teletex
    // How do we want to do this ???

    public void setValue(String string) {
        byte[] tmp = string.getBytes();
        setRaw(tmp);
    }

    public String getValue() {
        return new String(value);
    }

}
