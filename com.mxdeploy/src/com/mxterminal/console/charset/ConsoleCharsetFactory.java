 
 
  
  
  
  
  
  
  
  
  
  
 
  

package com.mxterminal.console.charset;

public class ConsoleCharsetFactory {
    public static ConsoleCharsetTranslator create(String charset)
    throws ConsoleCharsetException {
        if (charset.equals("none")) {
            return null;
        } else if (charset.equals("vga")) {
            return new ConsoleCharsetVGA();
        }
        throw new ConsoleCharsetException();
    }
}
