/**
 * Copyright (c) 2014-present, Facebook, Inc. All rights reserved.
 *
 * You are hereby granted a non-exclusive, worldwide, royalty-free license to use,
 * copy, modify, and distribute this software in source code or binary form for use
 * in connection with the web services and APIs provided by Facebook.
 *
 * As with any software that integrates with the Facebook platform, your use of
 * this software is subject to the Facebook Developer Principles and Policies
 * [http://developers.facebook.com/policy/]. This copyright notice shall be
 * included in all copies or substantial portions of the software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.undp_iwomen.iwomen.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;

import org.smk.iwomen.BaseActionBarActivity;
import org.undp_iwomen.iwomen.CommonConfig;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class MainAccountKitPhoneLoginActivity extends BaseActionBarActivity {
    private static final int FRAMEWORK_REQUEST_CODE = 1;

    private int nextPermissionsRequestCode = 4000;
    private final Map<Integer, OnCompleteListener> permissionsListeners = new HashMap<>();

    //For Phone number store
    private SharedPreferences mSharedPreferencesUserInfo;
    private SharedPreferences.Editor mEditorUserInfo;
    String mphoneNumberString;
    private Context mContext;
    private CustomTextView textViewTitle;
    SharedPreferences sharePrefLanguageUtil;
    String strLang;

    private interface OnCompleteListener {
        void onComplete();
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_kit);
        mContext = getApplicationContext();
        sharePrefLanguageUtil = getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);
        mSharedPreferencesUserInfo = getSharedPreferences(CommonConfig.SHARE_PREFERENCE_USER_INFO, Context.MODE_PRIVATE);
        strLang = sharePrefLanguageUtil.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");

        textViewTitle = (CustomTextView) toolbar.findViewById(R.id.title_action2);


        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if (strLang.equals(Utils.ENG_LANG)) {

            textViewTitle.setText(R.string.phone_number_label);


        } else //FOR ALl MM FONT
        {
            textViewTitle.setText(R.string.phone_number_label);


        }
 /*if (AccountKit.getCurrentAccessToken() != null) {
            startActivity(new Intent(this, TokenActivity.class));
        }*/
    }

    public void onLoginEmail(final View view) {
        onLogin(LoginType.EMAIL);
    }

    public void onLoginPhone(final View view) {
        onLogin(LoginType.PHONE);
    }

    @Override
    protected void onActivityResult(
            final int requestCode,
            final int resultCode,
            final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != FRAMEWORK_REQUEST_CODE) {
            //return;
            Intent i = new Intent(MainAccountKitPhoneLoginActivity.this, RegisterMainActivity.class);//DrawerMainActivity
            startActivity(i);
            finish();
        }
        mEditorUserInfo = mSharedPreferencesUserInfo.edit();


        //mEditorUserInfo.putString(CommonConfig.USER_PH, mobileNoForNrc);
        //Log.e("OnActivity Result","====>" + data.getDataString() + "//" + requestCode + "/"+resultCode);
        //Result: ====>null//1/0
        //03-15 13:48:02.542 26194-26194/? E/OnActivity Ok: ====>+959798254924
        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(final Account account) {
                String accountKitId = account.getId();
                PhoneNumber phoneNumber = account.getPhoneNumber();
                mphoneNumberString = phoneNumber.toString();
                Log.e("OnActivity Ok","====>" + mphoneNumberString);
                if (mphoneNumberString != null && mphoneNumberString != "") {
                    mEditorUserInfo.putString(CommonConfig.USER_PH, mphoneNumberString);
                }
                mEditorUserInfo.commit();

                Bundle bundle = new Bundle();
                bundle.putString(CommonConfig.PHONE_SUCCESS,mphoneNumberString );

                Intent i = new Intent(MainAccountKitPhoneLoginActivity.this, RegisterMainActivity.class);//DrawerMainActivity
                i.putExtra(CommonConfig.PHONE_SUCCESS_BUNDLE,bundle);
                startActivity(i);
                finish();
                // Get email
                //String email = account.getEmail();
            }

            @Override
            public void onError(final AccountKitError error) {
                // Handle Error
                Log.e("OnActivity Errr","====>" + error.toString());
            }
        });


        /*final String toastMessage;

        Log.e("OnActivity Result","====>" + data.getDataString() + "//");
        final AccountKitLoginResult loginResult = AccountKit.loginResultWithIntent(data);
        if (loginResult == null || loginResult.wasCancelled()) {
            toastMessage = "Login Cancelled";
        } else if (loginResult.getError() != null) {
            toastMessage = loginResult.getError().getErrorType().getMessage();
            final Intent intent = new Intent(this, ErrorActivity.class);
            intent.putExtra(ErrorActivity.HELLO_TOKEN_ACTIVITY_ERROR_EXTRA, loginResult.getError());

            startActivity(intent);
        } else {
            final AccessToken accessToken = loginResult.getAccessToken();
            final long tokenRefreshIntervalInSeconds =
                    loginResult.getTokenRefreshIntervalInSeconds();
            if (accessToken != null) {
                toastMessage = "Success:" + accessToken.getAccountId()
                        + tokenRefreshIntervalInSeconds;
                startActivity(new Intent(this, TokenActivity.class));
            } else {
                toastMessage = "Unknown response type";
            }
        }*/

        /*Toast.makeText(
                this,
                toastMessage,
                Toast.LENGTH_LONG)
                .show();*/
    }

    private void onLogin(final LoginType loginType) {
        //Log.e("On Log In Function Ph","==Ph===>");

        final Intent intent = new Intent(this, AccountKitActivity.class);
        final AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder
                = new AccountKitConfiguration.AccountKitConfigurationBuilder(
                loginType,
                AccountKitActivity.ResponseType.TOKEN);
        final AccountKitConfiguration configuration = configurationBuilder.build();
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configuration);
        OnCompleteListener completeListener = new OnCompleteListener() {
            @Override
            public void onComplete() {
                startActivityForResult(intent, FRAMEWORK_REQUEST_CODE);
            }
        };
        switch (loginType) {
            case EMAIL:
                final OnCompleteListener getAccountsCompleteListener = completeListener;
                completeListener = new OnCompleteListener() {
                    @Override
                    public void onComplete() {
                        requestPermissions(
                                Manifest.permission.GET_ACCOUNTS,
                                R.string.permissions_get_accounts_title,
                                R.string.permissions_get_accounts_message,
                                getAccountsCompleteListener);
                    }
                };
                break;
            case PHONE:
                Log.e("On Click Ph","==Ph===>");
                if (configuration.isReceiveSMSEnabled()) {
                    final OnCompleteListener receiveSMSCompleteListener = completeListener;
                    completeListener = new OnCompleteListener() {
                        @Override
                        public void onComplete() {
                            requestPermissions(
                                    Manifest.permission.RECEIVE_SMS,
                                    R.string.permissions_receive_sms_title,
                                    R.string.permissions_receive_sms_message,
                                    receiveSMSCompleteListener);
                        }
                    };
                }
                if (configuration.isReadPhoneStateEnabled()) {
                    final OnCompleteListener readPhoneStateCompleteListener = completeListener;
                    completeListener = new OnCompleteListener() {
                        @Override
                        public void onComplete() {
                            requestPermissions(
                                    Manifest.permission.READ_PHONE_STATE,
                                    R.string.permissions_read_phone_state_title,
                                    R.string.permissions_read_phone_state_message,
                                    readPhoneStateCompleteListener);
                        }
                    };
                }
                break;
        }
        completeListener.onComplete();
    }

    private void requestPermissions(
            final String permission,
            final int rationaleTitleResourceId,
            final int rationaleMessageResourceId,
            final OnCompleteListener listener) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (listener != null) {
                listener.onComplete();
            }
            return;
        }

        checkRequestPermissions(
                permission,
                rationaleTitleResourceId,
                rationaleMessageResourceId,
                listener);
    }

    @TargetApi(23)
    private void checkRequestPermissions(
            final String permission,
            final int rationaleTitleResourceId,
            final int rationaleMessageResourceId,
            final OnCompleteListener listener) {
        if (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
            if (listener != null) {
                listener.onComplete();
            }
            return;
        }

        final int requestCode = nextPermissionsRequestCode++;
        permissionsListeners.put(requestCode, listener);

        if (shouldShowRequestPermissionRationale(permission)) {
            new AlertDialog.Builder(this)
                    .setTitle(rationaleTitleResourceId)
                    .setMessage(rationaleMessageResourceId)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, final int which) {
                            requestPermissions(new String[] { permission }, requestCode);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, final int which) {
                            // ignore and clean up the listener
                            permissionsListeners.remove(requestCode);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            requestPermissions(new String[]{ permission }, requestCode);
        }
    }

    @TargetApi(23)
    @SuppressWarnings("unused")
    @Override
    public void onRequestPermissionsResult(final int requestCode,
                                           final @NonNull String permissions[],
                                           final @NonNull int[] grantResults) {
        final OnCompleteListener permissionsListener = permissionsListeners.remove(requestCode);
        if (permissionsListener != null
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            permissionsListener.onComplete();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        /*
        //When click on back , out from Activity
        if (item.getItemId() == android.R.id.home) {

            finish();
            return true;


        }*/


        return super.onOptionsItemSelected(item);
    }
}
