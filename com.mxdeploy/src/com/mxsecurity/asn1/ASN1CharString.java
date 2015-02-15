package com.mxsecurity.asn1;

public abstract class ASN1CharString extends ASN1String {

    protected ASN1CharString(int tag) {
        super(tag);
    }

    public abstract void setValue(String string);
    public abstract String getValue();

    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof ASN1CharString)) {
            return false;
        }
        ASN1CharString other = (ASN1CharString)obj;
        return getValue().equals(other);
    }

    public int hashCode() {
        int hc = super.hashCode();
        if(value != null) {
            hc = getValue().hashCode();
        }
        return hc;
    }

}
