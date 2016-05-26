package com.adslate.models;

/**
 * Created by pooja.b on 17-12-2015.
 */
public class SpaceType
{
    String mId,mName,mUserType;

    public SpaceType(String mId, String mName,String mUserType)
    {
        this.mId = mId;
        this.mName = mName;
        this.mUserType = mUserType;
    }

    public String getmId()
    {
        return mId;
    }

    public String getmName()
    {
        return mName;
    }

    public String getmUserType()
    {
        return mUserType;
    }
}
