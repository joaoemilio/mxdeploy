
package com.mxsecurity.asn1;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class ASN1Null extends ASN1Object {

    public ASN1Null() {
        super(ASN1.TAG_NULL);
    }

    public int encodeValue(ASN1Encoder encoder, OutputStream out)
    throws IOException {
        return encoder.encodeNull(out);
    }

    public void decodeValue(ASN1Decoder decoder, InputStream in, int len)
    throws IOException {
        setValue();
        decoder.decodeNull(in, len);
    }

}
