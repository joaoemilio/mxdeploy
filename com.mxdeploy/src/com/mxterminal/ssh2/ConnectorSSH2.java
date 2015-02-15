package com.mxterminal.ssh2;

import java.io.IOException;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.mxterminal.channel.ChannelSSH2;
import com.mxterminal.channel.TCPChannel;
import com.mxterminal.ssh2.exception.ConnectException;
import com.mxterminal.util.Trigger;

public final class ConnectorSSH2 implements Runnable {
	static Logger logger = Logger.getLogger(ConnectorSSH2.class);
	
    private Trigger  openQueue;
    private Thread myThread;

    private ConnectionSSH2 connection;

    private volatile boolean keepRunning;

    public ConnectorSSH2(ConnectionSSH2 connection) {
        this.connection  = connection;
        this.openQueue   = new Trigger();
        this.keepRunning = true;

        myThread = new Thread(this, "SSH2Connector");
        myThread.setDaemon(true);
        myThread.start();
    }

    public void run() {
        while (keepRunning) {
            TransportPDUSSH2 pdu     = null;
            ChannelSSH2      channel = null;

            String channelType = "<unknown>";
            int    peerChanId  = -1;
            int    txInitWinSz = -1;
            int    txMaxPktSz  = -1;
            String targetAddr  = "<unknown>";
            int    targetPort  = -1;
            String originAddr  = "<unknown>";
            int    originPort  = -1;

            try {
                pdu = (TransportPDUSSH2)openQueue.getFirst();

                if(pdu == null) {
                    keepRunning = false;
                    continue;
                }

                channelType = pdu.readJavaString();
                peerChanId  = pdu.readInt();
                txInitWinSz = pdu.readInt();
                txMaxPktSz  = pdu.readInt();

                if(channelType.equals(ConnectionSSH2.CHAN_FORWARDED_TCPIP)) {
                    String remoteAddr = pdu.readJavaString();
                    int    remotePort = pdu.readInt();
                    originAddr = pdu.readJavaString();
                    originPort = pdu.readInt();

                    logger.debug(
                                              "remote connect on " +
                                              remoteAddr + ":" + remotePort +
                                              " (orig. " +
                                              originAddr + ":" + originPort +
                                              ")");

                    String[] localTarget =
                        connection.getForwardTarget(remoteAddr, remotePort);

                    StreamFilterFactory filterFactory =
                        connection.getForwardFilterFactory(remoteAddr,
                                                           remotePort);

                    if(localTarget == null) {
                        throw new IOException("Unsolicited forward attempted");
                    }

                    targetAddr = localTarget[0];
                    targetPort = Integer.valueOf(localTarget[1]).intValue();

                    logger.info(
                                               "connect: " +
                                               localTarget[0] + ":" +
                                               localTarget[1] + " (peerid: " +
                                               peerChanId + ")");

                    channel = connect(channelType, peerChanId,
                                      txInitWinSz, txMaxPktSz,
                                      remoteAddr, remotePort,
                                      targetAddr, targetPort,
                                      originAddr, originPort,
                                      filterFactory);
                    connection.getEventHandler().remoteForwardedConnect(connection,
                            remoteAddr,
                            remotePort,
                            channel);

                } else if(channelType.equals(ConnectionSSH2.CHAN_DIRECT_TCPIP)) {
                    targetAddr = pdu.readJavaString();
                    targetPort = pdu.readInt();
                    originAddr = pdu.readJavaString();
                    originPort = pdu.readInt();
                    channel = connect(channelType, peerChanId,
                                      txInitWinSz, txMaxPktSz,
                                      targetAddr, targetPort,
                                      originAddr, originPort,
                                      null);
                    connection.getEventHandler().remoteDirectConnect(connection,
                            channel);

                } else if(channelType.equals(ConnectionSSH2.CHAN_X11)) {
                    String display = connection.getPreferences().
                                     getPreference(PreferencesSSH2.X11_DISPLAY);
                    int i = display.indexOf(":");
                    if(i != -1) {
                        targetAddr = display.substring(0, i);
                        targetPort = Integer.parseInt(display.substring(i + 1));
                    } else {
                        targetAddr = display;
                        targetPort = 6000;
                    }

                    if(targetPort <= 10) {
                        targetPort += 6000;
                    }

                    originAddr = pdu.readJavaString();
                    originPort = pdu.readInt();

                    if(!connection.hasX11Mapping()) {
                        throw new IOException("Unexpected X11 channel open");
                    }

                    channel = connect(channelType, peerChanId,
                                      txInitWinSz, txMaxPktSz,
                                      targetAddr, targetPort,
                                      originAddr, originPort,
                                      X11FilterSSH2.getFilterFactory());

                    connection.getEventHandler().remoteX11Connect(connection,
                            channel);

                } else if(channelType.equals(ConnectionSSH2.CHAN_SESSION)) {
                    throw new IOException("Unexpected session channel open");
                } else if(channelType.equals(ConnectionSSH2.CHAN_AUTH_AGENT)) {
                    throw new IOException("Agent forwarding not supported");
                } else {
                    throw new IOException("Unknown channel type: " + channelType);
                }

            } catch (IOException e) {
                String msg =  "open failed: " + e.getMessage();
                logger.error( msg);
                sendOpenFailure(peerChanId, 2, msg);
                connection.getEventHandler().remoteChannelOpenFailure(
                    connection, channelType,
                    targetAddr, targetPort,
                    originAddr, originPort,
                    new ConnectException("Failed in SSH2Connector", e));
            } finally {
                if(pdu != null)
                    pdu.release();
            }
        }
    }

