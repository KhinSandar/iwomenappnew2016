package org.undp_iwomen.iwomen.ui.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.smk.iwomen.BaseActionBarActivity;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.manager.MainApplication;
import org.undp_iwomen.iwomen.model.TransitionHelper;

/**
 * Created by lgvalle on 12/09/15.
 */
public class BaseDetailTransitionActivity extends BaseActionBarActivity {
    protected static final String EXTRA_SAMPLE = "sample";
    protected static final String EXTRA_TYPE = "type";
    protected static final int TYPE_PROGRAMMATICALLY = 0;
    protected static final int TYPE_XML = 1;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainApplication application = (MainApplication) getApplication();
        mTracker = application.getDefaultTracker();

        PackageManager packageManager = getPackageManager();
        try {
            ActivityInfo info = packageManager.getActivityInfo(getComponentName(), 0);
            Log.e("app", "Activity name:" + info.name);
            mTracker.setScreenName("Screen: "+ info.name);
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @SuppressWarnings("unchecked")
    protected void transitionTo(Intent i) {
        final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants(this, true);
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pairs);
        startActivity(i, transitionActivityOptions.toBundle());
    }
}
