package org.smk.iwomen;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import org.smk.model.GcmMessage;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.utils.Utils;

import java.util.Locale;

public class GcmNotificationDialogActivity extends AppCompatActivity {

    private GcmMessage gcmMessage;
    private TextView txt_title;
    private TextView txt_message;
    private Button btn_close;
    SharedPreferences sharePrefLanguageUtil;
    String mstr_lang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gcm_notification_dialog);
        sharePrefLanguageUtil = getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);

        txt_title = (TextView) findViewById(R.id.txt_gcm_title);
        txt_message = (TextView) findViewById(R.id.txt_gcm_message);
        btn_close = (Button) findViewById(R.id.btn_gcm_close);

        //TODO FONT DRAWERMAIN
        mstr_lang = sharePrefLanguageUtil.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            gcmMessage = new Gson().fromJson(bundle.getString("gcm_message"), GcmMessage.class);

            txt_title.setText(Html.fromHtml(gcmMessage.getTitle()));
            txt_message.setText(Html.fromHtml(gcmMessage.getMessage()));
        }

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //TODO FONT DRAWERMAIN
        if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.ENG_LANG)) {

            String languageToLoad = "eng"; // your language
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,getResources().getDisplayMetrics());

            btn_close.setText(getResources().getString(R.string.str_ok));
        }else{
            String languageToLoad  = "mm"; // your language
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getResources().getDisplayMetrics());

            btn_close.setText(getResources().getString(R.string.str_ok));

        }
    }

}
