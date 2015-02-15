package com.mxsecurity.jca;

public abstract class MessageDigestSpi {

    public MessageDigestSpi() {}

    protected int engineGetDigestLength() {
        return 0;
    }

    protected abstract byte[] engineDigest();

    protected int engineDigest(byte[] buf, int offset, int len)
    throws DigestException {
        byte[] digest = engineDigest();
        if(len < digest.length) {
            throw new DigestException("MessageDigestSpi, buffer too short");
        }
        System.arraycopy(digest, 0, buf, offset, digest.length);
        return digest.length;
    }

    protected abstract void engineReset();

    protected abstract void engineUpdate(byte input);

    protected abstract void engineUpdate(byte[] input, int offset, int len);

    public Object clone() throws CloneNotSupportedException {
        if(this instanceof Cloneable) {
            return super.clone();
        } else {
            throw new CloneNotSupportedException();
        }
    }

}
