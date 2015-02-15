package com.mxsecurity.asn1;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public abstract class ASN1DynamicType extends ASN1Object {

    protected ASN1Object value;

    protected ASN1DynamicType() {
        super(0);
    }

    public final void setValue(ASN1Object value) {
        this.value = value;
        this.tag   = value.getTag();
        setValue();
    }

    public final ASN1Object getValue() {
        return value;
    }

    public String getType() {
        if(value != null) {
            return value.getType();
        }
        return "Unbound dynamic type";
    }

    public void setValue() {
        super.setValue();
        if(value != null) {
            value.setValue();
        }
    }

    public int encodeValue(ASN1Encoder encoder, OutputStream out)
    throws IOException {
        if(value == null) {
            throw new IOException("Value of " + getType() + " not set");
        }
        return value.encodeValue(encoder, out);
    }

    public void decodeValue(ASN1Decoder decoder, InputStream in,
                            int tag, int len)
    throws IOException {
        ASN1Object value = bindType(tag);
        setValue(value);
        decoder.decodeValue(in, (value.tag != 0) ? value.tag : tag , len, value);
    }

    protected abstract ASN1Object bindType(int tag) throws IOException;

}
