package com.fossdevs.securenotes;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DbHelper dbhelper=new DbHelper(getApplicationContext());
        SQLiteDatabase db=dbhelper.getWritableDatabase();
        Cursor c=db.rawQuery("SELECT * FROM passphrase;", null);
        if(c.getCount()==0){
        	Intent intent=new Intent(getApplicationContext(),ChangePassword.class);
        	startActivity(intent);
        }
        setContentView(R.layout.activity_main);
    }
    @Override
    protected void onResume() {
    	super.onResume();
    	EditText passwordField=(EditText) findViewById(R.id.password);
    	passwordField.setText("");
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public void login(View v) {
        EditText pass=(EditText) findViewById(R.id.password);
        String password=pass.getText().toString();
        SHA1 sha1=new SHA1(password);
        String hash=sha1.SHAHash;
        sha1=new SHA1(hash);
        hash=sha1.SHAHash;
        DbHelper dbhelper=new DbHelper(getApplicationContext());
        SQLiteDatabase db=dbhelper.getReadableDatabase();
        Cursor c=db.rawQuery("SELECT * FROM passphrase;", null);
        c.moveToFirst();
        String hashInTable=c.getString(c.getColumnIndex("passphrase"));
        if(hash.equals(hashInTable)){
        	Intent intent=new Intent(getApplicationContext(),ShowNotes.class);
        	GlobalObject.keyphrase=hash;
        	GlobalObject.password=password;
        	startActivity(intent);
        }else{
        	Toast.makeText(getApplicationContext(), "Password Incorrect", Toast.LENGTH_LONG).show();
        }
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
        if (id == R.id.change_password) {
            Intent intent=new Intent(getApplicationContext(),ChangePassword.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
