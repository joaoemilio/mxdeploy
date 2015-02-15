
package com.mxsecurity.asn1;

public class ASN1IA5String extends ASN1CharString {

    public ASN1IA5String() {
        super(ASN1.TAG_IA5STRING);
    }

    // !!! TODO, should have check here, can't contain non-7-bit-ASCII !!!
    // How do we want to do this ???

    public void setValue(String string) {
        byte[] tmp = string.getBytes();
        setRaw(tmp);
    }

    public String getValue() {
        return new String(value);
    }

}
