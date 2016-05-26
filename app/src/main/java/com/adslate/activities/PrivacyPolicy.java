package com.adslate.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.adslate.R;

public class PrivacyPolicy extends Activity implements View.OnClickListener {

    private RelativeLayout mRelLayBack;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);


        mRelLayBack = (RelativeLayout)findViewById(R.id.xRelLayBack);
        mRelLayBack.setOnClickListener(this);

    }


    @Override
    public void onClick(View view)
    {

        if(view == mRelLayBack)
        {
            finish();
        }

    }
}
