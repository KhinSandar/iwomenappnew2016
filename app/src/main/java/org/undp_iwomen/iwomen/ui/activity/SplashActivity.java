package org.undp_iwomen.iwomen.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import org.smk.application.StoreUtil;
import org.smk.iwomen.BaseActionBarActivity;
import org.smk.iwomen.TakeAndTourActivity;
import org.undp_iwomen.iwomen.CommonConfig;
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
    //private CustomTextView mNoInternetErrorTextView;
    private boolean isFetching = false;
    private final String STORAGE_READ_PERMISSION = "android.permission.READ_EXTERNAL_STORAGE";
    boolean storagePermissionAccepted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        setContentView(R.layout.activity_splash_img);
        sharePrefUtils = SharePrefUtils.getInstance(this);

        mSharedPreferences = getSharedPreferences(CommonConfig.SHARE_PREFERENCE_USER_INFO, Context.MODE_PRIVATE);
        mLoadingProgress = (ProgressWheel) findViewById(R.id.splash_loading);
        //mNoInternetErrorTextView = (CustomTextView) findViewById(R.id.no_internet_error_loading);

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



            sharePrefUtils.setFirstTime(false);
            startNextActivity(SPLASH_TIME_OUT);


        } else {
            // everything is ok, start next activity directly
            //sharePrefUtils.setFirstTime(false);
            startNextActivity(SPLASH_TIME_OUT);
        }
    }


    private void startNextActivity(int splashTimeOut) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Boolean take_tour = StoreUtil.getInstance().selectFrom("user_guide");
                if(take_tour != null && take_tour){
                    Intent i = new Intent(SplashActivity.this, MainLoginActivity.class);//DrawerMainActivity
                    startActivity(i);
                }else{
                    Intent i = new Intent(SplashActivity.this, TakeAndTourActivity.class);
                    startActivity(i);
                }

                /*Intent i = new Intent(SplashActivity.this, MainLoginActivity.class);//DrawerMainActivity
                startActivity(i);*/
                finish();
            }
        }, splashTimeOut);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {


            case 200:
                storagePermissionAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (storagePermissionAccepted) {
                    //chooseImage();
                    //ShowLangDialog();
                }
                break;

        }
    }

    private boolean hasPermission(String permission) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
        } else {
            return true;
        }


    }
}
