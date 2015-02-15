package com.mxterminal.channel;

import java.io.IOException;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.mxterminal.ssh2.ConnectionSSH2;
import com.mxterminal.ssh2.ListenerSSH2;


/**
 * A subclass of <code>SSH2TCPChannel</code> which retries the open if
 * it fails.
 */
public class RetryingTCPChannel extends TCPChannel {
	static Logger logger = Logger.getLogger(RetryingTCPChannel.class);
    private int  numOfRetries;
    private long retryDelayTime;

    /**
     * Create a new retrying tcp channel of the given type. The channel is
     * associated with an ssh connection. Channel types are
     * defined in <code>SSH2Connection</code> and starts with
     * <code>CH_TYPE</code>.
     *
     * @param channelType Type of channel to create.
     * @param connection The ssh connection to associate the channel with.
     * @param creator The object the channel is created from.
     * @param endpoint Socket the channel is connected to at the local end.
     * @param remoteAddr Remote server to connect to.
     * @param remotePort Remote port to connect to.
     * @param originAddr Originating host of local connection.
     * @param originPort Originating port of local connection.
     */
    public RetryingTCPChannel(int channelType, ConnectionSSH2 connection,
                                  Object creator,
                                  Socket endpoint,
                                  String remoteAddr, int remotePort,
                                  String originAddr, int originPort)
    throws IOException {
        super(channelType, connection, creator,
              endpoint, remoteAddr, remotePort, originAddr, originPort);
        this.numOfRetries   = 3;
        this.retryDelayTime = 200L;
    }

    /**
     * Set number of retries to do.
     *
     * @param numOfRetries Number of retries.
     */
    public void setRetries(int numOfRetries) {
        this.numOfRetries = numOfRetries;
    }

    /**
     * Set delay between retries.
     *
     * @param retryDelayTime Delay in seconds.
     */
    public void setRetryDelay(long retryDelayTime) {
        this.retryDelayTime = retryDelayTime;
    }

    protected boolean openFailureImpl(int reasonCode, String reasonText,
                                      String langTag) {
        boolean retry = true;
        if(numOfRetries > 0) {
            if(getCreator() instanceof ListenerSSH2) {
                try {
                	
                	logger.debug("Thread.sleep(retryDelayTime)");
                    Thread.sleep(retryDelayTime);
                    
                } catch (InterruptedException e) {}
                logger.info(
                                           "retry (" + numOfRetries +
                                           ") connection on ch. #" + getChannelId() +
                                           " to " + remoteAddr + ":" + remotePort);
                ListenerSSH2 listener = (ListenerSSH2)getCreator();
                listener.sendChannelOpen(this, endpoint);
            } else {
            	logger.error(
                                          "unexpected use of this class");
            }
        } else {
            outputClosed();
            retry = false;
        }
        numOfRetries--;

        return retry;
    }

}
