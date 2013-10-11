package com.example.scheduleralarm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class alarmTime extends Activity {
	Editor appPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timetowakeup);

		ImageView imgWheel = (ImageView) findViewById(R.id.imageView1);
		
		

//		imgWheel.setBackgroundResource(R.drawable.image_background_animation);
		AnimationDrawable frameAnimation = (AnimationDrawable) imgWheel
				.getBackground();
		frameAnimation.start();
		
		TextView ttl = (TextView) findViewById(R.id.ttl);
		TextView msg = (TextView) findViewById(R.id.msg);
		Button cancelBtn = (Button) findViewById(R.id.cancelbtn);

		final SharedPreferences myPrefs = getApplicationContext()
				.getSharedPreferences("myprefs", Context.MODE_PRIVATE);
		final String title = getIntent().getExtras().getString("title");
		appPreferences = myPrefs.edit();
		String messageStr = myPrefs.getString(title, "");

		if (!messageStr.equals("")) {

			ttl.setText(title);
			msg.setText(messageStr.split(",")[3]);
			
			Uri alert = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_ALARM);
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

			final MediaPlayer mediaPlayer = MediaPlayer.create(
					getBaseContext(), alert);

			final Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			long[] pattern = { 0, 400, 800 };

			mediaPlayer.start();
			vib.vibrate(pattern, 0);

			cancelBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (mediaPlayer.isPlaying()) {
						mediaPlayer.stop();
						mediaPlayer.release();
					}

					vib.cancel();
					finish();
				}
			});
			
		}else{
			Toast.makeText(this, "Some error occoured", Toast.LENGTH_SHORT).show();
			finish();
		}
	}

}
