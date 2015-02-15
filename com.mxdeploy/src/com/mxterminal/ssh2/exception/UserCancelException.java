 
 
  
  
  
  
  
  
  
  
  
  
 
  

package com.mxterminal.ssh2.exception;


public class UserCancelException extends ExceptionSSH2 {

    public UserCancelException() {
        super(null, null);
    }

    public UserCancelException(String message) {
        super(message, null);
    }

}
