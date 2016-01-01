package org.undp_iwomen.iwomen.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.model.FontConverter;
import org.undp_iwomen.iwomen.model.MyTypeFace;
import org.undp_iwomen.iwomen.ui.activity.SettingActivity;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.utils.StoreUtil;
import org.undp_iwomen.iwomen.utils.Utils;

import java.util.Locale;

/**
 * Created by khinsandar on 8/7/15.
 */
public class SettingsFragment extends Fragment implements CompoundButton.OnCheckedChangeListener{

    private RadioGroup radioLanguageGroup;
    private RadioButton radioLanguageButton;

    RadioButton rd_lang_en, rd_lang_mm_zawgyi, rd_lang_mm_uni, rd_lang_mm_default;
    SharedPreferences sharePrefLanguageUtil;


    private Context mContext;
    private CheckBox chk_settings_getnotification;
    private CustomTextView settings_language_setting_title;
    private CustomTextView settings_changeTheme;
    RadioButton color_blue, color_pink, color_yellow;

    public void SettingsFragment(Context context){

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
        radioLanguageGroup = (RadioGroup) rootView.findViewById(R.id.settings_language);
        rd_lang_en = (RadioButton) rootView.findViewById(R.id.settings_english_language);
        rd_lang_mm_zawgyi = (RadioButton) rootView.findViewById(R.id.settings_mm_zawgyi_language);
        rd_lang_mm_uni = (RadioButton) rootView.findViewById(R.id.settings_mm_unicode_language);
        rd_lang_mm_default = (RadioButton) rootView.findViewById(R.id.settings_mm_default_language);
        settings_language_setting_title = (CustomTextView)rootView.findViewById(R.id.settings_language_setting_title);
        settings_changeTheme = (CustomTextView)rootView.findViewById(R.id.settings_changeTheme);
        chk_settings_getnotification = (CheckBox)rootView.findViewById(R.id.settings_getnotification);

        color_blue = (RadioButton) rootView.findViewById(R.id.setting_color_blue);
        color_pink = (RadioButton) rootView.findViewById(R.id.setting_color_pink);
        color_yellow = (RadioButton) rootView.findViewById(R.id.setting_color_yellow);

        String lang = sharePrefLanguageUtil.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);
        rd_lang_en.setOnCheckedChangeListener(this);
        rd_lang_mm_zawgyi.setOnCheckedChangeListener(this);
        rd_lang_mm_uni.setOnCheckedChangeListener(this);
        rd_lang_mm_default.setOnCheckedChangeListener(this);

        if(lang.equals(Utils.ENG_LANG)){
            rd_lang_en.setChecked(true);
            setEnglishFont();
        }
        else if(lang.equals(Utils.MM_LANG)){
            rd_lang_mm_zawgyi.setChecked(true);
            setMyanmarFont();
        }else if(lang.equals(Utils.MM_LANG_UNI)){
            rd_lang_mm_uni.setChecked(true);
            setMyanmarFontUni();
        }else if(lang.equals(Utils.MM_LANG_DEFAULT)){
            rd_lang_mm_default.setChecked(true);
            setMyanmarFontDefault();
        }

