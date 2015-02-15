package com.mxsecurity.mac;

public final class HMACSHA1_96 extends HMACSHA1 {
    public HMACSHA1_96() {
        super();
        macLength = 96 / 8;
    }
}
