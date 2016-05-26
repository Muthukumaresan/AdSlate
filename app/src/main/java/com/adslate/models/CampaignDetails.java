package com.adslate.models;

/**
 * Created by pooja.b on 21-11-2015.
 */
public class CampaignDetails
{

    String /*mImageUrl,*/ mName,mSubscribedNumber,mThresholdNumber,mDaysLeft,mLocation;



    public CampaignDetails(String mName, String mSubscribedNumber, /*String mImageUrl,*/ String mThresholdNumber, String mDaysLeft, String mLocation) {
        this.mName = mName;
        this.mSubscribedNumber = mSubscribedNumber;
       // this.mImageUrl = mImageUrl;
        this.mThresholdNumber = mThresholdNumber;
        this.mDaysLeft = mDaysLeft;
        this.mLocation = mLocation;
    }


   /* public String getmImageUrl() {
        return mImageUrl;
    }
*/
    public String getmName() {
        return mName;
    }

    public String getmSubscribedNumber() {
        return mSubscribedNumber;
    }

    public String getmThresholdNumber() {
        return mThresholdNumber;
    }

    public String getmDaysLeft() {
        return mDaysLeft;
    }

    public String getmLocation() {
        return mLocation;
    }

}
