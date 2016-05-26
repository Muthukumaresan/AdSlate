package com.adslate.activities;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adslate.R;
import com.adslate.baseclasses.BaseFragmentActivity;
import com.adslate.fragments.menu.ContractHistoryFragment;
import com.adslate.fragments.menu.EditProfileFragment;
import com.adslate.fragments.menu.HomeFragment;
import com.adslate.fragments.menu.MyActivityFragment;
import com.adslate.fragments.menu.MyWalletFragment;
import com.adslate.fragments.menu.NotificationsFragment;
import com.adslate.fragments.menu.ReportIssueFragment;
import com.adslate.fragments.menu.RequestQuoteFragment;
import com.adslate.helperclasses.RoundedImageView;
import com.adslate.storage.AdSlateDB;
import com.adslate.utils.StaticUtils;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class Home extends BaseFragmentActivity implements View.OnClickListener {

    private ImageView mIvMenu;
    private RoundedImageView mRIvProfileImage;
    private DrawerLayout mDrawerLayout;
    private TextView mTvTitle,mTvUsername,mTvEmail;
    private LinearLayout mLinLayHome,mLinLayMyWallet,mLinLayMyActivity,mLinLayNotifications,mLinLayContractHistory,mLinLayRequestQuote,mLinLaySettings,mLinLayEditProfile,mLinLayReportIssue,mLinLayLogout,mLinLayMenuLayout;
    private RelativeLayout mRelLayExtraImages;
    private FrameLayout mFramelayoutFab;
    private FloatingActionsMenu mFabMenu;
    private com.getbase.floatingactionbutton.FloatingActionButton mFabCreateCampaign,mFabHostSpace;
    private String mUserName,mEmail,mProfileImage;
    private DisplayImageOptions mOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mOptions =  new DisplayImageOptions.Builder().considerExifParams(true).build();

        getValuesFromDB();

        mInitialiseViews();
    }

    private void getValuesFromDB()
    {


        AdSlateDB mAdSlateDB = AdSlateDB.GET_INSTANCE(Home.this);
       Cursor cursor = mAdSlateDB.mSelectAllData(mAdSlateDB.mUserTable);

        if(cursor.getCount() > 0)
        {
            Log.e("COUNT",""+cursor.getCount());
            cursor.moveToFirst();

                mUserName = cursor.getString(cursor.getColumnIndex("_user_name"));

                mEmail = cursor.getString(cursor.getColumnIndex("_email"));

                mProfileImage = cursor.getString(cursor.getColumnIndex("_profile_image"));
                Log.e("PROFILE_IMAGE1", "" + mProfileImage);

        }


    }

    private void mInitialiseViews()
    {
        mIvMenu = (ImageView)findViewById(R.id.xIvMenu);
        mRIvProfileImage = (RoundedImageView)findViewById(R.id.xRIvProfilePic);
        mTvTitle = (TextView)findViewById(R.id.xTitle);
        mTvUsername = (TextView)findViewById(R.id.xTvUserName);
        mTvEmail = (TextView)findViewById(R.id.xTvEmail);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.xDrawerLayout);
        mDrawerLayout.setScrimColor(Color.parseColor("#88000000"));

        mLinLayMenuLayout = (LinearLayout)findViewById(R.id.xRelLayMenu);
        mLinLayHome = (LinearLayout)findViewById(R.id.xLinLayHome);
        mLinLayMyWallet = (LinearLayout)findViewById(R.id.xLinLayMyWallet);
        mLinLayMyActivity = (LinearLayout)findViewById(R.id.xLinLayMyActivity);
        mLinLayNotifications = (LinearLayout)findViewById(R.id.xLinLayNotifications);
        mLinLayContractHistory = (LinearLayout)findViewById(R.id.xLinLayContractHistory);
        mLinLayRequestQuote = (LinearLayout)findViewById(R.id.xLinLayRequestQuote);
        mLinLaySettings = (LinearLayout)findViewById(R.id.xLinLaySettings);
        mLinLayEditProfile = (LinearLayout)findViewById(R.id.xLinLayEditprofile);
        mLinLayReportIssue = (LinearLayout)findViewById(R.id.xLinLayReportAnIssue);
        mLinLayLogout = (LinearLayout)findViewById(R.id.xLinLayLogout);

        mRelLayExtraImages = (RelativeLayout)findViewById(R.id.xRelLayExtraImages);


        mFabMenu = (FloatingActionsMenu)findViewById(R.id.xfab_menu);
        mFramelayoutFab = (FrameLayout)findViewById(R.id.xFrameLayoutFab);
        mFabCreateCampaign = (com.getbase.floatingactionbutton.FloatingActionButton)findViewById(R.id.xfab_createCampaign);
        mFabHostSpace = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.xfab_hostSpace);
        mFramelayoutFab.getBackground().setAlpha(0);

        mIvMenu.setOnClickListener(this);
        mLinLayHome.setOnClickListener(this);
        mLinLayMyWallet.setOnClickListener(this);
        mLinLayMyActivity.setOnClickListener(this);
        mLinLayNotifications.setOnClickListener(this);
        mLinLayContractHistory.setOnClickListener(this);
        mLinLayRequestQuote.setOnClickListener(this);
        mLinLaySettings.setOnClickListener(null);
        mLinLayEditProfile.setOnClickListener(this);
        mLinLayReportIssue.setOnClickListener(this);
        mLinLayLogout.setOnClickListener(this);

        mFabCreateCampaign.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent createCampaign = new Intent(Home.this,CreateCampaign.class);
                startActivity(createCampaign);
            }
        });
        mFabHostSpace.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent hostSpace = new Intent(Home.this,HostSpace.class);
                startActivity(hostSpace);
            }
        });



        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int panelWidth = (int) (metrics.widthPixels * 0.65);
        mLinLayMenuLayout.getLayoutParams().width = panelWidth;
        mLinLayMenuLayout.getLayoutParams().height = metrics.heightPixels ;


        mFabMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                mFramelayoutFab.getBackground().setAlpha(240);
                mFramelayoutFab.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        return true;
                    }
                });
            }

            @Override
            public void onMenuCollapsed() {
                mFramelayoutFab.getBackground().setAlpha(0);
                mFramelayoutFab.setOnTouchListener(null);

            }
        });


        mFabMenu.setVisibility(View.VISIBLE);

        mTvUsername.setText(mUserName);
        mTvEmail.setText(mEmail);
        ImageLoader.getInstance().displayImage(mProfileImage, mRIvProfileImage, mOptions);

        HomeFragment mHomeFragment = new HomeFragment();
        mChangeFragment(mHomeFragment);
    }



    @Override
    public void onClick(View view)
    {
        if(view == mIvMenu)
        {
            mDrawerOpenClose();
        }
        else if( view == mLinLayHome)
        {

            mDrawerOpenClose();
            if(!(getSupportFragmentManager().findFragmentById(R.id.xFrameLayoutDrawerContent) instanceof HomeFragment))
            {


                mFabMenu.setVisibility(View.VISIBLE);
                mTvTitle.setText("Home");
                mRelLayExtraImages.setVisibility(View.VISIBLE);
                HomeFragment mHomeFragment = new HomeFragment();
                mChangeFragment(mHomeFragment);
            }
        }
        else if(view == mLinLayMyWallet)
        {

            mDrawerOpenClose();
            if(!(getSupportFragmentManager().findFragmentById(R.id.xFrameLayoutDrawerContent) instanceof MyWalletFragment))
            {

                mFabMenu.setVisibility(View.GONE);
                mTvTitle.setText("My Wallet");
                mRelLayExtraImages.setVisibility(View.GONE);
                MyWalletFragment mMyWalletFragment = new MyWalletFragment();
                mChangeFragment(mMyWalletFragment);
            }

        }
        else if(view == mLinLayMyActivity)
        {
            mDrawerOpenClose();
            if(!(getSupportFragmentManager().findFragmentById(R.id.xFrameLayoutDrawerContent) instanceof MyActivityFragment))
            {
                mFabMenu.setVisibility(View.GONE);
                mTvTitle.setText("My Activity");
                mRelLayExtraImages.setVisibility(View.GONE);
                MyActivityFragment mMyActivityFragment = new MyActivityFragment();
                mChangeFragment(mMyActivityFragment);
            }
        }
        else if(view == mLinLayNotifications)
        {
            mDrawerOpenClose();
            if(!(getSupportFragmentManager().findFragmentById(R.id.xFrameLayoutDrawerContent) instanceof NotificationsFragment))
            {
                mFabMenu.setVisibility(View.GONE);
                mTvTitle.setText("Notifications");
                mRelLayExtraImages.setVisibility(View.GONE);
                NotificationsFragment mNotificationsFragment = new NotificationsFragment();
                mChangeFragment(mNotificationsFragment);

            }
        }
        else if(view == mLinLayContractHistory)
        {
            mDrawerOpenClose();
            if(!(getSupportFragmentManager().findFragmentById(R.id.xFrameLayoutDrawerContent) instanceof ContractHistoryFragment))
            {
                mFabMenu.setVisibility(View.GONE);
                mTvTitle.setText("Contract History");
                mRelLayExtraImages.setVisibility(View.GONE);
                ContractHistoryFragment mContractHistoryFragment = new ContractHistoryFragment();
                mChangeFragment(mContractHistoryFragment);

            }

        }
        else if(view == mLinLayRequestQuote)
        {
            mDrawerOpenClose();
            if(!(getSupportFragmentManager().findFragmentById(R.id.xFrameLayoutDrawerContent) instanceof RequestQuoteFragment))
            {
                mFabMenu.setVisibility(View.GONE);
                mTvTitle.setText("Request Quote");
                mRelLayExtraImages.setVisibility(View.GONE);
                RequestQuoteFragment mRequestQuoteFragment = new RequestQuoteFragment();
                mChangeFragment(mRequestQuoteFragment);

            }

        }
        else if(view == mLinLayEditProfile)
        {
            mDrawerOpenClose();
            if(!(getSupportFragmentManager().findFragmentById(R.id.xFrameLayoutDrawerContent) instanceof EditProfileFragment))
            {
                mFabMenu.setVisibility(View.GONE);
                mTvTitle.setText("Edit Profile");
                mRelLayExtraImages.setVisibility(View.GONE);
                EditProfileFragment mEditProfileFragment = new EditProfileFragment();
                mChangeFragment(mEditProfileFragment);

            }

        }
        else if(view == mLinLayReportIssue)
        {
            mDrawerOpenClose();
            if(!(getSupportFragmentManager().findFragmentById(R.id.xFrameLayoutDrawerContent) instanceof ReportIssueFragment))
            {
                mFabMenu.setVisibility(View.GONE);
                mTvTitle.setText("Report Issue");
                mRelLayExtraImages.setVisibility(View.GONE);
                ReportIssueFragment mReportIssueFragment = new ReportIssueFragment();
                mChangeFragment(mReportIssueFragment);

            }

        }
        else if(view == mLinLayLogout)
        {
            mShowDialog(Home.this, StaticUtils.LOGOUT_TITLE, StaticUtils.LOGOUT_MESSAGE, false, true);
        }

    }

    private void mChangeFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.xFrameLayoutDrawerContent,fragment).commit();
    }

    private void mDrawerOpenClose()
    {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START))
        {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }
}

