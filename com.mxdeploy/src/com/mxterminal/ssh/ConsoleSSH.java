package com.mxterminal.ssh;

import com.mxterminal.channel.ChannelController;
import com.mxterminal.console.ConsoleWindows;

public interface ConsoleSSH {
    public ConsoleWindows getTerminal();

    public void stdoutWriteString(byte[] str);
    public void stderrWriteString(byte[] str);

    public void print(String str);
    public void println(String str);

    public void serverConnect(ChannelController controller,
                              CipherSSH sndCipher);
    public void serverDisconnect(String reason);
}
