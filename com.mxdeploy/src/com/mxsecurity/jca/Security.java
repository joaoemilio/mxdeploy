
package com.mxsecurity.jca;

import java.util.Vector;
import java.util.Enumeration;

public final class Security {

    private static Vector providers;

    static {
        providers = new Vector();
        providers.addElement(new com.mxsecurity.SecBright());
    };

    public static int addProvider(Provider provider) {
        if(getProvider(provider.getName()) != null)
            return -1;
        providers.addElement(provider);
        return providers.size();
    }

    public static Provider getProvider(String name) {
        Enumeration e = providers.elements();
        while (e.hasMoreElements()) {
            Provider prov = (Provider)e.nextElement();
            if (prov.getName().equals(name))
                return prov;
        }
        return null;
    }

    public static String getAlgorithmProperty(String algName, String propName) {
        return null;
    }

    public static String getProperty(String key) {
        return null;
    }

    public static Provider[] getProviders() {
        Provider[]  list = new Provider[providers.size()];
        Enumeration e    = providers.elements();
        int         i    = 0;
        while(e.hasMoreElements()) {
            list[i++] = (Provider)e.nextElement();
        }
        return list;
    }

    /*
    public static Provider[] getProviders(Map filter) {
    }
    */

    public static Provider[] getProviders(String filter) {
        return null;
    }

    public static int insertProviderAt(Provider provider, int position) {
        return 0;
    }

    public static void removeProvider(String name) {}

    public static void setProperty(String key, String datum) {}

}
