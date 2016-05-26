package com.adslate.baseclasses;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.adslate.R;
import com.adslate.activities.Login;
import com.adslate.activities.WhoYouAre;
import com.adslate.storage.AdSlateDB;
import com.adslate.storage.AdSlatePreferences;

/**
 * Created by pooja.b on 18-11-2015.
 */
public class BaseFragmentActivity extends FragmentActivity
{

    private AdSlateDB mAdSlateDB ;
   public AdSlatePreferences mPref;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mPref = new AdSlatePreferences(BaseFragmentActivity.this);
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
                    dialog.dismiss();
                    mAdSlateDB.mDeleteTable(mAdSlateDB.mUserTable);
                    mAdSlateDB.mDeleteTable(mAdSlateDB.mSpaceType);
                    mAdSlateDB.close();
                    mPref.mSaveSpaceTypeLstSync("");

                    Log.e("SPACELSTSYN", "" +mPref.mGetSpaceTypeLstSyn());

                    Intent intent = new Intent(getApplicationContext(), WhoYouAre.class);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
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
