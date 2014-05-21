package com.lutechmobile.ui;

import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Vector;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lutechmobile.bean.EventListBean;
import com.lutechmobile.bean.TimeListBean;
import com.lutechmobile.bean.TimeZoneCalendarBean;
import com.lutechmobile.calendar.MyCalendar;
import com.lutechmobile.util.Constants;
import com.lutechmobile.util.SQLiteHelper;
import com.lutechmobile.util.TimeZoneEntry;
import com.lutechmobile.util.TimezoneUtil;
import com.lutechmobile.view.EventView;

/**
 * @author Ravindra.Prajapati
 * 
 * 
 */
public class CategoryViewScreen extends Activity implements Constants,
		OnClickListener {
	private String arrMonth[] = null;
	private int numberOfCityDisplay = 0;
	private int event_TextViewW = 0;
	private int cityTextW = 0;

	private static final int DATE_DIALOG_ID = 999;

	private int year;
	private int month;
	private int day;
	private int hour;

	private TextView txtDate;
	private ImageView btnSetting, btnRefresh;
	Vector<TimeListBean> vctDateDisplay = null;
	private LinearLayout scrView = null;
	private LinearLayout lrEnentCity = null;
	private SQLiteDatabase db;
	private Vector<TimeZoneCalendarBean> vctCalendarList = null;
	private Vector<Vector> dateFormat;
    private EventView eventView;
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_timezone);
		Calendar now = Calendar.getInstance();
		year = now.get(Calendar.YEAR);
		month = now.get(Calendar.MONTH);
		day = now.get(Calendar.DATE);
		selectedYear = year;
		selectedMonth = month;
		selectedDay = day;
		boolean isExist = TimezoneUtil.checkDataBase(this);
		Log.i("DBError", "isExist ==  "+isExist);
		if(!isExist){
			String DB_PATH = "/data/data/"+ getApplicationContext().getPackageName()+ "/databases";
			try {
				TimezoneUtil.copyDataBase(this, DB_PATH, Constants.DB_Name);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.i("DBError", "database error ==  "+e.getMessage());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i("on resume", "**************");

		db = (new SQLiteHelper(this).getWritableDatabase());

		arrMonth = getResources().getStringArray(R.array.monthText);
		txtDate = (TextView) findViewById(R.id.txtDate);

		btnSetting = (ImageView) findViewById(R.id.btnSetting);
		btnSetting.setOnClickListener(this);
		btnRefresh = (ImageView) findViewById(R.id.btnRefresh);
		btnRefresh.setOnClickListener(this);
		
		Calendar now = Calendar.getInstance();

		//Log.i("onResume", " currentTime ==   "+now.getTimeInMillis());
		if(year != now.get(Calendar.YEAR) || month != now.get(Calendar.MONTH) || day != now.get(Calendar.DATE)){
		    
		     now.set(Calendar.YEAR, year);
		     now.set(Calendar.MONTH, month);
		     now.set(Calendar.DATE, day);
		     now.set(Calendar.HOUR_OF_DAY, 0);
		     now.set(Calendar.MINUTE, 0);
		    // Log.i("onResume", " change ==   "+now.getTimeInMillis());
		
		}
		ShowCalendar(now.getTimeInMillis());
		txtDate.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				// Log.i("Timezone", "clicked");
				removeDialog(DATE_DIALOG_ID);
				showDialog(DATE_DIALOG_ID);

			}
		});

	}

	/**
	 * @param now
	 *            show the events and added city and
	 */
	private void ShowCalendar(long timeNow) {
		if (scrView == null)
			scrView = (LinearLayout) findViewById(R.id.lnrlist);
		scrView.removeAllViews();
		Calendar now = Calendar.getInstance();
		now.setTimeInMillis(timeNow);
		setCitiesCalander(now);
		MyCalendar.getInstance(getApplicationContext()).readContent(now.getTimeInMillis());

		Vector<EventListBean> vAllDayEvents=MyCalendar.getInstance(getApplicationContext()).getAllDayEvents(now.getTimeInMillis());
		((LinearLayout) findViewById(R.id.linearAllDayEvent)).removeAllViews();
		if(vAllDayEvents!=null && vAllDayEvents.size()>0){
			for (final EventListBean eventListBean : vAllDayEvents) {
				TextView txtAll=new TextView(this);
				txtAll.setTextColor(Color.WHITE);
				txtAll.setPadding(2, 2, 2, 2);
				txtAll.setTextSize(TimezoneUtil.pxTosp(12, this));
				txtAll.setEllipsize(TruncateAt.END);
				txtAll.setSingleLine(true);
				//txtAll.setMaxLines(1);
				txtAll.setBackgroundColor(Color.GRAY);
				android.widget.LinearLayout.LayoutParams lp=new android.widget.LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
				lp.setMargins(0, 0, 0, 1);
				txtAll.setLayoutParams(lp);
				txtAll.setText(eventListBean.getEventName());
				txtAll.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						editCalendarEvent(eventListBean);
					}
				});
				((LinearLayout) findViewById(R.id.linearAllDayEvent)).addView(txtAll);
			}
		}
		
		Vector<TimeZoneCalendarBean> vctCitiesCalendar = getCitiesCalander();
		setCityOnscreen();
		setCityTimeList();
		txtDate.setText(day + " " + arrMonth[month] + " " + year);
		if (lrEnentCity == null)
			lrEnentCity = (LinearLayout) findViewById(R.id.linearEventCity);
		lrEnentCity.removeAllViews();

		for (int i = 0; i < vctCitiesCalendar.size() + 1; i++) {
			TextView text = null;

			if (i == 0) {// ///// for event

				text = TimezoneUtil.getTextView(this, event_TextViewW,
						TimezoneUtil.convertPxtoDip(CEll_H, this),
						R.drawable.eventcitybg);
				text.setText("Event");

			} else {
				text = TimezoneUtil.getTextView(this, cityTextW,
						TimezoneUtil.convertPxtoDip(CEll_H, this),
						R.drawable.eventcitybg);
				TimeZoneCalendarBean timeZoneCalendarBean = vctCitiesCalendar
						.elementAt(i - 1);
				String cityName = timeZoneCalendarBean.getCityName();
				cityName = cityName.replaceAll("_", " ");
				text.setText(cityName);
			}
			text.setTextColor(android.graphics.Color.WHITE);
			lrEnentCity.addView(text);
		}

		Vector<Vector> vctCityHours = getCityTimeList();

		String currentTimeZoneTimeOffset = "";

		final View event = getLayoutInflater().inflate(R.layout.event_view,
				null);
		
		if(eventView !=  null){
			eventView.destroyDrawingCache();
			eventView.invalidate();
			
		}
		eventView = null;
		eventView = (EventView) event.findViewById(R.id.eventview);
	//eventView.invalidate();
		

		Vector vctBundule = new Vector();
		vctBundule.add(cityTextW);
		vctBundule.add(MyCalendar.getInstance(getApplicationContext())
				.getHashEvent());
		vctBundule.add(vctCityHours.elementAt(0));// // for current country
		eventView.setResource(vctBundule,this);

		event.setLayoutParams(new LayoutParams(event_TextViewW, cityTextW
				* time_Hours_ResId.length));
		scrView.addView(event);

		for (int i = 0; i < vctCitiesCalendar.size(); i++) {

			final View child = getLayoutInflater().inflate(R.layout.event_time,
					null);
			LinearLayout lr = (LinearLayout) child
					.findViewById(R.id.lnrlistevevnts);
			Vector<TimeListBean> vctDateToShowFormat = null;
			boolean isHalfSelNeededInFirst = false;
			boolean isHalfSelNeededInLast = false;
			vctDateToShowFormat = vctCityHours.elementAt(i);
			TimeZoneCalendarBean timeZoneCalendarBean = vctCitiesCalendar
					.elementAt(i);
			isHalfSelNeededInFirst = timeZoneCalendarBean
					.isHalfCellAddedFirst();
			isHalfSelNeededInLast = timeZoneCalendarBean.isHalfCellAddedLast();

			int lenght = time_Hours_ResId.length;
			for (int j = 0; j < lenght; j++) {

				TimeListBean bean = null;
				if (vctDateToShowFormat != null)
					bean = vctDateToShowFormat.elementAt(j);
				TextView text = null;

				if (isHalfSelNeededInFirst) {
					text = TimezoneUtil.getTextView(this, cityTextW,
							cityTextW / 2, bean.getBgcellId());

					isHalfSelNeededInFirst = false;
					lr.addView(text);

					text = TimezoneUtil.getTextView(this, cityTextW, cityTextW,
							bean.getBgcellId());
					text.setText(Html.fromHtml(bean.getHours()));
					lr.addView(text);
				} else {
					text = TimezoneUtil.getTextView(this, cityTextW, cityTextW,
							bean.getBgcellId());
					text.setText(Html.fromHtml(bean.getHours()));
					lr.addView(text);
					if (j == lenght - 1 && isHalfSelNeededInLast) {
						TextView text123 = TimezoneUtil.getTextView(this,
								cityTextW, cityTextW / 2, bean.getBgcellId());

						isHalfSelNeededInLast = false;
						lr.addView(text123);
					}
				}

				// }

				if (bean.getTextColor() != 0)
					text.setTextColor(bean.getTextColor());
				// }

				lr.invalidate();
			}
			scrView.addView(child);
			scrView.invalidate();
		}

	}

	/**
	 * @param now
	 *            set the calenddar of added city according to timezone
	 */
	private void setCitiesCalander(Calendar now) {
		vctCalendarList = new Vector<TimeZoneCalendarBean>();

		TimeZone currentTimeZone = now.getTimeZone();
		Log.i("currentTimeZone", "currentTimeZone  ==  "+currentTimeZone.toString());
		String currentCityGmtTime = TimezoneUtil
				.dateFormatInHoursMinut(currentTimeZone.getRawOffset());
		long curruntTimeInLong = now.getTimeInMillis();
		currentCityGmtTime = currentCityGmtTime.substring(currentCityGmtTime
				.lastIndexOf(':') + 1);

		String timeZonecityName = currentTimeZone.getID().substring(
				currentTimeZone.getID().lastIndexOf('/') + 1);

		TimeZoneCalendarBean timeZoneCalendarBean = new TimeZoneCalendarBean();
		timeZoneCalendarBean.setGMTTime(currentCityGmtTime);
		timeZoneCalendarBean.setCalendar(now);
		timeZoneCalendarBean.setCityName(timeZonecityName);
		timeZoneCalendarBean.setHalfCellAddedFirst(false);
		timeZoneCalendarBean.setHalfCellAddedLast(false);
		timeZoneCalendarBean.setTimeDiff(now.getTimeInMillis());
		vctCalendarList.add(timeZoneCalendarBean);
		Cursor cursor = TimeZoneEntry.getAll(db);
		if (cursor != null && cursor.getCount() > 0) {
			if (!cursor.isFirst())
				cursor.moveToFirst();

			do {
				int columnsIndex = cursor.getColumnIndex("tzid");
				currentTimeZone = null;
				currentTimeZone = TimeZone.getTimeZone(cursor
						.getString(columnsIndex));

				String otherCityGmtTime = TimezoneUtil
						.dateFormatInHoursMinut(currentTimeZone.getRawOffset());

				otherCityGmtTime = otherCityGmtTime.substring(otherCityGmtTime
						.lastIndexOf(':') + 1);

				now = null;
				now = Calendar.getInstance();
				now.setTimeZone(currentTimeZone);
				timeZoneCalendarBean.setTimeDiff(now.getTimeInMillis());
				if (datePickerListener != null) {
					if (year != selectedYear || month != selectedMonth
							|| day != selectedDay) {

						now.set(year, month, day, 0, 0);
						now.setTimeInMillis(now.getTimeInMillis()
								+ (curruntTimeInLong - now.getTimeInMillis()));
						Log.i("tagg", "hi  ");

					}
				}

				timeZonecityName = currentTimeZone.getID().substring(
						currentTimeZone.getID().lastIndexOf('/') + 1);
				timeZoneCalendarBean = new TimeZoneCalendarBean();
				timeZoneCalendarBean.setGMTTime(currentCityGmtTime);
				timeZoneCalendarBean.setCalendar(now);
				timeZoneCalendarBean.setCityName(timeZonecityName);

				if (!currentCityGmtTime.equalsIgnoreCase(otherCityGmtTime)) {
					timeZoneCalendarBean.setHalfCellAddedFirst(true);
					timeZoneCalendarBean.setHalfCellAddedLast(false);
				}

				vctCalendarList.add(timeZoneCalendarBean);

				cursor.moveToNext();

			} while (!cursor.isAfterLast());
			cursor.close();
		}
		boolean isHalfCellAddedLast = false;
		for (TimeZoneCalendarBean tbean : vctCalendarList) {
			if (tbean.isHalfCellAddedFirst()) {
				isHalfCellAddedLast = true;
				break;
			}

		}
		if (isHalfCellAddedLast) {
			for (int i = 0; i < vctCalendarList.size(); i++) {
				TimeZoneCalendarBean tbean = vctCalendarList.elementAt(i);
				if (!tbean.isHalfCellAddedFirst()) {
					tbean.setHalfCellAddedLast(true);
					vctCalendarList.set(i, tbean);
				}
			}

		}
	}

	/**
	 * @return get the list of cities calendar
	 */
	private Vector<TimeZoneCalendarBean> getCitiesCalander() {
		return vctCalendarList;
	}

	/**
	 * set the 24 hour time format of Added city
	 */
	private void setCityTimeList() {
		String weekDay[] = getResources().getStringArray(R.array.weekday);
		dateFormat = new Vector<Vector>();
		int hrs = 0;
		int lenth = time_Hours_ResId.length;
		boolean isContinue = false;
		vctDateDisplay = new Vector<TimeListBean>();
		Vector<TimeZoneCalendarBean> vctCitiesCalendar = getCitiesCalander();

		for (int i = 0; i < vctCitiesCalendar.size(); i++) {
			TimeZoneCalendarBean timeZoneCalendarBean = vctCitiesCalendar
					.elementAt(i);
			Calendar cal = timeZoneCalendarBean.getCalendar();
			int cityCurrentHour = cal.get(Calendar.HOUR_OF_DAY);

			isContinue = false;
			vctDateDisplay = new Vector<TimeListBean>();
			int start = cityCurrentHour;
			lenth = time_Hours_ResId.length;

			for (hrs = start; hrs <= lenth; hrs++) {

				TimeListBean bean = new TimeListBean();
				if (hrs >= 0 && hrs <= 9) {
					bean.setBgcellId(R.drawable.darkbluecell);
					bean.setTextColor(Color.WHITE);
				} else if (hrs >= 10 && hrs <= 17) {
					bean.setBgcellId(R.drawable.skybluecell);

				} else if (hrs >= 18 && hrs <= 24)
					bean.setBgcellId(R.drawable.orangecell);
				else
					bean.setBgcellId(R.drawable.whitecell);

				bean.setEventTime(hrs);

				bean.setHours(getCellText(hrs,
						weekDay[cal.get(Calendar.DAY_OF_WEEK) - 1],
						cal.get(Calendar.AM_PM)));

				bean.setCalendar(cal);

				// Log.i("onclick", "hour ==  " + cal.getTimeInMillis());
				bean.setCalendarTimeInMillisecond(cal.getTimeInMillis());
				cal.setTimeInMillis(cal.getTimeInMillis() + (1000 * 60 * 60));
				// Log.i("onclick","hour ==  "+bean.getEventTime()+"  year == "+cal.get(Calendar.YEAR)+"  Month  == "+cal.get(Calendar.DAY_OF_MONTH)+" Hour ==  "+cal.get(Calendar.HOUR)+"  minut  == "+cal.get(Calendar.MINUTE));
				vctDateDisplay.add(bean);
				if (hrs == (lenth)) {
					if (!isContinue) {
						hrs = 0;
						lenth = start;
						isContinue = true;
					} else {
						break;
					}
				}
			}
			dateFormat.add(vctDateDisplay);
		}

	}

	/**
	 * @return get the 24 hour time format of Added city
	 */
	private Vector<Vector> getCityTimeList() {
		return dateFormat;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPrepareDialog(int, android.app.Dialog)
	 */
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		// TODO Auto-generated method stub
		super.onPrepareDialog(id, dialog);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			// set date picker as current date
			// Log.i("onCreateDialog", "year == " + year + "  month == " + month
			// + "  day  ==  " + day);
			return new DatePickerDialog(this, datePickerListener, year, month,
					day);

		}

		return null;
	}

	/**
	 * date picker dialog for date change
	 */
	static int selectedYear;
	static int selectedMonth;
	static int selectedDay;
	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.app.DatePickerDialog.OnDateSetListener#onDateSet(android.
		 * widget.DatePicker, int, int, int)
		 */
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			final Calendar now = Calendar.getInstance();
			now.set(selectedYear, selectedMonth, selectedDay, 0, 0);
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;

			txtDate.setText(day + " " + arrMonth[month] + " " + year);

			// TODO Auto-generated method stub
			ShowCalendar(now.getTimeInMillis());

		}

	};

	/**
	 * @return return the current date
	 */
	private String getCuttentDate() {
		String strfoirmatedDate = TimezoneUtil.dateFormat(System
				.currentTimeMillis());
		String date[] = strfoirmatedDate.split("-");
		int monthIndex = Integer.parseInt(date[1]);
		return date[0] + "-" + arrMonth[monthIndex - 1] + "-" + date[2];
	}

	/**
	 * calculate number of city on screen
	 */
	private void setCityOnscreen() {
		Cursor curser = TimeZoneEntry.getAll(db);
		int screenWOneThirdPart = TimezoneUtil.getScreenWidth(this) / 4;
		int totalWidthOfCityCell = screenWOneThirdPart * 3;
		cityTextW = totalWidthOfCityCell / NUMBER_OF_CITY;
		event_TextViewW = screenWOneThirdPart
				+ (totalWidthOfCityCell - (cityTextW * (curser.getCount() + 1)));

	}

	/**
	 * @return get number of city
	 */
	private int getCityNumberOnScreen() {
		return NUMBER_OF_CITY;
	}

	/**
	 * @return return city textview width
	 */
	private int getCityTextWidth() {
		return event_TextViewW;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {

		if (v == btnSetting) {
			startActivity(new Intent(this, MenuViewScreen.class));
		} else if (v == btnRefresh) {
			Calendar now = Calendar.getInstance();
			year = now.get(Calendar.YEAR);
			month = now.get(Calendar.MONTH);
			day = now.get(Calendar.DATE);
			ShowCalendar(now.getTimeInMillis());

		} else if (v instanceof TextView) {
			TextView textEvent = (TextView) v;
			if (textEvent.getText().length() == 0) {
				try {
					long getTagTime = (Long) textEvent.getTag();
					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis(getTagTime);
					cal.set(Calendar.YEAR, cal.get(Calendar.YEAR));
					cal.set(Calendar.MONTH, cal.get(Calendar.MONTH));
					cal.set(Calendar.DATE, cal.get(Calendar.DATE));
					cal.set(Calendar.HOUR, cal.get(Calendar.HOUR));
					cal.set(Calendar.MINUTE, 0);
					cal.set(Calendar.SECOND, 0);

					Intent intent = new Intent(Intent.ACTION_EDIT);
					intent.setType("vnd.android.cursor.item/event");
					intent.putExtra("beginTime", cal.getTimeInMillis());
					intent.putExtra("endTime",
							cal.getTimeInMillis() + 1000 * 60 * 60);

					this.startActivity(intent);
				} catch (Exception e) {
					Toast.makeText(
							this,
							"No suitable application found to handle this intent",
							Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			} else {
				EventListBean bean = (EventListBean) textEvent.getTag();
				editCalendarEvent(bean);

			}

		}

	}

	/**
	 * @param bean
	 */
	/**
	 * @param bean
	 *            edit the calendar event
	 */
	public void editCalendarEvent(EventListBean bean) {
		// Context context = getContext();

		Intent intent = new Intent(Intent.ACTION_VIEW);
		// intent.setType("vnd.android.cursor.item/event");
		intent.setData(Uri.parse("content://com.android.calendar/events/"
				+ String.valueOf(bean.getEventId())));
		intent.putExtra("beginTime", bean.getEventStartTime());
		intent.putExtra("endTime", bean.getEventEndTime());
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NO_HISTORY
				| Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		db.close();
	}

	
	/**
	 * @param hours
	 * @param day
	 * @param AM_PMP
	 * @return get formatted text display in hour cell
	 */
	private String getCellText(int hours, String day, int AM_PMP) {
		String AM_PM = "AM";
		if (AM_PMP == 0)
			AM_PM = "AM";
		else
			AM_PM = "PM";
		if (hours != 0) {
			if (hours >= 12) {
				// AM_PM="PM";
			}
			if (hours > 12) {
				hours = hours - 12;
			}
		} else {
			hours = 12;
		}

		String text = new String("<b>" + hours + " <font size='15'>" + AM_PM
				+ "</font></b><br />" + day);
		return text;
	}
   public void addNewEvent(int cellID){
	   Vector<TimeListBean> vctDateToShowFormat = dateFormat.elementAt(0);
	   TimeListBean bean = vctDateToShowFormat.elementAt(cellID);
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(bean.getCalendarTimeInMillisecond());
		cal.set(Calendar.YEAR, cal.get(Calendar.YEAR));
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH));
		cal.set(Calendar.DATE, cal.get(Calendar.DATE));
		cal.set(Calendar.HOUR, cal.get(Calendar.HOUR));
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);

		Intent intent = new Intent(Intent.ACTION_EDIT);
		intent.setType("vnd.android.cursor.item/event");
		intent.putExtra("beginTime", cal.getTimeInMillis());
		intent.putExtra("endTime",
				cal.getTimeInMillis() + 1000 * 60 * 60);

		this.startActivity(intent);
   }
	
   @Override
public void onConfigurationChanged(Configuration newConfig) {
	// TODO Auto-generated method stub
	onResume();
   }
}
