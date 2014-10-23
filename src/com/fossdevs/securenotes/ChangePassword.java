package com.fossdevs.securenotes;

import android.support.v7.app.ActionBarActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePassword extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_password);
		DbHelper dbhelper=new DbHelper(getApplicationContext());
		final SQLiteDatabase db=dbhelper.getWritableDatabase();
		final Cursor c=db.rawQuery("SELECT * FROM passphrase;", null);
		try{
			EditText et= (EditText)findViewById(R.id.oldPassword);
			if(c.getCount()==0){
				et.setFocusable(false);
				et.setEnabled(false);
			}
			Button btn=(Button) findViewById(R.id.changePasswordBtn);
			btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					EditText oldPassword=(EditText) findViewById(R.id.oldPassword);
					EditText newPassword=(EditText) findViewById(R.id.newPassword);
					EditText confirmPassword=(EditText) findViewById(R.id.reEnterNewPassword);
					String oldPass=oldPassword.getText().toString();
					String newPass=newPassword.getText().toString();
					String reNewPass=confirmPassword.getText().toString();
					if("".equals(oldPass)){
						if(newPass.equals(reNewPass)&& !("".equals(newPass))&&c.getCount()==0){
							SHA1 sha1=new SHA1(newPass);
				            String hash=sha1.SHAHash;
				            sha1=new SHA1(hash);
				            hash=sha1.SHAHash;
				            db.execSQL("INSERT INTO passphrase(passphrase) VALUES('"+hash+"')");
				            Toast.makeText(getApplicationContext(), "Inserted", Toast.LENGTH_LONG).show();
						}
					}else{
						//check if old pass is valid.
					}
				}

			});

		}catch(Exception e){
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.change_password, menu);
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
