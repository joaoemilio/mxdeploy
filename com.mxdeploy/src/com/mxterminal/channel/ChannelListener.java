package com.mxterminal.channel;

import java.io.IOException;

import com.mxterminal.ssh.PduSSH;

public interface ChannelListener {
    public PduSSH prepare(PduSSH pdu) throws IOException ;
    public void transmit(PduSSH pdu);
    public void receive(PduSSH pdu);
    public void close(ChannelSSH chan);
}
