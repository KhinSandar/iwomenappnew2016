package org.undp_iwomen.iwomen.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.ui.fragment.LeadershipTipDetailFragment;
import org.undp_iwomen.iwomen.ui.fragment.LeadershipTipsFragment;

public class LeadershipTipDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leadership_tips);

        // Set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(null == savedInstanceState){
            String tipId = getIntent().getStringExtra(LeadershipTipDetailFragment.EXTRA_ID);
            initFragment(LeadershipTipDetailFragment.newInstance(tipId));
        }
    }

    private void initFragment(Fragment leadershipTipsFragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.contentFrame, leadershipTipsFragment);
        transaction.commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }
}
