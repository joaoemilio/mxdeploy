package com.mxterminal.channel;

import java.io.IOException;
import java.io.InputStream;

import com.mxterminal.ssh.PduSSH;
import com.mxterminal.ssh.SSH;

public class ChannelRx extends ChannelSSH {

    protected InputStream  in;
    protected PduSSH       pduFactory;

    public ChannelRx(InputStream in, int channelId) {
        super(channelId);
        this.in = in;
    }

    public void setSSHPduFactory(PduSSH pduFactory) {
        this.pduFactory = pduFactory;
    }

    public void serviceLoop() throws Exception { 
        SSH.logExtra("Starting rx-chan: " + channelId);
        for(;;) {
            PduSSH pdu;
            pdu = pduFactory.createPdu();
            pdu = listener.prepare(pdu);
            //      pdu = pdu.preProcess(pdu);
            pdu.readFrom(in);
            //      pdu = pdu.postProcess();
            listener.receive(pdu);
        }
    }

    public void forceClose() {
        try {
            in.close();
        } catch (IOException e) {
            // !!!
        }
    }

}
