package com.adslate.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adslate.R;
import com.adslate.baseclasses.BaseActivity;
import com.adslate.storage.AdSlateDB;
import com.adslate.utils.AdSlateUrlConnection;
import com.adslate.utils.StaticUtils;

import org.json.JSONObject;

import java.net.URL;

public class OTPScreen extends BaseActivity implements View.OnClickListener {

    private TextView mTvEmail,mResendOTP,mVerifyOTP;
    private EditText mEtPhoneNumber,mEtOTP;
    String mEmail,mPhoneNumber,mUserId,mUserType,mOTP;
    private ImageView mIvBack;
    private TextInputLayout mTLayPhNum,mTLayOTP;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpscreen);

        getIntentValues();

        mInitialiseViews();
    }

    private void getIntentValues()
    {
        Intent intent = getIntent();
        mPhoneNumber = intent.getStringExtra("mPhoneNumber");
        mUserId = intent.getStringExtra("mUserId");
        mUserType = intent.getStringExtra("mUserType");
        mEmail = intent.getStringExtra("mEmail");
    }

    private void mInitialiseViews()
    {

        mTvEmail = (TextView)findViewById(R.id.xTvEmail);
        mEtPhoneNumber = (EditText)findViewById(R.id.xEtPhNum);
        mEtOTP = (EditText)findViewById(R.id.xEtOTP);
        mResendOTP = (TextView)findViewById(R.id.xTvResendOTP);
        mVerifyOTP = (TextView)findViewById(R.id.xTvVerifyOTP);
        mIvBack = (ImageView)findViewById(R.id.xIvBack);

        mTLayPhNum = (TextInputLayout)findViewById(R.id.xTLayPhNum);
        mTLayOTP = (TextInputLayout)findViewById(R.id.xTLayOTP);


        mTvEmail.setText(mEmail);

        if(mPhoneNumber.equalsIgnoreCase("") || mPhoneNumber.equalsIgnoreCase("null"))
        {

        }
        else
        {
            mEtPhoneNumber.setText(mPhoneNumber);
        }

        mResendOTP.setOnClickListener(this);
        mVerifyOTP.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {

        if(view == mResendOTP)
        {
            mValidateResendFields();

        }
        else if(view == mVerifyOTP)
        {

            mValidateVerifyFields();

        }
        else if(view == mIvBack)
        {
            finish();
        }
    }

    private void mValidateVerifyFields()
    {

        String email = mTvEmail.getText().toString().trim();
        String PhoneNum = mEtPhoneNumber.getText().toString().trim();
        String OTP = mEtOTP.getText().toString().trim();

        if(email.length() == 0 )
        {
            mTvEmail.setError(StaticUtils.CHECK_DETAILS_EMAIL);
        }
        else
        {

            if(PhoneNum.length() == 0)
            {
                mTLayPhNum.setError(StaticUtils.CHECK_DETAILS_PH);
            }
            else
            {
                mTLayPhNum.setErrorEnabled(false);

                if(OTP.length() == 0)
                {
                    mTLayOTP.setError(StaticUtils.GET_OTP);
                }
                else
                {
                    mTLayOTP.setErrorEnabled(false);

                    if(mNetworkStatus.isNetworkAvailable())
                    {
                        new VerifyOTP(PhoneNum,OTP).execute();
                    }
                    else
                    {
                        mShowDialog(OTPScreen.this,StaticUtils.NO_INTERNET_TITLE,StaticUtils.NO_INTERNET_MESSAGE,true,false);
                    }
                }
            }

        }


    }


    private void mValidateResendFields()
    {

        String email = mTvEmail.getText().toString().trim();
        String PhoneNum = mEtPhoneNumber.getText().toString().trim();

        if(email.length() == 0 )
        {
            mTvEmail.setError(StaticUtils.CHECK_DETAILS_EMAIL);

        }
        else
        {
            if(PhoneNum.length() == 0)
            {
                mTLayPhNum.setError(StaticUtils.CHECK_DETAILS_PH);
            }
            else
            {
                mTLayPhNum.setErrorEnabled(false);
                if(mNetworkStatus.isNetworkAvailable())
                {
                    new ResendOTPWebservice(PhoneNum).execute();
                }
                else
                {
                    mShowDialog(OTPScreen.this,StaticUtils.NO_INTERNET_TITLE,StaticUtils.NO_INTERNET_MESSAGE,true,false);
                }
            }

        }

    }


    private class VerifyOTP extends AsyncTask<String,Void,String>
    {

        // private URL url;
        private String mResponseMsg,wPhoneNum,wOTP;


        public VerifyOTP(String phoneNum, String otp)
        {
            wPhoneNum = phoneNum;
            wOTP = otp;
        }

        @Override
        protected void onPreExecute()
        {
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings)
        {
            try
            {

                AdSlateDB mAdSlateDB = AdSlateDB.GET_INSTANCE(OTPScreen.this);

                Log.e("Num",""+wPhoneNum);
                Log.e("OTP",""+wOTP);
               /* Uri buildUri = Uri.parse(StaticUtils.VALIDATE_OTP)
                        .buildUpon()
                        .appendQueryParameter("userId", mUserId)
                        .appendQueryParameter("mobileNumber", wPhoneNum)
                        .appendQueryParameter("userType",mUserType)
                        .appendQueryParameter("OTP",wOTP)
                        .build();

                url = new URL(buildUri.toString());

                Log.e("VERIFY_OTP_REQUEST",""+url);*/

                JSONObject jsonObjValidateOTP = new JSONObject();
                jsonObjValidateOTP.put("userId",mUserId);
                jsonObjValidateOTP.put("mobileNumber",wPhoneNum);
                jsonObjValidateOTP.put("userType",mUserType);
                jsonObjValidateOTP.put("OTP",wOTP);

                ContentValues values = new ContentValues();
                values.put("json",jsonObjValidateOTP.toString());

                Log.e("VERIFY_OTP_REQUEST", "" + StaticUtils.VALIDATE_OTP+String.valueOf(values));

                AdSlateUrlConnection connection = new AdSlateUrlConnection();
                String response = connection.mEstablishConnectionJSON(StaticUtils.VALIDATE_OTP, String.valueOf(values));

                Log.e("VERIFY_OTP_RESPONSE",""+response);

                JSONObject responseObj = new JSONObject(response);

                JSONObject resObj = responseObj.getJSONObject("response");

                String mErrorFlag = resObj.getString("errorFlag");

                mResponseMsg =resObj.getString("msg");

                if(mErrorFlag.equalsIgnoreCase("500")) //Mandatory parameters are empty
                {
                    return "500";
                }
                else if(mErrorFlag.equalsIgnoreCase("501"))  // Success
                {

                    mAdSlateDB.mInsertIntoUserTable(resObj.getString("userId"), resObj.getString("userType"),resObj.getString("profileImage") ,resObj.getString("loginType"), resObj.getString("userName"), resObj.getString("email"), resObj.getString("dob"),
                            resObj.getString("mobileNumber"), resObj.getString("orgType"),resObj.getString("address") , resObj.getString("orgDesc"), resObj.getString("orgTurnover"), resObj.getString("contactPersonName"),
                            resObj.getString("contactPersonDesignation"), resObj.getString("contactPersonMobile"), resObj.getString("orgBusinessCard"),resObj.getString("isMobileVerified"), resObj.getString("referralCode"),resObj.getString("otherOrgType"));

                    mAdSlateDB.close();

                    return "501";
                }
                else if(mErrorFlag.equalsIgnoreCase("502")) // Information provided is incorrect.
                {
                    return "502";
                }
                else if(mErrorFlag.equalsIgnoreCase("505")) // Some error occurred while storing in the database.
                {
                    return "505";
                }
                else if(mErrorFlag.equalsIgnoreCase("508")) // OTP entered is wrong.
                {
                    return "508";
                }
                else
                {
                    return "";
                }
            }
            catch (Exception e)
            {

                e.printStackTrace();
                return "mError";
            }

        }

        @Override
        protected void onPostExecute(String result)
        {
            mProgressDialog.dismiss();
            if(result.equalsIgnoreCase("500")) //Mandatory parameters are empty
            {
                mShowDialog(OTPScreen.this,"",mResponseMsg,true,false);
            }

            else if(result.equalsIgnoreCase("501")) // Success
            {
                mPref.mSaveLoginStatus(true);
                Intent intent = new Intent(OTPScreen.this,Home.class) ;
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
            else if(result.equalsIgnoreCase("502")) // Information provided is incorrect.
            {
                mShowDialog(OTPScreen.this,"",mResponseMsg,true,false);
            }
            else if(result.equalsIgnoreCase("505"))// Some error occurred while storing in the database.
            {
                mShowDialog(OTPScreen.this,"",mResponseMsg,true,false);
            }
            else if(result.equalsIgnoreCase("508")) // OTP entered is wrong.
            {
                mShowDialog(OTPScreen.this,"",mResponseMsg,true,false);
            }
            else
            {
                mShowDialog(OTPScreen.this,"", StaticUtils.ERROR_OCCURRED,true,false);
            }
        }
    }

    private class ResendOTPWebservice extends AsyncTask<String,Void,String>
    {
        // private URL url;
        private String mResponseMsg,wPhoneNum;

        public ResendOTPWebservice(String phNum)
        {
            wPhoneNum = phNum;
        }


        @Override
        protected void onPreExecute()
        {
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                Log.e("UserId",""+mUserId);
                Log.e("Num",""+wPhoneNum);
                Log.e("UserType",""+mUserType);

           /*   Uri builUri = Uri.parse(StaticUtils.RESEND_OTP)
                      .buildUpon()
                      .appendQueryParameter("userId", mUserId)
                      .appendQueryParameter("mobileNumber", wPhoneNum)
                      .appendQueryParameter("userType", mUserType)
                      .build();

              url = new URL(builUri.toString());

              Log.e("SEND_OTP_REQUEST",""+url);*/

                JSONObject jsonObjResendOTP = new JSONObject();
                jsonObjResendOTP.put("userId",mUserId);
                jsonObjResendOTP.put("mobileNumber",wPhoneNum);
                jsonObjResendOTP.put("userType",mUserType);

                ContentValues values = new ContentValues();
                values.put("json",jsonObjResendOTP.toString());

                Log.e("SEND_OTP_REQUEST", "" + StaticUtils.RESEND_OTP+String.valueOf(values));

                AdSlateUrlConnection connection = new AdSlateUrlConnection();
                String response = connection.mEstablishConnectionJSON(StaticUtils.RESEND_OTP, String.valueOf(values));

                Log.e("SEND_OTP_RESPONSE",""+response);

                JSONObject responseObj = new JSONObject(response);

                JSONObject resObj = responseObj.getJSONObject("response");

                String mErrorFlag = resObj.getString("errorFlag");

                mResponseMsg = resObj.getString("msg");

                if(mErrorFlag.equalsIgnoreCase("500")) // Mandatory parameters are empty.
                {
                    return "500";
                }
                else if(mErrorFlag.equalsIgnoreCase("501")) // Successful.
                {
                    mOTP = resObj.getString("OTP");
                    return "501";
                }
                else if(mErrorFlag.equalsIgnoreCase("502"))//Information provided is incorrect.
                {
                    return "502";
                }
                else if(mErrorFlag.equalsIgnoreCase("505 ")) //Some error occurred while storing in the database.
                {
                    return "505";
                }
                else
                {
                    return "";
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

            mProgressDialog.dismiss();
            if(result.equalsIgnoreCase("500")) // Mandatory parameters are empty
            {
                mShowDialog(OTPScreen.this, "", mResponseMsg, true, false);
            }
            else if(result.equalsIgnoreCase("501"))
            {
                //mShowDialog(OTPScreen.this,"",mResponseMsg,true,false);
                mEtOTP.setText(mOTP);
            }
            else if(result.equalsIgnoreCase("502"))
            {
                mShowDialog(OTPScreen.this,"",mResponseMsg,true,false);
            }
            else if(result.equalsIgnoreCase("505"))
            {
                mShowDialog(OTPScreen.this,"",mResponseMsg,true,false);
            }
            else
            {
                mShowDialog(OTPScreen.this,"",StaticUtils.ERROR_OCCURRED,true,false);
            }
        }
    }
}
