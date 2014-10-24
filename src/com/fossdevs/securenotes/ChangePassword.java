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
import android.widget.ProgressBar;
import android.widget.Toast;

public class ChangePassword extends ActionBarActivity {

	@Override
	protected void onPause() {
		android.os.Process.killProcess(android.os.Process.myPid());
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_password);
		ProgressBar pb=(ProgressBar) findViewById(R.id.progressBar1);
		pb.setVisibility(View.INVISIBLE);
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
				            Toast.makeText(getApplicationContext(), "Password Set!", Toast.LENGTH_LONG).show();
				            finish();
						}else{
							Toast.makeText(getApplicationContext(), "Please enter old password", Toast.LENGTH_LONG).show();
						}
					}else{
						//check if old pass is valid.
						SHA1 sha1=new SHA1(oldPass);
						String cypher=sha1.SHAHash;
						sha1=new SHA1(cypher);
						cypher=sha1.SHAHash;
						Cursor c=db.rawQuery("SELECT * FROM passphrase;", null);
						c.moveToFirst();
						String cypherInDb=c.getString(c.getColumnIndex("passphrase"));
						if(cypherInDb.equals(cypher)){
							if(newPass.equals(reNewPass)){
								Crypt crypt=new Crypt();
								ProgressBar pb=(ProgressBar) findViewById(R.id.progressBar1);
								pb.setVisibility(View.VISIBLE);
								int total=c.getCount();
								int current=0;
								DbHelper dhelper=new DbHelper(getApplicationContext());
								SQLiteDatabase sqldb=dhelper.getWritableDatabase();
								Cursor cu=sqldb.rawQuery("SELECT * FROM notes;",null);
								oldPass=oldPassword.getText().toString();
								while(cu.moveToNext()){
									String noteInDb=cu.getString(cu.getColumnIndex("note"));
									String originalNote=crypt.decrypt(noteInDb, oldPass);
									String newEncryptedNote=crypt.encrypt(originalNote, newPass);
									String _id=cu.getString(cu.getColumnIndex("_id"));
									sqldb.execSQL("UPDATE notes SET note='"+newEncryptedNote+"' WHERE _id="+_id+";");
									current++;
									int perc=(current/total)*100;
									pb.setProgress(perc);
								}
								sha1=new SHA1(newPass);
								String updatedPass=sha1.SHAHash;
								sha1=new SHA1(updatedPass);
								updatedPass=sha1.SHAHash;
								sqldb.execSQL("UPDATE passphrase SET passphrase='"+updatedPass+"';");
								finish();
							}else{
								Toast.makeText(getApplicationContext(), "New password mismatch", Toast.LENGTH_LONG).show();
							}
						}else{
							Toast.makeText(getApplicationContext(), "Password Incorrect!", Toast.LENGTH_LONG).show();
						}
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
