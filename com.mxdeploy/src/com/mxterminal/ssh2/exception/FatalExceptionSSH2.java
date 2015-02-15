 
 
  
  
  
  
  
  
  
  
  
  
 
  

package com.mxterminal.ssh2.exception;

public class FatalExceptionSSH2 extends ExceptionSSH2 {

    public FatalExceptionSSH2(String message) {
        this(message, null);
    }

    public FatalExceptionSSH2(String message, Throwable rootCause) {
        super(message, rootCause);
    }

}
