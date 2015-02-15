
package com.mxsecurity.asn1;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public final class ASN1Implicit extends ASN1Object {

    private ASN1Object underlying;

    public ASN1Implicit(int tag, ASN1Object underlying) {
        this(tag, ASN1.CLASS_CONTEXT, underlying);
    }

    public ASN1Implicit(int tag, int cl, ASN1Object underlying) {
        super(tag | cl | (underlying.getTag() & ASN1.TYPE_CONSTRUCTED));
        this.underlying = underlying;
    }

    public void setValue() {
        underlying.setValue();
    }

    public boolean isSet() {
        return underlying.isSet();
    }

    public int encodeValue(ASN1Encoder encoder, OutputStream out)
    throws IOException {
        return underlying.encodeValue(encoder, out);
    }

    public void decodeValue(ASN1Decoder decoder, InputStream in,
                            int tag, int len)
    throws IOException {
        tag = ((tag & ASN1.TYPE_CONSTRUCTED) | underlying.getTag());
        decoder.decodeValue(in, tag, len, underlying);
    }

    public boolean equals(Object obj) {
        return underlying.equals(obj);
    }

    public int hashCode() {
        return underlying.hashCode();
    }

}
