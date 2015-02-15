package com.mxsecurity.asn1;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;

public class ASN1DER implements ASN1Encoder, ASN1Decoder {

    protected final static byte[] BOOL_TRUE  = { (byte)0xff };
    protected final static byte[] BOOL_FALSE = { (byte)0x00 };

    protected final static int INDEFINITE_LEN = -1;

    public int encode(OutputStream out, ASN1Object object) throws IOException {
        ByteArrayOutputStream encVal = new ByteArrayOutputStream();

        int tag = object.getTag();
        int len = object.encodeValue(this, encVal);
        int cnt = len;

        out.write((byte)tag);
        cnt++;

        cnt += encodeLength(out, len);

        encVal.writeTo(out);

        return cnt;
    }

    public int encodeLength(OutputStream out, int len) throws IOException {
        int cnt = 0;
        if(len < 0x80) {
            out.write((byte)len);
            cnt++;
        } else {
            byte[] encLen = new byte[5];
            int i = 4;
            do {
                encLen[i--] = (byte)(len & 0xff);
                len >>>= 8;
            } while(len > 0);
            int c = (5 - i);
            encLen[i] = (byte)(0x80 | (c - 1));
            out.write(encLen, i, c);
            cnt += c;
        }
        return cnt;
    }

    public int encodeBoolean(OutputStream out, boolean b) throws IOException {
        out.write(b ? BOOL_TRUE : BOOL_FALSE);
        return 1;
    }

    public int encodeInteger(OutputStream out, BigInteger i)
    throws IOException {
        byte[] enc = i.toByteArray();
        out.write(enc);
        return enc.length;
    }

    public int encodeNull(OutputStream out) throws IOException {
        return 0;
    }

    public int encodeOID(OutputStream out, int[] oid) throws IOException {
        int cnt = 1;
        out.write((40 * oid[0]) + oid[1]);
        for(int i = 2; i < oid.length; i++) {
            int v = oid[i];
            if(v < 0x80) {
                out.write((byte)v);
                cnt++;
            } else {
                byte[] b = new byte[4];
                int    j = 4;
                do {
                    j--;
                    b[j] = (byte)(v & 0x7f);
                    v >>>= 7;
                    if(j < 3) {
                        b[j] = (byte)(((int)b[j] & 0xff) | 0x80);
                    }
                } while(v > 0);
                while(j < 4) {
                    out.write(b[j++]);
                    cnt++;
                }
            }
        }
        return cnt;
    }

    public int encodeString(OutputStream out, byte[] string)
    throws IOException {
        out.write(string);
        return string.length;
    }

    public int encodeStructure(OutputStream out, ASN1Structure struct)
    throws IOException {
        int count = struct.getCount();
        int size  = 0;
        for(int i = 0; i < count; i++) {
            ASN1Object component = struct.getComponent(i);
            if(struct.isOptional(i)) {
                ASN1Object d = struct.getDefault(i);
                if(!component.isSet() || component.equals(d)) {
                    continue;
                }
            }
            size += encode(out, component);
        }
        return size;
    }

    public int decode(InputStream in, ASN1Object object) throws IOException {
        int   tag = decodeTag(in);
        int[] dl  = decodeLength(in);

        decodeValue(in, tag, dl[0], object);

        return 1 + dl[1] + dl[0];
    }

    public int decodeTag(InputStream in) throws IOException {
        int tag = in.read();
        if((tag & ASN1.MASK_NUMBER) == ASN1.MASK_NUMBER) {
            throw new IOException("Long form of tags not supported yet");
        }
        return tag;
    }

    public int[] decodeLength(InputStream in) throws IOException {
        int cnt = 1;
        int len = in.read();

        if(len > 0x80) {
            int n = (len & 0x7f);
            len = 0;
            for(int i = 0; i < n; i++) {
                len <<= 8;
                len += in.read();
                cnt++;
            }
        } else if(len == 0x80) {
            len = INDEFINITE_LEN;
        }

        return new int[] { len, cnt };
    }

    public void decodeValue(InputStream in, int tag, int len, ASN1Object object)
    throws IOException {
        if(((tag & ASN1.TYPE_CONSTRUCTED) != 0) &&
                (object instanceof ASN1String)) {
            ASN1ConstructedString cs =
                new ASN1ConstructedString(tag, object.getClass());
            cs.decodeValue(this, in, tag, len);
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            for(int i = 0; i < cs.getCount(); i++) {
                ASN1Object s = cs.getComponent(i);
                s.encodeValue(this, ba);
            }
            ((ASN1String)object).setRaw(ba.toByteArray());
        } else {
            object.decodeValue(this, in, tag, len);
        }
    }

