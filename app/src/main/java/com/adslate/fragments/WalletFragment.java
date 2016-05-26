package com.adslate.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adslate.R;
import com.adslate.baseclasses.BaseFragment;

/**
 * Created by pooja.b on 16-11-2015.
 */
public class WalletFragment extends BaseFragment
{
    private View fragmentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentView = inflater.inflate(R.layout.fragment_wallet,container,false);
        return fragmentView;
    }


}
