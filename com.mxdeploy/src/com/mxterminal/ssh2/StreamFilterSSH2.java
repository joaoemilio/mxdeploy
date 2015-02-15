package com.mxterminal.ssh2;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Filers which operate on SSH datastreams.
 */
public interface StreamFilterSSH2 {
    /**
     * Get the filter for the input stream.
     *
     * @param toBeFiltered The stream to filter.
     */
    public InputStream getInputFilter(InputStream toBeFiltered);

    /**
     * Get the filter for the output stream.
     *
     * @param toBeFiltered The stream to filter.
     */
    public OutputStream getOutputFilter(OutputStream toBeFiltered);
}
