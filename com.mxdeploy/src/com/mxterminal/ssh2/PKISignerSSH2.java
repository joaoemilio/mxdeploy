package com.mxterminal.ssh2;

import com.mxterminal.ssh2.key.BTSignatureException;

/**
 * This interface is a simple abstraction of a PKI signing mechanism. An
 * implementation of this interface can use certificates or plain public keys,
 * this is something which is defined by the ssh2 specific algorithm name used
 * to identify it.
 *
 * @see AuthPublicKey
 */
public interface PKISignerSSH2 {
    /**
     * Get the algorithm name.
     *
     * @return The algorithm name.
     */
    public String getAlgorithmName();

    /**
     * Get the public key blob encoded according to the ssh standard.
     *
     * @return A byte array containing the public key.
     */
    public byte[] getPublicKeyBlob() throws BTSignatureException;

    /**
     * Sign a blob of data.
     *
     * @param data The data to be signed.
     *
     * @return The signature, encoded according to the ssh standard.
     */
    public byte[] sign(byte[] data) throws BTSignatureException;

    /**
     * Set eventual incompatibility modes depending on the remote end.
     * Some older ssh implementations use slightly incompatible algorithms
     * when signing data.
     *
     * @param transport An <code>SSH2Transport</code> object which identifies the
     *        other end.
     */
    public void setIncompatibility(TransportSSH2 transport);

    /**
     * Try to remove any sensitive data from memory.
     */
    public void clearSensitiveData();
}
