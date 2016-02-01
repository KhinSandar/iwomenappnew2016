package org.undp_iwomen.iwomen.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.smk.iwomen.BaseActionBarActivity;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.model.MyTypeFace;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.utils.StoreUtil;
import org.undp_iwomen.iwomen.utils.Utils;

import java.util.Locale;


public class MainLoginActivity extends BaseActionBarActivity implements View.OnClickListener {

    private RadioGroup radioGroup;
    SharedPreferences sharePrefLanguageUtil;

    private ImageView appLogo;
    private EditText usernameField;
    private EditText passwordField;
    private Button parseLoginButton;
    private Button parseSignupButton;
    private CheckBox chkShowPwd;

    private CustomTextView txtChangLanEng, txtChangLanMM;
    private Button btnOk;
    private RadioButton rd_lang_en, rd_lang_mm_zawgyi, rd_lang_mm_uni, rd_lang_mm_default;
    private String lang;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_fragment);
        sharePrefLanguageUtil = getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);

        appLogo = (ImageView) findViewById(R.id.app_logo);
        usernameField = (EditText) findViewById(R.id.login_username_input);
        passwordField = (EditText) findViewById(R.id.login_password_input);
        chkShowPwd = (CheckBox) findViewById(R.id.login_showpwd);

        parseLoginButton = (Button) findViewById(R.id.parse_login_button);
        parseSignupButton = (Button) findViewById(R.id.parse_signup_button);

        txtChangLanEng = (CustomTextView) findViewById(R.id.login_change_lan_eng);
        txtChangLanMM = (CustomTextView) findViewById(R.id.login_change_lan_mm);


        ShowLangDialog();

        txtChangLanEng.setOnClickListener(this);
        txtChangLanMM.setOnClickListener(this);
        parseSignupButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
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
            @Override
            public void onClick(DialogPlus dialog, View view) {

                SharedPreferences.Editor editor = sharePrefLanguageUtil.edit();


                switch (view.getId()) {
                    case R.id.header_container:
                        //Toast.makeText(MainLoginActivity.this, "Header clicked", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.dialog_english_language:

                        StoreUtil.getInstance().saveTo("fonts", "english");

                        editor.putString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);
                        editor.commit();

                        SharedPreferences.Editor fontEditor = getSharedPreferences("mLanguage", Activity.MODE_PRIVATE).edit();
                        fontEditor.putString("lang", "eng");
                        fontEditor.commit();

                        setEnglishFont();
                        break;
                    case R.id.dialog_mm_zawgyi_language:


                        StoreUtil.getInstance().saveTo("fonts", "zawgyione");
                        editor.putString(Utils.PREF_SETTING_LANG, Utils.MM_LANG);
                        editor.commit();

                        String languageToLoad = "mm"; // your language
                        Locale locale = new Locale(languageToLoad);
                        Locale.setDefault(locale);
                        Configuration config = new Configuration();
                        config.locale = locale;
                        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

                        SharedPreferences.Editor fontEditorMM = getSharedPreferences("mLanguage", Activity.MODE_PRIVATE).edit();
                        fontEditorMM.putString("lang", "mm");
                        fontEditorMM.commit();

                        setMyanmarFontZawgyi();
                        break;
                    case R.id.dialog_mm_unicode_language:

                        StoreUtil.getInstance().saveTo("fonts", "myanmar3");
                        editor.putString(Utils.PREF_SETTING_LANG, Utils.MM_LANG_UNI);
                        editor.commit();

                        String languageToLoaduni = "mm"; // your language
                        Locale localeuni = new Locale(languageToLoaduni);
                        Locale.setDefault(localeuni);
                        Configuration configuni = new Configuration();
                        configuni.locale = localeuni;
                        getBaseContext().getResources().updateConfiguration(configuni, getBaseContext().getResources().getDisplayMetrics());

                        SharedPreferences.Editor fontEditorUni = getSharedPreferences("mLanguage", Activity.MODE_PRIVATE).edit();
                        fontEditorUni.putString("lang", "mm");
                        fontEditorUni.commit();

                        setMyanmarFontUni();
                        break;


                    case R.id.dialog_mm_default_language:

                        StoreUtil.getInstance().saveTo("fonts", "default");
                        editor.putString(Utils.PREF_SETTING_LANG, Utils.MM_LANG_DEFAULT);
                        editor.commit();

                        String languageToLoadDe = "mm"; // your language
                        Locale localeDe = new Locale(languageToLoadDe);
                        Locale.setDefault(localeDe);
                        Configuration configDe = new Configuration();
                        configDe.locale = localeDe;
                        getBaseContext().getResources().updateConfiguration(configDe, getBaseContext().getResources().getDisplayMetrics());

                        SharedPreferences.Editor fontEditorDe = getSharedPreferences("mLanguage", Activity.MODE_PRIVATE).edit();
                        fontEditorDe.putString("lang", "mm");
                        fontEditorDe.commit();

                        setMyanmarFontDefault();

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
        rd_lang_en = (RadioButton) view.findViewById(R.id.dialog_english_language);
        rd_lang_mm_zawgyi = (RadioButton) view.findViewById(R.id.dialog_mm_zawgyi_language);
        rd_lang_mm_uni = (RadioButton) view.findViewById(R.id.dialog_mm_unicode_language);
        rd_lang_mm_default = (RadioButton) view.findViewById(R.id.dialog_mm_default_language);
        if (lang.equals(Utils.ENG_LANG)) {
            rd_lang_en.setChecked(true);
            setEnglishFont();
        } else if (lang.equals(Utils.MM_LANG)) {
            rd_lang_mm_zawgyi.setChecked(true);
            setMyanmarFontZawgyi();
        } else if (lang.equals(Utils.MM_LANG_UNI)) {
            rd_lang_mm_uni.setChecked(true);
            setMyanmarFontUni();
        } else if (lang.equals(Utils.MM_LANG_DEFAULT)) {
            rd_lang_mm_default.setChecked(true);
            setMyanmarFontDefault();
        }


    }

    public void setEnglishFont() {
        appLogo.setImageResource(com.parse.ui.R.drawable.logo_eng);
        usernameField.setHint(getResources().getString(com.parse.ui.R.string.com_parse_ui_name_input_hint));
        passwordField.setHint(getResources().getString(com.parse.ui.R.string.com_parse_ui_password_input_hint));

        parseLoginButton.setText(getResources().getString(com.parse.ui.R.string.com_parse_ui_parse_login_button_label));
        parseSignupButton.setText(getResources().getString(com.parse.ui.R.string.com_parse_ui_create_account_button_label));

        chkShowPwd.setText(getResources().getString(com.parse.ui.R.string.com_parse_ui_show_pwd_eng));
        chkShowPwd.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.NORMAL));


        txtChangLanEng.setText(getResources().getString(R.string.login_language_eng));
        txtChangLanMM.setText(getResources().getString(R.string.login_language_mm));

    }

    public void setMyanmarFontUni() {
        appLogo.setImageResource(com.parse.ui.R.drawable.logo_mm);
        usernameField.setHint(getResources().getString(com.parse.ui.R.string.com_parse_ui_username_input_hint_mm));
        passwordField.setHint(getResources().getString(com.parse.ui.R.string.com_parse_ui_password_input_hint_mm));
        parseLoginButton.setText(getResources().getString(com.parse.ui.R.string.com_parse_ui_parse_login_button_label_mm));

        parseSignupButton.setText(getResources().getString(com.parse.ui.R.string.com_parse_ui_parse_signup_button_label_mm));

        chkShowPwd.setText(getResources().getString(com.parse.ui.R.string.com_parse_ui_show_pwd_mm));
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
        appLogo.setImageResource(com.parse.ui.R.drawable.logo_mm);
        usernameField.setHint(getResources().getString(com.parse.ui.R.string.com_parse_ui_username_input_hint_mm));
        passwordField.setHint(getResources().getString(com.parse.ui.R.string.com_parse_ui_password_input_hint_mm));
        parseLoginButton.setText(getResources().getString(com.parse.ui.R.string.com_parse_ui_parse_login_button_label_mm));

        parseSignupButton.setText(getResources().getString(com.parse.ui.R.string.com_parse_ui_parse_signup_button_label_mm));
        chkShowPwd.setText(getResources().getString(com.parse.ui.R.string.com_parse_ui_show_pwd_mm));


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
        appLogo.setImageResource(com.parse.ui.R.drawable.logo_mm);
        usernameField.setHint(getResources().getString(com.parse.ui.R.string.com_parse_ui_username_input_hint_mm));
        passwordField.setHint(getResources().getString(com.parse.ui.R.string.com_parse_ui_password_input_hint_mm));
        parseLoginButton.setText(getResources().getString(com.parse.ui.R.string.com_parse_ui_parse_login_button_label_mm));

        parseSignupButton.setText(getResources().getString(com.parse.ui.R.string.com_parse_ui_parse_signup_button_label_mm));
        chkShowPwd.setText(getResources().getString(com.parse.ui.R.string.com_parse_ui_show_pwd_mm));


        txtChangLanEng.setText(getResources().getString(R.string.login_language_eng));
        txtChangLanMM.setText(getResources().getString(R.string.login_language_mm));


    }


}