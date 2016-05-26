package com.adslate.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adslate.R;
import com.adslate.baseclasses.BaseFragment;

/**
 * Created by pooja.b on 12-12-2015.
 */
public class IndividualEnterpriseFragment extends BaseFragment
{

    private View fragmentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        fragmentView = inflater.inflate(R.layout.fragment_help,container,false);
        return fragmentView;
    }
}
