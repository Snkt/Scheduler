package com.example.scheduleralarm;

import java.util.Calendar;
import java.util.Map;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class AddAlarm extends Activity{
	public static SharedPreferences myPrefs;
	public static Editor addIntoPrefs;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addalarm);
		
		final Button saveButton = (Button)findViewById(R.id.savebtn);
		final EditText title = (EditText)findViewById(R.id.title);
		final EditText message = (EditText)findViewById(R.id.message);
		final TimePicker setTime = (TimePicker)findViewById(R.id.timePicker1);
		setTime.setIs24HourView(true);
		
		final SharedPreferences myPrefs = getApplicationContext().getSharedPreferences("myprefs", Context.MODE_PRIVATE);
		addIntoPrefs = myPrefs.edit();
		
		
		
		saveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Boolean isPresent = false;
				if(title.getText().toString()==null || title.getText().toString().equals("")){
					Toast.makeText(AddAlarm.this, "Please add title.", Toast.LENGTH_SHORT).show();
				}else if(message.getText().toString()==null || message.getText().toString().equals("")){
					Toast.makeText(AddAlarm.this, "Please add message.", Toast.LENGTH_SHORT).show();
				}else{
					Map<String,?> keys = myPrefs.getAll();
					for(Map.Entry<String,?> entry : keys.entrySet()){
					           if(title.getText().toString().equalsIgnoreCase(entry.getKey())){
					        	   isPresent=true;
					        	   break;
					           }
					 }
					if(isPresent){
						Toast.makeText(AddAlarm.this, "Same title already exists, Try some different title.", Toast.LENGTH_LONG).show();
					}else{
						int hr = setTime.getCurrentHour();
			//			Toast.makeText(AddAlarm.this, ""+hr, Toast.LENGTH_SHORT).show();
						if(hr>=12){
							hr=hr-12;
						}
			//			Toast.makeText(AddAlarm.this, ""+hr, Toast.LENGTH_SHORT).show();
						int min = setTime.getCurrentMinute();
						String titlestr = title.getText().toString();
						String messagestr = message.getText().toString();
						MainActivity.arrayAdapter.add(titlestr);
						addIntoPrefs.putString(titlestr,titlestr+"?"+hr+"?"+min+"?"+messagestr);
						addIntoPrefs.commit();
	//					Toast.makeText(AddAlarm.this, "Reminder is set ", Toast.LENGTH_LONG).show();
						
						Calendar cal = Calendar.getInstance();
						cal.add(Calendar.HOUR_OF_DAY, hr);
						cal.add(Calendar.MINUTE, min);
	//					Toast.makeText(AddAlarm.this,""+(Calendar.getInstance().getTimeInMillis()-cal.getTimeInMillis())+"hr :"+Calendar.getInstance().HOUR_OF_DAY+" "+cal.HOUR_OF_DAY, Toast.LENGTH_SHORT).show();
			              Intent intent = new Intent(AddAlarm.this, TestService.class);
			      
			              PendingIntent pintent = PendingIntent.getBroadcast(AddAlarm.this, 0, intent, 0);
			             
			              AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			              //for 24 hr timeinmillies = 24*60*60*1000
			              alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pintent);
//			              alarm.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pintent);
						
						AddAlarm.this.finish();
					}
					
				}
			}
		});
		
		
		
	}

}
