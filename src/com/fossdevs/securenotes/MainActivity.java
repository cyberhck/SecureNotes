package com.fossdevs.securenotes;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            DbHelper dbhelper=new DbHelper(getApplicationContext());
            SQLiteDatabase db=dbhelper.getWritableDatabase();
            Cursor c=db.rawQuery("SELECT * FROM notes;", null);
            if(c.getCount()==0){
            	c=db.rawQuery("SELECT * FROM passphrase;",null);
            	if(c.getCount()==0){
            		//set password here.
            		Intent intent=new Intent(getApplicationContext(),ChangePassword.class);
            		startActivity(intent);
            	}else{
                	setContentView(R.layout.activity_main_empty_notes);
            	}
            }else{
            	setContentView(R.layout.activity_main);
            }
        }catch(Exception e){
        	Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
        return super.onOptionsItemSelected(item);
    }
}
