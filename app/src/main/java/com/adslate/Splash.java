package com.adslate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.adslate.activities.Help;
import com.adslate.activities.Home;
import com.adslate.activities.Login;
import com.adslate.activities.WhoYouAre;
import com.adslate.baseclasses.BaseActivity;
import com.adslate.services.RegistrationIntentService;
import com.adslate.storage.AdSlateDB;
import com.adslate.utils.StaticUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;


public class Splash extends BaseActivity
{


    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";
    private GCMIntentReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        if (mPref.mGetGCMId().equalsIgnoreCase("")) {
            if (mNetworkStatus.isNetworkAvailable())
            {
                mReceiver = new GCMIntentReceiver();
                LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("registrationComplete"));

                if (checkPlayServices())
                {
                    Intent intent = new Intent(this, RegistrationIntentService.class);
                    startService(intent);
                }
            } else
            {
                mShowDialog(this, StaticUtils.NO_INTERNET_TITLE, StaticUtils.NO_INTERNET_MESSAGE,true,false);
            }
        }
        else
        {
            moveToLoginScreen();
        }


    }

    private boolean checkPlayServices()
    {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.e("", "This device is not supported.");
                //finish();
                mShowDialog(Splash.this, "", StaticUtils.DEVICE_NOT_SUPPORTED,true,false);
            }
            return false;
        }
        return true;

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        checkPlayServices();
    }

    @Override
    protected void onDestroy()
    {
        if (mReceiver != null)
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        super.onDestroy();
    }


    public class GCMIntentReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (!mPref.mGetGCMId().equalsIgnoreCase(""))
            {
                moveToLoginScreen();
            }
            else
            {
                mShowDialog(Splash.this, "", StaticUtils.GCM_REGISTRATION_ERROR,true,false);
            }
        }
    }

    private void moveToLoginScreen()
    {

        if(mPref.mGetHelpPagesStatus())
        {

            if(mPref.mGetLoginStatus()) {


                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(Splash.this, Home.class);
                        startActivity(intent);
                        finish();

                    }
                }, 2000);

            }
            else
            {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(Splash.this, WhoYouAre.class);
                        startActivity(intent);
                        finish();

                    }
                }, 2000);

            }

        }
        else
        {

               Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run()
                    {
                        Intent intent = new Intent(Splash.this,Help.class);
                        startActivity(intent);
                        finish();

                    }
                },2000);

            }


    }

}
