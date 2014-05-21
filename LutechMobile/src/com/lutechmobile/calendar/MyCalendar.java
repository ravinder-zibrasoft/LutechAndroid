package com.lutechmobile.calendar;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;

import com.lutechmobile.bean.EventListBean;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

/**
 * @author Ravindra.Prajapati
 *
 */
public class MyCalendar {
	private static MyCalendar myCalendar = null;
	private static  Context mContext = null;
	Hashtable<String, Vector<EventListBean>> hashEvent;
	public Hashtable<String, Vector<EventListBean>> getHashEvent() {
		return hashEvent;
	}
	String uriString = "content://com.android.calendar/events";
	private MyCalendar() {
		
		Log.i("INFO", "Reading content from " + uriString);
		//readContent();
	
	}

	/**
	 * @param ctx
	 * @return
	 * return the instance of  this class
	 */
	public static MyCalendar getInstance(Context ctx) {
		if (myCalendar == null) {
			mContext = ctx;
			myCalendar = new MyCalendar();

		}
		return myCalendar;
	}
    private Vector<EventListBean> tableEvent;
	/**
	 * @param currentTime
	 * read the events from calendar
	 */
	public  void readContent(long currentTime123) {
		hashEvent =new Hashtable<String, Vector<EventListBean>>();
		Calendar currentTime = Calendar.getInstance();
		currentTime.setTimeInMillis(currentTime123);
         currentTime.set(currentTime.get(Calendar.YEAR), currentTime.get(Calendar.MONTH), currentTime.get(Calendar.DAY_OF_MONTH), currentTime.get(Calendar.HOUR_OF_DAY), -1);
    //   currentTime.set(2012, 11, 15);
        long date =currentTime.getTimeInMillis();
        Log.i("starttime", currentTime.get(Calendar.DAY_OF_MONTH) + "-" + currentTime.get(Calendar.MONTH) +
				 "-" +
				 currentTime.get(Calendar.YEAR)+"-"+currentTime.get(Calendar.HOUR)+"-"+currentTime.get(Calendar.MINUTE));
        long endTime = date+(1000*60*60*24);
        currentTime.setTimeInMillis(endTime);
       /*Log.i("end", currentTime.get(Calendar.DAY_OF_MONTH) + "-" + currentTime.get(Calendar.MONTH) +
				 "-" +
				 currentTime.get(Calendar.YEAR));*/
        
        
		Uri uri = Uri.parse(uriString);
		String str[]={"title","dtstart","_id","dtend"};
		String WHERE =  "dtstart > "+date + " AND dtstart <= "+endTime +" AND allDay==0";
		tableEvent = new Vector<EventListBean>();
		Cursor cursor = mContext.getContentResolver().query(uri, str,WHERE,null, null);
		if (cursor != null && cursor.getCount() > 0) {
			
			cursor.moveToFirst();
			String columnNames[] = cursor.getColumnNames();
			String value = "";
			String colNamesString = "";
			do {
				
				value = "";
                String dd ="";
                EventListBean bean = new EventListBean();
				for (String colName : columnNames) {
				
					value = colName + " = ";
					value = cursor.getString(cursor.getColumnIndex(colName))
							+ " ||";

		
					if(colName.equalsIgnoreCase("dtstart")){
						Calendar cal = Calendar.getInstance();
						cal.setTimeInMillis(Long.parseLong(cursor.getString(cursor.getColumnIndex(colName))));
						bean.setEventTime(""+cal.get(Calendar.HOUR_OF_DAY));
						//Log.i("read contact","HOUR_OF_DAY ==  "+cal.get(Calendar.HOUR_OF_DAY));
						bean.setEventStartTime(Long.parseLong(cursor.getString(cursor.getColumnIndex(colName))));
						bean.setStartMin(cal.get(Calendar.MINUTE));
						//Log.i("read contact","hour ==  "+cal.get(Calendar.HOUR)+"minut  ==  "+cal.get(Calendar.HOUR_OF_DAY));
				    	
					}else{
						if(colName.equalsIgnoreCase("title"))
						   bean.setEventName(cursor.getString(cursor.getColumnIndex(colName)));
						else if(colName.equalsIgnoreCase("_id"))
							   bean.setEventId(cursor.getString(cursor.getColumnIndex(colName)));
						else if(colName.equalsIgnoreCase("dtend")){
							bean.setEventEndTime(Long.parseLong(cursor.getString(cursor.getColumnIndex(colName))));
							Calendar cal = Calendar.getInstance();
							cal.setTimeInMillis(Long.parseLong(cursor.getString(cursor.getColumnIndex(colName))));
							bean.setEndMin(cal.get(Calendar.MINUTE));
						}
					}
				}
				if(!hashEvent.containsKey((bean.getEventTime()))){
					tableEvent=new Vector<EventListBean>();
					
				}else{
					
					tableEvent=hashEvent.get(bean.getEventTime());
				}
				tableEvent.add(bean);
				hashEvent.put(bean.getEventTime(), tableEvent);
				//Log.e("INFO : ", value);
			} while (cursor.moveToNext());

		}

	}
	/**
	 * @param currentTime
	 * write the events from calendar
	 */
	private void writeContent(String uriString) {

		  Uri uri = Uri.parse(uriString);
		  Cursor cursor = mContext.getContentResolver().query(uri,
	                new String[] {"event_id", "title", "begin", "end", "allDay","description" },null,
	                null, "startDay ASC, startMinute ASC"); 
		//  Cursor cus = mContext.getContentResolver().q
		  if (cursor != null && cursor.getCount() > 0) {
		   cursor.moveToFirst();
		   String columnNames[] = cursor.getColumnNames();
		   String value = "";
		   String colNamesString = "";
		   do {
		    value = "";

		    for (String colName : columnNames) {
		     value += colName + " = ";
		     value += cursor.getString(cursor.getColumnIndex(colName))
		       + " ||";
		    }

		    Log.e("INFO : ", value);
		   } while (cursor.moveToNext());

		  }

		 }
	/**
	 * @param hour
	 * @return
	 * get particular events 
	 */
	public String getItem(String hour){
		//Log.i("hour", "hour ==  "+hour);
		if(hour.equals("24"))
			hour = "0";
		for (EventListBean bean : tableEvent) {
			if(bean.getEventTime().equalsIgnoreCase(hour))
				return bean.getEventName();
		}
		return null;
	}
	/**
	 * @param currentTime
	 * return EventListBean instance 
	 */
	public Object getItemBean(String hour){
		//Log.i("hour", "hour ==  "+hour);
		if(hour.equals("24"))
			hour = "0";
		for (EventListBean bean : tableEvent) {
			if(bean.getEventTime().equalsIgnoreCase(hour))
				return bean;
		}
		return null;
	}

