package com.mxsecurity.asn1;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class ASN1Boolean extends ASN1Object {

    private boolean value;

    public ASN1Boolean() {
        super(ASN1.TAG_BOOLEAN);
    }

    public int encodeValue(ASN1Encoder encoder, OutputStream out)
    throws IOException {
        return encoder.encodeBoolean(out, value);
    }

    public void decodeValue(ASN1Decoder decoder, InputStream in, int len)
    throws IOException {
        setValue(decoder.decodeBoolean(in, len));
    }

    public void setValue(boolean value) {
        setValue();
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

}
