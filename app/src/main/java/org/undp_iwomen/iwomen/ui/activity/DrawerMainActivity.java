package org.undp_iwomen.iwomen.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.google.gson.Gson;
import com.makeramen.RoundedImageView;
import com.smk.skalertmessage.SKToastMessage;
import com.squareup.picasso.Picasso;

import org.smk.application.StoreUtil;
import org.smk.clientapi.NetworkEngine;
import org.smk.gcm.GcmCommon;
import org.smk.iwomen.BaseActionBarActivity;
import org.smk.iwomen.CompetitionNewGameActivity;
import org.smk.iwomen.CompetitionWinnerGroupActivity;
import org.smk.iwomen.ResponseError;
import org.smk.model.APKVersion;
import org.smk.model.CompetitionQuestion;
import org.smk.model.Review;
import org.smk.model.User;
import org.undp_iwomen.iwomen.CommonConfig;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.model.DownloadFileFromURL;
import org.undp_iwomen.iwomen.model.retrofit_api.SMKserverAPI;
import org.undp_iwomen.iwomen.model.retrofit_api.SMKserverStringConverterAPI;
import org.undp_iwomen.iwomen.ui.adapter.DrawerListViewAdapter;
import org.undp_iwomen.iwomen.ui.fragment.BeTogetherFragment;
import org.undp_iwomen.iwomen.ui.fragment.GoogleMapFragment;
import org.undp_iwomen.iwomen.ui.fragment.MainMaterialTab;
import org.undp_iwomen.iwomen.ui.fragment.ResourcesFragment;
import org.undp_iwomen.iwomen.ui.fragment.SettingsFragment;
import org.undp_iwomen.iwomen.ui.fragment.SisterAppFragment;
import org.undp_iwomen.iwomen.ui.fragment.TalkTogetherCategoryFragment;
import org.undp_iwomen.iwomen.ui.fragment.WinPrizesFragment;
import org.undp_iwomen.iwomen.ui.widget.AnimateCustomTextView;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.utils.Connection;
import org.undp_iwomen.iwomen.utils.SharePrefUtils;
import org.undp_iwomen.iwomen.utils.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by khinsandar on 7/29/15.
 */


public class DrawerMainActivity extends BaseActionBarActivity {
    private DrawerLayout drawerLayoutt;
    private LinearLayout mDrawerLinearLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;

    private String[] DrawerListName;
    private int[] DrawerListIcon;
    private CharSequence mTitle;
    public CustomTextView textViewTitle;
    private TextView txt_user_name;
    private CustomTextView txt_sing_out;
    private static final int LOGIN_REQUEST = 0;
    private SharedPreferences mSharedPreferencesUserInfo;
    private SharedPreferences.Editor mEditorUserInfo;
    private String user_name, user_obj_id, user_id, user_ph, register_msg, user_img_path;
    SharedPreferences sharePrefLanguageUtil;
    String mstr_lang;
    Runnable run;
    DrawerListViewAdapter drawer_adapter;
    //ProfilePictureView userProfilePicture;
    ProgressBar drawer_progressBar_profile_item;
    RoundedImageView drawer_profilePic_rounded;
    String userprofile_Image_path;
    String mstrUserfbId;
    SharePrefUtils sessionManager;

    String post_count, user_code, user_points, user_share_status;
    TextView txt_menu_user_post_count;
    TextView txt_menu_setting;
    TextView txt_menu_user_points;
    TextView txt_menu_user_code;

    LinearLayout ly_menu_profile_area;
    private AnimateCustomTextView btn_play_game;
    private LinearLayout layout_play_game;
    private ImageView img_play_game;
    public int mversionCode = 0;
    public String mversionName ;

    //Copied User Code for share
    private ClipboardManager myClipboard;
    private ClipData myClip;

    private String mstrBackParam;


    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // For checking rating & reviews control;
        UsageCount = StoreUtil.getInstance().selectFrom("usage" + versionCode);
        if (UsageCount != null && UsageCount > 0) {
            UsageCount = StoreUtil.getInstance().saveTo("usage" + versionCode, UsageCount + 1);
            Log.i("Usage Count:", "if Usage : " + UsageCount);
        } else {
            UsageCount = StoreUtil.getInstance().saveTo("usage" + versionCode, 1);
            Log.i("Usage Count:", "else Usage : " + UsageCount);

        }

        FacebookSdk.sdkInitialize(getApplicationContext());
        // Initialize the SDK before executing any other operations,
        // especially, if you're using Facebook UI elements.


        //Register GCM Device User
        if (Connection.isOnline(getApplicationContext())) {
            GcmCommon.register(this);
        }


