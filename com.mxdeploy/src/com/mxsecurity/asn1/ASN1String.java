
package com.mxsecurity.asn1;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class ASN1String extends ASN1Object {

    protected byte[] value;

    public ASN1String(int tag) {
        super(tag);
    }

    public int encodeValue(ASN1Encoder encoder, OutputStream out)
    throws IOException {
        return encoder.encodeString(out, value);
    }

    public void decodeValue(ASN1Decoder decoder, InputStream in, int len)
    throws IOException {
        setRaw(decoder.decodeString(in, len));
    }

    public void setRaw(byte[] value) {
        setValue();
        this.value = value;
    }

    public byte[] getRaw() {
        return value;
    }

}
