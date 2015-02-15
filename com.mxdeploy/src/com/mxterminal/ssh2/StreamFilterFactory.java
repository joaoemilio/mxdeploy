package com.mxterminal.ssh2;

import com.mxterminal.channel.StreamChannel;


/**
 * An interfaces for classes which creates instances of classes which
 * implements <code>SSH2StreamFiler</code>.
 */
public interface StreamFilterFactory {
    /**
     * Instance constructor
     */
    public StreamFilterSSH2 createFilter(ConnectionSSH2 connection,
                                         StreamChannel channel);

}
