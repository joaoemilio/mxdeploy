package com.mxterminal.channel;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;

import org.apache.log4j.Logger;

import com.mxterminal.ssh2.ConnectionSSH2;
import com.mxterminal.ssh2.PreferencesSSH2;
import com.mxterminal.ssh2.SSH2;
import com.mxterminal.ssh2.StreamFilterSSH2;
import com.mxterminal.ssh2.TransportPDUSSH2;
import com.mxterminal.util.Trigger;
import com.mxterminal.util.io.DynamicBuffer;

/**
 * Class implementing streams-based channels. That is channels which
 * locally are connected to a pair of Input/Output streams. It is also
 * possible to apply filters to the channels.
 */
public class StreamChannel extends ChannelSSH2 {
	//static Logger logger = Logger.getLogger(StreamChannel.class);
	static Logger logger = Logger.getLogger(SessionChannel.class);
    protected InputStream  in;
    protected OutputStream out;

    protected DynamicBuffer dynamic = null;
    
    protected Thread transmitter;
    protected Thread receiver;
    protected Trigger  rxQueue;
    protected long   txCounter;
    protected long   rxCounter;
    
	private int countEvent=0;
	
	private String prompt = null;
	private boolean needChangePrompt = true;
    private boolean forgetData = false;
    private boolean wasStopped = false;
    
    private boolean isStarting = true;
    
    private boolean isRunningProcedure = false;
    private int count = 0;
    
    /*
     * NOTE, if enabled can cause dead-lock when doing re-keyexchange
     * if we initiate it, hence SSH2Transport.incompatibleCantReKey is
     * set accordingly
     */
    private boolean rxChanIsQueued;

    /**
     * Create a new stream channel of the given type. The channel is
     * associated with an ssh connection. Channel types are
     * defined in <code>SSH2Connection</code> and starts with
     * <code>CH_TYPE</code>.
     *
     * @param channelType Type of channel to create.
     * @param connection The ssh connection to associate the channel with.
     * @param creator The object the channel is created from.
     * @param in The input stream from which data to be sent over the
     * channel is read.
     * @param out The output stream onto which data received from the
     * channel is written.
     */
    protected StreamChannel(int channelType, ConnectionSSH2 connection, Object creator, InputStream in, OutputStream out) {
        super(channelType, connection, creator);

        rxChanIsQueued = "true".equals(connection.getPreferences().getPreference(PreferencesSSH2.QUEUED_RX_CHAN));

        this.in  = in;
        this.out = out;
        createStreams();
    }

    /**
     * Apply the given filter to this channel.
     *
     * @param filter Filter to apply.
     */
    public void applyFilter(StreamFilterSSH2 filter) {
        if(filter != null) {
            in  = filter.getInputFilter(in);
            out = filter.getOutputFilter(out);
        }
    }

