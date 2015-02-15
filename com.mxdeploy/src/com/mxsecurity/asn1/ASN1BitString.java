package com.mxsecurity.asn1;


public class ASN1BitString extends ASN1String {

    public ASN1BitString() {
        super(ASN1.TAG_BITSTRING);
    }

    public int getBitCount() {
        return ((value.length - 1) * 8) - value[0];
    }

    public byte[] getBitArray() {
        byte[] ba = new byte[value.length - 1];
        System.arraycopy(value, 1, ba, 0, ba.length);
        return ba;
    }

    public void setBitArray(byte[] bitArray, int off, int bitCount) {
        int    unusedBits = (8 - (bitCount % 8)) % 8;
        int    byteCount  = ((bitCount / 8) + 1);
        byte[] derEnc     = new byte[byteCount + 1];

        derEnc[0] = (byte)unusedBits;
        System.arraycopy(bitArray, off, derEnc, 1, byteCount);

        setRaw(derEnc);
    }

    public boolean[] getBooleanArray() {
        int       bitCount = ((value.length - 1) * 8) - value[0];
        boolean[] bits     = new boolean[bitCount];

        for(int i = 0; i < bitCount; i++) {
            int m = 0x80 >>> (i % 8);
            bits[i] = ((((int)value[(i / 8) + 1] & 0xff) & m) == m);
        }

        return bits;
    }

    public void setBooleanArray(boolean[] bits) {
        int    bitCount   = bits.length;
        int    unusedBits = (8 - (bitCount % 8)) % 8;
        int    byteCount  = ((bitCount / 8) + 1);
        byte[] derEnc     = new byte[byteCount + 1];

        derEnc[0] = (byte)unusedBits;
        for(int i = 0; i < bitCount; i++) {
            int m = 0x80 >>> (i % 8);
            derEnc[(i / 8) + 1] |= (bits[i] ? m : 0x00);
        }
        setRaw(derEnc);
    }

    public String toPrintableString() {
        for (int i=0; i<value.length; i++) {
            if (value[i] < 10 || value[i] > 127)
                return "0x" + com.mxsecurity.util.HexDump.toString(value);
        }
        return new String(value);
    }
}
