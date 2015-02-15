package com.mxterminal.ssh;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import com.mxsecurity.publickey.RSAPublicKey;

public class RSAPublicKeyStringSSH extends RSAPublicKey {

    String user;
    String opts;

    public RSAPublicKeyStringSSH(String opts, String user, BigInteger e, BigInteger n) {
        super(n, e);
        this.opts = opts;
        this.user = user;
    }

    public static RSAPublicKeyStringSSH createKey(String opts, String pubKey) throws NoSuchElementException {
        StringTokenizer tok  = new StringTokenizer(pubKey);
        String          user = null;
        String bits;
        String e;
        String n;

        bits = tok.nextToken();
        e    = tok.nextToken();
        n    = tok.nextToken();
        if(tok.hasMoreElements())
            user = tok.nextToken();

        return new RSAPublicKeyStringSSH(opts, user, new BigInteger(e), new BigInteger(n));
    }

    public String getOpts() {
        return opts;
    }

    public String getUser() {
        return user;
    }

    public String toString() {
        int bitLen = getModulus().bitLength();
        return ((opts != null ? (opts + " ") : "") +
                bitLen + " " + getPublicExponent() + " " + getModulus() + " " +
                (user != null ? user : ""));
    }

    public void toFile(String fileName) throws IOException {
        FileOutputStream    fileOut = new FileOutputStream(fileName);
        DataOutputStreamSSH dataOut = new DataOutputStreamSSH(fileOut);
        dataOut.writeBytes(toString());
        dataOut.close();
    }

}
