package com.adslate.fragments.SpacesDetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adslate.R;
import com.adslate.baseclasses.BaseFragment;

/**
 * Created by pooja.b on 19-05-2016.
 */
public class SpacesDetailAbtSpc extends BaseFragment
{
    private View fragmentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentView = inflater.inflate(R.layout.space_detail_abt,container,false);
        return fragmentView;
    }
}
