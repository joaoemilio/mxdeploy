 
 
  
  
  
  
  
  
  
  
  
  
 
  

package com.mxterminal.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface PduSSH {
    public void   writeTo(OutputStream out) throws IOException;
    public void   readFrom(InputStream in) throws IOException;
    public PduSSH createPdu() throws IOException;

    public byte[] rawData();
    public void   rawSetData(byte[] raw);
    public int    rawOffset();
    public int    rawSize();
    public void   rawAdjustSize(int size);

    //  public SSHPdu preProcess();
    //  public SSHPdu postProcess();
}
