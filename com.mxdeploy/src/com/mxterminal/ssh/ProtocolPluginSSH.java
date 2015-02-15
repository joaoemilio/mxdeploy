package com.mxterminal.ssh;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import com.mxterminal.channel.ChannelController;

public class ProtocolPluginSSH {

    static Hashtable plugins = new Hashtable();

    static {
        ProtocolPluginSSH.addPlugin("general", new ProtocolPluginSSH());
//        try {
//            SSHProtocolPlugin.addPlugin("ftp", new SSHFtpPlugin());
//        } catch (Throwable e) {
//            System.out.println("FTP plugin not found, disabled");
//        }
    }

    public static ProtocolPluginSSH getPlugin(String name) {
        return (ProtocolPluginSSH)plugins.get(name);
    }

    public static void addPlugin(String name, ProtocolPluginSSH plugin) {
        plugins.put(name, plugin);
    }

    public static void initiateAll(ClientSSH client) {
        ProtocolPluginSSH plugin;
        Enumeration e = plugins.elements();
        while(e.hasMoreElements()) {
            plugin = (ProtocolPluginSSH)e.nextElement();
            plugin.initiate(client);
        }
    }

    public void initiate(ClientSSH client) {}

    public ListenChannelSSH localListener(String localHost, int localPort,
                                          String remoteHost, int remotePort,
                                          ChannelController controller) throws IOException {
        return new ListenChannelSSH(localHost, localPort, remoteHost, remotePort, controller);
    }

    public void remoteListener(int remotePort, String localHost, int localPort,
                               ChannelController controller) {}

}
