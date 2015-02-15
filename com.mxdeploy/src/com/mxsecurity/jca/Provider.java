package com.mxsecurity.jca;

import java.util.Properties;

public class Provider extends Properties {
    String name;
    double version;
    String info;

    protected Provider(String name, double version, String info) {
        this.name    = name;
        this.version = version;
        this.info    = info;
    }

    public void clear() {}

    /*
    public Set entrySet() {
    }
    */

    public String getInfo() {
        return info;
    }

    public String getName() {
        return name;
    }

    public double getVersion() {
        return version;
    }

    /*
    public Set keySet() {
    }
    */

    /*
    public void load(InputStream inStream) {
    }
    */

    /*
    public Object put(Object key, Object value) {
    return null;
    }
    */

    /*
    public void putAll(Map t) {
    }
    */

    public Object remove
        (Object key) {
        return null;
    }

    public String toString() {
        return name + " (" + version + ")";
    }

    /*
    public Collection values() {
    }
    */
}
