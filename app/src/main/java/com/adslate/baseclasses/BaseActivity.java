package com.adslate.baseclasses;

import android.app.Activity;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;


import com.adslate.R;
import com.adslate.storage.AdSlateDB;
import com.adslate.storage.AdSlatePreferences;
import com.adslate.utils.NetworkStatus;

/**
 * Created by pooja.b on 18-11-2015.
 */
public class BaseActivity extends Activity
{


    public ProgressDialog mProgressDialog;
    public NetworkStatus mNetworkStatus;
    public AdSlatePreferences mPref;
    private AdSlateDB mAdSlateDB;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mNetworkStatus = new NetworkStatus(BaseActivity.this);
        mPref = new AdSlatePreferences(BaseActivity.this);
        mProgressDialog = new ProgressDialog(BaseActivity.this);
        mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Please wait...");

    }



    public void mShowDialog(Context context, String title, String message, Boolean cancelable,Boolean logout)
    {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert);
        dialog.show();

        Window window = dialog.getWindow();

        TextView mTitle = (TextView)window.findViewById(R.id.xTvTitle);
        TextView mMessage = (TextView)window.findViewById(R.id.xTvMessage);
        TextView mOk = (TextView)window.findViewById(R.id.xTvOK);
        TextView mYes = (TextView)window.findViewById(R.id.xTvYes);
        TextView mNo = (TextView)window.findViewById(R.id.xTvNo);

        mYes.setVisibility(View.GONE);
        mNo.setVisibility(View.GONE);

        mTitle.setText(title);
        mMessage.setText(message);
        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        if(cancelable)
        {
            dialog.setCancelable(true);
        }
        else
        {
            dialog.setCancelable(false);
        }

        if(logout)
        {

            mOk.setVisibility(View.GONE);
            mYes.setVisibility(View.VISIBLE);
            mNo.setVisibility(View.VISIBLE);

            mAdSlateDB = AdSlateDB.GET_INSTANCE(context);

            mYes.setOnClickListener(new View.OnClickListener()
            {


                @Override
                public void onClick(View view)
                {
                    mPref.mSaveLoginStatus(false);
                    mAdSlateDB.mDeleteTable(mAdSlateDB.mUserTable);
                    mAdSlateDB.mDeleteTable(mAdSlateDB.mSpaceType);
                    mAdSlateDB.close();
                    mPref.mSaveLoginStatus(false);
                    mPref.mSaveSpaceTypeLstSync("");

                    Log.e("SPACELSTSYN", "" + mPref.mGetSpaceTypeLstSyn());

                }


            });

            mNo.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    dialog.dismiss();
                }
            });
        }
    }
}
