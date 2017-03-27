package org.undp_iwomen.iwomen.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import com.google.gson.Gson;
import com.smk.model.SisterAppItem;

import org.smk.iwomen.BaseActionBarActivity;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.ui.fragment.SisterAppDetailFragment;

public class SisterAppDetailActivity extends BaseActionBarActivity {

    private SisterAppItem sisterAppItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sister_app_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //TODO id
        Intent i = getIntent();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            sisterAppItem = new Gson().fromJson(bundle.getString("SisterObj"), SisterAppItem.class);
            initFragment(SisterAppDetailFragment.newInstance(sisterAppItem));

        }

       /* if(null == savedInstanceState){
            sisterAppItem =
            String id = getIntent().getStringExtra(EventDetailFragment.EXTRA_ID);
            initFragment(SisterAppDetailFragment.newInstance(id));
        }*/
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
