package com.mxsecurity.asn1;

import java.io.IOException;

public class ASN1AnyDefinedBy extends ASN1DynamicType {

    private ASN1OID definedById;
    private boolean fallBackToAny = false;

    public ASN1AnyDefinedBy(ASN1OID definedById) {
        this(definedById, false);
    }

    public ASN1AnyDefinedBy(ASN1OID definedById, boolean fallBackToAny) {
        super();
        this.definedById = definedById;
        this.fallBackToAny = fallBackToAny;
    }

    protected ASN1Object bindType(int tag) throws IOException {
        if(definedById.getValue() == null) {
            throw new IOException("Unresolved ANY DEFINED BY, OID not set");
        }
        Class type = ASN1OIDRegistry.lookupType(definedById);
        if(type == null) {
            if (fallBackToAny)
                type = ASN1Any.class;
            else
                throw new IOException("Unknown member of " + getType() + " (" +
                                      definedById.getString() + ")");
        }
        ASN1Object value = null;
        try {
            value = (ASN1Object)type.newInstance();
        } catch (Exception e) {
            throw new IOException("Error decoding " + getType() + ": " +
                                  e.getMessage());
        }

        return value;
    }

}
