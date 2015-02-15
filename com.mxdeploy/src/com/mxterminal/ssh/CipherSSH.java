package com.mxterminal.ssh;

import com.mxsecurity.jca.MessageDigest;

public abstract class CipherSSH {

    public static CipherSSH getInstance(String algorithm) {
        Class c;
        try {
            c = Class.forName("com.mxterminal.ssh." + algorithm);
            return (CipherSSH)c.newInstance();
        } catch(Throwable t) {
        	t.printStackTrace();
            return null;
        }
    }

    public abstract void encrypt(byte[] src, int srcOff,
                                 byte[] dest, int destOff, int len);
    public abstract void decrypt(byte[] src, int srcOff,
                                 byte[] dest, int destOff, int len);
    public abstract void setKey(byte[] key);

    public byte[] encrypt(byte[] src) {
        byte[] dest = new byte[src.length];
        encrypt(src, 0, dest, 0, src.length);
        return dest;
    }

    public byte[] decrypt(byte[] src) {
        byte[] dest = new byte[src.length];
        decrypt(src, 0, dest, 0, src.length);
        return dest;
    }

    public void setKey(String key) {
        MessageDigest md5;
        byte[] mdKey = new byte[32];
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(key.getBytes());
            byte[] digest = md5.digest();
            System.arraycopy(digest, 0, mdKey, 0, 16);
            System.arraycopy(digest, 0, mdKey, 16, 16);
        } catch(Exception e) {
            // !!!
            System.out.println("MD5 not implemented, can't generate key out of string!");
            System.exit(1);
        }
        setKey(mdKey);
    }

}
