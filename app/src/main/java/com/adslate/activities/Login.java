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
import android.widget.TextView;

import com.adslate.R;
import com.adslate.baseclasses.BaseActivity;
import com.adslate.storage.AdSlateDB;
import com.adslate.utils.AdSlateUrlConnection;
import com.adslate.utils.StaticUtils;

import org.json.JSONObject;

import java.net.URL;

public class Login extends BaseActivity implements View.OnClickListener
{
    private TextView mTvLogin,mTvRegister,mTvForgotPassword;
    TextInputLayout mTLayEmailId,mTLayPassword;
    private EditText mEtEmail,mEtPassword;
    private String mEmail,mPassword;
    private ImageView mIvBack;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.e("USERTYPELOGIN", "" + mPref.mGetUserType());
        mInitialiseViews();

    }

    private void mInitialiseViews()
    {

        mTvLogin = (TextView)findViewById(R.id.xTvLogin);
        mTvRegister = (TextView)findViewById(R.id.xTvRegisterNow);
        mEtEmail = (EditText)findViewById(R.id.xEtEmailId);
        mEtPassword = (EditText)findViewById(R.id.xEtPassword);
        mTvForgotPassword = (TextView)findViewById(R.id.xTvForgotPassword);
        mIvBack = (ImageView)findViewById(R.id.xIvBackButton);
        mTLayEmailId = (TextInputLayout)findViewById(R.id.xTLayEmailId);
        mTLayPassword = (TextInputLayout)findViewById(R.id.xTLayPassword);

        mTvLogin.setOnClickListener(this);
        mTvRegister.setOnClickListener(this);
        mTvForgotPassword.setOnClickListener(this);
        mIvBack.setOnClickListener(this);

    }


    @Override
    public void onClick(View view)
    {

        if(view == mTvLogin )
        {
            mValidateDetails();
        }
        else if(view == mTvRegister)
        {
            Intent intent = new Intent(Login.this,Registration1.class);
            startActivity(intent);
            finish();
        }
        else if(view == mTvForgotPassword)
        {
            mShowDialog(Login.this,"","Development in progress",true,false);
        }
        else if (view == mIvBack)
        {
            finish();
        }
    }

    private void mValidateDetails()
    {

        mEmail = mEtEmail.getText().toString().trim();
        mPassword = mEtPassword.getText().toString().trim();


        if(mEmail.length() == 0)
        {

            mTLayEmailId.setErrorEnabled(true);
            mTLayEmailId.setError(StaticUtils.CHECK_DETAILS_EMAIL);

        }
        else
        {
            mTLayEmailId.setErrorEnabled(false);

            if(!StaticUtils.isValidEmail(mEmail))
            {
                mEtEmail.setError(StaticUtils.EMAIL_ADDRESS);

            }
            else
            {


                if(mPassword.length() == 0)
                {
                    mTLayPassword.setErrorEnabled(true);
                    mTLayPassword.setError(StaticUtils.CHECK_DETAILS_PASSWORD);

                }
                else
                {
                    mTLayPassword.setErrorEnabled(false);

                    if(mNetworkStatus.isNetworkAvailable())
                    {
                        new mLoginWebservice(mEmail,mPassword).execute();

                    }
                    else
                    {
                        mShowDialog(Login.this,"",StaticUtils.ERROR_OCCURRED,true,false);
                    }
                }

            }


        }
    }


    private class mLoginWebservice extends AsyncTask<String,Void,String>
    {

        private String email,password,mResponseMsg,isMobileVerified,phoneNumber,mUserId,mEmailFrmRes;

        public mLoginWebservice(String mEmail, String mPassword)
        {
            email = mEmail;
            password = mPassword;
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
                Log.e("EMAIL",""+email);
                Log.e("password", "" + password);
                Log.e("UserType", "" + mPref.mGetUserType());


                JSONObject jsonObjLogin = new JSONObject();
                jsonObjLogin.put("email", email);
                jsonObjLogin.put("password", password);
                jsonObjLogin.put("userType", mPref.mGetUserType());
                jsonObjLogin.put("loginType", "1");
                jsonObjLogin.put("deviceType", StaticUtils.DEVICE_TYPE);
                jsonObjLogin.put("deviceToken", mPref.mGetGCMId());

                ContentValues values = new ContentValues();
                values.put("json", String.valueOf(jsonObjLogin));

             /*   Uri buildUri = Uri.parse(StaticUtils.LOGIN_URL)
                        .buildUpon()
                        .appendQueryParameter("email", email)
                        .appendQueryParameter("password", password)
                        .appendQueryParameter("userType", mPref.mGetUserType())
                        .appendQueryParameter("loginType", "1")
                        .appendQueryParameter("deviceType","A")
                        .appendQueryParameter("deviceToken",mPref.mGetGCMId())
                        .build();

                url = new URL(buildUri.toString());

                Log.e("LOGIN_REQUEST",""+url);*/

                Log.e("LOGIN_REQUEST",""+StaticUtils.LOGIN_URL+String.valueOf(values));

                AdSlateUrlConnection connection = new AdSlateUrlConnection();
                String response = connection.mEstablishConnectionJSON(StaticUtils.LOGIN_URL, String.valueOf(values));

                Log.e("LOGIN_RESPONSE",""+response);

                JSONObject responseObj = new JSONObject(response);

                JSONObject resObj = responseObj.getJSONObject("response");

                mResponseMsg = resObj.getString("msg");

                String errorFlag = resObj.getString("errorFlag");

                if(errorFlag.equalsIgnoreCase("500")) //Mandatory fields are empty.
                {
                    return "500";
                }
                else if(errorFlag.equalsIgnoreCase("501")) // Successful.
                {
                    AdSlateDB mAdSlateDB = AdSlateDB.GET_INSTANCE(Login.this);
                    mAdSlateDB.mInsertIntoUserTable(resObj.getString("userId"), resObj.getString("userType"), resObj.getString("profileImage"), resObj.getString("loginType"), resObj.getString("userName"), resObj.getString("email"), resObj.getString("dob"),
                            resObj.getString("mobileNumber"), resObj.getString("orgType"), resObj.getString("address"), resObj.getString("orgDesc"), resObj.getString("orgTurnover"), resObj.getString("contactPersonName"),
                            resObj.getString("contactPersonDesignation"), resObj.getString("contactPersonMobile"), resObj.getString("orgBusinessCard"), resObj.getString("isMobileVerified"), resObj.getString("referralCode"), resObj.getString("otherOrgType"));

                    mAdSlateDB.close();

                    return "501";
                }
                else if(errorFlag.equalsIgnoreCase("502")) // Information provided is incorrect.
                {
                    return "502";
                }
                else if(errorFlag.equalsIgnoreCase("503")) //User registered but mobile number is not verified.
                {

                    mEmailFrmRes = resObj.getString("email");
                    mUserId = resObj.getString("userId");
                    if(mPref.mGetUserType().equalsIgnoreCase("1")) //Individual
                    {
                        phoneNumber = resObj.getString("mobileNumber");
                    }
                    else
                    {
                        phoneNumber = resObj.getString("contactPersonMobile");
                    }

                    return "503";
                }
                else if(errorFlag.equalsIgnoreCase("505"))  //Some error occurred while storing in the database.
                {
                    return "505";
                }
                else if(errorFlag.equalsIgnoreCase("506"))  //Logged in successfully with Social Media but mobile number is not entered
                {
                    isMobileVerified = resObj.getString("isMobileVerified");
                    mEmailFrmRes = resObj.getString("email");
                    mUserId = resObj.getString("userId");

                    if(mPref.mGetUserType().equalsIgnoreCase("1"))  //Individual
                    {
                        phoneNumber = resObj.getString("mobileNumber");
                    }
                    else
                    {
                        phoneNumber = resObj.getString("contactPersonMobile");
                    }

                    if(isMobileVerified.equalsIgnoreCase("1"))
                    {
                        AdSlateDB mAdSlateDB = AdSlateDB.GET_INSTANCE(Login.this);
                        mAdSlateDB.mInsertIntoUserTable(mUserId, resObj.getString("userType"), resObj.getString("profileImage"), resObj.getString("loginType"), resObj.getString("userName"), mEmailFrmRes, resObj.getString("dob"),
                                resObj.getString("mobileNumber"), resObj.getString("orgType"), resObj.getString("address"), resObj.getString("orgDesc"), resObj.getString("orgTurnover"), resObj.getString("contactPersonName"),
                                resObj.getString("contactPersonDesignation"), resObj.getString("contactPersonMobile"), resObj.getString("orgBusinessCard"), isMobileVerified, resObj.getString("referralCode"), resObj.getString("otherOrgType"));

                        mAdSlateDB.close();
                    }


                    return "506";
                }
                else if(errorFlag.equalsIgnoreCase("509")) //Account is Blocked
                {
                    return "509";
                }
                else if(errorFlag.equalsIgnoreCase("510")) //Error occurred while storing the image.
                {
                    return "510";
                }
                else if(errorFlag.equalsIgnoreCase("511")) // Image is not updated, please provide an image.
                {
                    return "511";
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

            if(result.equalsIgnoreCase("500"))//Mandatory fields are empty.
            {
                mShowDialog(Login.this, "", mResponseMsg, true, false);
            }
            else if(result.equalsIgnoreCase("501")) //Successful
            {
                mPref.mSaveLoginStatus(true);
                Intent intent = new Intent (Login.this, Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
            else if(result.equalsIgnoreCase("502")) // Information provided is incorrect.
            {
                mShowDialog(Login.this,"",mResponseMsg,true,false);
            }
            else if(result.equalsIgnoreCase("503"))  //User registered but mobile number is not verified.
            {

                Intent intent = new Intent(Login.this, OTPScreen.class);
                intent.putExtra("mEmail",mEmailFrmRes);
                intent.putExtra("mPhoneNumber",phoneNumber);
                intent.putExtra("mUserId",mUserId);
                intent.putExtra("mUserType", mPref.mGetUserType());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            else if(result.equalsIgnoreCase("505")) // Some error occurred while storing in the database.
            {
                mShowDialog(Login.this,"",mResponseMsg,true,false);
            }
            else if(result.equalsIgnoreCase("506")) //Logged in successfully with Social Media but mobile number is not entered
            {

                if(isMobileVerified.equalsIgnoreCase("0"))
                {
                    Intent intent = new Intent(Login.this, OTPScreen.class);
                    intent.putExtra("mEmail",mEmailFrmRes);
                    intent.putExtra("mPhoneNumber",phoneNumber);
                    intent.putExtra("mUserId",mUserId);
                    intent.putExtra("mUserType", mPref.mGetUserType());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }
                else
                {
                    mPref.mSaveLoginStatus(true);
                    Intent intent = new Intent(Login.this, Home.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }
            }
            else if(result.equalsIgnoreCase("509")) //Account is blocked.
            {
                mShowDialog(Login.this,"",mResponseMsg,true,false);
            }
            else if(result.equalsIgnoreCase("510")) //Error occurred while storing the image.
            {
                mShowDialog(Login.this,"",mResponseMsg,true,false);
            }
            else if(result.equalsIgnoreCase("511")) // Image is not updated, please provide an image.
            {
                mShowDialog(Login.this,"",mResponseMsg,true,false);
            }
            else
            {
                mShowDialog(Login.this,"",StaticUtils.ERROR_OCCURRED,true,false);
            }
        }
    }
}
