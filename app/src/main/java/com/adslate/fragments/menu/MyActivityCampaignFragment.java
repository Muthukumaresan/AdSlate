package com.adslate.fragments.menu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.adslate.R;
import com.adslate.activities.CampaignDetailsPage;
import com.adslate.baseclasses.BaseFragment;
import com.adslate.models.CampaignDetails;

import java.util.ArrayList;

/**
 * Created by pooja.b on 05-02-2016.
 */
public class MyActivityCampaignFragment extends BaseFragment
{

    private ArrayList<CampaignDetails> mMyCampaignDetails;
    private GridView mGridMyActivityCampaign;
    private MyActivityCampaignAdapter mMyActivityCampaignAdapter;



    View fragmentView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentView = inflater.inflate(R.layout.fragment_myactivity_campaign,container,false);
        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        mMyCampaignDetails = new ArrayList<CampaignDetails>();
        mMyCampaignDetails.add(new CampaignDetails("Thumb's up", "128", "200", "3", "Malleshwaram"));
        mMyCampaignDetails.add(new CampaignDetails("Limca", "100", "200", "3", "Malleshwaram"));
        mMyCampaignDetails.add(new CampaignDetails("Coca cola", "100", "200", "3", "Malleshwaram"));

        mGridMyActivityCampaign = (GridView)fragmentView.findViewById(R.id.gridMyActivityCampaign);
        mMyActivityCampaignAdapter = new MyActivityCampaignAdapter(getActivity(),mMyCampaignDetails);
        mGridMyActivityCampaign.setAdapter(mMyActivityCampaignAdapter);
        mGridMyActivityCampaign.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent campaignDetails = new Intent(getActivity(),CampaignDetailsPage.class);
                campaignDetails.putExtra("campaignName",mMyCampaignDetails.get(position).getmName());
                campaignDetails.putExtra("flag","1");// 1 for my campaign , 0 for other campaigns
                startActivity(campaignDetails);
            }
        });

    }

    private class MyActivityCampaignAdapter extends BaseAdapter
    {
        Context mAContext;
        ArrayList<CampaignDetails> mAMyCampaignDetails;
        LayoutInflater inflator;
        ViewHolder holder;

        public MyActivityCampaignAdapter(Context context, ArrayList<CampaignDetails> mMyCampaignDetails)
        {
            mAContext = context;
            inflator = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mAMyCampaignDetails = mMyCampaignDetails;
        }

        @Override
        public int getCount()
        {
            return mAMyCampaignDetails.size();
        }

        @Override
        public Object getItem(int position)
        {
            return mAMyCampaignDetails.get(position);
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

            holder.campaignName.setText(mAMyCampaignDetails.get(position).getmName());
            holder.subscribedNumber.setText(mAMyCampaignDetails.get(position).getmSubscribedNumber()+"/");
            holder.thresholdNumber.setText(mAMyCampaignDetails.get(position).getmThresholdNumber());
            holder.daysLeft.setText(mAMyCampaignDetails.get(position).getmDaysLeft());
            holder.location.setText(mAMyCampaignDetails.get(position).getmLocation());
            return convertView;

        }

        class ViewHolder
        {
            TextView campaignName,subscribedNumber,thresholdNumber,daysLeft,location;
        }

    }
}
