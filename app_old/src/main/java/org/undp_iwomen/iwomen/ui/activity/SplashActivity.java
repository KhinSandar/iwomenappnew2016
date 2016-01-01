package org.undp_iwomen.iwomen.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.widget.TextView;

import com.parse.CommonConfig;
import com.smk.iwomen.BaseActionBarActivity;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.ui.widget.ProgressWheel;
import org.undp_iwomen.iwomen.utils.SharePrefUtils;


public class SplashActivity extends BaseActionBarActivity {
    private static final String LOG_TAG = SplashActivity.class.getSimpleName();

    SharePrefUtils sharePrefUtils;

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1000;
    private SharedPreferences mSharedPreferences;
    private ProgressWheel mLoadingProgress;
    private TextView mNoInternetErrorTextView;
    private boolean isFetching = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        setContentView(R.layout.activity_splash_img);
        sharePrefUtils = SharePrefUtils.getInstance(this);

        mSharedPreferences = getSharedPreferences(CommonConfig.SHARE_PREFERENCE_USER_INFO, Context.MODE_PRIVATE);
        mLoadingProgress = (ProgressWheel) findViewById(R.id.splash_loading);
        mNoInternetErrorTextView = (TextView) findViewById(R.id.no_internet_error_loading);

        doFetching();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                doFetching();
                return true;
        }
        return super.onTouchEvent(event);
    }

    private void doFetching() {

        if (sharePrefUtils.isFirstTime()) {


            /*new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mLoadingProgress.setVisibility(View.GONE);

                    mNoInternetErrorTextView.setText(getString(R.string.open_internet_warning_eng));
                    mNoInternetErrorTextView.setVisibility(View.VISIBLE);
                }
            }, 1000);*/

            sharePrefUtils.setFirstTime(false);
            startNextActivity(SPLASH_TIME_OUT);

            /*if (Connection.isOnline(this)) {
                *//*if(!isFetching) {
                    fetchCity();
                    sharePrefUtils.setFirstTime(false);
                        startNextActivity(0);
                }*//*
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mLoadingProgress.setVisibility(View.GONE);

                        mNoInternetErrorTextView.setText(getString(R.string.open_internet_warning_eng));
                        mNoInternetErrorTextView.setVisibility(View.VISIBLE);
                    }
                }, 1000);
            }*/
        } else {
            // everything is ok, start next activity directly
            //sharePrefUtils.setFirstTime(false);
            startNextActivity(SPLASH_TIME_OUT);
        }
    }

    /*private void fetchCity() {
        mNoInternetErrorTextView.setText("");
        mLoadingProgress.setVisibility(View.VISIBLE);
        isFetching = true;
        ParseQuery<City> query = City.getQuery();
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<City>() {
            @Override
            public void done(List<City> cities, ParseException e) {

                if (e == null) {

                    //Content Provider save
                    Uri uri = saveCityInLocal(cities);

                    if (uri.getLastPathSegment() != null) {
                        sharePrefUtils.setFirstTime(false);
                        startNextActivity(0);
                    } else {
                        Toast.makeText(SplashActivity.this, "Error: " + e, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    sharePrefUtils.setFirstTime(true);
                    mLoadingProgress.setVisibility(View.GONE);

                    mNoInternetErrorTextView = (TextView) findViewById(R.id.no_internet_error_loading);
                    mNoInternetErrorTextView.setText(getString(R.string.network_connection_error));
                    mNoInternetErrorTextView.setVisibility(View.VISIBLE);
                }
                isFetching = false;
            }
        });

    }*/

    /*private Uri saveCityInLocal(List<City> cityList) {

        Uri uri = null;

        for (City city : cityList) {
            ContentValues cv = new ContentValues();
            cv.put(TableAndColumnsName.ParseUtil.PARSE_ID, city.getObjectId());
            cv.put(TableAndColumnsName.CityTableUtil.CITY_NAME_COLM, city.getCityName());
            cv.put(TableAndColumnsName.CityTableUtil.CITY_NAME_MM_COLM, city.getCityMyanmarName());
            cv.put(TableAndColumnsName.CityTableUtil.DESCRIPTION, city.getCityDescription());

            uri = getContentResolver().insert(GoBusProviderData.CityProvider.CONTETN_URI, cv);
        }
        return uri;
    }*/

    /*private boolean isCityDataExisted() {
        try {
            Cursor cursor = getContentResolver().query(GoBusProviderData.CityProvider.CONTETN_URI, null, null, null, null);
            if (cursor.getCount() > 0) {
                return true;
            } else {
                return false;
            }
        }catch (Exception e){
            SqliteHelper dbHelper = new SqliteHelper(this);
            dbHelper.getReadableDatabase();
            return false;
        }
    }*/

    private void startNextActivity(int splashTimeOut) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, DrawerMainActivity.class);
                startActivity(i);
                finish();
            }
        }, splashTimeOut);
    }
}
