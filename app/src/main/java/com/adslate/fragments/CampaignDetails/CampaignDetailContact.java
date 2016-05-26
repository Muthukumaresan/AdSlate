package com.adslate.fragments.CampaignDetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adslate.R;
import com.adslate.baseclasses.BaseFragment;

/**
 * Created by pooja.b on 18-05-2016.
 */
public class CampaignDetailContact extends BaseFragment
{
    private View fragmentView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentView = inflater.inflate(R.layout.campaign_detail_contact,container,false);
        return fragmentView;

    }
}