    public void channelOpen(TransportPDUSSH2 pdu) {
        openQueue.putLast(pdu);
    }

    public void setThreadPriority(int prio) {
        myThread.setPriority(prio);
    }

    public void stop() {
        keepRunning = false;
        openQueue.setBlocking(false);
    }

    private ChannelSSH2 connect(String channelType, int peerChanId,
                                int txInitWinSz, int txMaxPktSz,
                                String targetAddr, int targetPort,
                                String originAddr, int originPort,
                                StreamFilterFactory filterFactory)
    throws IOException {
        return connect(channelType, peerChanId, txInitWinSz, txMaxPktSz,
                       "<N/A>", 0,
                       targetAddr, targetPort, originAddr, originPort,
                       filterFactory);
    }

    private ChannelSSH2 connect(String channelType, int peerChanId,
                                int txInitWinSz, int txMaxPktSz,
                                String remoteAddr, int remotePort,
                                String targetAddr, int targetPort,
                                String originAddr, int originPort,
                                StreamFilterFactory filterFactory)
    throws IOException {
        Socket fwdSocket = new Socket(targetAddr, targetPort);

        int ch = 0;
        for(ch = 0; ch < ConnectionSSH2.channelTypes.length; ch++) {
            if(ConnectionSSH2.channelTypes[ch].equals(channelType))
                break;
        }
        if(ch == ConnectionSSH2.channelTypes.length) {
            throw new IOException("Invalid channelType: " + channelType);
        }

        TCPChannel channel = new TCPChannel(ch, connection, this,
                                 fwdSocket,
                                 remoteAddr, remotePort,
                                 originAddr, originPort);

        channel.init(peerChanId, txInitWinSz, txMaxPktSz);

        connection.setSocketOptions(PreferencesSSH2.SOCK_OPT_REMOTE +
                                    remoteAddr + "." + remotePort, fwdSocket);

        connection.getEventHandler().channelConnect(this, channel,
                fwdSocket);

        if(filterFactory != null) {
            channel.applyFilter(filterFactory.createFilter(connection,
                                channel));
        }

        // MUST send confirmation BEFORE starting streams
        //
        sendOpenConfirmation(channel);

        channel.startStreams();

        return channel;
    }

    private void sendOpenConfirmation(ChannelSSH2 channel) {
        TransportPDUSSH2 pdu =
            TransportPDUSSH2.createOutgoingPacket(SSH2.MSG_CHANNEL_OPEN_CONFIRMATION);
        pdu.writeInt(channel.peerChanId);
        pdu.writeInt(channel.channelId);
        pdu.writeInt(channel.rxInitWinSz);
        pdu.writeInt(channel.rxMaxPktSz);
        connection.transmit(pdu);
    }

    private void sendOpenFailure(int peerChanId,
                                 int reason, String description) {
        TransportPDUSSH2 pdu =
            TransportPDUSSH2.createOutgoingPacket(SSH2.MSG_CHANNEL_OPEN_FAILURE);
        pdu.writeInt(peerChanId);
        pdu.writeInt(reason);

        if(!connection.getTransport().incompatibleChannelOpenFail) {
            pdu.writeString(description);
            pdu.writeString(""); // !!! TODO: Language tags again...
        }

        connection.transmit(pdu);
    }

}
