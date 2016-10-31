package org.undp_iwomen.iwomen.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.smk.skalertmessage.SKToastMessage;

import org.smk.iwomen.BaseActionBarActivity;
import org.smk.iwomen.TakeAndTourActivity;
import org.undp_iwomen.iwomen.CommonConfig;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.ui.widget.ProgressWheel;
import org.undp_iwomen.iwomen.utils.SharePrefUtils;


public class SplashActivity extends BaseActionBarActivity {
    private static final String LOG_TAG = SplashActivity.class.getSimpleName();
    private static final int PERMISSION_REQUEST = 10002;

    SharePrefUtils sharePrefUtils;

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1000;
    private SharedPreferences mSharedPreferences;
    private ProgressWheel mLoadingProgress;
    //private CustomTextView mNoInternetErrorTextView;
    private boolean isFetching = false;
    private final String STORAGE_WRITE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String STORAGE_READ_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String CAMERA = Manifest.permission.CAMERA;
    private static final String RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    private static final String MODIFY_AUDIO_SETTINGS = Manifest.permission.MODIFY_AUDIO_SETTINGS;
    boolean storagePermissionAccepted = false;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        setContentView(R.layout.activity_splash_img);
        sharePrefUtils = SharePrefUtils.getInstance(this);

        mSharedPreferences = getSharedPreferences(CommonConfig.SHARE_PREFERENCE_USER_INFO, Context.MODE_PRIVATE);
        mLoadingProgress = (ProgressWheel) findViewById(R.id.splash_loading);
        //mNoInternetErrorTextView = (CustomTextView) findViewById(R.id.no_internet_error_loading);

        if (!hasPermission(STORAGE_READ_PERMISSION) || !hasPermission(STORAGE_WRITE_PERMISSION) || !hasPermission(CAMERA) || !hasPermission(RECORD_AUDIO) || !hasPermission(MODIFY_AUDIO_SETTINGS)) {
            requestPermissions(new String[]{STORAGE_READ_PERMISSION, STORAGE_WRITE_PERMISSION, CAMERA, RECORD_AUDIO, MODIFY_AUDIO_SETTINGS}, PERMISSION_REQUEST);
            return;
        }

        doFetching();
    }

    /*@Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                doFetching();
                return true;
        }
        return super.onTouchEvent(event);
    }*/

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
                /*Boolean take_tour = StoreUtil.getInstance().selectFrom("user_guide");
                if(take_tour != null && take_tour){
                    Intent i = new Intent(SplashActivity.this, MainLoginActivity.class);//DrawerMainActivity
                    startActivity(i);
                }else{
                    Intent i = new Intent(SplashActivity.this, TakeAndTourActivity.class);
                    startActivity(i);
                }*/
                Intent i = new Intent(SplashActivity.this, TakeAndTourActivity.class);
                startActivity(i);

                /*Intent i = new Intent(SplashActivity.this, MainLoginActivity.class);//DrawerMainActivity
                startActivity(i);*/
                finish();
            }
        }, splashTimeOut);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doFetching();
                } else {
                    SKToastMessage.showMessage(SplashActivity.this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", SKToastMessage.ERROR);
                }
            }
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
