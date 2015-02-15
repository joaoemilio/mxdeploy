package com.mxterminal.util;

import java.io.IOException;

import com.mxterminal.util.io.DynamicBuffer;


public final class Trigger {

	//static Logger logger = Logger.getLogger(Trigger.class);
	
    final static int DEFAULT_DEPTH   = 64;
    final static int DEFAULT_HIWATER = 32;

    Object[]   queue;
    boolean    isWaitGet;
    boolean    isWaitPut;
    boolean    isBlocking;
    int        rOffset;
    int        wOffset;
    int        depth;
    int        hiwater;
    int        size;

    // Copies used for saving real values when disabling queue
    //
    int        depthCP;
    int        sizeCP;
    
    public StringBuffer cmd_response = null;
    
    /**
     * Constructs a new queue with the default depth (64) and hiwater
     * (32) levels.
     */
    public Trigger() {
        this(DEFAULT_DEPTH, DEFAULT_HIWATER);
    }

    /**
     * Constructs a new queue with custom depth and hiwater levels.
     */
    public Trigger(int depth, int hiwater) {
        this.queue      = new Object[depth + 1];
        this.isWaitGet  = false;
        this.isWaitPut  = false;
        this.isBlocking = true;
        this.rOffset    = 0;
        this.wOffset    = 0;
        this.size       = 0;
        this.sizeCP     = -1;
        this.depth      = depth;
        this.hiwater    = hiwater;
    }

    /**
     * Append an object to the queue. This function will hang until
     * there is room in the queue.
     *
     * @param obj the object to append
     */
    public synchronized void putLast(Object obj) {
        putFlowControl();
        queue[wOffset++] = obj;
        if(wOffset == (depth + 1))
            wOffset = 0;
        if(isWaitGet)
        	this.notify();
        size++;
    }

    /**
     * insert an object at the head of the queue. This function will hang until
     * there is room in the queue.
     *
     * @param obj the object to insert
     */
    public synchronized void putFirst(Object obj) {
        putFlowControl();
        rOffset--;
        if(rOffset == -1)
            rOffset = depth;
        queue[rOffset] = obj;
        size++;
        if(isWaitGet)
            this.notify();
        
    }

    /**
     * Release any blocked object. That is eventual calls to get are
     * unblocked.
     */
    public synchronized void release() {
        if(isWaitGet)
            this.notify();
    }

    /**
     * Disable the queue. A disabled queue will not accept any new objects
     */
    public synchronized void disable() {
        depthCP = depth;
        sizeCP  = size;
        depth   = 0;
        size    = 0;
        release();
    }

    /**
     * Enable a disabled queue.
     */
    public synchronized void enable() {
        if(sizeCP >= 0) {
            depth = depthCP;
            size          = sizeCP;
            sizeCP        = -1;
            if(!isEmpty()) {
                this.release();
            }
            if(isWaitPut && (size <= hiwater)) {
                this.notifyAll();
                isWaitPut = false;
            }
        }
    }

    /**
     * Controls if the getFirst call should block. The default
     * is that calls are blocking.
     *
     * @param block true if calls to getFirst should block
     */
    public synchronized void setBlocking(boolean block) {
        isBlocking = block;
        release();
    }

    /**
     * Check if the queue is empty
     *
     * @return true if the queue is empty
     */
    public synchronized boolean isEmpty() {
        return size == 0;
    }

    private final void putFlowControl() {
        /*
         * Note this must be a while-loop because of the possibility of unfair
         * thread scheduling due to the fact that the sematics for wait/notify
         * in java is that the order in which wait() unblocks is not defined
         * (i.e. even a thread which wasn't waiting for the lock BEFORE we
         * called wait() here can acquire the lock BEFORE our wait() returns)
         */
        while(size >= depth) {
            isWaitPut = true;
            try {
                this.wait(1000);
            } catch (InterruptedException e) { /* ignore */
            }
        }
    }

    private boolean dowait(long ms) {
        long start = System.currentTimeMillis();
        while (isEmpty()) {
            if(!isBlocking) {
                return false;
            }
            isWaitGet = true;
            try {
           	  this.wait(ms);
            } catch (InterruptedException e) {
                // !!!
            }

            if (ms > 0) {
                if ((System.currentTimeMillis() - start) > ms)
                    return false;
            }
        }
        return true;
    }

    /**
     * Get the first object on the queue, optionally with a timeout.
     *
     * @param ms how long, in milliseconds, to wait, if the queue is
     * blocking, for a new object.
     */
    public synchronized Object getFirst(long ms) {
        if (!dowait(ms))
            return null;

        Object obj = queue[rOffset];
        isWaitGet = false;
        queue[rOffset++] = null;
        if(rOffset == (depth + 1))
            rOffset = 0;
        if(isWaitPut && (size <= hiwater)) {
            this.notifyAll();
            isWaitPut = false;
        }
        size--;
        return obj;
    }

    public synchronized Object getFirst(long ms,DynamicBuffer dynamicBuffer) {
        if (!dowait(ms,dynamicBuffer))
            return null;

        Object obj = queue[rOffset];
        //logger.debug("getFirst ["+rOffset+"]"); 
        isWaitGet = false;
        queue[rOffset++] = null;
        if(rOffset == (depth + 1))
            rOffset = 0;
        if(isWaitPut && (size <= hiwater)) {
            this.notifyAll();
            isWaitPut = false;
        }
        size--;
        return obj;
    }
    
    private synchronized boolean dowait(long ms, final DynamicBuffer dynamicBuffer) {
        long start = System.currentTimeMillis();
        int count = 0;
        while (isEmpty()) {
        	count++;
            if(!isBlocking) {
                return false;
            } 
            isWaitGet = true;
            try {
                if(dynamicBuffer!=null){
             	   try { 
 	           			String strLinha = null;
 	        			String wr = cmd_response.toString();
 	        			if(wr.length()>4){
 	        			   strLinha = wr.substring(wr.length()-3);
 	        			} else {
 	        			   strLinha = wr;  
 	        			} 
 	        			if ( strLinha.contains("# ")|| wr.contains(":websrvr]") ||
 	        				 strLinha.contains("$ ")|| strLinha.contains("> ") || wr.contains("assword:")	) {
 	        				 dynamicBuffer.getOutputStream().write("\r\n".getBytes());  
 	        				 //logger.debug("dowait[dynamic] :\n"+wr);
 	        				 //logger.debug("**********************************"); 
 	        			}  
 				   } catch (IOException e) { 
 					 e.printStackTrace();
 				   }
                }
            	
                this.wait(ms);
                //logger.debug("Saiu do Wait ["+count+"]");
            } catch (InterruptedException e) {
                // !!!
            } 

            if (ms > 0) {
                if ((System.currentTimeMillis() - start) > ms)
                    return false;
            }
        }
        return true;
    }
    
    
    /**
     * Get the first object on the queue.
     *
     * @return the first object of the queue or null if the queue is empty
     */
    public Object getFirst() { 
        return getFirst(0);
    }
    
    public boolean getWaitGetStatus(){
    	return isWaitGet;
    }
    
   
    
}
