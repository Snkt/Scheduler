package com.example.scheduleralarm;

import java.util.Map;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	public static ArrayAdapter<String> arrayAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final SharedPreferences myPrefs = getApplicationContext().getSharedPreferences("myprefs", Context.MODE_PRIVATE);
		Button addBtn = (Button)findViewById(R.id.button1);
		ListView listOfAlarms = (ListView)findViewById(R.id.listofalarms);
		
		arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		arrayAdapter.notifyDataSetChanged();
		listOfAlarms.setAdapter(arrayAdapter);
		
		Map<String,?> keys = myPrefs.getAll();
		for(Map.Entry<String,?> entry : keys.entrySet()){
		            arrayAdapter.add(entry.getKey());          
		 }
		listOfAlarms.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
		        alertDialog.setTitle("Confirm Delete...");
		        alertDialog.setMessage(arrayAdapter.getItem(arg2));
		        alertDialog.setIcon(R.drawable.ic_launcher);
		        alertDialog.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog,int which) {
		            	arrayAdapter.remove(arrayAdapter.getItem(arg2));
		            dialog.cancel();
		            dialog.dismiss();
		            }
		        });
		        alertDialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
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
		        alertDialog.setTitle("Details of scheduler");
		        alertDialog.setMessage(arrayAdapter.getItem(arg2));
		        alertDialog.setIcon(R.drawable.ic_launcher);
		        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog,int which) {
		            dialog.cancel();
		            dialog.dismiss();
		            }
		        });
//		        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//		            public void onClick(DialogInterface dialog, int which) {
//		            Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
//		            dialog.cancel();
//		            dialog.dismiss();
//		            }
//		        });
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

}
