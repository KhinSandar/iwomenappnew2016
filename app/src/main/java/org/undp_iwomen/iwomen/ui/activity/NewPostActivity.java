package org.undp_iwomen.iwomen.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import org.smk.iwomen.BaseActionBarActivity;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.ui.fragment.NewIWomenPostFragment;
import org.undp_iwomen.iwomen.ui.fragment.NewPostFragment;

public class NewPostActivity extends BaseActionBarActivity {

    private String categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            categoryId = bundle.getString("categoryId");
        }

        if(null == savedInstanceState){
            if(bundle != null && categoryId != null)
                initFragment(NewPostFragment.newInstance(categoryId));
            else
                initFragment(NewIWomenPostFragment.newInstance());
        }
    }

    private void initFragment(Fragment newPostFragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.contentFrame, newPostFragment);
        transaction.commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

}
