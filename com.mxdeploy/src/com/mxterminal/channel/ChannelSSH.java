package com.mxterminal.channel;

import org.apache.log4j.Logger;

import com.mxterminal.ssh.SSH;

public abstract class ChannelSSH extends Thread {

	static Logger logger = Logger.getLogger(ChannelSSH.class);
	
    protected int                channelId;
    protected ChannelListener listener;

    public ChannelSSH(int channelId) {
        super();
        setName(this.getClass().getName() + "[" + channelId + "]");
        this.channelId = channelId;
        this.listener  = null;
    }

    public void setSSHChannelListener(ChannelListener listener) {
        this.listener = listener;
    }

    public int getChannelId() {
        return channelId;
    }

    public abstract void serviceLoop() throws Exception;

    public void close() {}

    public void run() {

        try {
        	logger.debug("serviceLoop()");
            serviceLoop();
        } catch (Exception e) {

        	logger.debug( e.getMessage() );

        	close();
            if(listener != null)
                listener.close(this);

        } catch (ThreadDeath death) {
            SSH.logExtra("Channel killed " + channelId + " " + this);
            throw death;
        }
    }

}
