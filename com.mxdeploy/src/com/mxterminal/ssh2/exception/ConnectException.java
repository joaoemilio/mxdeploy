 
 
  
  
  
  
  
  
  
  
  
  
 
  

package com.mxterminal.ssh2.exception;



public class ConnectException extends ExceptionSSH2 {
    public ConnectException(String message) {
        this(message, null);
    }
    public ConnectException(String message, Throwable rootCause) {
        super(message, rootCause);
    }
}
