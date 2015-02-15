package com.mxterminal.channel;

import java.io.IOException;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Vector;

import com.mxterminal.ssh.PduInputStreamSSH;
import com.mxterminal.ssh.PduOutputStreamSSH;
import com.mxterminal.ssh.SSH;
import com.mxterminal.ssh.TunnelSSH;

public class ConnectChannel extends ChannelTx {
    ChannelController controller;

    Hashtable hostMap;

    public ConnectChannel(ChannelController controller) {
        super(null, SSH.CONNECT_CHAN_NUM);
        this.controller     = controller;
        this.hostMap        = new Hashtable();
    }

    public synchronized void addHostMapPermanent(String fromHost, String toHost, int toPort) {
        Vector hostPortPair = new Vector();
        hostPortPair.addElement(toHost);
        hostPortPair.addElement(new Integer(toPort));
        hostPortPair.addElement(new Boolean(true));
        hostMap.put(fromHost, hostPortPair);
    }
    public synchronized void addHostMapTemporary(String fromHost, String toHost, int toPort) {
        Vector hostPortPair = new Vector();
        hostPortPair.addElement(toHost);
        hostPortPair.addElement(new Integer(toPort));
        hostPortPair.addElement(new Boolean(false));
        hostMap.put(fromHost, hostPortPair);
    }

    public synchronized void delHostMap(String fromHost) {
        hostMap.remove(fromHost);
    }

    public synchronized Vector getHostMap(String fromHost) {
        Vector hostPortPair = (Vector)hostMap.get(fromHost);
        if(hostPortPair != null && !(((Boolean)hostPortPair.elementAt(2)).booleanValue())) {
            delHostMap(fromHost);
        }
        return hostPortPair;
    }

    int displayNumber(String display) {
        int hostEnd;
        int dispEnd;
        int displayNum;
        if(display == null || display.equals("") ||
                (hostEnd = display.indexOf(':')) == -1)
            return 0;

        if((dispEnd = display.indexOf('.', hostEnd)) == -1)
            dispEnd = display.length();

        try {
            return Integer.parseInt(display.substring(hostEnd + 1, dispEnd));
        } catch (Exception e) {
            // !!!
            displayNum = 0;
        }
        return displayNum;
    }

    String displayHost(String display) {
        int hostEnd;
        if(display == null || display.equals("") ||
                display.charAt(0) == ':' || display.indexOf("unix:") == 0 ||
                (hostEnd = display.indexOf(':')) == -1)
            return "localhost";
        return display.substring(0, hostEnd);
    }

    public void serviceLoop() throws Exception {
        PduInputStreamSSH inPdu;
        int               remoteChannel;
        int               port;
        String            host;
        String            origin;
        Socket            fwdSocket;

        for(;;) {
            inPdu         = (PduInputStreamSSH) queue.getFirst();
            remoteChannel = inPdu.readInt();

            if(inPdu.type == SSH.SMSG_X11_OPEN) {
                if(!controller.sshAsClient().user.wantX11Forward()) {
                    controller.alert("Something is fishy with the server, unsolicited X11 forward!");
                    throw new Exception("Something is fishy with the server, unsolicited X11 forward!");
                }
                String display = controller.sshAsClient().user.getDisplay();
                host = displayHost(display);
                port = 6000 + displayNumber(display);
            } else {
                host = inPdu.readString();
                port = inPdu.readInt();
            }

            if(controller.haveHostInFwdOpen())
                origin = inPdu.readString();
            else
                origin = "unknown (origin-option not used)";

            // See if there is a translation entry for this host
            //
            Vector hostPortPair = getHostMap(host);
            if(hostPortPair != null) {
                host = (String)hostPortPair.elementAt(0);
                port = ((Integer)hostPortPair.elementAt(1)).intValue();
            }

            PduOutputStreamSSH respPdu;

            try {
                fwdSocket        = new Socket(host, port);
                int newChan      = controller.newChannelId();
                TunnelSSH tunnel = new TunnelSSH(fwdSocket, newChan, remoteChannel, controller);
                controller.addTunnel(tunnel);
                tunnel.setRemoteDesc(origin);

                respPdu = new PduOutputStreamSSH(SSH.MSG_CHANNEL_OPEN_CONFIRMATION,
                                                 controller.sndCipher,
                                                 controller.secureRandom());
                respPdu.writeInt(remoteChannel);
                respPdu.writeInt(newChan);

                SSH.log("Port open (" + origin + ") : " + host + ": " + port +
                        " (#" + remoteChannel + ")" + " new: " + newChan);

                controller.transmit(respPdu);

                // We must wait until after we have put the response in the
                // controllers tx-queue with starting the tunnel
                // (to avoid data reaching the server before the response)
                //
                tunnel.start();

            } catch (IOException e) {
                respPdu = new PduOutputStreamSSH(SSH.MSG_CHANNEL_OPEN_FAILURE,
                                                 controller.sndCipher,
                                                 controller.secureRandom());
                respPdu.writeInt(remoteChannel);

                controller.alert("Failed port open (" + origin + ") : " + host + ": " + port +
                                 " (#" + remoteChannel + ")");

                controller.transmit(respPdu);
            }

        }
    }

}
