
package com.mxsecurity.asn1;

import java.util.Properties;
import java.util.Hashtable;
import java.util.Enumeration;

public class ASN1OIDRegistry extends Properties {

    private static ASN1OIDRegistry registry = new ASN1OIDRegistry();
    private static Hashtable       modules  = new Hashtable();

    public void putName(String oid, String name, String shortName) {
        putName(oid, name);
        putShortName(oid, shortName);
    }

    public void putShortName(String oid, String shortName) {
        put("shortname." + oid, shortName);
        put("shortname." + shortName, oid);
    }

    public void putName(String oid, String name) {
        put("name." + oid, name);
        put("name." + name, oid);
    }

    public static synchronized Class lookupType(ASN1OID oid) {
        String id       = oid.getString();
        String type     = registry.getProperty(id);
        Class  typeImpl = null;
        if(type != null) {
            if(type.indexOf('.') == -1) {
                type = "com.mxsecurity.asn1." + type;
            }
            try {
                typeImpl = Class.forName(type);
            } catch (ClassNotFoundException e) {
                throw new Error("Can't find class '" + type +
                                "' registered in ASN1OIDRegistry with '" +
                                id + "'");
            }
        }
        return typeImpl;
    }

    public static synchronized String lookupName(String id) {
        id = "name." + id;
        return registry.getProperty(id);
    }

    public static synchronized String lookupShortName(String id) {
        id = "shortname." + id;
        return registry.getProperty(id);
    }

    public static void register(ASN1OID oid, String type) {
        register(oid.getString(), type);
    }

    public static synchronized void register(String oid, String type) {
        registry.put(oid, type);
    }

    public static void addModule(String moduleName) {
        // !!! TODO circular references will loop !!!
        try {
            ASN1OIDRegistry registeredTypes = (ASN1OIDRegistry)
                                              Class.forName(moduleName + ".RegisteredTypes").newInstance();
            addModule(moduleName, registeredTypes);
        } catch (Exception e) {
            throw new Error("In ASN1OIDRegistry: " + e.getMessage());
        }
    }

    public static void addModule(String moduleName, Properties moduleTypes) {
        // !!! OUCH synchronization
        if(modules.get(moduleName) != null) {
            /* already added... */
            return;
        }
        modules.put(moduleName, moduleTypes);
        Enumeration e = moduleTypes.propertyNames();
        while(e.hasMoreElements()) {
            String key = (String)e.nextElement();
            register(key, moduleTypes.getProperty(key));
        }
    }

}
