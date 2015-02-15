package com.mxterminal.channel;


/**
 * An interface for classes which want to be notified when a channel is closed.
 *
 * @see Channel
 */
public interface ChannelCloseListener {
    /**
     * Called when the channel is closed.
     *
     * @param channel The channel which is closed
     */
    public void closed(ChannelSSH2 channel);
}