    public boolean decodeBoolean(InputStream in, int len) throws IOException {
        byte b = (byte)in.read();
        if(len != 1 ||
                (b != BOOL_FALSE[0] && b != BOOL_TRUE[0])) {
            throw new IOException("Invalid DER encoding of boolean");
        }
        return (b == BOOL_TRUE[0]);
    }

    public BigInteger decodeInteger(InputStream in, int len)
    throws IOException {
        byte[] enc = new byte[len];
        readNextN(in, enc, len);

        return new BigInteger(enc);
    }

    public void decodeNull(InputStream in, int len) throws IOException {
        if(len != 0) {
            throw new IOException("Invalid DER encoding of NULL");
        }
    }

    public int[] decodeOID(InputStream in, int len) throws IOException {
        int[] oid = new int[len + 1];

        if(len < 1) {
            throw new IOException("Invalid DER encoding of OID");
        }

        oid[0] = in.read();
        len--;
        if(oid[0] > ((2 * 40) + 39)) {
            oid[1] = oid[0] - (2 * 40);
            oid[0] = 2;
        } else {
            oid[1] = oid[0] % 40;
            oid[0] = oid[0] / 40;
        }

        int i = 2;
        while(len > 0) {
            int v = 0;
            int d = in.read();
            len--;
            while(d > 0x80) {
                d &= 0x7f;
                v <<= 7;
                v |= d;
                d = in.read();
                len--;
            }
            v <<= 7;
            v |= d;
            oid[i++] = v;
        }

        int[] tmp = oid;
        oid = new int[i];
        System.arraycopy(tmp, 0, oid, 0, i);

        return oid;
    }

    public byte[] decodeString(InputStream in, int len) throws IOException {
        byte[] string = new byte[len];
        readNextN(in, string, len);
        return string;
    }

    private final class CountingInputStream extends InputStream {
        int pos;
        InputStream in;

        CountingInputStream(InputStream in) {
            pos = 0;
            this.in = in;
        }

        public int read() throws IOException {
            int res = in.read();
            pos++;
            return res;
        }

        public int read(byte[] b) throws IOException {
            return read(b, 0, b.length);
        }

        public int read(byte[] b, int off, int len)
        throws IOException {
            int res = in.read(b, off, len);
            if (res > 0)
                pos += res;
            return res;
        }
    }

    private void decodeStructureIndef(InputStream in, ASN1Structure struct)
    throws IOException {
        boolean endOfStruct = false;
        int     i           = 0;
        while(!endOfStruct) {
            int   tag = decodeTag(in);
            int[] dl  = decodeLength(in);
            if((tag == 0) && (dl[0] == 0)) {
                endOfStruct = true;
            } else {
                ASN1Object component = null;
                do {
                    component = struct.getDecodeComponent(i++, tag);
                } while(component == null);
                decodeValue(in, tag, dl[0], component);
            }
        }
    }

    public void decodeStructure(InputStream in, int len, ASN1Structure struct)
    throws IOException {

        if (len == INDEFINITE_LEN) {
            decodeStructureIndef(in, struct);
            return;
        }

        CountingInputStream cin;

        if (in instanceof CountingInputStream) {
            cin = (CountingInputStream)in;
        } else {
            cin = new CountingInputStream(in);
        }

        int i     = 0;
        while(len > 0) {
            int   tag = decodeTag(cin);
            int[] dl  = decodeLength(cin);

            len -= (1 + dl[1]);

            ASN1Object component = null;
            int pos = cin.pos;

            do {
                component = struct.getDecodeComponent(i++, tag);
            } while(component == null);
            decodeValue(cin, tag, dl[0], component);

            if (dl[0] < 0) {
                len -= (cin.pos - pos);
            } else {
                if ((cin.pos - pos) != dl[0]) {
                    throw new IOException("Component size mismatch: " + (cin.pos - pos) + " " + len);
                }
                len -= dl[0];
            }
        }
    }

    private void readNextN(InputStream in, byte[] buf, int n)
    throws IOException {
        int wPos = 0;
        while(wPos < n) {
            int s = in.read(buf, wPos, n - wPos);
            if(s == -1)
                throw new IOException("DER encoding ended prematurely");
            wPos += s;
        }
    }

}
