package com.adslate.fragments.menu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.adslate.R;
import com.adslate.activities.SpaceDetailsPage;
import com.adslate.models.CampaignDetails;
import com.adslate.models.OrderDetails;
import com.adslate.models.SpacesDetails;

import java.util.ArrayList;

/**
 * Created by pooja.b on 19-11-2015.
 */
public class HomeSpacesFragment extends Fragment
{
    private View fragmentView;
    private ArrayList<SpacesDetails> mSpacesDetails;
    private GridView mGridHomeSpaces;
    private HomeSpacesAdapter mHomeSpacesAdapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentView = inflater.inflate(R.layout.fragment_home_spaces,container,false);
        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);


        mSpacesDetails = new ArrayList<SpacesDetails>();
        mSpacesDetails.add(new SpacesDetails("10,000/day", "Active"));
        mSpacesDetails.add(new SpacesDetails("20,000/day", "Inactive"));
        mSpacesDetails.add(new SpacesDetails("30,000/day", "Active"));
        mSpacesDetails.add(new SpacesDetails("40,000/day", "Inactive"));
        mSpacesDetails.add(new SpacesDetails("50,000/day", "Active"));
        mSpacesDetails.add(new SpacesDetails("1000/day", "Inactive"));
        mSpacesDetails.add(new SpacesDetails("2000/day", "Active"));
        mSpacesDetails.add(new SpacesDetails("3000/day", "Inactive"));
        mSpacesDetails.add(new SpacesDetails("4000/day", "Active"));
        mSpacesDetails.add(new SpacesDetails("5000/day", "Inactive"));



        mGridHomeSpaces = (GridView)fragmentView.findViewById(R.id.gridSpaces);
        mHomeSpacesAdapter = new HomeSpacesAdapter(getActivity(),mSpacesDetails);
        mGridHomeSpaces.setAdapter(mHomeSpacesAdapter);
        mGridHomeSpaces.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent spaceIntent = new Intent(getActivity(),SpaceDetailsPage.class);
                spaceIntent.putExtra("flag","1");// 1 for my campaign , 0 for other campaigns
                startActivity(spaceIntent);
            }
        });



    }


    private class HomeSpacesAdapter extends BaseAdapter
    {
        Context mAContext;
        ArrayList<SpacesDetails> mASpaceDetails;
        LayoutInflater inflator;
        ViewHolder holder;
        public HomeSpacesAdapter(Context context, ArrayList<SpacesDetails> mSpacesDetails)
        {
            mAContext = context;
            mASpaceDetails = mSpacesDetails;
            inflator = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount()
        {
            return mASpaceDetails.size();
        }

        @Override
        public Object getItem(int position)
        {
            return mASpaceDetails.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {

            if(convertView == null)
            {
                convertView = inflator.inflate(R.layout.spaces_layout,null);
                convertView.setLayoutParams(new GridView.LayoutParams((int) getResources().getDimension(R.dimen.cell_width), (int) getResources().getDimension(R.dimen.cell_height)));
                holder = new ViewHolder();
                holder.quotesRate = (TextView)convertView.findViewById(R.id.xTvQuotedrate);
                holder.status = (TextView)convertView.findViewById(R.id.xTvStatus);
                convertView.setTag(holder);

            }
            else
            {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.quotesRate.setText(mASpaceDetails.get(position).getQuotedrate());
            holder.status.setText(mASpaceDetails.get(position).getStatus());


            return convertView;
        }

        class ViewHolder
        {
            TextView quotesRate,status;
        }
    }
}
