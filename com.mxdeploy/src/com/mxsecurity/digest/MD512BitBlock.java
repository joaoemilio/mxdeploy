package com.mxsecurity.digest;

import com.mxsecurity.jca.DigestException;
import com.mxsecurity.jca.MessageDigest;

public abstract class MD512BitBlock extends MessageDigest implements Cloneable {
    protected long   count;
    protected int    rest;
    protected byte[] buffer;
    protected int    digestLength;

    protected final static byte md4_padding[] = {
                (byte) 0x80, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
            };

    protected final static int rotateLeft(int x, int n) {
        return (x << n) | (x >>> (32 - n));
    }

    protected MD512BitBlock(String algorithm, int digestLength) {
        super(algorithm);
        buffer = new byte[64];
        count  = 0;
        rest   = 0;
        this.digestLength = digestLength;
    }

    public final Object clone() {
        MD512BitBlock c = cloneInternal();
        System.arraycopy(c.buffer, 0, buffer, 0, this.rest);
        c.count = this.count;
        c.rest  = this.rest;
        return c;
    }

    protected final void engineUpdate(byte input) {
        engineUpdate(new byte[] { input }, 0, 1);
    }

    protected final void engineUpdate(byte[] data, int offset, int length) {
        int left = 64 - rest;

        count += length;

        if(rest > 0 && length >= left) {
            System.arraycopy(data, offset, buffer, rest, left);
            transform(buffer, 0);
            offset += left;
            length -= left;
            rest   =  0;
        }

        while(length > 63) {
            transform(data, offset);
            offset += 64;
            length -= 64;
        }

        if(length > 0) {
            System.arraycopy(data, offset, buffer, rest, length);
            rest += length;
        }
    }

    protected final byte[] engineDigest() {
        byte[] buf = new byte[digestLength];
        try {
            engineDigest(buf, 0, buf.length);
        } catch(DigestException e) {
            /* Can't happen... */
        }
        return buf;
    }

    protected final int engineGetDigestLength() {
        return digestLength;
    }

    protected abstract void transform(byte data[], int offset);
    protected abstract MD512BitBlock cloneInternal();

}
