package com.mxsecurity.asn1;

public class ASN1BMPString extends ASN1CharString {

    public ASN1BMPString() {
        super(ASN1.TAG_BMPSTRING);
    }

    public void setValue(String string) {
        char[] s = string.toCharArray();
        byte[] tmp = new byte[s.length * 2];
        for(int i = 0; i < s.length; i++) {
            tmp[i * 2]       = (byte)(s[i] >>> 8);
            tmp[(i * 2) + 1] = (byte)(s[i] & 0xff);
        }
        setRaw(tmp);
    }

    public String getValue() {
        char[] string = new char[value.length / 2];
        for(int i = 0; i < string.length; i++) {
            string[i] = (char)((value[i * 2] << 8) +
                               (value[(i * 2) + 1] & 0xff));
        }
        return new String(string);
    }

}