        org.undp_iwomen.iwomen.utils.Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.main_drawer_material);

        //Back from LoginActivity Control
        if (getIntent().getBooleanExtra("Exit me", false)) {
            finish();
            return;// add this to prevent from doing unnecessary stuffs
        }

       // getFacebookHashKey();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);
        //toolbar.setLogo(R.drawable.ic_action_myanmadeals_app_icon);
        textViewTitle = (CustomTextView) toolbar.findViewById(R.id.title_action2);
        textViewTitle.setText(R.string.app_name);


        drawerLayoutt = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLinearLayout = (LinearLayout) findViewById(R.id.left_drawer);
        //userProfilePicture = (ProfilePictureView) findViewById(R.id.profilePicture);
        mDrawerList = (ListView) findViewById(R.id.left_drawer_lv);
        txt_user_name = (TextView) findViewById(R.id.txt_user_name);
        txt_sing_out = (CustomTextView) findViewById(R.id.menu_sing_out);
        txt_menu_user_post_count = (TextView) findViewById(R.id.menu_user_post_count);
        ly_menu_profile_area = (LinearLayout) findViewById(R.id.menu_profile_area_ly);
        drawer_profilePic_rounded = (RoundedImageView) findViewById(R.id.drawer_profilePic_rounded);
        drawer_progressBar_profile_item = (ProgressBar) findViewById(R.id.drawer_progressBar_profile_item);
        layout_play_game = (LinearLayout) findViewById(R.id.ly2);
        btn_play_game = (AnimateCustomTextView) findViewById(R.id.drawer_btn_take_challenge);
        img_play_game = (ImageView) findViewById(R.id.img_play_game);
        txt_menu_setting = (TextView) findViewById(R.id.menu_setting_name_txt);
        txt_menu_user_points = (TextView) findViewById(R.id.drawer_point_txt);
        txt_menu_user_code = (TextView) findViewById(R.id.menu_user_code);

        // set a custom shadow that overlays the main content when the drawer opens
        drawerLayoutt.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayoutt, toolbar, R.string.app_name, R.string.app_name);
        drawerLayoutt.setDrawerListener(actionBarDrawerToggle);

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        // set up the drawer's list view with items and click listener
        mSharedPreferencesUserInfo = getSharedPreferences(CommonConfig.SHARE_PREFERENCE_USER_INFO, Context.MODE_PRIVATE);
        sharePrefLanguageUtil = getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);

        sessionManager = new SharePrefUtils(getApplicationContext());

        user_name = mSharedPreferencesUserInfo.getString(CommonConfig.USER_NAME, null);
        user_obj_id = mSharedPreferencesUserInfo.getString(CommonConfig.USER_OBJ_ID, null);

        user_id = mSharedPreferencesUserInfo.getString(CommonConfig.USER_ID, null);
        user_img_path = mSharedPreferencesUserInfo.getString(CommonConfig.USER_UPLOAD_IMG_URL, null);

        user_code = user_obj_id;

        //Log.e("<<USer Point>>", "==>" + mSharedPreferencesUserInfo.getString(CommonConfig.USER_POINTS, null));

        //Log.e("<<USer Status>>", "==>" + mSharedPreferencesUserInfo.getString(CommonConfig.USER_SHARE_STATUS, null));
        if (mSharedPreferencesUserInfo.getString(CommonConfig.USER_POINTS, null) == null) {
            user_points = "0"; // initial no point condition
        } else {
            user_points = mSharedPreferencesUserInfo.getString(CommonConfig.USER_POINTS, null);
        }

        if (mSharedPreferencesUserInfo.getString(CommonConfig.USER_SHARE_STATUS, null) == null) {
            user_share_status = "0";// For didn't share  //initial no status condition
        } else {
            user_share_status = mSharedPreferencesUserInfo.getString(CommonConfig.USER_SHARE_STATUS, null);
        }

        //TODO CHECK LOGIN OR NOT
        if (mSharedPreferencesUserInfo.getString(CommonConfig.USER_ROLE, null) == null) {
            startActivity(new Intent(this, MainLoginActivity.class));
            finish();
            return;// add this to prevent from doing unnecessary stuffs
        }

        setUserImg();

        txt_user_name.setText(user_name);
        txt_menu_user_code.setText("Code - " + user_code);
        txt_menu_user_points.setText(user_points);


        //TODO WHEN DRAWER ACTIVITY START CALLING for check
        LoadDrawerCustomData();

        if (savedInstanceState == null) {
            selectItem(0);
            drawerLayoutt.openDrawer(mDrawerLinearLayout);
        }

        //TODO USER POST COUNT and USER POINTS
        getUserPostCount();
        getCompetitionQuestion();

        txt_menu_user_post_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.ENG_LANG)) {
                    org.undp_iwomen.iwomen.utils.Utils.doToastEng(getApplicationContext(), getResources().getString(R.string.my_post_coming_soon_cmt));
                } else {

                    org.undp_iwomen.iwomen.utils.Utils.doToastMM(getApplicationContext(), getResources().getString(R.string.my_post_coming_soon_cmt_mm));
                }
            }
        });

        ly_menu_profile_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //org.undp_iwomen.iwomen.utils.Utils.doToastEng(getApplicationContext(), "On CLick" + user_obj_id);

                Intent intent = new Intent(getApplicationContext(), ProfileEditActivity.class);

                intent.putExtra("UserId", user_id);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //SKToastMessage.showMessage(DrawerMainActivity.this, getResources().getString(R.string.resource_coming_soon_eng), SKToastMessage.ERROR);


            }
        });

        txt_sing_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sessionManager.ClearLogOut();
                Intent intent = new Intent(getApplicationContext(), MainLoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

            }
        });

        txt_menu_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
                //fragmentManager.beginTransaction().replace(R.id.content_frame, settingsFragment).commit();


                setTitle(getResources().getString(R.string.menu8));
            }
        });
        myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        txt_menu_user_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myClip = ClipData.newPlainText("text", user_code);
                myClipboard.setPrimaryClip(myClip);

                if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.ENG_LANG)) {
                    org.undp_iwomen.iwomen.utils.Utils.doToastEng(getApplicationContext(), getResources().getString(R.string.Code_copy));
                } else {

                    org.undp_iwomen.iwomen.utils.Utils.doToastMM(getApplicationContext(), getResources().getString(R.string.Code_copy_mm));
                }

            }
        });

        run = new Runnable() {
            @Override
            public void run() {


                drawer_adapter.notifyDataSetChanged();
                mDrawerList.invalidateViews();
                mDrawerList.refreshDrawableState();


            }
        };


    }

    //TODO show profile image
    private void setUserImg() {


        if (Connection.isOnline(getApplicationContext())) {
            if (user_img_path != null && user_img_path != "") {
                try {

                    drawer_progressBar_profile_item.setVisibility(View.VISIBLE);

                    Picasso.with(getApplicationContext())
                            .load(user_img_path) //"http://cheapandcheerfulshopper.com/wp-content/uploads/2013/08/shopping1257549438_1370386595.jpg" //deal.photo1
                            .placeholder(R.drawable.place_holder)
                            .error(R.drawable.place_holder)
                            .into(drawer_profilePic_rounded, new ImageLoadedCallback(drawer_progressBar_profile_item) {
                                @Override
                                public void onSuccess() {
                                    if (this.progressBar != null) {
                                        this.progressBar.setVisibility(View.GONE);
                                    } else {
                                        this.progressBar.setVisibility(View.VISIBLE);
                                    }
                                }

                            });
                } catch (OutOfMemoryError outOfMemoryError) {
                    outOfMemoryError.printStackTrace();
                }
            } else {

                drawer_progressBar_profile_item.setVisibility(View.GONE);
            }
        } else {
            drawer_progressBar_profile_item.setVisibility(View.GONE);
        }

    }

    //TODO Comment Count API
    private void getUserPostCount() {


        if (Connection.isOnline(getApplicationContext())) {

            SMKserverStringConverterAPI.getInstance().getService().getUserPostCountByObjID(user_obj_id, new Callback<String>() {
                @Override
                public void success(String s, Response response) {
                    post_count = s;
                    txt_menu_user_post_count.setText(post_count + " Post");
                    //TODO get User points too

                }

                @Override
                public void failure(RetrofitError error) {

                }
            });


        } else {

            if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.ENG_LANG)) {
                org.undp_iwomen.iwomen.utils.Utils.doToastEng(getApplicationContext(), getResources().getString(R.string.open_internet_warning_eng));
            } else {

                org.undp_iwomen.iwomen.utils.Utils.doToastMM(getApplicationContext(), getResources().getString(R.string.open_internet_warning_mm));
            }
        }


    }

    //TODO Comment Count API
    /*private void getUserPointsCount() {
        if (Connection.isOnline(getApplicationContext())) {

        } else {

            if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.ENG_LANG)) {
                org.undp_iwomen.iwomen.utils.Utils.doToastEng(getApplicationContext(), getResources().getString(R.string.open_internet_warning_eng));
            } else {

                org.undp_iwomen.iwomen.utils.Utils.doToastMM(getApplicationContext(), getResources().getString(R.string.open_internet_warning_mm));
            }
        }
    }*/


    public void setThemeToApp() {
        sharePrefLanguageUtil = getSharedPreferences(Utils.PREF_SETTING_LANG, MODE_PRIVATE);
        int theme = sharePrefLanguageUtil.getInt(org.undp_iwomen.iwomen.utils.Utils.PREF_THEME, org.undp_iwomen.iwomen.utils.Utils.THEME_PINK);

        if (theme == org.undp_iwomen.iwomen.utils.Utils.THEME_BLUE) {
            setTheme(R.style.AppTheme_Blue);
        } else if (theme == org.undp_iwomen.iwomen.utils.Utils.THEME_PINK) {
            setTheme(R.style.AppTheme);
        } else if (theme == org.undp_iwomen.iwomen.utils.Utils.THEME_YELLOW) {
            setTheme(R.style.AppTheme_Yellow);
        }


    }

    public void LoadDrawerCustomData() {

        /*DrawerListName = new String[]
                {"Stories
                ",  "Resources", "Setting","AboutUs"};*/

        //TODO FONT DRAWERMAIN
        mstr_lang = sharePrefLanguageUtil.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);

        //TODO FONT DRAWERMAIN
        if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.ENG_LANG)) {
            DrawerListName = new String[]
                    //{getResources().getString(R.string.menu1), "Be Knowledgeable", "Be Together", "Talk Together", "Settings", "AboutUs", "Sister Apps"};
                    {getResources().getString(R.string.menu1), getResources().getString(R.string.menu2), getResources().getString(R.string.menu3), getResources().getString(R.string.menu4)
                            , getResources().getString(R.string.menu5), getResources().getString(R.string.menu6), getResources().getString(R.string.menu7), getResources().getString(R.string.menu8),getResources().getString(R.string.menu9)};
            DrawerListIcon = new int[]
                    {R.drawable.ic_stories,
                            R.drawable.ic_resources,
                            R.drawable.be_together,
                            R.drawable.ic_talk_together,
                            R.drawable.sister_app,//Win prize
                            R.mipmap.sister_app_new,
                            R.drawable.about_us,
                            R.drawable.ic_setting,
                            R.mipmap.ic_menu_update,
                    };

            drawer_adapter = new DrawerListViewAdapter(getApplicationContext(), DrawerListName, DrawerListIcon, mstr_lang);//mCategoriesTitles
                    /*mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                    R.layout.drawer_list_item, mPlanetTitles));*/
            drawer_adapter.notifyDataSetChanged();
            mDrawerList.setAdapter(drawer_adapter);
            mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
            setListViewHeightBasedOnChildren(mDrawerList);
        } else if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.MM_LANG)) {
            DrawerListName = new String[]
                    // {"စိတ္ဓာတ္ခ\u103Cန္အား\u107Fဖည္ ့ရန္", "ဗဟုုသုုတရရန္", "ေပ\u102Bင္းစည္းေဆာင္ရ\u103Cက္ရန္", "ေမး\u107Fမန္းေဆ\u103Cးေ\u108F\u103Cးရန္", "\u107Fပင္ဆင္ရန္", "က\u103C\u103A\u108Fုုပ္တိုု ့အေ\u107Eကာင္း", " Sister Apps"};
                    {getResources().getString(R.string.menu1_mm), getResources().getString(R.string.menu2_mm), getResources().getString(R.string.menu3_mm), getResources().getString(R.string.menu4_mm)
                            , getResources().getString(R.string.menu5_mm), getResources().getString(R.string.menu6_mm), getResources().getString(R.string.menu7_mm), getResources().getString(R.string.menu8_mm),getResources().getString(R.string.menu9_mm)};

            DrawerListIcon = new int[]
                    {R.drawable.ic_stories,
                            R.drawable.ic_resources,
                            R.drawable.be_together,
                            R.drawable.ic_talk_together,
                            R.drawable.sister_app,
                            R.mipmap.sister_app_new,
                            R.drawable.about_us,
                            R.drawable.ic_setting,
                            R.mipmap.ic_menu_update,
                    };

            drawer_adapter = new DrawerListViewAdapter(getApplicationContext(), DrawerListName, DrawerListIcon, mstr_lang);//mCategoriesTitles
                    /*mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                    R.layout.drawer_list_item, mPlanetTitles));*/
            drawer_adapter.notifyDataSetChanged();
            mDrawerList.setAdapter(drawer_adapter);
            mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
            setListViewHeightBasedOnChildren(mDrawerList);
        } else if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.MM_LANG_UNI)) {
            DrawerListName = new String[]
                    //{"စိတ္ဓာတ္ခ\u103Cန္အား\u107Fဖည္ ့ရန္", "ဗဟုုသုုတရရန္", "ေပ\u102Bင္းစည္းေဆာင္ရ\u103Cက္ရန္", "ေမး\u107Fမန္းေဆ\u103Cးေ\u108F\u103Cးရန္", "\u107Fပင္ဆင္ရန္", "က\u103C\u103A\u108Fုုပ္တိုု ့အေ\u107Eကာင္း", " Sister Apps"};
                    {getResources().getString(R.string.menu1_mm), getResources().getString(R.string.menu2_mm), getResources().getString(R.string.menu3_mm), getResources().getString(R.string.menu4_mm)
                            , getResources().getString(R.string.menu5_mm), getResources().getString(R.string.menu6_mm), getResources().getString(R.string.menu7_mm), getResources().getString(R.string.menu8_mm),getResources().getString(R.string.menu9_mm)};

            DrawerListIcon = new int[]
                    {R.drawable.ic_stories,
                            R.drawable.ic_resources,
                            R.drawable.be_together,
                            R.drawable.ic_talk_together,
                            R.drawable.sister_app,
                            R.mipmap.sister_app_new,
                            R.drawable.about_us,
                            R.drawable.ic_setting,
                            R.mipmap.ic_menu_update,
                    };

            drawer_adapter = new DrawerListViewAdapter(getApplicationContext(), DrawerListName, DrawerListIcon, mstr_lang);//mCategoriesTitles
                    /*mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                    R.layout.drawer_list_item, mPlanetTitles));*/
            drawer_adapter.notifyDataSetChanged();
            mDrawerList.setAdapter(drawer_adapter);
            mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
            setListViewHeightBasedOnChildren(mDrawerList);
        } else if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.MM_LANG_DEFAULT)) {
            DrawerListName = new String[]
                    //{"စိတ္ဓာတ္ခ\u103Cန္အား\u107Fဖည္ ့ရန္", "ဗဟုုသုုတရရန္", "ေပ\u102Bင္းစည္းေဆာင္ရ\u103Cက္ရန္", "ေမး\u107Fမန္းေဆ\u103Cးေ\u108F\u103Cးရန္", "\u107Fပင္ဆင္ရန္", "က\u103C\u103A\u108Fုုပ္တိုု ့အေ\u107Eကာင္း", " Sister Apps"};
                    {getResources().getString(R.string.menu1_mm), getResources().getString(R.string.menu2_mm), getResources().getString(R.string.menu3_mm), getResources().getString(R.string.menu4_mm)
                            , getResources().getString(R.string.menu5_mm), getResources().getString(R.string.menu6_mm), getResources().getString(R.string.menu7_mm), getResources().getString(R.string.menu8_mm),getResources().getString(R.string.menu9_mm)};

            DrawerListIcon = new int[]
                    {R.drawable.ic_stories,
                            R.drawable.ic_resources,
                            R.drawable.be_together,
                            R.drawable.ic_talk_together,
                            R.drawable.sister_app,
                            R.mipmap.sister_app_new,
                            R.drawable.about_us,
                            R.drawable.ic_setting,
                            R.mipmap.ic_menu_update,

                    };

            // R.drawable.ic_community, R.drawable.ic_news

            drawer_adapter = new DrawerListViewAdapter(getApplicationContext(), DrawerListName, DrawerListIcon, mstr_lang);//mCategoriesTitles
                    /*mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                    R.layout.drawer_list_item, mPlanetTitles));*/
            drawer_adapter.notifyDataSetChanged();
            mDrawerList.setAdapter(drawer_adapter);
            mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
            setListViewHeightBasedOnChildren(mDrawerList);
        } else {
            DrawerListName = new String[]
                    //{"စိတ္ဓာတ္ခ\u103Cန္အား\u107Fဖည္ ့ရန္", "ဗဟုုသုုတရရန္", "ေပ\u102Bင္းစည္းေဆာင္ရ\u103Cက္ရန္", "ေမး\u107Fမန္းေဆ\u103Cးေ\u108F\u103Cးရန္", "\u107Fပင္ဆင္ရန္", "က\u103C\u103A\u108Fုုပ္တိုု ့အေ\u107Eကာင္း", " Sister Apps"};
                    {getResources().getString(R.string.menu1_mm), getResources().getString(R.string.menu2_mm), getResources().getString(R.string.menu3_mm), getResources().getString(R.string.menu4_mm)
                            , getResources().getString(R.string.menu5_mm), getResources().getString(R.string.menu6_mm), getResources().getString(R.string.menu7_mm), getResources().getString(R.string.menu8_mm), getResources().getString(R.string.menu9_mm)};

            DrawerListIcon = new int[]
                    {R.drawable.ic_stories,
                            R.drawable.ic_resources,
                            R.drawable.be_together,
                            R.drawable.ic_talk_together,
                            R.drawable.sister_app,
                            R.mipmap.sister_app_new,
                            R.drawable.about_us,
                            R.drawable.ic_setting,
                            R.mipmap.ic_menu_update,
                    };

            drawer_adapter = new DrawerListViewAdapter(getApplicationContext(), DrawerListName, DrawerListIcon, mstr_lang);//mCategoriesTitles
                    /*mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                    R.layout.drawer_list_item, mPlanetTitles));*/
            drawer_adapter.notifyDataSetChanged();
            mDrawerList.setAdapter(drawer_adapter);
            mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
            setListViewHeightBasedOnChildren(mDrawerList);
        }


    }

    /**
     * **************** ListView WIthin ScrollView Step1 ********************
     */
    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = listView.getPaddingTop()
                + listView.getPaddingBottom();
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem instanceof ViewGroup)
                listItem.setLayoutParams(new AbsListView.LayoutParams(
                        AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT));
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
        //
        MainMaterialTab mainMaterialTab = new MainMaterialTab();
        /*StoriesFragment storiesFragment = new StoriesFragment();
        Bundle args = new Bundle();
        args.putInt(StoriesFragment.ARG_MENU_INDEX, position);
        storiesFragment.setArguments(args);*/


        ResourcesFragment resourcesFragment = new ResourcesFragment();

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

        SettingsFragment settingsFragment = new SettingsFragment();

        BeTogetherFragment beTogetherFragment = new BeTogetherFragment();

        GoogleMapFragment googleMapFragment = new GoogleMapFragment();

        SisterAppFragment sisterAppFragment = new SisterAppFragment();

        //TLGUserStoriesRecentFragment tlgUserStoriesRecentFragment = new TLGUserStoriesRecentFragment();

        TalkTogetherCategoryFragment talkTogetherCategoryFragment = new TalkTogetherCategoryFragment();

        //IntroFragment.newInstance(Color.parseColor("#4CAF50"), position);
        WinPrizesFragment winPrizesFragment = new WinPrizesFragment();
        switch (position) {
            case 0://Categories 1
                fragmentManager.beginTransaction().replace(R.id.content_frame, mainMaterialTab).commit();
                setTitle(DrawerListName[position]);
                break;
            case 1:
                fragmentManager.beginTransaction().replace(R.id.content_frame, resourcesFragment).commit();
                setTitle(DrawerListName[position]);
                break;
            case 2:
                fragmentManager.beginTransaction().replace(R.id.content_frame, googleMapFragment).commit();
                setTitle(DrawerListName[position]);
                break;

            case 3:
                //tlgUserStoriesRecentFragment
                fragmentManager.beginTransaction().replace(R.id.content_frame, talkTogetherCategoryFragment).commit();
                setTitle(DrawerListName[position]);
                break;

            case 4://Win Prize

                //fragmentManager.beginTransaction().replace(R.id.content_frame, winPrizesFragment.newInstance(Integer.parseInt(user_points),user_share_status)).commit();

                //fragmentManager.beginTransaction().replace(R.id.content_frame, winPrizesFragment.newInstance(10,user_share_status)).commit();
                Intent i = new Intent(this, WinPrizeActivity.class);
                //i.putExtra("point", "10");
                i.putExtra("page", user_share_status);
                startActivity(i);

                setTitle(DrawerListName[position]);
                break;
            case 5:// sister App
                /*Intent i = new Intent(getApplicationContext(), WinPrizeActivity.class);

                //i.putExtra("point", user_points);
                i.putExtra("page",user_share_status );

                startActivity(i);
                setTitle(DrawerListName[position]);
                break;*/
                fragmentManager.beginTransaction().replace(R.id.content_frame, sisterAppFragment).commit();
                setTitle(DrawerListName[position]);


                break;
            case 6://About us

                if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.ENG_LANG)) {

                    Intent intent2 = new Intent(this, AboutUsWebActivity.class);
                    intent2.putExtra("ActivityName", "DrawerMainActivity");
                    intent2.putExtra("URL", "file:///android_asset/tos/About-Us-Eng-v2.html");
                    startActivity(intent2);
                } else {
                    Intent intent2 = new Intent(this, AboutUsWebActivity.class);
                    intent2.putExtra("ActivityName", "DrawerMainActivity");
                    intent2.putExtra("URL", "file:///android_asset/tos/About-Us-MM-v2.html");
                    startActivity(intent2);
                }
                break;
            case 7:
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
                //fragmentManager.beginTransaction().replace(R.id.content_frame, settingsFragment).commit();
                setTitle(getResources().getString(R.string.menu8));

                break;//Sister apps

            case 8:

                // TODO it is for non application market.
                try {
                    mversionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
                    mversionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
                }catch (PackageManager.NameNotFoundException e){

                }


                checkAPKVersion();

                //new DownloadFileFromURL(DrawerMainActivity.this).execute(CommonConfig.GET_APK_DOWNLOAD_URL);




                break;//Sister apps
        }

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);

        //TODO close drawer
        drawerLayoutt.closeDrawer(mDrawerLinearLayout);
    }

    @Override
    public void setTitle(CharSequence title) {
        //getSupportActionBar().setTitle(title); // Black Letter showing default
        mTitle = title;
        //toolbar.setTitle(mTitle);

        //getActionBar().setTitle(mTitle);
        textViewTitle.setText(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void getFacebookHashKey() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo("org.undp_iwomen.iwomen", PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException ex) {


        } catch (NoSuchAlgorithmException e) {

        }
    }

    private void showRefreshInfoDialog() {
        /*MaterialDialog dialog = new MaterialDialog.Builder(DrawerMainActivity.this)
                .title("")//Title
                .customView(R.layout.custom_refresh_dialog, true)
                .backgroundColor(getResources().getColor(R.color.lime_color))
                .positiveText("OK")
                .positiveColor(R.color.primary)
                .positiveColorRes(R.color.primary)
                .negativeText("")
                .negativeColor(R.color.primary)
                .negativeColorRes(R.color.primary)
                .autoDismiss(false)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        //TODO logout method
                        //signOut();
                        dialog.dismiss();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .build();

        dialog.show();
        CustomTextView txt_dialog_head = (CustomTextView) dialog.findViewById(R.id.dialog_refresh_title);
        TextView txt_dialog_content = (TextView) dialog.findViewById(R.id.dialog_refresh_content);

        //TODO FONT DRAWERMAIN
        if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.ENG_LANG)) {
            txt_dialog_head.setText(R.string.app_name);
            txt_dialog_content.setText(R.string.dialog_refresh_msg_eng);

            //btn_play_game.setText(R.string.competition_play_game);
        } else if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.MM_LANG)) { //Zawygi

            txt_dialog_head.setText(R.string.app_name_mm);
            txt_dialog_content.setText(R.string.dialog_refresh_msg_mm);

            txt_dialog_head.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));
            txt_dialog_content.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));
        } else if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.MM_LANG_UNI)) {

            txt_dialog_head.setText(R.string.app_name_mm);
            txt_dialog_content.setText(R.string.dialog_refresh_msg_mm);
        } else {//DEFAULT

            txt_dialog_head.setText(R.string.app_name_mm);
            txt_dialog_content.setText(R.string.dialog_refresh_msg_mm);
        }*/

    }

    private class ImageLoadedCallback implements com.squareup.picasso.Callback {
        ProgressBar progressBar;

        public ImageLoadedCallback(ProgressBar progBar) {
            progressBar = progBar;
        }

        @Override
        public void onSuccess() {

        }

        @Override
        public void onError() {

        }
    }

    private void getCompetitionQuestion() {
        if (Connection.isOnline(this)) {

            NetworkEngine.getInstance().getCompetitionQuestion("", user_id, new Callback<CompetitionQuestion>() {

                @Override
                public void failure(final RetrofitError arg0) {
                    // TODO Auto-generated method stub
                    if (arg0.getResponse() != null) {
                        switch (arg0.getResponse().getStatus()) {
                            case 403:
                                layout_play_game.setVisibility(View.VISIBLE);
                                img_play_game.setImageResource(R.drawable.sticker2);
                                btn_play_game.setText(getResources().getString(R.string.competition_play_game));
                                btn_play_game.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        // TODO Auto-generated method stub
                                        try {
                                            ResponseError error = (ResponseError) arg0.getBodyAs(ResponseError.class);
                                            if (mstr_lang.equals(Utils.ENG_LANG)) {
                                                SKToastMessage.showMessage(DrawerMainActivity.this, error.getError(), SKToastMessage.INFO);
                                            } else if (mstr_lang.equals(Utils.MM_LANG)) {
                                                SKToastMessage.showMessage(DrawerMainActivity.this, error.getErrorMm(), SKToastMessage.INFO);
                                            }else{
                                                SKToastMessage.showMessage(DrawerMainActivity.this, error.getErrorMm(), SKToastMessage.INFO);

                                            }
                                        }catch (Exception e){

                                        }
                                    }
                                });
                                break;
                            case 400:
                                layout_play_game.setVisibility(View.GONE);
                                break;
                            default:
                                break;
                        }
                    }
                    getUserPointsCount();
                }

                @Override
                public void success(final CompetitionQuestion arg0, Response arg1) {
                    // TODO Auto-generated method stub
                    layout_play_game.setVisibility(View.VISIBLE);
                    Log.i("Competition : ", "Size = " + arg0.getCorrectAnswer().size());
                    if (arg0 != null && arg0.getCorrectAnswer().size() == 0) {
                        img_play_game.setImageResource(R.drawable.sticker2);
                        btn_play_game.setText(getResources().getString(R.string.competition_play_game));
                        btn_play_game.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                startActivity(new Intent(getApplicationContext(), CompetitionNewGameActivity.class).putExtra("competition_question", new Gson().toJson(arg0)));
                            }
                        });

                    } else if(arg0 != null && arg0.getCorrectAnswer().size() > 0) {
                        Log.i("Competition : ", "Who is discover");
                        img_play_game.setImageResource(R.drawable.sticker1);
                        btn_play_game.setText(getResources().getString(R.string.competition_discover_winner));
                        btn_play_game.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                startActivity(new Intent(getApplicationContext(), CompetitionWinnerGroupActivity.class).putExtra("competition_question", new Gson().toJson(arg0)));
                            }
                        });
                    }
                    getUserPointsCount();
                }
            });
        } else {

            layout_play_game.setVisibility(View.VISIBLE);
            btn_play_game.setText(getResources().getString(R.string.competition_play_game));

            layout_play_game.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.ENG_LANG)) {
                        org.undp_iwomen.iwomen.utils.Utils.doToastEng(getApplicationContext(), getResources().getString(R.string.no_connection));
                    } else {

                        org.undp_iwomen.iwomen.utils.Utils.doToastMM(getApplicationContext(), getResources().getString(R.string.no_connection_mm));
                    }
                }
            });

            //SKConnectionDetector.getInstance(this).showErrorMessage();
        }
    }

    private void getUserPointsCount() {
        if (Connection.isOnline(this)) {
            //Activity activity = getActivity();

                SMKserverAPI.getInstance().getService().getUserPoinsByID(user_id, new Callback<User>() {
                    @Override
                    public void success(User user, Response response) {


                            // etc ...
                            if (user.getPoints() != null) {

                                //Log.e("<<<<Drawer Main>>","User Point====>" + user.getPoints() + "/"+user.getShared());
                                //txt_points.setText(user.getPoints().toString() + getResources().getString(R.string.points_prize));

                                txt_menu_user_points.setText(user.getPoints().toString());
                                user_share_status =user.getShared().toString();
                                mEditorUserInfo = mSharedPreferencesUserInfo.edit();
                                mEditorUserInfo.putString(CommonConfig.USER_POINTS, user.getPoints().toString());
                                mEditorUserInfo.putString(CommonConfig.USER_SHARE_STATUS, user.getShared().toString());
                                mEditorUserInfo.commit();
                            } else {

                            }

                            //getPrizePointsCount();




                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });

        } else {

            /*if (lang.equals(Utils.ENG_LANG)) {
                Utils.doToastEng(mContext, getResources().getString(R.string.open_internet_warning_eng));
            } else {
                Utils.doToastMM(mContext, getResources().getString(R.string.open_internet_warning_mm));
            }*/
        }
    }

    @Override
    public void onBackPressed() {
        Log.i("Reviews", "Back Press : here obj-" + user_obj_id +"/count/"+UsageCount );
        if (user_obj_id != null) {
            Review review = StoreUtil.getInstance().selectFrom(user_obj_id + versionCode);
            Boolean feedback = StoreUtil.getInstance().selectFrom("feedback");
            Log.i("Reviews", "Back Press Review check :" + review + "feedback"+ feedback );
            if (review == null && UsageCount != null && UsageCount > 10) {
                Log.i("Reviews", " if Reviews :" + UsageCount);
                showReviewDialog(user_obj_id);

                UsageCount = StoreUtil.getInstance().saveTo("usage" + versionCode, 0);


            } else if (UsageCount != null && UsageCount > 10 && feedback == null) {//UsageCount != null && UsageCount > 3 && feedback == null
                Log.i("Reviews", " else if  Reviews :" + UsageCount );

                showFeedBack(user_obj_id);

                UsageCount = StoreUtil.getInstance().saveTo("usage" + versionCode, 0);

            } else {
                Log.i("Reviews", " else end  Reviews :" );

                finish();

            }
        } else {
            finish();
        }
    }

    private void checkAPKVersion() {
        if (Connection.isOnline(getApplicationContext())) {
            NetworkEngine.getInstance().getAPKVersion("", new Callback<APKVersion>() {
                @Override
                public void success(final APKVersion arg0, Response arg1) {
                    // TODO Auto-generated method stub
                    try {
                        if (arg0 != null) {
                            if (arg0.getVersionId() > mversionCode) {
                                showVersionDialog(DrawerMainActivity.this ,true );

                            }else{
                                showVersionDialog(DrawerMainActivity.this ,false );
                            }
                        }
                    } catch (NullPointerException ex) {
                        //ex.printStackTrace();
                    }
                }
                @Override
                public void failure(RetrofitError arg0) {
                    // TODO Auto-generated method stub

                }
            });
        }else {

            if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.ENG_LANG)) {
                org.undp_iwomen.iwomen.utils.Utils.doToastEng(getApplicationContext(), getResources().getString(R.string.no_connection));
            } else {

                org.undp_iwomen.iwomen.utils.Utils.doToastMM(getApplicationContext(), getResources().getString(R.string.no_connection_mm));
            }
        }

    }
    public static void showVersionDialog(final Activity activity , Boolean isAvalibale) {


        final Dialog alertDialog = new Dialog(activity);
        View convertView = View.inflate(activity, R.layout.custom_update_version_dialog, null);

        CustomTextView mtext = (CustomTextView) convertView.findViewById(R.id.dialog_version_content);
        Button btn_ok = (Button) convertView.findViewById(R.id.version_dialog_btn_ok);
        Button btn_no = (Button) convertView.findViewById(R.id.version_dialog_btn_no);
        alertDialog.setContentView(convertView);
        alertDialog.show();

        if(isAvalibale){
            mtext.setText(activity.getResources().getString(R.string.version_new_yes));
            btn_no.setVisibility(View.VISIBLE);
            btn_ok.setVisibility(View.VISIBLE);


        }else{
            mtext.setText(activity.getResources().getString(R.string.version_new_no));
            btn_ok.setVisibility(View.INVISIBLE);

            btn_no.setVisibility(View.VISIBLE);
            btn_no.setText(activity.getResources().getString(R.string.ok));
            //btn_no.setVisibility(View.INVISIBLE);

        }

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
                new DownloadFileFromURL(activity).execute(CommonConfig.GET_APK_DOWNLOAD_URL);



            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //finish();
                alertDialog.dismiss();

            }
        });
    }
}
