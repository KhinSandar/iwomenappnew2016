package org.undp_iwomen.iwomen.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
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
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.google.gson.Gson;
import com.makeramen.RoundedImageView;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;
import com.parse.utils.Utils;
import com.smk.application.StoreUtil;
import com.smk.clientapi.NetworkEngine;
import com.smk.iwomen.BaseActionBarActivity;
import com.smk.iwomen.CompetitionNewGameActivity;
import com.smk.iwomen.CompetitionWinnerGroupActivity;
import com.smk.model.CompetitionQuestion;
import com.smk.model.Review;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.undp_iwomen.iwomen.CommonConfig;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.model.MyTypeFace;
import org.undp_iwomen.iwomen.model.retrofit_api.UserPostAPI;
import org.undp_iwomen.iwomen.ui.adapter.DrawerListViewAdapter;
import org.undp_iwomen.iwomen.ui.fragment.BeTogetherFragment;
import org.undp_iwomen.iwomen.ui.fragment.GoogleMapFragment;
import org.undp_iwomen.iwomen.ui.fragment.MainMaterialTab;
import org.undp_iwomen.iwomen.ui.fragment.ResourcesFragment;
import org.undp_iwomen.iwomen.ui.fragment.SettingsFragment;
import org.undp_iwomen.iwomen.ui.fragment.SisterAppFragment;
import org.undp_iwomen.iwomen.ui.fragment.TLGUserStoriesRecentFragment;
import org.undp_iwomen.iwomen.ui.fragment.TalkTogetherCategoryFragment;
import org.undp_iwomen.iwomen.ui.widget.AnimateCustomTextView;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.ui.widget.ProfilePictureView;
import org.undp_iwomen.iwomen.utils.Connection;
import org.undp_iwomen.iwomen.utils.SharePrefUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

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
    private ParseUser currentUser;

    private SharedPreferences mSharedPreferencesUserInfo;
    private SharedPreferences.Editor mEditorUserInfo;
    private String user_name, user_obj_id, user_ph;
    SharedPreferences sharePrefLanguageUtil;
    String mstr_lang;
    Runnable run;
    DrawerListViewAdapter drawer_adapter;

    ProfilePictureView userProfilePicture;
    ProgressBar drawer_progressBar_profile_item;


    RoundedImageView drawer_profilePic_rounded;
    String userprofile_Image_path;

    String mstrUserfbId;
    SharePrefUtils sessionManager;

    String post_count;
    TextView menu_user_post_count;

    LinearLayout ly_menu_profile_area;
    private AnimateCustomTextView btn_play_game;
    private LinearLayout layout_play_game;

    @Override
    protected void onStart() {
        super.onStart();

        currentUser = ParseUser.getCurrentUser();
        /*if (currentUser != null) {
            showProfileLoggedIn();
        } else {
            showProfileLoggedOut();
        }*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // For checking rating & reviews control;
        UsageCount = StoreUtil.getInstance().selectFrom("usage"+versionCode);
        if(UsageCount != null && UsageCount > 0){
            UsageCount = StoreUtil.getInstance().saveTo("usage"+versionCode, UsageCount + 1);
            Log.i("Usage Count:","Usage : "+ UsageCount);
        }
        else
            UsageCount = StoreUtil.getInstance().saveTo("usage"+versionCode, 1);


        FacebookSdk.sdkInitialize(getApplicationContext());
        // Initialize the SDK before executing any other operations,
        // especially, if you're using Facebook UI elements.



        org.undp_iwomen.iwomen.utils.Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.main_drawer_material);

        //Back from LoginActivity Control
        if (getIntent().getBooleanExtra("Exit me", false)) {
            finish();
            return; // add this to prevent from doing unnecessary stuffs
        }

        //getFacebookHashKey();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);
        //toolbar.setLogo(R.drawable.ic_action_myanmadeals_app_icon);
        textViewTitle = (CustomTextView) toolbar.findViewById(R.id.title_action2);
        //textViewTitle.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));
        //textViewTitle.setText("Myanma\u0020Deals");//"MYANMARDEALS"
        textViewTitle.setText(R.string.app_name);
        //textViewTitle.setTypeface(faceBold);


        drawerLayoutt = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLinearLayout = (LinearLayout) findViewById(R.id.left_drawer);
        userProfilePicture = (ProfilePictureView) findViewById(R.id.profilePicture);
        mDrawerList = (ListView) findViewById(R.id.left_drawer_lv);
        txt_user_name = (TextView) findViewById(R.id.txt_user_name);
        txt_sing_out = (CustomTextView) findViewById(R.id.menu_sing_out);
        menu_user_post_count = (TextView) findViewById(R.id.menu_user_post_count);

        ly_menu_profile_area = (LinearLayout) findViewById(R.id.menu_profile_area_ly);

        drawer_profilePic_rounded = (RoundedImageView) findViewById(R.id.drawer_profilePic_rounded);
        drawer_progressBar_profile_item = (ProgressBar) findViewById(R.id.drawer_progressBar_profile_item);

        layout_play_game = (LinearLayout) findViewById(R.id.ly2);
        btn_play_game = (AnimateCustomTextView)findViewById(R.id.drawer_btn_take_challenge);
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
       /* if (user_obj_id == null) {

            if (savedInstanceState == null) {
                selectItem(0);
            }
        } else {
            // User clicked to log in.
            ParseLoginBuilder loginBuilder = new ParseLoginBuilder(
                    DrawerMainActivity.this);
            startActivityForResult(loginBuilder.build(), LOGIN_REQUEST);

        }*/
        Bundle bundle = getIntent().getExtras();

        currentUser = ParseUser.getCurrentUser();

        //TODO WHEN DRAWER ACTIVITY START CALLING for check
        LoadDrawerCustomData();

        if (currentUser != null) {
            // User clicked to log out.
            //showProfileLoggedOut();
            if (savedInstanceState == null) {


                if (!mSharedPreferencesUserInfo.getBoolean(CommonConfig.IS_LOGIN, false)) {


                    String objectId = bundle.getString(CommonConfig.USER_OBJ_ID);
                    String userName = bundle.getString(CommonConfig.USER_NAME);
                    String userPH = bundle.getString(CommonConfig.USER_PH);
                    String lang = bundle.getString(Utils.PREF_SETTING_LANG);
                    Boolean isFbInclude = bundle.getBoolean(CommonConfig.USER_FBID_INCLUDE);


                    if (objectId.length() != 0) {
                        mEditorUserInfo = mSharedPreferencesUserInfo.edit();
                        mEditorUserInfo.putBoolean(CommonConfig.IS_LOGIN, true);
                        mEditorUserInfo.putString(CommonConfig.USER_OBJ_ID, objectId);
                        mEditorUserInfo.putString(CommonConfig.USER_NAME, userName);
                        if (isFbInclude) {
                            mstrUserfbId = bundle.getString(CommonConfig.USER_FBID);
                            mEditorUserInfo.putString(CommonConfig.USER_FBID, mstrUserfbId);
                        }

                        SharedPreferences.Editor editor = sharePrefLanguageUtil.edit();
                        editor.putString(Utils.PREF_SETTING_LANG, lang);

                        /*if(userNrc != null) {
                            mEditorUserInfo.putString(CommonConfig.USER_NRC, userNrc);
                        }else if(passport != null){
                            mEditorUserInfo.putString(CommonConfig.USER_NRC, passport); // For rare register with Passport case
                        }*/
                        mEditorUserInfo.putString(CommonConfig.USER_PH, userPH);

                        mEditorUserInfo.commit();


                    }

                    user_name = mSharedPreferencesUserInfo.getString(CommonConfig.USER_NAME, null);
                    user_obj_id = mSharedPreferencesUserInfo.getString(CommonConfig.USER_OBJ_ID, null);


                    //TODO 1
                    getCompetitionQuestion();


                    txt_user_name.setText(user_name);

                    if (mstrUserfbId != null) {

                        userProfilePicture.setProfileId(mstrUserfbId);
                    }

                    //TODO 1st priority if update image case
                    if (currentUser.get("userImgPath") != null && currentUser.get("userImgPath") != "null") {
                        userprofile_Image_path = currentUser.get("userImgPath").toString();

                        if (userprofile_Image_path != null) {

                            mEditorUserInfo = mSharedPreferencesUserInfo.edit();
                            mEditorUserInfo.putString(CommonConfig.USER_IMAGE_PATH, userprofile_Image_path);

                            mEditorUserInfo.commit();
                            drawer_progressBar_profile_item.setVisibility(View.VISIBLE);
                            try {
                                drawer_profilePic_rounded.setVisibility(View.VISIBLE);
                                userProfilePicture.setVisibility(View.GONE);
                                Picasso.with(getApplicationContext())
                                        .load(userprofile_Image_path) //"http://cheapandcheerfulshopper.com/wp-content/uploads/2013/08/shopping1257549438_1370386595.jpg" //deal.photo1
                                        .placeholder(R.drawable.blank_profile)
                                        .error(R.drawable.blank_profile)
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
                            drawer_profilePic_rounded.setVisibility(View.GONE);
                            userProfilePicture.setVisibility(View.VISIBLE);

                            //TODO IF user no upload imagess and Fb login
                            if (mstrUserfbId != null) {


                                userProfilePicture.setProfileId(mstrUserfbId);
                                drawer_progressBar_profile_item.setVisibility(View.GONE);

                            } else {
                                drawer_progressBar_profile_item.setVisibility(View.GONE);
                                drawer_profilePic_rounded.setVisibility(View.VISIBLE);
                                userProfilePicture.setVisibility(View.GONE);
                            }

                        }
                    } else {//TODO very first time upload image case
                        if (currentUser.get("user_profile_img") != null && currentUser.get("user_profile_img") != "null") {


                            Uri uri = Uri.parse(currentUser.getParseFile("user_profile_img").getUrl());
                            userprofile_Image_path = currentUser.getParseFile("user_profile_img").getUrl();
                            //userProfilePicture.setD


                            if (userprofile_Image_path != null) {

                                mEditorUserInfo = mSharedPreferencesUserInfo.edit();
                                mEditorUserInfo.putString(CommonConfig.USER_IMAGE_PATH, userprofile_Image_path);

                                mEditorUserInfo.commit();
                                drawer_progressBar_profile_item.setVisibility(View.VISIBLE);
                                try {
                                    drawer_profilePic_rounded.setVisibility(View.VISIBLE);
                                    userProfilePicture.setVisibility(View.GONE);
                                    Picasso.with(getApplicationContext())
                                            .load(userprofile_Image_path) //"http://cheapandcheerfulshopper.com/wp-content/uploads/2013/08/shopping1257549438_1370386595.jpg" //deal.photo1
                                            .placeholder(R.drawable.blank_profile)
                                            .error(R.drawable.blank_profile)
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
                                drawer_profilePic_rounded.setVisibility(View.GONE);
                                userProfilePicture.setVisibility(View.VISIBLE);

                                //TODO IF user no upload imagess and Fb login
                                if (mstrUserfbId != null) {


                                    userProfilePicture.setProfileId(mstrUserfbId);
                                    drawer_progressBar_profile_item.setVisibility(View.GONE);

                                } else {
                                    drawer_progressBar_profile_item.setVisibility(View.GONE);
                                    drawer_profilePic_rounded.setVisibility(View.VISIBLE);
                                    userProfilePicture.setVisibility(View.GONE);
                                }

                            }

                        } else {//TODO very first time not upload image case , But Login with Fb case
                            drawer_progressBar_profile_item.setVisibility(View.GONE);
                            drawer_profilePic_rounded.setVisibility(View.GONE);
                            userProfilePicture.setVisibility(View.VISIBLE);

                            //TODO IF user no upload imagess and Fb login
                            if (mstrUserfbId != null) {


                                userProfilePicture.setProfileId(mstrUserfbId);
                                drawer_progressBar_profile_item.setVisibility(View.GONE);

                            } else {
                                drawer_progressBar_profile_item.setVisibility(View.GONE);
                                drawer_profilePic_rounded.setVisibility(View.VISIBLE);
                                userProfilePicture.setVisibility(View.GONE);
                            }

                        }
                    }

                    Log.e("1st Login==== > user Info parse", objectId + "Parse :" + userName + ":mstrUserfbId >>" + mstrUserfbId);

                    //TODO FONT DRAWERMAIN
                    mstr_lang = sharePrefLanguageUtil.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);



                    getUserPostCount();

                    LoadDrawerCustomData();
                    showRefreshInfoDialog();


                } else {
                    user_name = mSharedPreferencesUserInfo.getString(CommonConfig.USER_NAME, null);
                    user_obj_id = mSharedPreferencesUserInfo.getString(CommonConfig.USER_OBJ_ID, null);
                    //Toast.makeText(this, "2ndTime(PaymentActivity) You are login as \n " + mSharedPreferencesUserInfo.getString(CommonConfig.USER_NAME, null) + "\n" + sharePrefLanguageUtil.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG), Toast.LENGTH_LONG).show();

                    //TODO 2
                    getCompetitionQuestion();

                    mstrUserfbId = mSharedPreferencesUserInfo.getString(CommonConfig.USER_FBID, null);
                    //2nd Login==== > user Info parse﹕ GuzTg3T1aWParse :Su:mstrUserfbId >>null
                    Log.e("2nd Login==== > user Info parse", user_obj_id + "Parse :" + user_name + ":mstrUserfbId >>" + mstrUserfbId);


                    //TODO FONT DRAWERMAIN
                    mstr_lang = sharePrefLanguageUtil.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);
                    getUserPostCount();

                    //LoadDrawerCustomData();


                    userprofile_Image_path = mSharedPreferencesUserInfo.getString(CommonConfig.USER_IMAGE_PATH, null);


                    if (userprofile_Image_path != null) {

                        try {
                            drawer_profilePic_rounded.setVisibility(View.VISIBLE);
                            userProfilePicture.setVisibility(View.GONE);
                            Picasso.with(getApplicationContext())
                                    .load(userprofile_Image_path) //"http://cheapandcheerfulshopper.com/wp-content/uploads/2013/08/shopping1257549438_1370386595.jpg" //deal.photo1
                                    .placeholder(R.drawable.blank_profile)
                                    .error(R.drawable.blank_profile)
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
                        drawer_profilePic_rounded.setVisibility(View.GONE);
                        userProfilePicture.setVisibility(View.VISIBLE);
                        //TODO IF user no upload imagess and Fb login
                        if (mstrUserfbId != null) {


                            userProfilePicture.setProfileId(mstrUserfbId);
                            drawer_progressBar_profile_item.setVisibility(View.GONE);

                        } else {
                            drawer_progressBar_profile_item.setVisibility(View.GONE);
                            drawer_profilePic_rounded.setVisibility(View.VISIBLE);
                            userProfilePicture.setVisibility(View.GONE);
                        }
                    }


                    txt_user_name.setText(user_name);

                }


                if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.ENG_LANG)) {

                    txt_sing_out.setText("Sign Out");
                    txt_sing_out.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.NORMAL));

                    String languageToLoad  = "eng"; // your language
                    Locale locale = new Locale(languageToLoad);
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

                    SharedPreferences.Editor editor = getSharedPreferences("mLanguage", MODE_PRIVATE).edit();
                    editor.putString("lang", "eng");
                    editor.commit();

                } else if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.MM_LANG)) {
                    txt_sing_out.setText("ထ\u103Cက္ရန္");
                    txt_sing_out.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));
                    String languageToLoad  = "mm"; // your language
                    Locale locale = new Locale(languageToLoad);
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

                    SharedPreferences.Editor editor = getSharedPreferences("mLanguage", MODE_PRIVATE).edit();
                    editor.putString("lang", "mm");
                    editor.commit();
                } else if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.MM_LANG_UNI)) {
                    txt_sing_out.setText("ထ\u103Cက္ရန္");
                    //txt_sing_out.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));
                    String languageToLoad  = "mm"; // your language
                    Locale locale = new Locale(languageToLoad);
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

                    SharedPreferences.Editor editor = getSharedPreferences("mLanguage", MODE_PRIVATE).edit();
                    editor.putString("lang", "mm");
                    editor.commit();
                } else if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.MM_LANG_DEFAULT)) {
                    txt_sing_out.setText("ထ\u103Cက္ရန္");
                    //txt_sing_out.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));
                    String languageToLoad  = "mm"; // your language
                    Locale locale = new Locale(languageToLoad);
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

                    SharedPreferences.Editor editor = getSharedPreferences("mLanguage", MODE_PRIVATE).edit();
                    editor.putString("lang", "mm");
                    editor.commit();
                }


                selectItem(0);
                drawerLayoutt.openDrawer(mDrawerLinearLayout);

            }

            ly_menu_profile_area.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //org.undp_iwomen.iwomen.utils.Utils.doToastEng(getApplicationContext(), "On CLick" + user_obj_id);

                    Intent intent = new Intent(getApplicationContext(), ProfileEditActivity.class);

                    intent.putExtra("UserId", user_obj_id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
            });


        } else {

            // User clicked to log in.
            ParseLoginBuilder loginBuilder = new ParseLoginBuilder(
                    DrawerMainActivity.this);
            startActivityForResult(loginBuilder.build(), LOGIN_REQUEST);


        }

        txt_sing_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser != null) {
                    ParseUser.logOut();
                    sessionManager.ClearLogOut();
                    // User clicked to log in.
                    ParseLoginBuilder loginBuilder = new ParseLoginBuilder(
                            DrawerMainActivity.this);
                    startActivityForResult(loginBuilder.build(), LOGIN_REQUEST);

                }
            }
        });


        run = new Runnable() {
            @Override
            public void run() {


                //queryToBooking();

                drawer_adapter.notifyDataSetChanged();
                mDrawerList.invalidateViews();
                mDrawerList.refreshDrawableState();

                //Log.e("Load Adapter===","==runRunnable=" );
            }
        };


    }

    //TODO Comment Count API
    private void getUserPostCount() {


        if (Connection.isOnline(getApplicationContext())) {

            UserPostAPI.getInstance().getService().getPostCount(0, 1, "{\"postUploadName\":\"" + user_name + "\"}", new Callback<String>() {
                @Override
                public void success(String s, Response response) {
                    try {
                        JSONObject whole_body = new JSONObject(s);
                        JSONArray result = whole_body.getJSONArray("results");

                        post_count = whole_body.getString("count");
                        menu_user_post_count.setText(post_count + " Post");

                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }

                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("///Count Error//", "==>" + error.toString());

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
                    {"Be Inspired", "Be Knowledgeable", "Be Together", "Talk Together", "Settings", "AboutUs", "Sister Apps"};

            DrawerListIcon = new int[]
                    {R.drawable.ic_stories,
                            R.drawable.ic_resources,
                            R.drawable.be_together,
                            R.drawable.ic_talk_together,
                            R.drawable.ic_setting,
                            R.drawable.about_us,
                            R.drawable.sister_app};

            drawer_adapter = new DrawerListViewAdapter(getApplicationContext(), DrawerListName, DrawerListIcon, mstr_lang);//mCategoriesTitles
                    /*mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                    R.layout.drawer_list_item, mPlanetTitles));*/
            drawer_adapter.notifyDataSetChanged();
            mDrawerList.setAdapter(drawer_adapter);
            mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
            setListViewHeightBasedOnChildren(mDrawerList);
        } else if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.MM_LANG)) {
            DrawerListName = new String[]
                    {"စိတ္ဓာတ္ခ\u103Cန္အား\u107Fဖည္ ့ရန္", "ဗဟုုသုုတရရန္", "ေပ\u102Bင္းစည္းေဆာင္ရ\u103Cက္ရန္", "ေမး\u107Fမန္းေဆ\u103Cးေ\u108F\u103Cးရန္", "\u107Fပင္ဆင္ရန္", "က\u103C\u103A\u108Fုုပ္တိုု ့အေ\u107Eကာင္း", " Sister Apps"};

            DrawerListIcon = new int[]
                    {R.drawable.ic_stories,
                            R.drawable.ic_resources,
                            R.drawable.be_together,
                            R.drawable.ic_talk_together,
                            R.drawable.ic_setting,
                            R.drawable.about_us,
                            R.drawable.sister_app};

            drawer_adapter = new DrawerListViewAdapter(getApplicationContext(), DrawerListName, DrawerListIcon, mstr_lang);//mCategoriesTitles
                    /*mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                    R.layout.drawer_list_item, mPlanetTitles));*/
            drawer_adapter.notifyDataSetChanged();
            mDrawerList.setAdapter(drawer_adapter);
            mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
            setListViewHeightBasedOnChildren(mDrawerList);
        } else if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.MM_LANG_UNI)) {
            DrawerListName = new String[]
                    {"စိတ္ဓာတ္ခ\u103Cန္အား\u107Fဖည္ ့ရန္", "ဗဟုုသုုတရရန္", "ေပ\u102Bင္းစည္းေဆာင္ရ\u103Cက္ရန္", "ေမး\u107Fမန္းေဆ\u103Cးေ\u108F\u103Cးရန္", "\u107Fပင္ဆင္ရန္", "က\u103C\u103A\u108Fုုပ္တိုု ့အေ\u107Eကာင္း", " Sister Apps"};

            DrawerListIcon = new int[]
                    {R.drawable.ic_stories,
                            R.drawable.ic_resources,
                            R.drawable.be_together,
                            R.drawable.ic_talk_together,
                            R.drawable.ic_setting,
                            R.drawable.about_us,
                            R.drawable.sister_app};

            drawer_adapter = new DrawerListViewAdapter(getApplicationContext(), DrawerListName, DrawerListIcon, mstr_lang);//mCategoriesTitles
                    /*mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                    R.layout.drawer_list_item, mPlanetTitles));*/
            drawer_adapter.notifyDataSetChanged();
            mDrawerList.setAdapter(drawer_adapter);
            mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
            setListViewHeightBasedOnChildren(mDrawerList);
        } else if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.MM_LANG_DEFAULT)) {
            DrawerListName = new String[]
                    {"စိတ္ဓာတ္ခ\u103Cန္အား\u107Fဖည္ ့ရန္", "ဗဟုုသုုတရရန္", "ေပ\u102Bင္းစည္းေဆာင္ရ\u103Cက္ရန္", "ေမး\u107Fမန္းေဆ\u103Cးေ\u108F\u103Cးရန္", "\u107Fပင္ဆင္ရန္", "က\u103C\u103A\u108Fုုပ္တိုု ့အေ\u107Eကာင္း", " Sister Apps"};

            DrawerListIcon = new int[]
                    {R.drawable.ic_stories,
                            R.drawable.ic_resources,
                            R.drawable.be_together,
                            R.drawable.ic_talk_together,
                            R.drawable.ic_setting,
                            R.drawable.about_us,
                            R.drawable.sister_app};

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
                    {"စိတ္ဓာတ္ခ\u103Cန္အား\u107Fဖည္ ့ရန္", "ဗဟုုသုုတရရန္", "ေပ\u102Bင္းစည္းေဆာင္ရ\u103Cက္ရန္", "ေမး\u107Fမန္းေဆ\u103Cးေ\u108F\u103Cးရန္", "\u107Fပင္ဆင္ရန္", "က\u103C\u103A\u108Fုုပ္တိုု ့အေ\u107Eကာင္း", " Sister Apps"};

            DrawerListIcon = new int[]
                    {R.drawable.ic_stories,
                            R.drawable.ic_resources,
                            R.drawable.be_together,
                            R.drawable.ic_talk_together,
                            R.drawable.ic_setting,
                            R.drawable.about_us,
                            R.drawable.sister_app};

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

        TLGUserStoriesRecentFragment tlgUserStoriesRecentFragment = new TLGUserStoriesRecentFragment();

        TalkTogetherCategoryFragment talkTogetherCategoryFragment = new TalkTogetherCategoryFragment();
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
            case 4:

                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                //fragmentManager.beginTransaction().replace(R.id.content_frame, settingsFragment).commit();


                setTitle(DrawerListName[position]);
                break;
            case 5:

                if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.ENG_LANG)){

                    Intent intent2 = new Intent(this, AboutUsWebActivity.class);
                    intent2.putExtra("ActivityName","DrawerMainActivity");
                    intent2.putExtra("URL","file:///android_asset/tos/About-Us-Eng.html");
                    startActivity(intent2);
                }else{
                    Intent intent2 = new Intent(this, AboutUsWebActivity.class);
                    intent2.putExtra("ActivityName","DrawerMainActivity");
                    intent2.putExtra("URL","file:///android_asset/tos/About-Us-MM.html");
                    startActivity(intent2);
                }

                break;
            case 6:
                fragmentManager.beginTransaction().replace(R.id.content_frame, sisterAppFragment).commit();
                setTitle(DrawerListName[position]);

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
    private void getCompetitionQuestion(){
        Log.e("Drawing Game calling===>","===>");
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();
        NetworkEngine.getInstance().getCompetitionQuestion("", user_obj_id, new Callback<CompetitionQuestion>() {

            @Override
            public void failure(RetrofitError arg0) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                if(arg0.getResponse() != null){
                    switch (arg0.getResponse().getStatus()) {
                        case 403:
                            //startActivity(new Intent(getApplicationContext(), GameOverActivity.class));
                            break;
                        case 400:
                            //String error = (String) arg0.getBodyAs(String.class);
                            //SKToastMessage.showMessage(DrawerMainActivity.this, error ,SKToastMessage.ERROR);
                            layout_play_game.setVisibility(View.GONE);
                            break;
                        default:
                            break;
                    }
                }
            }

            @Override
            public void success(final CompetitionQuestion arg0, Response arg1) {
                // TODO Auto-generated method stub
                layout_play_game.setVisibility(View.VISIBLE);
                if(arg0.getCorrectAnswer().size() == 0){
                    btn_play_game.setText(getResources().getString(R.string.competition_play_game));
                    btn_play_game.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            startActivity(new Intent(getApplicationContext(), CompetitionNewGameActivity.class).putExtra("competition_question", new Gson().toJson(arg0)));
                        }
                    });

                }else{
                    btn_play_game.setText(getResources().getString(R.string.competition_discover_winner));
                    btn_play_game.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            startActivity(new Intent(getApplicationContext(), CompetitionWinnerGroupActivity.class).putExtra("competition_question", new Gson().toJson(arg0)));
                        }
                    });

                }
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Log.i("Reviews", "Reviews : here");
        if(user_obj_id != null){
            Review review = StoreUtil.getInstance().selectFrom(user_obj_id+versionCode);
            Log.i("Reviews", "Reviews :"+ review);
            if(review == null && UsageCount > 3){
                showReviewDialog(user_obj_id);
            }else{
                finish();
            }
        }else{
            finish();
        }
    }
}
