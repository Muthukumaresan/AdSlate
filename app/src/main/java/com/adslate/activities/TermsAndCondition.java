package com.adslate.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.adslate.R;
import com.adslate.baseclasses.BaseActivity;

public class TermsAndCondition extends BaseActivity implements View.OnClickListener {

    private RelativeLayout mRelLayBack;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_condition);

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
