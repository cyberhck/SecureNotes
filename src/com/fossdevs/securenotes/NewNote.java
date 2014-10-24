package com.fossdevs.securenotes;

import java.util.Calendar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.support.v7.app.ActionBarActivity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class NewNote extends ActionBarActivity {

	@Override
	protected void onResume() {
		super.onResume();
		if(GlobalObject.keyphrase==null || GlobalObject.password==null){
			finish();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_note);
		try{
			AdView av=(AdView)findViewById(R.id.adView);
			AdRequest ar=new AdRequest.Builder().build();
			av.loadAd(ar);
		}catch (Exception e){
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.new_note, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		if (id == R.id.action_save_note){
			try{
				Crypt crypt=new Crypt();
				EditText title=(EditText) findViewById(R.id.noteTitle);
				EditText body=(EditText) findViewById(R.id.noteBody);
				String cryptedBody=crypt.encrypt(body.getText().toString(), GlobalObject.password);
				Toast.makeText(getApplicationContext(), "Saved!", Toast.LENGTH_LONG).show();
				//String cryptedTitle=crypt.encrypt(title.getText().toString(), GlobalObject.password);
				//Insert into database.
				DbHelper dbhelper=new DbHelper(getApplicationContext());
				SQLiteDatabase db=dbhelper.getWritableDatabase();
				Calendar calendar=Calendar.getInstance();
				String Date=calendar.get(Calendar.DATE)+":"+calendar.get(Calendar.MONTH)+":"+calendar.get(Calendar.YEAR);
				String Time=calendar.get(Calendar.HOUR)+":"+calendar.get(Calendar.MINUTE)+":"+calendar.get(Calendar.SECOND);
				String DateTime=Date+"-"+Time;
				db.execSQL("INSERT INTO notes(topic,note,lastUpdated,createdOn) VALUES ('"+title.getText().toString()+"' , '"+cryptedBody+"' , '"+DateTime+"' , '"+DateTime+"')");
				finish();
				
			}catch(Exception e){
				Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
			}
		}
		return super.onOptionsItemSelected(item);
	}
}
