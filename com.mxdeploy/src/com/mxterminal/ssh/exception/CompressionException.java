 
 
  
  
  
  
  
  
  
  
  
  
 
  

package com.mxterminal.ssh.exception;

import java.io.IOException;

public class CompressionException extends IOException {
    public CompressionException(String message) {
        super(message);
    }

    public CompressionException() {
        super();
    }
}
