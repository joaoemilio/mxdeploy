package com.mxterminal.console.charset;

/**
 * Interface for classes recoding between external encodings
 */
public interface ConsoleCharsetFilter {
    public String convertFrom(byte b);
    public String convertFrom(byte[] c, int off, int len);

    public byte[] convertTo(char c);
    public byte[] convertTo(byte[] b);
}
