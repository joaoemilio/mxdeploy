package com.mxterminal.ssh;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.mxterminal.channel.ChannelController;
import com.mxterminal.channel.ChannelListener;
import com.mxterminal.channel.ChannelRx;
import com.mxterminal.channel.ChannelSSH;
import com.mxterminal.channel.ChannelTx;
import com.mxterminal.util.Trigger;

public class TunnelSSH implements ChannelListener {

    public int channelId;
    int remoteChannelId;

    boolean   sentInputEOF;
    boolean   sentOutputClosed;
    boolean   receivedInputEOF;
    boolean   receivedOutputClosed;

    protected ChannelController controller;

    protected Socket       ioSocket;
    protected ChannelTx txChan;
    protected ChannelRx rxChan;

    protected Trigger        txQueue;

    public String remoteDesc;

    public TunnelSSH(Socket ioSocket, int channelId, int remoteChannelId,
                     ChannelController controller)
    throws IOException {

        this.ioSocket             = ioSocket;
        this.channelId            = channelId;
        this.remoteChannelId      = remoteChannelId;
        this.controller           = controller;
        this.sentInputEOF         = false;
        this.sentOutputClosed     = false;
        this.receivedInputEOF     = false;
        this.receivedOutputClosed = false;

        if(ioSocket != null) {
            try {
                rxChan = new ChannelRx(new BufferedInputStream(ioSocket.getInputStream(), 8192), channelId);
                txChan = new ChannelTx(new BufferedOutputStream(ioSocket.getOutputStream()), channelId);
            } catch (Exception e) {
                throw new IOException("Could not create tunnel: " + e.toString());
            }
            txQueue = txChan.getQueue();

            rxChan.setSSHPduFactory(new PduOutputStreamSSH(SSH.MSG_CHANNEL_DATA,
                                    controller.sndCipher,
                                    controller.secureRandom()));
            txChan.setSSHChannelListener(this);
            rxChan.setSSHChannelListener(this);
        }
    }

    public int getLocalPort() {
        if(ioSocket != null)
            return ioSocket.getLocalPort();
        return 0;
    }

    public String getLocalHost() {
        if(ioSocket != null)
            return ioSocket.getLocalAddress().getHostAddress();
        return "N/A";
    }

    public boolean isOpen() {
        if(this.remoteChannelId == SSH.UNKNOWN_CHAN_NUM)
            return false;
        return true;
    }

    public boolean setRemoteChannelId(int remoteChannelId) {
        if(isOpen())
            return false;
        this.remoteChannelId = remoteChannelId;
        return true;
    }

    public void start() {
        txChan.start();
        rxChan.start();
    }

    public void openFailure() {
        if(ioSocket != null) {
            try {
                ioSocket.close();
            } catch (IOException e) {
                // !!!
            }
        }
    }

    public PduSSH prepare(PduSSH pdu) throws IOException {
        ((PduOutputStreamSSH)pdu).writeInt(remoteChannelId);
        return pdu;
    }

    public void receive(PduSSH pdu) {
        controller.transmit(pdu);
    }

    public void transmit(PduSSH pdu) {
        txQueue.putLast(pdu);
    }

    public void close(ChannelSSH chan) {
        if(chan == null || chan instanceof ChannelTx) {
            sendOutputClosed();
            try {
                ioSocket.close();
            } catch (IOException e) {
                controller.alert("Error closing socket for: " + channelId + " : " + e.toString());
            }
        } else {
            sendInputEOF();
        }
        checkTermination();
    }

    public synchronized void terminateNow() {
        close(null);
    }

    public synchronized void checkTermination() {
        if(sentInputEOF && sentOutputClosed &&
                receivedInputEOF && receivedOutputClosed) {
            controller.delTunnel(channelId);
            if(txChan != null && txChan.isAlive())
                txChan.stop();
            if(rxChan != null && rxChan.isAlive())
                rxChan.stop();
        }
    }

    public void sendOutputClosed() {
        if(sentOutputClosed)
            return;
        try {
            PduOutputStreamSSH pdu =
                new PduOutputStreamSSH(SSH.MSG_CHANNEL_OUTPUT_CLOSED,
                                       controller.sndCipher,
                                       controller.secureRandom());
            pdu.writeInt(remoteChannelId);
            controller.transmit(pdu);
            sentOutputClosed = true;
        } catch (Exception e) {
            controller.alert("Error sending output-closed: " + e.toString());
        }
    }

    public void sendInputEOF() {
        if(sentInputEOF)
            return;
        try {
            PduOutputStreamSSH pdu = new PduOutputStreamSSH(SSH.MSG_CHANNEL_INPUT_EOF,
                                     controller.sndCipher,
                                     controller.secureRandom());
            pdu.writeInt(remoteChannelId);
            controller.transmit(pdu);
            sentInputEOF = true;
        } catch (Exception e) {
            controller.alert("Error sending input-EOF: " + e.toString());
        }
    }

    public void receiveOutputClosed() {
        if(rxChan != null)
            rxChan.stop();
        receivedOutputClosed = true;
        sendInputEOF();
        checkTermination();
    }

    public void receiveInputEOF() {
        if(txChan != null)
            txChan.setClosePending();
        receivedInputEOF = true;
        checkTermination();
    }

    public void setRemoteDesc(String desc) {
        remoteDesc = desc;
    }

    public String getDescription() {
        if(ioSocket != null)
            return ioSocket.getInetAddress().getHostAddress() + ":" + ioSocket.getPort() + " <--> " +
                   getLocalHost() + ":" + ioSocket.getLocalPort() + " <-ssh-> " + remoteDesc;
        else
            return "< N/A >";
    }

}
