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
		Intent intent1 = new Intent(context,TimeToRing.class);
		intent1.putExtra("title", intent.getExtras().getString("title"));
		intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent1);
		
	}

}
