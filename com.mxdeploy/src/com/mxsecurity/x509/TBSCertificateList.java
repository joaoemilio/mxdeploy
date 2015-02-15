
package com.mxsecurity.x509;

import com.mxsecurity.asn1.ASN1Explicit;
import com.mxsecurity.asn1.ASN1Integer;
import com.mxsecurity.asn1.ASN1Sequence;
import com.mxsecurity.asn1.ASN1SequenceOf;

public class TBSCertificateList extends ASN1Sequence {

    public ASN1Integer          version;
    public AlgorithmIdentifier  signature;
    public Name                 issuer;
    public Time                 thisUpdate;
    public Time                 nextUpdate;
    public ASN1SequenceOf       revokedCertificates;
    public Extensions           extensions;

    public TBSCertificateList() {
        version              = new ASN1Integer();
        signature            = new AlgorithmIdentifier();
        issuer               = new Name();
        thisUpdate           = new Time();
        nextUpdate           = new Time();
        revokedCertificates  = new ASN1SequenceOf(RevokedCertificate.class);
        extensions           = new Extensions();

        addOptional(version);
        addComponent(signature);
        addComponent(issuer);
        addComponent(thisUpdate);
        addOptional(thisUpdate);
        addOptional(revokedCertificates);
        addOptional(new ASN1Explicit(0, extensions));
    }

    //     public String toString() {
    //         String s =
    //             "version= " + (version.isSet() ? version.getValue().toString() : "<not set>") + NL +
    //             "signature= " + signature.algorithmName() + NL +
    //             "issuer= " + issuer.getRFC2253Value() + NL +
    //             "thisUpdate= " + thisUpdate + NL +
    //             "nextUpdate= " + (nextUpdate.isSet() ? nextUpdate.toString() : "<not set>") + NL;

    //         s += "revokedCertificates= " + NL;
    //         for (int i=0; i<revokedCertificates.getCount(); i++) {
    //             s += "[" + i + "]: " + ((RevokedCertificate)revokedCertificates.getComponent(i)).toString() + NL;
    //         }

    //         s += "extensions= ...";
    //         return s;
    //     }
}
