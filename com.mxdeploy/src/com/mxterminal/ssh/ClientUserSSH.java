package com.mxterminal.ssh;

import java.io.IOException;
import java.net.Socket;

public interface ClientUserSSH {
    public String  getSrvHost() throws IOException;
    public int     getSrvPort();
    public Socket  getProxyConnection() throws IOException;
    public String  getDisplay();
    public int     getMaxPacketSz();
    public int     getAliveInterval();
    public int     getCompressionLevel();

    public boolean wantX11Forward();
    public boolean wantPTY();

    public InteractorSSH getInteractor();
}
