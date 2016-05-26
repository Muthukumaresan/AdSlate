package com.adslate.storage;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by pooja.b on 27-11-2015.
 */
public class AdSlatePreferences
{
    public static AdSlatePreferences mAdSlatePref;
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;
    private String mFileName = "AdSlate.Preferences";



    //Preference keys
    private String mRegistrationStatus = "registrationStatus";
    private String mGCMRegId = "gcmRegId";
    private String mOrgTypeLastSynTime = "OrgTypeLstSync";
    private String mSpaceTypeLastSyncTime = "getSpaceType";
    private String mUserType = "getUserType";
    private String mLoginStatus = "getLoginStatus";
    private String mHelpPages = "showHelpPages";


    public AdSlatePreferences(Context context)
    {
        this.mPref = context.getSharedPreferences(mFileName, Activity.MODE_PRIVATE);
        this.mEditor = mPref.edit();
    }


    public static AdSlatePreferences GET_PREFERENCES(Context context)
    {
        if(mAdSlatePref == null)

            mAdSlatePref = new AdSlatePreferences(context);
        return mAdSlatePref;

    }

    public void mSaveUserType(String val)
    {
        mEditor.putString(mUserType,val);
        mEditor.commit();

    }



    public void mSaveLoginStatus(Boolean val)
    {
        mEditor.putBoolean(mLoginStatus,val);
        mEditor.commit();
    }

    public void mSaveSpaceTypeLstSync(String val)
    {
        mEditor.putString(mSpaceTypeLastSyncTime, val);
        mEditor.commit();
    }

    public String mGetSpaceTypeLstSyn()
    {
        String spaceTypeLstSync = mPref.getString(mSpaceTypeLastSyncTime,"");
        return spaceTypeLstSync;
    }

    public void mSaveGCMId(String val)
    {
        mEditor.putString(mGCMRegId,val);
        mEditor.commit();
    }



    public void mSaveOrgTypeLastSync(String val)
    {
        mEditor.putString(mOrgTypeLastSynTime, val);
        mEditor.commit();
    }

    public void mSaveHelpPagesStatus(Boolean val)
    {
        mEditor.putBoolean(mHelpPages, val);
        mEditor.commit();
    }



    public String mGetGCMId()
    {
        String gcmId = mPref.getString(mGCMRegId,"");
        return gcmId;
    }

    public String mGetOrgTypeLastSync()
    {
        String commonDataLstSync = mPref.getString(mOrgTypeLastSynTime,"");
        return commonDataLstSync;
    }

    public String mGetUserType()
    {
        String userType = mPref.getString(mUserType,"");
        return userType;
    }

    public  Boolean mGetLoginStatus()
    {
        Boolean loginstatus = mPref.getBoolean(mLoginStatus, false);
        return loginstatus;
    }

    public  Boolean mGetHelpPagesStatus()
    {
        Boolean helpPagesstatus = mPref.getBoolean(mHelpPages,false);
        return helpPagesstatus;
    }

}
