package org.undp_iwomen.iwomen.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import org.smk.iwomen.BaseActionBarActivity;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.model.IntroPageTransformer;
import org.undp_iwomen.iwomen.ui.adapter.IntroAdapter;

public class IntroActivity extends BaseActionBarActivity {

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.intro_layout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        // Set an Adapter on the ViewPager
        mViewPager.setAdapter(new IntroAdapter(getSupportFragmentManager()));
        // Set a PageTransformer
        mViewPager.setPageTransformer(false, new IntroPageTransformer());

    }
    
}
