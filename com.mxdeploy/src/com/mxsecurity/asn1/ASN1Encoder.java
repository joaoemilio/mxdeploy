
package com.mxsecurity.asn1;

import java.io.OutputStream;
import java.io.IOException;
import java.math.BigInteger;

public interface ASN1Encoder {
    public int encode(OutputStream out, ASN1Object object) throws IOException;
    public int encodeBoolean(OutputStream out, boolean b) throws IOException;
    public int encodeInteger(OutputStream out, BigInteger i) throws IOException;
    public int encodeNull(OutputStream out) throws IOException;
    public int encodeOID(OutputStream out, int[] oid) throws IOException;
    public int encodeString(OutputStream out, byte[] string) throws IOException;
    public int encodeStructure(OutputStream out, ASN1Structure struct)
    throws IOException;
}
