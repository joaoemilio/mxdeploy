package com.mxterminal.channel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.mxterminal.ssh.CipherSSH;
import com.mxterminal.ssh.ClientSSH;
import com.mxterminal.ssh.ConsoleSSH;
import com.mxterminal.ssh.InteractorSSH;
import com.mxterminal.ssh.ListenChannelSSH;
import com.mxterminal.ssh.PduInputStreamSSH;
import com.mxterminal.ssh.PduOutputStreamSSH;
import com.mxterminal.ssh.PduSSH;
import com.mxterminal.ssh.ProtocolPluginSSH;
import com.mxterminal.ssh.SSH;
import com.mxterminal.ssh.TunnelSSH;
import com.mxterminal.util.Trigger;

public final class ChannelController extends SSH implements ChannelListener {

	static Logger logger = Logger.getLogger(ChannelController.class);
	
    protected ChannelTx        txChan;
    protected ChannelRx        rxChan;
    protected ConnectChannel   cnChan;
    protected Trigger               txQueue;
    protected Trigger               cnQueue;

    protected int      totalTunnels;
    protected int      nextEmptyChan;
    protected Object[] tunnels;

    protected Vector listenChannels;

    protected SSH        sshHook;
    public ConsoleSSH console;

    public CipherSSH     sndCipher;
    protected CipherSSH     rcvCipher;
    
    public ChannelController(SSH sshHook, InputStream in, OutputStream out,
                                CipherSSH sndCipher,
                                CipherSSH rcvCipher,
                                ConsoleSSH console, boolean haveCnxWatch) {
        this.sndCipher = sndCipher;
        this.rcvCipher = rcvCipher;

        this.sshHook = sshHook;
        this.console = console;

        this.tunnels         = new Object[16];
        this.nextEmptyChan   = 0;
        this.totalTunnels    = 0;
        this.listenChannels  = new Vector();

        txChan = new ChannelTx(out, MAIN_CHAN_NUM);
        rxChan = new ChannelRx(in, MAIN_CHAN_NUM);

        rxChan.setSSHChannelListener(this);
        txChan.setSSHChannelListener(this);
        rxChan.setSSHPduFactory(new PduInputStreamSSH(MSG_ANY, rcvCipher));
        txQueue = txChan.getQueue();

        if(haveCnxWatch) {
            cnChan = new ConnectChannel(this);
            cnChan.setSSHChannelListener(this);
            cnQueue = cnChan.getQueue();
        } else {
            cnQueue = new Trigger();
        }

    }

    public void start() {
        txChan.start();
        rxChan.start();
        if(cnChan != null)
            cnChan.start();
    }

    public void waitForExit() throws InterruptedException {
        waitForExit(0); // Wait forever...
    }
    public void waitForExit(long msWait) throws InterruptedException {
        if(rxChan != null)
            rxChan.join(msWait);
        logger.debug("Thread.sleep(100)");
        Thread.sleep(100);
        killAll();
    }

    public void killAll() {
        killAllTunnels();
        killListenChannels();
        if(rxChan != null && rxChan.isAlive())
            rxChan.stop();
        if(txChan != null && txChan.isAlive())
            txChan.stop();
        if(cnChan != null && cnChan.isAlive())
            cnChan.stop();

        rxChan = null;
        txChan = null;
        cnChan = null;
        System.runFinalization();
    }

    public synchronized int newChannelId() {
        int newChan = nextEmptyChan;
        if(nextEmptyChan < tunnels.length) {
            int i;
            for(i = nextEmptyChan + 1; i < tunnels.length; i++)
                if(tunnels[i] == null)
                    break;
            nextEmptyChan = i;
        } else {
            Object[] tmp = new Object[tunnels.length + 16];
            System.arraycopy(tunnels, 0, tmp, 0, tunnels.length);
            tunnels = tmp;
            nextEmptyChan++;
        }

        return newChan;
    }

    public synchronized String[]
    listTunnels() {
        int i, cnt = 0;
        String[] list1 = new String[tunnels.length];

        for(i = 0; i < tunnels.length; i++) {
            if(tunnels[i] == null)
                continue;
            list1[cnt++] = ((TunnelSSH)tunnels[i]).getDescription();
        }

        String[] list2 = new String[cnt];
        System.arraycopy(list1, 0, list2, 0, cnt);

        return list2;
    }

    public synchronized void closeTunnelFromList(int listIdx) {
        int i;
        for(i = 0; i < tunnels.length; i++) {
            if(tunnels[i] == null)
                continue;
            listIdx--;
            if(listIdx < 0)
                break;
        }
        if(i < tunnels.length) {
            ((TunnelSSH)tunnels[i]).terminateNow();
        }
    }

