package com.mxssh;


/**
 * @author Fabio Santos/Joao Emilio
 */
public interface ChannelListener {

	public void receiveMessage(String message, int messageType, Callback callback);

	public void receiveMessage(String message, int messageType);
	
}
