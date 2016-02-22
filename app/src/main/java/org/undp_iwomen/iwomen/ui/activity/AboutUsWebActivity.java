package org.undp_iwomen.iwomen.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import org.smk.iwomen.BaseActionBarActivity;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.model.MyTypeFace;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.utils.Utils;


public class AboutUsWebActivity extends BaseActionBarActivity {

    private WebView mWebView;
    SharedPreferences sharePrefLanguageUtil;

    private CustomTextView textViewTitle;
    private String strLang;
    private String mActivityName;
    private String mWeburl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().requestFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us_webview);
        sharePrefLanguageUtil = getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);
        // Makes Progress bar Visible
        this.getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);


        Intent i = getIntent();
        mActivityName = i.getStringExtra("ActivityName");
        mWeburl = i.getStringExtra("URL");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        textViewTitle = (CustomTextView) toolbar.findViewById(R.id.title_action2);


        mWebView = (WebView) findViewById(R.id.tos_webview);

        //For Responsive UI and Zoom In Out effect
        /*mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);*/

        //mWebView.setWebViewClient(new MyWebClient());
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDisplayZoomControls(true);

        mWebView.getSettings().setJavaScriptEnabled(true);

        strLang = sharePrefLanguageUtil.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);

        if (mActivityName.equalsIgnoreCase("DrawerMainActivity")) {

            mWebView.loadUrl(mWeburl);
            /*if (strLang.equals(Utils.ENG_LANG)) {

                mWebView.loadUrl("file:///android_asset/tos/About-Us-Eng.html");

            }
            //FOR ALL ZAWGYI WILL SHOW
            else {

                mWebView.loadUrl("file:///android_asset/tos/About-Us-MM.html");

            }*/

        } else {
            mWebView.loadUrl(mWeburl);
        }


        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {

                if (strLang.equals(Utils.ENG_LANG)) {
                    textViewTitle.setText(R.string.loading_eng);
                    textViewTitle.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.NORMAL));
                    //setTitle(R.string.loading_eng);


                } else {
                    textViewTitle.setText(R.string.loading_mm);
                    //textViewTitle.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));

                }

                setProgress(progress * 100); //Make the bar disappear after URL is loaded
                //setValue(progress);

                super.onProgressChanged(view, progress);
                // Return the app name after finish loading
                if (progress == 100) {
                    if (mActivityName.equalsIgnoreCase("DrawerMainActivity")) {
                        if (strLang.equals(Utils.ENG_LANG)) {
                            //setTitle(R.string.title_activity_about_us_eng);
                            textViewTitle.setText(R.string.title_activity_about_us_eng);
                            textViewTitle.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.NORMAL));

                        } else{
                            //setTitle(R.string.title_activity_about_us_mm);
                            textViewTitle.setText(R.string.title_activity_about_us_mm);
                            //textViewTitle.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));

                        }

                    }else{
                        if (strLang.equals(Utils.ENG_LANG)) {
                            //setTitle(R.string.title_activity_about_us_eng);
                            textViewTitle.setText(R.string.app_name);
                            textViewTitle.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.NORMAL));

                        } else{
                            //setTitle(R.string.title_activity_about_us_mm);
                            textViewTitle.setText(R.string.app_name_mm);
                            //textViewTitle.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));

                        }

                    }


                }
            }
        });

    }

    public void setValue(int progress) {
        //progressbar.setProgress(progress);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
