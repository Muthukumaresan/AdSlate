package com.adslate.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.adslate.R;
import com.adslate.baseclasses.BaseActivity;
import com.adslate.helperclasses.GalleryHelper;
import com.adslate.helperclasses.RangeSeekBar;
import com.adslate.models.SpaceType;
import com.adslate.storage.AdSlateDB;
import com.adslate.utils.AdSlateUrlConnection;
import com.adslate.utils.StaticUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;


public class HostSpace extends BaseActivity implements View.OnClickListener
{



    private ImageView mIvBackButton,mIvSpcImg1,mIvSpcImg2,mIvSpcImg3,mIvSpcImg4,mIvSpcImg5,mIvBrandlogo,mIvSignImg;

    private Spinner mSpHostedBy,mSpSpaceType,mSpWidthWall,mSpHeightWall,mSpAreaWall,mSpVehicleType,mSpDoor1,mSpDoor2, mSpWidthVehi,mSpHeightVehi,mSpAreaVehi,mSpSigns,mSpDimenGate, mSpFleetCity,
            mSpSideDoors,mSpHireTime,mSpPeriod,mSpRadioCities,mSpRadioStatns,mSpWidthCampus,mSpHeightCampus,mSpAreaCampus,mSpPayMode,mSpEntEvntType,mSpWidthEntEvents,mSpHeightEntEvents,
            mSpAreaEntEvents,mSpEntEvntPayMode,mSpIndoorSpaceType,mSpNamingRightsPayMode,mSpHrMin,mSpHrMin1;

    private TextView mTvHostedBy,mTvHostSpace;

    private LinearLayout mLinLayImgLay2,mLinLayWall,mLinLayVehicle,mLinLayPersonal,mLinLayPerEvents,mLinLayGate,mLinLayhostEnt,mLinLayFleetVehi,mLinLayUniforms,mLinLayRadio,mLinLayCampus,
            mLinLayEntEvents,mLinLaybillboards,mLinLayIndoorSpc,mLinLayNamingRights,mLinLayImages;

    private ArrayList<SpaceType> mSpaceTypeArr;

    private ArrayList<String> hostedByValues,spaceNames,dimenWidth,vehicleType,oneDoor,bothSides,signsPerGate,getDimens,entSpcVehiCities,entSpcVehiDoors,monthlyYearly,radioCities,radioStations,
            entEventType,indoorSpaceType,minMaxHours;

    private String mUserType;

    private int REQUEST_CAMERA = 100;
    private int SELECT_FILE = 200;
    private int imageNo = 0;