        setSelectedTheme();

    }

    public void setSelectedTheme(){
        int selected_theme = sharePrefLanguageUtil.getInt(Utils.PREF_THEME, Utils.THEME_PINK);

        if(selected_theme == Utils.THEME_PINK){
            color_pink.setChecked(true);
        }else if(selected_theme == Utils.THEME_BLUE){
            color_blue.setChecked(true);
        }else if(selected_theme == Utils.THEME_YELLOW){
            color_yellow.setChecked(true);
        }

    }


    public void setThemeToApp(int theme){


        if(theme == Utils.THEME_BLUE){
            getActivity().setTheme(R.style.AppTheme_Blue);
        }else if(theme == Utils.THEME_PINK){
            getActivity().setTheme(R.style.AppTheme);
        }else if(theme == Utils.THEME_YELLOW){
            getActivity().setTheme(R.style.AppTheme_Yellow);
        }


    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        SharedPreferences.Editor editor = sharePrefLanguageUtil.edit();

        if(buttonView.getId() == R.id.setting_color_blue){
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




            if (isChecked) {
                if (buttonView.getId() == R.id.settings_english_language) {


                    StoreUtil.getInstance().saveTo("fonts", "english");

                    editor.putString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);

                    setEnglishFont();


                    SharedPreferences.Editor fontEditor = getActivity().getSharedPreferences("mLanguage", Activity.MODE_PRIVATE).edit();
                    fontEditor.putString("lang", "eng");
                    fontEditor.commit();


                } else if (buttonView.getId() == R.id.settings_mm_zawgyi_language) {
                    StoreUtil.getInstance().saveTo("fonts", "zawgyione");
                    editor.putString(Utils.PREF_SETTING_LANG, Utils.MM_LANG);

                    String languageToLoad  = "mm"; // your language
                    Locale locale = new Locale(languageToLoad);
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());

                    SharedPreferences.Editor fontEditor = getActivity().getSharedPreferences("mLanguage", Activity.MODE_PRIVATE).edit();
                    fontEditor.putString("lang", "mm");
                    fontEditor.commit();

                    setMyanmarFont();

                }
                else if (buttonView.getId() == R.id.settings_mm_unicode_language) {
                    StoreUtil.getInstance().saveTo("fonts", "myanmar3");
                    editor.putString(Utils.PREF_SETTING_LANG, Utils.MM_LANG_UNI);

                    String languageToLoad  = "mm"; // your language
                    Locale locale = new Locale(languageToLoad);
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());

                    SharedPreferences.Editor fontEditor = getActivity().getSharedPreferences("mLanguage", Activity.MODE_PRIVATE).edit();
                    fontEditor.putString("lang", "mm");
                    fontEditor.commit();

                    setMyanmarFontUni();

                } else if (buttonView.getId() == R.id.settings_mm_default_language) {
                    StoreUtil.getInstance().saveTo("fonts", "default");
                    editor.putString(Utils.PREF_SETTING_LANG, Utils.MM_LANG_DEFAULT);

                    String languageToLoad  = "mm"; // your language
                    Locale locale = new Locale(languageToLoad);
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());

                    SharedPreferences.Editor fontEditor = getActivity().getSharedPreferences("mLanguage", Activity.MODE_PRIVATE).edit();
                    fontEditor.putString("lang", "mm");
                    fontEditor.commit();

                    setMyanmarFontDefault();

                }


            }
            editor.commit();
        }
    }

    public void setEnglishFont(){

        // Set title bar
        ((SettingActivity) getActivity()).textViewTitle.setText(R.string.action_settings);
        settings_language_setting_title.setText(R.string.title_action_settings_eng);
        chk_settings_getnotification.setText(R.string.title_notrification_eng);
        settings_changeTheme.setText(R.string.title_change_theme_eng);

        //((SettingActivity) getActivity()).textViewTitle.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));
        settings_language_setting_title.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));
        chk_settings_getnotification.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));
        settings_changeTheme.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));

    }
    public void setMyanmarFont(){
        // Set title bar
        ((SettingActivity) getActivity()).textViewTitle.setText(R.string.action_settings_mm);
        settings_language_setting_title.setText(R.string.title_action_settings_mm);
        chk_settings_getnotification.setText(R.string.title_notrification_mm);
        settings_changeTheme.setText(R.string.title_change_theme_mm);

        //((SettingActivity) getActivity()).textViewTitle.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));
        chk_settings_getnotification.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));

        /*((SettingActivity) getActivity()).textViewTitle.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));
        settings_language_setting_title.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));
        chk_settings_getnotification.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));
        settings_changeTheme.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));*/


    }public void setMyanmarFontUni(){
        // Set title bar
        ((SettingActivity) getActivity()).textViewTitle.setText(R.string.action_settings_mm);

        chk_settings_getnotification.setText(FontConverter.zg12uni51(getResources().getString(R.string.title_notrification_mm)));

        settings_language_setting_title.setText(R.string.title_action_settings_mm);
        settings_changeTheme.setText(R.string.title_change_theme_mm);


        //((SettingActivity) getActivity()).textViewTitle.setTypeface(MyTypeFace.get(mContext, MyTypeFace.UNI));
        chk_settings_getnotification.setTypeface(MyTypeFace.get(mContext, MyTypeFace.UNI));
        /*((SettingActivity) getActivity()).textViewTitle.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));
        settings_language_setting_title.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));
        chk_settings_getnotification.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));
        settings_changeTheme.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));*/


    }public void setMyanmarFontDefault(){
        // Set title bar
        ((SettingActivity) getActivity()).textViewTitle.setText(R.string.action_settings_mm);
        settings_language_setting_title.setText(R.string.title_action_settings_mm);
        chk_settings_getnotification.setText(R.string.title_notrification_mm);
        settings_changeTheme.setText(R.string.title_change_theme_mm);

        /*((SettingActivity) getActivity()).textViewTitle.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));
        settings_language_setting_title.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));
        chk_settings_getnotification.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));
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
}
