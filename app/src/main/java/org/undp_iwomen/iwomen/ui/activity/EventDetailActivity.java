package org.undp_iwomen.iwomen.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.ui.fragment.EventDetailFragment;

public class EventDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        // Set up the tool bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        if(null == savedInstanceState){
            int type = getIntent().getIntExtra(EventDetailFragment.EXTRA_EVENT_TYPE, 0);

            if(type == EventDetailFragment.TYPE_IMPLICIT){
                int month = getIntent().getIntExtra(EventDetailFragment.EXTRA_MONTH, 0);
                initFragment(EventDetailFragment.newInstance("", EventDetailFragment.TYPE_IMPLICIT, month));
            }else{
                String id = getIntent().getStringExtra(EventDetailFragment.EXTRA_ID);
                initFragment(EventDetailFragment.newInstance(id, EventDetailFragment.TYPE_USER_DEFINED, -1));
            }
        }
    }

    private void initFragment(Fragment eventDetailFragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.contentFrame, eventDetailFragment);
        transaction.commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }
}
