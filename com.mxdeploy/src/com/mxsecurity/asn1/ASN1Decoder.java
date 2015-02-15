package com.mxsecurity.asn1;

import java.io.InputStream;
import java.io.IOException;
import java.math.BigInteger;

public interface ASN1Decoder {
    public int        decode(InputStream in, ASN1Object object)
    throws IOException;
    public void decodeValue(InputStream in, int tag, int len,
                            ASN1Object object) throws IOException;
    public boolean    decodeBoolean(InputStream in, int len) throws IOException;
    public BigInteger decodeInteger(InputStream in, int len) throws IOException;
    public void       decodeNull(InputStream in, int len) throws IOException;
    public int[]      decodeOID(InputStream in, int len) throws IOException;
    public byte[]     decodeString(InputStream in, int len) throws IOException;
    public void       decodeStructure(InputStream in, int len,
                                      ASN1Structure struct) throws IOException;
}
