package com.mxterminal.ssh;

import java.io.IOException;

import com.mxsecurity.jca.interfaces.RSAPublicKey;



public interface Authenticator {
    public String        getUsername(ClientUserSSH origin) throws IOException;
    public String        getPassword(ClientUserSSH origin) throws IOException;
    public String        getChallengeResponse(ClientUserSSH origin, String challenge) throws IOException;
    public int[]         getAuthTypes(ClientUserSSH origin);
    public int           getCipher(ClientUserSSH origin);
    public RSAKeyFileSSH getIdentityFile(ClientUserSSH origin) throws IOException;
    public String        getIdentityPassword(ClientUserSSH origin) throws IOException;
    public boolean       verifyKnownHosts(RSAPublicKey hostPub) throws IOException;
}
