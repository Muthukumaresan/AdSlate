package com.adslate.activities;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import android.widget.TextView;
import android.widget.Toast;

import com.adslate.R;
import com.adslate.baseclasses.BaseActivity;
import com.adslate.helperclasses.GalleryHelper;
import com.adslate.helperclasses.RoundedImageView;
import com.adslate.models.OrganizationType;
import com.adslate.storage.AdSlateDB;
import com.adslate.utils.AdSlateMultipartUrlConn;
import com.adslate.utils.AdSlateUrlConnection;
import com.adslate.utils.StaticUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Registration2 extends BaseActivity implements View.OnClickListener
{
    private LinearLayout mLinLayIndv,mLinLayEnt;
    private EditText mEtFullName,mEtEmail,mEtPassword,mEtAddress,mEtPhNum,mEtOrgDesc,mEtOrgTurnOvr,mEtContPersnName,mEtContPerDesgntn,mEtContPerNum,mEtOrgTypeOdrs;
    private TextView mTvTerms,mTvPrivacy,mTvSignUp,mTvDOB,mTvOrgType;
    TextInputLayout mTLayPassword,mTLayPhNum,mTLayOrgTypeOdrs,mTLayOrgDesc,mTLayCntPersnName,mTLayCntPersnDesgntn,mTLayCntPerPhNum;
    private String mPickdYear,mPickdMonth,mPickdDay,mFullName,mEmail,mPassword,mDOB,mPhoneNumber,mLoginType,mOrgType,mOrgDesc,mContPerName,mContPerDesgntn,mContPerPhNum,mAddress,mGender,mOrgTurnOvr,dobDateFormatted,mOrgTypeOthers,mPasswordOrgType;
    private CheckBox mCheckBoxTermsCondition;
    private ArrayList<String>mOrganizationNames;
    private RoundedImageView mRIvProfileImage;
    private ImageView mIvBusinessCard, mIvBack;
    private int REQUEST_CAMERA = 100;
    private int SELECT_FILE = 200;
    private int imageType ;
    String mProfileImgPath = "",mBusinessCardImgPath = "";
    private ArrayList<OrganizationType> mOrgTypeArr;


    DisplayImageOptions mOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration2);

        mOptions =  new DisplayImageOptions.Builder().considerExifParams(true).build();
        getIntentValues();
        mInitialiseViews();

        if(mPref.mGetUserType().equalsIgnoreCase("2"))
        {
            if(mNetworkStatus.isNetworkAvailable())
            {
                new getOrgTypeWebservice().execute();
            }
            else
            {
                Toast.makeText(Registration2.this,"Could not update Organization type",Toast.LENGTH_LONG).show();
            }
        }

    }

    private void mInitialiseViews()
    {

        mEtFullName = (EditText) findViewById(R.id.xEtFullName);
        mEtEmail = (EditText) findViewById(R.id.xEtEmail);
        mEtPassword = (EditText) findViewById(R.id.xEtPassword);
        mEtAddress = (EditText) findViewById(R.id.xEtAddress);
        mEtOrgTypeOdrs = (EditText)findViewById(R.id.xEtOrgTypeOdrs);
        mTvTerms = (TextView) findViewById(R.id.xTvTerms);
        mTvPrivacy = (TextView)findViewById(R.id.xTvPrivacy);
        mTvSignUp = (TextView)findViewById(R.id.xTvSignUp);
        mLinLayIndv = (LinearLayout)findViewById(R.id.xLinLayIndv);
        mLinLayEnt = (LinearLayout)findViewById(R.id.xLinLayEnt);
        mRIvProfileImage = (RoundedImageView)findViewById(R.id.xRIvProfilePic);
        mIvBusinessCard = (ImageView)findViewById(R.id.xTvBusinessCardImage);
        mIvBack = (ImageView)findViewById(R.id.xIvBack);

        mTLayPassword = (TextInputLayout)findViewById(R.id.xTLayPassword);
        mTLayPhNum = (TextInputLayout)findViewById(R.id.xTLayPhNum);
        mTLayOrgTypeOdrs = (TextInputLayout)findViewById(R.id.xTLayOrgTypeOdrs);
        mTLayOrgDesc =  (TextInputLayout)findViewById(R.id.xTLayOrgDesc);
        mTLayCntPersnName = (TextInputLayout)findViewById(R.id.xTLayCntPersnName);
        mTLayCntPersnDesgntn = (TextInputLayout)findViewById(R.id.xTLayCntPersnDesgntn);
        mTLayCntPerPhNum = (TextInputLayout)findViewById(R.id.xTLayCntPerPhNum);

        mCheckBoxTermsCondition = (CheckBox)findViewById(R.id.xCheckBoxTermsCondition);

        if(mPref.mGetUserType().equalsIgnoreCase("1"))
        {
            mTvDOB = (TextView) findViewById(R.id.xTvDOB);
            mEtPhNum = (EditText) findViewById(R.id.xEtPhNum);

            mTvDOB.setOnClickListener(this);
            mLinLayEnt.setVisibility(View.GONE);
        }
        else
        {
            mLinLayIndv.setVisibility(View.GONE);
            mTvOrgType = (TextView) findViewById(R.id.xTvOrgType);
            mEtOrgDesc = (EditText)findViewById(R.id.xEtOrgDesc);
            mEtOrgTurnOvr = (EditText)findViewById(R.id.xEtOrgTurnOvr);
            mEtContPersnName = (EditText)findViewById(R.id.xEtCntPersnName);
            mEtContPerDesgntn = (EditText)findViewById(R.id.xEtCntPersnDesgntn);
            mEtContPerNum = (EditText)findViewById(R.id.xEtCntPersnPhNum);
            mTLayOrgTypeOdrs.setVisibility(View.GONE);
            mTvOrgType.setOnClickListener(this);
        }


        mTvTerms.setOnClickListener(this);
        mTvPrivacy.setOnClickListener(this);
        mTvSignUp.setOnClickListener(this);
        mRIvProfileImage.setOnClickListener(this);
        mIvBusinessCard.setOnClickListener(this);
        mIvBack.setOnClickListener(this);


        mEtFullName.setText(mFullName);
        mEtEmail.setText(mEmail);

    }


    private void getIntentValues()
    {
        Intent intent = getIntent();
        mFullName = intent.getStringExtra("fullName");
        mEmail = intent.getStringExtra("email");
        mLoginType = intent.getStringExtra("loginType");

    }

    @Override
    public void onClick(View view)
    {
        if(view == mIvBack)
        {
            finish();
        }
        else if( view == mTvDOB)
        {
            datePicker();
        }
        else if(view == mTvSignUp)
        {
            mValidateFields();
        }
        else if(view == mTvOrgType)
        {
            mShowOrgTypeDialog();
        }
        else if(view == mRIvProfileImage)
        {
            imageType = 1; // profile Image
            selectImage();
        }
        else if(view == mIvBusinessCard)
        {
            imageType = 2; //BusinessCard
            selectImage();
        }
        else if(view == mTvTerms)
        {
            Intent intent = new Intent(Registration2.this,TermsAndCondition.class);
            startActivity(intent);
        }
        else if(view == mTvPrivacy)
        {
            Intent intent = new Intent(Registration2.this,PrivacyPolicy.class);
            startActivity(intent);
        }

    }

    private void selectImage()
    {
        final CharSequence[] items = { "Take Photo", "Choose from Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Registration2.this);
        builder.setTitle("Add Photo");
        builder.setCancelable(true);
        builder.setItems(items, new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int i)
            {
                if(items[i].equals("Take Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,REQUEST_CAMERA);

                }
                else if(items[i].equals("Choose from Gallery"))
                {
                    Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);

                }

            }

        });
        builder.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == SELECT_FILE || requestCode == REQUEST_CAMERA) && resultCode == Activity.RESULT_OK && data != null)
        {
            Uri selectedImage = null;
            String picturePath="";

            final File in;
            try
            {
                selectedImage = data.getData();

                //    picturePath = cursor.getString(columnIndex);
                if (Build.VERSION.SDK_INT < 19)
                {
                    Cursor cursor = null;
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

                    picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    Log.e("", "" + picturePath);
                }
                else
                {
                    if(selectedImage != null)
                    {
                        picturePath = GalleryHelper.getPath(getApplicationContext(), selectedImage);
                        Log.e("", "" + picturePath);
                    } else
                    {
                        if(requestCode == REQUEST_CAMERA)//in some devices(kitkat, lollipop) i am getting the Uri is null. so i am getting the last image what i took from camera.
                        {
                            Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    new String[] { MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.ImageColumns.ORIENTATION }, MediaStore.Images.Media.DATE_ADDED, null, "date_added ASC");
                            if (cursor != null && cursor.moveToLast())
                            {
                                selectedImage = Uri.parse(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                                picturePath = selectedImage.toString();
                                cursor.close();
                            }
                        }
                    }
                }

                if(imageType == 1)
                {

                    ImageLoader.getInstance().displayImage(selectedImage.toString(), mRIvProfileImage, mOptions);

                    mProfileImgPath = picturePath;
                }
                else
                {
                    ImageLoader.getInstance().displayImage(selectedImage.toString(), mIvBusinessCard, mOptions);
                    mBusinessCardImgPath = picturePath;
                }


            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private void mShowOrgTypeDialog()
    {

        final Dialog mDialog = new Dialog(Registration2.this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.org_type);
        mDialog.show();

        Window window = mDialog.getWindow();
        ListView mOrgType = (ListView) window.findViewById(R.id.xLvOrgType);

        getOrganizationTypeFrmDB();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1,mOrganizationNames);
        mOrgType.setAdapter(adapter);
        mOrgType.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View convertView, int position, long id)
            {
                mDialog.dismiss();
                mTvOrgType.setText(mOrganizationNames.get(position));

                if (mOrganizationNames.get(position).equalsIgnoreCase(StaticUtils.ORG_TYPENAME_OTHERS))
                {
                    mTLayOrgTypeOdrs.setVisibility(View.VISIBLE);
                }
                else
                {
                    mTLayOrgTypeOdrs.setVisibility(View.GONE);
                }
            }
        });

    }

    private void getOrganizationTypeFrmDB()
    {
        AdSlateDB mAdSlateDB = AdSlateDB.GET_INSTANCE(Registration2.this);
        Cursor cursor = mAdSlateDB.mSelectAllData(mAdSlateDB.mOrganizationType);

        mOrganizationNames = new ArrayList<String>();
        if(cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            do {
                mOrganizationNames.add(cursor.getString(cursor.getColumnIndex("_name")));
            }while(cursor.moveToNext());

            mOrganizationNames.add(StaticUtils.ORG_TYPENAME_OTHERS);
        }
        cursor.close();
        mAdSlateDB.close();

    }

    private void mValidateFields()
    {
        mPassword = mEtPassword.getText().toString().trim();
        mAddress = mEtAddress.getText().toString().trim();

        if(mPref.mGetUserType().equalsIgnoreCase("1"))   //Individual user = 1
        {
            mPhoneNumber = mEtPhNum.getText().toString().trim();
            mDOB = mTvDOB.getText().toString().trim();

            if( mPassword.length() == 0 )
            {
                mTLayPassword.setError(StaticUtils.CHECK_DETAILS_PASSWORD);

            }
            else
            {
                mTLayPassword.setErrorEnabled(false);

                if( mDOB.length() == 0 )
                {
                    mTvDOB.setError(StaticUtils.CHECK_DETAILS_DOB);

                }
                else
                {
                    mTLayPhNum.setErrorEnabled(false);

                    if(mPhoneNumber.length() == 0)
                    {
                        mTLayPhNum.setError(StaticUtils.CHECK_DETAILS_PH);
                    }
                    else
                    {
                        if(mProfileImgPath.equalsIgnoreCase(""))
                        {
                            mShowDialog(Registration2.this, "", StaticUtils.PROFILE_PICTURE, true,false);
                        }
                        else
                        {


                            SimpleDateFormat formatToString = new SimpleDateFormat("dd-MM-yyyy");
                            SimpleDateFormat formatToDate = new SimpleDateFormat("MMMM dd, yyyy");
                            Date date;
                            try {
                                date = formatToDate.parse(mDOB);
                                dobDateFormatted = formatToString.format(date);
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }

                            if(!(mCheckBoxTermsCondition.isChecked()))
                            {
                                mShowDialog(Registration2.this,"",StaticUtils.TERMS_CONDITION,true,false);

                            }
                            else
                            {
                                if(mNetworkStatus.isNetworkAvailable())
                                {
                                    new RegisterWebservice().execute();
                                }
                                else
                                {
                                    mShowDialog(Registration2.this,StaticUtils.NO_INTERNET_TITLE,StaticUtils.NO_INTERNET_MESSAGE,true,false);
                                }
                            }
                        }

                    }
                }

            }
        }
        else // Enterprise User = 2
        {
            mOrgType = mTvOrgType.getText().toString().trim();
            mOrgDesc = mEtOrgDesc.getText().toString().trim();
            mOrgTurnOvr = mEtOrgTurnOvr.getText().toString().trim();
            mContPerName = mEtContPersnName.getText().toString().trim();
            mContPerDesgntn = mEtContPerDesgntn.getText().toString().trim();
            mContPerPhNum = mEtContPerNum.getText().toString().trim();
            mOrgTypeOthers = mEtOrgTypeOdrs.getText().toString().trim();
            mPassword = mEtPassword.getText().toString().trim();
            mAddress = mEtAddress.getText().toString().trim();


            if(mOrgType.length() == 0 ||mOrgDesc.length() == 0 ||mContPerName.length() == 0 ||mContPerDesgntn.length() == 0 ||mContPerPhNum.length() == 0 ||mPassword.length()== 0)
            {

                // mShowDialog(Registration2.this, "", StaticUtils.ENTER_ALL_MANDATORY_FIELDS, true,false);
                mTLayPassword.setError(StaticUtils.CHECK_DETAILS_PASSWORD);
                mTvOrgType.setError(StaticUtils.CHECK_DETAILS_ORG);
                mTLayOrgDesc.setError(StaticUtils.CHECK_DETAILS_ORG_DESC);
                mTLayCntPersnName.setError(StaticUtils.CHECK_DETAILS_CONTPERSON_NAME);
                mTLayCntPersnDesgntn.setError(StaticUtils.CHECK_DETAILS_CONTPERSON_DESG);
                mTLayCntPerPhNum.setError(StaticUtils.CHECK_DETAILS_CONTPERSON_PH);
            }
            else
            {
                mTLayPassword.setErrorEnabled(false);
                mTLayOrgDesc.setErrorEnabled(false);
                mTLayCntPersnName.setErrorEnabled(false);
                mTLayCntPersnDesgntn.setErrorEnabled(false);
                mTLayCntPerPhNum.setErrorEnabled(false);


                if(mProfileImgPath.equalsIgnoreCase(""))
                {
                    mShowDialog(Registration2.this, "", StaticUtils.PROFILE_PICTURE, true,false);
                }
                else
                {
                    if(mOrgType.equalsIgnoreCase(StaticUtils.ORG_TYPENAME_OTHERS))
                    {
                        if(mEtOrgTypeOdrs.length() == 0)
                        {
                            // mShowDialog(Registration2.this, "", StaticUtils.ORG_TYPE_MSG, true,false);
                            mTLayOrgTypeOdrs.setError(StaticUtils.CHECK_DETAILS_CONTPERSON_ODRORG);
                        }
                        else
                        {
                            mTLayOrgTypeOdrs.setErrorEnabled(false);
                            if (!(mCheckBoxTermsCondition.isChecked()))
                            {
                                mShowDialog(Registration2.this, "", StaticUtils.TERMS_CONDITION, true, false);
                            }
                            else
                            {
                                if (mNetworkStatus.isNetworkAvailable())
                                {
                                    new RegisterWebservice().execute();
                                }
                                else
                                {
                                    mShowDialog(Registration2.this, StaticUtils.NO_INTERNET_TITLE, StaticUtils.NO_INTERNET_MESSAGE, true, false);
                                }
                            }
                        }

                    }
                    else
                    {
                        if (!(mCheckBoxTermsCondition.isChecked()))
                        {
                            mShowDialog(Registration2.this, "", StaticUtils.TERMS_CONDITION, true, false);
                        }
                        else
                        {
                            if (mNetworkStatus.isNetworkAvailable())
                            {
                                new RegisterWebservice().execute();
                            }
                            else
                            {
                                mShowDialog(Registration2.this, StaticUtils.NO_INTERNET_TITLE, StaticUtils.NO_INTERNET_MESSAGE, true, false);
                            }
                        }
                    }
                }

            }

        }
    }


    private void datePicker()
    {

        int mCalDay, mCalMonth,mCalYear;
        Calendar cal = Calendar.getInstance();
        mCalDay = cal.get(Calendar.DAY_OF_MONTH);
        mCalMonth = cal.get(Calendar.MONTH);
        mCalYear = cal.get(Calendar.YEAR) - 20;

        DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                mPickdYear = String.valueOf(year);
                mPickdMonth = String.valueOf(month);
                mPickdDay = String.valueOf(day);

                String dateOfBirth = String.valueOf(mPickdYear+"-"+mPickdMonth+"-"+mPickdDay);
                Log.e("Date", "" + dateOfBirth);

                SimpleDateFormat formatToDate = new SimpleDateFormat("yyyy-mm-dd");
                SimpleDateFormat formatToString = new SimpleDateFormat("MMMM dd, yyyy");
                String printDate = null;
                Date date = null;

                try {
                    date = formatToDate.parse(dateOfBirth);
                    Log.e("DateDate",""+date);


                } catch (Exception e)
                {
                    e.printStackTrace();
                }

                printDate = formatToString.format(date);
                Log.e("StringDate",""+printDate);

                mTvDOB.setText(printDate);


            }
        },mCalYear,mCalMonth,mCalDay);
        datePicker.show();

    }

    private class RegisterWebservice extends AsyncTask<String,Void,String>
    {
        String mResponseMsg,mUserId,isMobileVerified,mEmailFrmRes;
        private URL url;


        @Override
        protected void onPreExecute()
        {
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings)
        {
            try {

                AdSlateDB mAdSlateDB = AdSlateDB.GET_INSTANCE(Registration2.this);

                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

                JSONObject jsonObject = new JSONObject();

                url = new URL(StaticUtils.REGISTER);
                if (mPref.mGetUserType().equalsIgnoreCase("1"))
                {
                    FileBody fileBodyProfileImg = new FileBody(new File(mProfileImgPath));
                    builder.addPart("profileImage", fileBodyProfileImg);
                    builder.addPart("userType", new StringBody(mPref.mGetUserType(), ContentType.TEXT_PLAIN));
                    builder.addPart("logintype", new StringBody(mLoginType, ContentType.TEXT_PLAIN));
                    builder.addPart("username", new StringBody(mFullName, ContentType.TEXT_PLAIN));
                    builder.addPart("email", new StringBody(mEmail, ContentType.TEXT_PLAIN));
                    builder.addPart("password", new StringBody(mPassword, ContentType.TEXT_PLAIN));
                    builder.addPart("userdob", new StringBody(dobDateFormatted, ContentType.TEXT_PLAIN));
                    builder.addPart("mobile", new StringBody(mPhoneNumber, ContentType.TEXT_PLAIN));
                    builder.addPart("address", new StringBody(mAddress, ContentType.TEXT_PLAIN));
                    builder.addPart("deviceType", new StringBody(StaticUtils.DEVICE_TYPE, ContentType.TEXT_PLAIN));
                    builder.addPart("deviceToken", new StringBody(mPref.mGetGCMId(), ContentType.TEXT_PLAIN));


                    /*jsonObject.put("profileImage",mProfileImgPath);
                    jsonObject.put("userType",mPref.mGetUserType());
                    jsonObject.put("logintype",mLoginType);
                    jsonObject.put("username",mFullName);
                    jsonObject.put("email",mEmail);
                    jsonObject.put("password",mPassword);
                    jsonObject.put("userdob",dobDateFormatted);
                    jsonObject.put("mobile",mPhoneNumber);
                    jsonObject.put("address",mAddress);
                    jsonObject.put("deviceType", StaticUtils.DEVICE_TYPE);
                    jsonObject.put("deviceToken", mPref.mGetGCMId());*/








                }
                else
                {

                    Log.e("profileImg",""+mProfileImgPath);
                    // Log.e("cardImg", "" + mBusinessCardImgPath);
                    Log.e("phNUM", "" + mContPerPhNum);
                    Log.e("OrgType", "" + mOrgType);
                    Log.e("OdrOrgType", "" + mOrgTypeOthers);

                    FileBody fileBodyProfileImg = new FileBody(new File(mProfileImgPath));
                    builder.addPart("profileImage", fileBodyProfileImg);
                    builder.addPart("userType", new StringBody(mPref.mGetUserType(), ContentType.TEXT_PLAIN));
                    builder.addPart("logintype", new StringBody(mLoginType, ContentType.TEXT_PLAIN));
                    builder.addPart("username", new StringBody(mFullName, ContentType.TEXT_PLAIN));
                    builder.addPart("email", new StringBody(mEmail, ContentType.TEXT_PLAIN));
                    builder.addPart("password", new StringBody(mPassword, ContentType.TEXT_PLAIN));
                    builder.addPart("address", new StringBody(mAddress, ContentType.TEXT_PLAIN));
                    builder.addPart("orgdesc", new StringBody(mOrgDesc, ContentType.TEXT_PLAIN));
                    builder.addPart("orgturnover", new StringBody(mOrgTurnOvr,ContentType.TEXT_PLAIN));
                    builder.addPart("contactpersonname", new StringBody(mContPerName, ContentType.TEXT_PLAIN));
                    builder.addPart("contactpersondesignation", new StringBody(mContPerDesgntn, ContentType.TEXT_PLAIN));
                    builder.addPart("contactpersonmobile", new StringBody(mContPerPhNum, ContentType.TEXT_PLAIN));
                    builder.addPart("deviceType", new StringBody(StaticUtils.DEVICE_TYPE, ContentType.TEXT_PLAIN));
                    builder.addPart("deviceToken", new StringBody(mPref.mGetGCMId(), ContentType.TEXT_PLAIN));

                    if(!(mBusinessCardImgPath.equalsIgnoreCase("")))
                    {
                        builder.addPart("orgBusinessCard", new FileBody(new File(mBusinessCardImgPath)));
                    }

                    if(mOrgType.equalsIgnoreCase(StaticUtils.ORG_TYPENAME_OTHERS)) // if org type is others the orgtype = 0 and otherOrgType should be what user inputs
                    {

                        builder.addPart("orgtype", new StringBody("0", ContentType.TEXT_PLAIN));
                        builder.addPart("otherOrgType", new StringBody(mOrgTypeOthers, ContentType.TEXT_PLAIN));
                    }
                    else
                    {
                        builder.addPart("orgtype", new StringBody(mOrgType, ContentType.TEXT_PLAIN));
                    }

                }

                Log.e("REGISTER_REQUEST",""+StaticUtils.REGISTER+builder);


                AdSlateMultipartUrlConn connection = new AdSlateMultipartUrlConn();
                String response = connection.mEstablishConnection(url, builder);

                Log.e("REGISTER_RESPONSE",""+response);

                JSONObject responseObj = new JSONObject(response);

                JSONObject resObj = responseObj.getJSONObject("response");

                mResponseMsg = resObj.getString("msg");

                String mErrorFlag = resObj.getString("errorFlag");

                Log.e("ErrorFlag",""+mErrorFlag);

                if(mErrorFlag.equalsIgnoreCase("500")) // Mandatory fields are empty
                {
                    return "500";
                }
                else if (mErrorFlag.equalsIgnoreCase("501")) //Successful
                {
                    mUserId = resObj.getString("userId");
                    mEmailFrmRes = resObj.getString("email");

                    if(mPref.mGetUserType().equalsIgnoreCase("1"))
                    {
                        mPhoneNumber = resObj.getString("mobileNumber");
                    }
                    else
                    {
                        mPhoneNumber = resObj.getString("contactPersonMobile");
                    }

                    isMobileVerified = resObj.getString("isMobileVerified");

                    if(isMobileVerified.equalsIgnoreCase("1"))
                    {
                        mAdSlateDB.mInsertIntoUserTable(mUserId,resObj.getString("userType"),resObj.getString("profileImage") ,resObj.getString("loginType"), resObj.getString("userName"),mEmailFrmRes , resObj.getString("dob"),
                                resObj.getString("mobileNumber"), resObj.getString("gender"), resObj.getString("orgType"),resObj.getString("address") , resObj.getString("orgDesc"), resObj.getString("orgTurnover"), resObj.getString("contactPersonName"),
                                resObj.getString("contactPersonDesignation"), resObj.getString("contactPersonMobile"), resObj.getString("orgBusinessCard"),isMobileVerified, resObj.getString("referralCode"));

                        mAdSlateDB.close();
                    }


                    return "501";
                }
                else if (mErrorFlag.equalsIgnoreCase("502"))  //Information provided is incorrect.
                {
                    return "502";
                }

                else if (mErrorFlag.equalsIgnoreCase("503")) //User registered but mobile number is not verified.
                {
                    mEmailFrmRes = resObj.getString("email");
                    mUserId = resObj.getString("userId");

                    if(mPref.mGetUserType().equalsIgnoreCase("1"))
                    {
                        mPhoneNumber = resObj.getString("mobileNumber");
                    }
                    else
                    {
                        mPhoneNumber = resObj.getString("contactPersonMobile");
                    }
                    return "503";
                }
                else if (mErrorFlag.equalsIgnoreCase("504")) //Email already exists
                {
                    return "504";
                }
                else if (mErrorFlag.equalsIgnoreCase("505")) // Some error occurred while storing in the database.
                {
                    return "505";
                }
                else if (mErrorFlag.equalsIgnoreCase("506")) //Registered successfully using social media and mobile number not entered.
                {
                    mEmailFrmRes = resObj.getString("email");
                    mUserId = resObj.getString("userId");
                    if(mPref.mGetUserType().equalsIgnoreCase("1"))
                    {
                        mPhoneNumber = resObj.getString("mobileNumber");
                    }
                    else
                    {
                        mPhoneNumber = resObj.getString("contactPersonMobile");
                    }
                    return "506";
                }
                else if(mErrorFlag.equalsIgnoreCase("510"))  //Image is not uploaded
                {
                    return "510";
                }
                else if(mErrorFlag.equalsIgnoreCase("511")) //Profile Image is empty
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
            if(result.equalsIgnoreCase("500")) // Mandatory fields are empty
            {
                mShowDialog(Registration2.this,"",mResponseMsg,true,false);
            }
            else if(result.equalsIgnoreCase("501")) //Successful
            {
                if(isMobileVerified.equalsIgnoreCase("0"))
                {
                    Intent intent = new Intent(Registration2.this, OTPScreen.class);
                    intent.putExtra("mEmail",mEmailFrmRes);
                    intent.putExtra("mPhoneNumber",mPhoneNumber);
                    intent.putExtra("mUserId",mUserId);
                    intent.putExtra("mUserType", mPref.mGetUserType());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }
                else
                {
                    mPref.mSaveLoginStatus(true);
                    Intent intent = new Intent(Registration2.this, Home.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

            }
            else if (result.equalsIgnoreCase("502")) //Information provided is incorrect.
            {
                mShowDialog(Registration2.this,"",mResponseMsg,true,false);
            }
            else if (result.equalsIgnoreCase("503")) //User registered but mobile number is not verified.
            {
                Intent intent = new Intent(Registration2.this, OTPScreen.class);
                intent.putExtra("mEmail",mEmailFrmRes);
                intent.putExtra("mPhoneNumber",mPhoneNumber);
                intent.putExtra("mUserId",mUserId);
                intent.putExtra("mUserType", mPref.mGetUserType());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
            else if(result.equalsIgnoreCase("504")) //Not Success (Email Id already exists)
            {
                mShowDialog(Registration2.this,"",mResponseMsg,true,false);
            }
            else if(result.equalsIgnoreCase("505")) //Error while storing to database
            {
                mShowDialog(Registration2.this, "", mResponseMsg, true, false);
            }
            else if(result.equalsIgnoreCase("506")) //Registered successfully with Social Media and mobile number is not entered
            {
                Intent intent = new Intent(Registration2.this, OTPScreen.class);
                intent.putExtra("mPhoneNumber",mPhoneNumber);
                intent.putExtra("mEmail",mEmailFrmRes);
                intent.putExtra("mUserId",mUserId);
                intent.putExtra("mUserType", mPref.mGetUserType());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
            else if(result.equalsIgnoreCase("510")) //Image is not uploaded
            {
                mShowDialog(Registration2.this,"",mResponseMsg,true,false);
            }
            else if(result.equalsIgnoreCase("511")) //Profile Image is empty
            {
                mShowDialog(Registration2.this,"",mResponseMsg,true,false);
            }
            else
            {
                mShowDialog(Registration2.this,"",StaticUtils.ERROR_OCCURRED,true,false);
            }
        }
    }

    private class getOrgTypeWebservice extends AsyncTask<String,Void,String>
    {

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

                AdSlateDB mAdSlateDB = AdSlateDB.GET_INSTANCE(Registration2.this);

                JSONObject jsonObj = new JSONObject();
                jsonObj.put("lastSyncTime",mPref.mGetOrgTypeLastSync());

                ContentValues mcv = new ContentValues();
                mcv.put("json",String.valueOf(jsonObj));


                Log.e("ORG_TYPE_REQUEST",""+StaticUtils.ORG_TYPE+mcv.toString());

                AdSlateUrlConnection connection = new AdSlateUrlConnection();
                String response =  connection.mEstablishConnectionJSON(StaticUtils.ORG_TYPE,mcv.toString());

                Log.e("ORG_TYPE_RESPONSE",""+response);

                JSONObject responseObj = new JSONObject(response); //Converting String to json Object

                JSONObject resObj = responseObj.getJSONObject("response"); //getting the Json Obj "response" obj from webservice response

                String mErrorFlag = resObj.getString("errorFlag");

                if(mErrorFlag.equalsIgnoreCase("501"))
                {
                    JSONArray orgType = resObj.getJSONArray("OrganizationType");
                    mOrgTypeArr = new ArrayList<OrganizationType>();

                    for(int i = 0 ; i < orgType.length();i++)
                    {
                        JSONObject orgTypeArrObj = orgType.getJSONObject(i);
                        mOrgTypeArr.add(new OrganizationType(orgTypeArrObj.getString("Id"), orgTypeArrObj.getString("Name")));
                    }

                    mAdSlateDB.mInsertOrganizationType(mOrgTypeArr);

                    mAdSlateDB.close();
                    mPref.mSaveOrgTypeLastSync(resObj.getString("lastSyncTime"));
                    return "501";
                }
                else
                {
                    mAdSlateDB.close();
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

            if(result.equalsIgnoreCase("501"))
            {
                Toast.makeText(Registration2.this, "Organization Type updated", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(Registration2.this, "Could not update Organization type", Toast.LENGTH_LONG).show();
            }
        }
    }
}