    DisplayImageOptions mOptions;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_space);

        getIntentValues();

        callSpaceTypeWebService();

        mInitialiseView();

        // Setup the new range seek bar
        RangeSeekBar<Integer> rangeSeekBar = new RangeSeekBar<Integer>(this);
        // Set the range
        rangeSeekBar.setRangeValues(0, 100);
        rangeSeekBar.setSelectedMinValue(20);
        rangeSeekBar.setSelectedMaxValue(88);

        // Add to layout
        LinearLayout layout = (LinearLayout) findViewById(R.id.seekbar_placeholder);
        layout.addView(rangeSeekBar);


        mOptions =  new DisplayImageOptions.Builder().considerExifParams(true).build();

        hostedByValues = new ArrayList<String>();
        hostedByValues.add("Individual");
        hostedByValues.add("Enterprise");

        dimenWidth = new ArrayList<String>();
        dimenWidth.add("Feet");
        dimenWidth.add("Sq ft");

        monthlyYearly = new ArrayList<String>();
        monthlyYearly.add("Daily ");
        monthlyYearly.add("Weekly");
        monthlyYearly.add("Monthly");

        final ArrayAdapter<String> monthlyYearlyAdapter = new ArrayAdapter<String>(HostSpace.this, R.layout.spinner_value,monthlyYearly);
        mSpHireTime.setAdapter(monthlyYearlyAdapter);
        mSpPeriod.setAdapter(monthlyYearlyAdapter);


        final ArrayAdapter<String> dimenWidthAdapter = new ArrayAdapter<String>(HostSpace.this, R.layout.spinner_value,dimenWidth);


        ArrayAdapter<String> hostedByAdapter = new ArrayAdapter<String>(this, R.layout.spinner_value, hostedByValues);
        mSpHostedBy.setAdapter(hostedByAdapter);

        mSpHostedBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("NAME", "" + mSpHostedBy.getSelectedItem().toString());

                if (mSpHostedBy.getSelectedItem().toString().equalsIgnoreCase("Individual")) {
                    ArrayAdapter<String> spaceTypeAdapter = new ArrayAdapter<String>(HostSpace.this, R.layout.spinner_value, getSpaceTypeFrmDb("1"));
                    mSpSpaceType.setAdapter(spaceTypeAdapter);
                } else if (mSpHostedBy.getSelectedItem().toString().equalsIgnoreCase("Enterprise")) {
                    ArrayAdapter<String> spaceTypeAdapter = new ArrayAdapter<String>(HostSpace.this, R.layout.spinner_value, getSpaceTypeFrmDb("2"));
                    mSpSpaceType.setAdapter(spaceTypeAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpSpaceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if (mSpSpaceType.getSelectedItem().toString().equalsIgnoreCase("Wall"))
                {
                    mLinLayWall.setVisibility(View.VISIBLE);
                    mLinLayImages.setVisibility(View.VISIBLE);

                    mSpWidthWall.setAdapter(dimenWidthAdapter);
                    mSpHeightWall.setAdapter(dimenWidthAdapter);
                    mSpAreaWall.setAdapter(dimenWidthAdapter);
                }
                else
                {
                    mLinLayWall.setVisibility(View.GONE);
                }

                if (mSpSpaceType.getSelectedItem().toString().equalsIgnoreCase("Vehicle"))
                {
                    mLinLayVehicle.setVisibility(View.VISIBLE);
                    mLinLayhostEnt.setVisibility(View.GONE);
                    mLinLayImages.setVisibility(View.VISIBLE);

                    vehicleType = new ArrayList<String>();
                    vehicleType.add("Auto rickshaw");
                    vehicleType.add("Bike");
                    vehicleType.add("Car");

                    oneDoor = new ArrayList<String>();
                    oneDoor.add("1 door");
                    oneDoor.add("2 door");

                    bothSides = new ArrayList<String>();
                    bothSides.add("2 doors");
                    bothSides.add("4 doors");

                    ArrayAdapter<String> vehicleTypeAdapter = new ArrayAdapter<String>(HostSpace.this, R.layout.spinner_value,vehicleType);
                    mSpVehicleType.setAdapter(vehicleTypeAdapter);

                    ArrayAdapter<String> oneDoorAdapter = new ArrayAdapter<String>(HostSpace.this, R.layout.spinner_value,oneDoor);
                    mSpDoor1.setAdapter(oneDoorAdapter);

                    ArrayAdapter<String> bothDoorAdapter = new ArrayAdapter<String>(HostSpace.this, R.layout.spinner_value,bothSides);
                    mSpDoor2.setAdapter(bothDoorAdapter);

                    mSpWidthVehi.setAdapter(dimenWidthAdapter);
                    mSpHeightVehi.setAdapter(dimenWidthAdapter);
                    mSpAreaVehi.setAdapter(dimenWidthAdapter);

                }
                else
                {
                    mLinLayVehicle.setVisibility(View.GONE);
                    mLinLayhostEnt.setVisibility(View.GONE);
                }
                if (mSpSpaceType.getSelectedItem().toString().equalsIgnoreCase("People/Personal"))
                {
                    mLinLayPersonal.setVisibility(View.VISIBLE);
                    mLinLayhostEnt.setVisibility(View.GONE);
                    mLinLayImages.setVisibility(View.GONE);

                }
                else
                {
                    mLinLayPersonal.setVisibility(View.GONE);
                    mLinLayhostEnt.setVisibility(View.GONE);

                }
                if (mSpSpaceType.getSelectedItem().toString().equalsIgnoreCase("Personal Events"))
                {
                    mLinLayPerEvents.setVisibility(View.VISIBLE);
                    mLinLayhostEnt.setVisibility(View.GONE);
                    mLinLayImages.setVisibility(View.GONE);

                }
                else
                {
                    mLinLayPerEvents.setVisibility(View.GONE);
                    mLinLayhostEnt.setVisibility(View.GONE);

                }
                 if (mSpSpaceType.getSelectedItem().toString().equalsIgnoreCase("Gate"))
                {
                    mLinLayGate.setVisibility(View.VISIBLE);
                    mLinLayhostEnt.setVisibility(View.GONE);
                    mLinLayImages.setVisibility(View.VISIBLE);


                    signsPerGate = new ArrayList<String>();
                    signsPerGate.add("1");
                    signsPerGate.add("2");

                    ArrayAdapter<String> signsAdapter = new ArrayAdapter<String>(HostSpace.this, R.layout.spinner_value,signsPerGate);
                    mSpSigns.setAdapter(signsAdapter);

                    getDimens = new ArrayList<String>();
                    getDimens.add("m");
                    getDimens.add("cm");

                    ArrayAdapter<String> gateDimenAdapter = new ArrayAdapter<String>(HostSpace.this, R.layout.spinner_value,getDimens);

                    mSpDimenGate.setAdapter(gateDimenAdapter);
                }
                 else
                 {
                     mLinLayGate.setVisibility(View.GONE);
                     mLinLayhostEnt.setVisibility(View.GONE);
                 }
                 if (mSpSpaceType.getSelectedItem().toString().equalsIgnoreCase("Vehicles"))
                {
                    mLinLayhostEnt.setVisibility(View.VISIBLE);
                    mLinLayFleetVehi.setVisibility(View.VISIBLE);
                    mLinLayImages.setVisibility(View.VISIBLE);

                    entSpcVehiCities = new ArrayList<String>();
                    entSpcVehiCities.add("Banglore");
                    entSpcVehiCities.add("Manglore");
                    entSpcVehiCities.add("Hubli");


                    ArrayAdapter<String> entSpcVehiCitiesAdapter = new ArrayAdapter<String>(HostSpace.this, R.layout.spinner_value,entSpcVehiCities);
                    mSpFleetCity.setAdapter(entSpcVehiCitiesAdapter);


                    entSpcVehiDoors = new ArrayList<String>();
                    entSpcVehiDoors.add("1");
                    entSpcVehiDoors.add("2");
                    entSpcVehiDoors.add("3");
                    entSpcVehiDoors.add("4");

                    ArrayAdapter<String> entSpcVehiDoorsAdapter = new ArrayAdapter<String>(HostSpace.this, R.layout.spinner_value,entSpcVehiDoors);
                    mSpSideDoors.setAdapter(entSpcVehiDoorsAdapter);

                }
                 else
                 {
                     mLinLayFleetVehi.setVisibility(View.GONE);
                 }
                 if (mSpSpaceType.getSelectedItem().toString().equalsIgnoreCase("School/Work Uniforms"))
                {
                    mLinLayUniforms.setVisibility(View.VISIBLE);
                    mLinLayImages.setVisibility(View.VISIBLE);

                    mIvBrandlogo.setOnClickListener(HostSpace.this);
                    mIvSignImg.setOnClickListener(HostSpace.this);
                }
                 else
                 {

                     mLinLayUniforms.setVisibility(View.GONE);
                 }
                 if (mSpSpaceType.getSelectedItem().toString().equalsIgnoreCase("Radio"))
                {

                    mLinLayRadio.setVisibility(View.VISIBLE);
                    mLinLayImages.setVisibility(View.VISIBLE);

                    radioCities = new ArrayList<String>();
                    radioCities.add("Banglore");
                    radioCities.add("Shimoga");
                    radioCities.add("Dharwad");

                    radioStations = new ArrayList<String>();
                    radioStations.add("93.5");
                    radioStations.add("98.4");
                    radioStations.add("101.1");

                    ArrayAdapter<String> radioCitiesAdapter = new ArrayAdapter<String>(HostSpace.this, R.layout.spinner_value,radioCities);
                    mSpRadioCities.setAdapter(radioCitiesAdapter);

                    ArrayAdapter<String> radioStationsAdapter = new ArrayAdapter<String>(HostSpace.this, R.layout.spinner_value,radioStations);
                    mSpRadioStatns.setAdapter(radioStationsAdapter);


                }
                 else
                 {

                     mLinLayRadio.setVisibility(View.GONE);
                 }

                 if (mSpSpaceType.getSelectedItem().toString().equalsIgnoreCase("Corporate/School Campus"))
                {
                    mLinLayCampus.setVisibility(View.VISIBLE);
                    mLinLayImages.setVisibility(View.VISIBLE);

                    mSpAreaCampus.setAdapter(dimenWidthAdapter);
                    mSpWidthCampus.setAdapter(dimenWidthAdapter);
                    mSpHeightCampus.setAdapter(dimenWidthAdapter);

                    mSpPayMode.setAdapter(monthlyYearlyAdapter);
                }
                 else
                 {
                     mLinLayCampus.setVisibility(View.GONE);
                 }
                 if (mSpSpaceType.getSelectedItem().toString().equalsIgnoreCase("Corporate/School Events"))
                {
                    mLinLayEntEvents.setVisibility(View.VISIBLE);
                    mLinLayImages.setVisibility(View.VISIBLE);

                    entEventType = new ArrayList<String>();
                    entEventType.add("Get togethers");
                    entEventType.add("Retreats");
                    entEventType.add("Conventions");
                    entEventType.add("Sports events");
                    entEventType.add("Annual days");

                    ArrayAdapter<String> entEventAdapter = new ArrayAdapter<String>(HostSpace.this, R.layout.spinner_value,entEventType);
                    mSpEntEvntType.setAdapter(entEventAdapter);

                    mSpWidthEntEvents.setAdapter(dimenWidthAdapter);
                    mSpHeightEntEvents.setAdapter(dimenWidthAdapter);
                    mSpAreaEntEvents.setAdapter(dimenWidthAdapter);
                    mSpEntEvntPayMode.setAdapter(monthlyYearlyAdapter);


                }
                 else
                 {
                     mLinLayEntEvents.setVisibility(View.GONE);
                 }
                if (mSpSpaceType.getSelectedItem().toString().equalsIgnoreCase("Billboards"))
                {
                    mLinLaybillboards.setVisibility(View.VISIBLE);
                    mLinLayImages.setVisibility(View.VISIBLE);
                }
                else
                {
                    mLinLaybillboards.setVisibility(View.GONE);
                }
                 if (mSpSpaceType.getSelectedItem().toString().equalsIgnoreCase("Indoor Space"))
                {
                    mLinLayIndoorSpc.setVisibility(View.VISIBLE);
                    mLinLayImages.setVisibility(View.VISIBLE);

                    indoorSpaceType = new ArrayList<String>();
                    indoorSpaceType.add("Logo art");
                    indoorSpaceType.add("Posters");
                    indoorSpaceType.add("Tables");

                    ArrayAdapter<String> indoorSpaceAdapter = new ArrayAdapter<String>(HostSpace.this, R.layout.spinner_value,indoorSpaceType);
                    mSpIndoorSpaceType.setAdapter(indoorSpaceAdapter);



                }
                 else
                 {
                     mLinLayIndoorSpc.setVisibility(View.GONE);

                 }
                 if (mSpSpaceType.getSelectedItem().toString().equalsIgnoreCase("Naming Rights"))
                {
                    mLinLayNamingRights.setVisibility(View.VISIBLE);
                    mLinLayImages.setVisibility(View.VISIBLE);

                    mSpNamingRightsPayMode.setAdapter(monthlyYearlyAdapter);

                    minMaxHours = new ArrayList<String>();
                    minMaxHours.add("hr");
                    minMaxHours.add("min");

                    ArrayAdapter<String> minMaxHrAdapter = new ArrayAdapter<String>(HostSpace.this, R.layout.spinner_value,minMaxHours);
                    mSpHrMin.setAdapter(minMaxHrAdapter);
                    mSpHrMin1.setAdapter(minMaxHrAdapter);


                }
                else
                 {
                     mLinLayNamingRights.setVisibility(View.GONE);
                 }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    private ArrayList<String> getSpaceTypeFrmDb(String userType)
    {

        AdSlateDB mAdSlateDB = AdSlateDB.GET_INSTANCE(this);

        Cursor cursor = mAdSlateDB.mSelectSpaceNames(userType);
        Log.e("COUNTYY", "" + cursor.getCount());

        spaceNames = new ArrayList<String>();

        if(cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            do {
                spaceNames.add(cursor.getString(cursor.getColumnIndex("_name")));

            }while(cursor.moveToNext());

        }
        cursor.close();
        mAdSlateDB.close();

        return spaceNames;

    }

    private void mInitialiseView()
    {
        mIvBackButton = (ImageView)findViewById(R.id.xIvBackButton);
        mLinLayImages = (LinearLayout)findViewById(R.id.xLinLayImages);
        mIvSpcImg1 = (ImageView)findViewById(R.id.xIvSpcImg1);
        mIvSpcImg2 = (ImageView)findViewById(R.id.xIvSpcImg2);
        mIvSpcImg3 = (ImageView)findViewById(R.id.xIvSpcImg3);
        mIvSpcImg4 = (ImageView)findViewById(R.id.xIvSpcImg4);
        mIvSpcImg5 = (ImageView)findViewById(R.id.xIvSpcImg5);
        mIvBrandlogo = (ImageView)findViewById(R.id.xIvBrandlogo);
        mIvSignImg = (ImageView)findViewById(R.id.xIvSignImg);

        mTvHostSpace = (TextView)findViewById(R.id.xTvHostSpace);

        mSpHostedBy = (Spinner)findViewById(R.id.xSpHostedBy);
        mSpSpaceType = (Spinner)findViewById(R.id.xSpSpaceType);


        mLinLayImgLay2 = (LinearLayout)findViewById(R.id.xLinLayImgLay2);
        mLinLayWall = (LinearLayout)findViewById(R.id.xLinLayWall);
        mLinLayVehicle = (LinearLayout)findViewById(R.id.xLinLayVehicle);
        mLinLayPersonal = (LinearLayout)findViewById(R.id.xLinLayPersonal);
        mLinLayPerEvents = (LinearLayout)findViewById(R.id.xLinLayPerEvents);
        mLinLayGate = (LinearLayout)findViewById(R.id.xLinLayGate);

        mLinLayhostEnt = (LinearLayout)findViewById(R.id.xLinLayhostEnt);
        mLinLayFleetVehi = (LinearLayout)findViewById(R.id.xLinLayFleetVehi);
        mLinLayUniforms = (LinearLayout)findViewById(R.id.xLinLayUniforms);
        mLinLayRadio = (LinearLayout)findViewById(R.id.xLinLayRadio);
        mLinLayCampus = (LinearLayout)findViewById(R.id.xLinLayCampus);
        mLinLayEntEvents = (LinearLayout)findViewById(R.id.xLinLayEntEvents);
        mLinLaybillboards = (LinearLayout)findViewById(R.id.xLinLaybillboards);
        mLinLayIndoorSpc = (LinearLayout)findViewById(R.id.xLinLayIndoorSpc);
        mLinLayNamingRights = (LinearLayout)findViewById(R.id.xLinLayNamingRights);

        mSpWidthWall = (Spinner)findViewById(R.id.xSpWidthWall);
        mSpHeightWall = (Spinner)findViewById(R.id.xSpHeightWall);
        mSpAreaWall = (Spinner)findViewById(R.id.xSpAreaWall);

        mSpVehicleType = (Spinner)findViewById(R.id.xSpVehicleType);
        mSpDoor1 = (Spinner)findViewById(R.id.xSpDoor1);
        mSpDoor2 = (Spinner)findViewById(R.id.xSpDoor2);
        mSpWidthVehi = (Spinner)findViewById(R.id.xSpWidthVehi);
        mSpHeightVehi = (Spinner)findViewById(R.id.xSpHeightVehi);
        mSpAreaVehi = (Spinner)findViewById(R.id.xSpAreaVehi);

        mSpSigns = (Spinner)findViewById(R.id.xSpSigns);
        mSpDimenGate = (Spinner)findViewById(R.id.xSpDimenGate);
        mSpSideDoors = (Spinner)findViewById(R.id.xSpSideDoors);

        mSpHireTime = (Spinner)findViewById(R.id.xSpHireTime);
        mSpPeriod = (Spinner)findViewById(R.id.xSpPeriod);
        mSpFleetCity = (Spinner)findViewById(R.id.xSpFleetCity);
        mSpRadioCities = (Spinner)findViewById(R.id.xSpRadioCities);
        mSpRadioStatns = (Spinner)findViewById(R.id.xSpRadioStatns);

        mSpWidthCampus = (Spinner)findViewById(R.id.xSpWidthCampus);
        mSpHeightCampus = (Spinner)findViewById(R.id.xSpHeightCampus);
        mSpAreaCampus = (Spinner)findViewById(R.id.xSpAreaCampus);
        mSpPayMode = (Spinner)findViewById(R.id.xSpPayMode);

        mSpEntEvntType = (Spinner)findViewById(R.id.xSpEntEvntType);
        mSpWidthEntEvents = (Spinner)findViewById(R.id.xSpWidthEntEvents);
        mSpHeightEntEvents = (Spinner)findViewById(R.id.xSpHeightEntEvents);
        mSpAreaEntEvents = (Spinner)findViewById(R.id.xSpAreaEntEvents);
        mSpEntEvntPayMode = (Spinner)findViewById(R.id.xSpEntEvntPayMode);

        mSpIndoorSpaceType = (Spinner)findViewById(R.id.xSpIndoorSpaceType);

        mSpNamingRightsPayMode = (Spinner)findViewById(R.id.xSpNamingRightsPayMode);
        mSpHrMin = (Spinner)findViewById(R.id.xSpHrMin);
        mSpHrMin1 = (Spinner)findViewById(R.id.xSpHrMin1);

        mTvHostedBy = (TextView)findViewById(R.id.xTvHostedBy);

        mIvBackButton.setOnClickListener(this);
        mTvHostSpace.setOnClickListener(this);
        mIvSpcImg1.setOnClickListener(this);

    }

    private void callSpaceTypeWebService()
    {

        if(mNetworkStatus.isNetworkAvailable())
        {
            new getSpaceTypeWebservice().execute();
        }
        else
        {
            mShowDialog(HostSpace.this,StaticUtils.NO_INTERNET_TITLE,StaticUtils.NO_INTERNET_MESSAGE,true,false);
        }
    }

    private void getIntentValues()
    {
        Intent intent = getIntent();
        mUserType = intent.getStringExtra("userType");
    }

    @Override
    public void onClick(View view)
    {
        if(view == mIvBackButton)
        {
            finish();
        }
        else if(view == mTvHostSpace)
        {
            Toast.makeText(HostSpace.this,"Space hosted",Toast.LENGTH_LONG).show();
        }
        else if(view == mIvSpcImg1)
        {
            imageNo = 1;
            selectImage();
        }
        else if(view == mIvSpcImg2)
        {
            imageNo = 2;
            selectImage();
        }
        else if(view == mIvSpcImg3)
        {
            imageNo = 3;
            selectImage();
        }
        else if(view == mIvSpcImg4)
        {
            imageNo = 4;
            selectImage();
        }
        else if(view == mIvSpcImg5)
        {
            imageNo = 5;
            selectImage();
        }
        else if(view == mIvBrandlogo)
        {
            imageNo = 6;
            selectImage();
        }
        else if(view == mIvSignImg)
        {
            imageNo = 7;
            selectImage();
        }

    }

    private void selectImage()
    {
        final CharSequence[] items = { "Take Photo", "Choose from Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(HostSpace.this);
        builder.setTitle("Add Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (items[i].equals("Take Photo")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);

                } else if (items[i].equals("Choose from Gallery")) {

                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);

                }

            }

        });
        builder.show();

    }


    private class getSpaceTypeWebservice extends AsyncTask<String,Void,String>
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

                AdSlateDB mAdSlateDB = AdSlateDB.GET_INSTANCE(HostSpace.this);

                JSONObject jsonObj = new JSONObject();
                jsonObj.put("lastSyncTime",mPref.mGetSpaceTypeLstSyn());

                ContentValues mcv = new ContentValues();
                mcv.put("json",String.valueOf(jsonObj));

                Log.e("SPACE_TYPE_REQUEST",""+StaticUtils.SPACE_TYPE+mcv.toString());

                AdSlateUrlConnection connection = new AdSlateUrlConnection();
                String response = connection.mEstablishConnectionJSON(StaticUtils.SPACE_TYPE,mcv.toString());

                Log.e("SPACE_TYPE_RESPONSE",""+response);

                JSONObject responseObj = new JSONObject(response);

                JSONObject resObj = responseObj.getJSONObject("response");

                String mErrorFlag = resObj.getString("errorFlag");

                if(mErrorFlag.equalsIgnoreCase("501"))
                {
                    JSONArray indSpaceType = resObj.getJSONArray("IndividualSpaceType");
                    JSONArray entSpaceType = resObj.getJSONArray("EnterpriseSpaceType");

                    mSpaceTypeArr = new ArrayList<SpaceType>();

                    for(int i = 0 ; i < indSpaceType.length(); i++)
                    {
                        JSONObject indSpaceArrObj = indSpaceType.getJSONObject(i);
                        mSpaceTypeArr.add(new SpaceType(indSpaceArrObj.getString("id"),indSpaceArrObj.getString("Name"),"1"));
                    }
                    mAdSlateDB.mInsertSpaceType(mSpaceTypeArr);

                    for(int i = 0 ; i < entSpaceType.length();i++)
                    {
                        JSONObject entSpaceArrObj = entSpaceType.getJSONObject(i);
                        mSpaceTypeArr.add(new SpaceType(entSpaceArrObj.getString("id"),entSpaceArrObj.getString("Name"),"2"));
                    }
                    mAdSlateDB.mInsertSpaceType(mSpaceTypeArr);



                    Cursor cursor = mAdSlateDB.mSelectAllData(mAdSlateDB.mSpaceType);

                    Log.e("CHECK_VALUES",""+cursor.getCount());


                    mPref.mSaveSpaceTypeLstSync(resObj.getString("lastSyncTime"));
                    mAdSlateDB.close();
                    return "501";
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
           if(result.equalsIgnoreCase("501"))
           {
               if (mSpHostedBy.getSelectedItem().toString().equalsIgnoreCase("Individual"))
               {
                   ArrayAdapter<String> spaceTypeAdapter = new ArrayAdapter<String>(HostSpace.this, R.layout.spinner_value, getSpaceTypeFrmDb("1"));
                   mSpSpaceType.setAdapter(spaceTypeAdapter);
               }
               else if (mSpHostedBy.getSelectedItem().toString().equalsIgnoreCase("Enterprise"))
               {
                   ArrayAdapter<String> spaceTypeAdapter = new ArrayAdapter<String>(HostSpace.this, R.layout.spinner_value, getSpaceTypeFrmDb("2"));
                   mSpSpaceType.setAdapter(spaceTypeAdapter);
               }
           }
            else
           {
               mShowDialog(HostSpace.this,"",StaticUtils.ERROR_OCCURRED,true,false);
           }
        }

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




                if(imageNo ==1)
                {
                    ImageLoader.getInstance().displayImage(selectedImage.toString(), mIvSpcImg1, mOptions);

                    mIvSpcImg2.setVisibility(View.VISIBLE);

                    mIvSpcImg2.setOnClickListener(this);
                }
                else if(imageNo == 2)
                {
                    ImageLoader.getInstance().displayImage(selectedImage.toString(), mIvSpcImg2, mOptions);

                    mIvSpcImg3.setVisibility(View.VISIBLE);

                    mIvSpcImg3.setOnClickListener(this);
                }
                else if(imageNo == 3)
                {
                    ImageLoader.getInstance().displayImage(selectedImage.toString(), mIvSpcImg3, mOptions);

                    mLinLayImgLay2.setVisibility(View.VISIBLE);

                    mIvSpcImg4.setVisibility(View.VISIBLE);
                    mIvSpcImg4.setOnClickListener(this);

                }
                else if(imageNo == 4)
                {
                    ImageLoader.getInstance().displayImage(selectedImage.toString(), mIvSpcImg4, mOptions);

                    mIvSpcImg5.setVisibility(View.VISIBLE);
                    mIvSpcImg5.setOnClickListener(this);
                }
                else if(imageNo == 5)
                {
                    ImageLoader.getInstance().displayImage(selectedImage.toString(), mIvSpcImg5, mOptions);
                }
                else if(imageNo == 6)
                {
                    ImageLoader.getInstance().displayImage(selectedImage.toString(), mIvBrandlogo, mOptions);
                }
                else if(imageNo == 7)
                {
                    ImageLoader.getInstance().displayImage(selectedImage.toString(), mIvSignImg, mOptions);
                }
                else
                {

                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

}
