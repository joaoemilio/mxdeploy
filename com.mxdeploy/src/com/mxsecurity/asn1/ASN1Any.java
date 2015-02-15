package com.mxsecurity.asn1;

import java.io.InputStream;
import java.io.IOException;

public class ASN1Any extends ASN1Choice {

    public static class ASN1SequenceOfAny extends ASN1SequenceOf {
        public ASN1SequenceOfAny() {
            super(ASN1Any.class);
        }
    }

    public static class ASN1SetOfAny extends ASN1SetOf {
        public ASN1SetOfAny() {
            super(ASN1Any.class);
        }
    }

    public static class ASN1AnyStructure extends ASN1Structure {
        public ASN1AnyStructure() {
            super(0);
            addComponent(new ASN1Any());
        }

        public void decodeValue(ASN1Decoder decoder, InputStream in,
                                int tag, int len)
        throws IOException {
            decodeValue(decoder, in, len);
        }
    }

    public ASN1Any() {
        super();
        setMember(ASN1.TAG_BOOLEAN,     ASN1Boolean.class);
        setMember(ASN1.TAG_INTEGER,     ASN1Integer.class);
        setMember(ASN1.TAG_BITSTRING,   ASN1BitString.class);
        setMember(ASN1.TAG_OCTETSTRING, ASN1OctetString.class);
        setMember(ASN1.TAG_NULL,        ASN1Null.class);
        setMember(ASN1.TAG_OID,         ASN1OID.class);
        setMember(ASN1.TAG_SEQUENCE | ASN1.TYPE_CONSTRUCTED,
                  ASN1SequenceOfAny.class);
        setMember(ASN1.TAG_SET | ASN1.TYPE_CONSTRUCTED,
                  ASN1SetOfAny.class);
        setMember(ASN1.TAG_IA5STRING,    ASN1IA5String.class);
        setMember(ASN1.TAG_BMPSTRING,    ASN1BMPString.class);
        setMember(ASN1.TAG_PRINTABLESTRING, ASN1PrintableString.class);
        setMember(ASN1.TYPE_CONSTRUCTED, ASN1AnyStructure.class);
    }

    protected Object memberMapping(int tag) {
        Object o = super.memberMapping(tag);
        if(o == null) {
            tag &= (ASN1.MASK_NUMBER | ASN1.TYPE_CONSTRUCTED);
            o = super.memberMapping(tag);
        }
        if(o == null) {
            tag &= ASN1.MASK_NUMBER;
            o = super.memberMapping(tag);
        }
        return o;
    }

}
