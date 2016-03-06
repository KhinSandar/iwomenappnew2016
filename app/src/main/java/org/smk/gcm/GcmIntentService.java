package org.smk.gcm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.support.v7.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;

import org.smk.iwomen.GcmNotificationDialogActivity;
import org.smk.model.GcmMessage;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.ui.activity.DrawerMainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;


public class GcmIntentService extends IntentService {

	public GcmIntentService() {
		super(GcmCommon.SENDER_ID);
	}
	
	/**
	 * Push notification with message
	 * @param context
	 * @param message
	 */
	protected void generateNotification(Context context, Intent message) {
		if(message != null){
			int icon = R.drawable.ic_launcher;
			long when = System.currentTimeMillis();
			NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
			GcmMessage gcmMessage = new Gson().fromJson(message.getExtras().getString(GcmCommon.MESSAGE_KEY), GcmMessage.class);
			Log.i("iWomen","Gcm Message: "+ message.getExtras().getString(GcmCommon.MESSAGE_KEY));
			Intent notificationIntent = new Intent(context, DrawerMainActivity.class).putExtra("gcm_message", new Gson().toJson(gcmMessage));

			notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			
			NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
			builder.setSmallIcon(icon);
			builder.setContentTitle(gcmMessage.getTitle());
			builder.setStyle(new NotificationCompat.BigTextStyle().bigText(gcmMessage.getMessage()));
			builder.setContentText(gcmMessage.getMessage());
			builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
			builder.setAutoCancel(true);
			builder.setWhen(when);
			builder.addAction(R.drawable.ic_launcher, "i-Women", intent);
			if(gcmMessage.getImage() != null && gcmMessage.getMessage().length() > 0){
				Bitmap bmURL=getBitmapFromURL(gcmMessage.getImage().replace(" ", "%20"));
				if(bmURL!=null){
					float multiplier= getImageFactor(getResources());
					bmURL=Bitmap.createScaledBitmap(bmURL, (int)(bmURL.getWidth()*multiplier), (int)(bmURL.getHeight()*multiplier), false);
					builder.setLargeIcon(bmURL);
				}
			}
			builder.setContentIntent(intent);
			  
			Random random = new Random();
			int m = random.nextInt(9999 - 1000) + 1000;
			notificationManager.notify(m, builder.build());

			Intent popuIntent = new Intent(context, GcmNotificationDialogActivity.class).putExtra("gcm_message", new Gson().toJson(gcmMessage));;
			popuIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			this.getApplicationContext().startActivity(popuIntent);
		}
	}
	
	public Bitmap getBitmapFromURL(String strURL) {
	    try {
	        URL url = new URL(strURL);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setDoInput(true);
	        connection.connect();
	        InputStream input = connection.getInputStream();
	        Bitmap myBitmap = BitmapFactory.decodeStream(input);
	        return myBitmap;
	    } catch (IOException e) {
	        return null;
	    }
	}
	
	public static float getImageFactor(Resources r){
		try {
			DisplayMetrics metrics = r.getDisplayMetrics();
		    float multiplier=metrics.density/3f;
		    return multiplier;
			
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
	      
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		String messageType = gcm.getMessageType(intent);
		//Check if message type is message
		if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
			//Send a notification with message from server
			generateNotification(this, intent);	
		}
	}

}