package com.lutechmobile.util;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author anandkumar
 *
 */
public class SQLiteHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "timezones.db";
	private static final int SCHEMA_VERSION = 1;
	
	/**
	 * @param context
	 * constructor
	 */
	public SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, SCHEMA_VERSION);
	}
	
	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE if not exists timezones (_id INTEGER PRIMARY KEY AUTOINCREMENT, tzid TEXT);");
		db.execSQL("CREATE TABLE if not exists " + Constants.TIMEZONE_CITY_TABLE+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"+Constants.mainCity+" TEXT,"+Constants.subCity+" TEXT,"+Constants.statndardTime+" TEXT,"+Constants.country+" TEXT);");
	}
	
	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		android.util.Log.w("BookLoc", "Upgrading database, which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS timezones");
		db.execSQL("DROP TABLE IF EXISTS "+Constants.TIMEZONE_CITY_TABLE);
		onCreate(db);
		
		/* todo: make temp table, copy current data to it, fix real table's schema and then copy the data back */
	}
}

