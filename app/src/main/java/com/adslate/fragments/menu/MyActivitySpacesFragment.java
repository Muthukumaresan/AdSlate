package com.adslate.fragments.menu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.adslate.baseclasses.BaseFragment;
import com.adslate.models.SpacesDetails;

import java.util.ArrayList;

/**
 * Created by pooja.b on 05-02-2016.
 */
public class MyActivitySpacesFragment extends BaseFragment
{

   private View fragmentView;
    private ArrayList<SpacesDetails> mySpacesDetails;
    private GridView mGridMyActivitySpaces;
    private MyActivitySpacesAdapter mMyActivitySpacesAdapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentView = inflater.inflate(R.layout.fragment_myactivity_spaces,container,false);
        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        mySpacesDetails = new ArrayList<SpacesDetails>();
        mySpacesDetails.add(new SpacesDetails("500/day", "Cancelled"));
        mySpacesDetails.add(new SpacesDetails("600/day", "Cancelled"));
        mySpacesDetails.add(new SpacesDetails("700/day", "Cancelled"));

        mGridMyActivitySpaces = (GridView)fragmentView.findViewById(R.id.gridMyActivitySpaces);
        mMyActivitySpacesAdapter = new MyActivitySpacesAdapter(getActivity(),mySpacesDetails);

        mGridMyActivitySpaces.setAdapter(mMyActivitySpacesAdapter);
        mGridMyActivitySpaces.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent spaceIntent = new Intent(getActivity(), SpaceDetailsPage.class);
                spaceIntent.putExtra("flag","1");// 1 for my campaign , 0 for other campaigns
                startActivity(spaceIntent);
            }
        });

    }


    private class MyActivitySpacesAdapter extends BaseAdapter
    {

        Context mAContext;
        ArrayList<SpacesDetails> mASpaceDetails;
        LayoutInflater inflator;
        ViewHolder holder;

        public MyActivitySpacesAdapter(Context context, ArrayList<SpacesDetails> mySpacesDetails)
        {
            mAContext = context;
            mASpaceDetails = mySpacesDetails;
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
