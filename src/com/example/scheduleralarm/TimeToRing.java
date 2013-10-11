package com.example.scheduleralarm;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class TimeToRing extends Activity{
//	public int i=0,snooze_id;
//	public static String temp,store,snooze,alrm_time,store_alrmtime;
	public PendingIntent pendingIntent;
	AlarmManager alarmmanager;
//	final Timer tmr = new Timer();
//	public static Timer timer;
	Editor appPreferences;
	
	
	

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final SharedPreferences myPrefs = getApplicationContext().getSharedPreferences("myprefs", Context.MODE_PRIVATE);
		final String title = getIntent().getExtras().getString("title");
//		String pendingId = getIntent().getExtras().getString("pendingId");
		
		appPreferences  = myPrefs.edit();
		
		final int pendingId = Integer.parseInt(myPrefs.getString(title, "No message defined.").split(",")[4]);


		
		
		Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
		if (alert == null) {
			// if alert is null, using backup
			alert = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		}
		
		final Window win = getWindow();
		win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
		              | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD); 
		win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
		              | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		final MediaPlayer mediaPlayer = MediaPlayer.create(getBaseContext(),
				alert);

		final Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		long[] pattern = { 0, 400, 800 };

		mediaPlayer.start();
		v.vibrate(pattern,0);
		
		
		final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		final AlertDialog ad = alertDialog.create();
		
		alertDialog.setTitle(title);
		String messageStr = myPrefs.getString(title, "");
		String message = null;
		if(messageStr.equals("")){
			message = "No message defined.";
		}else{
			message = messageStr.split(",")[3];
		}
		alertDialog.setMessage(message);
		
		ad.setIcon(R.drawable.notification);
		alertDialog.setPositiveButton("Ok",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						if(mediaPlayer.isPlaying()){
							   mediaPlayer.stop();
								mediaPlayer.release();
						   }
						
//						tmr.cancel();
						v.cancel();
						ad.cancel();
						ad.dismiss();
						finish();
					}
				});
		
		alertDialog.show();
	
//		tmr.schedule(new TimerTask() {
//		   public void run() {
//			   if(mediaPlayer.isPlaying()){
//				   mediaPlayer.stop();
//					mediaPlayer.release();
//			   }
//					v.cancel();
//					tmr.cancel();
//					ad.cancel();
//					ad.dismiss();
//					store=temp;
//					store_alrmtime=alrm_time;
//					
//	                final String ns = Context.NOTIFICATION_SERVICE;
//	                NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
//	                
//	                //Instantiate the Notification:
//	                int icon = R.drawable.ic_launcher;
//	                CharSequence tickerText = "Alarm missed";
//	                long when = System.currentTimeMillis();
//	                
//	                Notification notification = new Notification(icon, tickerText, when);
//	                notification.defaults |= Notification.DEFAULT_SOUND;
//	                notification.defaults |= Notification.DEFAULT_VIBRATE;
//	                notification.defaults |= Notification.DEFAULT_LIGHTS;
//	                //Define the notification's message and PendingIntent:
//	                Context context = getApplicationContext();
//	                CharSequence contentTitle = "Alarm";
//	                CharSequence contentText = "There was an Alarm for "+store+" at "+store_alrmtime;
//	                Intent notificationIntent = new Intent(TimeToRing.this, MainActivity.class);
//	                PendingIntent contentIntent = PendingIntent.getActivity(TimeToRing.this, 0, notificationIntent, 0);
//	                
//	                notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
//	                
//	                notification.flags |= Notification.FLAG_AUTO_CANCEL;
//	                
//	                final int WC_ID = 1;
//	                mNotificationManager.notify(WC_ID, notification);
//	               
//	                ad.cancel();
//					ad.dismiss();
//					finish();
//		   }
//		}, 30000);
	}
}