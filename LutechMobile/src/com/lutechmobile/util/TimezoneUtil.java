package com.lutechmobile.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import com.lutechmobile.ui.R;
import com.lutechmobile.ui.CategoryViewScreen;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.webkit.WebSettings.TextSize;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author Ravindra.Prajapati
 *
 */
public class TimezoneUtil {

	/**
	 * @param timeinmilll
	 * @return in format dd-MM-yyyy
	 */
	public static String dateFormat(long timeinmilll) {
		Date now = new Date();
		now.setTime(timeinmilll);
		DateFormat df = new java.text.SimpleDateFormat("dd-MM-yyyy");
		return df.format(now);

	}
	/**
	 * @param timeinmilll
	 * @return in format yyyy-MM-dd HH:mm:ss
	 */
	public static String dateFormatHHMM(long timeinmilll) {   
		Date now = new Date();
		now.setTime(timeinmilll);
		DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(now);

	}
	/**
	 * @param timeinmilll
	 * @return in format hh:mm
	 */
	public static String dateFormatInHoursMinut(long timeinmilll) {

		SimpleDateFormat si = new SimpleDateFormat("hh:mm");
		String date = si.format(new Date(timeinmilll));
		return date;

	}

	/**
	 * @param pixel
	 * @param context
	 * @return dp
	 */
	public static int convertPxtoDip(int pixel, Context context) {

		float scale = context.getResources().getDisplayMetrics().density;
		int dips = (int) (pixel * scale + 0.5f);
		return dips;
	}

	/**
	 * @param value
	 * @param context
	 * @return float Converts the dip value to pixel
	 */
	public static float convertDiptoPx(int value, Context context) {
		Resources resources = context.getResources();
		float dipValue = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				value, resources.getDisplayMetrics());
		return dipValue;
	}

	/**
	 * @param activity
	 * @return screen width
	 * 
	 */
	public static int getScreenWidth(Activity activity) {
		Display display = activity.getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		return width;
	}

	/**
	 * @param activity
	 * @return int Returns the height of the screen
	 */
	public static int getScreenHeight(Activity activity) {
		Display display = activity.getWindowManager().getDefaultDisplay();
		int height = display.getHeight();
		return height;
	}

	
	/**
	 * @param ctx
	 * @param width
	 * @param height
	 * @param bgId
	 * @return   instance of textview 
	 */
	public static TextView getTextView(Activity ctx, int width, int height,
			int bgId) {
		TextView txt = new TextView(ctx);
		txt.setWidth(width);
		txt.setHeight(height);
		txt.setGravity(Gravity.CENTER);
		txt.setBackgroundResource(bgId);
		txt.setFocusable(true);
		txt.setClickable(true);
		// text.set

		return txt;
	}
	/**
	 * @param ctx
	 * @param width
	 * @param height
	 * @param bgId
	 * @return   instance of textview 
	 */
	public static TextView getTextView(Activity ctx, int width, int height,
			int bgId,boolean isFocusable,boolean isClicable,Object tagId) {
		final TextView txt = new TextView(ctx);
		txt.setWidth(width);
		txt.setHeight(height);
		txt.setGravity(Gravity.CENTER);
		txt.setBackgroundResource(bgId);
		txt.setFocusable(isFocusable);
		txt.setClickable(isClicable);
		txt.setOnClickListener((CategoryViewScreen)ctx);
		txt.setTag(tagId);
		txt.setPadding(2, 2, 2, 2);
		txt.setTextColor(Color.WHITE);
		ViewTreeObserver vto = txt.getViewTreeObserver();
	    vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

	        @Override
	        public void onGlobalLayout() {
	        ViewTreeObserver obs = txt.getViewTreeObserver();
	        obs.removeGlobalOnLayoutListener(this);
	        if(txt.getLineCount() > 2)
	        {
	            int lineEndIndex = txt.getLayout().getLineEnd(2);
	            String text = txt.getText().subSequence(0, lineEndIndex-3) +"...";
	            txt.setText(text);
	        }
	        }
	    });
		return txt;
	}
	
	public static float pxTosp(float px,Context ctx){
		DisplayMetrics metrics=ctx.getResources().getDisplayMetrics();
		 return px * metrics.scaledDensity;
		
	}
	public static  void copyDataBase(Context context , String dbPath , String dbName) throws IOException{
    	InputStream myInput = context.getAssets().open(dbName);
    	String outFileName = dbPath + dbName;
    	OutputStream myOutput = new FileOutputStream(outFileName);
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }
	/**
	 * function to check if database is already exist
	 * @return true if database is already created otherwise false
	 */
	public static  boolean checkDataBase(Context appContext){
		SQLiteDatabase checkDB = null;
		
		String DB_PATH = "/data/data/"+ appContext.getApplicationContext().getPackageName()+ "/databases/";
		try {
			String pathToDb = DB_PATH + Constants.DB_Name;
			
			if(!new File(pathToDb).exists()){
				return false;
			}
			
			checkDB = SQLiteDatabase.openDatabase(pathToDb, null,
					SQLiteDatabase.OPEN_READWRITE|SQLiteDatabase.NO_LOCALIZED_COLLATORS);

		}catch (SQLiteException e) {
			//Utility.printLog(getClass().getName(),"checkDataBase() : checking if the database already exist"+e);
			checkDB = null;
			return false;
		}finally{
			/*if(db!=null){
				if(db.isOpen()){
					
					db.close();
					db = null;
				}
			}*/
		}
		/*if (checkDB != null) {
			db = checkDB;
		}*/
		
		return checkDB != null ? true : false;
	} 
}