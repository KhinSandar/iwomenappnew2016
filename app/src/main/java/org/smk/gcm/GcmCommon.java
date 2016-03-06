package org.smk.gcm;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.JsonObject;

import org.smk.clientapi.NetworkEngine;
import org.undp_iwomen.iwomen.CommonConfig;
import org.undp_iwomen.iwomen.utils.DeviceUtil;

import java.io.IOException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class GcmCommon {
	public static final String SENDER_ID = "822483433675";
	public static final String TITLE_KEY = "title";
	public static final String MESSAGE_KEY = "message";
	public static final String IMAGE_URL = "image";
	public static final String LAUNCH_IMAGE_KEY = "launchImage";
	public static final String SHARED_PREFERENCES_NAME = GcmCommon.class
			.getPackage().toString();
	public static final String SERVER_CHECK_KEY = "is_server_registered";
	public static final String REGISTER_ID_KEY = "is_registered";
	private static SharedPreferences mSharedPreferencesUserInfo;

	public static void register(final Context context) {
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				boolean isServerRegistered = isServerRegistered(context);
				String regId = getRegisterId(context);
				
				if (!isServerRegistered || regId == null) {

					GoogleCloudMessaging gcm = GoogleCloudMessaging
							.getInstance(context);
					try {
						if (regId == null) {
							regId = gcm.register(SENDER_ID);
							Log.i("iWomen","Hello Gcm ID: "+ regId);
							if (regId != null && regId.length() > 0) {
								setRegisterId(context, regId);
								mSharedPreferencesUserInfo = context.getSharedPreferences(CommonConfig.SHARE_PREFERENCE_USER_INFO, Context.MODE_PRIVATE);
								NetworkEngine.getInstance()
										.postRegisterNotification(regId, 
												DeviceUtil.getInstance((Activity) context).getID(), mSharedPreferencesUserInfo.getString(CommonConfig.USER_OBJ_ID, null),
												new Callback<JsonObject>() {

													public void success(
															JsonObject obj,
															Response res) {
														setServerRegistered(context);
													}

													public void failure(
															RetrofitError err) {
													}
												});
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return null;
			}
		}.execute(null, null, null);
	}

	protected static boolean isServerRegistered(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
		return sharedPreferences.getBoolean(SERVER_CHECK_KEY, false);
	}

	protected static void setServerRegistered(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
		sharedPreferences.edit().putBoolean(SERVER_CHECK_KEY, true).commit();
	}

	protected static String getRegisterId(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
		return sharedPreferences.getString(REGISTER_ID_KEY, null);
	}

	protected static void setRegisterId(Context context, String regId) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
		sharedPreferences.edit().putString(REGISTER_ID_KEY, regId).commit();
	}
}
