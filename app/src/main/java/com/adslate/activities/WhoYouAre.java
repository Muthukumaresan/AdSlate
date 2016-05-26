package com.adslate.activities;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adslate.R;
import com.adslate.baseclasses.BaseActivity;
import com.adslate.utils.StaticUtils;

public class WhoYouAre extends BaseActivity implements View.OnClickListener {

    private RelativeLayout mRelLayIndividual, mRelLayEnterprise;
    private TextView mLogin,mRegister;
    private Boolean mUserTypeChosen = false;
    private ImageView mBack;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_who_you_are);
        mInitialiseViews();

    }


    private void mInitialiseViews()
    {

        mRelLayIndividual = (RelativeLayout)findViewById(R.id.xRelLayIndividual);
        mRelLayEnterprise = (RelativeLayout)findViewById(R.id.xRelLayEnterprise);


        mLogin = (TextView)findViewById(R.id.xTvLogin);
        mRegister = (TextView)findViewById(R.id.xTvRegister);
        mBack = (ImageView)findViewById(R.id.xIvBackButton);


        mRelLayIndividual.setOnClickListener(this);
        mRelLayEnterprise.setOnClickListener(this);
        mLogin.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View view)
    {

        if(view == mRelLayIndividual)
        {
            Log.e("USERTYPE", "" + mPref.mGetUserType());
            mUserTypeChosen = true;
            mPref.mSaveUserType("1");// 1 for individual
            Toast.makeText(WhoYouAre.this,"User Type chosen was Individual",Toast.LENGTH_SHORT).show();
        }
        else if(view == mRelLayEnterprise)
        {
            Log.e("USERTYPE", "" + mPref.mGetUserType());
            mUserTypeChosen = true;
            mPref.mSaveUserType("2");// 2 for enterprise
            Toast.makeText(WhoYouAre.this,"User Type chosen was Enterprise",Toast.LENGTH_SHORT).show();
        }
        else if(view == mLogin)
        {

            if(mUserTypeChosen)
            {
                Intent intent = new Intent(WhoYouAre.this,Login.class);
                startActivity(intent);
                //finish();
            }
            else
            {
                mShowDialog(WhoYouAre.this,"",StaticUtils.CHOOSE_USER_TYPE,true,false);
            }

        }
        else if(view == mRegister)
        {

            if(mUserTypeChosen)
            {
                Intent intent = new Intent(WhoYouAre.this,Registration1.class);
                startActivity(intent);
                //finish();
            }
            else
            {
                mShowDialog(WhoYouAre.this,"",StaticUtils.CHOOSE_USER_TYPE,true,false);
            }


        }
        else if(view == mBack)
        {
            finish();
        }

    }
}
