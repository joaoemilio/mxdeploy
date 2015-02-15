package com.mxterminal.channel;

import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mxterminal.console.component.ComponentView;

public class ChannelEventAdapter implements ChannelEventListener {
	
	private static Log log = LogFactory.getLog(ChannelEventAdapter.class);
	
	public static final int COMMAND_ENTER = 10;
	public static final int COMMAND_STOP  = 3;
	public static final int COMMAND_TRASH = 8;
	public static final int COMMAND_FINISHED = -1;
	
	private boolean forgetData = false;	
	private byte[] readEvent =  new byte[32768];
	private int countEvent=0;
	private String cmd = null;
	private String prompt = null;
	private boolean needChangePrompt = true;
    private boolean wasStopped = false;
    private boolean isStartShell = true;
	
	
    /**
     * Creates a new ChannelEventAdapter object.
     */
    public ChannelEventAdapter() {    }

    /**
     *
     *
     * @param channel
     */
    public void onChannelOpen() {
    }

    /**
     *
     *
     * @param channel
     */
    public void onChannelEOF() {
    }

    /**
     *
     *
     * @param channel
     */
    public void onChannelClose() {
    }

    /**
     *
     *
     * @param channel
     * @param data
     */
    public void onDataSent(byte[] data) {
    }
    
	
	public boolean onDataReceived( ComponentView display, byte[] data ) {
		String wr = new String(data);
		strLinha = strLinha + wr; 
		
//		try {
//			dynamic.getOutputStream().write(data);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		if(strLinha.contains("'$")){
			wr = strLinha;
			wr = wr.replace("'$", "");
			System.out.println("globalVar Replaced ="+wr);
		}		 
		
		
		String test = null;
		if(strLinha.trim().length()>4){
			test = strLinha.trim().substring(strLinha.trim().length()-3).trim();
		} else {
			test = strLinha.trim();
		} 
		
	    String lastPasswordExpected = "";
	    if(wr.length()>=9){  
	       lastPasswordExpected = wr.substring(wr.length()-8);
	    }
	    
    	log.debug("onDataReceived : \n"+wr);
	    
		if ( test.trim().endsWith("#") || test.trim().endsWith("$")
		  || test.contains(">") || lastPasswordExpected.contains("ssword:") 
		  || ( !wr.contains("if [") && test.trim().endsWith("]")  ) 
		   ) {
			
			if(test.contains(">")){ 
				if( test.contains("\">") || test.contains("\'>") || test.contains("##") || test.contains("\\n>")){
					return false;
				}
				
				
				if( test.endsWith("/>") && test.length()>2 ){
					if ( test.substring(test.length()-3).contains(" ") ||  test.substring(test.length()-3).contains("\"")){
						return false;
					}
				}
				
				if( strLinha.contains("iest:")){
					String strtoken = null;
					StringTokenizer token = new StringTokenizer( strLinha, "</");
					while(token.hasMoreTokens()){
						strtoken = token.nextToken();
					}
					if(strtoken!=null && strtoken.contains(test) && strtoken.contains("iest:")){
						return false;
					}
				}
				
//			    if( !test.trim().endsWith(">") ){
//				    return false;
//			    }
			}
			
			if (test.contains("\\$")){
				return false;
			}

			display.closeDynamicBuffer();	
			strLinha = "";
			//System.out.println("Fechou o dynamic [1] - "+wr);
			return true;
		} else if (wr.contains("The authenticity of host")){
			display.closeDynamicBuffer();	
			//dynamic=null;
			strLinha = "";
			//System.out.println("Fechou o dynamic [2] - "+wr);
			return true;			
		}
		
		return false;
	}
	
	private String strLinha = "";
    
}
