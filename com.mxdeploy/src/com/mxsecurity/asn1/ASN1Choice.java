package com.mxsecurity.asn1;

import java.io.IOException;

import java.util.Hashtable;

public class ASN1Choice extends ASN1DynamicType {

    protected Hashtable members;

    public ASN1Choice() {
        super();
        members = new Hashtable();
    }

    public final void setMember(int id, Class ofType) {
        members.put(new Integer(id), ofType);
    }

    public final void setMember(int id, ASN1Object value) {
        members.put(new Integer(id), value);
    }

    public final void setMember(ASN1Object value) {
        members.put(new Integer(value.getTag()), value);
    }

    public final Object getMember(int tag) {
        return members.get(memberMapping(tag));
    }

    protected ASN1Object bindType(int tag) throws IOException {
        Object o = getMember(tag);
        if(o == null) {
            throw new IOException("Invalid member of " + getType() + " (" +
                                  tag + ")");
        }
        ASN1Object value = null;
        if(o instanceof Class) {
            try {
                Class c = (Class)o;
                value = (ASN1Object)c.newInstance();
            } catch (Exception e) {
                throw new IOException("Error decoding " + getType() + ": " +
                                      e.getMessage());
            }
        } else if(o instanceof ASN1Object) {
            value = (ASN1Object)o;
        } else {
            throw new IOException("Error decoding " + getType() +
                                  ", invalid member: " + o);
        }

        return value;
    }

    protected Object memberMapping(int tag) {
        return new Integer(tag);
    }

}

