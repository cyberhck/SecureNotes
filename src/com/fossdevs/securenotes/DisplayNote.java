package com.fossdevs.securenotes;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayNote extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		return;
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(GlobalObject.keyphrase==null || GlobalObject.password==null){
			finish();
		}
		setContentView(R.layout.activity_display_note);
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
			LinearLayout ll=(LinearLayout)findViewById(R.id.Container);
			TextView unitView=new TextView(getApplicationContext());
			unitView.setTextColor(Color.parseColor("#000000"));
			unitView.setText(title);
			unitView.setTextSize(26);
			unitView.setGravity(Gravity.CENTER);
			ll.addView(unitView);
			unitView=new TextView(getApplicationContext());
			unitView.setText(text);
			unitView.setTextColor(Color.parseColor("#000000"));
			unitView.setTextSize(20);
			ll.addView(unitView);
			TextView dummyTextView=new TextView(getApplicationContext());
			dummyTextView.setText("");
			dummyTextView.setPadding(0, 0, 0, 70);
			ll.addView(dummyTextView);
			
		}catch(Exception e){
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();			
		}

	};
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_note, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		Intent intent=getIntent();
		Bundle extra=intent.getExtras();
		String _id=extra.getString("_ID");
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		if (id == R.id.editNote){
			Bundle bundle=new Bundle();
			bundle.putString("_ID", _id);
			Intent updateNote=new Intent(getApplicationContext(),UpdateNote.class);
			updateNote.putExtras(bundle);
			startActivity(updateNote);
		}
		if (id==R.id.deleteNote){
			Intent mIntent=getIntent();
			Bundle mextra=mIntent.getExtras();
			final String m_id=mextra.getString("_ID");
			DbHelper dbhelper=new DbHelper(getApplicationContext());
			final SQLiteDatabase db=dbhelper.getReadableDatabase();
			final Cursor c=db.rawQuery("SELECT * FROM notes WHERE _id="+m_id+";", null);
			c.moveToFirst();
			String title=c.getString(c.getColumnIndex("topic"));
			try{
				AlertDialog.Builder builder = new AlertDialog.Builder(DisplayNote.this)
		        .setPositiveButton(R.string.confirmDeleteOkButton, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						db.execSQL("DELETE FROM notes WHERE _id="+m_id);
						Toast.makeText(getApplicationContext(), "Note Deleted!", Toast.LENGTH_LONG).show();
						finish();
					}
				})
		        .setTitle(title)
		        .setMessage(R.string.confirmDeleteNote)
		        .setNegativeButton(R.string.confirmDeleteNoButton, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				});

		AlertDialog alert = builder.create(); // create one

		alert.show();
				
				
				
				
				
			}catch(Exception e){
				Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
			}
		}
		return super.onOptionsItemSelected(item);
	}
}
