package com.adslate.fragments.menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.adslate.R;
import com.adslate.baseclasses.BaseFragment;

import java.util.ArrayList;

/**
 * Created by pooja.b on 01-02-2016.
 */
public class RequestQuoteFragment extends BaseFragment
{
    private View fragmentView;
   private Spinner mSpinnerCities;
   private ArrayList<String> mCitiesList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentView = inflater.inflate(R.layout.request_quote,container,false);
        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        mCitiesList = new ArrayList<String>();
        mCitiesList.add("Banglore");
        mCitiesList.add("Chennai");
        mCitiesList.add("Kolkata");
        mCitiesList.add("Mumbai");




        mSpinnerCities = (Spinner)fragmentView.findViewById(R.id.xSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_value,mCitiesList);
        mSpinnerCities.setAdapter(adapter);
    }
}
