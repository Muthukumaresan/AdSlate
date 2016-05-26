package com.adslate.fragments.menu;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.adslate.R;
import com.adslate.baseclasses.BaseFragment;
import com.adslate.models.OrderDetails;

import java.util.ArrayList;

/**
 * Created by pooja.b on 01-02-2016.
 */
public class ContractHistoryFragment extends BaseFragment
{
    private View fragmentView;
    private ArrayList<OrderDetails> mOrderDetails;
    private ListView mLvContractHistory;
    private ContractHistoryAdapter contractHistoryAdapter;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentView = inflater.inflate(R.layout.contract_history,container,false);
        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        mOrderDetails = new ArrayList<OrderDetails>();
        mOrderDetails.add(new OrderDetails("101","27/05/2016","1.00pm","2 weeks","Collapsed",""));
        mOrderDetails.add(new OrderDetails("102","28/05/2016","2.00pm","3 weeks","Collapsed",""));
        mOrderDetails.add(new OrderDetails("103","29/05/2016","3.00pm","4 weeks","Collapsed",""));
        mOrderDetails.add(new OrderDetails("104","30/05/2016","4.00pm","5 weeks","Collapsed",""));
        mOrderDetails.add(new OrderDetails("105","31/05/2016","5.00pm","6 weeks","Collapsed",""));
        mOrderDetails.add(new OrderDetails("106","01/06/2016","6.00pm","7 weeks","Collapsed",""));


        mLvContractHistory = (ListView)fragmentView.findViewById(R.id.xlistContractHistory);
        contractHistoryAdapter = new ContractHistoryAdapter(getActivity(),mOrderDetails);
        mLvContractHistory.setAdapter(contractHistoryAdapter);

    }


    private class ContractHistoryAdapter extends BaseAdapter
    {
        ArrayList<OrderDetails> mAOrderDetails;
        Context mAContext;
        ViewHolder holder;
        public ContractHistoryAdapter(Context context, ArrayList<OrderDetails> mOrderDetails)
        {
            mAOrderDetails = mOrderDetails;
            mAContext = context;
        }

        @Override
        public int getCount() {
            return mAOrderDetails.size();
        }

        @Override
        public Object getItem(int position) {
            return mAOrderDetails.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            LayoutInflater inflator;

            if(convertView == null)
            {
                inflator = (LayoutInflater)mAContext.getSystemService(mAContext.LAYOUT_INFLATER_SERVICE);
                convertView = inflator.inflate(R.layout.contract_history_row,null);
                holder = new ViewHolder();
                holder.orderNo = (TextView)convertView.findViewById(R.id.xTvOrderno);
                holder.duration = (TextView)convertView.findViewById(R.id.xTvDuration);
                holder.orderDate = (TextView)convertView.findViewById(R.id.xTvDateTime);
                holder.status = (TextView)convertView.findViewById(R.id.xTvStatus);
                convertView.setTag(holder);
            }
            else
            {
                holder = (ViewHolder)convertView.getTag();
            }

            holder.orderNo.setText(mAOrderDetails.get(position).getOrderNo());
            holder.duration.setText(mAOrderDetails.get(position).getDuration());
            holder.orderDate.setText(mAOrderDetails.get(position).getOnsetDate()+" "+mAOrderDetails.get(position).getOnsetTime());
            holder.status.setText(mAOrderDetails.get(position).getStatus());


            return convertView;
        }

       private class ViewHolder
        {
            TextView orderNo,orderDate,duration,status;
        }
    }
}
