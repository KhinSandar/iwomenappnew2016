package org.undp_iwomen.iwomen.manager;

import android.app.Application;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.alexbbb.uploadservice.UploadService;
import com.facebook.FacebookSdk;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;
import com.path.android.jobqueue.log.CustomLogger;

import org.undp_iwomen.iwomen.R;


/**
 * Created by khinsandar on 4/12/15.
 */
public class MainApplication extends MultiDexApplication {
    private static MainApplication instance;
    private JobManager jobManager;

    public MainApplication() {
        instance = this;
    }

    private Tracker mTracker;

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());

        UploadService.NAMESPACE = "org.undp_iwomen.iwomen";

        configureJobManager();
    }

    private void configureJobManager() {
        Configuration configuration = new Configuration.Builder(this)
                .customLogger(new CustomLogger() {
                    private static final String TAG = "JOBS";

                    public boolean isDebugEnabled() {
                        return true;
                    }

                    public void d(String text, Object... args) {
                        Log.d(TAG, String.format(text, args));
                    }

                    public void e(Throwable t, String text, Object... args) {
                        Log.e(TAG, String.format(text, args), t);
                    }

                    public void e(String text, Object... args) {
                        Log.e(TAG, String.format(text, args));
                    }
                })
                .minConsumerCount(1)//always keep at least one consumer alive
                .maxConsumerCount(1)//up to 3 consumers at a time
                .loadFactor(1)//3 jobs per consumer
                .consumerKeepAlive(120)//wait 2 minute
                .build();
        jobManager = new JobManager(this, configuration);
    }

    public JobManager getJobManager() {
        return jobManager;
    }

    public static MainApplication getInstance() {
        if(instance == null){
            instance = new MainApplication();
        }
        return instance;
    }


}
