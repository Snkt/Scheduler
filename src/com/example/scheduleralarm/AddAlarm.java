package com.example.scheduleralarm;

import java.util.Calendar;
import java.util.Date;
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
	int pendingId;
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
		setTime.setCurrentHour(Calendar.HOUR_OF_DAY);
		setTime.setCurrentMinute(Calendar.MINUTE);
//		setTime.setOnTimeChangedListener(mStartTimeChangedListener);
		
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
						int min = setTime.getCurrentMinute();
//						Toast.makeText(AddAlarm.this, "Hr :"+hr+"Min "+min, Toast.LENGTH_SHORT).show();
						String titlestr = title.getText().toString();
						String messagestr = message.getText().toString();
						MainActivity.arrayAdapter.add(titlestr);
						MainActivity.arrayAdapter.notifyDataSetChanged();
						pendingId = myPrefs.getInt("lastPendingID", 0);
						if(pendingId==0){
							pendingId=1;
						}else{
							pendingId++;
						}
						addIntoPrefs.putInt("lastPendingID", pendingId);
						addIntoPrefs.putString(titlestr,titlestr+","+hr+","+min+","+messagestr+","+pendingId);
						addIntoPrefs.commit();
						
						Calendar cal = Calendar.getInstance();
						cal.set(Calendar.HOUR_OF_DAY, hr);
						cal.set(Calendar.MINUTE, min);
						cal.set(Calendar.SECOND, 0);
			            Intent intent = new Intent(AddAlarm.this, TestService.class);
			            intent.putExtra("title",titlestr);
			            PendingIntent pintent = PendingIntent.getBroadcast(AddAlarm.this, pendingId, intent, 0);
			            AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			            //for 24 hr timeinmillies = 24*60*60*1000
//			            Toast.makeText(AddAlarm.this, ""+(Calendar.getInstance().getTimeInMillis()-cal.getTimeInMillis()), Toast.LENGTH_SHORT).show();
			            alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pintent);
//			            Toast.makeText(AddAlarm.this, ""+titlestr+","+hr+","+min+","+messagestr+","+pendingId, Toast.LENGTH_LONG).show();
	//			        alarm.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pintent);
						AddAlarm.this.finish();
					}
					
				}
			}
		});
		
		
		
		
		
	}
	
	
	
	@Override
	protected void onStart() {
		super.onStart();
		final TimePicker setTime = (TimePicker)findViewById(R.id.timePicker1);
		setTime.setCurrentHour(Calendar.HOUR_OF_DAY);
		setTime.setCurrentMinute(Calendar.MINUTE);
	}

}
