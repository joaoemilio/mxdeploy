package com.mxterminal.console;

import org.apache.log4j.Logger;


public abstract class ConsoleInputChaff implements ConsoleInputListener,    Runnable {

	static Logger logger = Logger.getLogger(ConsoleInputChaff.class);
	
	private Thread           chaffThread;
    private volatile boolean chaffActive;
    private volatile long    chaffLastKeyTime;

    private int[] lastKeyBuf;
    private int   lastKeyROffs;
    private int   lastKeyWOffs;

    /**
     * Start sending chaff
     */
    public void startChaff() {
        if(!chaffActive) {
            chaffActive  = true;
            lastKeyBuf   = new int[4];
            lastKeyROffs = 0;
            lastKeyWOffs = 0;
            chaffThread  = new Thread(this, "SSH2TerminalAdapterImpl.chaff");
            chaffThread.setDaemon(true);
            logger.debug("chaffThread.start()");
            chaffThread.start();
        }
    }

    /**
     * Stop sending chaff
     */
    public void stopChaff() {
        if(chaffActive) {
            chaffActive = false;
            synchronized(chaffThread) {
                chaffThread.notify();
            }
            chaffThread = null;
        }
    }

    /**
     * Tell if chaffing is active or not
     *
     * @return true if chaff is being sent
     */
    protected boolean isChaffActive() {
        return chaffActive;
    }

    /**
     * Receive a character typed by the user, the whole point of
     * chaffing is to disguise the timing of calls to this function.
     *
     * @param c typed character
     */
    public void typedChar(char c) {
        if(chaffThread == null) {
            sendTypedChar((int)c);
        } else {
            synchronized(this) {
                lastKeyBuf[lastKeyWOffs++] = (int)c;
                lastKeyWOffs &= 0x03;
            }
            dispenseChaff();
        }
    }

    /**
     * Receive a character typed by the user, the whole point of
     * chaffing is to disguise the timing of calls to this function.
     *
     * @param b byte array contained the encoded version of the character
     */
    public void typedChar(byte[] b) {
        for (int i=0; i<b.length; i++) {
            typedChar((char)b[i]);
        }
    }

    /**
     * This callback is only interesting for local input listeners
     * such as LineReaderTerminal
     */
    public void signalTermTypeChanged(String newTermType) {
    }

    /**
     * Classes derived from this class that are capable of sending
     * a break signal can implement this.
     */
    public void sendBreak() {
    }

    /**
     * The thread which actually sends the chaff or real data.
     */
    public void run() {
    	logger.debug("run()");
        long now;
        int  wait;
        while(chaffActive) {
            try {
                synchronized(chaffThread) {
                    chaffThread.wait();
                }
                wait = (int)(System.currentTimeMillis() ^
                             (new Object()).hashCode()) & 0x1ff;
                do {
                    int lastKeyChar = -1;
                    synchronized(this) {
                        if(lastKeyWOffs != lastKeyROffs) {
                            lastKeyChar = lastKeyBuf[lastKeyROffs++];
                            lastKeyROffs &= 0x03;
                        }
                    }
                    if(lastKeyChar >= 0) {
                        sendTypedChar(lastKeyChar);
                    } else {
                        sendFakeChar();
                    }
                    
                    Thread.sleep(30);
                    now = System.currentTimeMillis();
                } while(chaffActive &&
                        (now - chaffLastKeyTime) < (1500 + wait));
            } catch (InterruptedException e) {
                /* don't care... */
            }
        }
    }

    /**
     * Kick chaff thread
     */
    public void dispenseChaff() {
        if(chaffThread != null) {
            long now = System.currentTimeMillis();
            if((now - chaffLastKeyTime) > 1000) {
                chaffLastKeyTime = now;
                synchronized(chaffThread) {
                    chaffThread.notify();
                }
            }
        }
    }

    /**
     * Send a real typed character to the server.
     */
    protected abstract void sendTypedChar(int c);

    /**
     * Send a fake character to the server.
     */
    protected abstract void sendFakeChar();
}
