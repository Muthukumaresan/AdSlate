package com.adslate.fragments.menu;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adslate.R;
import com.adslate.baseclasses.BaseFragment;
import com.adslate.utils.StaticUtils;

import java.util.ArrayList;
/**
 * Created by pooja.b on 19-11-2015.
 */
public class HomeFragment extends BaseFragment
{
    private View fragmentView;
    private ViewPager mVPager;
    private HomeFragmentAdapter mHomeFragAdapter;
    private ArrayList<String> mHomeTitles;
    private TabLayout mTabLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentView = inflater.inflate(R.layout.fragment_home, container,false);
        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        mHomeTitles = new ArrayList<String>();


        if(mPref.mGetUserType().equalsIgnoreCase("1")) //Individual
        {

            mHomeTitles.add("Campaigns");
            mHomeTitles.add("Spaces");


        }
        else
        {
            mHomeTitles.add("Spaces");
            mHomeTitles.add("Campaigns");

        }

        mVPager = (ViewPager)fragmentView.findViewById(R.id.xViewpager);
        mHomeFragAdapter = new HomeFragmentAdapter(getFragmentManager());
        mVPager.setAdapter(mHomeFragAdapter);
        mTabLayout = (TabLayout)fragmentView.findViewById(R.id.sliding_tabs);
        mTabLayout.setupWithViewPager(mVPager);

    }



    private class HomeFragmentAdapter extends FragmentStatePagerAdapter
    {

        ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
        public HomeFragmentAdapter(FragmentManager fm)
        {
            super(fm);

            if(mPref.mGetUserType().equalsIgnoreCase("1"))
            {
                mFragments.add(new HomeCampaignsFragment());
                mFragments.add(new HomeSpacesFragment());

            }
            else
            {
                mFragments.add(new HomeSpacesFragment());
                mFragments.add(new HomeCampaignsFragment());

            }

        }

        @Override
        public Fragment getItem(int position)
        {
            Fragment fragment = mFragments.get(position);
            return fragment;
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return mHomeTitles.get(position);
        }
    }
}
