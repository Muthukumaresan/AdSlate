package com.adslate.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adslate.R;
import com.adslate.baseclasses.BaseFragmentActivity;
import com.adslate.fragments.SpacesDetails.SpacesDetailAbtSpc;
import com.adslate.fragments.SpacesDetails.SpacesDetailContact;

import java.util.ArrayList;

/**
 * Created by pooja.b on 19-05-2016.
 */
public class SpaceDetailsPage extends BaseFragmentActivity implements View.OnClickListener {
    private ViewPager mVpImages,mVpSpcDetails;
    private TabLayout mTabSpcDetails;
    private TextView mTvSpcNameHeader,mTvLike;
    private ImageView mIvBackButton;

    private ImagesAdapter imagesAdapter ;
    private SpaceDetailsAdapter spaceDetailsAdapter;

    private ArrayList<Integer> spcImages;
    private String flag ;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_space_details);

        getIntentvalues();

        mInitialiseViews();

        spcImages = new ArrayList<Integer>();
        spcImages.add(R.drawable.background1);
        spcImages.add(R.drawable.background2);
        spcImages.add(R.drawable.background3);
        spcImages.add(R.drawable.background6);
        spcImages.add(R.drawable.background5);

        imagesAdapter = new ImagesAdapter();
        mVpImages.setAdapter(imagesAdapter);

        spaceDetailsAdapter = new SpaceDetailsAdapter(getSupportFragmentManager());
        mVpSpcDetails.setAdapter(spaceDetailsAdapter);
        mTabSpcDetails.setupWithViewPager(mVpSpcDetails);


    }

    private void getIntentvalues()
    {
        Intent intent = this.getIntent();
        flag = intent.getStringExtra("flag");

    }

    private void mInitialiseViews()
    {

        mTvSpcNameHeader = (TextView)findViewById(R.id.xTvSpcNameHeader);
        mTvLike = (TextView)findViewById(R.id.xTvLike);
        mIvBackButton = (ImageView)findViewById(R.id.xIvBackButton);
        mVpImages = (ViewPager)findViewById(R.id.xVpImages);
        mVpSpcDetails = (ViewPager)findViewById(R.id.xVpSpcDetails);
        mTabSpcDetails = (TabLayout)findViewById(R.id.xTabSpcDetails);

        mIvBackButton.setOnClickListener(this);
        mTvLike.setOnClickListener(this);


        if(flag.equalsIgnoreCase("0"))
        {
            mTvLike.setText("LIKE");
            mTvLike.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Toast.makeText(SpaceDetailsPage.this, "Space liked", Toast.LENGTH_LONG).show();
                }
            });
        }
        else
        {
            mTvLike.setText("CANCEL");
            mTvLike.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Toast.makeText(SpaceDetailsPage.this, "Space cancelled", Toast.LENGTH_LONG).show();
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

    private class ImagesAdapter extends PagerAdapter
    {
        @Override
        public int getCount()
        {
            return spcImages.size();
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
         ViewGroup.LayoutParams params = new  ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(params);
            imageView.setImageResource(spcImages.get(position));

            LinearLayout layout = new LinearLayout(container.getContext());
            ViewGroup.LayoutParams param = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
            layout.setLayoutParams(param);
            layout.addView(imageView);

            container.addView(layout);

            return layout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            container.removeView((LinearLayout) object);
        }
    }

    private class SpaceDetailsAdapter extends FragmentPagerAdapter
    {
        private String[] spcTabHeader = {"Contact","About space"};
        private ArrayList<Fragment> fragments;

        public SpaceDetailsAdapter(FragmentManager fm)
        {
            super(fm);
            fragments = new ArrayList<Fragment>();
            fragments.add(new SpacesDetailContact());
            fragments.add(new SpacesDetailAbtSpc());
        }

        @Override
        public Fragment getItem(int position)
        {

            if(position ==0)
            {

            }
            else
            {

            }

            return fragments.get(position);
        }

        @Override
        public int getCount()
        {
            return spcTabHeader.length;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return spcTabHeader[position];
        }
    }
}