    public synchronized void killAllTunnels() {
        for(int i = 0; i < tunnels.length; i++) {
            if(tunnels[i] == null)
                continue;
            ((TunnelSSH)tunnels[i]).openFailure(); // !!! Forced close
            tunnels[i] = null;
        }
        tunnels = new Object[16];
    }

    public synchronized void addTunnel(TunnelSSH tunnel)
    throws IOException {
        totalTunnels++;
        tunnels[tunnel.channelId] = tunnel;
    }

    public synchronized TunnelSSH delTunnel(int channelId) {
        TunnelSSH tunnelToDelete = (TunnelSSH) tunnels[channelId];
        tunnels[channelId] = null;
        nextEmptyChan = (channelId < nextEmptyChan ? channelId : nextEmptyChan);
        totalTunnels--;
        return tunnelToDelete;
    }

    public boolean haveHostInFwdOpen() {
        return sshHook.isProtocolFlagSet(PROTOFLAG_HOST_IN_FWD_OPEN);
    }

    public ListenChannelSSH newListenChannel(String localHost, int localPort,
            String remoteHost, int remotePort,
            String plugin) throws IOException {
        ListenChannelSSH newListenChan = null;
        newListenChan = ProtocolPluginSSH.getPlugin(plugin).localListener(localHost, localPort,
                        remoteHost, remotePort,
                        this);
        newListenChan.setSSHChannelListener(this);
        newListenChan.start();
        synchronized(listenChannels) {
            listenChannels.addElement(newListenChan);
        }
        return newListenChan;
    }

    public void killListenChannel(String localHost, int listenPort) {
        ListenChannelSSH listenChan;
        synchronized(listenChannels) {
            for(int i = 0; i < listenChannels.size(); i++) {
                listenChan = (ListenChannelSSH) listenChannels.elementAt(i);
                if(listenChan.getListenPort() == listenPort && listenChan.getListenHost().equals(localHost)) {
                    listenChannels.removeElementAt(i);
                    listenChan.forceClose();
                    break;
                }
            }
        }
    }

    public void killListenChannels() {
        ListenChannelSSH listenChan;
        synchronized(listenChannels) {
            while(listenChannels.size() > 0) {
                listenChan = (ListenChannelSSH) listenChannels.elementAt(0);
                listenChan.forceClose();
                listenChannels.removeElementAt(0);
            }
        }
    }

    public PduSSH prepare(PduSSH pdu) {
        return pdu;
    }

    public void transmit(PduSSH pdu) {
        txQueue.putLast(pdu);
    }

    public void receive(PduSSH pdu) {
        PduInputStreamSSH inPdu = (PduInputStreamSSH) pdu;
        TunnelSSH         tunnel;
        int               channelNum;
        try {
            switch(inPdu.type) {
            case SMSG_STDOUT_DATA:
                if(console != null)
                    console.stdoutWriteString(inPdu.readStringAsBytes());
                break;
            case SMSG_STDERR_DATA:
                if(console != null)
                    console.stderrWriteString(inPdu.readStringAsBytes());
                break;
            case SMSG_EXITSTATUS:
                PduOutputStreamSSH exitPdu =
                    new PduOutputStreamSSH(CMSG_EXIT_CONFIRMATION, sndCipher, secureRandom());
                int status = inPdu.readInt();
                if(console != null) {
                    if(status != 0)
                        //console.serverDisconnect(sshAsClient().getServerAddr().getHostName() + " disconnected: " + status);
                    	console.serverDisconnect(sshAsClient().getHostName() + " disconnected: " + status);
                    else
                        //console.serverDisconnect("Connection to " + sshAsClient().getServerAddr().getHostName() + " closed.");
                    	console.serverDisconnect("Connection to " + sshAsClient().getHostName() + " closed.");
                }
                transmit(exitPdu);
                sshAsClient().disconnect(true);
                break;
            case SMSG_X11_OPEN:
                // Fallthrough
            case MSG_PORT_OPEN:
                cnQueue.putLast(inPdu);
                break;
            case MSG_CHANNEL_DATA:
                channelNum = inPdu.readInt();
                tunnel     = (TunnelSSH)tunnels[channelNum];
                if(tunnel != null)
                    tunnel.transmit(pdu);
                else
                    throw new Exception("Data on nonexistent channel: " + channelNum);
                break;
            case MSG_CHANNEL_OPEN_CONFIRMATION:
                channelNum  = inPdu.readInt();
                tunnel = (TunnelSSH)tunnels[channelNum];
                if(tunnel != null) {
                    if(!tunnel.setRemoteChannelId(inPdu.readInt()))
                        throw new Exception("Open confirmation on allready opened channel!");
                    tunnel.start();
                } else
                    throw new Exception("Open confirm on nonexistent: " + channelNum);
                break;
            case MSG_CHANNEL_OPEN_FAILURE:
                TunnelSSH failTunnel;
                channelNum = inPdu.readInt();
                if((failTunnel = delTunnel(channelNum)) != null) {
                    alert("Channel open failure on " + failTunnel.remoteDesc);
                    failTunnel.openFailure();
                } else
                    throw new Exception("Open failure on nonexistent channel: " + channelNum);
                break;
            case MSG_CHANNEL_INPUT_EOF:
                channelNum = inPdu.readInt();
                tunnel     = (TunnelSSH)tunnels[channelNum];
                if(tunnel != null) {
                    tunnel.receiveInputEOF();
                } else
                    throw new Exception("Input eof on nonexistent channel: " + channelNum);
                break;
            case MSG_CHANNEL_OUTPUT_CLOSED:
                channelNum  = inPdu.readInt();
                ;
                if(channelNum < tunnels.length && ((tunnel = (TunnelSSH)tunnels[channelNum]) != null)) {
                    tunnel.receiveOutputClosed();
                } else
                    throw new Exception("Output closed on nonexistent channel: " + channelNum);
                break;
            case MSG_DISCONNECT:
                disconnect("Peer disconnected: " + inPdu.readString());
                break;
            case CMSG_WINDOW_SIZE:
                break;
            case CMSG_STDIN_DATA:
                break;
            case CMSG_EOF:
                System.out.println("!!! EOF received...");
                break;
            case CMSG_EXIT_CONFIRMATION:
                break;
            default:
                throw new Exception("Unknown packet type (" + inPdu.type + "), disconnecting...");
            }
        } catch(Exception e) {
            // !!! Are there known BUGS in here?? Nah... :-)
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            System.out.println("\nBug found: " + e.getMessage());
            System.out.println(sw.toString());
            sendDisconnect("Bug found: " + e.getMessage() + "\n\r" +
                           kludgeLF2CRLFMap(sw.toString()));
            // !!!
        }
    }

