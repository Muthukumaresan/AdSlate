package com.adslate.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.adslate.models.OrganizationType;
import com.adslate.models.SpaceType;


import java.util.ArrayList;

/**
 * Created by pooja.b on 27-11-2015.
 */
public class AdSlateDB extends SQLiteOpenHelper
{

    private static AdSlateDB mAdSlate = null;
    private static String DB_NAME = "AdSlate.db";
    private static int DB_VERSION = 1;

    public String mUserTable = "_tbl_user";
    public String mOrganizationType = "_tbl_organization_type";
    public String mSpaceType = "_tbl_space_type";


    private final String mCreateUserTable = "CREATE TABLE "+mUserTable+"(_id INTEGER,_user_type TEXT,_profile_image TEXT ,_login_type TEXT, _user_name TEXT, _email TEXT,_dob TEXT,_ph_num TEXT,_org_typ TEXT,_address TEXT,_org_desc TEXT,_org_turn_ovr TEXT," +
            "_cont_person_name TEXT,_cont_person_desg TEXT,_cont_person_mobile TEXT,_org_business_card TEXT, _is_mobile_verified TEXT,_referral_code TEXT,other_org_type TEXT,PRIMARY KEY(_id))";

    private final String mCreateOrganizationType = "CREATE TABLE "+mOrganizationType+"(_id INTEGER, _name TEXT, PRIMARY KEY(_id)) ";

    private final String mCreateSpaceType = "CREATE TABLE "+mSpaceType+"(_id INTEGER, _name TEXT,_userType TEXT ,PRIMARY KEY(_id))";

    public AdSlateDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }


    public static AdSlateDB GET_INSTANCE(Context context)
    {
        if(mAdSlate == null)
        {
            mAdSlate = new AdSlateDB(context,DB_NAME,null,DB_VERSION);
            return mAdSlate;
        }
        else
        {
            return mAdSlate;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(mCreateUserTable);
        db.execSQL(mCreateOrganizationType);
        db.execSQL(mCreateSpaceType);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {

    }


    public void mInsertSpaceType(String stId, String name, String userType)
    {





    }

    public void mInsertSpaceType(ArrayList<SpaceType> mSpaceTypeArr)
    {
        ContentValues mCv = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        for(SpaceType entry : mSpaceTypeArr)
        {
            mCv.put("_id", entry.getmId());
            mCv.put("_name", entry.getmName());
            mCv.put("_userType", entry.getmUserType());
            try
            {
                db.insertOrThrow(mSpaceType, null, mCv);
            }
            catch(SQLException e)
            {

            }
        }

        db.setTransactionSuccessful();
        db.endTransaction();

    }

    public void mInsertOrganizationType(ArrayList<OrganizationType> mOrgTypeArr)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues mCv = new ContentValues();
        for (OrganizationType entry:mOrgTypeArr)
        {
            mCv.put("_id", entry.getmId());
            mCv.put("_name", entry.getmName());
            try {
                db.insertOrThrow(mOrganizationType, null, mCv);
            }
            catch(SQLException e)
            {

            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }


    public void mInsertIntoUserTable(String id,String userType,String profileImage,String loginType,String userName,String email,String dob,String phNumber,String orgType,String address,String orgDesc,String orgTurnOvr,String contPersonName,String contPersonDesg,String contPersonPhNum,String orgBusinessCard,String isMobileVerified,String referralCode,String otherOrgType)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues mCv = new ContentValues();
        mCv.put("_id",id);
        mCv.put("_user_type",userType);
        mCv.put("_profile_image",profileImage);
        mCv.put("_login_type",loginType);
        mCv.put("_user_name",userName);
        mCv.put("_email",email);
        mCv.put("_dob",dob);
        mCv.put("_ph_num",phNumber);
        mCv.put("_org_typ",orgType);
        mCv.put("_address",address);
        mCv.put("_org_desc",orgDesc);
        mCv.put("_org_turn_ovr",orgTurnOvr);
        mCv.put("_cont_person_name",contPersonName);
        mCv.put("_cont_person_desg",contPersonDesg);
        mCv.put("_cont_person_mobile",contPersonPhNum);
        mCv.put("_org_business_card",orgBusinessCard);
        mCv.put("_is_mobile_verified",isMobileVerified);
        mCv.put("_referral_code", referralCode);
        mCv.put("other_org_type", otherOrgType);


        try
        {
            db.insertOrThrow(mUserTable,null,mCv);
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }

    }

    public Cursor mSelectAllData(String tableName)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "SELECT *FROM "+tableName;
        return db.rawQuery(Query,null);
    }

    public void mDeleteTable(String tableName)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tableName,null,null);
        Log.e("", "deleted table: " + tableName);
    }

    public Cursor mSelectSpaceNames(String mUserType)
    {
        Log.e("Inside DB method","");
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "SELECT * FROM "+mSpaceType+" WHERE _userType = '"+mUserType+"'";
        return db.rawQuery(Query,null);
    }


}
