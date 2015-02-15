package com.mxterminal.ssh2.key;

import com.mxterminal.ssh2.exception.ExceptionSSH2;

public class BTSignatureException extends ExceptionSSH2 {
	
    public BTSignatureException(String message) {
        super(message);
    }
    public BTSignatureException(String message, Throwable rootCause) {
        super(message, rootCause);
    }
}
