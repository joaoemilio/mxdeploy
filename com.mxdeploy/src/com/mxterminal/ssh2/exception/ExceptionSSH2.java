 
 
  
  
  
  
  
  
  
  
  
  
 
  

package com.mxterminal.ssh2.exception;

public abstract class ExceptionSSH2 extends Exception {

    protected Throwable rootCause;

    public ExceptionSSH2(String message) {
        this(message, null);
    }

    public ExceptionSSH2(String message, Throwable rootCause) {
        super(message);
        this.rootCause = rootCause;
    }

    public Throwable getRootCause() {
        return rootCause;
    }

}

