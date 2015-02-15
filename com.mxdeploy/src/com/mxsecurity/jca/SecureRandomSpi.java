
package com.mxsecurity.jca;

import java.io.Serializable;

/**
 * Interface for random number generators
 */
public abstract class SecureRandomSpi implements Serializable {

    public SecureRandomSpi() {}

    /**
     * Generate a number of random seed bytes.
     *
     * @param numBytes how many bytes to generate
     *
     * @return an array filled with the requested number of random bytes
     */
    protected abstract byte[] engineGenerateSeed(int numBytes);

    /**
     * Generate random bytes
     *
     * @param bytes array which should be filled with random bytes
     */
    protected abstract void engineNextBytes(byte[] bytes);

    /**
     * Set the seed used by the random number generator.
     *
     * @param seed an array of random bytes
     */
    protected abstract void engineSetSeed(byte[] seed);

}