    private void channelTransmitLoop() {
    	logger.debug("starting ch. #" + channelId +" (" + getType() + ") transmitter");
    	logger.debug("Thread.yield();");
        Thread.yield();
        int count = 0;
        try {
            TransportPDUSSH2 pdu;
            int              maxSz = 0;
            int              rcvSz = 0;
            boolean          interrupted = false;
            while(!eofSent && !closeSent) {
                pdu = TransportPDUSSH2.createOutgoingPacket(SSH2.MSG_CHANNEL_DATA, txMaxPktSz + 256);
                pdu.writeInt(peerChanId);
                maxSz = checkTxWindowSize(rcvSz);
                do {
                    try {
                        rcvSz = in.read(pdu.data, pdu.wPos + 4, maxSz);
                        interrupted = false;
                    } catch (InterruptedIOException e) {
                        interrupted = true;
                    }
                } while (interrupted);
                if(rcvSz == -1) {
                    sendEOF();
                } else if (!eofSent && !closeSent) {
                	if(dynamic!=null){
                		synchronized (pdu) {
                			count++;
    	                    pdu.writeInt(rcvSz);
    	                    pdu.wPos  += rcvSz;
    	                    txCounter += rcvSz;
    	                    isStarting = false;
    	                    transmit(pdu);
						}
                	} else {
                        pdu.writeInt(rcvSz);
                        pdu.wPos  += rcvSz;
                        txCounter += rcvSz;
                        isStarting = false;
                        transmit(pdu);
                	}
                }
            }
        } catch (IOException e) {
            if(!eofSent) {
            	logger.error(e.toString());
            }
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) { /* don't care */
            }
            sendClose();
        }
        logger.debug( "exiting ch. #" + channelId + " (" + getType() + ") transmitter, " + txCounter +" bytes tx");
    }
  
    private void channelReceiveLoop() {
    	logger.debug("starting ch. #" + channelId + " (" + getType() + ") receiver");
    	logger.debug("Thread.yield();");
        Thread.yield();
        try {
            TransportPDUSSH2 pdu;
            while((pdu = (TransportPDUSSH2)rxQueue.getFirst(0)) != null) { 
            	count++;
            	logger.debug("channelReceiveLoop(rxWrite)["+count+"]"); 
                rxWrite(pdu); 
            }  
        } catch (IOException e) {
        	logger.error(e.toString());
        } finally {
            rxClosing(); 
        }
        logger.debug("exiting ch. #" + channelId + " (" + getType()+") receiver, " + rxCounter +" bytes rx");
    }
    
    private synchronized final void rxWrite(TransportPDUSSH2 pdu) throws IOException {
        int len = pdu.readInt();
        int off = pdu.getRPos();
        rxCounter += len;
        
        //if(dynamic!=null){
        	//String wr = getConnection().getFilter().convertFrom(pdu.data, off, len);    
            ///char[] carr = wr.toCharArray();

//            int end = carr.length;
//            String a = null;
//            for(int i = 0; i < end; i++)
//            	a += carr[i];

//        	if(rxQueue.cmd_response==null){ 
//        		rxQueue.cmd_response = new StringBuffer(); 
//        		rxQueue.cmd_response.append(wr); 
//        	} else {
//        		rxQueue.cmd_response.append(wr);
//        	}
//        }
        
        out.write(pdu.data, off, len);
        
        pdu.release();
        checkRxWindowSize(len);
    }
    
    
    
    private final void rxClosing() {
        // Signal to transmitter that this is an orderly shutdown
        //
        eofSent = true;
        try {
            if (out != null)
                out.close();
        } catch (IOException e) { /* don't care */
        }
        try {
            if (in != null)
                in.close();
        } catch (IOException e) { /* don't care */
        }
        outputClosed();

        // there is a slight chance that the transmitter is waiting for
        // window adjust in which case we must interrupt it here so it
        // doesn't hang
        //
        if(txCurrWinSz == 0) {
            txCurrWinSz = -1;
            transmitter.interrupt();
        }
    }

    private final synchronized int checkTxWindowSize(int lastSz) {
        txCurrWinSz -= lastSz;
        while(txCurrWinSz == 0) {
            // Our window is full, wait for ACK from peer
            try {
                this.wait();
            } catch (InterruptedException e) {
                if(!eofSent) {
                	logger.error(
                                              "window adjust wait interrupted");
                }
            }
        }
        // Try sending remaining window size or max packet size before ACK
        //
        int dataSz = (txCurrWinSz < txMaxPktSz ? txCurrWinSz : txMaxPktSz);
        return dataSz;
    }

    protected final void checkRxWindowSize(int len) {
        rxCurrWinSz -= len;
        if(rxCurrWinSz < 0) {
            connection.fatalDisconnect(SSH2.DISCONNECT_PROTOCOL_ERROR,
                                       "Peer overflowed window");
        } else if(rxCurrWinSz <= (rxInitWinSz >>> 1)) {
            // ACK on >= 50% of window received
            TransportPDUSSH2 pdu =
                TransportPDUSSH2.createOutgoingPacket(SSH2.MSG_CHANNEL_WINDOW_ADJUST);
            pdu.writeInt(peerChanId);
            pdu.writeInt(rxInitWinSz - rxCurrWinSz);
            transmit(pdu);
            rxCurrWinSz = rxInitWinSz;
        }
    }

    public void data(TransportPDUSSH2 pdu) {
	        if(rxChanIsQueued) {
	            rxQueue.putLast(pdu);
	        } else {
	            try {
	                rxWrite(pdu);
	            } catch (IOException e) {
	            	logger.error(e.toString());
	                rxClosing();
	            }
        	}
    }

    protected void openConfirmationImpl(TransportPDUSSH2 pdu) {
        startStreams();
    }

    protected boolean openFailureImpl(int reasonCode, String reasonText,
                                      String langTag) {
        // Just return false since we don't want to keep the channel,
        // handle in derived class if needed
        return false;
    }

    protected synchronized void windowAdjustImpl(int inc) {
        txCurrWinSz += inc;
        this.notify();
    }

    protected void eofImpl() {
        if(rxChanIsQueued) {
            rxQueue.setBlocking(false);
        } else {
            rxClosing();
        }
    }

    protected void closeImpl() {
        eofImpl();
    }

    /**
     * Called when no more data can be written to the channel.
     */
    protected void outputClosed() {
        // Do nothing, handle in derived class if needed
    }

    protected void handleRequestImpl(String type, boolean wantReply,
                                     TransportPDUSSH2 pdu) {
        // Do nothing, handle in derived class if needed
    }

    /**
     * Create the transmitter and receiver threads.
     */
    protected void createStreams() {
        if(rxChanIsQueued) {
        	logger.debug("receiver = new Thread(new Runnable() {");
            receiver = new Thread(new Runnable() {
                                      public void run() {
                                          channelReceiveLoop();
                                      }
                                  }, "SSH2StreamRX_" + getType() + "_" + channelId);
            receiver.setDaemon(false);
            logger.debug("rxQueue = new Trigger(connection.getPreferences().");
            rxQueue = new Trigger(connection.getPreferences().
                          getIntPreference(PreferencesSSH2.QUEUE_DEPTH),
                          connection.getPreferences().
                          getIntPreference(PreferencesSSH2.QUEUE_HIWATER));
        }
        logger.debug("transmitter = new Thread(new Runnable() {");
        transmitter = new Thread(new Runnable() {
                                     public void run() {
                                         channelTransmitLoop();
                                     }
                                 }, "SSH2StreamTX_" + getType() + "_" + channelId);
        transmitter.setDaemon(false);
    }

    /**
     * Starts the transmitter and receiver threads.
     */
    public void startStreams() {
    	logger.debug("transmitter.start();");
        transmitter.start();
        if(rxChanIsQueued) {
        	logger.debug("receiver.start();");
            receiver.start();
        }
    }

    public void waitUntilClosed() {
        super.waitUntilClosed();
        if(rxChanIsQueued) {
            try {
            	logger.debug("receiver.join();");
                receiver.join();
            } catch (InterruptedException e) { }
        }
    }

	/**
	 * @return the isRunningProcedure
	 */
	public boolean isRunningProcedure() {
		return isRunningProcedure;
	}

	/**
	 * @param isRunningProcedure the isRunningProcedure to set
	 */
	public void setRunningProcedure(boolean isRunningProcedure) {
		this.isRunningProcedure = isRunningProcedure;
	}

    public String waitEndProcedure(){
    	if(isRunningProcedure){
    	   try {
    		   //int next = getDynamicBuffer().getInputStream().read();
    		   logger.debug("getDynamicBuffer().getInputStream().read();");
    		   getDynamicBuffer().getInputStream().read();
		   } catch (IOException e) {
			 e.printStackTrace();
		   } finally{
			   closeDynamicBuffer();
		   }
    	}
    	String result = new String(rxQueue.cmd_response.toString());
    	rxQueue.cmd_response = null;
    	return result;
    }

	public DynamicBuffer getDynamicBuffer(){
			if(dynamic==null)
			   dynamic = new DynamicBuffer();
			return dynamic;
	}

	public void closeDynamicBuffer(){
		if(dynamic!=null){
		   dynamic.close();
		   dynamic = null;
		}
	}
	
    
}


