package com.mxterminal.ssh2;

import com.mxterminal.ssh2.exception.ExceptionSSH2;
import com.mxterminal.ssh2.key.BTSignature;
import com.mxterminal.ssh2.key.BTSignatureException;

/**
 * Handle signatures for the ssh draft standard algorithms.
 */
public abstract class SimpleSignatureSSH2 extends BTSignature {

    protected String signatureAlgorithm;
    protected String ssh2KeyFormat;

    protected boolean draftIncompatibleSignature;

    /**
     * Constructor.
     *
     * @param signatureAlgorithm The algorithm name to use when looking in
     *                           the crypto provider for the implementation.
     *
     * @param ssh2KeyFormat The ssh2 name for this algorithm.
     */
    protected SimpleSignatureSSH2(String signatureAlgorithm,
                                  String ssh2KeyFormat) {
        super();
        this.signatureAlgorithm = signatureAlgorithm;
        this.ssh2KeyFormat      = ssh2KeyFormat;
    }

    /**
     * Get an instance of <code>SSH2Signature</code> which is prepared to
     * do a verify operation which the given public key.
     *
     * @param pubKeyBlob Blob containing the public key to use.
     */
    public static BTSignature getVerifyInstance(byte[] pubKeyBlob)
    throws ExceptionSSH2 {
        String keyFormat        = getKeyFormat(pubKeyBlob);
        BTSignature signature = BTSignature.getInstance(keyFormat);
        signature.initVerify(pubKeyBlob);
        return signature;
    }

    /**
     * Get the signature algorithm name. Used internally to find the
     * right implementation of the algorithm.
     *
     * @return The name of the algorithm.
     */
    public final String getSignatureAlgorithm() {
        return signatureAlgorithm;
    }

    /**
     * Encode the given signature into the form specified by the ssh
     * standard.
     *
     * @param sigRaw The signature as a raw byte array.
     *
     * @return The encoded signature as a byte array.
     */
    public byte[] encodeSignature(byte[] sigRaw) {
        if(draftIncompatibleSignature) {
            return sigRaw;
        } else {
            DataBufferSSH2 buf = new DataBufferSSH2(sigRaw.length + 4 +
                                                    ssh2KeyFormat.length() + 4);
            buf.writeString(ssh2KeyFormat);
            buf.writeString(sigRaw);
            return buf.readRestRaw();
        }
    }

    /**
     * Decode a signature encoded according to the ssh standard.
     *
     * @param sigBlob a signature blob encodeded according to the ssh standard.
     *
     * @return The raw signature
     */
    public byte[] decodeSignature(byte[] sigBlob)
    throws BTSignatureException {
        if(draftIncompatibleSignature) {
            return sigBlob;
        } else {
            DataBufferSSH2 buf = new DataBufferSSH2(sigBlob.length);
            buf.writeRaw(sigBlob);

            int len = buf.readInt();
            if(len <= 0 || len > sigBlob.length) {
                // This is probably an undetected buggy implemenation
                // !!! TODO: might want to report this...
                return sigBlob;
            }

            buf.setRPos(buf.getRPos() - 4); // undo above readInt

            String type = buf.readJavaString();
            if(!type.equals(ssh2KeyFormat)) {
                throw new BTSignatureException(ssh2KeyFormat +
                                                 ", signature blob type " +
                                                 "mismatch, got '" + type);
            }

            return buf.readString();
        }
    }

    /**
     * Get the key format of the given public key.
     *
     * @param pubKeyBlob the public key blob (encoded accoring to the
     *                   ssh standard).
     *
     * @return The ssh name of the key format.
     */
    public static String getKeyFormat(byte[] pubKeyBlob) {
        DataBufferSSH2 buf = new DataBufferSSH2(pubKeyBlob.length);
        buf.writeRaw(pubKeyBlob);
        return buf.readJavaString();
    }

    /**
     * Set the appropriate incompatibility mode which depends on the
     * peer version.
     *
     * @param transport <code>SSH2Transport</code> object which
     * identifies the peer.
     */
    public void setIncompatibility(TransportSSH2 transport) {
        draftIncompatibleSignature = transport.incompatibleSignature;
    }

}

