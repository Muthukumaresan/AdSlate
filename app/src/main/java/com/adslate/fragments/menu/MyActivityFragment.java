package com.adslate.fragments.menu;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adslate.R;
import com.adslate.baseclasses.BaseFragment;
import com.adslate.fragments.CampaignsFragment;
import com.adslate.fragments.SpacesFragment;
import com.adslate.models.CampaignDetails;
import com.adslate.utils.StaticUtils;

import java.util.ArrayList;

/**
 * Created by pooja.b on 25-11-2015.
 */
public class MyActivityFragment extends BaseFragment
{
    private View fragmentView;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ArrayList<String> mMyActivityTitles;
    private MyActivityAdapter mMyActivityAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        fragmentView = inflater.inflate(R.layout.fragment_myactivity,container,false);
        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mMyActivityTitles = new ArrayList<String>();


            mMyActivityTitles.add("My Campaigns");
            mMyActivityTitles.add("My Spaces");


        mViewPager = (ViewPager)fragmentView.findViewById(R.id.xViewpagerMyActivity);
        mMyActivityAdapter = new MyActivityAdapter(getFragmentManager());
        mViewPager.setAdapter(mMyActivityAdapter);
        mTabLayout = (TabLayout)fragmentView.findViewById(R.id.sliding_tabs_myActivity);
        mTabLayout.setupWithViewPager(mViewPager);


    }

    private class MyActivityAdapter extends FragmentStatePagerAdapter
    {
        ArrayList<Fragment> myActivityFragments;
        public MyActivityAdapter(FragmentManager fragmentManager)
        {
            super(fragmentManager);
            myActivityFragments = new ArrayList<Fragment>();
            myActivityFragments.add(new MyActivityCampaignFragment());
            myActivityFragments.add(new MyActivitySpacesFragment());

        }

        @Override
        public Fragment getItem(int position)
        {
            return myActivityFragments.get(position);
        }

        @Override
        public int getCount()
        {
            return myActivityFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return mMyActivityTitles.get(position);
        }
    }
}
