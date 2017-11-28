package org.undp_iwomen.iwomen.ui.fragment;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import com.smk.skalertmessage.SKToastMessage;

import org.undp_iwomen.iwomen.CommonConfig;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.model.FontConverter;
import org.undp_iwomen.iwomen.model.MyTypeFace;
import org.undp_iwomen.iwomen.ui.activity.SettingActivity;
import org.undp_iwomen.iwomen.ui.widget.CustomRadioButton;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.utils.StoreUtil;
import org.undp_iwomen.iwomen.utils.Utils;

import java.util.Locale;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * Created by khinsandar on 8/7/15.
 */
public class SettingsFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {

    private RadioGroup radioLanguageGroup;
    private RadioButton radioLanguageButton;

    CustomRadioButton rd_lang_en, rd_lang_mm_zawgyi, rd_lang_mm_uni, rd_lang_mm_default, rd_shan, rd_mon;
    SharedPreferences sharePrefLanguageUtil;

    private SharedPreferences mSharedPreferencesUserInfo;
    private SharedPreferences.Editor mEditorUserInfo;

    private Context mContext;
    //private CheckBox chk_settings_getnotification;
    private Switch sw_noti;
    private CustomTextView settings_language_setting_title;
    private CustomTextView settings_changeTheme;
    RadioButton color_blue, color_pink, color_yellow;

    //Permission for file storage
    private final String STORAGE_READ_PERMISSION = "android.permission.READ_EXTERNAL_STORAGE";
    boolean storagePermissionAccepted = false;

