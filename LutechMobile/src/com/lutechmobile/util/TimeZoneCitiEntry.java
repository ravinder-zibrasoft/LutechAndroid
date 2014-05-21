package com.lutechmobile.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TimeZoneCitiEntry {
	private String mainCity = "";
	private String subCity = "";
	private String country = "";
	private String statndardTime="";

	/**
	 * constructor
	 */
	public TimeZoneCitiEntry() {
		
	}
	
	/**
	 * @param id
	 * constructor with id
	 */
	public TimeZoneCitiEntry(String mainCity,String subCity,String country,String statndardTime) {
		this.mainCity = mainCity;
		this.subCity = subCity;
		this.country = country;
		this.statndardTime=statndardTime;
		
	}

	/**
	 * @return id of timezone
	 */
	public String getId() {
		return mainCity;
	}

	/**
	 * @param id
	 * sets the id of timezone
	 */
	public void setId(String id) {
		this.mainCity = id;
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
		String args[] = {mainCity};
		ContentValues cv = new ContentValues();
		
		cv.put(Constants.mainCity, mainCity);
		Cursor c = db.rawQuery("SELECT * FROM "+Constants.TIMEZONE_CITY_TABLE+" WHERE "+Constants.mainCity+"=?", args);
		
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
		
		cv.put(Constants.mainCity, mainCity);
		cv.put(Constants.subCity, subCity);
		cv.put(Constants.statndardTime, statndardTime);
		cv.put(Constants.country,country);
		db.insert(""+Constants.TIMEZONE_CITY_TABLE,Constants.mainCity, cv);
		
	}
	
	/**
	 * @param db
	 * deletes the timezone from db.
	 */
	public void delete(SQLiteDatabase db) {
		String args[] = {mainCity};
		ContentValues cv = new ContentValues();
		
		cv.put("tzid", mainCity);
		db.delete("timezones", "tzid=?", args);
	}

	/**
	 * @param db
	 * @return cursor containg all the timezone from the db
	 */
	public static Cursor getAll(SQLiteDatabase db) {
		return db.rawQuery("SELECT * FROM "+ Constants.TIMEZONE_CITY_TABLE, null);
	}
}