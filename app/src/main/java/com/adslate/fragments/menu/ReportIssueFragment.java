package com.adslate.fragments.menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.adslate.R;
import com.adslate.baseclasses.BaseFragment;

import java.util.ArrayList;

/**
 * Created by pooja.b on 29-01-2016.
 */
public class ReportIssueFragment extends BaseFragment
{
   private View fragmentView;
   private Spinner mSpinnerTypeOfIssue;
    private ArrayList<String> issueType;
    private EditText mIssueDesc,mEmail;
    private TextView mTvReportIssue;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentView = inflater.inflate(R.layout.report_issue,container,false);
        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mSpinnerTypeOfIssue = (Spinner)fragmentView.findViewById(R.id.xSpinner);
        mIssueDesc = (EditText)fragmentView.findViewById(R.id.xEtIssueDesc);
        mEmail = (EditText)fragmentView.findViewById(R.id.xEtEmail);
        mTvReportIssue = (TextView)fragmentView.findViewById(R.id.xTvReportIssue);

        issueType = new ArrayList<String>();
        issueType.add("Application Issue");
        issueType.add("Operational Issue");
        issueType.add("Feedback");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_value, issueType);
        mSpinnerTypeOfIssue.setAdapter(dataAdapter);


    }
}