    static String kludgeLF2CRLFMap(String orig) {
        int o = 0, n;
        String result = "";
        while((n = orig.indexOf('\n', o)) != -1) {
            result += orig.substring(o, n) + "\n\r";
            o = n + 1;
        }
        result += orig.substring(o);
        return result;
    }

    public void close(ChannelSSH chan) {
        // !!!
        if(chan instanceof ConnectChannel)
            SSH.logExtra("Controller connect-channel closed");
        else if(chan instanceof ChannelTx)
            SSH.logExtra("Controller TX-channel closed");
        else if(chan instanceof ChannelRx) {
            SSH.logExtra("Controller RX-channel closed");
            if (txChan != null)
                txChan.setClosePending();
        } else if(chan instanceof ListenChannelSSH)
            SSH.logExtra("Listen channel for port " + ((ListenChannelSSH)chan).getListenPort() +
                         " closed");
        else
            alert("Bug in SSHChannelController.close 'chan' is: " + chan);
    }

    public void disconnect(String reason) {
        if(sshHook.isAnSSHClient)
            sshAsClient().disconnect(false);
        if(txChan != null)
            txChan.setClosePending();
        if(console != null)
            console.serverDisconnect("\r\nDisconnecting, " + reason);
        else
            SSH.log("\r\nDisconnecting, " + reason);

        if(!sshHook.isAnSSHClient && rxChan != null) {
            rxChan.forceClose();
        }
    }

    public void sendDisconnect(String reason) {
        try {
            PduOutputStreamSSH pdu = new PduOutputStreamSSH(MSG_DISCONNECT,
                                     sndCipher, 
                                     secureRandom());
            pdu.writeString(reason);
            if(txQueue != null)
                txQueue.putFirst(pdu);
            logger.debug("Thread.sleep(300)");
            Thread.sleep(300);
            disconnect(reason);
        } catch (Exception e) {
            alert("Error in sendDisconnect: " + e.toString());
        }
    }

    public void alert(String msg) {
        if(sshHook.isAnSSHClient) {
            InteractorSSH interactor = sshAsClient().user.getInteractor();
            if(interactor != null)
                interactor.alert(msg);
        } else {
            SSH.log(msg);
        }
    }

    public ClientSSH sshAsClient() {
        return (ClientSSH)sshHook;
    }

    public Trigger getCnQueue() {
        return cnQueue;
    }

    public void addHostMapTemporary(String fromHost, String toHost, int toPort) {
        cnChan.addHostMapTemporary(fromHost, toHost, toPort);
    }

    public void addHostMapPermanent(String fromHost, String toHost, int toPort) {
        cnChan.addHostMapPermanent(fromHost, toHost, toPort);
    }

    public void delHostMap(String fromHost) {
        cnChan.delHostMap(fromHost);
    }

    public Vector getHostMap(String fromHost) {
        return cnChan.getHostMap(fromHost);
    }

}
