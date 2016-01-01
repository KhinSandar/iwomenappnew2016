package org.undp_iwomen.iwomen;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.parse.ParseAnalytics;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;
import org.undp_iwomen.iwomen.ui.activity.DrawerMainActivity;


/**
 * Created by khinsandar on 7/4/15.
 */
public class Receiver extends ParsePushBroadcastReceiver {

    private String TAG = "Push";

    @Override
    public void onPushOpen(Context context, Intent intent) {
        Log.e("Push", "Clicked");
        //To track "App Opens"
        ParseAnalytics.trackAppOpenedInBackground(intent);

        //Here is data you sent
        Log.e("Push", intent.getExtras().getString("com.parse.Data"));
        Intent i = new Intent(context, DrawerMainActivity.class);
        i.putExtras(intent.getExtras());
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        //08-12 23:50:00.379  28481-28481/org.undp_iwomen.iwomen E/Push﹕ {"alert":"Tune in for the World Series, tonight at 8pm EDT","badge":"Increment","push_hash":"943d77866248c0edb42922f529f77bd8","sound":"chime","title":"Baseball News"}

        /*{
  "alert": "Tune in for the World Series, tonight at 8pm EDT",
  "badge": "Increment",
  "sound": "chime",
  "title": "Baseball News"
}
*/
    }

    @Override
    public void onPushReceive(Context context, Intent intent) {
        Log.i(TAG, "onPushReceive triggered!");

        JSONObject pushData;
        String alert = null;
        String title = null;
        try {
            pushData = new JSONObject(intent.getStringExtra(Receiver.KEY_PUSH_DATA));
            alert = pushData.getString("alert");
            title = pushData.getString("title");
        } catch (JSONException e) {
        }

        Log.i(TAG, "alert is " + alert);
        Log.i(TAG, "title is " + title);

        Intent cIntent = new Intent(Receiver.ACTION_PUSH_OPEN);
        cIntent.putExtras(intent.getExtras());
        cIntent.setPackage(context.getPackageName());

        // WE SHOULD HANDLE DELETE AS WELL
        // BUT IT'S NOT HERE TO SIMPLIFY THINGS!

        PendingIntent pContentIntent =
                PendingIntent.getBroadcast(context, 0 /*just for testing*/, cIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(alert)
                .setContentText(title)
                .setContentIntent(pContentIntent)
                .setAutoCancel(true);


        NotificationManager myNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        myNotificationManager.notify(1, builder.build());
    }
}
