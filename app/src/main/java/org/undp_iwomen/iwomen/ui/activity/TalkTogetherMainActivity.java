package org.undp_iwomen.iwomen.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.smk.iwomen.BaseActionBarActivity;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.model.MyTypeFace;
import org.undp_iwomen.iwomen.ui.fragment.TLGUserPostRecentFragment;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.utils.Utils;

public class TalkTogetherMainActivity extends BaseActionBarActivity {

    String strBookId, strPaymentStatus;
    private CustomTextView textViewTitle;
    private Context mContext;
    SharedPreferences sharePrefLanguageUtil;
    String strLang, mstrTitle ,mstrTileMM, mCatID;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.talk_together_main_holder);

        /*Intent i = getIntent();
        strBookId = i.getStringExtra(CommonConfig.BOOKING_ID);
        strPaymentStatus = i.getStringExtra(CommonConfig.TICKET_PAYMENT_STATUS);*/
        sharePrefLanguageUtil = getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);
        strLang = sharePrefLanguageUtil.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);

        mContext = getApplicationContext();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");

        textViewTitle = (CustomTextView) toolbar.findViewById(R.id.title_action2);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent i = getIntent();

        mstrTitle = i.getStringExtra("CategoryName");
        mstrTileMM =i.getStringExtra("CategoryNameMM");
        mCatID = i.getStringExtra("CategoryID");


        if (strLang.equals(Utils.ENG_LANG)) {
            textViewTitle.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));
            textViewTitle.setText(mstrTitle);
        } else {//FOR Default and Custom
            //textViewTitle.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));
            textViewTitle.setText(mstrTileMM);
        }
        //textViewTitle.setText(mstrTitle);

        bundle = new Bundle();
        bundle.putString("CategoryName", mstrTitle);
        bundle.putString("CategoryNameMM", mstrTileMM);
        bundle.putString("CategoryID", mCatID);


        /*if (strLang.equals(Utils.ENG_LANG)) {

            textViewTitle.setText(R.string.edit_profile_eng);

        } else //FOR ALl MM FONT
        {
            textViewTitle.setText(R.string.edit_profile_mm);

        }*/
        if (savedInstanceState == null) {
            TLGUserPostRecentFragment tlgUserPostRecentFragment = new TLGUserPostRecentFragment();
            tlgUserPostRecentFragment.setArguments(bundle);

            /*TLGUserStoriesRecentFragment tlgUserStoriesRecentFragment = new TLGUserStoriesRecentFragment();
            tlgUserStoriesRecentFragment.setArguments(bundle);*/

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, tlgUserPostRecentFragment)
                    .commit();
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

        if (item.getItemId() == android.R.id.home) {

            finish();
            return true;


        }


        return super.onOptionsItemSelected(item);
    }
}
