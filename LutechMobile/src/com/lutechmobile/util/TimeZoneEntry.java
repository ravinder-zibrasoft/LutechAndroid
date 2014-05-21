package com.lutechmobile.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author anandkumar
 *
 */
public class TimeZoneEntry {
	private String id = "";

	/**
	 * constructor
	 */
	public TimeZoneEntry() {
		
	}
	
	/**
	 * @param id
	 * constructor with id
	 */
	public TimeZoneEntry(String id) {
		this.id = id;
	}

	/**
	 * @return id of timezone
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 * sets the id of timezone
	 */
	public void setId(String id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getId();
	}
	
	/**
	 * @param db
	 * @return true if timezone is already added in db or false if not
	 */
	public boolean alreadySaved(SQLiteDatabase db) {
		String args[] = {id};
		ContentValues cv = new ContentValues();
		
		cv.put("tzid", id);
		Cursor c = db.rawQuery("SELECT * FROM timezones WHERE tzid=?", args);
		
		if (c.getCount() == 0) {
			return false;
		}
		c.close();
		
		return true;
	}
	
	/**
	 * @param db
	 * saves the timezone in db
	 */
	public void save(SQLiteDatabase db) {
		ContentValues cv = new ContentValues();
		
		cv.put("tzid", id);
		db.insert("timezones", "tzid", cv);
	}
	
	/**
	 * @param db
	 * deletes the timezone from db.
	 */
	public void delete(SQLiteDatabase db) {
		String args[] = {id};
		ContentValues cv = new ContentValues();
		
		cv.put("tzid", id);
		db.delete("timezones", "tzid=?", args);
	}

	/**
	 * @param db
	 * @return cursor containg all the timezone from the db
	 */
	public static Cursor getAll(SQLiteDatabase db) {
		return db.rawQuery("SELECT * FROM timezones", null);
	}
}