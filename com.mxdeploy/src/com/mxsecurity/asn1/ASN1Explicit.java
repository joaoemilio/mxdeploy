
package com.mxsecurity.asn1;

public final class ASN1Explicit extends ASN1Structure {

    private ASN1Object underlying;

    public ASN1Explicit(int tag, ASN1Object underlying) {
        this(tag, ASN1.CLASS_CONTEXT, underlying);
    }

    public ASN1Explicit(int tag, int cl, ASN1Object underlying) {
        super(tag | cl);
        this.underlying = underlying;
        addComponent(underlying);
    }

    public void setValue() {
        underlying.setValue();
    }

    public boolean isSet() {
        return underlying.isSet();
    }

    public boolean equals(Object obj) {
        return underlying.equals(obj);
    }

    public int hashCode() {
        return underlying.hashCode();
    }

}
