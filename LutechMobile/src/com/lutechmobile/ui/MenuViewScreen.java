package com.lutechmobile.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.lutechmobile.bean.EventListBean;
import com.lutechmobile.calendar.MyCalendar;
import com.lutechmobile.util.SQLiteHelper;
import com.lutechmobile.util.TimeZoneEntry;

/**
 * @author anandkumar
 *
 */
public class MenuViewScreen extends Activity implements OnClickListener {

	LinearLayout lParentAddMore;
	TextView txtCurrentTimeZone, txtCurrentTime;
	Button btnAddMore;
	private SQLiteDatabase db;
	Cursor savedTZs = null;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.settings);

		lParentAddMore = (LinearLayout) findViewById(R.id.parentMoreTimeZones);
		txtCurrentTimeZone = (TextView) findViewById(R.id.txtTimeZoneName);
		txtCurrentTime = (TextView) findViewById(R.id.txtCurrentTime);
		btnAddMore = (Button) findViewById(R.id.btnAddMore);
		btnAddMore.setOnClickListener(this);
		db = (new SQLiteHelper(this)).getWritableDatabase();
		// get Calendar instance
		Calendar now = Calendar.getInstance();

		// get current TimeZone using getTimeZone method of Calendar class
		TimeZone timeZone = now.getTimeZone();

		// display current TimeZone using getDisplayName() method of TimeZone
		// class
		System.out.println("Current TimeZone is : " + timeZone.getDisplayName());
		txtCurrentTimeZone.setText(timeZone.getDisplayName());
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
		StringBuffer currentTimeText=new StringBuffer(sdf.format(now.getTime()));
//		StringBuffer currentTimeText = new StringBuffer(""
//				+ now.get(Calendar.HOUR) + ":" + now.get(Calendar.MINUTE));
		if (now.get(now.AM_PM) == 0) {
			currentTimeText.append(" am");
		} else {
			currentTimeText.append(" pm");
		}
		txtCurrentTime.setText(currentTimeText);
		
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	protected void onResume() {
		super.onResume();
		populateListOfSavedTZs();

	}

	/**
	 * populates the list of timezones from the DB.
	 */
	private void populateListOfSavedTZs() {
		if (savedTZs != null) {
			stopManagingCursor(savedTZs);
			savedTZs.close();
		}

		savedTZs = TimeZoneEntry.getAll(db);
		try {
			// savedTZs = TimeZoneEntry.getAll(db);
			if (savedTZs.getCount() >= 3) {
				btnAddMore.setEnabled(false);
			} else {
				btnAddMore.setEnabled(true);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		startManagingCursor(savedTZs);
		if (savedTZs.getCount() == 0) {
			try {
				lParentAddMore.removeAllViews();
			} catch (Exception e) {
				e.printStackTrace();
			}
			Toast.makeText(MenuViewScreen.this,
					"Add time zones with button add more.", Toast.LENGTH_SHORT)
					.show();
		} else {
			lParentAddMore.removeAllViews();
			savedTZs.moveToFirst();
			do {

				LayoutInflater li = MenuViewScreen.this.getLayoutInflater();
				ViewGroup vg = (ViewGroup) li.inflate(R.layout.timezone_text,
						null);
				TextView txtTimeZoneName = (TextView) vg
						.findViewById(R.id.txtTimeZoneName);
				TextView txtCurrentTime = (TextView) vg
						.findViewById(R.id.txtCurrentTime);
				String tzstr = savedTZs.getString(savedTZs
						.getColumnIndex("tzid"));
				TimeZone tz = TimeZone.getTimeZone(tzstr);
				final TimeZoneEntry tzEntry = new TimeZoneEntry(tzstr);
				Time now = new Time(tzstr);
				now.setToNow();
				String datenow = now.format("%A %B %e");
				// getDate().setText(datenow);
				String timenow = now.format("%I:%M %P");
				// getTime().setText(timenow);
				txtTimeZoneName.setText(tz.getID().substring(
						tz.getID().lastIndexOf('/') + 1).replaceAll("_", " "));// (tz.getDisplayName());
				txtCurrentTime.setText(timenow);

				ImageView imgDelete = (ImageView) vg
						.findViewById(R.id.imgDelete);
				imgDelete.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						tzEntry.delete(db);
						populateListOfSavedTZs();
					}
				});

				LinearLayout.LayoutParams lp = new LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
				lp.setMargins(0, 1, 0, 1);

				lParentAddMore.addView(vg, lp);

			} while (savedTZs.moveToNext());
		}

		/*
		 * if (savedTZs.getCount() == 0 && !showedInstructions) { AlertDialog
		 * noTZs = new AlertDialog.Builder(this).create(); noTZs.setMessage(
		 * "Use Menu to add time zones and long click to remove them");
		 * noTZs.setButton("OK", new DialogInterface.OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface dialog, int which) {
		 * return; } }); showedInstructions = true; noTZs.show(); } else {
		 * adapter = new TimeZoneEntryAdapter(savedTZs);
		 * list.setAdapter(adapter); }
		 */
	}

	

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		if (v == btnAddMore) {
			Intent intent = new Intent(this, HomeScreenActity.class);
			startActivity(intent);
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();

		db.close();
	}
}
