package com.example.scheduleralarm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
	public static ArrayAdapter<String> arrayAdapter;
	Editor appPreferences;
	SharedPreferences myPrefs;
	ViewHolder holder = new ViewHolder();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		myPrefs  = getApplicationContext().getSharedPreferences("myprefs", Context.MODE_PRIVATE);
		Button addBtn = (Button)findViewById(R.id.button1);
		ListView listOfAlarms = (ListView)findViewById(R.id.listofalarms);
		
		
		final ArrayList<String> list = new ArrayList<String>();
		Map<String,?> keys = myPrefs.getAll();
		appPreferences  = myPrefs.edit();
		for(Map.Entry<String,?> entry : keys.entrySet()){
				if(!entry.getKey().equals("lastPendingID")){
//		            arrayAdapter.add(entry.getKey());
		            list.add(entry.getKey());
				}
		 }

		
		arrayAdapter = new CustomArrayAdapter(this,R.layout.customlistview,R.id.custmtxtview, list);
		arrayAdapter.notifyDataSetChanged();
		listOfAlarms.setAdapter(arrayAdapter);
		
		
		listOfAlarms.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
		        alertDialog.setTitle("Confirm Delete...");
		        alertDialog.setMessage(arrayAdapter.getItem(arg2));
		        alertDialog.setIcon(R.drawable.delete);
		        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog,int which) {
		            	dialog.cancel();
			            dialog.dismiss();
		            }
		        });
		        alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		            	try{
		            		int pendingId = Integer.parseInt(myPrefs.getString(arrayAdapter.getItem(arg2), "No message defined.").split(",")[4]);
		            		Intent intent = new Intent(MainActivity.this, TestService.class);
				            PendingIntent pintent = PendingIntent.getBroadcast(MainActivity.this, pendingId, intent, 0);
				            AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
				            //for 24 hr timeinmillies = 24*60*60*1000
				            alarm.cancel(pintent);
				            Toast.makeText(MainActivity.this, "Cancelled alarm for "+pendingId, Toast.LENGTH_SHORT).show();
		            		
		            		appPreferences.remove(arrayAdapter.getItem(arg2));
		            		appPreferences.commit();
			            	arrayAdapter.remove(arrayAdapter.getItem(arg2));
			            	Toast.makeText(MainActivity.this, "Successfully removed.", Toast.LENGTH_SHORT).show();
		            	}catch(Exception e){
		            		Toast.makeText(MainActivity.this, "Some error occoured." +e.getMessage(), Toast.LENGTH_SHORT).show();
		            	}
		            dialog.cancel();
		            dialog.dismiss();
		            }
		        });
		        alertDialog.show();
				return false;
			}
		});
		
		listOfAlarms.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
		        alertDialog.setTitle("Details");
		        alertDialog.setMessage("Title :"+ arrayAdapter.getItem(arg2)+"\nMessage :"+myPrefs.getString(arrayAdapter.getItem(arg2), "No message defined.").split(",")[3]);
		        alertDialog.setIcon(R.drawable.info);
		        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog,int which) {
		            dialog.cancel();
		            dialog.dismiss();
		            }
		        });
		        alertDialog.show();
			}
		});
		addBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,AddAlarm.class);
				startActivity(intent);
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private class CustomArrayAdapter extends ArrayAdapter<String> implements OnClickListener {

	    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();
	    //Context contxt;

	    public CustomArrayAdapter(Context context,int resource ,int textViewResourceId,
	        List<String> objects) {
	    	
	      super(context, resource,textViewResourceId, objects);
	    //  contxt = context;
	      for (int i = 0; i < objects.size(); ++i) {
	        mIdMap.put(objects.get(i), i);
	      }
	    }

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.customlistview, parent, false);
			
			
			holder.editbtn = (Button)rowView.findViewById(R.id.custmeditbutton);
			holder.editbtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Toast.makeText(MainActivity.this, "btn cliked"+position, Toast.LENGTH_SHORT).show();
				}
			});
			
			return super.getView(position, rowView, parent);
		}

	    

//		@Override
//	    public long getItemId(int position) {
//	      String item = getItem(position);
//	      return mIdMap.get(item);
//	    }

//	    @Override
//	    public boolean hasStableIds() {
//	      return true;
//	    }




	  }
	
	static class ViewHolder {
        TextView name;
        Button editbtn;
        ToggleButton toggleBtn;
    }

}
