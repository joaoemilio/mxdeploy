package com.mxterminal.ssh2;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.mxterminal.channel.StreamChannel;


/**
 * Implements a stream filter which handles the X11 authentication
 * cookie replacement.
 */
public class X11FilterSSH2 implements StreamFilterSSH2, StreamFilterFactory {

    protected final static String X11AUTH_PROTO = "MIT-MAGIC-COOKIE-1";

    class X11Output extends FilterOutputStream {

        byte[]  buf;
        int     idx;
        int     wantedLen;
        int     protoLen;
        int     cookieLen;
        boolean readAuth;

        public X11Output(OutputStream toBeFiltered) {
            super(toBeFiltered);
            this.buf       = new byte[1024];
            this.readAuth  = false;
            this.idx       = 0;
            this.wantedLen = 12; // header len
        }

        public void write(byte b[], int off, int len) throws IOException {
            if(!readAuth) {
                int n;

                // Read header of authentication packet
                //
                if(idx < 12) {
                    n = readMore(b, off, len);
                    len -= n;
                    off += n;
                    if(wantedLen == 0) {
                        if(buf[0] == 0x42) {
                            protoLen  =
                                ((buf[6] & 0xff) << 8) | (buf[7] & 0xff);
                            cookieLen =
                                ((buf[8] & 0xff) << 8) | (buf[9] & 0xff);
                        } else if(buf[0] == 0x6c) {
                            protoLen  =
                                ((buf[7] & 0xff) << 8) | (buf[6] & 0xff);
                            cookieLen =
                                ((buf[9] & 0xff) << 8) | (buf[8] & 0xff);
                        } else {
                            throw new IOException("Corrupt X11 authentication");
                        }
                        wantedLen  = (protoLen + 0x03) & ~0x03;
                        wantedLen += (cookieLen + 0x03) & ~0x03;
                        if(wantedLen + idx > buf.length) {
                            throw new IOException("Corrupt X11 authentication");
                        }
                        if(wantedLen == 0) {
                            throw
                            new IOException("No X11 authentication cookie");
                        }
                    }
                }

                // Read payload of authentication packet
                //
                if(len > 0) {
                    n = readMore(b, off, len);
                    len -= n;
                    off += n;
                    if(wantedLen == 0) {
                        byte[] fakeCookie = connection.getX11FakeCookie();
                        String protoStr   = new String(buf, 12, protoLen);
                        byte[] recCookie  = new byte[fakeCookie.length];

                        protoLen = ((protoLen + 0x03) & ~0x03);

                        System.arraycopy(buf, 12 + protoLen,
                                         recCookie, 0, fakeCookie.length);
                        if(!X11AUTH_PROTO.equals(protoStr) ||
                                !compareCookies(fakeCookie, recCookie,
                                                fakeCookie.length)) {
                            throw new IOException("X11 authentication failed");
                        }
                        byte[] realCookie = connection.getX11RealCookie();
                        if(realCookie.length != cookieLen) {
                            throw new IOException("X11 wrong cookie length");
                        }
                        System.arraycopy(realCookie, 0, buf, 12 + protoLen,
                                         realCookie.length);
                        readAuth = true;
                        out.write(buf, 0, idx);
                        buf = null;
                    }
                }

                if(!readAuth || len == 0) {
                    return;
                }
            }

            out.write(b, off, len);
        }

        private boolean compareCookies(byte[] src, byte[] dst, int len) {
            int i = 0;
            for(; i < len; i++) {
                if(src[i] != dst[i]) {
                    break;
                }
            }
            return i == len;
        }

        private int readMore(byte[] b, int off, int len) {
            if(len > wantedLen) {
                System.arraycopy(b, off, buf, idx, wantedLen);
                idx      += wantedLen;
                len       = wantedLen;
                wantedLen = 0;
            } else {
                System.arraycopy(b, off, buf, idx, len);
                idx       += len;
                wantedLen -= len;
            }
            return len;
        }

    }

    private static X11FilterSSH2 factoryInstance;

    public X11FilterSSH2() {
        //
        // Factory instance constructor
        //
    }

    public static synchronized X11FilterSSH2 getFilterFactory() {
        if(factoryInstance == null) {
            factoryInstance = new X11FilterSSH2();
        }
        return factoryInstance;
    }

    protected ConnectionSSH2    connection;
    protected StreamChannel channel;
    protected X11Output         x11Out;

    protected X11FilterSSH2(ConnectionSSH2 connection,
                            StreamChannel channel) {
        this.connection = connection;
        this.channel    = channel;
    }

    public StreamFilterSSH2 createFilter(ConnectionSSH2 connection,
                                         StreamChannel channel) {
        return new X11FilterSSH2(connection, channel);
    }

    public InputStream getInputFilter(InputStream toBeFiltered) {
        return toBeFiltered;
    }

    public OutputStream getOutputFilter(OutputStream toBeFiltered) {
        this.x11Out = new X11Output(toBeFiltered);
        return this.x11Out;
    }

}
