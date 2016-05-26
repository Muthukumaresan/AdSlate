package com.adslate.backgroundtasks;

import android.content.Context;
import android.os.AsyncTask;


/**
 * Created by pooja.b on 27-11-2015.
 */
public class ValidateOTPWebservice extends AsyncTask<String,Void,String>
{
    private String mResponsemessage ;
    private Context mContext;



    public ValidateOTPWebservice(Context mContext)
    {
        this.mContext = mContext;

    }

    @Override
    protected String doInBackground(String... strings)
    {

        try
        {

            int mErrorFlag = 0;
            if(mErrorFlag == 0)
            {
                return "mSuccess";
            }
            else
            {
                return "mUnSuccess";
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return "mError";
        }
    }

    @Override
    protected void onPostExecute(String result)
    {

        if(result.equalsIgnoreCase("mSuccess"))
        {

        }
        else if(result.equalsIgnoreCase("mUnsucess"))
        {

        }
        else
        {

        }
    }
}
