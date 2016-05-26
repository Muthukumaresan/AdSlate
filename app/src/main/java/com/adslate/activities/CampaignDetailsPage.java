package com.adslate.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adslate.R;
import com.adslate.baseclasses.BaseActivity;
import com.adslate.baseclasses.BaseFragmentActivity;
import com.adslate.fragments.CampaignDetails.CampaignDetailAbtCampaign;
import com.adslate.fragments.CampaignDetails.CampaignDetailContact;

import java.util.ArrayList;
import java.util.List;

public class CampaignDetailsPage extends BaseFragmentActivity implements View.OnClickListener
{
    private ImageView mIvBackButton;
    private TextView mTvCamNameHeader,mTvCamName,mTvSubscribeNow;
    private ViewPager mVpImages,mVpCamDetails;
    private TabLayout mTabCamDetails;

    private CampaignDetailAdapter campaignDetailsAdapter;
    private ImageAdapter imageAdapter;

    private String campaignName;
    private ArrayList<Integer> camImages;
    private String flag ;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_details);

        getIntentValues();
        initialiseViews();

        campaignDetailsAdapter = new CampaignDetailAdapter(getSupportFragmentManager());
        mVpCamDetails.setAdapter(campaignDetailsAdapter);
        mTabCamDetails.setupWithViewPager(mVpCamDetails);

        camImages = new ArrayList<Integer>();
        camImages.add(R.drawable.background1);
        camImages.add( R.drawable.background2);
        camImages.add(R.drawable.background3);
        camImages.add(R.drawable.background6);
        camImages.add(R.drawable.background5);

        imageAdapter = new ImageAdapter();
        mVpImages.setAdapter(imageAdapter);


    }

    private void getIntentValues()
    {
        Intent intent = this.getIntent();
        campaignName = intent.getStringExtra("campaignName");
        flag = intent.getStringExtra("flag");

    }

    private void initialiseViews()
    {
        mIvBackButton = (ImageView)findViewById(R.id.xIvBackButton);
        mTvCamNameHeader= (TextView)findViewById(R.id.xTvCamNameHeader);
        mTvCamName = (TextView)findViewById(R.id.xTvCamName);
        mTvSubscribeNow = (TextView)findViewById(R.id.xTvSubscribeNow);

        mVpImages = (ViewPager)findViewById(R.id.xVpImages);
        mVpCamDetails = (ViewPager)findViewById(R.id.xVpCamDetails);
        mTabCamDetails = (TabLayout)findViewById(R.id.xTabCamDetails);



        mIvBackButton.setOnClickListener(this);
        mTvSubscribeNow.setOnClickListener(this);

        mTvCamNameHeader.setText(campaignName);
        mTvCamName.setText(campaignName);

        if(flag.equalsIgnoreCase("0")) // 0 for other campaigns , 1 for my camapigns
        {
            mTvSubscribeNow.setText("SUBSCRIBE NOW");
            mTvSubscribeNow.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Toast.makeText(CampaignDetailsPage.this,"Campaign subscribed",Toast.LENGTH_LONG).show();
                }
            });
        }
        else
        {
            mTvSubscribeNow.setText("CANCEL");
            mTvSubscribeNow.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Toast.makeText(CampaignDetailsPage.this,"Campaign cancelled",Toast.LENGTH_LONG).show();
                }
            });
        }
    }



    @Override
    public void onClick(View view)
    {
        if(view == mIvBackButton)
        {
            finish();
        }
    }

    private class CampaignDetailAdapter extends FragmentPagerAdapter
    {
        private String[] tabHeader = {"Contact","About campaign"};
        private List<Fragment> fragments;


        public CampaignDetailAdapter(FragmentManager fm)
        {
            super(fm);
            fragments = new ArrayList<Fragment>();
            fragments.add(new CampaignDetailContact());
            fragments.add(new CampaignDetailAbtCampaign());
        }

        @Override
        public Fragment getItem(int position)
        {

            if(position == 0)
            {

            }
            else
            {

            }
            return fragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return tabHeader[position];
        }

        @Override
        public int getCount()
        {
            return tabHeader.length;
        }
    }

    private class ImageAdapter extends PagerAdapter
    {

        @Override
        public int getCount()
        {
            return camImages.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            ImageView imageView = new ImageView(container.getContext());
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(params);
            imageView.setImageResource(camImages.get(position));

            LinearLayout layout = new LinearLayout(container.getContext());
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            layout.addView(imageView);
            container.addView(layout);
            return layout;


        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            container.removeView((LinearLayout)object);
        }
    }
}
