package com.adslate.fragments.menu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.adslate.R;
import com.adslate.activities.CampaignDetailsPage;
import com.adslate.models.CampaignDetails;

import java.util.ArrayList;

/**
 * Created by pooja.b on 19-11-2015.
 */
public class HomeCampaignsFragment extends Fragment {
    View fragmentView;
    private ArrayList<CampaignDetails> mCampaignDetails;
    private GridView mGridHomeCampaign;
    private HomeCampaignAdapter mHomeCampaignAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentView = inflater.inflate(R.layout.fragment_home_campaigns, container, false);
        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);



        mCampaignDetails = new ArrayList<CampaignDetails>();
        mCampaignDetails.add(new CampaignDetails("Tesla Model X Promotion", "128", "200", "3", "Malleshwaram"));
        mCampaignDetails.add(new CampaignDetails("Google Tees Promo", "100", "200", "3", "Malleshwaram"));
        mCampaignDetails.add(new CampaignDetails("UBER Car Sticker", "100", "200", "3", "Malleshwaram"));
        mCampaignDetails.add(new CampaignDetails("LINKIN Park Tour", "100", "200", "3", "Malleshwaram"));
        mCampaignDetails.add(new CampaignDetails("Campaign Pepsi Classic", "100", "200", "3", "Malleshwaram"));
        mCampaignDetails.add(new CampaignDetails("Tesla Model X Promotion", "128", "200", "3", "Malleshwaram"));
        mCampaignDetails.add(new CampaignDetails("Google Tees Promo", "100", "200", "3", "Malleshwaram"));
        mCampaignDetails.add(new CampaignDetails("UBER Car Sticker", "100", "200", "3", "Malleshwaram"));
        mCampaignDetails.add(new CampaignDetails("LINKIN Park Tour", "100", "200", "3", "Malleshwaram"));
        mCampaignDetails.add(new CampaignDetails("Campaign Pepsi Classic", "100", "200", "3", "Malleshwaram"));
        mCampaignDetails.add(new CampaignDetails("Tesla Model X Promotion", "128", "200", "3", "Malleshwaram"));
        mCampaignDetails.add(new CampaignDetails("Google Tees Promo", "100", "200", "3", "Malleshwaram"));
        mCampaignDetails.add(new CampaignDetails("UBER Car Sticker", "100", "200", "3", "Malleshwaram"));
        mCampaignDetails.add(new CampaignDetails("LINKIN Park Tour", "100", "200", "3", "Malleshwaram"));
        mCampaignDetails.add(new CampaignDetails("Campaign Pepsi Classic", "100", "200", "3", "Malleshwaram"));

        mGridHomeCampaign = (GridView)fragmentView.findViewById(R.id.gridCampaign);
        mHomeCampaignAdapter = new HomeCampaignAdapter(getActivity(),mCampaignDetails);
        mGridHomeCampaign.setAdapter(mHomeCampaignAdapter);
        mGridHomeCampaign.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent campaignDetails = new Intent(getActivity(),CampaignDetailsPage.class);
                campaignDetails.putExtra("campaignName",mCampaignDetails.get(position).getmName());
                campaignDetails.putExtra("flag","0"); //1 for my campaign , 0 for other campaigns
                startActivity(campaignDetails);

            }
        });


    }


    private class HomeCampaignAdapter extends BaseAdapter
    {
        ArrayList<CampaignDetails> mACampaignDetails;
        LayoutInflater inflator;
        ViewHolder holder;
        public HomeCampaignAdapter(Context context, ArrayList<CampaignDetails> mCampaignDetails)
        {
            mACampaignDetails =  mCampaignDetails;
            inflator = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount()
        {
            return mACampaignDetails.size();
        }

        @Override
        public Object getItem(int position)
        {
            return mACampaignDetails.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {

            if(convertView == null)
            {
                convertView = inflator.inflate(R.layout.campaign_layout,null);
                convertView.setLayoutParams(new GridView.LayoutParams((int) getResources().getDimension(R.dimen.cell_width), (int) getResources().getDimension(R.dimen.cell_height)));
                holder = new ViewHolder();
                holder.campaignName = (TextView)convertView.findViewById(R.id.xTvCampaignName);
                holder.subscribedNumber = (TextView)convertView.findViewById(R.id.xTvSubscribedNumber);
                holder.thresholdNumber = (TextView)convertView.findViewById(R.id.xTvThresholdNumber);
                holder.daysLeft = (TextView)convertView.findViewById(R.id.xTvDaysLeft);
                holder.location = (TextView)convertView.findViewById(R.id.xTvLocation);
                convertView.setTag(holder);

            }
            else
            {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.campaignName.setText(mACampaignDetails.get(position).getmName());
            holder.subscribedNumber.setText(mACampaignDetails.get(position).getmSubscribedNumber()+"/");
            holder.thresholdNumber.setText(mACampaignDetails.get(position).getmThresholdNumber());
            holder.daysLeft.setText(mACampaignDetails.get(position).getmDaysLeft());
            holder.location.setText(mACampaignDetails.get(position).getmLocation());
            return convertView;
        }


        class ViewHolder
        {
            TextView campaignName,subscribedNumber,thresholdNumber,daysLeft,location;
        }
    }

}
