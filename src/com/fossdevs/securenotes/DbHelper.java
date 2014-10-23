package com.fossdevs.securenotes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper{
	public static String DB_NAME="secureNotes";
	public static int DB_VERSION=1;
	public static String createTablePassPhraseSql="CREATE TABLE passphrase(_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, passphrase VARCHAR(50) NOT NULL);";
	public static String createTableNotesSql="CREATE TABLE notes(_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, topic VARCHAR (50) NOT NULL, note TEXT, lastUpdated VARCHAR (20), createdOn VARCHAR (20) );";
	public DbHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(createTablePassPhraseSql);
		db.execSQL(createTableNotesSql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

}
