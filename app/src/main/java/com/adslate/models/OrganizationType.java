package com.adslate.models;

/**
 * Created by pooja.b on 18-12-2015.
 */
public class OrganizationType
{
    String mId,mName;

    public OrganizationType(String mId, String mName)
    {
        this.mId = mId;
        this.mName = mName;
    }

    public String getmId() {
        return mId;
    }

    public String getmName() {
        return mName;
    }
}
