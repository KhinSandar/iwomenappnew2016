package org.smk.gcm;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class GcmBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		intent.setComponent(new ComponentName(context, GcmIntentService.class));
		context.startService(intent);
	}

}
