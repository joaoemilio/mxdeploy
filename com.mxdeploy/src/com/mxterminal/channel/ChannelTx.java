 
 
  
  
  
  
  
  
  
  
  
  
 
  

package com.mxterminal.channel;

import java.io.OutputStream;

import com.mxterminal.ssh.PduSSH;
import com.mxterminal.ssh.SSH;
import com.mxterminal.util.Trigger;

public class ChannelTx extends ChannelSSH {

    protected OutputStream out;
    protected Trigger        queue;

    boolean closePending;

    public ChannelTx(OutputStream out, int channelId) {
        super(channelId);
        this.out          = out;
        this.closePending = false;
        queue = new Trigger();
    }

    public Trigger getQueue() {
        return queue;
    }

    public void setClosePending() {
        closePending = true;
        queue.setBlocking(false);
    }

    public synchronized boolean isClosePending() {
        return closePending;
    }

    public void serviceLoop() throws Exception {
        SSH.logExtra("Starting tx-chan: " + channelId);
        for(;;) {
            PduSSH pdu;
            // !!! the thread is (hopefully) suspended when we set closePending
            // so we don't have to access a lock each loop
            if(closePending && queue.isEmpty()) {
                throw new Exception("CLOSE");
            }
            pdu = (PduSSH)queue.getFirst();
            //      pdu = pdu.preProcess();
            pdu.writeTo(out);
            //      pdu = pdu.postProcess();
        }
    }

}
