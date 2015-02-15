package com.mxterminal.ssh2.key;

import java.util.Hashtable;

import com.mxsecurity.jca.InvalidKeyException;
import com.mxsecurity.jca.PrivateKey;
import com.mxsecurity.jca.PublicKey;
import com.mxsecurity.jca.Signature;
import com.mxsecurity.jca.SignatureException;
import com.mxterminal.ssh2.PKISignerSSH2;
import com.mxterminal.ssh2.TransportSSH2;
import com.mxterminal.ssh2.exception.ExceptionSSH2;
import com.mxterminal.ssh2.exception.FatalExceptionSSH2;

/**
 * Abstract base class for classes implementing the ssh2 signature algorithms.
 */
public abstract class BTSignature implements PKISignerSSH2 {

    private static Hashtable algorithms;

    static {
        algorithms = new Hashtable();
        algorithms.put("ssh-dss", "com.mxterminal.ssh2.DSSSSH2");
        algorithms.put("ssh-rsa", "com.mxterminal.ssh2.RSASSH2");
    };

    protected String     algorithm;
    protected Signature  signature;
    protected PrivateKey privateKey;
    protected PublicKey  publicKey;
    protected byte[]     pubKeyBlob;

    /**
     * Get a <code>SSH2Signature</code> instance suitable for encoding
     * with the given algorithm.
     *
     * @param algorithm The algorithm. Currently the valid values are
     * "ssh-dss" and "ssh-rsa".
     *
     * @return An instance of the apropriate signature class.
     */
    public static BTSignature getInstance(String algorithm)
    throws ExceptionSSH2 {
        BTSignature impl = getEncodingInstance(algorithm);
        impl.init(algorithm);
        return impl;
    }

    /**
     * Get a <code>SSH2Signature</code> instance suitable for encoding
     * with the given algorithm.
     *
     * @param algorithm The algorithm. Currently the valid values are
     * "ssh-dss" and "ssh-rsa".
     *
     * @return An instance of the apropriate signature class.
     */
    public static BTSignature getEncodingInstance(String algorithm)
    throws ExceptionSSH2 {
        BTSignature impl      = null;
        String        className = (String)algorithms.get(algorithm);
        try {
            impl = (BTSignature)Class.forName(className).newInstance();
        } catch (Exception e) {
            // !!! TODO
            throw new FatalExceptionSSH2("Public key algorithm '" + algorithm +
                                         "' not supported");
        }
        return impl;
    }

    private void init(String algorithm) throws ExceptionSSH2 {
        this.algorithm = algorithm;
        String sigAlg  = getSignatureAlgorithm();
        try {
            signature = Signature.getInstance(sigAlg);
        } catch (Exception e) {
            // !!! TODO
            throw new FatalExceptionSSH2("Error initializing SSH2Signature: " +
                                         algorithm + "/" + sigAlg + " - " + e);
        }
    }

    /**
     * Constructor.
     */
    protected BTSignature() {}

    /**
     * Get the algorithm this instance handles.
     *
     * @return The algorithm name.
     */
    public final String getAlgorithmName() {
        return algorithm;
    }

    /**
     * Get the public key associated with this
     * <code>SSH2Signature</code> object.
     *
     * @return A public key blob.
     */
    public final byte[] getPublicKeyBlob() throws BTSignatureException {
        if(pubKeyBlob == null) {
            try {
                pubKeyBlob = encodePublicKey(publicKey);
            } catch (ExceptionSSH2 e) {
                throw new BTSignatureException(e.getMessage());
            }
        }
        return pubKeyBlob;
    }

    /**
     * Get the public key associated with this
     * <code>SSH2Signature</code> object.
     *
     * @return A public key object.
     */
    public final PublicKey getPublicKey() throws BTSignatureException {
        if(publicKey == null) {
            try {
                publicKey = decodePublicKey(pubKeyBlob);
            } catch (ExceptionSSH2 e) {
                throw new BTSignatureException(e.getMessage());
            }
        }
        return publicKey;
    }