	/**
	 * @param current
	 * @return  get event type all day
	 */
	public Vector<EventListBean> getAllDayEvents(long current){
		Vector<EventListBean> v=null;
		Calendar currentTime = Calendar.getInstance();
		currentTime.setTimeInMillis(current);
         currentTime.set(currentTime.get(Calendar.YEAR), currentTime.get(Calendar.MONTH), currentTime.get(Calendar.DAY_OF_MONTH), 0, -1);
         long date =currentTime.getTimeInMillis();
         long endTime = date+(1000*60*60*24);
     
        Uri uri = Uri.parse(uriString);
 		String WHERE =  "dtstart > "+date + " AND dtstart <= "+endTime + " AND allDay==1";
 		
 		Cursor cursor = mContext.getContentResolver().query(uri, null,WHERE,null, null);
 		if (cursor != null && cursor.getCount() > 0) {
 			v = new Vector<EventListBean>();
 			cursor.moveToFirst();
 			String columnNames[] = cursor.getColumnNames();
 			String value = "";
 			do {
 				value = "";
                EventListBean bean = new EventListBean();
 				for (String colName : columnNames) {
 					
					value = colName + " = ";
					value = cursor.getString(cursor.getColumnIndex(colName))
							+ " ||";
			//		Log.i("colName", colName+"    value ==  "+value);
					if(colName.equalsIgnoreCase("dtstart")){
						Calendar cal = Calendar.getInstance();
						cal.setTimeInMillis(Long.parseLong(cursor.getString(cursor.getColumnIndex(colName))));
						bean.setEventTime(""+cal.get(Calendar.HOUR_OF_DAY));
						
						bean.setEventStartTime(Long.parseLong(cursor.getString(cursor.getColumnIndex(colName))));
						bean.setStartMin(cal.get(Calendar.MINUTE));
						//Log.i("read contact","hour ==  "+cal.get(Calendar.HOUR)+"minut  ==  "+cal.get(Calendar.HOUR_OF_DAY));
				    	
					}else{
						if(colName.equalsIgnoreCase("title"))
						   bean.setEventName(cursor.getString(cursor.getColumnIndex(colName)));
						else if(colName.equalsIgnoreCase("_id"))
							   bean.setEventId(cursor.getString(cursor.getColumnIndex(colName)));
						else if(colName.equalsIgnoreCase("dtend")){
							bean.setEventEndTime(Long.parseLong(cursor.getString(cursor.getColumnIndex(colName))));
							Calendar cal = Calendar.getInstance();
							cal.setTimeInMillis(Long.parseLong(cursor.getString(cursor.getColumnIndex(colName))));
							bean.setEndMin(cal.get(Calendar.MINUTE));
						}
					}
				}
 				//Log.i("All day event :: ","name ==  "+bean.getEventName());
 				v.add(bean);
 			}while(cursor.moveToNext());
 		}
		return v;
	}
}
