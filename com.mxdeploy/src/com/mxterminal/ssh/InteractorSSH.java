package com.mxterminal.ssh;

import java.io.IOException;

public interface InteractorSSH {
    public void    startNewSession(ClientSSH client);
    public void    sessionStarted(ClientSSH client);

    public void    connected(ClientSSH client);
    public void    open(ClientSSH client);
    public void    disconnected(ClientSSH client, boolean graceful);

    public void    report(String msg);
    public void    alert(String msg);

    public void    propsStateChanged(PropertyHandlerSSH props);

    public boolean askConfirmation(String message, boolean defAnswer);
    public boolean licenseDialog(String license);

    public boolean quietPrompts();
    public String  promptLine(String prompt, String defaultVal) throws IOException;
    public String  promptPassword(String prompt) throws IOException;

    public boolean isVerbose();
}
