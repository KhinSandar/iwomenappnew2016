package org.undp_iwomen.iwomen.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.ui.fragment.ViewEventsFragment;

public class ViewEventsActivity extends AppCompatActivity {

    String str_date;
    int imonth;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_events);

        Intent i = getIntent();
        str_date = i.getStringExtra("Date");
        imonth = i.getIntExtra("Month", 1);


        bundle = new Bundle();
        bundle.putString("Date", str_date);
        bundle.putInt("Month", imonth);

        // Set up the actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        if(null == savedInstanceState){
            ViewEventsFragment viewEventsFragment = new ViewEventsFragment();
            viewEventsFragment.setArguments(bundle);
            initFragment(viewEventsFragment);
        }
    }

    private void initFragment(Fragment viewEventsFragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.contentFrame, viewEventsFragment);
        transaction.commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }
}
