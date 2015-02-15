package com.mxterminal.ssh2.key;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import com.mxsecurity.jca.GeneralSecurityException;
import com.mxsecurity.jca.MessageDigest;
import com.mxsecurity.jce.Cipher;
import com.mxsecurity.jce.Mac;
import com.mxsecurity.jce.spec.SecretKeySpec;
import com.mxsecurity.util.HexDump;
import com.mxterminal.ssh2.DataBufferSSH2;
import com.mxterminal.ssh2.exception.FatalExceptionSSH2;
import com.mxterminal.util.Base64;




/**
 * This class implements support for reading the PuTTY key file format.
 * 
 */

public class PuttyKeyFile {

    private boolean version1;
    private String format;
    private String encryption;
    private String comment;
    private String pubblob;
    private byte[] pubbytes;
    private String privblob;
    private byte[] privbytes;
    private String privmacorhash;
    private boolean isprivmac;

    public PuttyKeyFile(InputStream in) 
	throws IOException {
	super();
	load(in);
    }

    public void load(InputStream in) 
	throws IOException {

	BufferedReader r = new BufferedReader(new InputStreamReader(in));
	StringTokenizer st = new StringTokenizer(r.readLine());
	String s = st.nextToken();
	version1 = s.endsWith("-1:");
	format = st.nextToken();
	st = new StringTokenizer(r.readLine());
	st.nextToken();
	encryption = st.nextToken();
	st = new StringTokenizer(r.readLine());
	st.nextToken();
	comment = st.nextToken();
	st = new StringTokenizer(r.readLine());
	st.nextToken();
	s = st.nextToken();
	int i, no = Integer.parseInt(s);
	pubblob = "";
	for (i=0; i<no; i++)
	    pubblob += r.readLine();
        pubbytes = Base64.decode(pubblob.getBytes());
	st = new StringTokenizer(r.readLine());
	st.nextToken();
	s = st.nextToken();
	no = Integer.parseInt(s);
	privblob = "";
	for (i=0; i<no; i++)
	    privblob += r.readLine();
	st = new StringTokenizer(r.readLine());
        isprivmac = st.nextToken().equals("Private-MAC:");        
	privmacorhash = st.nextToken();
    }

    public boolean validate(String passphrase) throws FatalExceptionSSH2 {
        // decrypt private key blob and check mac
        byte[] privbytesenc = Base64.decode(privblob.getBytes());

        if (encryption.equals("none")) {
            privbytes = privbytesenc;
        } else if (encryption.equals("aes256-cbc")) {
            try {
                byte pass[];
                byte key[] = new byte[40];
                MessageDigest sha1 = MessageDigest.getInstance("SHA1");
                if (passphrase == null) {
                    pass = new byte[0];
                } else {
                    pass = passphrase.getBytes();
                }
                sha1.update(new byte[] {0,0,0,0});
                sha1.update(pass);
                sha1.digest(key, 0, 20);
                sha1.reset();
                sha1.update(new byte[] {0,0,0,1});
                sha1.update(pass);
                sha1.digest(key, 20, 20);
                
                Cipher cipher = Cipher.getInstance("AES/CBC");
                cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, 0, 32, cipher.getAlgorithm()));
                privbytes = cipher.doFinal(privbytesenc);
            } catch (GeneralSecurityException e) {
                throw new FatalExceptionSSH2("Failed to decrypt PuTTY private key");
            }
        } else {
            throw new FatalExceptionSSH2("Failed to decrypt PuTTY private key - " + 
                                         "unsupported encryption type: " + encryption);
        }

        byte[] data;
        
        if (version1) {
            data = privbytes;
        } else {
            DataBufferSSH2 b = new DataBufferSSH2(32768);
            b.writeString(format);
            b.writeString(encryption);
            b.writeString(comment);
            b.writeString(pubbytes);
            b.writeString(privbytes);
            data = b.readRestRaw();
        }

        byte[] hash = null;

        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA1");
            if (isprivmac) {
                sha1.update("putty-private-key-file-mac-key".getBytes());
                if (!encryption.equals("none") && passphrase != null)
                    sha1.update(passphrase.getBytes());
                byte[] key = sha1.digest();
                Mac mac = Mac.getInstance("HmacSHA1");
                mac.init(new SecretKeySpec(key, 0, 20, mac.getAlgorithm()));
                hash = mac.doFinal(data);
            } else {
                hash = sha1.digest(data);
            }
        } catch (GeneralSecurityException e) {
            throw new FatalExceptionSSH2("Failed to calculate hash for PuTTY key file");
        }
        
	return hash != null && privmacorhash.equals(HexDump.toString(hash));
    }

    public byte[] getPublicKeyBlob() {
	return pubbytes;
    }

    public byte[] getPrivateKeyBlob() {
	return privbytes;
    }

    public String getFormat() {
	return format;
    }

    public String getComment() {
	return comment;
    }

    public String toString() {
	return 
	    "version1=" + version1 + ",format=" + format + 
	    ",encryption=" + encryption + 
	    ",comment=" + comment + ",pubblob=" + pubblob +
	    ",privblob=" + privblob + ",privmacorhash=" + privmacorhash +
            ",isprivmac=" + isprivmac;
    }
}
