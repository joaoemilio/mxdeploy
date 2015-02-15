package com.mxsecurity.pkcs1;

import java.math.BigInteger;

import com.mxsecurity.asn1.ASN1Integer;
import com.mxsecurity.asn1.ASN1Sequence;

/**
 * Represents a DSA private key.
 *
 * <pre>
 *  Dss-Parms ::= SEQUENCE {
 *    p INTEGER,
 *    q INTEGER,
 *    g INTEGER
 * }
 * </pre>
 */
public class DSAParams extends ASN1Sequence {

    public ASN1Integer p;
    public ASN1Integer q;
    public ASN1Integer g;

    public DSAParams() {
        p = new ASN1Integer();
        q = new ASN1Integer();
        g = new ASN1Integer();
        addComponent(p);
        addComponent(q);
        addComponent(g);
    }

    public DSAParams(BigInteger p, BigInteger q, BigInteger g) {
        this();
        this.p.setValue(p);
        this.q.setValue(q);
        this.g.setValue(g);
    }
}
