package com.mxsecurity;

import com.mxsecurity.jca.Provider;

public class SecBright extends Provider {

    public SecBright() {
        super("SecBright", 1.1, "SecBright JCA/JCE provider v1.1");

        put("Cipher.DES",          "com.mxsecurity.cipher.DES");
        put("Cipher.DES/ECB",      "com.mxsecurity.cipher.DES");
        put("Cipher.DES/CBC",      "com.mxsecurity.cipher.DES");
        put("Cipher.DES/CFB",      "com.mxsecurity.cipher.DES");
        put("Cipher.DES/OFB",      "com.mxsecurity.cipher.DES");
        put("Cipher.DES/CBC/PKCS5Padding", "com.mxsecurity.cipher.DES");
        put("Cipher.3DES",         "com.mxsecurity.cipher.DES3");
        put("Cipher.3DES/ECB",     "com.mxsecurity.cipher.DES3");
        put("Cipher.3DES/CBC",     "com.mxsecurity.cipher.DES3");
        put("Cipher.3DES/CFB",     "com.mxsecurity.cipher.DES3");
        put("Cipher.3DES/OFB",     "com.mxsecurity.cipher.DES3");
        put("Cipher.3DES/CTR",     "com.mxsecurity.cipher.DES3");
        put("Cipher.3DES/CBC/PKCS5Padding", "com.mxsecurity.cipher.DES3");
        put("Cipher.Blowfish",     "com.mxsecurity.cipher.Blowfish");
        put("Cipher.Blowfish/ECB", "com.mxsecurity.cipher.Blowfish");
        put("Cipher.Blowfish/CBC", "com.mxsecurity.cipher.Blowfish");
        put("Cipher.Blowfish/CFB", "com.mxsecurity.cipher.Blowfish");
        put("Cipher.Blowfish/OFB", "com.mxsecurity.cipher.Blowfish");
        put("Cipher.Blowfish/CTR", "com.mxsecurity.cipher.Blowfish");
        put("Cipher.Twofish",      "com.mxsecurity.cipher.Twofish");
        put("Cipher.Twofish/ECB",  "com.mxsecurity.cipher.Twofish");
        put("Cipher.Twofish/CBC",  "com.mxsecurity.cipher.Twofish");
        put("Cipher.Twofish/CFB",  "com.mxsecurity.cipher.Twofish");
        put("Cipher.Twofish/OFB",  "com.mxsecurity.cipher.Twofish");
        put("Cipher.Twofish/CTR",  "com.mxsecurity.cipher.Twofish");
        put("Cipher.Rijndael",     "com.mxsecurity.cipher.Rijndael");
        put("Cipher.Rijndael/ECB", "com.mxsecurity.cipher.Rijndael");
        put("Cipher.Rijndael/CBC", "com.mxsecurity.cipher.Rijndael");
        put("Cipher.Rijndael/CFB", "com.mxsecurity.cipher.Rijndael");
        put("Cipher.Rijndael/OFB", "com.mxsecurity.cipher.Rijndael");
        put("Cipher.Rijndael/CTR", "com.mxsecurity.cipher.Rijndael");
        put("Cipher.IDEA",         "com.mxsecurity.cipher.IDEA");
        put("Cipher.IDEA/ECB",     "com.mxsecurity.cipher.IDEA");
        put("Cipher.IDEA/CBC",     "com.mxsecurity.cipher.IDEA");
        put("Cipher.IDEA/CFB",     "com.mxsecurity.cipher.IDEA");
        put("Cipher.IDEA/OFB",     "com.mxsecurity.cipher.IDEA");
        put("Cipher.CAST128",      "com.mxsecurity.cipher.CAST128");
        put("Cipher.CAST128/ECB",  "com.mxsecurity.cipher.CAST128");
        put("Cipher.CAST128/CBC",  "com.mxsecurity.cipher.CAST128");
        put("Cipher.CAST128/CFB",  "com.mxsecurity.cipher.CAST128");
        put("Cipher.CAST128/OFB",  "com.mxsecurity.cipher.CAST128");
        put("Cipher.RC2",          "com.mxsecurity.cipher.RC2");
        put("Cipher.RC2/ECB",      "com.mxsecurity.cipher.RC2");
        put("Cipher.RC2/CBC",      "com.mxsecurity.cipher.RC2");
        put("Cipher.RC2/CFB",      "com.mxsecurity.cipher.RC2");
        put("Cipher.RC2/OFB",      "com.mxsecurity.cipher.RC2");
        put("Cipher.RC2/CBC/PKCS5Padding", "com.mxsecurity.cipher.RC2");
        put("Cipher.RC4/OFB",      "com.mxsecurity.cipher.ArcFour");
        put("Cipher.RC4/OFB/PKCS5Padding", "com.mxsecurity.cipher.ArcFour");
        put("Cipher.RC4Skip/OFB", "com.mxsecurity.cipher.ArcFourSkip");
        put("Alg.Alias.Cipher.AES",     "Rijndael/ECB");
        put("Alg.Alias.Cipher.AES/ECB", "Rijndael/ECB");
        put("Alg.Alias.Cipher.AES/CBC", "Rijndael/CBC");
        put("Alg.Alias.Cipher.AES/CFB", "Rijndael/CFB");
        put("Alg.Alias.Cipher.AES/OFB", "Rijndael/OFB");
        put("Alg.Alias.Cipher.AES/CTR", "Rijndael/CTR");
        put("Alg.Alias.Cipher.DESede",     "3DES/ECB");
        put("Alg.Alias.Cipher.DESede/ECB", "3DES/ECB");
        put("Alg.Alias.Cipher.DESede/CBC", "3DES/CBC");
        put("Alg.Alias.Cipher.DESede/CFB", "3DES/CFB");
        put("Alg.Alias.Cipher.DESede/OFB", "3DES/OFB");
        put("Alg.Alias.Cipher.DESede/CBC/PKCS5Padding", "3DES/CBC/PKCS5Padding");
        put("Alg.Alias.Cipher.RC4",     "RC4/OFB");
        put("Alg.Alias.Cipher.ArcFour", "RC4/OFB");
        put("Alg.Alias.Cipher.ArcFourSkip", "RC4Skip/OFB");
        put("Alg.Alias.Cipher.CAST5",   "CAST128");
        put("Alg.Alias.Cipher.CAST5/ECB", "CAST128/ECB");
        put("Alg.Alias.Cipher.CAST5/CBC", "CAST128/CBC");
        put("Alg.Alias.Cipher.CAST5/CFB", "CAST128/CFB");
        put("Alg.Alias.Cipher.CAST5/OFB", "CAST128/OFB");

        put("MessageDigest.MD2",       "com.mxsecurity.digest.MD2");
        put("MessageDigest.MD5",       "com.mxsecurity.digest.MD5");
        put("MessageDigest.SHA",       "com.mxsecurity.digest.SHA1");
        put("MessageDigest.SHA256",    "com.mxsecurity.digest.SHA256");
        put("MessageDigest.RIPEMD160", "com.mxsecurity.digest.RIPEMD160");
        put("Alg.Alias.MessageDigest.SHA-1",              "SHA");
        put("Alg.Alias.MessageDigest.SHA1",               "SHA");
        put("Alg.Alias.MessageDigest.1.3.14.3.2.26",      "SHA");
        put("Alg.Alias.MessageDigest.1.2.840.113549.2.5", "MD5");
        put("Alg.Alias.MessageDigest.1.2.840.113549.2.2", "MD2");
        put("Alg.Alias.MessageDigest.1.3.36.3.2.1",       "RIPEMD160");
        put("Alg.Alias.MessageDigest.SHA-256",            "SHA256");
        put("Alg.Alias.MessageDigest.SHA2-256",           "SHA256");

        put("Mac.HmacSHA1",      "com.mxsecurity.mac.HMACSHA1");
        put("Mac.HmacMD5",       "com.mxsecurity.mac.HMACMD5");
        put("Mac.HmacRIPEMD160", "com.mxsecurity.mac.HMACRIPEMD160");
        put("Mac.HmacSHA1-96",   "com.mxsecurity.mac.HMACSHA1_96");
        put("Mac.HmacMD5-96",    "com.mxsecurity.mac.HMACMD5_96");
        put("Mac.HmacRIPEMD160-96", "com.mxsecurity.mac.HMACRIPEMD160_96");
        /* Convenience, not id of MAC itself but of the used HASH */
        put("Alg.Alias.Mac.1.2.840.113549.2.5", "HmacMD5");
        put("Alg.Alias.Mac.1.3.14.3.2.26",      "HmacSHA1");
        /* From IANA numbers for ipsec */
        put("Alg.Alias.Mac.1.3.6.1.5.5.8.1.1",  "HmacMD5");
        put("Alg.Alias.Mac.1.3.6.1.5.5.8.1.2",  "HmacSHA1");
        put("Alg.Alias.Mac.1.3.6.1.5.5.8.1.4",  "HmacRIPEMD160");

        put("SecureRandom.BlumBlumShub", "com.mxsecurity.prng.BlumBlumShub");

        put("Signature.SHA1withRawDSA", "com.mxsecurity.publickey.RawDSAWithSHA1");
        put("Signature.SHA1withDSA", "com.mxsecurity.publickey.DSAWithSHA1");
        put("Signature.SHA1withRSA", "com.mxsecurity.publickey.RSAWithSHA1");
        put("Signature.MD5withRSA", "com.mxsecurity.publickey.RSAWithMD5");
        put("Signature.MD2withRSA", "com.mxsecurity.publickey.RSAWithMD2");
        put("Signature.RIPEMD160withRSA", "com.mxsecurity.publickey.RSAWithRIPEMD160");
        put("Alg.Alias.Signature.1.3.14.3.2.13", "SHA1withDSA");
        put("Alg.Alias.Signature.1.3.14.3.2.27", "SHA1withDSA");
        put("Alg.Alias.Signature.1.2.840.10040.4.3", "SHA1withDSA");
        put("Alg.Alias.Signature.1.3.14.3.2.29",  "SHA1withRSA");
        put("Alg.Alias.Signature.1.2.840.113549.1.1.5", "SHA1withRSA");
        put("Alg.Alias.Signature.1.3.14.3.2.3", "MD5withRSA");
        put("Alg.Alias.Signature.1.3.14.3.2.25", "MD5withRSA");
        put("Alg.Alias.Signature.1.2.840.113549.1.1.4", "MD5withRSA");
        put("Alg.Alias.Signature.1.3.14.3.2.24", "MD2withRSA");
        put("Alg.Alias.Signature.1.2.840.113549.1.1.2", "MD2withRSA");
        put("Alg.Alias.Signature.1.3.36.3.3.1.2", "RIPEMD160withRSA");

        put("KeyFactory.RSA", "com.mxsecurity.publickey.RSAKeyFactory");
        put("KeyFactory.DSA", "com.mxsecurity.publickey.DSAKeyFactory");
        put("KeyFactory.DH",  "com.mxsecurity.publickey.DHKeyFactory");

        put("KeyPairGenerator.DH", "com.mxsecurity.publickey.DHKeyPairGenerator");
        put("KeyPairGenerator.RSA", "com.mxsecurity.publickey.RSAKeyPairGenerator");
        put("KeyPairGenerator.DSA", "com.mxsecurity.publickey.DSAKeyPairGenerator");

        put("KeyAgreement.DH", "com.mxsecurity.publickey.DHKeyAgreement");

        put("KeyStore.PKCS12", "com.mxsecurity.keystore.PKCS12KeyStore");
        put("KeyStore.Netscape", "com.mxsecurity.keystore.NetscapeKeyStore");
    }

}
