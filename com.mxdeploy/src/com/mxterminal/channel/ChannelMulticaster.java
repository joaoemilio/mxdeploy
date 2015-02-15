 
 
  
  
  
  
  
  
  
  
  
  
 
  

package com.mxterminal.channel;

import com.mxterminal.ssh.PduSSH;

public class ChannelMulticaster implements ChannelListener {
    public PduSSH prepare(PduSSH pdu) {
        return pdu;
    }
    public void receive(PduSSH pdu) {}
    public void transmit(PduSSH pdu) {}
    public void close(ChannelSSH chan) {}
}
