package com.adslate.models;

/**
 * Created by pooja.b on 05-02-2016.
 */
public class SpacesDetails
{
    String quotedrate,Status;

    public SpacesDetails(String quotedrate, String status)
    {
        this.quotedrate = quotedrate;
        Status = status;
    }

    public String getQuotedrate()
    {
        return quotedrate;
    }

    public String getStatus()
    {
        return Status;
    }
}
