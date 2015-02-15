package com.mxterminal.util;

import java.util.Iterator;
import java.util.Map;

public class HeaderFields
{

    public HeaderFields(Map headerFields)
    {
        this.headerFields = headerFields;
        getValues();
    }

    public HeaderFields(String mediaType, String charSet)
    {
        this.mediaType = mediaType;
        this.charSet = charSet;
    }

    public String getMediaType()
    {
        return mediaType;
    }

    public String getCharSet()
    {
        return charSet;
    }

    private void getValues()
    {
        Iterator it = headerFields.keySet().iterator();
        do
        {
            if(!it.hasNext())
                break;
            Object obHeader = it.next();
            if(obHeader != null && (obHeader instanceof String))
            {
                String key = (String)obHeader;
                if(key.equalsIgnoreCase("content-type"))
                {
                    String contentType = headerFields.get(key).toString();
                    String type[] = contentType.split(";");
                    mediaType = type[0];
                    if(mediaType.startsWith("["))
                        mediaType = mediaType.substring(1, mediaType.length());
                    if(mediaType.endsWith("]"))
                        mediaType = mediaType.substring(0, mediaType.length() - 1);
                    if(contentType != null && contentType.toLowerCase().indexOf("charset=") >= 0)
                        charSet = contentType.substring(contentType.toLowerCase().indexOf("charset=") + 8, contentType.length() - 1);
                    else
                        charSet = "ISO-8859-1";
                }
            }
        } while(true);
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("mediaType:").append(mediaType).append(" ");
        sb.append("charSet:").append(charSet);
        return sb.toString();
    }

    private Map headerFields;
    private String mediaType;
    private String charSet;
}