package com.mxsecurity.x509;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;

import com.mxsecurity.x509.Extension;
import com.mxsecurity.asn1.ASN1DER;
import com.mxsecurity.asn1.ASN1OIDRegistry;
import com.mxsecurity.asn1.ASN1Object;
import com.mxsecurity.jca.InvalidKeyException;
import com.mxsecurity.jca.KeyFactory;
import com.mxsecurity.jca.NoSuchAlgorithmException;
import com.mxsecurity.jca.NoSuchProviderException;
import com.mxsecurity.jca.PublicKey;
import com.mxsecurity.jca.SignatureException;
import com.mxsecurity.jca.cert.CertificateEncodingException;
import com.mxsecurity.jca.cert.CertificateException;
import com.mxsecurity.jca.spec.DSAPublicKeySpec;
import com.mxsecurity.jca.spec.RSAPublicKeySpec;
import com.mxsecurity.pkcs1.DSAParams;
import com.mxsecurity.pkcs1.DSAPublicKey;
import com.mxsecurity.pkcs1.RSAPublicKey;

public class X509Certificate
    extends com.mxsecurity.jca.cert.Certificate {
    private byte[]      encoded;
    private Certificate certificate;

    public X509Certificate(byte[] encoded) {
        super("X.509");
        this.encoded     = encoded;
        this.certificate = new Certificate();

        ASN1OIDRegistry.addModule("com.mxsecurity.x509");
        ASN1OIDRegistry.addModule("com.mxsecurity.pkcs1");

        try {
            ASN1DER              der = new ASN1DER();
            ByteArrayInputStream ba  = new ByteArrayInputStream(encoded);
            der.decode(ba, certificate);
        } catch (IOException e) {
            throw new Error("Internal error decoding DER encoded X.509 cert: " +
                            e.getMessage());
        }
    }

    public byte[] getEncoded() throws CertificateEncodingException {
        return encoded;
    }

    public void verify(PublicKey key)
    throws CertificateException, NoSuchAlgorithmException,
        InvalidKeyException, NoSuchProviderException, SignatureException {}

    public void verify(PublicKey key, String sigProvider)
    throws CertificateException, NoSuchAlgorithmException,
        InvalidKeyException, NoSuchProviderException, SignatureException {}

    public String toString() {
        return "X509 Certificate: \n" +
               "  subject: "    + getSubjectDN()    + "\n" +
               "  issuer: "     + getIssuerDN()     + "\n" +
               "  version: "    + (getVersion()+1)  + "\n" +
               "  pubalg: "     + getPubAlgName()   + "\n" +
               "  serialNo: "   + getSerialNumber() + "\n\n" +
               " Extensions:\n\n" + getExtensions();
    }

    public String getIssuerDN() {
        return certificate.tbsCertificate.issuer.getRFC2253Value();
    }

    public String getSubjectDN() {
        return certificate.tbsCertificate.subject.getRFC2253Value();
    }

    public BigInteger getSerialNumber() {
        return certificate.tbsCertificate.serialNumber.getValue();
    }

    public String getPubAlgName() {
        return certificate.tbsCertificate.subjectPublicKeyInfo.algorithm.algorithmName();
    }

    private ASN1Object getExtensionWithOID(String oid, Class c) {
        try {
            Extensions es = certificate.tbsCertificate.extensions;
            for (int i=0; i<es.getCount(); i++) {
                Extension e = (Extension)es.getComponent(i);
                if (e.extnID.getString().equals(oid)) {
                    ASN1DER der = new ASN1DER();
                    ByteArrayInputStream ba  = new ByteArrayInputStream(e.extnValue.getRaw());
                    ASN1Object obj = (ASN1Object)c.newInstance();
                    der.decode(ba, obj);
                    return obj;
                }
            }
        } catch (Throwable t) {}
        return null;
    }

    public SubjectKeyIdentifier getSubjectKeyIdentifier() {
        return (SubjectKeyIdentifier)getExtensionWithOID
               ("2.5.29.14", SubjectKeyIdentifier.class);
    }

    public KeyUsage getKeyUsage() {
        return (KeyUsage)getExtensionWithOID
               ("2.5.29.15", KeyUsage.class);
    }

    public BasicConstraints getBasicConstraints() {
        return (BasicConstraints)getExtensionWithOID
               ("2.5.29.19", BasicConstraints.class);
    }

    public String getExtensions() {
        String s = "";
        try {
            Extensions es = certificate.tbsCertificate.extensions;
            for (int i=0; i<es.getCount(); i++) {
                Extension e = (Extension)es.getComponent(i);
                String oid = e.extnID.getString();
                String crit = e.critical.getValue() ? "yes" : "no ";

                String val;

                if (oid.equals("2.5.29.14")) {
                    val = getKeyUsage().toString();
                } else if (oid.equals("2.5.29.15")) {
                    val = getSubjectKeyIdentifier().toString();
                } else if (oid.equals("2.5.29.19")) {
                    val = getBasicConstraints().toString();
                } else {
                    val = oid;
                }

                s += "  critical: " + crit + " " + val + "\n";
            }
        } catch (Throwable t) {}

        return s;
    }

    public int getVersion() {
        int ver = 0;
        try {
            ver = certificate.tbsCertificate.version.getValue().intValue();
        } catch (Throwable t) {}
        return ver;
    }

    public PublicKey getPublicKey() {
        SubjectPublicKeyInfo spki =
            certificate.tbsCertificate.subjectPublicKeyInfo;
        String alg = spki.algorithm.algorithmName().toUpperCase();
        ASN1DER              der = new ASN1DER();
        if (alg.startsWith("RSA")) {
            RSAPublicKey         rsa = new RSAPublicKey();
            ByteArrayInputStream ba  = new ByteArrayInputStream(
                                           spki.subjectPublicKey.getBitArray());
            try {
                der.decode(ba, rsa);
            } catch (Exception e) {
                throw new Error("Internal error decoding SubjectPublicKeyInfo.subjectPublicKey: " +
                                e.getMessage());
            }

            try {
                KeyFactory       keyFact = KeyFactory.getInstance("RSA");
                RSAPublicKeySpec pubSpec =
                    new RSAPublicKeySpec(rsa.modulus.getValue(),
                                         rsa.publicExponent.getValue());
                return keyFact.generatePublic(pubSpec);

            } catch (Exception e) {
                throw new Error("Error creating RSA key: " + e.getMessage());
            }

        } else if (alg.startsWith("DSA")) {
            DSAPublicKey         dsa = new DSAPublicKey();
            ByteArrayInputStream ba  = new ByteArrayInputStream(
                                           spki.subjectPublicKey.getBitArray());
            try {
                der.decode(ba, dsa);
            } catch (Exception e) {
                throw new Error("Internal error decoding SubjectPublicKeyInfo.subjectPublicKey: " +
                                e.getMessage());
            }
            BigInteger y = dsa.getValue();

            DSAParams dsaParams =
                (DSAParams) spki.algorithm.parameters.getValue();

            BigInteger p = dsaParams.p.getValue();
            BigInteger q = dsaParams.q.getValue();
            BigInteger g = dsaParams.g.getValue();

            try {
                KeyFactory       dsaKeyFact = KeyFactory.getInstance("DSA");
                DSAPublicKeySpec dsaPubSpec = new DSAPublicKeySpec(y, p, q, g);
                return dsaKeyFact.generatePublic(dsaPubSpec);

            } catch (Exception e) {
                throw new Error("Error creating DSA key: " + e.getMessage());
            }
        } else {
            throw new Error("Internal error decoding publicKey: unknown algorithm");
        }
    }
}

