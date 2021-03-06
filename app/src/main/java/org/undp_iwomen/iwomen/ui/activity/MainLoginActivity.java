package org.undp_iwomen.iwomen.ui.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.smk.skalertmessage.SKToastMessage;
import com.thuongnh.zprogresshud.ZProgressHUD;

import org.smk.clientapi.NetworkEngine;
import org.smk.iwomen.BaseActionBarActivity;
import org.smk.iwomen.ResponseError;
import org.smk.model.User;
import org.undp_iwomen.iwomen.CommonConfig;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.model.MyTypeFace;
import org.undp_iwomen.iwomen.ui.widget.CustomRadioButton;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.utils.Connection;
import org.undp_iwomen.iwomen.utils.StoreUtil;
import org.undp_iwomen.iwomen.utils.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainLoginActivity extends BaseActionBarActivity implements View.OnClickListener {

    private RadioGroup radioGroup;
    private SharedPreferences mSharedPreferencesUserInfo;
    private SharedPreferences.Editor mEditorUserInfo;
    private SharedPreferences sharePrefLanguageUtil;

    private ImageView appLogo;
    private EditText usernameField;
    private EditText passwordField;
    private TextInputLayout mUserNameTextInputLayout;
    private TextInputLayout mPwdTextInputLayout;

    private Button parseLoginButton;
    private Button parseSignupButton;
    private CheckBox chkShowPwd;

    private TextView txtChangLanEng, txtChangLanMM;
    private Button btnOk;
    private CustomRadioButton rd_lang_en, rd_lang_mm_zawgyi, rd_lang_mm_uni, rd_lang_mm_default;
    private String lang;
    private ZProgressHUD dialog;
    //PERMISSION FOR WRITE STORAGE SETTING FONTS (Marshmallow Android 6.0 and above)
    private final String STORAGE_READ_PERMISSION = "android.permission.READ_EXTERNAL_STORAGE";
    boolean storagePermissionAccepted = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_fragment);
        sharePrefLanguageUtil = getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);
        mSharedPreferencesUserInfo = getSharedPreferences(CommonConfig.SHARE_PREFERENCE_USER_INFO, Context.MODE_PRIVATE);

        CustomTextView textView1 = new CustomTextView(this);
        EditText editText = new EditText(this);

        //printHashKey();

        appLogo = (ImageView) findViewById(R.id.app_logo);
        usernameField = (EditText) findViewById(R.id.login_username_input);
        passwordField = (EditText) findViewById(R.id.login_password_input);
        mUserNameTextInputLayout = (TextInputLayout) findViewById(R.id.login_user_name_input_ly);
        mPwdTextInputLayout = (TextInputLayout) findViewById(R.id.login_user_pwd_input_ly);
        chkShowPwd = (CheckBox) findViewById(R.id.login_showpwd);

        parseLoginButton = (Button) findViewById(R.id.parse_login_button);
        parseSignupButton = (Button) findViewById(R.id.parse_signup_button);

        txtChangLanEng = (TextView) findViewById(R.id.login_change_lan_eng);
        txtChangLanMM = (TextView) findViewById(R.id.login_change_lan_mm);

        lang = sharePrefLanguageUtil.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);

        Log.e("MainLogin==","<<Lang>>"+ lang);
        if (lang.equals(Utils.ENG_LANG)) {

            String languageToLoad = "eng"; // your language
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

            setEnglishFont();
        } else if (lang.equals(Utils.MM_LANG)) {

            String languageToLoadm = "mm"; // your language
            Locale localem = new Locale(languageToLoadm);
            Locale.setDefault(localem);
            Configuration configm = new Configuration();
            configm.locale = localem;
            getBaseContext().getResources().updateConfiguration(configm, getBaseContext().getResources().getDisplayMetrics());


            setMyanmarFontZawgyi();
        } else if (lang.equals(Utils.MM_LANG_UNI)) {

            String languageToLoadm = "mm"; // your language
            Locale localem = new Locale(languageToLoadm);
            Locale.setDefault(localem);
            Configuration configm = new Configuration();
            configm.locale = localem;
            getBaseContext().getResources().updateConfiguration(configm, getBaseContext().getResources().getDisplayMetrics());

            setMyanmarFontUni();
        } else if (lang.equals(Utils.MM_LANG_DEFAULT)) {
            setMyanmarFontDefault();
        }

        //TODO CHECK LOGIN OR NOT
        if (mSharedPreferencesUserInfo.getString(CommonConfig.USER_ROLE, null) != null) {
            startActivity(new Intent(this, DrawerMainActivity.class));
            finish();
        }

        ShowLangDialog();
        ((CheckBox) findViewById(R.id.login_showpwd)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                passwordField.setInputType(!isChecked ? InputType.TYPE_TEXT_VARIATION_PASSWORD : InputType.TYPE_CLASS_TEXT);
                passwordField.setTransformationMethod(!isChecked ? PasswordTransformationMethod.getInstance() : null);
            }
        });

        txtChangLanEng.setOnClickListener(this);
        txtChangLanMM.setOnClickListener(this);
        parseLoginButton.setOnClickListener(this);
        parseSignupButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.parse_login_button:
                postLogin();
                break;
            case R.id.login_change_lan_eng:
                ShowLangDialog();
                break;
            case R.id.login_change_lan_mm:
                ShowLangDialog();
                break;

            case R.id.parse_signup_button:
                Intent i = new Intent(MainLoginActivity.this, RegisterMainActivity.class);//DrawerMainActivity
                startActivity(i);
                //finish();
                break;
        }
    }

    public void printHashKey() {
        try {

            PackageInfo info = getPackageManager().getPackageInfo("org.undp_iwomen.iwomen", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {

                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("TEMPTAGHASH KEY:","FB HashKey : "+ Base64.encodeToString(md.digest(), Base64.DEFAULT));

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {

        }
    }

    public void postLogin() {

        if (Connection.isOnline(getApplicationContext())) {
            final String username = usernameField.getText().toString().trim();
            final String pwd = passwordField.getText().toString().trim();
            if (TextUtils.isEmpty(username)) {
                mUserNameTextInputLayout.setError(getResources().getString(R.string.your_name_error));

                if (lang.equals(Utils.ENG_LANG)) {
                    Utils.doToastEng(getApplicationContext(), getResources().getString(R.string.your_name_error));
                } else if (lang.equals(Utils.MM_LANG)) {

                    Utils.doToastMM(getApplicationContext(), getResources().getString(R.string.your_name_error_mm));
                }

                return;
            } else {
                mUserNameTextInputLayout.setErrorEnabled(false);
            }

            if (TextUtils.isEmpty(pwd)) {
                mPwdTextInputLayout.setError(getResources().getString(R.string.confirm_password_error));

                if (lang.equals(Utils.ENG_LANG)) {
                    //doToast(getResources().getString(R.string.confirm_password_error));
                    Utils.doToastEng(getApplicationContext(), getResources().getString(R.string.password_error));
                } else if (lang.equals(Utils.MM_LANG)) {

                    Utils.doToastMM(getApplicationContext(), getResources().getString(R.string.password_error));
                }
                return;
            } else {
                mPwdTextInputLayout.setErrorEnabled(false);
            }


            dialog = new ZProgressHUD(MainLoginActivity.this);
            dialog.show();
            NetworkEngine.getInstance().postLogin(username, pwd, new Callback<User>() {
                @Override
                public void success(User user, Response response) {
                    Log.e("<<< >>> ", "===>" + user.toString() +"/" + dialog);
                    if (dialog != null && dialog.isShowing()){
                        dialog.dismissWithSuccess();
                       }

                    mEditorUserInfo = mSharedPreferencesUserInfo.edit();

                    mEditorUserInfo.putString(CommonConfig.USER_EMAIL, user.getEmail());
                    mEditorUserInfo.putString(CommonConfig.USER_NAME, user.getUsername());
                    mEditorUserInfo.putString(CommonConfig.USER_ID, user.getId().toString());
                    mEditorUserInfo.putString(CommonConfig.USER_OBJ_ID, user.getObjectId());
                    mEditorUserInfo.putString(CommonConfig.USER_ROLE, user.getRole());
                    mEditorUserInfo.putString(CommonConfig.USER_UPLOAD_IMG_URL, user.getProfileimage());
                    mEditorUserInfo.putString(CommonConfig.USER_PH, user.getPhoneNo());
                    mEditorUserInfo.putString(CommonConfig.USER_ROLE,user.getRole() );

                    if( user.getPoints()!= null){
                        mEditorUserInfo.putString(CommonConfig.USER_POINTS,user.getPoints().toString() );

                    }else{
                        mEditorUserInfo.putString(CommonConfig.USER_POINTS,"0" );

                    }
                    if( user.getShared()!= null){
                        mEditorUserInfo.putString(CommonConfig.USER_SHARE_STATUS,user.getShared().toString() );

                    }else{
                        mEditorUserInfo.putString(CommonConfig.USER_SHARE_STATUS,"0");

                    }

                    mEditorUserInfo.commit();
                    Intent i = new Intent(MainLoginActivity.this, DrawerMainActivity.class);//DrawerMainActivity
                    startActivity(i);
                    finish();

                }

                @Override
                public void failure(RetrofitError arg0) {

                    if(arg0.getResponse() != null){
                        switch (arg0.getResponse().getStatus()) {
                            case 400:
                                try {
                                    ResponseError error = (ResponseError) arg0.getBodyAs(ResponseError.class);
                                    if (lang.equals(Utils.ENG_LANG)) {
                                        SKToastMessage.showMessage(MainLoginActivity.this, error.getError(), SKToastMessage.ERROR);
                                    } else if (lang.equals(Utils.MM_LANG)) {
                                        SKToastMessage.showMessage(MainLoginActivity.this, error.getErrorMm(), SKToastMessage.ERROR);
                                    }
                                }catch (Exception e){

                                }
                                break;
                            default:
                                break;
                        }
                    }

                    dialog.dismissWithFailure();

                }
            });

        } else {
            //SKConnectionDetector.getInstance(this).showErrorMessage();
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void ShowLangDialog() {

        lang = sharePrefLanguageUtil.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);
        //Toast.makeText(MainLoginActivity.this, "Choose Lang" + lang, Toast.LENGTH_LONG).show();

        OnClickListener clickListener = new OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogPlus dialog, View view) {

                SharedPreferences.Editor editor = sharePrefLanguageUtil.edit();

                switch (view.getId()) {

                    case R.id.dialog_english_language:

                        if (!hasPermission(STORAGE_READ_PERMISSION)) {

                            //if no permission, request permission
                            String[] perms = {STORAGE_READ_PERMISSION};
                            int permsRequestCode = 200;
                            requestPermissions(perms, permsRequestCode);

                        } else {


                            StoreUtil.getInstance().saveTo("fonts", "english");
                            editor.putString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG_FONT);
                            editor.commit();

                            String languageToLoad = "eng"; // your language
                            Locale locale = new Locale(languageToLoad);
                            Locale.setDefault(locale);
                            Configuration config = new Configuration();
                            config.locale = locale;
                            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());


                            SharedPreferences.Editor fontEditor = getSharedPreferences(Utils.PREF_SETTING, Activity.MODE_PRIVATE).edit();
                            fontEditor.putString(Utils.PREF_SETTING_LANG, "eng");
                            fontEditor.commit();

                            setEnglishFont();
                        }
                        break;
                    case R.id.dialog_mm_zawgyi_language:

                        if (!hasPermission(STORAGE_READ_PERMISSION)) {

                            //if no permission, request permission
                            String[] perms = {STORAGE_READ_PERMISSION};
                            int permsRequestCode = 200;
                            requestPermissions(perms, permsRequestCode);

                        } else {
                            StoreUtil.getInstance().saveTo("fonts", "zawgyione");
                            editor.putString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG_FONT);
                            editor.commit();

                            String languageToLoadm = "mm"; // your language
                            Locale localem = new Locale(languageToLoadm);
                            Locale.setDefault(localem);
                            Configuration configm = new Configuration();
                            configm.locale = localem;
                            getBaseContext().getResources().updateConfiguration(configm, getBaseContext().getResources().getDisplayMetrics());

                            SharedPreferences.Editor fontEditorMM = getSharedPreferences(Utils.PREF_SETTING, Activity.MODE_PRIVATE).edit();
                            fontEditorMM.putString(Utils.PREF_SETTING_LANG, "mm");
                            fontEditorMM.commit();

                            setMyanmarFontZawgyi();
                        }
                        break;
                    case R.id.dialog_mm_unicode_language:

                        if (!hasPermission(STORAGE_READ_PERMISSION)) {

                            //if no permission, request permission
                            String[] perms = {STORAGE_READ_PERMISSION};
                            int permsRequestCode = 200;
                            requestPermissions(perms, permsRequestCode);

                        } else {
                            StoreUtil.getInstance().saveTo("fonts", "myanmar3");
                            editor.putString(Utils.PREF_SETTING_LANG, Utils.MM3_LANG_FONT);
                            editor.commit();

                            String languageToLoaduni = "mm"; // your language
                            Locale localeuni = new Locale(languageToLoaduni);
                            Locale.setDefault(localeuni);
                            Configuration configuni = new Configuration();
                            configuni.locale = localeuni;
                            getBaseContext().getResources().updateConfiguration(configuni, getBaseContext().getResources().getDisplayMetrics());

                            SharedPreferences.Editor fontEditorUni = getSharedPreferences(Utils.PREF_SETTING, Activity.MODE_PRIVATE).edit();
                            fontEditorUni.putString(Utils.PREF_SETTING_LANG, "mm");
                            fontEditorUni.commit();

                            setMyanmarFontUni();
                        }
                        break;


                    case R.id.dialog_mm_default_language:
                        if (!hasPermission(STORAGE_READ_PERMISSION)) {

                            //if no permission, request permission
                            String[] perms = {STORAGE_READ_PERMISSION};
                            int permsRequestCode = 200;
                            requestPermissions(perms, permsRequestCode);

                        } else {
                            StoreUtil.getInstance().saveTo("fonts", "default");
                            editor.putString(Utils.PREF_SETTING_LANG, Utils.MM_DEFAULT_FONT);
                            editor.commit();

                            String languageToLoadDe = "mm"; // your language
                            Locale localeDe = new Locale(languageToLoadDe);
                            Locale.setDefault(localeDe);
                            Configuration configDe = new Configuration();
                            configDe.locale = localeDe;
                            getBaseContext().getResources().updateConfiguration(configDe, getBaseContext().getResources().getDisplayMetrics());

                            SharedPreferences.Editor fontEditorDe = getSharedPreferences(Utils.PREF_SETTING, Activity.MODE_PRIVATE).edit();
                            fontEditorDe.putString(Utils.PREF_SETTING_LANG, "mm");
                            fontEditorDe.commit();

                            setMyanmarFontDefault();
                        }

                        break;
                    case R.id.dialog_ok_btn:
                        if (lang.equals(Utils.ENG_LANG)) {
                            setEnglishFont();
                        } else if (lang.equals(Utils.MM_LANG)) {
                            setMyanmarFontZawgyi();
                        } else if (lang.equals(Utils.MM_LANG_UNI)) {
                            setMyanmarFontUni();
                        } else if (lang.equals(Utils.MM_LANG_DEFAULT)) {

                            setMyanmarFontDefault();
                        }


                        break;
                }
                dialog.dismiss();
            }
        };
        OnItemClickListener itemClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                TextView textView = (TextView) view.findViewById(R.id.dialog_language_setting_title);
                String clickedAppName = textView.getText().toString();
                dialog.dismiss();
                Toast.makeText(MainLoginActivity.this, clickedAppName + "Item clicked", Toast.LENGTH_LONG).show();


                radioGroup = (RadioGroup) view.findViewById(R.id.dialog_language);
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        // checkedId is the RadioButton selected
                        Toast.makeText(MainLoginActivity.this, "Radio button check" + " " + checkedId, Toast.LENGTH_LONG).show();

                    }
                });

            }
        };
        final DialogPlus dialog = DialogPlus.newDialog(this)
                .setContentHolder(new ViewHolder(R.layout.activity_lang_custom_dialog))
                        //.setHeader(R.layout.header)
                        //.setFooter(R.layout.footer)
                .setCancelable(true)
                .setGravity(Gravity.CENTER)
                .setPadding(16, 0, 16, 8)
                .setOutAnimation(R.anim.fade_out_center)
                        //setPadding(left, top, right, bottom)

                        //.setAdapter(adapter)
                        //.setAdapter(adapter)
                        //.setContentWidth(800)
                        //.setContentHeight(1100)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setOnClickListener(clickListener)
                .setOnItemClickListener(itemClickListener)


                        //.setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
                .create();
        dialog.show();

        View view = dialog.getHolderView();
        rd_lang_en = (CustomRadioButton) view.findViewById(R.id.dialog_english_language);
        rd_lang_mm_zawgyi = (CustomRadioButton) view.findViewById(R.id.dialog_mm_zawgyi_language);
        rd_lang_mm_uni = (CustomRadioButton) view.findViewById(R.id.dialog_mm_unicode_language);
        rd_lang_mm_default = (CustomRadioButton) view.findViewById(R.id.dialog_mm_default_language);
        if (lang.equals(Utils.ENG_LANG)) {
            rd_lang_en.setChecked(true);

            String languageToLoad = "eng"; // your language
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

            setEnglishFont();
        } else if (lang.equals(Utils.MM_LANG)) {
            rd_lang_mm_zawgyi.setChecked(true);

            String languageToLoadm = "mm"; // your language
            Locale localem = new Locale(languageToLoadm);
            Locale.setDefault(localem);
            Configuration configm = new Configuration();
            configm.locale = localem;
            getBaseContext().getResources().updateConfiguration(configm, getBaseContext().getResources().getDisplayMetrics());


            setMyanmarFontZawgyi();
        } else if (lang.equals(Utils.MM_LANG_UNI)) {
            rd_lang_mm_uni.setChecked(true);

            String languageToLoadm = "mm"; // your language
            Locale localem = new Locale(languageToLoadm);
            Locale.setDefault(localem);
            Configuration configm = new Configuration();
            configm.locale = localem;
            getBaseContext().getResources().updateConfiguration(configm, getBaseContext().getResources().getDisplayMetrics());

            setMyanmarFontUni();
        } else if (lang.equals(Utils.MM_LANG_DEFAULT)) {
            rd_lang_mm_default.setChecked(true);
            setMyanmarFontDefault();
        }


    }

    public void setEnglishFont() {
        appLogo.setImageResource(R.drawable.logo_eng);
        usernameField.setHint(getResources().getString(R.string.com_parse_ui_name_input_hint));
        passwordField.setHint(getResources().getString(R.string.com_parse_ui_password_input_hint));

        parseLoginButton.setText(getResources().getString(R.string.com_parse_ui_parse_login_button_label));
        parseSignupButton.setText(getResources().getString(R.string.com_parse_ui_create_account_button_label));

        chkShowPwd.setText(getResources().getString(R.string.com_parse_ui_show_pwd_eng));
        chkShowPwd.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.NORMAL));


        txtChangLanEng.setText(getResources().getString(R.string.login_language_eng));
        txtChangLanMM.setText(getResources().getString(R.string.login_language_mm));

    }

    public void setMyanmarFontUni() {
        appLogo.setImageResource(R.drawable.logo_mm);
        usernameField.setHint(getResources().getString(R.string.com_parse_ui_username_input_hint_mm));
        passwordField.setHint(getResources().getString(R.string.com_parse_ui_password_input_hint_mm));
        parseLoginButton.setText(getResources().getString(R.string.com_parse_ui_parse_login_button_label_mm));

        parseSignupButton.setText(getResources().getString(R.string.com_parse_ui_parse_signup_button_label_mm));

        chkShowPwd.setText(getResources().getString(R.string.com_parse_ui_show_pwd_mm));
        chkShowPwd.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.UNI));

        txtChangLanEng.setText(getResources().getString(R.string.login_language_eng));
        txtChangLanMM.setText(getResources().getString(R.string.login_language_mm));

        usernameField.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.UNI));
        passwordField.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.UNI));
        chkShowPwd.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.UNI));
        parseSignupButton.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.UNI));
        parseLoginButton.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.UNI));
        txtChangLanMM.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.UNI));
    }

    public void setMyanmarFontZawgyi() {
        appLogo.setImageResource(R.drawable.logo_mm);
        usernameField.setHint(getResources().getString(R.string.com_parse_ui_username_input_hint_mm));
        passwordField.setHint(getResources().getString(R.string.com_parse_ui_password_input_hint_mm));
        parseLoginButton.setText(getResources().getString(R.string.com_parse_ui_parse_login_button_label_mm));

        parseSignupButton.setText(getResources().getString(R.string.com_parse_ui_parse_signup_button_label_mm));
        chkShowPwd.setText(getResources().getString(R.string.com_parse_ui_show_pwd_mm));


        txtChangLanEng.setText(getResources().getString(R.string.login_language_eng));
        txtChangLanMM.setText(getResources().getString(R.string.login_language_mm));

        usernameField.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));
        passwordField.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));
        chkShowPwd.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));
        parseSignupButton.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));
        parseLoginButton.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));
        txtChangLanMM.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));


    }

    public void setMyanmarFontDefault() {

        /******In the Default we don't need to give TypeFace ********/
        appLogo.setImageResource(R.drawable.logo_mm);
        usernameField.setHint(getResources().getString(R.string.com_parse_ui_username_input_hint_mm));
        passwordField.setHint(getResources().getString(R.string.com_parse_ui_password_input_hint_mm));
        parseLoginButton.setText(getResources().getString(R.string.com_parse_ui_parse_login_button_label_mm));

        parseSignupButton.setText(getResources().getString(R.string.com_parse_ui_parse_signup_button_label_mm));
        chkShowPwd.setText(getResources().getString(R.string.com_parse_ui_show_pwd_mm));


        txtChangLanEng.setText(getResources().getString(R.string.login_language_eng));
        txtChangLanMM.setText(getResources().getString(R.string.login_language_mm));


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {


            case 200:
                storagePermissionAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (storagePermissionAccepted) {
                    //chooseImage();
                    ShowLangDialog();
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
