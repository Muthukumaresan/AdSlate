package com.adslate.helperclasses;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.adslate.R;

import java.util.List;
import java.util.StringTokenizer;


public class Multispinner extends Spinner implements
        OnMultiChoiceClickListener, OnCancelListener
{
    private List<String> listitems;
    private boolean[] checked;
    private TextView mTvCountry;
    String str;


    public Multispinner(Context context)
    {
        super(context);
    }

    public Multispinner(Context arg0, AttributeSet arg1)
    {
        super(arg0, arg1);
    }

    public Multispinner(Context arg0, AttributeSet arg1, int arg2)
    {
        super(arg0, arg1, arg2);
    }



    @Override
    public void onClick(DialogInterface dialog, int ans, boolean isChecked)
    {
        if (isChecked)
            checked[ans] = true;
        else
            checked[ans] = false;
    }


    public void onCancel(DialogInterface dialog)
    {

        String str="Selected values are: ";

        for (int i = 0; i < listitems.size(); i++)
        {
            if (checked[i] == true)
            {
                str=str+"   "+listitems.get(i);
            }

        }

        AlertDialog.Builder alert1 = new AlertDialog.Builder(getContext());

        alert1.setTitle("Items:");

        alert1.setMessage(str);

        alert1.setPositiveButton("Ok", null);

        alert1.show();

    }

    @Override
    public boolean performClick()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMultiChoiceItems(
                listitems.toArray(new CharSequence[listitems.size()]), checked, this);
        builder.setPositiveButton("done",
                new DialogInterface.OnClickListener()
                {

                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.cancel();

                    }
                });
        builder.setOnCancelListener(this);
        builder.show();
        return true;
    }

    public void setItems(List<String> items, String allText,
                         multispinnerListener listener)
    {
        this.listitems = items;

        checked = new boolean[items.size()];
        for (int i = 0; i < checked.length; i++)
            checked[i] =false;


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, new String[] { allText });
        setAdapter(adapter);
    }

    public interface multispinnerListener
    {
        public void onItemschecked(boolean[] checked);

    }

}