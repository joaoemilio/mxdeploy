package com.mxterminal.ssh2;

import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.mxsecurity.util.HexDump;
import com.mxterminal.channel.StreamChannel;

/**
 * Sniffer class which dumps a copy of all data transmitted. This
 * class prints a hex-dump of all data to <code>System.err</code>. It
 * is very useful for debugging.
 */
public class StreamSnifferSSH2 implements StreamFilterSSH2,
    StreamFilterFactory {
    protected int id;

    protected class SniffOutput extends FilterOutputStream {

        public SniffOutput(OutputStream toBeFiltered) {
            super(toBeFiltered);
        }

        public void write(byte b[], int off, int len) throws IOException {
            HexDump.print("ch. #" + id + " tx:", true, b, off, len);
            out.write(b, off, len);
        }

    }

    protected class SniffInput extends FilterInputStream {

        public SniffInput(InputStream toBeFiltered) {
            super(toBeFiltered);
        }

        public int read(byte b[], int off, int len) throws IOException {
            int n = in.read(b, off, len);
            if(n >= 0) {
                HexDump.print("ch. #" + id + " rx:", true, b, off, n);
            } else {
                System.out.println("ch. #" + id + " rx: EOF");
            }
            return n;
        }

    }

    private static StreamSnifferSSH2 factoryInstance;

    private StreamSnifferSSH2() {}

    public static synchronized StreamSnifferSSH2 getFilterFactory() {
        if(factoryInstance == null) {
            factoryInstance = new StreamSnifferSSH2();
        }
        return factoryInstance;
    }

    public StreamFilterSSH2 createFilter(ConnectionSSH2 connection,
                                         StreamChannel channel) {
        StreamSnifferSSH2 sniffer = new StreamSnifferSSH2();
        sniffer.id = channel.getChannelId();
        return sniffer;
    }

    public InputStream getInputFilter(InputStream toBeFiltered) {
        return new SniffInput(toBeFiltered);
    }

    public OutputStream getOutputFilter(OutputStream toBeFiltered) {
        return new SniffOutput(toBeFiltered);
    }

}
