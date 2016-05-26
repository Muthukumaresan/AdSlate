package com.adslate.fragments.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adslate.R;
import com.adslate.baseclasses.BaseFragment;

/**
 * Created by pooja.b on 29-01-2016.
 */
public class EditProfileFragment extends BaseFragment
{

    private View framentView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        framentView = inflater.inflate(R.layout.edit_profile,container,false);
        return framentView;
    }
}
