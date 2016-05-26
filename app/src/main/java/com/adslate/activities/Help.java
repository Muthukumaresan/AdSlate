package com.adslate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.adslate.R;
import com.adslate.baseclasses.BaseFragmentActivity;
import com.adslate.fragments.CampaignsFragment;
import com.adslate.fragments.IndividualEnterpriseFragment;
import com.adslate.fragments.SpacesFragment;
import com.adslate.fragments.WalletFragment;
import com.adslate.utils.StaticUtils;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;


public class Help extends BaseFragmentActivity implements View.OnClickListener {

    private ViewPager mViewPager;
    private CirclePageIndicator mCirclePageIndicator;
    private HelpAdapter mHelpAdapter;
    private TextView mTvSkip;
    ArrayList<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        mInitialiseViews();

    }

    private void mInitialiseViews()
    {

        mTvSkip = (TextView)findViewById(R.id.xTvSkip);
        mViewPager = (ViewPager)findViewById(R.id.xVPager);
        mCirclePageIndicator = (CirclePageIndicator)findViewById(R.id.indicators);

        mTvSkip.setOnClickListener(this);


        mFragments = new ArrayList<Fragment>();
        mFragments.add(new CampaignsFragment());
        mFragments.add(new SpacesFragment());
        mFragments.add(new WalletFragment());
        mFragments.add(new IndividualEnterpriseFragment());

        mHelpAdapter = new HelpAdapter(getSupportFragmentManager(),mFragments);
        mViewPager.setAdapter(mHelpAdapter);
        mCirclePageIndicator.setViewPager(mViewPager);


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {

                if (position == mFragments.size()-1)
                    mTvSkip.setText("Done");
                else
                    mTvSkip.setText("Skip");
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });


    }

    @Override
    public void onClick(View view)
    {

        if(view == mTvSkip)
        {

            mPref.mSaveHelpPagesStatus(true);
            Intent intent = new Intent(Help.this,WhoYouAre.class);
            startActivity(intent);
            finish();
        }

    }

    private class HelpAdapter extends FragmentPagerAdapter
    {

      ArrayList<Fragment>mAFragments;

        public HelpAdapter(FragmentManager fm, ArrayList<Fragment> mFragments)
        {
            super(fm);
            mAFragments = mFragments;

        }

        @Override
        public Fragment getItem(int position)
        {
            Fragment fragment = mAFragments.get(position);
            return fragment;
        }

        @Override
        public int getCount()
        {
            return mAFragments.size();
        }
    }
}