    public void SettingsFragment(Context context) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        init(view);
        return view;
    }

    private void init(View rootView) {


        mContext = getActivity().getApplicationContext();


        sharePrefLanguageUtil = getActivity().getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);
        mSharedPreferencesUserInfo = getActivity().getSharedPreferences(CommonConfig.SHARE_PREFERENCE_USER_INFO, Context.MODE_PRIVATE);

        radioLanguageGroup = (RadioGroup) rootView.findViewById(R.id.settings_language);
        rd_lang_en = (CustomRadioButton) rootView.findViewById(R.id.settings_english_language);
        rd_lang_mm_zawgyi = (CustomRadioButton) rootView.findViewById(R.id.settings_mm_zawgyi_language);
        rd_lang_mm_uni = (CustomRadioButton) rootView.findViewById(R.id.settings_mm_unicode_language);
        rd_lang_mm_default = (CustomRadioButton) rootView.findViewById(R.id.settings_mm_default_language);
        rd_shan = (CustomRadioButton) rootView.findViewById(R.id.settings_ethic_shan_language);
        rd_mon = (CustomRadioButton) rootView.findViewById(R.id.settings_ethic_mon_language);
        settings_language_setting_title = (CustomTextView) rootView.findViewById(R.id.settings_language_setting_title);
        settings_changeTheme = (CustomTextView) rootView.findViewById(R.id.settings_changeTheme);
        //chk_settings_getnotification = (CheckBox)rootView.findViewById(R.id.settings_getnotification);

        sw_noti = (Switch) rootView.findViewById(R.id.setting_sw);
        color_blue = (RadioButton) rootView.findViewById(R.id.setting_color_blue);
        color_pink = (RadioButton) rootView.findViewById(R.id.setting_color_pink);
        color_yellow = (RadioButton) rootView.findViewById(R.id.setting_color_yellow);

        String lang = sharePrefLanguageUtil.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);
        rd_lang_en.setOnCheckedChangeListener(this);
        rd_lang_mm_zawgyi.setOnCheckedChangeListener(this);
        rd_lang_mm_uni.setOnCheckedChangeListener(this);
        rd_lang_mm_default.setOnCheckedChangeListener(this);
        rd_shan.setOnCheckedChangeListener(this);
        rd_mon.setOnCheckedChangeListener(this);

        if (lang.equals(Utils.ENG_LANG)) {
            rd_lang_en.setChecked(true);
            setEnglishFont();
        } else if (lang.equals(Utils.MM_LANG)) {
            rd_lang_mm_zawgyi.setChecked(true);
            setMyanmarFont();
        } else if (lang.equals(Utils.MM_LANG_UNI)) {
            rd_lang_mm_uni.setChecked(true);
            setMyanmarFontUni();
        } else if (lang.equals(Utils.MM_LANG_DEFAULT)) {
            rd_lang_mm_default.setChecked(true);
            setMyanmarFontDefault();
        }

        //setSelectedTheme();
        //sw_noti.setChecked(true);

        boolean willShowNoti = mSharedPreferencesUserInfo.getBoolean(CommonConfig.WILL_SHOW_NOTIFICATION, true);

        if (!willShowNoti) {
            /*Random random = new Random();
            int m = random.nextInt(9999 - 1000) + 1000;
            if (Context.NOTIFICATION_SERVICE != null) {
                String NS = Context.NOTIFICATION_SERVICE;
                NotificationManager nMgr = (NotificationManager) getApplicationContext()
                        .getSystemService(NS);
                nMgr.cancel(m);
            }*//*

            if (NOTIFICATION_SERVICE != null) {
                NotificationManager nMgr = (NotificationManager) getApplicationContext()
                        .getSystemService(NOTIFICATION_SERVICE);
                nMgr.cancelAll();
            }*/

            sw_noti.setChecked(false);

        } else {
            sw_noti.setChecked(true);
        }
        mEditorUserInfo = mSharedPreferencesUserInfo.edit();

        sw_noti.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    //switchStatus.setText("Switch is currently ON");

                    mEditorUserInfo.putBoolean(CommonConfig.WILL_SHOW_NOTIFICATION, true);
                    mEditorUserInfo.commit();
                    //Toast.makeText(getContext(),"Open",Toast.LENGTH_SHORT).show();


                } else {

                    if (NOTIFICATION_SERVICE != null) {
                        NotificationManager nMgr = (NotificationManager) getApplicationContext()
                                .getSystemService(NOTIFICATION_SERVICE);
                        nMgr.cancelAll();
                    }

                    mEditorUserInfo.putBoolean(CommonConfig.WILL_SHOW_NOTIFICATION, false);
                    mEditorUserInfo.commit();

                    //Toast.makeText(getContext(),"Close",Toast.LENGTH_SHORT).show();

                }
            }
        });
        //check the current state before we display the screen
        if (sw_noti.isChecked()) {
            //switchStatus.setText("Switch is currently ON");
            mEditorUserInfo.putBoolean(CommonConfig.WILL_SHOW_NOTIFICATION, true);
            mEditorUserInfo.commit();
            //Toast.makeText(getContext(),"before open",Toast.LENGTH_SHORT).show();

        } else {

            if (NOTIFICATION_SERVICE != null) {
                NotificationManager nMgr = (NotificationManager) getApplicationContext()
                        .getSystemService(NOTIFICATION_SERVICE);
                nMgr.cancelAll();
            }
            mEditorUserInfo.putBoolean(CommonConfig.WILL_SHOW_NOTIFICATION, false);
            mEditorUserInfo.commit();

            //Toast.makeText(getContext(),"before close",Toast.LENGTH_SHORT).show();


        }

        /*((CheckBox) rootView.findViewById(R.id.settings_getnotification)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SKToastMessage.showMessage(getActivity(), getResources().getString(R.string.resource_coming_soon_eng), SKToastMessage.ERROR);

                //passwordField.setInputType(!isChecked ? InputType.TYPE_TEXT_VARIATION_PASSWORD : InputType.TYPE_CLASS_TEXT);
                //passwordField.setTransformationMethod(!isChecked ? PasswordTransformationMethod.getInstance() : null);
            }
        });*/

    }

    public void setSelectedTheme() {
        int selected_theme = sharePrefLanguageUtil.getInt(Utils.PREF_THEME, Utils.THEME_PINK);

        if (selected_theme == Utils.THEME_PINK) {
            color_pink.setChecked(true);
        } else if (selected_theme == Utils.THEME_BLUE) {
            color_blue.setChecked(true);
        } else if (selected_theme == Utils.THEME_YELLOW) {
            color_yellow.setChecked(true);
        }

    }


    public void setThemeToApp(int theme) {


        if (theme == Utils.THEME_BLUE) {
            getActivity().setTheme(R.style.AppTheme_Blue);
        } else if (theme == Utils.THEME_PINK) {
            getActivity().setTheme(R.style.AppTheme);
        } else if (theme == Utils.THEME_YELLOW) {
            getActivity().setTheme(R.style.AppTheme_Yellow);
        }


    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        SharedPreferences.Editor editor = sharePrefLanguageUtil.edit();

       /* if(buttonView.getId() == R.id.setting_color_blue){
            if(isChecked){

                editor.putInt(Utils.PREF_THEME, Utils.THEME_BLUE);
                editor.commit();
                Utils.changeToTheme(getActivity());
            }

        }else if(buttonView.getId() == R.id.setting_color_pink){
            if(isChecked){
                editor.putInt(Utils.PREF_THEME, Utils.THEME_PINK);
                editor.commit();
                Utils.changeToTheme(getActivity());
            }
        }else if(buttonView.getId() == R.id.setting_color_yellow){
            if(isChecked){
                editor.putInt(Utils.PREF_THEME, Utils.THEME_YELLOW);
                editor.commit();
                Utils.changeToTheme(getActivity());
            }
        }else {
        */


        if (isChecked) {
            if (buttonView.getId() == R.id.settings_english_language) {

                if (!hasPermission(STORAGE_READ_PERMISSION)) {

                    //if no permission, request permission
                    String[] perms = {STORAGE_READ_PERMISSION};

                    int permsRequestCode = 200;

                    requestPermissions(perms, permsRequestCode);

                } else {
                    //FOR SMK
                    StoreUtil.getInstance().saveTo("fonts", "english");
                    editor.putString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG_FONT);
                    editor.commit();

                    String languageToLoad = "eng"; // your language
                    Locale locale = new Locale(languageToLoad);
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getResources().getDisplayMetrics());


                    //FOR KSD
                    SharedPreferences.Editor fontEditor = getActivity().getSharedPreferences(Utils.PREF_SETTING, Activity.MODE_PRIVATE).edit();
                    fontEditor.putString(Utils.PREF_SETTING_LANG, "eng");
                    fontEditor.commit();

                    setEnglishFont();
                }

            } else if (buttonView.getId() == R.id.settings_mm_zawgyi_language) {
                if (!hasPermission(STORAGE_READ_PERMISSION)) {

                    //if no permission, request permission
                    String[] perms = {STORAGE_READ_PERMISSION};

                    int permsRequestCode = 200;

                    requestPermissions(perms, permsRequestCode);

                } else {
                    StoreUtil.getInstance().saveTo("fonts", "zawgyione");
                    editor.putString(Utils.PREF_SETTING_LANG, Utils.MM_ZAWGYI_LANG_FONT);
                    editor.commit();

                    String languageToLoad = "mm"; // your language
                    Locale locale = new Locale(languageToLoad);
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getActivity().getBaseContext().getResources().updateConfiguration(config, getResources().getDisplayMetrics());

                    SharedPreferences.Editor fontEditor = getActivity().getSharedPreferences(Utils.PREF_SETTING, Activity.MODE_PRIVATE).edit();
                    fontEditor.putString(Utils.PREF_SETTING_LANG, "mm");
                    fontEditor.commit();

                    setMyanmarFont();
                }

            } else if (buttonView.getId() == R.id.settings_mm_unicode_language) {
                if (!hasPermission(STORAGE_READ_PERMISSION)) {

                    //if no permission, request permission
                    String[] perms = {STORAGE_READ_PERMISSION};

                    int permsRequestCode = 200;

                    requestPermissions(perms, permsRequestCode);

                } else {
                    StoreUtil.getInstance().saveTo("fonts", "myanmar3");
                    editor.putString(Utils.PREF_SETTING_LANG, Utils.MM_LANG_UNI);
                    editor.commit();

                    String languageToLoad = "mm"; // your language
                    Locale locale = new Locale(languageToLoad);
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getActivity().getBaseContext().getResources().updateConfiguration(config, getResources().getDisplayMetrics());

                   /* SharedPreferences.Editor fontEditor = getActivity().getSharedPreferences(Utils.PREF_SETTING, Activity.MODE_PRIVATE).edit();
                    fontEditor.putString(Utils.PREF_SETTING_LANG, "mm");
                    fontEditor.commit();*/

                    setMyanmarFontUni();
                }

            } else if (buttonView.getId() == R.id.settings_mm_default_language) {
                if (!hasPermission(STORAGE_READ_PERMISSION)) {

                    //if no permission, request permission
                    String[] perms = {STORAGE_READ_PERMISSION};

                    int permsRequestCode = 200;

                    requestPermissions(perms, permsRequestCode);

                } else {
                    StoreUtil.getInstance().saveTo("fonts", "default");
                    editor.putString(Utils.PREF_SETTING_LANG, Utils.MM_DEFAULT_FONT);
                    editor.commit();

                    String languageToLoad = "mm"; // your language
                    Locale locale = new Locale(languageToLoad);
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getActivity().getBaseContext().getResources().updateConfiguration(config, getResources().getDisplayMetrics());

                    /*SharedPreferences.Editor fontEditor = getActivity().getSharedPreferences(Utils.PREF_SETTING, Activity.MODE_PRIVATE).edit();
                    fontEditor.putString(Utils.PREF_SETTING_LANG, "mm");
                    fontEditor.commit();*/

                    setMyanmarFontDefault();
                }

            } else if (buttonView.getId() == R.id.settings_ethic_shan_language) {
                SKToastMessage.showMessage(getActivity(), getResources().getString(R.string.resource_coming_soon_eng), SKToastMessage.ERROR);

            } else if (buttonView.getId() == R.id.settings_ethic_mon_language) {
                SKToastMessage.showMessage(getActivity(), getResources().getString(R.string.resource_coming_soon_eng), SKToastMessage.ERROR);

            }


            editor.commit();
        }
    }

    public void setEnglishFont() {

        // Set title bar
        ((SettingActivity) getActivity()).textViewTitle.setText(R.string.action_settings);
        settings_language_setting_title.setText(R.string.title_action_settings_eng);
        sw_noti.setText(R.string.title_notrification_eng);

        //((SettingActivity) getActivity()).textViewTitle.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));
        settings_language_setting_title.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));
        sw_noti.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));
        settings_changeTheme.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));

    }

    public void setMyanmarFont() {
        // Set title bar
        ((SettingActivity) getActivity()).textViewTitle.setText(R.string.action_settings_mm);
        settings_language_setting_title.setText(R.string.title_action_settings_mm);
        sw_noti.setText(R.string.title_notrification_mm);

        //((SettingActivity) getActivity()).textViewTitle.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));
        sw_noti.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));

        /*((SettingActivity) getActivity()).textViewTitle.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));
        settings_language_setting_title.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));
        sw_noti.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));
        settings_changeTheme.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));*/


    }

    public void setMyanmarFontUni() {
        // Set title bar
        ((SettingActivity) getActivity()).textViewTitle.setText(R.string.action_settings_mm);

        sw_noti.setText(FontConverter.zg12uni51(getResources().getString(R.string.title_notrification_mm)));

        settings_language_setting_title.setText(R.string.title_action_settings_mm);


        //((SettingActivity) getActivity()).textViewTitle.setTypeface(MyTypeFace.get(mContext, MyTypeFace.UNI));
        sw_noti.setTypeface(MyTypeFace.get(mContext, MyTypeFace.UNI));
        /*((SettingActivity) getActivity()).textViewTitle.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));
        settings_language_setting_title.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));
        sw_noti.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));
        settings_changeTheme.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));*/


    }

    public void setMyanmarFontDefault() {
        // Set title bar
        ((SettingActivity) getActivity()).textViewTitle.setText(R.string.action_settings_mm);
        settings_language_setting_title.setText(R.string.title_action_settings_mm);
        sw_noti.setText(R.string.title_notrification_mm);

        /*((SettingActivity) getActivity()).textViewTitle.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));
        settings_language_setting_title.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));
        sw_noti.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));
        settings_changeTheme.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));*/


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);


        inflater.inflate(R.menu.menu_post_news, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {

            return false;

        }

        return super.onOptionsItemSelected(item);
    }

    //For permission of File access Storage
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case 200:

                storagePermissionAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                if (storagePermissionAccepted) {
                    //chooseImage();
                }

                break;

        }
    }

    private boolean hasPermission(String permission) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            return (getActivity().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
        } else {
            return true;
        }


    }

    /*private void unregister() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }

                    // Unregister from GCM
                    gcm.unregister();

                    // Remove Registration ID from memory
                    removeRegistrationId(context);

                    // Disable Push Notification
                    pubnub.disablePushNotificationsOnChannel("your channel name", regId);

                } catch (Exception e) { }
                return null;
            }
        }.execute(null, null, null);
    }

    private void removeRegistrationId(Context context) throws Exception {
        final SharedPreferences prefs = getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(PROPERTY_REG_ID);
        editor.apply();
    }*/

}
