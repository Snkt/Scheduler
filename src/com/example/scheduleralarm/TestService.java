package com.example.scheduleralarm;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class TestService extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, Intent intent) {
		Toast.makeText(context, "Alarm done..", Toast.LENGTH_LONG).show();
	}

}
