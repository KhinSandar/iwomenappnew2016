package com.parse.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Point;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class DeviceUtil {
	private static DeviceUtil instance;
	private int Width;
	private int Height;
	private Display display;
	private Point size;
	private String ID = null;
	private Context context;
	private float FrameScale;
	private int FrameWidth;
	private int FrameHeight;
	private int PaddingY;
	private int PaddingX;
	
	public DeviceUtil(Activity aty){
		context = aty.getApplicationContext();
		this.display = aty.getWindowManager().getDefaultDisplay();
		this.size = getDisplaySize(display);
		setWidth(this.size.x);
		setHeight(this.size.y);
		compute(aty);
	}
	
	public static DeviceUtil getInstance(Activity mActivity) {
    	if(instance == null){
    		instance = new DeviceUtil(mActivity);
		}
		return instance;
	}
	
	private void compute(Activity mActivity){
		
		this.display = mActivity.getWindowManager().getDefaultDisplay();
		this.size = getDisplaySize(display);
		
		setWidth(this.size.x);
		setHeight(this.size.y);
		
		DisplayMetrics dm = new DisplayMetrics();
		
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		int m_nTotalW = dm.widthPixels;
		int m_nTotalH = dm.heightPixels;
		
		// Scale factor
		FrameScale = (float)m_nTotalH / 1136.0f;

		// Compute our frame
		FrameWidth = (int) (682.0f * FrameScale);
		FrameHeight = m_nTotalH;

		// Compute padding for our frame inside the total screen size
		PaddingY = 0;
		PaddingX = (m_nTotalW - FrameWidth) / 2;

		// Debug
        Log.d("Device UI", "Device UI W x H: "+m_nTotalW + "x"+m_nTotalH+ " Scale:"+FrameScale+" Frame W x H: "+FrameWidth+"x"+FrameHeight + " Padding X x Y: "+PaddingX+"x"+PaddingY);
	}
	public float getFrameScale() {
		return FrameScale;
	}
	public int getFrameWidth() {
		return FrameWidth;
	}
	public int getFrameHeight() {
		return FrameHeight;
	}
	public int getPaddingY() {
		return PaddingY;
	}
	public int getPaddingX() {
		return PaddingX;
	}
	
	public int getWidth() {
		return Width;
	}

	public void setWidth(int width) {
		Width = width;
	}

	public int getHeight() {
		return Height;
	}

	public void setHeight(int height) {
		Height = height;
	}
	
	public int getScale(int v, float scale) {
		float s = (float)v * scale; int rs = 0;
		
		if (s - (int)s >= 0.5) rs= ((int)s)+1; else rs= (int)s;
		
		return rs;
	}
	
	public int getScaleSize(int size){
		return getScale(size, getFrameScale());
	}

	@SuppressWarnings("deprecation")
	private Point getDisplaySize(final Display display) {
	    final Point point = new Point();
	    try {
	        display.getSize(point);
	    } catch (NoSuchMethodError ignore) { // Older device
	        point.x = display.getWidth();
	        point.y = display.getHeight();
	    }
	    return point;
	}
	
	// return a cached unique ID for each device
	public String getID() {
		// if the ID isn't cached inside the class itself
		if (ID == null) {
			//get it from database / settings table (implement your own method here)
			SharedPreferences pref = context.getApplicationContext().getSharedPreferences("Device", Activity.MODE_PRIVATE);
			ID = pref.getString("DeviceID", "0");
		}
 
		// if the saved value was incorrect
		if (ID.equals("0")) {
			// generate a new ID
			ID = generateID();
 
			if (ID != null) {
				// save it to database / setting (implement your own method here)
				SharedPreferences sharedPreferences = context.getSharedPreferences("Device", Activity.MODE_PRIVATE);
				SharedPreferences.Editor editor = sharedPreferences.edit();
				editor.putString("DeviceID",ID);
				editor.commit();
			}
		}
		
		return ID;
	}

	// generate a unique ID for each device
	// use available schemes if possible / generate a random signature instead 
	private String generateID() {
		// use the ANDROID_ID constant, generated at the first device boot
		String deviceId = Secure.getString(context.getContentResolver(),Secure.ANDROID_ID);
		Log.i("","Physical Device ID = "+deviceId);
		// in case known problems are occured
		if ("9774d56d682e549c".equals(deviceId) || deviceId == null) {

			// get a unique deviceID like IMEI for GSM or ESN for CDMA phones
			// don't forget:
			// <uses-permission android:name="android.permission.READ_PHONE_STATE" />
			deviceId = ((TelephonyManager) context.getSystemService( Context.TELEPHONY_SERVICE )).getDeviceId();

			// if nothing else works, generate a random number
			if (deviceId == null) {

				Random tmpRand = new Random();
				deviceId = String.valueOf(tmpRand.nextLong());
			}
			
			// any value is hashed to have consistent format
		}
		
		return getHash(deviceId);
	}

	// generates a SHA-1 hash for any string
	public static String getHash(String stringToHash) {

	 	MessageDigest digest = null;
			try {
				digest = MessageDigest.getInstance("SHA-1");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
	
	 	byte[] result = null;
	
			try {
				result = digest.digest(stringToHash.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
	
	 	StringBuilder sb = new StringBuilder();
	
	 	for (byte b : result)
	 	{
	 	    sb.append(String.format("%02X", b));
	 	}
	
	 	String messageDigest = sb.toString();
	 	return messageDigest;
	}
	
	public static boolean isTablet(Context context) {
	    return (context.getResources().getConfiguration().screenLayout
	            & Configuration.SCREENLAYOUT_SIZE_MASK)
	            >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}
}
