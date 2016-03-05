package org.undp_iwomen.iwomen.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.ui.fragment.SisterAppDetailFragment;
import org.undp_iwomen.iwomen.ui.fragment.EventDetailFragment;

public class SisterAppDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sister_app_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if(null == savedInstanceState){
            String id = getIntent().getStringExtra(EventDetailFragment.EXTRA_ID);
            initFragment(SisterAppDetailFragment.newInstance(id));
        }
    }

    private void initFragment(Fragment sisterAppDetailFragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.contentFrame, sisterAppDetailFragment);
        transaction.commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

}
