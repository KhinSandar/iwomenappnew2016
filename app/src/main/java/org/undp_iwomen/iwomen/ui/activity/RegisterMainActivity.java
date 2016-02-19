package org.undp_iwomen.iwomen.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.analytics.Tracker;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.ui.fragment.RegisterLoginFragment1;
import org.undp_iwomen.iwomen.ui.fragment.Register_Congrat_Fragment;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.utils.Utils;

public class RegisterMainActivity extends BaseDetailTransitionActivity {

    String strBookId, strPaymentStatus;
    private Tracker mTracker;
    public CustomTextView textViewTitle;
    private Context mContext;
    SharedPreferences sharePrefLanguageUtil;
    String strLang, mstrTitle;

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

        /*Intent i = getIntent();

        mstrTitle = i.getStringExtra("CategoryName");
        textViewTitle.setText(mstrTitle);*/
        if (strLang.equals(Utils.ENG_LANG)) {

            textViewTitle.setText(R.string.register_title);

        }else {//FOR ALl MM FONT{
            textViewTitle.setText(R.string.register_title);

        }


        setupWindowAnimations();


        if (savedInstanceState == null) {

            Slide slideTransition = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                slideTransition = new Slide(Gravity.LEFT);
                slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));

            }
            // Create fragment and define some of it transitions
            RegisterLoginFragment1 sharedElementFragment1 = new RegisterLoginFragment1();
            sharedElementFragment1.setReenterTransition(slideTransition);
            sharedElementFragment1.setExitTransition(slideTransition);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                sharedElementFragment1.setSharedElementEnterTransition(new ChangeBounds());
            }


            Register_Congrat_Fragment register_congrat_fragment = new Register_Congrat_Fragment();

            /*Bundle b = new Bundle();
            b.putString(CommonConfig.BOOKING_ID,strBookId);
            b.putString(CommonConfig.TICKET_PAYMENT_STATUS, strPaymentStatus);
            ticketFragment.setArguments(b);*/
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, sharedElementFragment1)
                    .commit();
        }

    }


    private void setupWindowAnimations() {
        // We are not interested in defining a new Enter Transition. Instead we change default transition duration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getEnterTransition().setDuration(getResources().getInteger(R.integer.anim_duration_long));
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
