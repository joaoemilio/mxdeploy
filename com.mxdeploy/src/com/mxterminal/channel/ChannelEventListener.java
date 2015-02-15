package com.mxterminal.channel;

import com.mxterminal.console.component.ComponentView;


public interface ChannelEventListener {

    /**
    *
    *
    * @param channel
    */
   public void onChannelOpen();

   /**
    *
    *
    * @param channel
    */
   public void onChannelEOF();

   /**
    *
    *
    * @param channel
    */
   public void onChannelClose();

   /**
    *
    *
    * @param channel
    * @param data
    */
   public boolean onDataReceived( ComponentView display, byte[] data);

   /**
    *
    *
    * @param channel
    * @param data
    */
   public void onDataSent( byte[] data);
	
}
