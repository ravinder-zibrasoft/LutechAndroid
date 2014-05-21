package com.lutechmobile.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import android.app.Activity;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.lutechmobile.activity.R;
import com.lutechmobile.util.SQLiteHelper;
import com.lutechmobile.util.TimeZoneCitiEntry;
import com.lutechmobile.util.TimeZoneEntry;

/**
 * @author anandkumar
 * 
 */
public class HomeScreenActity extends Activity {
	String tzids[] = null;
	List<TimeZoneEntry> timezones = new ArrayList<TimeZoneEntry>();
	// backup copy
	List<TimeZoneEntry> timezonesorig = new ArrayList<TimeZoneEntry>();
	ArrayAdapter<TimeZoneEntry> adapter = null;

	ListView tzlist = null;
	EditText tzfilter = null;

	TimeZoneEntry current = null;
	SQLiteDatabase db = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.time_zone_list);

		db = (new SQLiteHelper(this)).getWritableDatabase();
		current = new TimeZoneEntry();

		tzlist = (ListView) findViewById(R.id.tzlist);
		// tzlist.setTextFilterEnabled(true);
		tzlist.setOnItemClickListener(onListClick);
		adapter = new ArrayAdapter<TimeZoneEntry>(this,
				android.R.layout.simple_list_item_1, timezones);
		tzlist.setAdapter(adapter);

		timezonesorig = new ArrayList<TimeZoneEntry>();
		tzids = TimeZone.getAvailableIDs();
    	for (int i = 0; i < tzids.length; i++) {
			TimeZoneEntry tze = new TimeZoneEntry(tzids[i]);
			timezonesorig.add(tze);
		}

		tzfilter = (EditText) findViewById(R.id.tzsearch);
		tzfilter.addTextChangedListener(new FilterWatcher());

		populateListOfAllTZs();

		/************** Add TimeZone from Cities Text File **********/
		Cursor curser = TimeZoneCitiEntry.getAll(db);
		
		Log.i("AddTimeZoneActivity","Cursor == "+curser+"  count == "+curser.getCount());
		if (curser == null || curser.getCount() == 0) {
			AssetManager assetstManager = getAssets();
			try {
				InputStream is = assetstManager.open("cities.txt");
				InputStreamReader inr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(inr);
				String temp1 = null;
				while ((temp1 = br.readLine()) != null) {
					// String temp1 = br.readLine();

					if (temp1 != null) {
						String str[] = temp1.split("\",\"");
						String mainCity = str[0].substring(1).trim();
						String subCity = str[1].trim();
						String CityInfo[] = str[2].replaceAll("\"", "").split(
								",");
						String country = CityInfo[0].trim();
						String standardTime = CityInfo[3].trim();

						Log.i("AddTimeZoneActivity", "mainCity ==  " + mainCity
								+ "   subCity  ==  " + subCity
								+ "  country  ==  " + country
								+ "  standardTime == " + standardTime);
						TimeZoneCitiEntry timeZoneCitiEntry = new TimeZoneCitiEntry(
								mainCity, subCity, country, standardTime);
						timeZoneCitiEntry.save(db);
					}

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/************************ END *********************************/

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();

		db.close();
	}

	/**
	 * add all the timezones in the list
	 */
	private void populateListOfAllTZs() {
		adapter.clear();

		for (int i = 0; i < tzids.length; i++) {
			TimeZoneEntry tze = new TimeZoneEntry(tzids[i]);
			adapter.add(tze);
		}

	}

	/**
	 * it handles the onclick of adapter item.
	 */
	private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			current = timezones.get(position);
			Cursor c = TimeZoneEntry.getAll(db);
			Log.i("total count", "******** " + c.getCount());
			if (current.alreadySaved(db)) {
				Toast.makeText(HomeScreenActity.this,
						current.getId() + " already added", Toast.LENGTH_SHORT)
						.show();
			} else if (c.getCount() >= 3) {
				Toast.makeText(HomeScreenActity.this,
						" You can not add more than 3 timezones.",
						Toast.LENGTH_SHORT).show();
			} else {
				current.save(db);
				Toast.makeText(HomeScreenActity.this, "Added " + current.getId(),
						Toast.LENGTH_SHORT).show();
				HomeScreenActity.this.finish();
			}
			try {
				c.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	};

	/**
	 * @author anandkumar filters the list of timezones
	 */
	class FilterWatcher implements TextWatcher {
		public void afterTextChanged(Editable s) {

		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			adapter.clear();
			Iterator<TimeZoneEntry> tzi = timezonesorig.iterator();
			while (tzi.hasNext()) {
				TimeZoneEntry tze = tzi.next();
				if (tze.toString().toUpperCase()
						.contains(s.toString().toUpperCase())) {
					adapter.add(tze);
				}
			}

		}
	}

}