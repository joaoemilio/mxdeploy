package com.mxterminal.util;

import java.io.IOException;
import java.io.OutputStream;

/**
 * An output stream pipe should be connected to an input stream pipe;
 * the input stream pipe then provides whatever data bytes are
 * written to the output stream pipe. This is very close to the
 * <code>PipedInputStream</code> and
 * <code>PipedOutputStream</code>. The main difference is that there
 * is a timeout in the write code so that any waiting write will be
 * aborted if the pipe is closed.
 * <p>
 * The input and output pipes are connected via a circular buffer
 * which decouples write and read operations.
 *
 * @see InputStreamTransport
 * @see java.io.PipedInputStream
 * @see java.io.PipedOutputStream
 */
public final class OutputStreamTransport extends OutputStream {

    private InputStreamTransport sink;

    /**
     * Create an OutputStreamPipe which is connected to the given
     * InputStreamPipe.
     *
     * @param sink pipe to connect to
     */
    public OutputStreamTransport(InputStreamTransport sink)  throws IOException {
        connect(sink);
    }

    /**
     * Create an OutputStreamPipe which is not connected to any
     * InputStreamPipe.
     */
    public OutputStreamTransport() {}

    /**
     * Connect this pipe to an InputStreamPipe
     *
     * @param sink pipe to connect to
     */
    public void connect(InputStreamTransport sink) throws IOException {
        if(this.sink == sink) {
            return;
        }
        if(this.sink != null) {
            throw new IOException("Already connected");
        }
        this.sink = sink;
    }

     /**
     * Writes a byte of data into this output stream pipe. If the buffer
     * is full then this call will wait until room becomes available or
     * the stream is closed.
     *
     * @param b the byte of data to write
     */
   public void write(int b) throws IOException {
        sink.put(b);
    }

    /**
     * Put data into this output stream pipe. If the buffer
     * is full then this call will wait until room becomes available or
     * the stream is closed. The function handles the case where the
     * amount of data to write is larger than the circular buffer.
     *
     * @param buf array holding data to put
     * @param off offset of first byte to put
     * @param len number of bytes to put
     */
    public void write(byte buf[], int off, int len) throws IOException {
        sink.put(buf, off, len);
    }

    /**
     * Notify all instances waiting on this stream.
     */
    public void flush() {
        if(sink != null) {
            sink.flush();
        }
    }

    /**
     * Close this output stream pipe.
     */
    public void close() throws IOException {
        sink.eof();
    }

}
