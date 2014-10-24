package com.fossdevs.securenotes;

import java.util.Calendar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateNote extends ActionBarActivity {
	@Override
	protected void onPause() {
		android.os.Process.killProcess(android.os.Process.myPid());
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_note);
		try{
			AdView av=(AdView)findViewById(R.id.adView);
			AdRequest ar=new AdRequest.Builder().build();
			av.loadAd(ar);
		}catch (Exception e){
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
		}
		try{
			Intent intent=getIntent();
			Bundle extra=intent.getExtras();
			String _id=extra.getString("_ID");
			DbHelper dbhelper=new DbHelper(getApplicationContext());
			SQLiteDatabase db=dbhelper.getReadableDatabase();
			final Cursor c=db.rawQuery("SELECT * FROM notes WHERE _id="+_id+";", null);
			c.moveToFirst();
			Crypt crypt=new Crypt();
			String cypher=c.getString(c.getColumnIndex("note"));
			String text=crypt.decrypt(cypher, GlobalObject.password);
			String title=c.getString(c.getColumnIndex("topic"));
			EditText noteBody=(EditText) findViewById(R.id.newNoteBody);
			EditText noteTitle=(EditText) findViewById(R.id.newNoteTitle);
			noteBody.setText(text);
			noteTitle.setText(title);
		}catch(Exception e){
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();			
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.update_note, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		if (id==R.id.action_update_note){
			try{
				Intent intent=getIntent();
				Bundle extra=intent.getExtras();
				String _id=extra.getString("_ID");
				Crypt crypt=new Crypt();
				EditText title=(EditText) findViewById(R.id.newNoteTitle);
				EditText body=(EditText) findViewById(R.id.newNoteBody);
				String cryptedBody=crypt.encrypt(body.getText().toString(), GlobalObject.password);
				Toast.makeText(getApplicationContext(), "Note Updated!", Toast.LENGTH_LONG).show();
				//String cryptedTitle=crypt.encrypt(title.getText().toString(), GlobalObject.password);
				//Insert into database.
				DbHelper dbhelper=new DbHelper(getApplicationContext());
				SQLiteDatabase db=dbhelper.getWritableDatabase();
				Calendar calendar=Calendar.getInstance();
				String Date=calendar.get(Calendar.DATE)+":"+calendar.get(Calendar.MONTH)+":"+calendar.get(Calendar.YEAR);
				String Time=calendar.get(Calendar.HOUR)+":"+calendar.get(Calendar.MINUTE)+":"+calendar.get(Calendar.SECOND);
				String DateTime=Date+"-"+Time;
				db.execSQL("UPDATE notes SET topic='"+title.getText().toString()+"', note='"+cryptedBody+"', lastUpdated='"+DateTime+"' WHERE _id="+_id+";");
				finish();				
			}catch(Exception e){
				Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
