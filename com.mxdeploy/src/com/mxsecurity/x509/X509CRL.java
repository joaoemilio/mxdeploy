
package com.mxsecurity.x509;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;

import com.mxsecurity.asn1.ASN1DER;
import com.mxsecurity.asn1.ASN1OIDRegistry;

public class X509CRL
    extends com.mxsecurity.jca.cert.CRL {
    private CertificateList certList;

    public X509CRL(byte[] encoded) {
        this(new ByteArrayInputStream(encoded));
    }

    public X509CRL(InputStream in) {
        super("X.509");
        init(in);
    }

    private void init(InputStream in) {
        this.certList = new CertificateList();

        ASN1OIDRegistry.addModule("com.mxsecurity.x509");
        ASN1OIDRegistry.addModule("com.mxsecurity.pkcs1");

        try {
            (new ASN1DER()).decode(in, certList);
        } catch (IOException e) {
            throw new Error("Internal error decoding DER encoded X.509 CRL: " +
                            e.getMessage());
        }
    }

    public boolean isRevoked(com.mxsecurity.jca.cert.Certificate cert) {
        // TODO
        return true;
    }

    public String toString() {
        return "X509CRL: certList=" + certList;
    }
}
