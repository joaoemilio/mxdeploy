package com.mxterminal.ssh2;

import java.io.IOException;
import java.io.OutputStream;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.mxterminal.channel.ChannelCloseListener;
import com.mxterminal.channel.ChannelSSH2;
import com.mxterminal.channel.SessionChannel;
import com.mxterminal.console.ConsoleInputChaff;
import com.mxterminal.console.ConsoleWindows;

/**
 * Adapter class which interfaces between terminal windows and ssh2.
 */
public class TerminalAdapterImplSSH2 extends ConsoleInputChaff
    implements TerminalAdapterSSH2, ChannelCloseListener {
	static Logger logger = Logger.getLogger(TerminalAdapterImplSSH2.class);
	//static Logger logger = Logger.getLogger(TerminalAdapterImplSSH2.class);
	
    private ConsoleWindows     terminal;
    private SessionChannel session;
    private TerminalOutStream  stdout;
    private boolean            minimumLatency;
    
	private boolean isStopped = false;

    final class TerminalOutStream extends OutputStream { 
        public void write(int b) throws IOException {
            terminal.write((char)b);
        }
        public void write(byte b[], int off, int len) throws IOException {
            terminal.write(b, off, len);
        }
    }
    
    public TerminalOutStream getStdOut(){
    	return stdout;
    }

    /**
     * Constructor.
     *
     * @param terminal Terminal window to use.
     */
    public TerminalAdapterImplSSH2(ConsoleWindows terminal) {
        this.terminal = terminal;
        this.stdout   = new TerminalOutStream();
    }

    public ConsoleWindows getTerminal() {
        return terminal;
    }

    public void attach(SessionChannel session) {
        this.session   = session;
        minimumLatency = "true".equals(session.getConnection().getPreferences().getPreference(PreferencesSSH2.TERM_MIN_LAT));
        session.changeStdOut(this.stdout);
        terminal.addInputListener(this);
    }

    public void detach() {
        if(terminal != null) {
            terminal.removeInputListener(this);
        }
        // !!! TODO want to do this ?
        // session.changeStdOut(
    }

    public void startChaff() {
        session.addCloseListener(this);
        super.startChaff();
    }

    public void stopChaff() {
        super.stopChaff();

        session.removeCloseListener(this);
        session.changeStdOut(null);

        terminal = null;
        session = null;
        stdout = null;
    }

    public void closed(ChannelSSH2 channel) {
        stopChaff();
    }

    /**
     * Send an actually typed character.
     *
     * @param c The typed character to send.
     */
    protected void sendTypedChar(int c) {
//    	if(getTerminal().isProcedureRunning())
//    		return;
        if(minimumLatency) {
            session.stdinWriteNoLatency((int)c);
        } else {
            try {
                session.getStdIn().write((int)c);
            } catch (IOException e) {
            	logger.error(
                   
                    "error writing to stdin: " + e.getMessage());
            }
        }
    }

    /**
     * Send a fake character. Sends a packet which is the same size as
     * a real keypress packet but it contains no data.
     */
    protected void sendFakeChar() {
        /*
         * 5 bytes of "ignore-payload" makes packet same size as one byte
         * channel data (i.e. a key-press).
         */
        byte[] chaff = new byte[] { 1, 2, 3, 4, 5 };
        session.getConnection().getTransport().sendIgnore(chaff);
    }

    /** 
     * Send a number of bytes.
     *
     * @param b Array of bytes to send. 
     * @deprecated
     */
    public String sendCommandProcedure(String command) {
            try {
	            synchronized (session.getDynamicBuffer()) { 
					session.getStdIn().write(command.getBytes());	            	
	                if(session.isRunningProcedure()){
	                	return session.waitEndProcedure();
	                }
				} 
            } catch (IOException e) {
 				e.printStackTrace();
			}
        return null;
    }

    public void sendBytes(byte[] b) {
        try {
            session.getStdIn().write(b, 0, b.length);
        } catch (IOException e) {
        	logger.error(
                                                   "error writing to stdin: " +
                                                   e.getMessage());
        }
    }
    
    public void setRunningProcedure(boolean answer){
    	session.setRunningProcedure(answer);
    } 

    /**
     * This function should be called by the actual terminal window
     * whenever it is resized.
     */
    public void signalWindowChanged(int rows, int cols,
                                    int vpixels, int hpixels) {
        session.sendWindowChange(rows, cols);
    }

    /**
     * This function should be called by the actual terminal window
     * whenever the user wants to send break.
     * A break length of 500ms is used, since most devices will 
     * recognize a break of that length.
     */
    public void sendBreak() {
        if (!session.doBreak(500)) {
        	logger.error(
                
                "Failed to send break");
        }
    }

	/**
	 * @return the isStopped
	 */
	public boolean isStopped() {
		return isStopped;
	}

	/**
	 * @param isStopped the isStopped to set
	 */
	public void setStopped(boolean isStopped) {
		this.isStopped = isStopped;
	}
	
	public void stopCommand(){
		String cmd = new String(new char[] {'\003'});
		try {
			session.getStdIn().write(cmd.getBytes());
            try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace(); 
			}			
			session.getStdIn().write("\r\n".getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	} 
	/**
	 * @deprecated
	 */
	public String getResult(String resultado){
		return getResult(resultado,false);
	}
	/**
	 * @deprecated
	 * @param resultado
	 * @param onlyFirst
	 * @return
	 */
	public String getResult(String resultado, boolean onlyFirst){
		String resultadoLocal = "";
		
		StringTokenizer token = new StringTokenizer(resultado,"\r\n");
		
		if(token.countTokens()<3){
			return null;
		}
		
		token.nextToken();
		
		while(token.hasMoreTokens()){
			if(token.countTokens()==1){
				break;
			} else {		
				if(onlyFirst){
					resultadoLocal = token.nextToken();
					break;
				} else {
					if(token.countTokens()>2){
					   resultadoLocal += token.nextToken()+"\r\n"; 
					} else {
					   resultadoLocal += token.nextToken();
					}
				}
			}
		}
		
        try {
		  Thread.sleep(500);
	    } catch (InterruptedException e) {
		  e.printStackTrace();
	    }	
		
		
		//logger.debug("getResult = "+resultadoLocal);
		return resultadoLocal.length()>0 ? resultadoLocal : null;
	}
    
}
