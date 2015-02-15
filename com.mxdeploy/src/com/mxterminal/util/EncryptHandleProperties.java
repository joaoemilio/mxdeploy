package com.mxterminal.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;

import com.mxsecurity.jca.MessageDigest;
import com.mxterminal.ssh.CipherSSH;
import com.mxterminal.ssh.exception.AccessDeniedException;

// !!! TODO, change this to real cipher

/**
 * Handles an encrypted properties file.
 */
public class EncryptHandleProperties extends Properties {
    /**
     * Property holding a hash of the encrypted data
     */
    public final static String HASH_KEY     = "EncryptedProperties.hash";

    /**
     * Property holding the name of the encryption algorithm used
     */
    public final static String CIPHER_KEY   = "EncryptedProperties.cipher";

    /**
     * Property holding the encrypted content
     */
    public final static String CONTENTS_KEY = "EncryptedProperties.contents";

    /**
     * Property holding the size of the encrypted content
     */
    public final static String SIZE_KEY     = "EncryptedProperties.size";

    /**
     * Property identifying the file
     */
    public final static String PROPS_HEADER = "Sealed with com.mxterminal.util.EncryptedProperties" +
            "(ver. $Name: v3_1_2 $" + "$Date: 2005/11/17 14:03:05 $)";

    private boolean isNormalPropsFile;

    /**
     * Create a new instance of EncryptedProperties without any
     * default values.
     */
    public EncryptHandleProperties() {
        super();
        isNormalPropsFile = false;
    }

    /**
     * Create a new instance of EncryptedProperties with the given
     * default values.
     */
    public EncryptHandleProperties(Properties defaultProperties) {
        super(defaultProperties);
        isNormalPropsFile = false;
    }

    /**
     * Checks if the underlying properties file was encrypted or not
     *
     * @return true if the file was no encrypted
     */
    public boolean isNormalPropsFile() {
        return isNormalPropsFile;
    }

    /**
     * Save the properties to an encrypted file.
     *
     * @param out strean to save to
     * @param header header string which is saved before properties
     * @param password encryption key
     * @param cipherName name of cipher to use. Currentl the valued
     *        algorithms are: Blowfish, DES, DES3 and IDEA.
     */
    public synchronized void save(OutputStream out, String header,
                                  String password, String cipherName)
        throws IOException {
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        Properties            encProps = new Properties();
        byte[]                contents, hash;
        String                hashStr;
        CipherSSH             cipher = CipherSSH.getInstance(cipherName);
        int                   size;

        if(cipher == null)
            throw new IOException("Unknown cipher '" + cipherName + "'");

        save(bytesOut, header);

        contents = bytesOut.toByteArray();
        size = contents.length;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(contents);
            hash = md5.digest();
        } catch(Exception e) {
            throw new IOException("MD5 not implemented, can't generate session-id");
        }

        hash    = Base64.encode(hash);
        hashStr = new String(hash);

        // Assume cipher-block length no longer than 8
        //
        byte[] tmp = new byte[contents.length + (8 - (contents.length % 8))];
        System.arraycopy(contents, 0, tmp, 0, contents.length);
        contents = new byte[tmp.length];

        cipher.setKey(hashStr + password);
        cipher.encrypt(tmp, 0, contents, 0, contents.length);

        contents = Base64.encode(contents);

        encProps.put(HASH_KEY, new String(hash));
        encProps.put(CIPHER_KEY, cipherName.substring(3)); // !!! Cut pre 'SSH'
        encProps.put(CONTENTS_KEY, new String(contents));
        encProps.put(SIZE_KEY, String.valueOf(size));
        encProps.save(out, PROPS_HEADER);
        out.flush();
    }

    /**
     * Load and decrypt properties
     *
     * @param in stream to load properties from
     * @param password decryptiopn key
     */
    public synchronized void load(InputStream in, String password)
    throws IOException, AccessDeniedException {
        Properties encProps = new Properties();
        String     hashStr, cipherName, contentsStr, sizeStr;
        byte[]     contents, hash, hashCalc;
        CipherSSH  cipher;
        int        size;

        encProps.load(in);

        hashStr     = encProps.getProperty(HASH_KEY);
        cipherName  = "SSH" + encProps.getProperty(CIPHER_KEY);
        contentsStr = encProps.getProperty(CONTENTS_KEY);
        sizeStr     = encProps.getProperty(SIZE_KEY);

        // Assume normal properties if our keys are not found (i.e. for
        // "backwards compatible" reading of properties which will be encrypted
        // if saved)
        //
        if(hashStr == null || cipherName == null ||
                contentsStr == null || sizeStr == null) {
            isNormalPropsFile = true;
            Enumeration keys = encProps.keys();
            while(keys.hasMoreElements()) {
                String key = (String)keys.nextElement();
                put(key, encProps.getProperty(key));
            }
            return;
        }

        size = Integer.parseInt(sizeStr);

        hash     = Base64.decode(hashStr.getBytes());
        contents = Base64.decode(contentsStr.getBytes());

        cipher = CipherSSH.getInstance(cipherName);
        if(cipher == null)
            throw new IOException("Unknown cipher '" + cipherName + "'");

        cipher.setKey(hashStr + password);
        cipher.decrypt(contents, 0, contents, 0, contents.length);

        byte[] tmp = new byte[size];
        System.arraycopy(contents, 0, tmp, 0, size);
        contents = tmp;

        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(contents);
            hashCalc = md5.digest();
        } catch(Exception e) {
            throw new IOException("MD5 not implemented, can't generate session-id");
        }

        for(int i = 0; i < hash.length; i++) {
            if(hash[i] != hashCalc[i])
                throw new AccessDeniedException("Access denied");
        }

        ByteArrayInputStream bytesIn = new ByteArrayInputStream(contents);
        load(bytesIn);
    }

    public Object remove(Object key) {
        defaults.remove(key);
        return super.remove(key);
    }

    /* !!! DEBUG
    public static void main(String[] argv) {
    EncryptedProperties test = new EncryptedProperties();

    test.put("Foo", "bar");
    test.put("foo", "bAR");
    test.put("bar", "FOO");
    test.put("BAR", "foo");

    try {
     test.save(new java.io.FileOutputStream("/tmp/fooprops"), "These are some meaningless props...",
        "foobar", "Blowfish");
     test = new EncryptedProperties();
     test.load(new java.io.FileInputStream("/tmp/fooprops"), "foobar");

     System.out.println("test: " + test.getProperty("BAR") + test.getProperty("Foo"));

    } catch (Exception e) {
     System.out.println("Error:" + e);
     e.printStackTrace();
    }
    }
    */

}
