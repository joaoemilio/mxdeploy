 
 
  
  
  
  
  
  
  
  
  
  
 
  

package com.mxterminal.console;

/**
 * Interface for classes interested in data sent to a terminal
 * window. That is data sent from the server end.
 */
public interface ConsoleOutputListener {
    /**
     * A single character which is written to the terminal.
     *
     * @param c charcter written
     */
    public void write(char c);
}