    /**
     * Associate a public key with this object.
     *
     * @param publicKey The key to associate.
     */
    public final void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public void setIncompatibility(TransportSSH2 transport) {
        // Do nothing here, derived class might be interested...
    }

    /**
     * Prepare for signing with the given private key.
     *
     * @param privateKey Key to use for signing.
     */
    public final void initSign(PrivateKey privateKey) throws ExceptionSSH2 {
        this.privateKey = privateKey;
        try {
            signature.initSign(privateKey);
        } catch (InvalidKeyException e) {
            throw new FatalExceptionSSH2("SSH2Signature.initSign, invalid key: "
                                         + e.getMessage());
        }
    }

    /**
     * Prepare to verify a signature with the given public key.
     *
     * @param publicKey Key to use when verifying.
     */
    public final void initVerify(PublicKey publicKey) throws ExceptionSSH2 {
        initVerify(encodePublicKey(publicKey));
    }

    /**
     * Prepare to verify a signature with the given public key.
     *
     * @param pubKeyBlob key to use when verifying, encoded as a public
     * key blob.
     */
    public final void initVerify(byte[] pubKeyBlob) throws ExceptionSSH2 {
        this.pubKeyBlob = pubKeyBlob;
        this.publicKey  = decodePublicKey(pubKeyBlob);
        try {
            signature.initVerify(publicKey);
        } catch (InvalidKeyException e) {
            throw new FatalExceptionSSH2("SSH2Signature.initVerify, invalid key: "
                                         + e.getMessage());
        }
    }

    /**
     * Sign the given data. The object must have been initialized for
     * signing first.
     *
     * @param data Data to sign.
     *
     * @return A signature blob encoded in the ssh format.
     */
    public final byte[] sign(byte[] data) throws BTSignatureException {
        try {
            signature.update(data);
            byte[] sigRaw = signature.sign();
            return encodeSignature(sigRaw);
        } catch (SignatureException e) {
            throw new BTSignatureException("Error in " + algorithm +
                                             " sign: " + e.getMessage());
        }
    }

    /**
     * Verify that the given signature matches the given data and the
     * public key. The public key is given in the initialization call.
     *
     * @param sigBlob Signature blob encoded in the ssh format.
     * @param data Signed data.
     *
     * @return True if the signature matches.
     */
    public final boolean verify(byte[] sigBlob, byte[] data)
    throws BTSignatureException {
        try {
            byte[] sigRaw = decodeSignature(sigBlob);
            signature.update(data);
            return signature.verify(sigRaw);
        } catch (SignatureException e) {
            throw new BTSignatureException("Error in " + algorithm +
                                             " verify: " + e.getMessage());
        }
    }

    /**
     * Get the signature algorithm.
     *
     * @return The algorithm name.
     */
    protected abstract String getSignatureAlgorithm();

    /**
     * Encode the given public key into a public key blob.
     *
     * @param publicKey The public key to encode. Must be an instance of
     *                  <code>DSAPublicKey</code>.
     *
     * @return A byte array containing the key suitably encoded.
     */
    protected abstract byte[] encodePublicKey(PublicKey publicKey)
    throws ExceptionSSH2;

    /**
     * Decode a public key blob.
     *
     * @param pubKeyBlob A byte array containing a public key blob.
     *
     * @return A <code>Publickey</code> instance.
     */
    protected abstract PublicKey decodePublicKey(byte[] pubKeyBlob)
    throws ExceptionSSH2;

    /**
     * Encode the given, internal form, signature into the ssh standard form.
     *
     * @param sigRaw The raw signature.
     *
     * @return A byte array containing the signature suitably encoded.
     */
    protected abstract byte[] encodeSignature(byte[] sigRaw);

    /**
     * Decode the given signature blob from the ssh standard form to
     * the internal form.
     *
     * @param sigBlob The encoded signature.
     *
     * @return A raw signature blob.
     */
    protected abstract byte[] decodeSignature(byte[] sigBlob)
    throws BTSignatureException;

    public void clearSensitiveData() {
        signature = null;
    }
}
