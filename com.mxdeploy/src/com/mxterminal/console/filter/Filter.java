
package com.mxterminal.console.filter;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;

import com.mxsecurity.util.HexDump;
import com.mxterminal.console.charset.ConsoleCharsetFilter;

/**
 * This class handles converting between javas internal
 * representation (utf-16) and various external encodings.
 */
public class Filter implements ConsoleCharsetFilter {
    final static boolean DEBUG = false;
    CharsetEncoder encoder;
    CharsetDecoder decoder;
    ByteBuffer decode_in;
    CharBuffer decode_out;
    CharBuffer encode_in;
    ByteBuffer encode_out;

    private Filter(String name) {
        Charset charset = Charset.forName(name);
        if (DEBUG) {
            System.err.println("Filter for " + name);
        }
        encoder = charset.newEncoder();
        encoder.onMalformedInput(CodingErrorAction.REPLACE);
        encoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
        decoder = charset.newDecoder();
        decoder.onMalformedInput(CodingErrorAction.REPLACE);
        decoder.onUnmappableCharacter(CodingErrorAction.REPLACE);

        decode_in = ByteBuffer.allocate(16);
        decode_out = CharBuffer.allocate(16);
        encode_in = CharBuffer.allocate(16);
        encode_out = ByteBuffer.allocate(16);

    }

    public static Filter create(String name) {
        try {
            return new Filter(name);
        } catch (IllegalCharsetNameException e) {
            return null;
        } catch (UnsupportedCharsetException e) {
            return null;
        }
    }

    private String intConvertFrom(ByteBuffer b) {
        if (DEBUG) {
            HexDump.print(System.out, "convertFrom in", true,
                          b.array(), 0, b.position());
        }

        String result = null;        
        CoderResult status;
        b.flip();
        do {
            decode_out.clear();
            status = decoder.decode(b, decode_out, false);
            decode_out.flip();
            if (result == null) {
                result = decode_out.toString();
            } else {
                result += decode_out.toString();
            }
        } while (status == CoderResult.OVERFLOW);
        b.compact();

        if (DEBUG) {
            byte[] r = result.getBytes();
            HexDump.print(System.out, "convertFrom out", true,
                          r, 0, r.length);
        }
        return result;
    }

    private void prepareDecodeIn(int needed) {
        if (decode_in.capacity()-decode_in.position() < needed) {
            ByteBuffer newBuffer = ByteBuffer.allocate(
                decode_in.capacity()+needed);
            decode_in.flip();
            while (decode_in.hasRemaining()) {
                newBuffer.put(decode_in.get());
            }
            decode_in = newBuffer;
        }
    }

    public String convertFrom(byte b) {
        prepareDecodeIn(1);
        decode_in.put(b);
        return intConvertFrom(decode_in);
    }

    public String convertFrom(byte[] c, int off, int len) {
        prepareDecodeIn(len*2);
        decode_in.put(c, off, len);
        return intConvertFrom(decode_in);
    }


    private byte[] intConvertTo(CharBuffer in) {
        in.flip();
        if (DEBUG) {
            byte[] r = in.toString().getBytes();
            HexDump.print(System.out, "convertTo in", true,
                          r, 0, r.length);
        }

        byte[] result = null;
        CoderResult status;
        do {
            encode_out.clear();
            status = encoder.encode(in, encode_out, false);
            if (result == null) {
                result = new byte[encode_out.position()];
                encode_out.rewind();
                encode_out.get(result);
            } else {
                byte[] result2 = new byte[result.length+encode_out.position()];
                encode_out.rewind();
                System.arraycopy(result, 0, result2, 0, result.length);
                encode_out.get(result2, result.length, encode_out.limit());
                result = result2;
            }
        } while (status == CoderResult.OVERFLOW);

        if (DEBUG) {
            HexDump.print(System.out, "convertTo out", true,
                          result, 0, result.length);
        }

        return result;
    }

    public byte[] convertTo(char c) {
        encode_in.clear();
        try{
           encode_in.put(c);
        } catch (BufferOverflowException e){
        	System.out.println("BufferOverflowException to "+String.valueOf(c));
        	e.printStackTrace();
        }
        return intConvertTo(encode_in);
    }

    public byte[] convertTo(byte[] b) {
        if (encode_in.capacity() < b.length) {
            encode_in = CharBuffer.allocate(b.length);
        } else {
            encode_in.clear();
        }
        for (int i=0; i<b.length; i++) {
            encode_in.put((char)b[i]);
        }
        return intConvertTo(encode_in);
    }

}
