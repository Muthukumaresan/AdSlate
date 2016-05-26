package com.adslate.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.adslate.R;
import com.adslate.baseclasses.BaseActivity;
import com.adslate.storage.AdSlateDB;
import com.adslate.utils.AdSlateUrlConnection;
import com.adslate.utils.StaticUtils;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class Registration1 extends BaseActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, FacebookCallback<LoginResult> {


    private EditText mEtFullName,mEtEmail;
    private String mFullName,mEmail;
    private String mLoginType = "1" ;
    private ImageView mIvFaceBook,mIvGooglePlus,mIvBack;
    private View mLine;
    TextView mRegisterNow,mLogin;
    TextInputLayout mTLayFullName,mTLayEmail;
    LinearLayout mLinLaySocialMedia;
    private GoogleApiClient mGoogleApiClient;
    private boolean mSignInClicked;
    private String TAG = "Registration1Activity";
    private ConnectionResult mConnectionResult;
    private boolean mIntentInProgress;
    private static final int RC_SIGN_IN = 0;
    private CallbackManager mCallbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration1);
        mInitialiseViews();

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager, Registration1.this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .addApi(Plus.API)
                .build();
    }

    private void mInitialiseViews()
    {


        mEtFullName = (EditText)findViewById(R.id.xEtFullName);
        mEtEmail = (EditText)findViewById(R.id.xEtEmail);
        mIvBack = (ImageView)findViewById(R.id.xIvBack);
        mIvFaceBook = (ImageView)findViewById(R.id.xIvFaceBook);
        mIvGooglePlus = (ImageView)findViewById(R.id.xIvGooglePlus);
        mRegisterNow = (TextView)findViewById(R.id.xTvRegisterNow);
        mLogin = (TextView)findViewById(R.id.xTvLogin);
        mLine = (View)findViewById(R.id.xVwLine);
        mTLayFullName = (TextInputLayout)findViewById(R.id.xTLayFullName);
        mTLayEmail =  (TextInputLayout)findViewById(R.id.xTLayEmail);

        mLinLaySocialMedia = (LinearLayout)findViewById(R.id.xLinLaySocialMedia);

        mIvBack.setOnClickListener(this);
        mIvFaceBook.setOnClickListener(this);
        mIvGooglePlus.setOnClickListener(this);
        mRegisterNow.setOnClickListener(this);
        mLogin.setOnClickListener(this);


        if(mPref.mGetUserType().equalsIgnoreCase("1")) //Social media registration only for Individual user type
        {
            mLinLaySocialMedia.setVisibility(View.VISIBLE);
            mLine.setVisibility(View.VISIBLE);
        }
        else
        {
            mLinLaySocialMedia.setVisibility(View.GONE);
            mLine.setVisibility(View.GONE);

        }

    }


    @Override
    protected void onStop()
    {
        super.onStop();
        mGoogleApiClient.disconnect();
    }


    private void googleSignIn()
    {
        if (!mGoogleApiClient.isConnecting())
        {
            mSignInClicked = true;
            mGoogleApiClient.connect();
        }
    }


    @Override
    public void onClick(View view)
    {

        if(view == mRegisterNow)
        {
            mValidate();
        }
        else if(view == mLogin)
        {
            Intent intent = new Intent(Registration1.this,Login.class);
            startActivity(intent);
            finish();
        }

        else if(view == mIvFaceBook)
        {

            if(mNetworkStatus.isNetworkAvailable())
            {
                try
                {
                    mLoginType = "2";
                    LoginManager.getInstance().logInWithReadPermissions(Registration1.this, Arrays.asList("public_profile", "email", "user_birthday"));

                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                mShowDialog(Registration1.this,StaticUtils.NO_INTERNET_TITLE,StaticUtils.NO_INTERNET_MESSAGE,true,false);

            }


        }
        else if(view == mIvGooglePlus)
        {

            if(mNetworkStatus.isNetworkAvailable())
            {
                mLoginType = "3";
                googleSignIn();
            }
            else
            {
                mShowDialog(Registration1.this,StaticUtils.NO_INTERNET_TITLE,StaticUtils.NO_INTERNET_MESSAGE,true,false);
            }


        }
        else if(view == mIvBack)
        {
            finish();
        }

    }

    @Override
    protected void onResume()
    {
        super.onResume();

        mEtFullName.setText("");
        mEtEmail.setText("");
    }

    public void mSignOutFromGooglePlus()
    {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
        }
    }

    public void mSignOutFromFacebook()
    {
        LoginManager.getInstance().logOut();
    }

    private void mValidate()
    {
        mFullName = mEtFullName.getText().toString().trim();
        mEmail = mEtEmail.getText().toString().trim();

        if(mFullName.length() == 0)
        {
            mTLayFullName.setErrorEnabled(true);
            mTLayFullName.setError(StaticUtils.CHECK_DETAILS_FULLNAME);
        }
        else
        {

            mTLayFullName.setErrorEnabled(false);

            if(mEmail.length() == 0)
            {
                mTLayEmail.setErrorEnabled(true);
                mTLayEmail.setError(StaticUtils.CHECK_DETAILS_EMAIL);
            }
            else
            {
                mTLayEmail.setErrorEnabled(false);

                if(StaticUtils.isValidEmail(mEmail))
                {

                    Intent intent = new Intent(Registration1.this,Registration2.class);
                    intent.putExtra("fullName",mFullName);
                    intent.putExtra("email",mEmail);
                    intent.putExtra("loginType",mLoginType);
                    startActivity(intent);
                    finish();
                }
                else
                {

                    mEtEmail.setError(StaticUtils.EMAIL_ADDRESS);
                    //   mShowDialog(Registration1.this, "", StaticUtils.EMAIL_ADDRESS, true, false);
                }
            }



        }

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mSignOutFromFacebook();
        mSignOutFromGooglePlus();
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        mSignInClicked = false;
        Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
        // Get user's information
        getProfileInformation();
    }

    private void getProfileInformation()
    {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null)
            {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                String userName = currentPerson.getDisplayName();
                String profilePic = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);


                Log.e(TAG, "Name: " + userName + ", plusProfile: "
                        + personGooglePlusProfile + ", email: " + email
                        + ", Image: " + profilePic);

                //call webservice and to check if OTP is verified


                if(mNetworkStatus.isNetworkAvailable())
                {
                    new SocialMediaRegister(email,userName,profilePic).execute();
                }
                else
                {
                    mShowDialog(Registration1.this,StaticUtils.NO_INTERNET_TITLE,StaticUtils.NO_INTERNET_MESSAGE,true,false);
                }


            }
            else
            {
                Toast.makeText(getApplicationContext(), "Person information is null", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();

        }

    }

    @Override
    public void onConnectionSuspended(int i)
    {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
        if (!connectionResult.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this,
                    0).show();
            return;
        }

        if (!mIntentInProgress)
        {
            // Store the ConnectionResult for later usage
            mConnectionResult = connectionResult;

            if (mSignInClicked)
            {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }

    }

    private void resolveSignInError()
    {
        if (mConnectionResult.hasResolution())
        {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data)
    {
        if (requestCode == RC_SIGN_IN)
        {
            if (responseCode != RESULT_OK)
            {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting())
            {
                mGoogleApiClient.connect();
            }
        }
        else
        {
            mCallbackManager.onActivityResult(requestCode, responseCode, data);
        }
    }

    @Override
    public void onSuccess(LoginResult loginResult)
    {
        System.out.println("onSuccess");
        String accessToken = loginResult.getAccessToken().getToken();
        Log.i("accessToken", accessToken);
        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback()
        {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response)
            {
                Log.i("LoginActivity", response.toString());
                try {
                    String id = object.getString("id");
                    URL profilePic = null;
                    try {
                        profilePic = new URL("http://graph.facebook.com/" + id + "/picture?type=large");
                        Log.i("profile_pic", profilePic + "");

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    String userName = object.getString("name");
                    String email = object.getString("email");
                    String gender = object.getString("gender");


                    Log.e("faceBook Username", userName);
                    Log.e("faceBook email", email);


                    if(mNetworkStatus.isNetworkAvailable())
                    {
                        new SocialMediaRegister(email, userName, profilePic.toString()).execute();
                    }
                    else
                    {
                        mShowDialog(Registration1.this,StaticUtils.NO_INTERNET_TITLE,StaticUtils.NO_INTERNET_MESSAGE,true,false);
                    }


                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }

        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender, birthday");
        request.setParameters(parameters);
        request.executeAsync();

    }



    @Override
    public void onCancel()
    {
        Log.e("", "Facebook login onCancel");
        Toast.makeText(Registration1.this, "Canceled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(FacebookException e)
    {
        Log.e("", "Facebook login onError");
        Toast.makeText(Registration1.this, "Error Occured", Toast.LENGTH_SHORT).show();
    }

    private class SocialMediaRegister extends AsyncTask<String,Void,String>
    {
        private String mEmail,mUserName,mProfilePic,mUserId,phoneNumber,isMobileVerified,mEmailFrmRes;
        private String mResponseMsg;
        private URL url;


        public SocialMediaRegister(String email, String userName, String profilePic)
        {
            mEmail =  email;
            mUserName = userName;
            mProfilePic = profilePic;

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
                Log.e("LoginType",""+mLoginType);
                Log.e("ProfilePic",""+mProfilePic);

               /* Uri buildUri = Uri.parse(StaticUtils.LOGIN_URL)
                        .buildUpon()
                        .appendQueryParameter("email", mEmail)
                        .appendQueryParameter("userType",mPref.mGetUserType())
                        .appendQueryParameter("loginType", mLoginType)
                        .appendQueryParameter("profileImage",mProfilePic)
                        .appendQueryParameter("username", mUserName)
                        .appendQueryParameter("deviceType", StaticUtils.DEVICE_TYPE)
                        .appendQueryParameter("deviceToken", mPref.mGetGCMId())
                        .build();

                url = new URL(buildUri.toString());

                Log.e("SOCIAL_MEDIA_REQUEST",""+url);*/

                JSONObject jsonObjLogin = new JSONObject();
                jsonObjLogin.put("email", mEmail);
                jsonObjLogin.put("userType", mPref.mGetUserType());
                jsonObjLogin.put("loginType", mLoginType);
                jsonObjLogin.put("profileImage", mProfilePic);
                jsonObjLogin.put("username", mUserName);
                jsonObjLogin.put("deviceType",StaticUtils.DEVICE_TYPE);
                jsonObjLogin.put("deviceToken", mPref.mGetGCMId());

                ContentValues values = new ContentValues();
                values.put("json", String.valueOf(jsonObjLogin));

                Log.e("SOCIAL_MEDIA_REQUEST", "" + StaticUtils.LOGIN_URL+String.valueOf(values));

                AdSlateUrlConnection connection = new AdSlateUrlConnection();
                String response = connection.mEstablishConnectionJSON(StaticUtils.LOGIN_URL, String.valueOf(values));

                Log.e("SOCIAL_MEDIA_RESPONSE",""+response);

                JSONObject mResponse = new JSONObject(response);


                JSONObject resObj = mResponse.getJSONObject("response");

                mResponseMsg = resObj.getString("msg");

                String errorFlag = resObj.getString("errorFlag");

                if(errorFlag.equalsIgnoreCase("500")) //Mandatory fields are empty.
                {
                    return "500";
                }
                else if(errorFlag.equalsIgnoreCase("501")) // Successful.
                {
                    AdSlateDB mAdSlateDB = AdSlateDB.GET_INSTANCE(Registration1.this);

                    Log.e("PROFILE_IMAGE", "" + resObj.getString("profileImage"));
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
                    mUserId = resObj.getString("userId");
                    phoneNumber = resObj.getString("mobileNumber");
                    mEmailFrmRes = resObj.getString("email");

                    return "503";
                }
                else if(errorFlag.equalsIgnoreCase("504"))  //Email already exists.
                {
                    return "504";
                }
                else if(errorFlag.equalsIgnoreCase("505"))  //Some error occurred while storing in the database.
                {
                    return "505";
                }
                else if(errorFlag.equalsIgnoreCase("506"))  //Logged in successfully with Social Media but mobile number is not entered
                {
                    mUserId = resObj.getString("userId");
                    phoneNumber = resObj.getString("mobileNumber");
                    mEmailFrmRes = resObj.getString("email");

                    isMobileVerified = resObj.getString("isMobileVerified");

                    if(isMobileVerified.equalsIgnoreCase("1"))
                    {
                        AdSlateDB mAdSlateDB = AdSlateDB.GET_INSTANCE(Registration1.this);

                        Log.e("PROFILE_IMAGEZZZ", "" + resObj.getString("profileImage"));
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
                } else if(errorFlag.equalsIgnoreCase("511")) // Image is not updated, please provide an image.
                {
                    return "511";
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

            mLogoutFrmSocialMedia();

            if(result.equalsIgnoreCase("500"))//Mandatory fields are empty.
            {
                mShowDialog(Registration1.this, "", mResponseMsg, true, false);
            }
            else if(result.equalsIgnoreCase("501")) //Successful
            {
                mPref.mSaveLoginStatus(true);
                Intent intent = new Intent (Registration1.this, Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
            else if(result.equalsIgnoreCase("502")) // Information provided is incorrect.
            {
                mShowDialog(Registration1.this,"",mResponseMsg,true,false);
            }
            else if(result.equalsIgnoreCase("503"))  //User registered but mobile number is not verified.
            {
                Intent intent = new Intent(Registration1.this, OTPScreen.class);
                intent.putExtra("mEmail",mEmailFrmRes);
                intent.putExtra("mPhoneNumber",phoneNumber);
                intent.putExtra("mUserId",mUserId);
                intent.putExtra("mUserType",mPref.mGetUserType());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
            else if(result.equalsIgnoreCase("504")) // Email already exists.

            {
                mShowDialog(Registration1.this,"",mResponseMsg,true,false);
            }

            else if(result.equalsIgnoreCase("505")) // Some error occurred while storing in the database.
            {
                mShowDialog(Registration1.this,"",mResponseMsg, true, false);
            }
            else if(result.equalsIgnoreCase("506")) //Logged in successfully with Social Media but mobile number is not entered
            {
                if(isMobileVerified.equalsIgnoreCase("0"))
                {
                    Intent intent = new Intent(Registration1.this, OTPScreen.class);
                    intent.putExtra("mEmail",mEmailFrmRes);
                    intent.putExtra("mPhoneNumber",phoneNumber);
                    intent.putExtra("mUserId",mUserId);
                    intent.putExtra("mUserType", mPref.mGetUserType());
                    startActivity(intent);
                    finish();
                }
                else
                {
                    mPref.mSaveLoginStatus(true);
                    Intent intent = new Intent(Registration1.this, Home.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }
            }
            else if(result.equalsIgnoreCase("509")) //Account is blocked.
            {
                mShowDialog(Registration1.this,"",mResponseMsg,true,false);
            }
            else if(result.equalsIgnoreCase("510")) //Error occurred while storing the image.
            {
                mShowDialog(Registration1.this,"",mResponseMsg,true,false);
            }
            else if(result.equalsIgnoreCase("511")) // Image is not updated, please provide an image.
            {
                mShowDialog(Registration1.this,"",mResponseMsg,true,false);
            }
            else
            {
                mShowDialog(Registration1.this,"",StaticUtils.ERROR_OCCURRED,true,false);
            }
        }

    }

    private void mLogoutFrmSocialMedia()
    {
        if(mLoginType.equalsIgnoreCase("2")) //Facebook
        {
            Log.e("FaceBook",""+"FaceBookLogout");
            mSignOutFromFacebook();
        }
        else if(mLoginType.equalsIgnoreCase("3"))//Google
        {
            Log.e("GooglePlus",""+"GooglePlusLogout");
            mSignOutFromGooglePlus();
        }
    }
}

