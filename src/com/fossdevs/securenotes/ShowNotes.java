package com.fossdevs.securenotes;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class ShowNotes extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		return;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.show_notes, menu);
		return true;
	}
	@Override
	public void onResume(){
		try{
			super.onResume();
			DbHelper dbhelper=new DbHelper(getApplicationContext());
			SQLiteDatabase db=dbhelper.getReadableDatabase();
			final Cursor c=db.rawQuery("SELECT * FROM notes;", null);
			if(c.getCount()==0){
				setContentView(R.layout.activity_show_notes_empty);
			}else{
				setContentView(R.layout.activity_show_notes);
				int to[]={R.id.noteTitle};
				String[] from=new String[] {"topic"};
				ListView lv=(ListView) findViewById(R.id.listNotes);
				Adapter adapter=new SimpleCursorAdapter(getBaseContext(), R.layout.item_list_notes, c, from, to,1);
				lv.setAdapter((ListAdapter)adapter);
				lv.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View view, int pos, long arg3) {
						// TODO Auto-generated method stub
						c.moveToPosition(pos);
						String _id=c.getString(c.getColumnIndex("_id"));
						Bundle extra=new Bundle();
						extra.putString("_ID", _id);
						Intent intent=new Intent(getApplicationContext(),DisplayNote.class);
						intent.putExtras(extra);
						startActivity(intent);
					}
				});
			}
			try{
				AdView av=(AdView)findViewById(R.id.adView);
				AdRequest ar=new AdRequest.Builder().build();
				av.loadAd(ar);
			}catch (Exception e){
				Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
			}
		}catch(Exception e){
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
		}
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		if (id==R.id.newNote){
			Intent intent=new Intent(getApplicationContext(),NewNote.class);
			startActivity(intent);
		}
		
		return super.onOptionsItemSelected(item);
	}
}
