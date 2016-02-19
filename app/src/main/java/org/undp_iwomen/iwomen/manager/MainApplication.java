package org.undp_iwomen.iwomen.manager;

import android.app.Application;
import android.util.Log;

import com.alexbbb.uploadservice.UploadService;
import com.facebook.FacebookSdk;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;
import com.path.android.jobqueue.log.CustomLogger;


/**
 * Created by khinsandar on 4/12/15.
 */
public class MainApplication extends Application {
    private static MainApplication instance;
    private JobManager jobManager;
    public MainApplication() {
        instance = this;
    }
    @Override
    public void onCreate() {
        super.onCreate();

        FacebookSdk.sdkInitialize(getApplicationContext());

        /*// Required - Initialize the Parse SDK
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, getString(R.string.parse_app_id),
                getString(R.string.parse_client_key));
        PushService.setDefaultPushCallback(this, DrawerMainActivity.class);
        PushService.startServiceIfRequired(this);
        //channel '13fb3c43 com.questmyanmar.gobus/com.questmyanmar.gobus.ui.activity.SearchActivity (server)'
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

        //You must register this ParseObject subclass before instantiating it.
        ParseObject.registerSubclass(AppVersion.class);
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(Comment.class);
        ParseObject.registerSubclass(IwomenPost.class);

        
        ParseACL defaultACL = new ParseACL();
        ParseACL.setDefaultACL(defaultACL, true);*/

        UploadService.NAMESPACE = "org.undp_iwomen.iwomen";





        /*ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });
        */

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
