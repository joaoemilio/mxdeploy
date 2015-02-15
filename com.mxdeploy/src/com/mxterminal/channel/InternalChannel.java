package com.mxterminal.channel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;

import com.mxterminal.ssh2.ConnectionSSH2;
import com.mxterminal.ssh2.PreferencesSSH2;
import com.mxterminal.util.InputStreamTransport;
import com.mxterminal.util.OutputStreamTransport;

/**
 * Implements an internal channel which is connected ot a pair of pipes.
 */
public class InternalChannel extends StreamChannel {
	static Logger logger = Logger.getLogger(InternalChannel.class);
	
    protected InputStreamTransport  rxPipe;
    protected OutputStreamTransport txPipe;

    /**
     * Create a new internal channel of the given type. The channel is
     * associated with an ssh connection. Channel types are
     * defined in <code>SSH2Connection</code> and starts with
     * <code>CH_TYPE</code>.
     *
     * @param channelType Type of channel to create.
     * @param connection The ssh connection to associate the channel with.
     */
    public InternalChannel(int channelType, ConnectionSSH2 connection) {
        super(channelType, connection, connection,null, null);

        int ioBufSz = connection.getPreferences().getIntPreference(PreferencesSSH2.INT_IO_BUF_SZ);
        in  = new InputStreamTransport(ioBufSz);
        out = new OutputStreamTransport();

        try {
            this.txPipe = new OutputStreamTransport();
            this.rxPipe = new InputStreamTransport(ioBufSz);
            this.rxPipe.connect((OutputStreamTransport)out);
            this.txPipe.connect((InputStreamTransport)in);
        } catch (IOException e) {
        	logger.error("can't happen, bug somewhere!?!");
        }
    }

    /**
     * Get the input stream of the channel.
     *
     * @return The input stream.
     */
    public InputStream getInputStream() {
        return rxPipe;
    }

    /**
     * Get the output stream of the channel.
     *
     * @return The output stream.
     */
    public OutputStream getOutputStream() {
        return txPipe;
    }

}
