
package com.mxsecurity.asn1;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.math.BigInteger;

public class ASN1Integer extends ASN1Object {

    private BigInteger value;

    public ASN1Integer() {
        super(ASN1.TAG_INTEGER);
    }

    public int encodeValue(ASN1Encoder encoder, OutputStream out)
    throws IOException {
        return encoder.encodeInteger(out, value);
    }

    public void decodeValue(ASN1Decoder decoder, InputStream in, int len)
    throws IOException {
        setValue(decoder.decodeInteger(in, len));
    }

    public void setValue(BigInteger value) {
        setValue();
        this.value = value;
    }

    public void setValue(long value) {
        setValue(BigInteger.valueOf(value));
    }

    public BigInteger getValue() {
        return value;
    }

    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof ASN1Integer)) {
            return false;
        }
        ASN1Integer other = (ASN1Integer)obj;
        return value.equals(other);
    }

    public int hashCode() {
        int hc = super.hashCode();
        if(value != null) {
            hc = value.hashCode();
        }
        return hc;
    }

}
